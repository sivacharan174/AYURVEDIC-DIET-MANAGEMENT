<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (
    isset($_POST['patient_id']) &&
    isset($_POST['diet_type']) &&
    isset($_POST['meal_timings']) &&
    isset($_POST['food_preferences']) &&
    isset($_POST['eating_habits'])
) {
    $patient_id = intval($_POST['patient_id']);
    $diet_type = $_POST['diet_type'];
    $meal_timings = $_POST['meal_timings'];
    $food_preferences = $_POST['food_preferences'];
    $eating_habits = $_POST['eating_habits'];

    // Check if record exists
    $checkStmt = $conn->prepare("SELECT id FROM dietary_habits WHERE patient_id = ?");
    $checkStmt->bind_param("i", $patient_id);
    $checkStmt->execute();
    $result = $checkStmt->get_result();

    if ($result->num_rows > 0) {
        $stmt = $conn->prepare(
            "UPDATE dietary_habits SET diet_type = ?, meal_timings = ?, food_preferences = ?, eating_habits = ?, updated_at = NOW() WHERE patient_id = ?"
        );
        $stmt->bind_param("ssssi", $diet_type, $meal_timings, $food_preferences, $eating_habits, $patient_id);
    } else {
        $stmt = $conn->prepare(
            "INSERT INTO dietary_habits (patient_id, diet_type, meal_timings, food_preferences, eating_habits) VALUES (?, ?, ?, ?, ?)"
        );
        $stmt->bind_param("issss", $patient_id, $diet_type, $meal_timings, $food_preferences, $eating_habits);
    }

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Dietary habits saved successfully";
    } else {
        $response['message'] = "Failed to save: " . $conn->error;
    }

    $stmt->close();
    $checkStmt->close();
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
