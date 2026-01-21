<?php
header("Content-Type: application/json");
include "db.php";

$patient_id = isset($_GET['patient_id']) ? intval($_GET['patient_id']) : 0;

$response = [
    "success" => true,
    "data" => [
        "score" => 70,
        "label" => "Good - Room for improvement",
        "suggestions" => [],
        "recommended_foods" => []
    ]
];

if ($patient_id > 0) {
    // Get patient's Prakriti
    $stmt = $conn->prepare("SELECT vata, pitta, kapha FROM prakriti_assessment WHERE patient_id = ? ORDER BY created_at DESC LIMIT 1");
    if ($stmt) {
        $stmt->bind_param("i", $patient_id);
        $stmt->execute();
        $result = $stmt->get_result();
        
        if ($row = $result->fetch_assoc()) {
            $vata = intval($row['vata']);
            $pitta = intval($row['pitta']);
            $kapha = intval($row['kapha']);
            
            // Calculate score based on balance
            $max = max($vata, $pitta, $kapha);
            $min = min($vata, $pitta, $kapha);
            $variance = $max - $min;
            
            $score = max(50, 100 - $variance);
            $response['data']['score'] = $score;
            
            if ($score >= 80) {
                $response['data']['label'] = "Excellent - Well balanced";
            } elseif ($score >= 60) {
                $response['data']['label'] = "Good - Room for improvement";
            } else {
                $response['data']['label'] = "Needs attention";
            }
            
            // Generate suggestions based on dominant dosha
            if ($pitta > 35) {
                $response['data']['suggestions'][] = "Reduce Pitta-aggravating foods";
                $response['data']['suggestions'][] = "Add cooling foods like cucumber, coconut";
            }
            if ($vata > 35) {
                $response['data']['suggestions'][] = "Add warming, grounding foods";
                $response['data']['suggestions'][] = "Include more sweet, sour, salty tastes";
            }
            if ($kapha > 35) {
                $response['data']['suggestions'][] = "Reduce heavy, oily foods";
                $response['data']['suggestions'][] = "Include more pungent, bitter tastes";
            }
        }
        $stmt->close();
    }
    
    // Get recommended foods
    $foodResult = $conn->query("SELECT id, name, category FROM foods ORDER BY RAND() LIMIT 5");
    if ($foodResult) {
        while ($row = $foodResult->fetch_assoc()) {
            $response['data']['recommended_foods'][] = $row;
        }
    }
}

echo json_encode($response);
$conn->close();
?>
