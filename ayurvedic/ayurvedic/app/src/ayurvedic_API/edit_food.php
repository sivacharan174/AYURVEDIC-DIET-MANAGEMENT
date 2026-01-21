<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (isset($_POST['action']) && $_POST['action'] == 'delete') {
    // Delete food
    $id = intval($_POST['id']);
    $stmt = $conn->prepare("DELETE FROM foods WHERE id = ?");
    $stmt->bind_param("i", $id);
    
    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Food deleted successfully";
    } else {
        $response['message'] = "Failed to delete: " . $conn->error;
    }
    $stmt->close();
    
} elseif (isset($_POST['id']) && isset($_POST['name'])) {
    // Update food
    $id = intval($_POST['id']);
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
        "UPDATE foods SET name=?, category=?, calories=?, protein=?, carbs=?, fat=?, rasa=?, guna=?, dosha_effect=?, updated_at=NOW() WHERE id=?"
    );
    $stmt->bind_param("ssidddsssi", $name, $category, $calories, $protein, $carbs, $fat, $rasa, $guna, $dosha_effect, $id);

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Food updated successfully";
    } else {
        $response['message'] = "Failed to update: " . $conn->error;
    }
    $stmt->close();
    
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
