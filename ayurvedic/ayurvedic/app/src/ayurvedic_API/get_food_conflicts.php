<?php
// Get Food Conflicts (Viruddha Ahara) API
header('Content-Type: application/json');
include 'db.php';

// Check if specific foods are being checked for conflicts
$food_ids = isset($_GET['food_ids']) ? $_GET['food_ids'] : '';
$check_single = isset($_GET['food_id']) ? intval($_GET['food_id']) : 0;

if (!empty($food_ids)) {
    // Check conflicts among a list of foods (for diet chart validation)
    $food_id_array = array_map('intval', explode(',', $food_ids));
    $food_id_list = implode(',', $food_id_array);
    
    $sql = "SELECT fc.id, fc.conflict_type, fc.reason, fc.ayurvedic_reference,
                   f1.id as food1_id, f1.name as food1_name, f1.category as food1_category,
                   f2.id as food2_id, f2.name as food2_name, f2.category as food2_category
            FROM food_conflicts fc
            JOIN foods f1 ON fc.food1_id = f1.id
            JOIN foods f2 ON fc.food2_id = f2.id
            WHERE (fc.food1_id IN ($food_id_list) AND fc.food2_id IN ($food_id_list))
               OR (fc.food2_id IN ($food_id_list) AND fc.food1_id IN ($food_id_list))";
    
    $result = mysqli_query($conn, $sql);
    
    $conflicts = [];
    $has_severe = false;
    $has_moderate = false;
    
    if ($result) {
        while ($row = mysqli_fetch_assoc($result)) {
            $conflicts[] = $row;
            if ($row['conflict_type'] == 'severe') $has_severe = true;
            if ($row['conflict_type'] == 'moderate') $has_moderate = true;
        }
    }
    
    echo json_encode([
        'success' => true,
        'has_conflicts' => count($conflicts) > 0,
        'has_severe_conflicts' => $has_severe,
        'has_moderate_conflicts' => $has_moderate,
        'conflict_count' => count($conflicts),
        'conflicts' => $conflicts,
        'warning_message' => $has_severe ? 'SEVERE: This diet contains incompatible food combinations (Viruddha Ahara). Please review and modify.' : 
                            ($has_moderate ? 'WARNING: Some food combinations may not be ideal according to Ayurvedic principles.' : '')
    ]);
    
} else if ($check_single > 0) {
    // Get all conflicts for a single food
    $sql = "SELECT fc.id, fc.conflict_type, fc.reason, fc.ayurvedic_reference,
                   f1.id as food1_id, f1.name as food1_name,
                   f2.id as food2_id, f2.name as food2_name
            FROM food_conflicts fc
            JOIN foods f1 ON fc.food1_id = f1.id
            JOIN foods f2 ON fc.food2_id = f2.id
            WHERE fc.food1_id = $check_single OR fc.food2_id = $check_single";
    
    $result = mysqli_query($conn, $sql);
    
    $conflicts = [];
    if ($result) {
        while ($row = mysqli_fetch_assoc($result)) {
            // Get the conflicting food name
            $conflicting_food = ($row['food1_id'] == $check_single) ? 
                               ['id' => $row['food2_id'], 'name' => $row['food2_name']] :
                               ['id' => $row['food1_id'], 'name' => $row['food1_name']];
            
            $conflicts[] = [
                'conflicting_food' => $conflicting_food,
                'conflict_type' => $row['conflict_type'],
                'reason' => $row['reason'],
                'reference' => $row['ayurvedic_reference']
            ];
        }
    }
    
    echo json_encode([
        'success' => true,
        'food_id' => $check_single,
        'conflicts' => $conflicts,
        'message' => count($conflicts) > 0 ? 'This food has known incompatibilities' : 'No known conflicts for this food'
    ]);
    
} else {
    // Get all food conflicts (for reference)
    $sql = "SELECT fc.id, fc.conflict_type, fc.reason, fc.ayurvedic_reference,
                   f1.id as food1_id, f1.name as food1_name, f1.category as food1_category,
                   f2.id as food2_id, f2.name as food2_name, f2.category as food2_category
            FROM food_conflicts fc
            JOIN foods f1 ON fc.food1_id = f1.id
            JOIN foods f2 ON fc.food2_id = f2.id
            ORDER BY fc.conflict_type DESC, f1.name ASC";
    
    $result = mysqli_query($conn, $sql);
    
    $conflicts = [];
    if ($result) {
        while ($row = mysqli_fetch_assoc($result)) {
            $conflicts[] = $row;
        }
    }
    
    echo json_encode([
        'success' => true,
        'total_conflicts' => count($conflicts),
        'conflicts' => $conflicts
    ]);
}

mysqli_close($conn);
?>
