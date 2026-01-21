<?php
header("Content-Type: application/json");
include "db.php";

$response = [];

$result = $conn->query("
    SELECT t.*, p.first_name, p.last_name 
    FROM tasks t 
    LEFT JOIN patients p ON t.patient_id = p.id 
    ORDER BY t.due_date ASC
");

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response[] = [
            "id" => $row['id'],
            "title" => $row['title'],
            "description" => $row['description'],
            "patient_name" => $row['first_name'] . ' ' . $row['last_name'],
            "patient_id" => $row['patient_id'],
            "due_date" => $row['due_date'],
            "status" => $row['status'],
            "type" => $row['type']
        ];
    }
    echo json_encode(["success" => true, "data" => $response]);
} else {
    echo json_encode(["success" => true, "data" => []]);
}

$conn->close();
?>
