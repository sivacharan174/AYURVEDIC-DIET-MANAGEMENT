<?php
/**
 * Simple Email Sender for AyurNutrition
 * Uses PHP's built-in mail function with SMTP configuration
 * 
 * For production use, download the full PHPMailer library from:
 * https://github.com/PHPMailer/PHPMailer/releases
 * 
 * Extract and place the 'src' folder contents into the PHPMailer directory
 */

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

require_once 'smtp_config.php';

$response = ["success" => false, "message" => ""];

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    $response['message'] = "Invalid request method";
    echo json_encode($response);
    exit;
}

// Validate required fields
if (!isset($_POST['pdf_base64']) || 
    !isset($_POST['patient_email']) || 
    !isset($_POST['patient_name']) || 
    !isset($_POST['report_type'])) {
    $response['message'] = "Missing required fields";
    echo json_encode($response);
    exit;
}

$pdfBase64 = $_POST['pdf_base64'];
$patientEmail = filter_var($_POST['patient_email'], FILTER_VALIDATE_EMAIL);
$patientName = $_POST['patient_name'];
$reportType = $_POST['report_type'];
$filename = isset($_POST['filename']) ? $_POST['filename'] : 'AyurNutrition_Report.pdf';

if (!$patientEmail) {
    $response['message'] = "Invalid email address";
    echo json_encode($response);
    exit;
}

// Check if PHPMailer exists
$phpmailerPath = __DIR__ . '/PHPMailer/PHPMailer.php';
if (!file_exists($phpmailerPath)) {
    // Provide instructions for setting up PHPMailer
    $response['message'] = "PHPMailer not installed. Please download from https://github.com/PHPMailer/PHPMailer and extract to PHPMailer folder.";
    $response['setup_required'] = true;
    echo json_encode($response);
    exit;
}

// Use PHPMailer
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

require $phpmailerPath;
require __DIR__ . '/PHPMailer/SMTP.php';
require __DIR__ . '/PHPMailer/Exception.php';

$pdfContent = base64_decode($pdfBase64);
if ($pdfContent === false) {
    $response['message'] = "Invalid PDF data";
    echo json_encode($response);
    exit;
}

try {
    $mail = new PHPMailer(true);
    $mail->isSMTP();
    $mail->Host = SMTP_HOST;
    $mail->SMTPAuth = true;
    $mail->Username = SMTP_USERNAME;
    $mail->Password = SMTP_PASSWORD;
    $mail->SMTPSecure = SMTP_SECURE;
    $mail->Port = SMTP_PORT;

    $mail->setFrom(SMTP_FROM_EMAIL, SMTP_FROM_NAME);
    $mail->addAddress($patientEmail, $patientName);
    $mail->addStringAttachment($pdfContent, $filename, 'base64', 'application/pdf');

    $mail->isHTML(true);
    $mail->Subject = "Your AyurNutrition " . $reportType . " Report";
    $mail->Body = "<h2>Dear " . htmlspecialchars($patientName) . ",</h2>" .
        "<p>Please find attached your <strong>" . htmlspecialchars($reportType) . "</strong> report.</p>" .
        "<p>Generated: " . date('F j, Y') . "</p>" .
        "<p>Best regards,<br>AyurNutrition Team</p>";
    $mail->AltBody = "Dear " . $patientName . ", Please find attached your " . $reportType . " report.";

    $mail->send();
    $response['success'] = true;
    $response['message'] = "Report sent to " . $patientEmail;
} catch (Exception $e) {
    $response['message'] = "Email failed: " . $mail->ErrorInfo;
}

echo json_encode($response);
?>
