<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['patient_id']) &&
    isset($_POST['vata']) &&
    isset($_POST['pitta']) &&
    isset($_POST['kapha'])
) {
    $patient_id = intval($_POST['patient_id']);
    $vata = intval($_POST['vata']);
    $pitta = intval($_POST['pitta']);
    $kapha = intval($_POST['kapha']);

    $stmt = $conn->prepare(
        "INSERT INTO vikriti_assessment (patient_id, vata, pitta, kapha)
         VALUES (?, ?, ?, ?)"
    );

    $stmt->bind_param("iiii", $patient_id, $vata, $pitta, $kapha);

    if ($stmt->execute()) {
        $response["success"] = true;
        $response["message"] = "Vikriti assessment saved successfully";
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
