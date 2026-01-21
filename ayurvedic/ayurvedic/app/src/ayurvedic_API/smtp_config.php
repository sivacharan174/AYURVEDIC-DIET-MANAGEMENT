<?php
/**
 * SMTP Configuration for AyurNutrition Email Service
 * 
 * Configure these settings with your Gmail credentials.
 * Note: Gmail requires an "App Password" if 2FA is enabled.
 * 
 * To generate App Password:
 * 1. Go to Google Account → Security
 * 2. Enable 2-Step Verification
 * 3. Go to App passwords
 * 4. Generate a new app password for "Mail"
 */

// Gmail SMTP Settings
define('SMTP_HOST', 'smtp.gmail.com');
define('SMTP_PORT', 587);
define('SMTP_SECURE', 'tls'); // Use 'tls' for port 587, 'ssl' for port 465

// Your Gmail credentials
define('SMTP_USERNAME', 'tprem6565@gmail.com');  // Replace with your Gmail
define('SMTP_PASSWORD', 'rrsb ohom ybmi yrar');      // Replace with App Password

// Sender information
define('SMTP_FROM_EMAIL', 'tprem6565@gmail.com'); // Same as SMTP_USERNAME
define('SMTP_FROM_NAME', 'Ayurnutrition');

// Debug mode (set to 0 for production, 2 for verbose debugging)
define('SMTP_DEBUG', 2);
?>
