<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['patient_id'])
) {
    $stmt = $conn->prepare(
        "INSERT INTO rasa_preferences
        (patient_id, sweet, sour, salty, pungent, bitter, astringent)
        VALUES (?, ?, ?, ?, ?, ?, ?)"
    );

    $stmt->bind_param(
        "iiiiiii",
        $_POST['patient_id'],
        $_POST['sweet'],
        $_POST['sour'],
        $_POST['salty'],
        $_POST['pungent'],
        $_POST['bitter'],
        $_POST['astringent']
    );

    if ($stmt->execute()) {
        $response["success"] = true;
        $response["message"] = "Rasa preferences saved";
    } else {
        $response["message"] = "Insert failed";
    }

    $stmt->close();
} else {
    $response["message"] = "Missing parameters";
}

echo json_encode($response);
$conn->close();
?>
