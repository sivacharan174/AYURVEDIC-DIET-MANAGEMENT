-- Missing Database Tables for Ayurvedic App
-- Run this SQL in phpMyAdmin to create missing tables

USE ayurvedic;

-- =============================================
-- PRAKRITI TABLE (Constitution Assessment)
-- =============================================
CREATE TABLE IF NOT EXISTS prakriti (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    vata INT DEFAULT 0,
    pitta INT DEFAULT 0,
    kapha INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- VIKRITI TABLE (Current Imbalance Assessment)
-- =============================================
CREATE TABLE IF NOT EXISTS vikriti (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    vata INT DEFAULT 0,
    pitta INT DEFAULT 0,
    kapha INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- AGNI TABLE (Digestive Fire Assessment)
-- =============================================
CREATE TABLE IF NOT EXISTS agni (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    agni_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- FOOD CONTRAINDICATIONS TABLE
-- =============================================
CREATE TABLE IF NOT EXISTS food_contraindications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    milk TINYINT(1) DEFAULT 0,
    curd TINYINT(1) DEFAULT 0,
    honey TINYINT(1) DEFAULT 0,
    radish TINYINT(1) DEFAULT 0,
    fruits TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Show created tables
SHOW TABLES;
