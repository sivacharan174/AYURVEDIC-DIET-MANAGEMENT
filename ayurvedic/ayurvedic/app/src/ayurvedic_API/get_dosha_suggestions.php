<?php
// Get Dosha-based food suggestions API
header('Content-Type: application/json');
include 'db.php';

$patient_id = isset($_GET['patient_id']) ? intval($_GET['patient_id']) : 0;
$meal_type = isset($_GET['meal_type']) ? $_GET['meal_type'] : '';
$limit = isset($_GET['limit']) ? intval($_GET['limit']) : 10;

if ($patient_id <= 0) {
    echo json_encode(['success' => false, 'message' => 'Invalid patient ID']);
    exit;
}

// Get patient's Prakriti and current Vikriti
$prakriti_sql = "SELECT vata, pitta, kapha FROM prakriti_assessment WHERE patient_id = $patient_id ORDER BY created_at DESC LIMIT 1";
$prakriti_result = mysqli_query($conn, $prakriti_sql);

$vikriti_sql = "SELECT vata, pitta, kapha FROM vikriti_assessment WHERE patient_id = $patient_id ORDER BY created_at DESC LIMIT 1";
$vikriti_result = mysqli_query($conn, $vikriti_sql);

if (!$prakriti_result || mysqli_num_rows($prakriti_result) == 0) {
    echo json_encode(['success' => false, 'message' => 'No Prakriti assessment found for patient']);
    exit;
}

$prakriti = mysqli_fetch_assoc($prakriti_result);
$vikriti = mysqli_fetch_assoc($vikriti_result);

// Determine dominant dosha to balance
$dominant_dosha = 'vata';
$max_value = 0;

// If vikriti exists, use it to determine what to balance
if ($vikriti) {
    if ($vikriti['vata'] > $max_value) { $max_value = $vikriti['vata']; $dominant_dosha = 'vata'; }
    if ($vikriti['pitta'] > $max_value) { $max_value = $vikriti['pitta']; $dominant_dosha = 'pitta'; }
    if ($vikriti['kapha'] > $max_value) { $max_value = $vikriti['kapha']; $dominant_dosha = 'kapha'; }
} else {
    // Use prakriti
    if ($prakriti['vata'] > $max_value) { $max_value = $prakriti['vata']; $dominant_dosha = 'vata'; }
    if ($prakriti['pitta'] > $max_value) { $max_value = $prakriti['pitta']; $dominant_dosha = 'pitta'; }
    if ($prakriti['kapha'] > $max_value) { $max_value = $prakriti['kapha']; $dominant_dosha = 'kapha'; }
}

// Foods that REDUCE the dominant dosha are recommended
// Vata: Sweet, Sour, Salty, Warm, Heavy foods
// Pitta: Sweet, Bitter, Astringent, Cool foods
// Kapha: Pungent, Bitter, Astringent, Light, Warm foods

$suitable_column = "suitable_for_" . $dominant_dosha;

// Get suggested foods with their details
$sql = "SELECT f.id, f.name, f.category, f.calories, f.protein, f.carbs, f.fat,
               f.rasa, f.guna, f.digestibility, f.dosha_effect,
               COALESCE(dfs.priority_score, 5) as priority_score,
               dfs.notes as suggestion_notes
        FROM foods f
        LEFT JOIN dosha_food_suggestions dfs ON f.id = dfs.food_id
        WHERE (dfs.$suitable_column = 1 OR f.dosha_effect LIKE '%$dominant_dosha%' OR f.dosha_effect LIKE '%All Doshas%')
        ORDER BY COALESCE(dfs.priority_score, 5) DESC, f.name ASC
        LIMIT $limit";

$result = mysqli_query($conn, $sql);

$suggestions = [];
if ($result) {
    while ($row = mysqli_fetch_assoc($result)) {
        $suggestions[] = $row;
    }
}

// Get foods to avoid for this dosha
$avoid_sql = "SELECT f.id, f.name, f.category, f.dosha_effect, f.guna
              FROM foods f
              WHERE (f.dosha_effect NOT LIKE '%$dominant_dosha%' AND f.dosha_effect NOT LIKE '%All Doshas%')
              LIMIT 10";
$avoid_result = mysqli_query($conn, $avoid_sql);

$foods_to_avoid = [];
if ($avoid_result) {
    while ($row = mysqli_fetch_assoc($avoid_result)) {
        $foods_to_avoid[] = $row;
    }
}

echo json_encode([
    'success' => true,
    'patient_id' => $patient_id,
    'dominant_dosha' => $dominant_dosha,
    'prakriti' => $prakriti,
    'vikriti' => $vikriti,
    'suggested_foods' => $suggestions,
    'foods_to_avoid' => $foods_to_avoid,
    'recommendation_note' => "Based on your $dominant_dosha imbalance, these foods are recommended to restore balance."
]);

mysqli_close($conn);
?>
