<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (isset($_POST['patient_id']) && isset($_POST['agni_type'])) {
    $patient_id = intval($_POST['patient_id']);
    $agni_type = $_POST['agni_type'];

    $stmt = $conn->prepare(
        "INSERT INTO agni_assessment (patient_id, agni_type)
         VALUES (?, ?)"
    );
    $stmt->bind_param("is", $patient_id, $agni_type);

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Agni saved successfully";
    } else {
        $response['message'] = "Database insert failed: " . $conn->error;
    }

    $stmt->close();
} else {
    $response['message'] = "Missing required parameters";
}

echo json_encode($response);
$conn->close();
?>
