<?php
header("Content-Type: application/json");
include "db.php";

$response = [];

// Get optional category filter
$category = isset($_GET['category']) ? $_GET['category'] : null;

if ($category && $category != 'All') {
    $stmt = $conn->prepare("SELECT * FROM foods WHERE category = ? ORDER BY name");
    $stmt->bind_param("s", $category);
    $stmt->execute();
    $result = $stmt->get_result();
} else {
    $result = $conn->query("SELECT * FROM foods ORDER BY name");
}

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response[] = [
            "id" => $row['id'],
            "name" => $row['name'],
            "category" => $row['category'],
            "calories" => intval($row['calories']),
            "protein" => floatval($row['protein']),
            "carbs" => floatval($row['carbs']),
            "fat" => floatval($row['fat']),
            "rasa" => $row['rasa'],
            "guna" => $row['guna'],
            "digestibility" => $row['digestibility'],
            "dosha_effect" => $row['dosha_effect']
        ];
    }
    echo json_encode(["success" => true, "data" => $response]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to fetch foods"]);
}

$conn->close();
?>
