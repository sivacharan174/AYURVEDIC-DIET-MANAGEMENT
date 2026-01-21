<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['patient_id']) &&
    isset($_POST['duration']) &&
    isset($_POST['goal'])
) {
    $patient_id = intval($_POST['patient_id']);
    $duration = $_POST['duration'];
    $goal = $_POST['goal'];
    $target_calories = isset($_POST['target_calories']) ? intval($_POST['target_calories']) : 0;
    $notes = isset($_POST['notes']) ? $_POST['notes'] : '';
    $meal_items = isset($_POST['meal_items']) ? $_POST['meal_items'] : '{}';

    $stmt = $conn->prepare(
        "INSERT INTO diet_charts (patient_id, duration, goal, target_calories, notes, meal_items, status, created_at) VALUES (?, ?, ?, ?, ?, ?, 'active', NOW())"
    );
    $stmt->bind_param("ississ", $patient_id, $duration, $goal, $target_calories, $notes, $meal_items);

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Diet chart saved successfully";
        $response['diet_chart_id'] = $conn->insert_id;
    } else {
        $response['message'] = "Failed to save: " . $conn->error;
    }

    $stmt->close();
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
