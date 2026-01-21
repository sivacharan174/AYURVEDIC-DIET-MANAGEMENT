<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (isset($_POST['patient_id']) && isset($_POST['report_type'])) {
    $patient_id = intval($_POST['patient_id']);
    $report_type = $_POST['report_type'];
    $from_date = isset($_POST['from_date']) ? $_POST['from_date'] : date('Y-m-d', strtotime('-30 days'));
    $to_date = isset($_POST['to_date']) ? $_POST['to_date'] : date('Y-m-d');
    $include_diet = isset($_POST['include_diet_charts']) ? $_POST['include_diet_charts'] : '1';
    $include_nutrients = isset($_POST['include_nutrients']) ? $_POST['include_nutrients'] : '1';
    $include_dosha = isset($_POST['include_dosha']) ? $_POST['include_dosha'] : '1';
    $include_recommendations = isset($_POST['include_recommendations']) ? $_POST['include_recommendations'] : '1';

    // Generate report filename
    $filename = 'report_' . $patient_id . '_' . date('Ymd_His') . '.pdf';

    // Store report record
    $stmt = $conn->prepare(
        "INSERT INTO reports (patient_id, report_type, from_date, to_date, filename, created_at) VALUES (?, ?, ?, ?, ?, NOW())"
    );
    $stmt->bind_param("issss", $patient_id, $report_type, $from_date, $to_date, $filename);

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Report generated successfully";
        $response['report_id'] = $conn->insert_id;
        $response['filename'] = $filename;
    } else {
        $response['message'] = "Failed to generate report: " . $conn->error;
    }

    $stmt->close();
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
