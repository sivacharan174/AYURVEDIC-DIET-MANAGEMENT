<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['name']) &&
    isset($_POST['email']) &&
    isset($_POST['phone']) &&
    isset($_POST['password']) &&
    isset($_POST['clinic'])
) {
    $name = $_POST['name'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);
    $clinic = $_POST['clinic'];

    // Check if email already exists
    $checkStmt = $conn->prepare("SELECT id FROM users WHERE email = ?");
    $checkStmt->bind_param("s", $email);
    $checkStmt->execute();
    $checkResult = $checkStmt->get_result();

    if ($checkResult->num_rows > 0) {
        $response['message'] = "Email already registered";
    } else {
        $stmt = $conn->prepare(
            "INSERT INTO users (name, email, phone, password, clinic)
             VALUES (?, ?, ?, ?, ?)"
        );
        $stmt->bind_param("sssss", $name, $email, $phone, $password, $clinic);

        if ($stmt->execute()) {
            $response['success'] = true;
            $response['message'] = "Signup successful";
            $response['user_id'] = $conn->insert_id;
        } else {
            $response['message'] = "Signup failed: " . $conn->error;
        }

        $stmt->close();
    }

    $checkStmt->close();
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
