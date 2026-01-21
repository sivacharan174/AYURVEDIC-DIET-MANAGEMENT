<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['first_name']) &&
    isset($_POST['last_name']) &&
    isset($_POST['dob']) &&
    isset($_POST['gender']) &&
    isset($_POST['email']) &&
    isset($_POST['phone']) &&
    isset($_POST['address']) &&
    isset($_POST['reg_date'])
) {
    $first_name = $_POST['first_name'];
    $last_name = $_POST['last_name'];
    $dob = $_POST['dob'];
    $gender = $_POST['gender'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];
    $address = $_POST['address'];
    $reg_date = $_POST['reg_date'];
    $followup1 = isset($_POST['followup1']) ? $_POST['followup1'] : null;
    $followup2 = isset($_POST['followup2']) ? $_POST['followup2'] : null;

    $stmt = $conn->prepare(
        "INSERT INTO patients
        (first_name, last_name, dob, gender, email, phone, address, registration_date, followup1, followup2)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
    );
    $stmt->bind_param(
        "ssssssssss",
        $first_name, $last_name, $dob, $gender, $email, $phone, $address, $reg_date, $followup1, $followup2
    );

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Patient added successfully";
        $response['patient_id'] = $conn->insert_id;
    } else {
        $response['message'] = "Failed to add patient: " . $conn->error;
    }

    $stmt->close();
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
