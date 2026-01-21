<?php
/**
 * Get Lifestyle data for a patient
 * GET: patient_id (required), all (optional - if true, returns all records)
 */
header('Content-Type: application/json');
include 'db.php';

if (!isset($_GET['patient_id']) || empty($_GET['patient_id'])) {
    echo json_encode(['success' => false, 'message' => 'Patient ID is required']);
    exit;
}

$patient_id = intval($_GET['patient_id']);
$get_all = isset($_GET['all']) && $_GET['all'] === 'true';

if ($get_all) {
    $query = "SELECT * FROM lifestyle_inputs WHERE patient_id = $patient_id ORDER BY created_at DESC";
    $result = mysqli_query($conn, $query);
    
    if ($result && mysqli_num_rows($result) > 0) {
        $records = [];
        while ($row = mysqli_fetch_assoc($result)) {
            $records[] = [
                'id' => intval($row['id']),
                'sleep_quality' => $row['sleep_quality'] ?? '',
                'sleep_hours' => $row['sleep_hours'] ?? '',
                'bowel_movement' => $row['bowel_movement'] ?? '',
                'water_intake' => $row['water_intake'] ?? '',
                'activity_level' => $row['activity_level'] ?? '',
                'exercise_type' => $row['exercise_type'] ?? '',
                'stress_level' => $row['stress_level'] ?? '',
                'created_at' => $row['created_at'] ?? ''
            ];
        }
        echo json_encode(['success' => true, 'records' => $records]);
    } else {
        echo json_encode(['success' => true, 'records' => []]);
    }
} else {
    $query = "SELECT * FROM lifestyle_inputs WHERE patient_id = $patient_id ORDER BY id DESC LIMIT 1";
    $result = mysqli_query($conn, $query);
    
    if ($result && mysqli_num_rows($result) > 0) {
        $data = mysqli_fetch_assoc($result);
        echo json_encode([
            'success' => true,
            'exists' => true,
            'data' => [
                'id' => intval($data['id']),
                'sleep_quality' => $data['sleep_quality'] ?? '',
                'sleep_hours' => $data['sleep_hours'] ?? '',
                'bowel_movement' => $data['bowel_movement'] ?? '',
                'water_intake' => $data['water_intake'] ?? '',
                'activity_level' => $data['activity_level'] ?? '',
                'exercise_type' => $data['exercise_type'] ?? '',
                'stress_level' => $data['stress_level'] ?? '',
                'created_at' => $data['created_at'] ?? ''
            ]
        ]);
    } else {
        echo json_encode([
            'success' => true,
            'exists' => false,
            'message' => 'No lifestyle data found for this patient'
        ]);
    }
}

mysqli_close($conn);
?>
