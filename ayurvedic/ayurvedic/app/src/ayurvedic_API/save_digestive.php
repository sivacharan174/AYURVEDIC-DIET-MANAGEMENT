<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['patient_id']) &&
    isset($_POST['strength_level']) &&
    isset($_POST['symptoms'])
) {
    $patient_id = intval($_POST['patient_id']);
    $strength_level = $_POST['strength_level'];
    $symptoms = $_POST['symptoms'];

    $stmt = $conn->prepare(
        "INSERT INTO digestive_strength (patient_id, strength_level, symptoms)
         VALUES (?, ?, ?)"
    );

    $stmt->bind_param("iss", $patient_id, $strength_level, $symptoms);

    if ($stmt->execute()) {
        $response["success"] = true;
        $response["message"] = "Digestive strength saved successfully";
    } else {
        $response["message"] = "Database insert failed";
    }

    $stmt->close();
} else {
    $response["message"] = "Missing required parameters";
}

echo json_encode($response);
$conn->close();
?>
