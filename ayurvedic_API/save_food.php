<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (isset($_POST['patient_id'])) {

    $stmt = $conn->prepare(
        "INSERT INTO food_contraindications
        (patient_id, milk, honey, fruits, curd, radish)
        VALUES (?, ?, ?, ?, ?, ?)"
    );

    $stmt->bind_param(
        "iiiiii",
        $_POST['patient_id'],
        $_POST['milk'],
        $_POST['honey'],
        $_POST['fruits'],
        $_POST['curd'],
        $_POST['radish']
    );

    if ($stmt->execute()) {
        $response["success"] = true;
        $response["message"] = "Food contraindications saved successfully";
    } else {
        $response["message"] = "Database insert failed";
    }

    $stmt->close();
} else {
    $response["message"] = "Missing parameters";
}

echo json_encode($response);
$conn->close();
?>
