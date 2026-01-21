-- Sample Data for Ayurvedic Assessment Tables
-- Insert up to 3 sample records for each table
-- Based on existing patients (id: 1, 7)

USE ayurvedic;

-- =============================================
-- PRAKRITI (Constitution) Sample Data
-- =============================================
INSERT INTO prakriti (patient_id, vata, pitta, kapha, created_at) VALUES
(1, 35, 40, 25, '2026-01-02 10:30:00'),
(7, 45, 30, 25, '2026-01-03 14:15:00'),
(1, 33, 42, 25, '2026-01-05 09:00:00');

-- =============================================
-- VIKRITI (Current Imbalance) Sample Data
-- =============================================
INSERT INTO vikriti (patient_id, vata, pitta, kapha, created_at) VALUES
(1, 50, 35, 15, '2026-01-02 10:35:00'),
(7, 40, 45, 15, '2026-01-03 14:20:00'),
(1, 55, 30, 15, '2026-01-05 09:05:00');

-- =============================================
-- AGNI (Digestive Fire) Sample Data
-- =============================================
INSERT INTO agni (patient_id, agni_type, created_at) VALUES
(1, 'Tikshna Agni', '2026-01-02 10:40:00'),
(7, 'Vishama Agni', '2026-01-03 14:25:00'),
(1, 'Sama Agni', '2026-01-05 09:10:00');

-- =============================================
-- FOOD CONTRAINDICATIONS Sample Data
-- =============================================
INSERT INTO food_contraindications (patient_id, milk, curd, honey, radish, fruits, created_at) VALUES
(1, 1, 1, 0, 0, 0, '2026-01-02 10:45:00'),
(7, 0, 1, 1, 1, 0, '2026-01-03 14:30:00'),
(1, 1, 0, 0, 1, 1, '2026-01-05 09:15:00');

-- =============================================
-- LIFESTYLE INPUTS Sample Data (if empty)
-- =============================================
INSERT IGNORE INTO lifestyle_inputs (patient_id, sleep_quality, sleep_hours, bowel_movement, water_intake, activity_level, stress_level, created_at) VALUES
(7, 'Good', '7', 'Once daily, morning', '8', 'Moderate exercise', 'Low', '2026-01-03 15:00:00');

-- =============================================
-- DIETARY HABITS Sample Data (if empty)
-- =============================================
INSERT IGNORE INTO dietary_habits (patient_id, diet_type, meal_timings, food_preferences, created_at) VALUES
(7, 'Non-Vegetarian', 'Breakfast: 9 AM, Lunch: 2 PM, Dinner: 8 PM', 'North Indian cuisine, prefers wheat-based meals', '2026-01-03 15:05:00');

-- =============================================
-- MEDICAL HISTORY Sample Data (if empty)
-- =============================================
INSERT IGNORE INTO medical_history (patient_id, conditions, medications, allergies, created_at) VALUES
(7, 'Occasional back pain', 'Calcium supplements', 'Shellfish', '2026-01-03 15:10:00');

-- =============================================
-- DIGESTIVE STRENGTH Sample Data
-- =============================================
INSERT IGNORE INTO digestive_strength (patient_id, strength_level, symptoms, created_at) VALUES
(1, 'Moderate', 'Gas after meals, Occasional bloating', '2026-01-02 11:00:00'),
(7, 'Strong', 'None', '2026-01-03 15:15:00');

-- =============================================
-- RASA PREFERENCES Sample Data
-- =============================================
INSERT IGNORE INTO rasa_preferences (patient_id, sweet, sour, salty, pungent, bitter, astringent, created_at) VALUES
(1, 1, 0, 1, 0, 0, 0, '2026-01-02 11:05:00'),
(7, 0, 1, 0, 1, 1, 0, '2026-01-03 15:20:00');

-- Verify inserted data
SELECT 'prakriti' as table_name, COUNT(*) as count FROM prakriti
UNION ALL SELECT 'vikriti', COUNT(*) FROM vikriti
UNION ALL SELECT 'agni', COUNT(*) FROM agni
UNION ALL SELECT 'food_contraindications', COUNT(*) FROM food_contraindications
UNION ALL SELECT 'lifestyle_inputs', COUNT(*) FROM lifestyle_inputs
UNION ALL SELECT 'dietary_habits', COUNT(*) FROM dietary_habits
UNION ALL SELECT 'medical_history', COUNT(*) FROM medical_history;
