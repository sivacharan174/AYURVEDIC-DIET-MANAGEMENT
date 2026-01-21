<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['patient_id']) &&
    isset($_POST['conditions']) &&
    isset($_POST['medications']) &&
    isset($_POST['allergies'])
) {
    $patient_id = intval($_POST['patient_id']);
    $conditions = $_POST['conditions'];
    $medications = $_POST['medications'];
    $allergies = $_POST['allergies'];

    // Check if record exists
    $checkStmt = $conn->prepare("SELECT id FROM medical_history WHERE patient_id = ?");
    $checkStmt->bind_param("i", $patient_id);
    $checkStmt->execute();
    $result = $checkStmt->get_result();

    if ($result->num_rows > 0) {
        // Update existing record
        $stmt = $conn->prepare(
            "UPDATE medical_history SET conditions = ?, medications = ?, allergies = ?, updated_at = NOW() WHERE patient_id = ?"
        );
        $stmt->bind_param("sssi", $conditions, $medications, $allergies, $patient_id);
    } else {
        // Insert new record
        $stmt = $conn->prepare(
            "INSERT INTO medical_history (patient_id, conditions, medications, allergies) VALUES (?, ?, ?, ?)"
        );
        $stmt->bind_param("isss", $patient_id, $conditions, $medications, $allergies);
    }

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Medical history saved successfully";
    } else {
        $response['message'] = "Failed to save: " . $conn->error;
    }

    $stmt->close();
    $checkStmt->close();
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
