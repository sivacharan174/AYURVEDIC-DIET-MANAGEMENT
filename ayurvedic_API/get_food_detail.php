<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

$food_id = isset($_GET['id']) ? intval($_GET['id']) : 0;

if ($food_id > 0) {
    $stmt = $conn->prepare("SELECT * FROM foods WHERE id = ?");
    $stmt->bind_param("i", $food_id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($row = $result->fetch_assoc()) {
        $response = [
            "success" => true,
            "data" => [
                "id" => $row['id'],
                "name" => $row['name'],
                "category" => $row['category'],
                "calories" => intval($row['calories']),
                "protein" => floatval($row['protein']),
                "carbs" => floatval($row['carbs']),
                "fat" => floatval($row['fat']),
                "fiber" => floatval($row['fiber']),
                "rasa" => $row['rasa'],
                "guna" => $row['guna'],
                "digestibility" => $row['digestibility'],
                "dosha_effect" => $row['dosha_effect'],
                "season" => $row['season'],
                "description" => $row['description']
            ]
        ];
    } else {
        $response['message'] = "Food not found";
    }

    $stmt->close();
} else {
    $response['message'] = "Invalid food ID";
}

echo json_encode($response);
$conn->close();
?>
