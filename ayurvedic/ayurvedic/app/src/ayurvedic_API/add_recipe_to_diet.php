<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "message" => ""];

if (isset($_POST['patient_id']) && isset($_POST['recipe_id'])) {
    $patient_id = intval($_POST['patient_id']);
    $recipe_id = intval($_POST['recipe_id']);
    $recipe_name = isset($_POST['recipe_name']) ? $_POST['recipe_name'] : '';

    $stmt = $conn->prepare(
        "INSERT INTO diet_chart_recipes (patient_id, recipe_id, recipe_name, created_at) VALUES (?, ?, ?, NOW())"
    );
    $stmt->bind_param("iis", $patient_id, $recipe_id, $recipe_name);

    if ($stmt->execute()) {
        $response['success'] = true;
        $response['message'] = "Recipe added to diet plan";
        $response['id'] = $conn->insert_id;
    } else {
        $response['message'] = "Failed to add: " . $conn->error;
    }

    $stmt->close();
} else {
    $response['message'] = "Missing required fields";
}

echo json_encode($response);
$conn->close();
?>
