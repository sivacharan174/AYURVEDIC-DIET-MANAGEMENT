<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (isset($_POST['name'])) {
    $name = $_POST['name'];
    $category = isset($_POST['category']) ? $_POST['category'] : 'Other';
    $calories = isset($_POST['calories']) ? intval($_POST['calories']) : 0;
    $protein = isset($_POST['protein']) ? floatval($_POST['protein']) : 0;
    $carbs = isset($_POST['carbs']) ? floatval($_POST['carbs']) : 0;
    $fat = isset($_POST['fat']) ? floatval($_POST['fat']) : 0;
    $rasa = isset($_POST['rasa']) ? $_POST['rasa'] : '';
    $guna = isset($_POST['guna']) ? $_POST['guna'] : '';
    $dosha_effect = isset($_POST['dosha_effect']) ? $_POST['dosha_effect'] : '';

    $stmt = $conn->prepare(
        "INSERT INTO foods (name, category, calories, protein, carbs, fat, rasa, guna, dosha_effect, is_custom, created_at) 
         VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, NOW())"
    );
    $stmt->bind_param("ssidddsss", $name, $category, $calories, $protein, $carbs, $fat, $rasa, $guna, $dosha_effect);

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Food added successfully";
        $response['food_id'] = $conn->insert_id;
    } else {
        $response['message'] = "Failed to add: " . $conn->error;
    }

    $stmt->close();
} else {
    $response['message'] = "Food name is required";
}

echo json_encode($response);
$conn->close();
?>
