<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "data" => [], "message" => ""];

// Get base URL dynamically
$protocol = isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? "https://" : "http://";
$host = $_SERVER['HTTP_HOST'];
$baseUrl = $protocol . $host . "/ayurvedic_API/";

$result = mysqli_query($conn, "SELECT * FROM recipes ORDER BY created_at DESC");

if ($result) {
    $data = [];

    while ($row = mysqli_fetch_assoc($result)) {
        // Construct full image URL if image_url is a relative path
        if (!empty($row['image_url']) && strpos($row['image_url'], 'http') !== 0) {
            $row['image_url'] = $baseUrl . $row['image_url'];
        }
        $data[] = $row;
    }

    $response["success"] = true;
    $response["data"] = $data;
} else {
    $response["message"] = "Failed to fetch recipes";
}

echo json_encode($response);
$conn->close();
?>
