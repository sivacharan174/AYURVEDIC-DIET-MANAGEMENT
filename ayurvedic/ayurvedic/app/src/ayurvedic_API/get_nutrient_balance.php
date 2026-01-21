<?php
header("Content-Type: application/json");
include "db.php";

$patient_id = isset($_GET['patient_id']) ? intval($_GET['patient_id']) : 0;

$response = [
    "success" => true,
    "data" => [
        "current_calories" => 0,
        "target_calories" => 1800,
        "current_protein" => 0,
        "target_protein" => 60,
        "current_carbs" => 0,
        "target_carbs" => 225,
        "current_fat" => 0,
        "target_fat" => 50
    ]
];

if ($patient_id > 0) {
    // Get target from latest diet chart
    $targetStmt = $conn->prepare("SELECT target_calories FROM diet_charts WHERE patient_id = ? ORDER BY created_at DESC LIMIT 1");
    $targetStmt->bind_param("i", $patient_id);
    $targetStmt->execute();
    $targetResult = $targetStmt->get_result();
    if ($row = $targetResult->fetch_assoc()) {
        $response['data']['target_calories'] = $row['target_calories'];
    }
    $targetStmt->close();

    // Calculate current nutrition from diet chart items
    $stmt = $conn->prepare("
        SELECT SUM(f.calories) as total_cal, SUM(f.protein) as total_prot, 
               SUM(f.carbs) as total_carbs, SUM(f.fat) as total_fat
        FROM diet_chart_items dci
        JOIN diet_charts dc ON dci.diet_chart_id = dc.id
        JOIN foods f ON dci.food_id = f.id
        WHERE dc.patient_id = ? AND dc.status = 'active'
    ");
    if ($stmt) {
        $stmt->bind_param("i", $patient_id);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($row = $result->fetch_assoc()) {
            $response['data']['current_calories'] = intval($row['total_cal']);
            $response['data']['current_protein'] = floatval($row['total_prot']);
            $response['data']['current_carbs'] = floatval($row['total_carbs']);
            $response['data']['current_fat'] = floatval($row['total_fat']);
        }
        $stmt->close();
    }
}

echo json_encode($response);
$conn->close();
?>
