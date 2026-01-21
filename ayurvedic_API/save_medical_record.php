<?php
/**
 * Save Medical Record
 * POST: patient_id, visit_date, visit_type, reason, diagnosis, prescription, notes
 * Optional: record_id (for updating existing record)
 */

header('Content-Type: application/json');
require_once 'config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(['success' => false, 'message' => 'Invalid request method']);
    exit;
}

$patient_id = isset($_POST['patient_id']) ? intval($_POST['patient_id']) : 0;
$visit_date = isset($_POST['visit_date']) ? trim($_POST['visit_date']) : '';
$visit_type = isset($_POST['visit_type']) ? trim($_POST['visit_type']) : 'Follow-up';
$reason = isset($_POST['reason']) ? trim($_POST['reason']) : '';
$diagnosis = isset($_POST['diagnosis']) ? trim($_POST['diagnosis']) : '';
$prescription = isset($_POST['prescription']) ? trim($_POST['prescription']) : '';
$notes = isset($_POST['notes']) ? trim($_POST['notes']) : '';
$record_id = isset($_POST['record_id']) ? intval($_POST['record_id']) : 0;

if ($patient_id <= 0) {
    echo json_encode(['success' => false, 'message' => 'Patient ID is required']);
    exit;
}

if (empty($visit_date)) {
    echo json_encode(['success' => false, 'message' => 'Visit date is required']);
    exit;
}

try {
    if ($record_id > 0) {
        // Update existing record
        $stmt = $conn->prepare("UPDATE medical_records SET 
            visit_date = ?, 
            visit_type = ?, 
            reason = ?, 
            diagnosis = ?, 
            prescription = ?, 
            notes = ?,
            updated_at = NOW()
            WHERE id = ? AND patient_id = ?");
        $stmt->bind_param("ssssssii", $visit_date, $visit_type, $reason, $diagnosis, $prescription, $notes, $record_id, $patient_id);
        
        if ($stmt->execute()) {
            echo json_encode([
                'success' => true, 
                'message' => 'Medical record updated successfully',
                'record_id' => $record_id
            ]);
        } else {
            echo json_encode(['success' => false, 'message' => 'Failed to update record']);
        }
    } else {
        // Insert new record
        $stmt = $conn->prepare("INSERT INTO medical_records 
            (patient_id, visit_date, visit_type, reason, diagnosis, prescription, notes, created_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("issssss", $patient_id, $visit_date, $visit_type, $reason, $diagnosis, $prescription, $notes);
        
        if ($stmt->execute()) {
            $new_id = $conn->insert_id;
            echo json_encode([
                'success' => true, 
                'message' => 'Medical record saved successfully',
                'record_id' => $new_id
            ]);
        } else {
            echo json_encode(['success' => false, 'message' => 'Failed to save record']);
        }
    }
    
    $stmt->close();
} catch (Exception $e) {
    echo json_encode(['success' => false, 'message' => 'Database error: ' . $e->getMessage()]);
}

$conn->close();
?>
