-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 19, 2026 at 04:58 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ayurvedic`
--

-- --------------------------------------------------------

--
-- Table structure for table `agni`
--

CREATE TABLE `agni` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `agni_type` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `agni`
--

INSERT INTO `agni` (`id`, `patient_id`, `agni_type`, `created_at`, `updated_at`) VALUES
(1, 1, 'Tikshna Agni', '2026-01-02 05:10:00', '2026-01-07 09:43:49'),
(2, 7, 'Vishama Agni', '2026-01-03 08:55:00', '2026-01-07 09:43:49'),
(3, 1, 'Sama Agni', '2026-01-05 03:40:00', '2026-01-07 09:43:49');

-- --------------------------------------------------------

--
-- Table structure for table `agni_assessment`
--

CREATE TABLE `agni_assessment` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `agni_type` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `agni_assessment`
--

INSERT INTO `agni_assessment` (`id`, `patient_id`, `agni_type`, `created_at`) VALUES
(1, 0, 'Sama Agni', '2026-01-04 07:42:15'),
(2, 1, 'Vishama Agni', '2026-01-04 13:27:13'),
(3, 2, 'Tikshna Agni', '2026-01-04 13:27:13'),
(4, 3, 'Manda Agni', '2026-01-04 13:27:13'),
(5, 4, 'Vishama Agni', '2026-01-04 13:27:13'),
(6, 5, 'Tikshna Agni', '2026-01-04 13:27:13'),
(7, 6, 'Manda Agni', '2026-01-04 13:27:13'),
(8, 1, 'Tikshna Agni', '2026-01-07 09:37:11'),
(9, 1, 'Sama Agni', '2026-01-07 09:38:41'),
(10, 8, 'Tikshna Agni', '2026-01-07 12:34:53'),
(11, 9, 'Sama Agni', '2026-01-07 17:08:40'),
(12, 9, 'Manda Agni', '2026-01-07 17:11:19'),
(13, 3, 'Manda Agni', '2026-01-07 17:12:39'),
(14, 7, 'Manda Agni', '2026-01-07 17:39:41');

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE `appointments` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `appointment_date` date DEFAULT NULL,
  `appointment_time` time DEFAULT NULL,
  `purpose` varchar(100) DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `status` varchar(20) DEFAULT 'scheduled',
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`id`, `patient_id`, `appointment_date`, `appointment_time`, `purpose`, `notes`, `status`, `created_at`) VALUES
(1, 8, '2026-01-08', '20:00:00', 'Initial Consultation', '', 'scheduled', '2026-01-07 20:15:44'),
(2, 8, '2026-01-08', '16:27:00', 'Follow-up Visit', '', 'scheduled', '2026-01-07 22:27:44'),
(3, 8, '2026-01-07', '21:28:00', 'Initial Consultation', '', 'scheduled', '2026-01-07 22:28:47');

-- --------------------------------------------------------

--
-- Table structure for table `clinical_alerts`
--

CREATE TABLE `clinical_alerts` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `type` enum('critical','warning','info') DEFAULT 'info',
  `title` varchar(255) NOT NULL,
  `message` text DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `clinical_alerts`
--

INSERT INTO `clinical_alerts` (`id`, `patient_id`, `type`, `title`, `message`, `is_read`, `created_at`) VALUES
(1, 1, 'critical', 'Food Conflict Detected', 'Milk and fish combination found in lunch menu', 0, '2026-01-04 12:55:12'),
(2, 1, 'warning', 'High Pitta Diet', 'Current diet may aggravate Pitta dosha', 0, '2026-01-04 12:55:12'),
(3, 1, 'info', 'Diet Review Due', 'Monthly diet review is scheduled', 0, '2026-01-04 12:55:12'),
(4, 2, 'warning', 'Low Protein Intake', 'Patient protein intake below recommended levels', 0, '2026-01-04 12:55:12'),
(5, 2, 'info', 'Follow-up Reminder', 'Scheduled follow-up in 2 days', 0, '2026-01-04 12:55:12'),
(6, 3, 'critical', 'Drug-Food Interaction', 'Thyroid medication should not be taken with calcium-rich foods', 0, '2026-01-04 13:27:13'),
(7, 4, 'warning', 'Cholesterol Alert', 'Reduce ghee and oil intake in diet', 0, '2026-01-04 13:27:13'),
(8, 5, 'info', 'GERD Management', 'Avoid citrus fruits and tomatoes in diet', 1, '2026-01-04 13:27:13'),
(9, 6, 'warning', 'Vitamin D Foods', 'Add more sunlight exposure and vitamin D rich foods', 0, '2026-01-04 13:27:13');

-- --------------------------------------------------------

--
-- Table structure for table `clinics`
--

CREATE TABLE `clinics` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `clinic_name` varchar(100) DEFAULT NULL,
  `clinic_type` varchar(50) DEFAULT NULL,
  `practitioners` varchar(20) DEFAULT NULL,
  `specialization` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `clinics`
--

INSERT INTO `clinics` (`id`, `user_id`, `clinic_name`, `clinic_type`, `practitioners`, `specialization`) VALUES
(1, 1, 'kumarclinic', 'ayurvedic', '10', 'foodthearpy'),
(2, 2, 'Shiva care', 'Ayurvedic ', '25', 'food therapy '),
(3, 8, 'ayurvedic', 'clinic', '2', 'homeopathy'),
(4, 9, 'shiva care', 'ayurvedic', '25', 'food therapy'),
(5, 10, 'SHIVA CLINIC', 'ayurvedic', '25', 'food therapy'),
(6, 11, 'ayurvedic', 'ayurvedic', '14', 'food therapy'),
(7, 12, 'vayu', 'ayurvedic', '7842219470', 'diet therapy');

-- --------------------------------------------------------

--
-- Table structure for table `dietary_habits`
--

CREATE TABLE `dietary_habits` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `diet_type` varchar(50) DEFAULT NULL,
  `meal_timings` text DEFAULT NULL,
  `food_preferences` text DEFAULT NULL,
  `eating_habits` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dietary_habits`
--

