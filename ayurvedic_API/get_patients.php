<?php
header("Content-Type: application/json");
include "db.php";

$response = ["success" => false, "data" => [], "message" => ""];

$result = mysqli_query($conn, "SELECT * FROM patients ORDER BY created_at DESC");

if ($result) {
    $patients = [];

    while ($row = mysqli_fetch_assoc($result)) {
        // Calculate Age from DOB
        $dob = new DateTime($row["dob"]);
        $today = new DateTime();
        $age = $today->diff($dob)->y;

        $patients[] = [
            "id" => $row["id"],
            "first_name" => $row["first_name"],
            "last_name" => $row["last_name"],
            "name" => $row["first_name"] . " " . $row["last_name"],
            "age" => $age,
            "gender" => $row["gender"],
            "email" => $row["email"],
            "phone" => $row["phone"],
            "prakriti" => $row["prakriti"],
            "vikriti" => $row["vikriti"],
            "agni" => $row["agni"],
            "last_visit" => $row["registration_date"]
        ];
    }

    $response["success"] = true;
    $response["data"] = $patients;
} else {
    $response["message"] = "Failed to fetch patients";
}

echo json_encode($response);
$conn->close();
?>
