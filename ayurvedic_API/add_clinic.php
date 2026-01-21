<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['user_id']) &&
    isset($_POST['clinic_name']) &&
    isset($_POST['clinic_type']) &&
    isset($_POST['practitioners']) &&
    isset($_POST['specialization'])
) {
    $user_id = intval($_POST['user_id']);
    $clinic_name = $_POST['clinic_name'];
    $clinic_type = $_POST['clinic_type'];
    $practitioners = $_POST['practitioners'];
    $specialization = $_POST['specialization'];

    $stmt = $conn->prepare(
        "INSERT INTO clinics (user_id, clinic_name, clinic_type, practitioners, specialization)
         VALUES (?, ?, ?, ?, ?)"
    );
    $stmt->bind_param("issss", $user_id, $clinic_name, $clinic_type, $practitioners, $specialization);

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Clinic added successfully";
    } else {
        $response['message'] = "Failed to add clinic: " . $conn->error;
    }

    $stmt->close();
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