INSERT INTO `dietary_habits` (`id`, `patient_id`, `diet_type`, `meal_timings`, `food_preferences`, `eating_habits`, `created_at`, `updated_at`) VALUES
(1, 1, 'Vegetarian', 'Breakfast: 8 AM, Lunch: 1 PM, Dinner: 7 PM', 'Prefers South Indian cuisine, likes rice-based meals', 'Eats slowly, drinks water after meals', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(2, 2, 'Non-Vegetarian', 'Breakfast: 7:30 AM, Lunch: 12:30 PM, Dinner: 8 PM', 'Enjoys spicy food, prefers wheat over rice', 'Fast eater, tends to skip breakfast', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(3, 3, 'Vegetarian', 'Breakfast: 9 AM, Lunch: 1:30 PM, Dinner: 7:30 PM', 'Loves dairy products, prefers mild spices', 'Regular meal timings, avoids late-night eating', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(4, 4, 'Eggetarian', 'Breakfast: 7 AM, Lunch: 12 PM, Dinner: 7 PM', 'Likes Punjabi food, enjoys paneer dishes', 'Snacks frequently between meals', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(5, 5, 'Vegetarian', 'Breakfast: 8:30 AM, Lunch: 1 PM, Dinner: 8:30 PM', 'Gujarati cuisine preferred, loves sweets', 'Eats smaller portions, multiple meals', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(6, 6, 'Non-Vegetarian', 'Breakfast: 8 AM, Lunch: 1 PM, Dinner: 7:30 PM', 'Maharashtrian food, enjoys seafood', 'Mindful eater, chews food well', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(7, 7, 'Non-Vegetarian', 'Breakfast: 9 AM, Lunch: 2 PM, Dinner: 8 PM', 'North Indian cuisine, prefers wheat-based meals', NULL, '2026-01-03 09:35:00', '2026-01-07 09:43:49'),
(9, 8, 'Vegetarian', 'Breakfast: , Lunch: , Dinner: ', 'Spicy, ', 'Eats quickly, ', '2026-01-07 12:37:24', '2026-01-07 12:37:24');

-- --------------------------------------------------------

--
-- Table structure for table `diet_charts`
--

CREATE TABLE `diet_charts` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `duration` varchar(50) DEFAULT NULL,
  `goal` varchar(100) DEFAULT NULL,
  `target_calories` int(11) DEFAULT 0,
  `notes` text DEFAULT NULL,
  `meal_items` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`meal_items`)),
  `status` enum('active','completed','paused') DEFAULT 'active',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `diet_charts`
--

INSERT INTO `diet_charts` (`id`, `patient_id`, `duration`, `goal`, `target_calories`, `notes`, `meal_items`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, '4 weeks', 'Reduce Vata imbalance, improve digestion', 1800, 'Focus on warm, grounding foods. Avoid cold and raw foods.', '{\"breakfast\":[{\"name\":\"Oatmeal with ghee\",\"qty\":\"1 bowl\"},{\"name\":\"Warm milk\",\"qty\":\"1 glass\"}],\"lunch\":[{\"name\":\"Rice\",\"qty\":\"1 cup\"},{\"name\":\"Moong dal\",\"qty\":\"1 cup\"},{\"name\":\"Ghee\",\"qty\":\"1 tsp\"}],\"dinner\":[{\"name\":\"Khichdi\",\"qty\":\"1 bowl\"},{\"name\":\"Vegetable soup\",\"qty\":\"1 bowl\"}]}', 'active', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(2, 2, '6 weeks', 'Control blood sugar, reduce Pitta', 1600, 'Cooling foods, avoid spicy. Regular meal timings critical.', '{\"breakfast\":[{\"name\":\"Idli\",\"qty\":\"3 pieces\"},{\"name\":\"Coconut chutney\",\"qty\":\"2 tbsp\"}],\"lunch\":[{\"name\":\"Brown rice\",\"qty\":\"1 cup\"},{\"name\":\"Mixed vegetables\",\"qty\":\"1 cup\"},{\"name\":\"Buttermilk\",\"qty\":\"1 glass\"}],\"dinner\":[{\"name\":\"Chapati\",\"qty\":\"2\"},{\"name\":\"Bottle gourd curry\",\"qty\":\"1 cup\"}]}', 'active', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(3, 3, '8 weeks', 'Weight management, balance Kapha', 1400, 'Light, spicy foods. Reduce heavy and oily items.', '{\"breakfast\":[{\"name\":\"Poha\",\"qty\":\"1 bowl\"},{\"name\":\"Green tea\",\"qty\":\"1 cup\"}],\"lunch\":[{\"name\":\"Quinoa\",\"qty\":\"1 cup\"},{\"name\":\"Vegetable stir-fry\",\"qty\":\"1 cup\"}],\"dinner\":[{\"name\":\"Soup\",\"qty\":\"1 bowl\"},{\"name\":\"Salad\",\"qty\":\"1 plate\"}]}', 'active', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(4, 8, '1 Week', 'Weight Gain', 399, '', '{\"Breakfast\":[],\"Evening\":[],\"Dinner\":[],\"Morning\":[],\"Lunch\":[]}', 'active', '2026-01-07 12:38:59', '2026-01-07 12:38:59'),
(5, 8, '1 Week', 'Maintenance', 0, '', '{\"Breakfast\":[],\"Evening\":[],\"Dinner\":[],\"Morning\":[],\"Lunch\":[]}', 'active', '2026-01-07 15:08:28', '2026-01-07 15:08:28'),
(6, 8, '1 Week', 'Weight Loss', 500, '', '{\"Breakfast\":[],\"Evening\":[],\"Dinner\":[],\"Morning\":[],\"Lunch\":[]}', 'active', '2026-01-07 15:31:09', '2026-01-07 15:31:09'),
(7, 8, '1 Week', 'Dosha Balancing', 1000, '', '{\"Breakfast\":[{\"id\":10,\"name\":\"Bottle Gourd\",\"calories\":14,\"rasa\":\"Sweet\"}],\"Evening\":[{\"id\":17,\"name\":\"Apple\",\"calories\":52,\"rasa\":\"Sweet\"}],\"Dinner\":[],\"Morning\":[{\"id\":43,\"name\":\"Almonds\",\"calories\":579,\"rasa\":\"Sweet\"},{\"id\":17,\"name\":\"Apple\",\"calories\":52,\"rasa\":\"Sweet\"}],\"Lunch\":[{\"id\":40,\"name\":\"Cardamom\",\"calories\":311,\"rasa\":\"Pungent\\/Sweet\"}]}', 'active', '2026-01-07 15:51:53', '2026-01-07 15:51:53'),
(8, 7, '1 Week', 'Weight Loss', 2500, '', '{\"Breakfast\":[],\"Evening\":[],\"Dinner\":[],\"Morning\":[{\"id\":43,\"name\":\"Almonds\",\"calories\":579,\"rasa\":\"Sweet\"},{\"id\":17,\"name\":\"Apple\",\"calories\":52,\"rasa\":\"Sweet\"},{\"id\":18,\"name\":\"Banana\",\"calories\":89,\"rasa\":\"Sweet\"},{\"id\":5,\"name\":\"Barley\",\"calories\":354,\"rasa\":\"Sweet\"},{\"id\":11,\"name\":\"Bitter Gourd\",\"calories\":17,\"rasa\":\"Bitter\"}],\"Lunch\":[]}', 'active', '2026-01-07 16:29:29', '2026-01-07 16:29:29'),
(9, 9, '1 Week', 'Weight Gain', 258, '', '{\"Breakfast\":[{\"id\":17,\"name\":\"Apple\",\"calories\":52,\"rasa\":\"Sweet\"},{\"id\":18,\"name\":\"Banana\",\"calories\":89,\"rasa\":\"Sweet\"},{\"id\":5,\"name\":\"Barley\",\"calories\":354,\"rasa\":\"Sweet\"}],\"Evening\":[],\"Dinner\":[],\"Morning\":[],\"Lunch\":[]}', 'active', '2026-01-07 17:06:30', '2026-01-07 17:06:30'),
(10, 9, '1 Week', 'Maintenance', 369, '', '{\"Breakfast\":[],\"Evening\":[],\"Dinner\":[],\"Morning\":[{\"id\":33,\"name\":\"Red Lentils (Masoor)\",\"calories\":352,\"rasa\":\"Sweet\"},{\"id\":4,\"name\":\"Quinoa\",\"calories\":120,\"rasa\":\"Sweet\"},{\"id\":16,\"name\":\"Cauliflower\",\"calories\":25,\"rasa\":\"Astringent\"},{\"id\":22,\"name\":\"Grapes\",\"calories\":69,\"rasa\":\"Sweet\"}],\"Lunch\":[]}', 'active', '2026-01-07 17:15:38', '2026-01-07 17:15:38');

-- --------------------------------------------------------

--
-- Table structure for table `diet_chart_items`
--

CREATE TABLE `diet_chart_items` (
  `id` int(11) NOT NULL,
  `diet_chart_id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `meal_type` varchar(50) DEFAULT NULL,
  `quantity` decimal(6,2) DEFAULT 1.00,
  `unit` varchar(20) DEFAULT 'serving',
  `notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `diet_chart_items`
--

INSERT INTO `diet_chart_items` (`id`, `diet_chart_id`, `food_id`, `meal_type`, `quantity`, `unit`, `notes`, `created_at`) VALUES
(1, 1, 3, 'Breakfast', 1.00, 'bowl', 'Cook with ghee and warm spices', '2026-01-04 13:27:13'),
(2, 1, 25, 'Breakfast', 1.00, 'glass', 'Warm milk with turmeric', '2026-01-04 13:27:13'),
(3, 1, 1, 'Lunch', 1.50, 'cup', 'Well-cooked basmati rice', '2026-01-04 13:27:13'),
(4, 1, 30, 'Lunch', 1.00, 'cup', 'Moong dal with ghee', '2026-01-04 13:27:13'),
(5, 1, 27, 'Lunch', 2.00, 'tsp', 'Ghee for digestion', '2026-01-04 13:27:13'),
(6, 1, 12, 'Dinner', 1.00, 'bowl', 'Pumpkin soup', '2026-01-04 13:27:13'),
(7, 2, 9, 'Breakfast', 1.00, 'serving', 'Cooling cucumber', '2026-01-04 13:27:13'),
(8, 2, 4, 'Lunch', 1.00, 'cup', 'Quinoa with vegetables', '2026-01-04 13:27:13'),
(9, 2, 10, 'Lunch', 1.00, 'cup', 'Bottle gourd curry', '2026-01-04 13:27:13'),
(10, 2, 28, 'Lunch', 1.00, 'glass', 'Cooling buttermilk', '2026-01-04 13:27:13'),
(11, 2, 7, 'Dinner', 1.00, 'cup', 'Spinach light preparation', '2026-01-04 13:27:13'),
(12, 3, 6, 'Breakfast', 0.75, 'cup', 'Light millet porridge', '2026-01-04 13:27:13'),
(13, 3, 36, 'Breakfast', 0.50, 'tsp', 'Ginger tea', '2026-01-04 13:27:13'),
(14, 3, 11, 'Lunch', 1.00, 'cup', 'Bitter gourd - Kapha reducing', '2026-01-04 13:27:13'),
(15, 3, 5, 'Lunch', 0.50, 'cup', 'Light barley preparation', '2026-01-04 13:27:13'),
(16, 3, 39, 'Dinner', 0.25, 'tsp', 'Black pepper for digestion', '2026-01-04 13:27:13');

-- --------------------------------------------------------

--
-- Table structure for table `digestive_strength`
--

CREATE TABLE `digestive_strength` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `strength_level` varchar(50) DEFAULT NULL,
  `symptoms` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `digestive_strength`
--

INSERT INTO `digestive_strength` (`id`, `patient_id`, `strength_level`, `symptoms`, `created_at`) VALUES
(1, 0, 'Strong', 'Gas after meals, ', '2026-01-04 07:49:13'),
(2, 1, 'Weak', 'Bloating after meals, gas, irregular appetite', '2026-01-04 13:27:13'),
(3, 2, 'Strong', 'High appetite, acidity, heartburn', '2026-01-04 13:27:13'),
(4, 3, 'Sluggish', 'Heavy feeling, slow digestion, nausea', '2026-01-04 13:27:13'),
(5, 4, 'Moderate', 'Occasional bloating, irregular bowel', '2026-01-04 13:27:13'),
(6, 5, 'Strong', 'Burns quickly, frequent hunger, loose stools', '2026-01-04 13:27:13'),
(7, 6, 'Weak', 'Low appetite, mucus, lethargy after eating', '2026-01-04 13:27:13'),
(8, 1, 'Moderate', 'Gas after meals, ', '2026-01-07 09:37:15'),
(9, 1, 'Moderate', 'Gas after meals, ', '2026-01-07 09:38:43'),
(10, 1, 'Moderate', 'Gas after meals, Occasional bloating', '2026-01-02 05:30:00'),
(11, 7, 'Strong', 'None', '2026-01-03 09:45:00'),
(12, 1, 'Moderate', 'Gas after meals, Occasional bloating', '2026-01-02 05:30:00'),
(13, 7, 'Strong', 'None', '2026-01-03 09:45:00'),
(14, 8, 'Moderate', 'Frequent bloating, Gas after meals, ', '2026-01-07 12:34:57'),
(15, 9, 'Strong', 'Frequent bloating, ', '2026-01-07 17:08:43'),
(16, 9, 'Weak', 'Frequent bloating, ', '2026-01-07 17:11:21'),
(17, 3, 'Strong', '', '2026-01-07 17:12:40'),
(18, 7, 'Strong', '', '2026-01-07 17:39:43');

-- --------------------------------------------------------

--
-- Table structure for table `dosha_food_suggestions`
--

CREATE TABLE `dosha_food_suggestions` (
  `id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `suitable_for_vata` tinyint(1) DEFAULT 0,
  `suitable_for_pitta` tinyint(1) DEFAULT 0,
  `suitable_for_kapha` tinyint(1) DEFAULT 0,
  `agni_type` varchar(50) DEFAULT NULL,
  `season_recommended` varchar(50) DEFAULT NULL,
  `priority_score` int(11) DEFAULT 5,
  `notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dosha_food_suggestions`
--

INSERT INTO `dosha_food_suggestions` (`id`, `food_id`, `suitable_for_vata`, `suitable_for_pitta`, `suitable_for_kapha`, `agni_type`, `season_recommended`, `priority_score`, `notes`) VALUES
(1, 1, 1, 1, 0, NULL, NULL, 8, 'Basmati rice - excellent for Vata and Pitta'),
(2, 2, 1, 1, 0, NULL, NULL, 7, 'Wheat - good for Vata and Pitta'),
(3, 3, 1, 1, 0, NULL, NULL, 8, 'Oats - calming, good for Vata'),
(4, 4, 1, 1, 1, NULL, NULL, 9, 'Quinoa - tridoshic, suitable for all'),
(5, 5, 0, 1, 1, NULL, NULL, 7, 'Barley - cooling, good for Pitta and Kapha'),
(6, 6, 0, 0, 1, NULL, NULL, 8, 'Millet - light, excellent for Kapha'),
(7, 7, 0, 1, 0, NULL, NULL, 7, 'Spinach - cooling for Pitta'),
(8, 8, 1, 1, 0, NULL, NULL, 7, 'Carrot - grounding for Vata'),
(9, 9, 0, 1, 0, NULL, NULL, 9, 'Cucumber - very cooling for Pitta'),
(10, 10, 0, 1, 1, NULL, NULL, 8, 'Bottle gourd - light and cooling'),
(11, 11, 0, 1, 1, NULL, NULL, 8, 'Bitter gourd - bitter taste balances Pitta/Kapha'),
(12, 12, 1, 1, 0, NULL, NULL, 7, 'Pumpkin - sweet and grounding'),
(13, 25, 1, 1, 0, NULL, NULL, 8, 'Milk - nourishing for Vata and Pitta'),
(14, 27, 1, 1, 1, NULL, NULL, 10, 'Ghee - tridoshic, excellent for all'),
(15, 28, 1, 0, 1, NULL, NULL, 7, 'Buttermilk - light, good for digestion'),
(16, 30, 1, 1, 1, NULL, NULL, 10, 'Moong dal - tridoshic, easiest to digest'),
(17, 31, 1, 0, 0, NULL, NULL, 6, 'Toor dal - warming for Vata'),
(18, 35, 1, 1, 1, NULL, NULL, 9, 'Turmeric - healing for all doshas'),
(19, 36, 1, 0, 1, NULL, NULL, 8, 'Ginger - digestive fire booster'),
(20, 37, 1, 1, 1, NULL, NULL, 8, 'Cumin - tridoshic digestive aid'),
(21, 38, 1, 1, 1, NULL, NULL, 8, 'Coriander - cooling and tridoshic');

-- --------------------------------------------------------

--
-- Table structure for table `foods`
--

CREATE TABLE `foods` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `calories` int(11) DEFAULT 0,
  `protein` decimal(6,2) DEFAULT 0.00,
  `carbs` decimal(6,2) DEFAULT 0.00,
  `fat` decimal(6,2) DEFAULT 0.00,
  `fiber` decimal(6,2) DEFAULT 0.00,
  `rasa` varchar(50) DEFAULT NULL,
  `guna` varchar(50) DEFAULT NULL,
  `digestibility` varchar(50) DEFAULT NULL,
  `dosha_effect` varchar(100) DEFAULT NULL,
  `season` varchar(50) DEFAULT NULL,
  `cuisine` varchar(50) DEFAULT 'Indian',
  `description` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `is_custom` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `foods`
--

INSERT INTO `foods` (`id`, `name`, `category`, `calories`, `protein`, `carbs`, `fat`, `fiber`, `rasa`, `guna`, `digestibility`, `dosha_effect`, `season`, `cuisine`, `description`, `created_at`, `is_custom`) VALUES
(1, 'Rice (Basmati)', 'Grains', 130, 2.70, 28.00, 0.30, 0.00, 'Sweet', 'Cold', 'Easy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(2, 'Wheat', 'Grains', 339, 13.20, 71.20, 2.50, 0.00, 'Sweet', 'Cold', 'Moderate', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(3, 'Oats', 'Grains', 389, 16.90, 66.30, 6.90, 0.00, 'Sweet', 'Cold', 'Easy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(4, 'Quinoa', 'Grains', 120, 4.40, 21.30, 1.90, 0.00, 'Sweet', 'Cold', 'Easy', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(5, 'Barley', 'Grains', 354, 12.50, 73.50, 2.30, 0.00, 'Sweet', 'Cold', 'Moderate', 'Pitta, Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(6, 'Millet', 'Grains', 378, 11.00, 73.00, 4.20, 0.00, 'Sweet', 'Hot', 'Light', 'Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(7, 'Spinach', 'Vegetables', 23, 2.90, 3.60, 0.40, 0.00, 'Astringent', 'Cold', 'Easy', 'Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(8, 'Carrot', 'Vegetables', 41, 0.90, 10.00, 0.20, 0.00, 'Sweet', 'Hot', 'Easy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(9, 'Cucumber', 'Vegetables', 15, 0.70, 3.60, 0.10, 0.00, 'Sweet', 'Cold', 'Easy', 'Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(10, 'Bottle Gourd', 'Vegetables', 14, 0.60, 3.40, 0.10, 0.00, 'Sweet', 'Cold', 'Easy', 'Pitta, Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(11, 'Bitter Gourd', 'Vegetables', 17, 1.00, 3.70, 0.20, 0.00, 'Bitter', 'Hot', 'Light', 'Pitta, Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(12, 'Pumpkin', 'Vegetables', 26, 1.00, 6.50, 0.10, 0.00, 'Sweet', 'Hot', 'Easy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(13, 'Okra', 'Vegetables', 33, 1.90, 7.50, 0.20, 0.00, 'Sweet', 'Cold', 'Moderate', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(14, 'Tomato', 'Vegetables', 18, 0.90, 3.90, 0.20, 0.00, 'Sour', 'Hot', 'Easy', 'Vata (avoid Pitta)', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(15, 'Potato', 'Vegetables', 77, 2.00, 17.50, 0.10, 0.00, 'Sweet', 'Cold', 'Heavy', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(16, 'Cauliflower', 'Vegetables', 25, 1.90, 5.00, 0.30, 0.00, 'Astringent', 'Cold', 'Heavy', 'Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(17, 'Apple', 'Fruits', 52, 0.30, 14.00, 0.20, 0.00, 'Sweet', 'Cold', 'Easy', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(18, 'Banana', 'Fruits', 89, 1.10, 23.00, 0.30, 0.00, 'Sweet', 'Cold', 'Heavy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(19, 'Mango', 'Fruits', 60, 0.80, 15.00, 0.40, 0.00, 'Sweet', 'Hot', 'Moderate', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(20, 'Papaya', 'Fruits', 43, 0.50, 11.00, 0.30, 0.00, 'Sweet', 'Hot', 'Easy', 'Vata, Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(21, 'Pomegranate', 'Fruits', 83, 1.70, 19.00, 1.20, 0.00, 'Sweet/Astringent', 'Cold', 'Easy', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(22, 'Grapes', 'Fruits', 69, 0.70, 18.00, 0.20, 0.00, 'Sweet', 'Cold', 'Easy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(23, 'Orange', 'Fruits', 47, 0.90, 12.00, 0.10, 0.00, 'Sour', 'Cold', 'Easy', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(24, 'Watermelon', 'Fruits', 30, 0.60, 7.60, 0.20, 0.00, 'Sweet', 'Cold', 'Easy', 'Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(25, 'Milk (Cow)', 'Dairy', 42, 3.40, 5.00, 1.00, 0.00, 'Sweet', 'Cold', 'Heavy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(26, 'Curd/Yogurt', 'Dairy', 59, 3.50, 3.40, 3.30, 0.00, 'Sour', 'Hot', 'Heavy', 'Vata (avoid at night)', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(27, 'Ghee', 'Dairy', 883, 0.00, 0.00, 99.50, 0.00, 'Sweet', 'Cold', 'Easy', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(28, 'Buttermilk', 'Dairy', 40, 3.30, 4.80, 0.90, 0.00, 'Sour', 'Hot', 'Light', 'Vata, Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(29, 'Paneer', 'Dairy', 265, 18.30, 1.20, 20.80, 0.00, 'Sweet', 'Cold', 'Heavy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(30, 'Moong Dal', 'Pulses', 347, 24.00, 59.00, 1.20, 0.00, 'Sweet', 'Cold', 'Light', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(31, 'Toor Dal', 'Pulses', 343, 22.30, 57.60, 1.50, 0.00, 'Sweet', 'Hot', 'Moderate', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(32, 'Chana Dal', 'Pulses', 364, 20.80, 58.00, 5.30, 0.00, 'Sweet', 'Hot', 'Heavy', 'Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(33, 'Red Lentils (Masoor)', 'Pulses', 352, 25.80, 59.00, 1.00, 0.00, 'Sweet', 'Hot', 'Light', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(34, 'Kidney Beans (Rajma)', 'Pulses', 333, 24.00, 60.00, 0.80, 0.00, 'Sweet', 'Hot', 'Heavy', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(35, 'Turmeric', 'Spices', 354, 7.80, 65.00, 10.00, 0.00, 'Bitter/Pungent', 'Hot', 'Light', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(36, 'Ginger', 'Spices', 80, 1.80, 18.00, 0.80, 0.00, 'Pungent', 'Hot', 'Light', 'Vata, Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(37, 'Cumin', 'Spices', 375, 17.80, 44.20, 22.30, 0.00, 'Pungent', 'Hot', 'Light', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(38, 'Coriander', 'Spices', 298, 12.40, 55.00, 17.80, 0.00, 'Pungent', 'Cold', 'Light', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(39, 'Black Pepper', 'Spices', 251, 10.40, 63.90, 3.30, 0.00, 'Pungent', 'Hot', 'Light', 'Kapha, Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(40, 'Cardamom', 'Spices', 311, 11.00, 68.50, 6.70, 0.00, 'Pungent/Sweet', 'Cold', 'Light', 'All Doshas', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(41, 'Cinnamon', 'Spices', 247, 4.00, 81.00, 1.20, 0.00, 'Pungent/Sweet', 'Hot', 'Light', 'Kapha, Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(42, 'Fenugreek', 'Spices', 323, 23.00, 58.00, 6.40, 0.00, 'Bitter', 'Hot', 'Light', 'Vata, Kapha', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(43, 'Almonds', 'Nuts', 579, 21.20, 21.60, 49.90, 0.00, 'Sweet', 'Hot', 'Heavy', 'Vata, Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(44, 'Walnuts', 'Nuts', 654, 15.20, 14.00, 65.20, 0.00, 'Sweet', 'Hot', 'Heavy', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(45, 'Cashews', 'Nuts', 553, 18.20, 30.20, 43.90, 0.00, 'Sweet', 'Hot', 'Heavy', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(46, 'Sesame Seeds', 'Nuts', 573, 17.70, 23.50, 49.70, 0.00, 'Sweet', 'Hot', 'Heavy', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(47, 'Flax Seeds', 'Nuts', 534, 18.30, 29.00, 42.20, 0.00, 'Sweet', 'Hot', 'Light', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(48, 'Coconut Oil', 'Oils', 862, 0.00, 0.00, 100.00, 0.00, 'Sweet', 'Cold', 'Easy', 'Pitta', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(49, 'Sesame Oil', 'Oils', 884, 0.00, 0.00, 100.00, 0.00, 'Sweet', 'Hot', 'Heavy', 'Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0),
(50, 'Mustard Oil', 'Oils', 884, 0.00, 0.00, 100.00, 0.00, 'Pungent', 'Hot', 'Light', 'Kapha, Vata', NULL, 'Indian', NULL, '2026-01-04 12:22:43', 0);

-- --------------------------------------------------------

--
-- Table structure for table `food_conflicts`
--

CREATE TABLE `food_conflicts` (
  `id` int(11) NOT NULL,
  `food1_id` int(11) NOT NULL,
  `food2_id` int(11) NOT NULL,
  `conflict_type` enum('severe','moderate','mild') DEFAULT 'moderate',
  `reason` text DEFAULT NULL,
  `ayurvedic_reference` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `food_conflicts`
--

INSERT INTO `food_conflicts` (`id`, `food1_id`, `food2_id`, `conflict_type`, `reason`, `ayurvedic_reference`, `created_at`) VALUES
(1, 25, 26, 'severe', 'Milk and curd together cause digestive issues', 'Charaka Samhita - Viruddha Ahara', '2026-01-06 05:18:11'),
(2, 25, 14, 'severe', 'Milk with sour foods like tomato is incompatible', 'Charaka Samhita', '2026-01-06 05:18:11'),
(3, 25, 23, 'severe', 'Milk with citrus fruits (orange) causes curdling in stomach', 'Ashtanga Hridaya', '2026-01-06 05:18:11'),
(4, 25, 18, 'moderate', 'Milk with banana is heavy and produces toxins (Ama)', 'Charaka Samhita', '2026-01-06 05:18:11'),
(5, 26, 25, 'severe', 'Curd and milk together are incompatible', 'Charaka Samhita', '2026-01-06 05:18:11'),
(6, 26, 8, 'moderate', 'Curd with hot foods like carrot (when cooked) can cause imbalance', 'Ayurvedic principles', '2026-01-06 05:18:11'),
(7, 27, 24, 'mild', 'Ghee (processed cold) with cold watermelon may cause digestive issues', 'General Ayurvedic principles', '2026-01-06 05:18:11'),
(8, 18, 25, 'moderate', 'Banana with milk is heavy, causes congestion', 'Ayurvedic diet rules', '2026-01-06 05:18:11'),
(9, 19, 25, 'mild', 'Mango with milk can cause skin issues for some', 'Traditional knowledge', '2026-01-06 05:18:11');

-- --------------------------------------------------------

--
-- Table structure for table `food_contraindications`
--

CREATE TABLE `food_contraindications` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `milk` tinyint(1) DEFAULT 0,
  `curd` tinyint(1) DEFAULT 0,
  `honey` tinyint(1) DEFAULT 0,
  `radish` tinyint(1) DEFAULT 0,
  `fruits` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `food_contraindications`
--

INSERT INTO `food_contraindications` (`id`, `patient_id`, `milk`, `curd`, `honey`, `radish`, `fruits`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 1, 0, 0, 0, '2026-01-02 05:15:00', '2026-01-07 09:43:49'),
(2, 7, 0, 1, 1, 1, 0, '2026-01-03 09:00:00', '2026-01-07 09:43:49'),
(3, 1, 1, 0, 0, 1, 1, '2026-01-05 03:45:00', '2026-01-07 09:43:49'),
(4, 1, 1, 1, 0, 0, 0, '2026-01-02 05:15:00', '2026-01-07 09:44:01'),
(5, 7, 0, 1, 1, 1, 0, '2026-01-03 09:00:00', '2026-01-07 09:44:01'),
(6, 1, 1, 0, 0, 1, 1, '2026-01-05 03:45:00', '2026-01-07 09:44:01'),
(7, 8, 0, 0, 1, 0, 1, '2026-01-07 12:35:04', '2026-01-07 12:35:04'),
(8, 9, 0, 0, 1, 0, 0, '2026-01-07 17:08:50', '2026-01-07 17:08:50'),
(9, 9, 0, 0, 0, 1, 0, '2026-01-07 17:11:26', '2026-01-07 17:11:26'),
(10, 3, 0, 0, 0, 0, 1, '2026-01-07 17:12:44', '2026-01-07 17:12:44'),
(11, 7, 0, 0, 0, 1, 0, '2026-01-07 17:39:47', '2026-01-07 17:39:47');

-- --------------------------------------------------------

--
-- Table structure for table `lifestyle_inputs`
--

CREATE TABLE `lifestyle_inputs` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `sleep_quality` varchar(20) DEFAULT NULL,
  `sleep_hours` int(11) DEFAULT NULL,
  `bowel_movement` varchar(50) DEFAULT NULL,
  `water_intake` int(11) DEFAULT NULL,
  `activity_level` varchar(50) DEFAULT NULL,
  `stress_level` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lifestyle_inputs`
--

INSERT INTO `lifestyle_inputs` (`id`, `patient_id`, `sleep_quality`, `sleep_hours`, `bowel_movement`, `water_intake`, `activity_level`, `stress_level`, `created_at`, `updated_at`) VALUES
(1, 1, 'Moderate', 6, 'Once daily, morning', 6, 'Light exercise (yoga)', 'High', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(2, 2, 'Poor', 5, 'Irregular', 4, 'Sedentary (desk job)', 'Very High', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(3, 3, 'Good', 8, 'Once daily, regular', 8, 'Moderate (walks daily)', 'Moderate', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(4, 4, 'Moderate', 7, 'Twice daily', 5, 'Active (gym 3x/week)', 'Moderate', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(5, 5, 'Good', 7, 'Once daily but loose', 7, 'Light (stretching)', 'Low', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(6, 6, 'Poor', 6, 'Constipated often', 4, 'Moderate (cycling)', 'High', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(7, 7, 'Good', 7, 'Once daily, morning', 8, 'Moderate exercise', 'Low', '2026-01-03 09:30:00', '2026-01-07 09:43:49');

-- --------------------------------------------------------

--
-- Table structure for table `medical_history`
--

CREATE TABLE `medical_history` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `conditions` text DEFAULT NULL,
  `medications` text DEFAULT NULL,
  `allergies` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medical_history`
--

INSERT INTO `medical_history` (`id`, `patient_id`, `conditions`, `medications`, `allergies`, `created_at`, `updated_at`) VALUES
(1, 1, 'Mild anxiety, Occasional migraines', 'Multivitamin supplements', 'Peanuts', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(2, 2, 'Type 2 Diabetes, Hypertension', 'Metformin 500mg, Amlodipine 5mg', 'Shellfish', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(3, 3, 'PCOS, Thyroid (Hypothyroid)', 'Thyronorm 50mcg', 'None', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(4, 4, 'High cholesterol, Heart disease history in family', 'Atorvastatin 10mg', 'Lactose intolerant', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(5, 5, 'Acid reflux (GERD), IBS', 'Omeprazole 20mg as needed', 'Gluten sensitivity', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(6, 6, 'Arthritis, Low vitamin D', 'Vitamin D3 60000IU weekly, Crocin for pain', 'Sulfa drugs', '2026-01-04 13:27:13', '2026-01-04 13:27:13'),
(7, 7, 'Arthritis, ', '', 'Nuts, ', '2026-01-06 08:39:03', '2026-01-06 08:39:03');

-- --------------------------------------------------------

--
-- Table structure for table `medical_records`
--

CREATE TABLE `medical_records` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `visit_date` date NOT NULL,
  `visit_type` varchar(50) DEFAULT 'Follow-up',
  `reason` text DEFAULT NULL,
  `diagnosis` text DEFAULT NULL,
  `prescription` text DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medical_records`
--

INSERT INTO `medical_records` (`id`, `patient_id`, `visit_date`, `visit_type`, `reason`, `diagnosis`, `prescription`, `notes`, `created_at`, `updated_at`) VALUES
(1, 1, '2026-01-05', 'Follow-up', 'Digestive issues and acidity', 'Pitta imbalance with increased Agni', 'Cooling herbs, Aloe vera juice, avoid spicy foods', 'Patient responding well to treatment. Follow-up in 2 weeks.', '2026-01-06 09:14:59', '2026-01-06 09:14:59'),
(2, 1, '2025-12-20', 'Initial', 'General consultation and Prakriti assessment', 'Vata-Pitta constitution identified', 'Diet modification recommended, warm foods preferred', 'Initial assessment completed. Full Prakriti evaluation performed.', '2026-01-06 09:14:59', '2026-01-06 09:14:59'),
(3, 2, '2026-01-03', 'Follow-up', 'Blood sugar monitoring', 'Type 2 Diabetes management', 'Continue Metformin, add bitter gourd to diet', 'Blood sugar levels improving with dietary changes.', '2026-01-06 09:14:59', '2026-01-06 09:14:59'),
(4, 3, '2026-01-02', 'Initial', 'Weight management consultation', 'Kapha imbalance', 'Light diet, reduce dairy, increase physical activity', 'Patient motivated to follow diet plan.', '2026-01-06 09:14:59', '2026-01-06 09:14:59');

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `id` int(11) NOT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `registration_date` date DEFAULT NULL,
  `followup1` varchar(100) DEFAULT NULL,
  `followup2` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `prakriti` varchar(50) DEFAULT NULL,
  `vikriti` varchar(50) DEFAULT NULL,
  `agni` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`id`, `first_name`, `last_name`, `dob`, `gender`, `email`, `phone`, `address`, `registration_date`, `followup1`, `followup2`, `created_at`, `prakriti`, `vikriti`, `agni`) VALUES
(1, 'Anika', 'Sharma', '1992-06-15', 'Female', 'tprem6565@gmail.com', '9876543210', 'Chennai', '2024-01-15', '2024-08-15', '2024-10-01', '2025-12-31 02:44:45', 'Vata', 'Vata-Pitta', 'Vishama'),
(2, 'Rohan', 'Verma', '1979-03-22', 'Male', 'rohan@gmail.com', '9876543211', 'Bangalore', '2024-02-10', '2024-08-10', '2024-09-20', '2025-12-31 02:44:45', 'Pitta', 'Pitta', 'Tikshna'),
(3, 'Priya', 'Kapoor', '1996-11-05', 'Female', 'priya@gmail.com', '9876543212', 'Delhi', '2024-03-01', '2024-07-30', '2024-09-05', '2025-12-31 02:44:45', 'Kapha', 'Kapha-Vata', 'Manda'),
(4, 'Arjun', 'Singh', '1974-09-18', 'Male', 'arjun@gmail.com', '9876543213', 'Hyderabad', '2024-01-28', '2024-08-05', '2024-10-10', '2025-12-31 02:44:45', 'Vata-Pitta', 'Vata', 'Vishama'),
(5, 'Divya', 'Patel', '1986-12-10', 'Female', 'divya@gmail.com', '9876543214', 'Ahmedabad', '2024-02-18', '2024-07-20', '2024-09-01', '2025-12-31 02:44:45', 'Pitta-Kapha', 'Pitta', 'Tikshna'),
(6, 'Vikram', 'Joshi', '1982-07-08', 'Male', 'vikram@gmail.com', '9876543215', 'Pune', '2024-03-12', '2024-08-01', '2024-09-15', '2025-12-31 02:44:45', 'Vata-Kapha', 'Kapha', 'Manda'),
(7, 'ravi', 'rao', '1986-01-04', 'Male', 'ravi12@gmail.com', '9901023145', 'Unnamed Road, Peddajonna Varam, Andhra Pradesh 516175, India', '2026-01-04', '', '', '2026-01-04 13:40:07', NULL, NULL, NULL),
(8, 'jp', 'prakash', '2000-01-07', 'Male', 'prakash78@gmail.com', '6281508699', 'Unnamed Road, Peddajonna Varam, Andhra Pradesh 516175, India', '2026-01-07', '', '', '2026-01-07 12:34:03', NULL, NULL, NULL),
(9, 'abhi', 'abhi reddy', '2026-01-07', 'Male', 'abhi12@gmail.com', '6281508699', 'Unnamed Road, Peddajonna Varam, Andhra Pradesh 516175, India', '2026-01-08', '', '', '2026-01-07 17:05:58', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `prakriti`
--

CREATE TABLE `prakriti` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `vata` int(11) DEFAULT 0,
  `pitta` int(11) DEFAULT 0,
  `kapha` int(11) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prakriti`
--

INSERT INTO `prakriti` (`id`, `patient_id`, `vata`, `pitta`, `kapha`, `created_at`, `updated_at`) VALUES
(1, 1, 35, 40, 25, '2026-01-02 05:00:00', '2026-01-07 09:43:49'),
(2, 7, 45, 30, 25, '2026-01-03 08:45:00', '2026-01-07 09:43:49'),
(3, 1, 33, 42, 25, '2026-01-05 03:30:00', '2026-01-07 09:43:49');

-- --------------------------------------------------------

--
-- Table structure for table `prakriti_assessment`
--

CREATE TABLE `prakriti_assessment` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `vata` int(11) DEFAULT NULL,
  `pitta` int(11) DEFAULT NULL,
  `kapha` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prakriti_assessment`
--

INSERT INTO `prakriti_assessment` (`id`, `patient_id`, `vata`, `pitta`, `kapha`, `created_at`) VALUES
(1, 0, 50, 50, 50, '2026-01-04 07:41:45'),
(2, 1, 45, 30, 25, '2026-01-04 13:27:13'),
(3, 2, 25, 50, 25, '2026-01-04 13:27:13'),
(4, 3, 20, 25, 55, '2026-01-04 13:27:13'),
(5, 4, 40, 35, 25, '2026-01-04 13:27:13'),
(6, 5, 25, 45, 30, '2026-01-04 13:27:13'),
(7, 6, 35, 25, 40, '2026-01-04 13:27:13'),
(8, 2, 10, 10, 10, '2026-01-06 06:17:37'),
(9, 1, 10, 50, 40, '2026-01-07 09:36:57'),
(10, 1, 10, 50, 40, '2026-01-07 09:38:32'),
(11, 8, 36, 45, 69, '2026-01-07 12:34:36'),
(12, 9, 25, 45, 69, '2026-01-07 17:08:29'),
(13, 9, 25, 45, 69, '2026-01-07 17:11:11'),
(14, 3, 36, 69, 18, '2026-01-07 17:12:30'),
(15, 7, 56, 78, 69, '2026-01-07 17:39:33');

-- --------------------------------------------------------

--
-- Table structure for table `rasa_preferences`
--

CREATE TABLE `rasa_preferences` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `sweet` tinyint(4) DEFAULT NULL,
  `sour` tinyint(4) DEFAULT NULL,
  `salty` tinyint(4) DEFAULT NULL,
  `pungent` tinyint(4) DEFAULT NULL,
  `bitter` tinyint(4) DEFAULT NULL,
  `astringent` tinyint(4) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rasa_preferences`
--

INSERT INTO `rasa_preferences` (`id`, `patient_id`, `sweet`, `sour`, `salty`, `pungent`, `bitter`, `astringent`, `created_at`) VALUES
(1, 0, 1, 1, 0, 0, 0, 0, '2026-01-04 07:49:23'),
(2, 0, 1, 1, 0, 0, 0, 0, '2026-01-04 07:49:34'),
(3, 1, 1, 1, 1, 0, 0, 0, '2026-01-04 13:27:13'),
(4, 2, 0, 0, 0, 0, 1, 1, '2026-01-04 13:27:13'),
(5, 3, 0, 0, 0, 1, 1, 1, '2026-01-04 13:27:13'),
(6, 4, 1, 1, 0, 1, 0, 0, '2026-01-04 13:27:13'),
(7, 5, 0, 0, 1, 0, 1, 1, '2026-01-04 13:27:13'),
(8, 6, 1, 0, 0, 1, 0, 1, '2026-01-04 13:27:13'),
(9, 1, 1, 0, 0, 1, 0, 0, '2026-01-07 09:37:19'),
(10, 1, 0, 0, 1, 0, 0, 0, '2026-01-07 09:38:45'),
(11, 1, 1, 0, 1, 0, 0, 0, '2026-01-02 05:35:00'),
(12, 7, 0, 1, 0, 1, 1, 0, '2026-01-03 09:50:00'),
(13, 1, 1, 0, 1, 0, 0, 0, '2026-01-02 05:35:00'),
(14, 7, 0, 1, 0, 1, 1, 0, '2026-01-03 09:50:00'),
(15, 8, 0, 0, 0, 1, 0, 0, '2026-01-07 12:35:00'),
(16, 9, 1, 0, 0, 0, 0, 1, '2026-01-07 17:08:48'),
(17, 9, 0, 0, 0, 0, 1, 1, '2026-01-07 17:11:24'),
(18, 3, 0, 0, 0, 0, 0, 0, '2026-01-07 17:12:42'),
(19, 7, 0, 0, 0, 0, 0, 1, '2026-01-07 17:39:45');

-- --------------------------------------------------------

--
-- Table structure for table `recipes`
--

CREATE TABLE `recipes` (
  `id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `prakriti` varchar(50) DEFAULT NULL,
  `image_url` text DEFAULT NULL,
  `ingredients` text DEFAULT NULL,
  `nutrients` text DEFAULT NULL,
  `method` text DEFAULT NULL,
  `ayurvedic_notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipes`
--

INSERT INTO `recipes` (`id`, `title`, `category`, `prakriti`, `image_url`, `ingredients`, `nutrients`, `method`, `ayurvedic_notes`, `created_at`) VALUES
(1, 'Oatmeal with Berries', 'Breakfast', 'Vata', 'https://j29c1t02-80.inc1.devtunnels.ms/ayurvedic/images/VegetableBiryani.png', 'Oats, berries, milk, honey', 'Calories: 250, Protein: 8g', 'Cook oats with milk. Add berries and honey.', 'Good for calming Vata and improving digestion', '2025-12-31 04:06:46'),
(2, 'Vegetable Biryani', 'Lunch', 'Kapha', 'https://j29c1t02-80.inc1.devtunnels.ms/ayurvedic/images/VegetableBiryani.png', 'Rice, vegetables, spices', 'Calories: 420, Carbs: 60g', 'Cook rice separately. Mix with sautéed vegetables.', 'Balances Kapha when spices are moderate', '2025-12-31 04:06:46'),
(3, 'Khichdi', 'Main Course', 'All Doshas', 'images/recipes/khichdi.png', 'Rice 1 cup, Moong dal 1/2 cup, Ghee 2 tbsp, Turmeric 1/2 tsp, Cumin 1 tsp, Salt to taste, Water 4 cups', 'Calories: 350, Protein: 12g, Carbs: 55g, Fat: 8g', '1. Wash rice and dal together. 2. Heat ghee, add cumin and turmeric. 3. Add rice, dal and water. 4. Pressure cook for 3 whistles. 5. Serve hot with ghee.', 'The most sattvic and easily digestible food. Balances all three doshas. Ideal for detox (Panchakarma).', '2026-01-04 13:27:13'),
(4, 'Ginger-Lemon Tea', 'Beverage', 'Vata, Kapha', 'https://example.com/ginger-tea.jpg', 'Fresh ginger 1 inch, Lemon juice 1 tbsp, Honey 1 tsp, Water 1 cup', 'Calories: 30, Protein: 0g, Carbs: 8g, Fat: 0g', '1. Boil water with sliced ginger for 5 minutes. 2. Strain and let cool slightly. 3. Add lemon juice and honey. 4. Serve warm.', 'Excellent for improving Agni (digestive fire). Reduces ama (toxins). Best consumed in morning.', '2026-01-04 13:27:13'),
(5, 'Coconut Rice', 'Main Course', 'Pitta', 'https://example.com/coconut-rice.jpg', 'Basmati rice 1 cup, Coconut milk 1 cup, Mustard seeds 1 tsp, Curry leaves 10, Cashews 2 tbsp, Salt to taste', 'Calories: 380, Protein: 7g, Carbs: 52g, Fat: 16g', '1. Cook rice and let it cool. 2. In a pan, temper mustard seeds and curry leaves. 3. Add coconut milk and cashews. 4. Mix with rice. 5. Serve warm.', 'Cooling and nourishing. Excellent for Pitta pacification. Good for summer months.', '2026-01-04 13:27:13'),
(6, 'Mung Bean Soup', 'Soup', 'All Doshas', 'https://example.com/mung-soup.jpg', 'Mung beans 1 cup, Ghee 1 tbsp, Cumin 1 tsp, Ginger 1 inch, Turmeric 1/2 tsp, Coriander leaves, Salt', 'Calories: 220, Protein: 15g, Carbs: 35g, Fat: 4g', '1. Cook mung beans until soft. 2. Blend partially for texture. 3. Temper with ghee, cumin, ginger. 4. Add turmeric and salt. 5. Garnish with coriander.', 'Light and easy to digest. Excellent source of plant protein. Suitable for all body types.', '2026-01-04 13:27:13'),
(7, 'Vegetable Upma', 'Breakfast', 'Kapha', 'https://example.com/upma.jpg', 'Semolina 1 cup, Mixed vegetables 1 cup, Mustard seeds 1 tsp, Curry leaves 8, Green chili 1, Ghee 1 tbsp', 'Calories: 280, Protein: 8g, Carbs: 45g, Fat: 7g', '1. Dry roast semolina until golden. 2. Temper mustard, curry leaves, chili. 3. Add vegetables, sauté. 4. Add water, salt. 5. Add semolina, stir until thick.', 'Light and warming. Good for Kapha types. Avoid for high Pitta conditions.', '2026-01-04 13:27:13'),
(8, 'Golden Milk (Haldi Doodh)', 'Beverage', 'All Doshas', 'images/recipes/golden_milk.png', 'Milk 1 cup, Turmeric 1/2 tsp, Black pepper 1 pinch, Ghee 1/2 tsp, Honey 1 tsp (add when warm)', 'Calories: 120, Protein: 4g, Fat: 5g', '1. Warm milk with turmeric and pepper for 5 minutes.\n2. Add ghee and stir well.\n3. Let cool slightly below 40°C.\n4. Add honey (never add honey to boiling liquids).\n5. Drink before bed.', 'Powerful anti-inflammatory. Boosts immunity and Ojas. Promotes restful sleep. The combination of turmeric with black pepper increases absorption by 2000%. Best taken at night.', '2026-01-07 16:42:14'),
(9, 'Triphala Water', 'Beverage', 'All Doshas', 'https://example.com/triphala.jpg', 'Triphala powder 1 tsp, Warm water 1 glass (250ml)', 'Calories: 5, Fiber: 2g', '1. Add triphala powder to warm (not hot) water.\n2. Stir well until dissolved.\n3. Drink on empty stomach early morning.\n4. Wait 30-45 minutes before eating.', 'The king of Ayurvedic formulas. Balances all three doshas. Gentle detox and colon cleanser. Improves digestion and absorption. Regular use promotes longevity.', '2026-01-07 16:42:14'),
(10, 'CCF Tea (Cumin Coriander Fennel)', 'Beverage', 'All Doshas', 'images/recipes/ccf_tea.png', 'Cumin seeds 1/2 tsp, Coriander seeds 1/2 tsp, Fennel seeds 1/2 tsp, Water 2 cups', 'Calories: 10, Carbs: 2g', '1. Add all seeds to water.\n2. Bring to boil.\n3. Simmer for 5-7 minutes.\n4. Strain into thermos.\n5. Sip throughout the day, warm or at room temperature.', 'The perfect digestive aid tea. Reduces bloating and gas. Improves metabolism and Agni. Helps with water retention. Safe for pregnant women. Suitable for all body types.', '2026-01-07 16:42:14'),
(11, 'Tulsi Ginger Tea', 'Beverage', 'Vata, Kapha', 'https://example.com/tulsi-tea.jpg', 'Fresh tulsi leaves 10-12, Fresh ginger 1 inch, Honey 1 tsp, Water 1.5 cups', 'Calories: 25, Vitamin C: 5mg', '1. Crush tulsi leaves lightly.\n2. Slice ginger thinly.\n3. Boil water with ginger for 3 minutes.\n4. Add tulsi, simmer 2 minutes.\n5. Strain, cool slightly, add honey.', 'Powerful immunity booster. Clears respiratory passages. Anti-viral and anti-bacterial. Reduces stress and anxiety. Avoid excessive use in high Pitta.', '2026-01-07 16:42:14'),
(12, 'Poha (Flattened Rice)', 'Breakfast', 'Vata, Pitta', 'images/recipes/poha.png', 'Poha (flattened rice) 2 cups, Onion 1 medium, Potato 1 medium, Turmeric 1/2 tsp, Mustard seeds 1 tsp, Curry leaves 8-10, Peanuts 2 tbsp, Lemon juice 1 tbsp, Fresh coriander for garnish, Salt to taste', 'Calories: 320, Protein: 8g, Carbs: 55g, Fat: 10g', '1. Rinse poha gently, drain and set aside.\n2. Heat oil, add mustard seeds, let splutter.\n3. Add curry leaves, peanuts, sauté.\n4. Add cubed potato, cook until soft.\n5. Add turmeric and onion, sauté.\n6. Add poha, mix gently.\n7. Add salt, lemon juice, coriander.', 'Light, sattvic breakfast. Easy to digest. Good for Vata pacification due to warmth. The lemon adds digestive benefits. Avoid heavy portions for Kapha types.', '2026-01-07 16:42:14'),
(13, 'Ragi Porridge', 'Breakfast', 'Pitta, Kapha', 'images/recipes/ragi_porridge.png', 'Ragi flour 3 tbsp, Water 1.5 cups, Jaggery 1 tbsp, Cardamom powder 1/4 tsp, Ghee 1 tsp', 'Calories: 180, Protein: 4g, Carbs: 35g, Calcium: 350mg, Iron: 3mg', '1. Mix ragi flour with 1/2 cup cold water to make smooth paste (prevents lumps).\n2. Boil remaining water.\n3. Add ragi paste slowly, stirring continuously.\n4. Cook on medium flame 5-7 minutes.\n5. Add jaggery and cardamom.\n6. Serve warm with ghee on top.', 'Exceptional source of calcium - better than milk. Cooling for Pitta. Controls blood sugar - good for diabetics. Strengthens bones and teeth. The slow-release carbs keep you full longer.', '2026-01-07 16:42:14'),
(14, 'Moong Dal Chilla', 'Breakfast', 'All Doshas', 'images/recipes/moong_chilla.png', 'Yellow moong dal 1 cup (soaked 4 hours), Fresh ginger 1 inch, Green chili 1, Cumin seeds 1/2 tsp, Fresh coriander 2 tbsp, Salt to taste, Ghee for cooking', 'Calories: 220, Protein: 14g, Carbs: 32g, Fat: 3g, Fiber: 6g', '1. Drain soaked moong dal.\n2. Grind with ginger, chili, cumin and little water to smooth batter.\n3. Add salt and chopped coriander.\n4. Heat tawa, apply ghee.\n5. Spread thin like dosa.\n6. Cook both sides until golden.\n7. Serve with green chutney.', 'High protein breakfast that is easy to digest. True sattvic food. Suitable for all prakriti types. The fermentation-free preparation makes it light. Good for weight management.', '2026-01-07 16:42:14'),
(15, 'Upma', 'Breakfast', 'Kapha', 'https://example.com/upma.jpg', 'Semolina (rava) 1 cup, Mixed vegetables 1 cup, Mustard seeds 1 tsp, Urad dal 1 tsp, Chana dal 1 tsp, Curry leaves 10, Green chili 2, Ginger 1 inch, Ghee 2 tbsp, Cashews 10', 'Calories: 280, Protein: 8g, Carbs: 45g, Fat: 9g', '1. Dry roast semolina until golden and aromatic.\n2. Heat ghee, add mustard, urad dal, chana dal.\n3. Add curry leaves, chili, ginger, cashews.\n4. Add vegetables, sauté 3 minutes.\n5. Add 2.5 cups hot water, salt.\n6. When boiling, add semolina slowly, stirring.\n7. Cover and cook 3 minutes.', 'Light and warming breakfast. Excellent for Kapha types. The spices boost Agni. Avoid if you have high Pitta or acidity.', '2026-01-07 16:42:14'),
(16, 'Khichdi (The Healing Food)', 'Main Course', 'All Doshas', 'images/recipes/khichdi.png', 'Basmati rice 1 cup, Yellow moong dal 1/2 cup, Ghee 2 tbsp, Cumin seeds 1 tsp, Turmeric 1/2 tsp, Fresh ginger 1 inch grated, Hing (asafoetida) 1 pinch, Salt to taste, Water 4-5 cups, Fresh coriander for garnish', 'Calories: 380, Protein: 14g, Carbs: 58g, Fat: 10g, Fiber: 4g', '1. Wash rice and dal together 3-4 times.\n2. Soak for 30 minutes (optional).\n3. Heat ghee in pressure cooker, add cumin.\n4. Add turmeric, ginger, hing, sauté 30 seconds.\n5. Add rice, dal, salt and water.\n6. Pressure cook for 3-4 whistles.\n7. Let pressure release naturally.\n8. Serve with extra ghee and coriander.', 'The most healing food in Ayurveda. Balances all three doshas. The perfect food during illness, detox, or Panchakarma. Easy to digest while being nourishing. The rice-dal combination provides complete protein.', '2026-01-07 16:42:14'),
(17, 'Lauki Sabzi (Bottle Gourd)', 'Main Course', 'Pitta, Kapha', 'https://example.com/lauki.jpg', 'Bottle gourd 500g, Tomato 1 medium, Cumin seeds 1 tsp, Turmeric 1/2 tsp, Coriander powder 1 tsp, Ghee 1 tbsp, Green chili 1, Fresh coriander', 'Calories: 120, Protein: 3g, Carbs: 18g, Fat: 5g, Water: 92%', '1. Peel and cube bottle gourd.\n2. Heat ghee, add cumin.\n3. Add lauki, sauté 2 minutes.\n4. Add turmeric, coriander powder, chili.\n5. Add chopped tomato.\n6. Add 1/2 cup water, salt.\n7. Cover and cook until soft.\n8. Garnish with fresh coriander.', 'Extremely cooling vegetable. Excellent for Pitta imbalance. High water content hydrates body. Supports weight loss. Good for heart and kidneys. Avoid in cold weather or high Vata.', '2026-01-07 16:42:14'),
(18, 'Palak Dal (Spinach Lentils)', 'Main Course', 'All Doshas', 'images/recipes/palak_dal.png', 'Moong dal 1 cup, Fresh spinach 2 bunches (300g), Ghee 2 tbsp, Cumin seeds 1 tsp, Garlic 4 cloves, Turmeric 1/2 tsp, Green chili 1, Tomato 1, Garam masala 1/4 tsp', 'Calories: 280, Protein: 16g, Carbs: 38g, Fat: 8g, Iron: 8mg, Vitamin A: 150%', '1. Cook moong dal with turmeric until soft.\n2. Blanch spinach, blend to puree.\n3. Mix spinach into cooked dal.\n4. Heat ghee, add cumin, garlic.\n5. Add tomato, cook until soft.\n6. Add this tadka to dal.\n7. Simmer 5 minutes with garam masala.', 'Iron powerhouse. Excellent for anemia. Moong dal makes it easy to digest. Good for blood purification. Nourishing for all body types. The ghee helps absorb fat-soluble vitamins from spinach.', '2026-01-07 16:42:14'),
(19, 'Vegetable Korma', 'Main Course', 'Vata', 'https://example.com/korma.jpg', 'Mixed vegetables 2 cups, Cashews 15, Coconut 2 tbsp, Poppy seeds 1 tbsp, Green cardamom 3, Cloves 3, Cinnamon 1 inch, Bay leaf 1, Onion 1, Ghee 2 tbsp, Cream 2 tbsp', 'Calories: 350, Protein: 8g, Carbs: 30g, Fat: 22g', '1. Soak cashews, coconut, poppy seeds; grind to paste.\n2. Heat ghee, add whole spices.\n3. Add sliced onion, sauté golden.\n4. Add vegetables, sauté.\n5. Add nut paste and water.\n6. Simmer until vegetables are cooked.\n7. Add cream and salt.', 'Rich and grounding. Excellent for Vata dosha. The nuts and cream provide healthy fats. Warming spices aid digestion. Good for cold weather.', '2026-01-07 16:42:14'),
(20, 'Kadhi', 'Main Course', 'Vata', 'https://example.com/kadhi.jpg', 'Yogurt 1 cup, Besan (gram flour) 2 tbsp, Turmeric 1/2 tsp, Mustard seeds 1 tsp, Curry leaves 10, Fenugreek seeds 1/4 tsp, Asafoetida 1 pinch, Dried red chilies 2, Ghee 1 tbsp', 'Calories: 180, Protein: 8g, Carbs: 15g, Fat: 10g, Probiotics: Yes', '1. Whisk yogurt with besan, turmeric and 2 cups water.\n2. Bring to boil, then simmer 15 minutes.\n3. Heat ghee, add mustard seeds.\n4. Add fenugreek, curry leaves, chilies, asafoetida.\n5. Pour tadka into kadhi.\n6. Simmer 5 more minutes.', 'Warming and grounding for Vata. The besan prevents yogurt from curdling. Improves appetite and digestion. Best consumed at lunch. Avoid at night and in Kapha conditions.', '2026-01-07 16:42:14'),
(21, 'Rasam', 'Soup', 'Vata, Kapha', 'images/recipes/rasam.png', 'Tomatoes 3 medium, Tamarind 1 small ball, Rasam powder 1.5 tbsp, Turmeric 1/4 tsp, Mustard seeds 1 tsp, Cumin seeds 1/2 tsp, Curry leaves 10, Dried red chili 1, Garlic 3 cloves, Fresh coriander', 'Calories: 60, Protein: 2g, Carbs: 12g, Vitamin C: 25mg', '1. Cook tomatoes until soft, mash.\n2. Soak tamarind, extract juice.\n3. Mix tomato, tamarind, rasam powder, turmeric with water.\n4. Bring to boil.\n5. Heat oil, add mustard, cumin, curry leaves, chili, garlic.\n6. Add to rasam, garnish with coriander.', 'South Indian healing soup. Clears respiratory congestion. Boosts appetite when sick. Excellent digestive. The pepper and garlic are antibacterial. Avoid in high Pitta.', '2026-01-07 16:42:14'),
(22, 'Pumpkin Soup', 'Soup', 'Vata, Pitta', 'https://example.com/pumpkin-soup.jpg', 'Pumpkin 3 cups cubed, Onion 1, Fresh ginger 1 inch, Cumin seeds 1 tsp, Ghee 1 tbsp, Vegetable stock 2 cups, Coconut milk 1/2 cup, Nutmeg 1 pinch', 'Calories: 180, Protein: 4g, Carbs: 28g, Fat: 7g, Beta-carotene: High', '1. Roast pumpkin cubes with ghee until soft.\n2. Sauté onion and ginger.\n3. Blend pumpkin with onion, ginger, stock.\n4. Heat, add coconut milk.\n5. Season with cumin and nutmeg.', 'Sweet and nourishing. Highly grounding for Vata. Soothes aggravated Pitta. Rich in beta-carotene for eyes and skin. Warming for cold weather.', '2026-01-07 16:42:14'),
(23, 'Moong Soup', 'Soup', 'All Doshas', 'https://example.com/moong-soup.jpg', 'Whole green moong 1/2 cup, Cumin 1 tsp, Ginger 1 inch, Turmeric 1/4 tsp, Ghee 1 tbsp, Lemon juice 1 tbsp, Fresh coriander', 'Calories: 160, Protein: 10g, Carbs: 25g, Fiber: 5g', '1. Soak moong 4 hours or overnight.\n2. Pressure cook with turmeric until very soft.\n3. Blend partially (keep some texture).\n4. Temper with ghee, cumin, ginger.\n5. Add lemon juice and coriander.', 'The ultimate sattvic soup. Perfect during fasting or detox. Easy to digest with high protein. Suitable for all body types. Excellent for recovery from illness.', '2026-01-07 16:42:14'),
(24, 'Dates Almond Energy Balls', 'Snack', 'Vata', 'images/recipes/energy_balls.png', 'Medjool dates 10 (pitted), Almonds 1/4 cup, Cashews 1/4 cup, Cardamom powder 1/4 tsp, Ghee 1 tsp, Desiccated coconut 2 tbsp', 'Calories: 180 per ball, Protein: 5g, Carbs: 22g, Fat: 9g, Natural sugars', '1. Soak dates in warm water 10 minutes if dry.\n2. Blend almonds and cashews to powder.\n3. Add dates, cardamom, ghee; blend to sticky mass.\n4. Roll into small balls.\n5. Coat with coconut.\n6. Refrigerate 30 minutes.', 'Natural energy without sugar crash. Excellent for Vata types. Dates nourish reproductive tissues. Almonds build Ojas. Good for active people and mothers.', '2026-01-07 16:42:14'),
(25, 'Roasted Makhana (Fox Nuts)', 'Snack', 'All Doshas', 'images/recipes/makhana.png', 'Makhana (fox nuts) 2 cups, Ghee 1 tbsp, Turmeric 1/4 tsp, Black pepper 1/4 tsp, Rock salt to taste', 'Calories: 130, Protein: 4g, Carbs: 20g, Fat: 4g, Low GI', '1. Heat large pan, dry roast makhana on low heat.\n2. Stir continuously for 8-10 minutes until crisp.\n3. Add ghee, turmeric, pepper.\n4. Toss well to coat evenly.\n5. Add rock salt when slightly cool.', 'Light and sattvic snack. Safe for fasting. Kidney-friendly. Anti-aging properties. Low glycemic index - good for diabetics. Balances all doshas. Rich in antioxidants.', '2026-01-07 16:42:14'),
(26, 'Chana Chaat', 'Snack', 'Kapha, Pitta', 'https://example.com/chana-chaat.jpg', 'Boiled chickpeas 1 cup, Onion 1 chopped, Tomato 1 chopped, Cucumber 1/2 cup, Green chili 1, Chaat masala 1 tsp, Lemon juice 2 tbsp, Fresh coriander', 'Calories: 220, Protein: 12g, Carbs: 35g, Fiber: 8g', '1. Mix boiled chickpeas with onion, tomato, cucumber.\n2. Add chopped green chili.\n3. Sprinkle chaat masala.\n4. Add lemon juice.\n5. Garnish with coriander.\n6. Serve immediately.', 'High protein vegetarian snack. Raw vegetables add enzymes. Lemon aids iron absorption. Good for Kapha management. Avoid if Vata is high (due to raw elements).', '2026-01-07 16:42:14'),
(27, 'Vegetable Daliya (Broken Wheat)', 'Dinner', 'Kapha', 'https://example.com/daliya.jpg', 'Broken wheat (daliya) 1 cup, Mixed vegetables 1 cup, Ghee 1 tbsp, Cumin seeds 1 tsp, Turmeric 1/2 tsp, Fresh ginger 1 inch, Green peas 1/4 cup', 'Calories: 280, Protein: 8g, Carbs: 48g, Fiber: 8g', '1. Dry roast daliya in ghee until aromatic.\n2. Add cumin and ginger.\n3. Add chopped vegetables and peas.\n4. Add 3 cups water and turmeric.\n5. Pressure cook 2 whistles.\n6. Fluff and serve.', 'Perfect light dinner. High fiber keeps you full. Good for weight management. Heart-healthy. Best suited for Kapha types. Avoid if you have wheat sensitivity.', '2026-01-07 16:42:14'),
(28, 'Masoor Dal Tadka', 'Dinner', 'Vata, Pitta', 'https://example.com/masoor.jpg', 'Red masoor dal 1 cup, Ghee 2 tbsp, Cumin seeds 1 tsp, Garlic 4 cloves sliced, Dried red chili 2, Turmeric 1/2 tsp, Tomato 1, Fresh coriander', 'Calories: 260, Protein: 18g, Carbs: 40g, Fat: 6g, Iron: 6mg', '1. Wash dal, cook with turmeric and 3 cups water.\n2. Mash lightly when soft.\n3. Heat ghee, add cumin.\n4. Add garlic, fry until golden.\n5. Add red chilies and tomato.\n6. Pour tadka over dal.\n7. Garnish with coriander.', 'Quick-cooking dal, ready in 20 minutes. Good protein source for vegetarians. Warming nature suits Vata. The ghee makes it easier to digest. Light enough for dinner.', '2026-01-07 16:42:14'),
(29, 'Lauki Kofta Curry', 'Dinner', 'Pitta', 'https://example.com/lauki-kofta.jpg', 'Bottle gourd 2 cups grated, Besan 1/2 cup, Yogurt 1/2 cup, Tomato puree 1 cup, Cashew paste 2 tbsp, Garam masala 1/2 tsp, Cream 2 tbsp', 'Calories: 320, Protein: 10g, Carbs: 35g, Fat: 16g', '1. Squeeze water from grated lauki.\n2. Mix with besan, make small balls.\n3. Steam or shallow fry koftas.\n4. Make gravy with tomato, yogurt, cashew paste.\n5. Add garam masala and cream.\n6. Add koftas before serving.', 'Cooling main dish. Excellent for Pitta pacification. The lauki is highly nutritious. Rich and satisfying without being heavy. Good for summer dinners.', '2026-01-07 16:42:14'),
(30, 'Ajwain Water', 'Beverage', 'Vata, Kapha', 'https://example.com/ajwain.jpg', 'Carom seeds (ajwain) 1 tsp, Water 1 glass', 'Calories: 5', '1. Dry roast ajwain lightly (optional).\n2. Boil water with ajwain for 5 minutes.\n3. Strain.\n4. Drink warm after meals.', 'Powerful digestive. Excellent for bloating and gas. Reduces abdominal pain. Improves Agni. Anti-bacterial properties. Avoid in high Pitta conditions or pregnancy.', '2026-01-07 16:42:14'),
(31, 'Jeera Water', 'Beverage', 'All Doshas', 'https://example.com/jeera-water.jpg', 'Cumin seeds 1 tsp, Water 1 glass', 'Calories: 8', '1. Soak cumin in water overnight.\n2. Boil in morning for 5 minutes.\n3. Strain and drink warm.\n4. Can be consumed throughout day.', 'Balances all doshas. Excellent digestive. Helps with weight loss. Improves metabolism. Safe during pregnancy. Reduces water retention.', '2026-01-07 16:42:14'),
(32, 'Buttermilk (Chaas)', 'Beverage', 'Vata, Kapha', 'images/recipes/buttermilk.png', 'Fresh yogurt 1/4 cup, Water 1 cup, Roasted cumin powder 1/2 tsp, Rock salt to taste, Fresh coriander, Fresh mint leaves', 'Calories: 40, Protein: 2g, Probiotics: High', '1. Blend yogurt with water until smooth.\n2. Add roasted cumin and salt.\n3. Add chopped coriander and mint.\n4. Serve at room temperature (not chilled).', 'Excellent post-meal drink. Aids digestion. Light unlike yogurt. Contains probiotics for gut health. Best consumed at lunch. Avoid at night and in cold weather.', '2026-01-07 16:42:14');

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE `reports` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `report_type` varchar(50) NOT NULL,
  `from_date` date DEFAULT NULL,
  `to_date` date DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reports`
--

INSERT INTO `reports` (`id`, `patient_id`, `report_type`, `from_date`, `to_date`, `filename`, `created_at`) VALUES
(1, 1, 'diet_summary', '2025-12-01', '2025-12-31', 'anika_diet_dec2025.pdf', '2026-01-04 13:27:13'),
(2, 2, 'assessment', '2026-01-01', '2026-01-04', 'rohan_assessment_jan2026.pdf', '2026-01-04 13:27:13'),
(3, 1, 'progress', '2025-10-01', '2025-12-31', 'anika_progress_q4_2025.pdf', '2026-01-04 13:27:13'),
(4, 3, 'comprehensive', '2025-12-01', '2026-01-04', 'priya_comprehensive.pdf', '2026-01-04 13:27:13'),
(5, 7, 'diet_summary', '0000-00-00', '0000-00-00', 'report_7_20260106_092314.pdf', '2026-01-06 08:23:14'),
(6, 7, 'diet_summary', '0000-00-00', '0000-00-00', 'report_7_20260107_134215.pdf', '2026-01-07 12:42:15');

-- --------------------------------------------------------

--
-- Table structure for table `tasks`
--

CREATE TABLE `tasks` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `type` enum('followup','diet_review','assessment','custom') DEFAULT 'custom',
  `status` enum('pending','completed') DEFAULT 'pending',
  `due_date` date DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tasks`
--

INSERT INTO `tasks` (`id`, `patient_id`, `title`, `description`, `type`, `status`, `due_date`, `created_at`, `updated_at`) VALUES
(1, 1, 'Monthly Diet Review', 'Review diet plan effectiveness', 'diet_review', 'completed', '2026-01-07', '2026-01-04 12:55:12', '2026-01-07 16:57:24'),
(2, 1, 'Follow-up Appointment', 'Check progress on weight management', 'followup', 'completed', '2026-01-11', '2026-01-04 12:55:12', '2026-01-07 09:06:20'),
(3, 2, 'Prakriti Assessment', 'Complete initial assessment', 'assessment', 'completed', '2026-01-05', '2026-01-04 12:55:12', '2026-01-07 16:57:20'),
(4, 2, 'Diet Plan Creation', 'Create personalized diet chart', 'custom', 'completed', '2026-01-02', '2026-01-04 12:55:12', '2026-01-04 12:55:12'),
(5, 3, 'Thyroid Check-up', 'Monthly thyroid level monitoring', 'followup', 'completed', '2026-01-10', '2026-01-04 13:27:13', '2026-01-07 09:06:20'),
(6, 4, 'Cholesterol Review', 'Check lipid profile after diet changes', 'assessment', 'completed', '2026-01-15', '2026-01-04 13:27:13', '2026-01-07 09:06:20'),
(7, 5, 'GERD Diet Adjustment', 'Review anti-reflux diet effectiveness', 'diet_review', 'completed', '2026-01-08', '2026-01-04 13:27:13', '2026-01-07 09:06:20'),
(8, 6, 'Arthritis Pain Assessment', 'Evaluate anti-inflammatory diet impact', 'followup', 'completed', '2026-01-12', '2026-01-04 13:27:13', '2026-01-07 09:06:20');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `clinic` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `phone`, `password`, `clinic`) VALUES
(1, 'kumar', 'kumar@gmail.com', '8056109669', '$2y$10$iuxAtytWB5P9x.awu.V8K.LtRhLWj6dDoSJdjnCUt1mMps2XyP8Na', 'kumarclinic'),
(2, 'siva charan', 'sivacharan9999@gmail.com', '6281508600', '$2y$10$p1.3D5nEsSAYbwKbuEtpweYTwHGdRq4gpUgUXsjeyFinbm/AmGYoW', 'Ayurvedic '),
(8, 'Timmareddy Prem Kumar Reddy', 'tprem6565@gmail.com', '+18985545407', '$2y$10$Y7USaAK9lov.3bCZHn/UiOhQ068bufuuksqOi7TY7n6tahKk/tjua', 'ayurvedic'),
(9, 'Siva charan', 'jarubulasivacharan174@gmail.com', '+1919113673562', '$2y$10$qHbrPHVvIqGuAX5UWw10relLOJnaeU0JpWKDrFKomDm5CUq0MMoAa', 'ayurvedic'),
(10, 'sivacharan', 'jscharan9999@gmail.com', '6281508600', '$2y$10$Cg4ooGJO7xWV8xu3rR0Aa.QO1oU5D7XF46OT2.gR.qOq8R2L36qzC', 'SHIVA CLEANIC'),
(11, 'raju', 'raju78@gmaio.com', '9281685694', '$2y$10$z84OXNeCMjqS2kM8bgqEY.1Ha0BUq7hQAKLxaOhszmarAyRRiZ2ua', 'ayurvedic'),
(12, 'prem', 'premkumart1087.sse@saveetha.com', '7842219470', '$2y$10$92PEqv1zBsMzhCDWBiJ6bO./RRdttf3D5y5D/Im3gmVZrsgAJxGDW', 'vayu');

-- --------------------------------------------------------

--
-- Table structure for table `user_preferences`
--

CREATE TABLE `user_preferences` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `preferred_language` varchar(20) DEFAULT 'English',
  `diet_system` enum('ayurveda_only','integrated','modern') DEFAULT 'integrated',
  `measurement_unit` enum('metric','imperial') DEFAULT 'metric',
  `calorie_display` tinyint(1) DEFAULT 1,
  `dosha_alerts` tinyint(1) DEFAULT 1,
  `conflict_warnings` tinyint(1) DEFAULT 1,
  `seasonal_suggestions` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_preferences`
--

INSERT INTO `user_preferences` (`id`, `user_id`, `preferred_language`, `diet_system`, `measurement_unit`, `calorie_display`, `dosha_alerts`, `conflict_warnings`, `seasonal_suggestions`, `created_at`, `updated_at`) VALUES
(1, 1, 'English', 'integrated', 'metric', 1, 1, 1, 1, '2026-01-06 05:18:11', '2026-01-06 05:18:11'),
(2, 2, 'English', 'ayurveda_only', 'metric', 1, 1, 1, 1, '2026-01-06 05:18:11', '2026-01-06 05:18:11'),
(3, 8, 'English', 'integrated', 'metric', 1, 1, 1, 1, '2026-01-06 05:18:11', '2026-01-06 05:18:11'),
(4, 9, 'English', 'ayurveda_only', 'metric', 1, 1, 1, 1, '2026-01-06 05:18:11', '2026-01-06 05:18:11');

-- --------------------------------------------------------

--
-- Table structure for table `vikriti`
--

CREATE TABLE `vikriti` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `vata` int(11) DEFAULT 0,
  `pitta` int(11) DEFAULT 0,
  `kapha` int(11) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vikriti`
--

INSERT INTO `vikriti` (`id`, `patient_id`, `vata`, `pitta`, `kapha`, `created_at`, `updated_at`) VALUES
(1, 1, 50, 35, 15, '2026-01-02 05:05:00', '2026-01-07 09:43:49'),
(2, 7, 40, 45, 15, '2026-01-03 08:50:00', '2026-01-07 09:43:49'),
(3, 1, 55, 30, 15, '2026-01-05 03:35:00', '2026-01-07 09:43:49');

-- --------------------------------------------------------

--
-- Table structure for table `vikriti_assessment`
--

CREATE TABLE `vikriti_assessment` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `vata` int(11) DEFAULT NULL,
  `pitta` int(11) DEFAULT NULL,
  `kapha` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vikriti_assessment`
--

INSERT INTO `vikriti_assessment` (`id`, `patient_id`, `vata`, `pitta`, `kapha`, `created_at`) VALUES
(1, 1, 55, 25, 20, '2026-01-04 13:27:13'),
(2, 2, 30, 55, 15, '2026-01-04 13:27:13'),
(3, 3, 25, 20, 55, '2026-01-04 13:27:13'),
(4, 4, 50, 30, 20, '2026-01-04 13:27:13'),
(5, 5, 30, 50, 20, '2026-01-04 13:27:13'),
(6, 6, 40, 20, 40, '2026-01-04 13:27:13'),
(7, 1, 20, 30, 50, '2026-01-07 09:37:05'),
(8, 1, 50, 20, 30, '2026-01-07 09:38:39'),
(9, 8, 25, 69, 45, '2026-01-07 12:34:51'),
(10, 9, 25, 45, 69, '2026-01-07 17:08:37'),
(11, 9, 25, 45, 69, '2026-01-07 17:11:17'),
(12, 3, 12, 69, 13, '2026-01-07 17:12:37'),
(13, 7, 25, 48, 98, '2026-01-07 17:39:40');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `agni`
--
ALTER TABLE `agni`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_patient_id` (`patient_id`);

--
-- Indexes for table `agni_assessment`
--
ALTER TABLE `agni_assessment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `clinical_alerts`
--
ALTER TABLE `clinical_alerts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_alerts_patient` (`patient_id`),
  ADD KEY `idx_alerts_type` (`type`);

--
-- Indexes for table `clinics`
--
ALTER TABLE `clinics`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `dietary_habits`
--
ALTER TABLE `dietary_habits`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_patient_dietary` (`patient_id`);

--
-- Indexes for table `diet_charts`
--
ALTER TABLE `diet_charts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_diet_charts_patient` (`patient_id`),
  ADD KEY `idx_diet_charts_status` (`status`);

--
-- Indexes for table `diet_chart_items`
--
ALTER TABLE `diet_chart_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `diet_chart_id` (`diet_chart_id`),
  ADD KEY `food_id` (`food_id`);

--
-- Indexes for table `digestive_strength`
--
ALTER TABLE `digestive_strength`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dosha_food_suggestions`
--
ALTER TABLE `dosha_food_suggestions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `food_id` (`food_id`);

--
-- Indexes for table `foods`
--
ALTER TABLE `foods`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_foods_category` (`category`),
  ADD KEY `idx_foods_rasa` (`rasa`);

--
-- Indexes for table `food_conflicts`
--
ALTER TABLE `food_conflicts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `food1_id` (`food1_id`),
  ADD KEY `food2_id` (`food2_id`);

--
-- Indexes for table `food_contraindications`
--
ALTER TABLE `food_contraindications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_patient_id` (`patient_id`);

--
-- Indexes for table `lifestyle_inputs`
--
ALTER TABLE `lifestyle_inputs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_patient_lifestyle` (`patient_id`);

--
-- Indexes for table `medical_history`
--
ALTER TABLE `medical_history`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_patient_medical` (`patient_id`);

--
-- Indexes for table `medical_records`
--
ALTER TABLE `medical_records`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_patient_visit` (`patient_id`,`visit_date`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prakriti`
--
ALTER TABLE `prakriti`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_patient_id` (`patient_id`);

--
-- Indexes for table `prakriti_assessment`
--
ALTER TABLE `prakriti_assessment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rasa_preferences`
--
ALTER TABLE `rasa_preferences`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `recipes`
--
ALTER TABLE `recipes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_reports_patient` (`patient_id`);

--
-- Indexes for table `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `idx_tasks_status` (`status`),
  ADD KEY `idx_tasks_due` (`due_date`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `user_preferences`
--
ALTER TABLE `user_preferences`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_user_pref` (`user_id`);

--
-- Indexes for table `vikriti`
--
ALTER TABLE `vikriti`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_patient_id` (`patient_id`);

--
-- Indexes for table `vikriti_assessment`
--
ALTER TABLE `vikriti_assessment`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `agni`
--
ALTER TABLE `agni`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `agni_assessment`
--
ALTER TABLE `agni_assessment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `appointments`
--
ALTER TABLE `appointments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `clinical_alerts`
--
ALTER TABLE `clinical_alerts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `clinics`
--
ALTER TABLE `clinics`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `dietary_habits`
--
ALTER TABLE `dietary_habits`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `diet_charts`
--
ALTER TABLE `diet_charts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `diet_chart_items`
--
ALTER TABLE `diet_chart_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `digestive_strength`
--
ALTER TABLE `digestive_strength`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `dosha_food_suggestions`
--
ALTER TABLE `dosha_food_suggestions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `foods`
--
ALTER TABLE `foods`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `food_conflicts`
--
ALTER TABLE `food_conflicts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `food_contraindications`
--
ALTER TABLE `food_contraindications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `lifestyle_inputs`
--
ALTER TABLE `lifestyle_inputs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `medical_history`
--
ALTER TABLE `medical_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `medical_records`
--
ALTER TABLE `medical_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `prakriti`
--
ALTER TABLE `prakriti`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `prakriti_assessment`
--
ALTER TABLE `prakriti_assessment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `rasa_preferences`
--
ALTER TABLE `rasa_preferences`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `recipes`
--
ALTER TABLE `recipes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `reports`
--
ALTER TABLE `reports`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tasks`
--
ALTER TABLE `tasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `user_preferences`
--
ALTER TABLE `user_preferences`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `vikriti`
--
ALTER TABLE `vikriti`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `vikriti_assessment`
--
ALTER TABLE `vikriti_assessment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agni`
--
ALTER TABLE `agni`
  ADD CONSTRAINT `agni_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `clinical_alerts`
--
ALTER TABLE `clinical_alerts`
  ADD CONSTRAINT `clinical_alerts_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `clinics`
--
ALTER TABLE `clinics`
  ADD CONSTRAINT `clinics_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `dietary_habits`
--
ALTER TABLE `dietary_habits`
  ADD CONSTRAINT `dietary_habits_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `diet_charts`
--
ALTER TABLE `diet_charts`
  ADD CONSTRAINT `diet_charts_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `diet_chart_items`
--
ALTER TABLE `diet_chart_items`
  ADD CONSTRAINT `diet_chart_items_ibfk_1` FOREIGN KEY (`diet_chart_id`) REFERENCES `diet_charts` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `diet_chart_items_ibfk_2` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `dosha_food_suggestions`
--
ALTER TABLE `dosha_food_suggestions`
  ADD CONSTRAINT `dosha_food_suggestions_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `food_conflicts`
--
ALTER TABLE `food_conflicts`
  ADD CONSTRAINT `food_conflicts_ibfk_1` FOREIGN KEY (`food1_id`) REFERENCES `foods` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `food_conflicts_ibfk_2` FOREIGN KEY (`food2_id`) REFERENCES `foods` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `food_contraindications`
--
ALTER TABLE `food_contraindications`
  ADD CONSTRAINT `food_contraindications_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `lifestyle_inputs`
--
ALTER TABLE `lifestyle_inputs`
  ADD CONSTRAINT `lifestyle_inputs_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `medical_history`
--
ALTER TABLE `medical_history`
  ADD CONSTRAINT `medical_history_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `medical_records`
--
ALTER TABLE `medical_records`
  ADD CONSTRAINT `fk_medical_records_patient` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `prakriti`
--
ALTER TABLE `prakriti`
  ADD CONSTRAINT `prakriti_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `reports`
--
ALTER TABLE `reports`
  ADD CONSTRAINT `reports_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `tasks`
--
ALTER TABLE `tasks`
  ADD CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `user_preferences`
--
ALTER TABLE `user_preferences`
  ADD CONSTRAINT `user_preferences_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `vikriti`
--
ALTER TABLE `vikriti`
  ADD CONSTRAINT `vikriti_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
