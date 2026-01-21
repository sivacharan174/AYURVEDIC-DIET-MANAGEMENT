<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['patient_id']) &&
    isset($_POST['appointment_date']) &&
    isset($_POST['appointment_time'])
) {
    $patient_id = intval($_POST['patient_id']);
    $appointment_date = $_POST['appointment_date'];
    $appointment_time = $_POST['appointment_time'];
    $purpose = isset($_POST['purpose']) ? $_POST['purpose'] : '';
    $notes = isset($_POST['notes']) ? $_POST['notes'] : '';
    $status = isset($_POST['status']) ? $_POST['status'] : 'scheduled';

    $stmt = $conn->prepare(
        "INSERT INTO appointments (patient_id, appointment_date, appointment_time, purpose, notes, status, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())"
    );
    $stmt->bind_param("isssss", $patient_id, $appointment_date, $appointment_time, $purpose, $notes, $status);

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Appointment scheduled successfully";
        $response['appointment_id'] = $conn->insert_id;
    } else {
        $response['message'] = "Failed to schedule appointment: " . $conn->error;
    }

    $stmt->close();
} else {
    $response['message'] = "Missing required fields (patient_id, appointment_date, appointment_time)";
}

echo json_encode($response);
$conn->close();
?>
