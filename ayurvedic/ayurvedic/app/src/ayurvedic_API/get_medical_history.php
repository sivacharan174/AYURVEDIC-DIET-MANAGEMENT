<?php
/**
 * Get Medical History for a patient
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
    $query = "SELECT * FROM medical_history WHERE patient_id = $patient_id ORDER BY created_at DESC";
    $result = mysqli_query($conn, $query);
    
    if ($result && mysqli_num_rows($result) > 0) {
        $records = [];
        while ($row = mysqli_fetch_assoc($result)) {
            $records[] = [
                'id' => intval($row['id']),
                'conditions' => $row['conditions'] ?? '',
                'medications' => $row['medications'] ?? '',
                'allergies' => $row['allergies'] ?? '',
                'family_history' => $row['family_history'] ?? '',
                'surgeries' => $row['surgeries'] ?? '',
                'created_at' => $row['created_at'] ?? ''
            ];
        }
        echo json_encode(['success' => true, 'records' => $records]);
    } else {
        echo json_encode(['success' => true, 'records' => []]);
    }
} else {
    $query = "SELECT * FROM medical_history WHERE patient_id = $patient_id ORDER BY id DESC LIMIT 1";
    $result = mysqli_query($conn, $query);
    
    if ($result && mysqli_num_rows($result) > 0) {
        $data = mysqli_fetch_assoc($result);
        echo json_encode([
            'success' => true,
            'exists' => true,
            'data' => [
                'id' => intval($data['id']),
                'conditions' => $data['conditions'] ?? '',
                'medications' => $data['medications'] ?? '',
                'allergies' => $data['allergies'] ?? '',
                'family_history' => $data['family_history'] ?? '',
                'surgeries' => $data['surgeries'] ?? '',
                'created_at' => $data['created_at'] ?? ''
            ]
        ]);
    } else {
        echo json_encode([
            'success' => true,
            'exists' => false,
            'message' => 'No medical history found for this patient'
        ]);
    }
}

mysqli_close($conn);
?>
