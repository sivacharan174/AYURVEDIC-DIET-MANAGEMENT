<?php
header("Content-Type: application/json");
include "db.php";

$response = [];

// Get patient ID if provided
$patient_id = isset($_GET['patient_id']) ? intval($_GET['patient_id']) : null;

if ($patient_id) {
    $stmt = $conn->prepare("SELECT * FROM diet_charts WHERE patient_id = ? ORDER BY created_at DESC");
    $stmt->bind_param("i", $patient_id);
    $stmt->execute();
    $result = $stmt->get_result();
} else {
    $result = $conn->query("SELECT * FROM diet_charts ORDER BY created_at DESC");
}

if ($result) {
    while ($row = $result->fetch_assoc()) {
        // Decode meal_items from JSON string to object
        $meal_items_decoded = json_decode($row['meal_items'], true);
        if ($meal_items_decoded === null) {
            $meal_items_decoded = [];
        }
        
        $response[] = [
            "id" => $row['id'],
            "patient_id" => $row['patient_id'],
            "duration" => $row['duration'],
            "goal" => $row['goal'],
            "target_calories" => intval($row['target_calories']),
            "notes" => $row['notes'],
            "meal_items" => $meal_items_decoded,
            "status" => $row['status'],
            "created_at" => $row['created_at']
        ];
    }
    echo json_encode(["success" => true, "data" => $response]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to fetch diet charts"]);
}

$conn->close();
?>
