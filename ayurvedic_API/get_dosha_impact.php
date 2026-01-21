<?php
header("Content-Type: application/json");
include "db.php";

$patient_id = isset($_GET['patient_id']) ? intval($_GET['patient_id']) : 0;

$response = [
    "success" => true,
    "data" => [
        "vata_impact" => 50,
        "pitta_impact" => 50,
        "kapha_impact" => 50,
        "analysis" => "Add foods to diet chart to see impact analysis."
    ]
];

if ($patient_id > 0) {
    // Get current diet chart foods
    $stmt = $conn->prepare("
        SELECT f.dosha_effect, COUNT(*) as count
        FROM diet_chart_items dci
        JOIN diet_charts dc ON dci.diet_chart_id = dc.id
        JOIN foods f ON dci.food_id = f.id
        WHERE dc.patient_id = ? AND dc.status = 'active'
        GROUP BY f.dosha_effect
    ");
    
    if ($stmt) {
        $stmt->bind_param("i", $patient_id);
        $stmt->execute();
        $result = $stmt->get_result();
        
        $vata = 50; $pitta = 50; $kapha = 50;
        $analysis = [];
        
        while ($row = $result->fetch_assoc()) {
            $effect = strtolower($row['dosha_effect']);
            $count = $row['count'];
            
            if (strpos($effect, 'vata') !== false) {
                $vata += $count * 5;
                $analysis[] = "Foods affecting Vata detected";
            }
            if (strpos($effect, 'pitta') !== false) {
                $pitta += $count * 5;
                $analysis[] = "Foods affecting Pitta detected";
            }
            if (strpos($effect, 'kapha') !== false) {
                $kapha += $count * 5;
                $analysis[] = "Foods affecting Kapha detected";
            }
        }
        
        // Cap at 100
        $response['data']['vata_impact'] = min(100, $vata);
        $response['data']['pitta_impact'] = min(100, $pitta);
        $response['data']['kapha_impact'] = min(100, $kapha);
        
        if (!empty($analysis)) {
            $response['data']['analysis'] = implode(". ", array_unique($analysis));
        }
        
        $stmt->close();
    }
}

echo json_encode($response);
$conn->close();
?>
