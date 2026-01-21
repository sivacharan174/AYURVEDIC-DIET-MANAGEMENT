<?php
header("Content-Type: application/json");
include "db.php";

$season = isset($_GET['season']) ? $_GET['season'] : 'winter';

$response = ["success" => true, "data" => []];

// Map seasons to Ayurvedic recommendations
$seasonFoods = [
    'spring' => ['Bitter', 'Pungent', 'Astringent'],
    'summer' => ['Sweet', 'Bitter', 'Astringent'],
    'monsoon' => ['Sour', 'Salty', 'Sweet'],
    'autumn' => ['Sweet', 'Bitter', 'Astringent'],
    'winter' => ['Sweet', 'Sour', 'Salty']
];

$tastes = isset($seasonFoods[$season]) ? $seasonFoods[$season] : $seasonFoods['winter'];
$tasteList = "'" . implode("','", $tastes) . "'";

// Get foods matching the season's recommended tastes
$stmt = $conn->prepare("SELECT * FROM foods WHERE rasa IN ($tasteList) ORDER BY name LIMIT 30");
if ($stmt) {
    $stmt->execute();
    $result = $stmt->get_result();
    while ($row = $result->fetch_assoc()) {
        $response['data'][] = [
            "id" => $row['id'],
            "name" => $row['name'],
            "category" => $row['category'],
            "calories" => $row['calories'],
            "rasa" => $row['rasa'],
            "season" => $season
        ];
    }
    $stmt->close();
} else {
    // Fallback: get all foods
    $result = $conn->query("SELECT * FROM foods ORDER BY name LIMIT 30");
    while ($row = $result->fetch_assoc()) {
        $response['data'][] = [
            "id" => $row['id'],
            "name" => $row['name'],
            "category" => $row['category'],
            "calories" => $row['calories'],
            "rasa" => $row['rasa'],
            "season" => $season
        ];
    }
}

echo json_encode($response);
$conn->close();
?>
