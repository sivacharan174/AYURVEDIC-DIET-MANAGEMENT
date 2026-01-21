<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['patient_id']) &&
    isset($_POST['sleep_quality']) &&
    isset($_POST['sleep_hours']) &&
    isset($_POST['bowel_movement']) &&
    isset($_POST['water_intake']) &&
    isset($_POST['activity_level']) &&
    isset($_POST['stress_level'])
) {
    $patient_id = intval($_POST['patient_id']);
    $sleep_quality = $_POST['sleep_quality'];
    $sleep_hours = intval($_POST['sleep_hours']);
    $bowel_movement = $_POST['bowel_movement'];
    $water_intake = intval($_POST['water_intake']);
    $activity_level = $_POST['activity_level'];
    $stress_level = $_POST['stress_level'];

    // Check if record exists
    $checkStmt = $conn->prepare("SELECT id FROM lifestyle_inputs WHERE patient_id = ?");
    $checkStmt->bind_param("i", $patient_id);
    $checkStmt->execute();
    $result = $checkStmt->get_result();

    if ($result->num_rows > 0) {
        $stmt = $conn->prepare(
            "UPDATE lifestyle_inputs SET sleep_quality = ?, sleep_hours = ?, bowel_movement = ?, water_intake = ?, activity_level = ?, stress_level = ?, updated_at = NOW() WHERE patient_id = ?"
        );
        $stmt->bind_param("sissssi", $sleep_quality, $sleep_hours, $bowel_movement, $water_intake, $activity_level, $stress_level, $patient_id);
    } else {
        $stmt = $conn->prepare(
            "INSERT INTO lifestyle_inputs (patient_id, sleep_quality, sleep_hours, bowel_movement, water_intake, activity_level, stress_level) VALUES (?, ?, ?, ?, ?, ?, ?)"
        );
        $stmt->bind_param("isisiss", $patient_id, $sleep_quality, $sleep_hours, $bowel_movement, $water_intake, $activity_level, $stress_level);
    }

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Lifestyle info saved successfully";
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
