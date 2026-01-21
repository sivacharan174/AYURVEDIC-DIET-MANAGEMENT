<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "data" => []];

// Get filter parameters
$patient_id = isset($_GET['patient_id']) ? intval($_GET['patient_id']) : null;
$date = isset($_GET['date']) ? $_GET['date'] : null;
$status = isset($_GET['status']) ? $_GET['status'] : null;

// Build query
$query = "SELECT a.*, 
          CONCAT(p.first_name, ' ', p.last_name) as patient_name,
          p.gender, p.phone
          FROM appointments a 
          LEFT JOIN patients p ON a.patient_id = p.id 
          WHERE 1=1";

$params = [];
$types = "";

if ($patient_id) {
    $query .= " AND a.patient_id = ?";
    $params[] = $patient_id;
    $types .= "i";
}

if ($date) {
    $query .= " AND a.appointment_date = ?";
    $params[] = $date;
    $types .= "s";
}

if ($status) {
    $query .= " AND a.status = ?";
    $params[] = $status;
    $types .= "s";
}

$query .= " ORDER BY a.appointment_date ASC, a.appointment_time ASC";

if (count($params) > 0) {
    $stmt = $conn->prepare($query);
    $stmt->bind_param($types, ...$params);
    $stmt->execute();
    $result = $stmt->get_result();
} else {
    $result = $conn->query($query);
}

if ($result) {
    $appointments = [];
    while ($row = $result->fetch_assoc()) {
        $appointments[] = [
            "id" => $row['id'],
            "patient_id" => $row['patient_id'],
            "patient_name" => $row['patient_name'],
            "gender" => $row['gender'],
            "phone" => $row['phone'],
            "appointment_date" => $row['appointment_date'],
            "appointment_time" => $row['appointment_time'],
            "purpose" => $row['purpose'],
            "notes" => $row['notes'],
            "status" => $row['status'],
            "created_at" => $row['created_at']
        ];
    }
    $response['success'] = true;
    $response['data'] = $appointments;
} else {
    $response['message'] = "Failed to fetch appointments: " . $conn->error;
}

echo json_encode($response);
$conn->close();
?>
