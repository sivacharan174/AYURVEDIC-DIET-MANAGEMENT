<?php
header("Content-Type: application/json");
include "db.php";

$response = [];

$result = $conn->query("
    SELECT a.*, p.first_name, p.last_name 
    FROM clinical_alerts a 
    LEFT JOIN patients p ON a.patient_id = p.id 
    ORDER BY a.created_at DESC 
    LIMIT 50
");

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response[] = [
            "id" => $row['id'],
            "type" => $row['type'],
            "title" => $row['title'],
            "message" => $row['message'],
            "patient_name" => $row['first_name'] . ' ' . $row['last_name'],
            "patient_id" => $row['patient_id'],
            "created_at" => $row['created_at']
        ];
    }
    echo json_encode(["success" => true, "data" => $response]);
} else {
    echo json_encode(["success" => true, "data" => []]);
}

$conn->close();
?>
