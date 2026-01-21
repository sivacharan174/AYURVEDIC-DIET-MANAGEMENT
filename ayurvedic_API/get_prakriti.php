<?php
/**
 * Get Prakriti Assessment for a patient
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
    // Return all records
    $query = "SELECT * FROM prakriti_assessment WHERE patient_id = $patient_id ORDER BY created_at DESC";
    $result = mysqli_query($conn, $query);
    
    if ($result && mysqli_num_rows($result) > 0) {
        $records = [];
        while ($row = mysqli_fetch_assoc($result)) {
            $records[] = [
                'id' => intval($row['id']),
                'vata' => intval($row['vata'] ?? 0),
                'pitta' => intval($row['pitta'] ?? 0),
                'kapha' => intval($row['kapha'] ?? 0),
                'created_at' => $row['created_at'] ?? ''
            ];
        }
        echo json_encode(['success' => true, 'records' => $records]);
    } else {
        echo json_encode(['success' => true, 'records' => []]);
    }
} else {
    // Return latest record only
    $query = "SELECT * FROM prakriti_assessment WHERE patient_id = $patient_id ORDER BY id DESC LIMIT 1";
    $result = mysqli_query($conn, $query);
    
    if ($result && mysqli_num_rows($result) > 0) {
        $data = mysqli_fetch_assoc($result);
        echo json_encode([
            'success' => true,
            'exists' => true,
            'data' => [
                'id' => intval($data['id']),
                'vata' => intval($data['vata'] ?? 0),
                'pitta' => intval($data['pitta'] ?? 0),
                'kapha' => intval($data['kapha'] ?? 0),
                'created_at' => $data['created_at'] ?? ''
            ]
        ]);
    } else {
        echo json_encode([
            'success' => true,
            'exists' => false,
            'message' => 'No Prakriti data found for this patient'
        ]);
    }
}

mysqli_close($conn);
?>
