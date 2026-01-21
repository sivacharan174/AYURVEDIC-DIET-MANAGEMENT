<?php
// Save user preferences API
header('Content-Type: application/json');
include 'db.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(['success' => false, 'message' => 'Invalid request method']);
    exit;
}

$user_id = isset($_POST['user_id']) ? intval($_POST['user_id']) : 0;
$preferred_language = isset($_POST['preferred_language']) ? $_POST['preferred_language'] : 'English';
$diet_system = isset($_POST['diet_system']) ? $_POST['diet_system'] : 'integrated';
$measurement_unit = isset($_POST['measurement_unit']) ? $_POST['measurement_unit'] : 'metric';
$calorie_display = isset($_POST['calorie_display']) ? intval($_POST['calorie_display']) : 1;
$dosha_alerts = isset($_POST['dosha_alerts']) ? intval($_POST['dosha_alerts']) : 1;
$conflict_warnings = isset($_POST['conflict_warnings']) ? intval($_POST['conflict_warnings']) : 1;
$seasonal_suggestions = isset($_POST['seasonal_suggestions']) ? intval($_POST['seasonal_suggestions']) : 1;

if ($user_id <= 0) {
    echo json_encode(['success' => false, 'message' => 'Invalid user ID']);
    exit;
}

// Check if preferences exist
$check = mysqli_query($conn, "SELECT id FROM user_preferences WHERE user_id = $user_id");

if (mysqli_num_rows($check) > 0) {
    // Update existing
    $sql = "UPDATE user_preferences SET 
            preferred_language = '$preferred_language',
            diet_system = '$diet_system',
            measurement_unit = '$measurement_unit',
            calorie_display = $calorie_display,
            dosha_alerts = $dosha_alerts,
            conflict_warnings = $conflict_warnings,
            seasonal_suggestions = $seasonal_suggestions
            WHERE user_id = $user_id";
} else {
    // Insert new
    $sql = "INSERT INTO user_preferences (user_id, preferred_language, diet_system, measurement_unit, 
            calorie_display, dosha_alerts, conflict_warnings, seasonal_suggestions)
            VALUES ($user_id, '$preferred_language', '$diet_system', '$measurement_unit',
            $calorie_display, $dosha_alerts, $conflict_warnings, $seasonal_suggestions)";
}

if (mysqli_query($conn, $sql)) {
    echo json_encode(['success' => true, 'message' => 'Preferences saved successfully']);
} else {
    echo json_encode(['success' => false, 'message' => 'Failed to save preferences: ' . mysqli_error($conn)]);
}

mysqli_close($conn);
?>
