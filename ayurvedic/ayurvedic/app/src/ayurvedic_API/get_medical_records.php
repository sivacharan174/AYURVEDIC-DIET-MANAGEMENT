<?php
/**
 * Get Medical Records
 * GET: patient_id (required)
 * Optional: record_id (to get single record)
 */

header('Content-Type: application/json');
include 'db.php';

$patient_id = isset($_GET['patient_id']) ? intval($_GET['patient_id']) : 0;
$record_id = isset($_GET['record_id']) ? intval($_GET['record_id']) : 0;

if ($patient_id <= 0) {
    echo json_encode(['success' => false, 'message' => 'Patient ID is required']);
    exit;
}

try {
    if ($record_id > 0) {
        // Get single record
        $stmt = $conn->prepare("SELECT id, patient_id, visit_date, visit_type, reason, diagnosis, prescription, notes, created_at 
            FROM medical_records 
            WHERE id = ? AND patient_id = ?");
        $stmt->bind_param("ii", $record_id, $patient_id);
    } else {
        // Get all records for patient
        $stmt = $conn->prepare("SELECT id, patient_id, visit_date, visit_type, reason, diagnosis, prescription, notes, created_at 
            FROM medical_records 
            WHERE patient_id = ? 
            ORDER BY visit_date DESC, created_at DESC");
        $stmt->bind_param("i", $patient_id);
    }
    
    $stmt->execute();
    $result = $stmt->get_result();
    
    $records = [];
    while ($row = $result->fetch_assoc()) {
        $records[] = [
            'id' => $row['id'],
            'patient_id' => $row['patient_id'],
            'visit_date' => $row['visit_date'],
            'visit_type' => $row['visit_type'],
            'reason' => $row['reason'],
            'diagnosis' => $row['diagnosis'],
            'prescription' => $row['prescription'],
            'notes' => $row['notes'],
            'created_at' => $row['created_at']
        ];
    }
    
    echo json_encode([
        'success' => true,
        'count' => count($records),
        'records' => $records
    ]);
    
    $stmt->close();
} catch (Exception $e) {
    echo json_encode(['success' => false, 'message' => 'Database error: ' . $e->getMessage()]);
}

$conn->close();
?>
