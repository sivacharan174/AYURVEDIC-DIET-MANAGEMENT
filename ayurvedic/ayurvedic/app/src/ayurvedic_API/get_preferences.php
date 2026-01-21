<?php
// Get user preferences API
header('Content-Type: application/json');
include 'db.php';

$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;

if ($user_id <= 0) {
    echo json_encode(['success' => false, 'message' => 'Invalid user ID']);
    exit;
}

$sql = "SELECT * FROM user_preferences WHERE user_id = $user_id";
$result = mysqli_query($conn, $sql);

if ($result && mysqli_num_rows($result) > 0) {
    $preferences = mysqli_fetch_assoc($result);
    echo json_encode([
        'success' => true,
        'preferences' => $preferences
    ]);
} else {
    // Return default preferences
    echo json_encode([
        'success' => true,
        'preferences' => [
            'preferred_language' => 'English',
            'diet_system' => 'integrated',
            'measurement_unit' => 'metric',
            'calorie_display' => 1,
            'dosha_alerts' => 1,
            'conflict_warnings' => 1,
            'seasonal_suggestions' => 1
        ]
    ]);
}

mysqli_close($conn);
?>
