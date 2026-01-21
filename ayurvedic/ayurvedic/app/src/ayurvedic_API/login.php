<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (isset($_POST['email']) && isset($_POST['password'])) {
    $email = $_POST['email'];
    $password = $_POST['password'];

    $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();

        if (password_verify($password, $row['password'])) {
            $response['success'] = true;
            $response['message'] = "Login successful";
            $response['user_id'] = $row['id'];
            $response['name'] = $row['name'];
        } else {
            $response['message'] = "Invalid password";
        }
    } else {
        $response['message'] = "User not found";
    }

    $stmt->close();
} else {
    $response['message'] = "Missing email or password";
}

echo json_encode($response);
$conn->close();
?>
