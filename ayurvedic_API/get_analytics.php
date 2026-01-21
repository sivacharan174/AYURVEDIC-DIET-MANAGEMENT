<?php
header("Content-Type: application/json");
include "db.php";

$response = [
    "success" => true,
    "data" => [
        "total_patients" => 0,
        "active_diets" => 0,
        "pending_tasks" => 0,
        "vata_percent" => 33,
        "pitta_percent" => 34,
        "kapha_percent" => 33
    ]
];

// Get total patients
$result = $conn->query("SELECT COUNT(*) as count FROM patients");
if ($result && $row = $result->fetch_assoc()) {
    $response['data']['total_patients'] = intval($row['count']);
}

// Get active diet charts
$result = $conn->query("SELECT COUNT(*) as count FROM diet_charts WHERE status = 'active'");
if ($result && $row = $result->fetch_assoc()) {
    $response['data']['active_diets'] = intval($row['count']);
}

// Get pending tasks
$result = $conn->query("SELECT COUNT(*) as count FROM tasks WHERE status = 'pending'");
if ($result && $row = $result->fetch_assoc()) {
    $response['data']['pending_tasks'] = intval($row['count']);
}

// Get dosha distribution from prakriti assessments
$result = $conn->query("SELECT AVG(vata) as vata, AVG(pitta) as pitta, AVG(kapha) as kapha FROM prakriti_assessment");
if ($result && $row = $result->fetch_assoc()) {
    $total = floatval($row['vata']) + floatval($row['pitta']) + floatval($row['kapha']);
    if ($total > 0) {
        $response['data']['vata_percent'] = round(floatval($row['vata']) / $total * 100);
        $response['data']['pitta_percent'] = round(floatval($row['pitta']) / $total * 100);
        $response['data']['kapha_percent'] = round(floatval($row['kapha']) / $total * 100);
    }
}

echo json_encode($response);
$conn->close();
?>
