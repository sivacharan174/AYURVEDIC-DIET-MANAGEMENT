<?php
header("Content-Type: application/json");
include "db.php";

$patient_id = isset($_GET['patient_id']) ? intval($_GET['patient_id']) : 0;

$response = [
    "success" => true,
    "data" => [
        "score" => 75,
        "label" => "Good",
        "vata" => "Balanced",
        "pitta" => "Balanced",
        "kapha" => "Balanced",
        "rasa_analysis" => "Complete assessment for detailed analysis.",
        "recommendations" => "• Complete Prakriti assessment\n• Add current diet information"
    ]
];

if ($patient_id > 0) {
    // Get Prakriti scores
    $stmt = $conn->prepare("SELECT vata, pitta, kapha FROM prakriti_assessment WHERE patient_id = ? ORDER BY created_at DESC LIMIT 1");
    if ($stmt) {
        $stmt->bind_param("i", $patient_id);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($row = $result->fetch_assoc()) {
            $vata = intval($row['vata']);
            $pitta = intval($row['pitta']);
            $kapha = intval($row['kapha']);
            $total = $vata + $pitta + $kapha;
            
            if ($total > 0) {
                // Determine dominant dosha
                $response['data']['vata'] = $vata > 40 ? "High" : ($vata > 25 ? "Slight Excess" : "Balanced");
                $response['data']['pitta'] = $pitta > 40 ? "High" : ($pitta > 25 ? "Slight Excess" : "Balanced");
                $response['data']['kapha'] = $kapha > 40 ? "High" : ($kapha > 25 ? "Slight Excess" : "Balanced");
                
                // Calculate balance score (simplified)
                $maxDosha = max($vata, $pitta, $kapha);
                $minDosha = min($vata, $pitta, $kapha);
                $variance = $maxDosha - $minDosha;
                $response['data']['score'] = max(50, 100 - $variance);
                
                if ($response['data']['score'] >= 80) {
                    $response['data']['label'] = "Excellent";
                } elseif ($response['data']['score'] >= 60) {
                    $response['data']['label'] = "Good";
                } else {
                    $response['data']['label'] = "Needs Improvement";
                }
                
                // Generate recommendations based on dominant dosha
                $recommendations = [];
                if ($vata > 30) {
                    $recommendations[] = "• Favor warm, cooked foods";
                    $recommendations[] = "• Include more sweet, sour, salty tastes";
                }
                if ($pitta > 30) {
                    $recommendations[] = "• Add cooling foods like cucumber, coconut";
                    $recommendations[] = "• Reduce spicy and fried foods";
                }
                if ($kapha > 30) {
                    $recommendations[] = "• Include more bitter and pungent tastes";
                    $recommendations[] = "• Reduce dairy and heavy foods";
                }
                if (!empty($recommendations)) {
                    $response['data']['recommendations'] = implode("\n", $recommendations);
                }
            }
        }
        $stmt->close();
    }
}

echo json_encode($response);
$conn->close();
?>
