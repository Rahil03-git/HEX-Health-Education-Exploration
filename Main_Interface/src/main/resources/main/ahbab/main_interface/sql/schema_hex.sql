-- Unified H.E.X Database Schema
-- Run this in MySQL to initialize the compact database used by the app

DROP DATABASE IF EXISTS hex_database;
CREATE DATABASE IF NOT EXISTS hex_database DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE hex_database;

-- Users (both normal users and admins)
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  location TEXT,
  role ENUM('USER','ADMIN') DEFAULT 'USER',
  department ENUM('HEALTH','EDUCATION','OTHERS') NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_login TIMESTAMP NULL
);

-- Messages between users (IDs reference users.id)
CREATE TABLE IF NOT EXISTS messages (
  id INT AUTO_INCREMENT PRIMARY KEY,
  sender_id INT NOT NULL,
  receiver_id INT NOT NULL,
  message TEXT NOT NULL,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_read BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_messages_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_messages_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Health domain
CREATE TABLE IF NOT EXISTS hospital (
  HospitalID INT NOT NULL PRIMARY KEY,
  HospitalName TEXT NOT NULL,
  Location MEDIUMTEXT NOT NULL,
  HasBlood MEDIUMTEXT NOT NULL,
  HasPharmacy TINYTEXT NOT NULL,
  ICUAvailability TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS beds (
  HospitalID INT NOT NULL PRIMARY KEY,
  ICU_Available INT NOT NULL,
  Bed_Available INT NOT NULL,
  CONSTRAINT fk_beds_hospital FOREIGN KEY (HospitalID) REFERENCES hospital(HospitalID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS healthtest (
  TestName VARCHAR(50) NOT NULL,
  HospitalID INT NOT NULL,
  Availability VARCHAR(50) NOT NULL DEFAULT 'Available',
  TestFees DOUBLE NOT NULL,
  INDEX idx_healthtest (TestName, HospitalID),
  CONSTRAINT fk_healthtest_hospital FOREIGN KEY (HospitalID) REFERENCES hospital(HospitalID) ON DELETE CASCADE
);

-- Parking
CREATE TABLE IF NOT EXISTS parking_slot (
  Slot INT NOT NULL PRIMARY KEY,
  Location MEDIUMTEXT NOT NULL,
  Area MEDIUMTEXT NOT NULL
);

-- Education domain
CREATE TABLE IF NOT EXISTS book_keeper (
  Book MEDIUMTEXT,
  Library MEDIUMTEXT,
  Availability TEXT,
  Price INT NOT NULL
);

CREATE TABLE IF NOT EXISTS hiretutors (
  Name MEDIUMTEXT,
  Subjects MEDIUMTEXT,
  Class VARCHAR(10),
  `Expected Salary` DECIMAL(20,0),
  Email TEXT NOT NULL,
  Adminverified TINYINT(1) NOT NULL DEFAULT 0
);

-- Seed data (optional but useful for dev/testing)
INSERT IGNORE INTO users (username, password, location, role, department) VALUES
('Md Ahbab Hamid Khan', '123456', '159/A, Senpara Parbata, Mirpur -10, Dhaka', 'USER', NULL),
('Sarah Johnson', 'pass123', '42 Elm Street, Springfield', 'USER', NULL),
('Alex Wang', 'secret456', '789 Pine Avenue, Sunnydale', 'USER', NULL),
('Education Admin 1', 'Education Admin 1', NULL, 'ADMIN', 'EDUCATION'),
('Education Admin 2', 'Education Admin 2', NULL, 'ADMIN', 'EDUCATION'),
('Health Admin 1', 'Health Admin 1', NULL, 'ADMIN', 'HEALTH'),
('Health Admin 2', 'Health Admin 2', NULL, 'ADMIN', 'HEALTH'),
('Others Admin 1', 'Others Admin 1', NULL, 'ADMIN', 'OTHERS');

INSERT IGNORE INTO hospital (HospitalID, HospitalName, Location, HasBlood, HasPharmacy, ICUAvailability) VALUES
(1, 'City general', 'New York City, New York', 'Yes', 'Yes', 'No'),
(2, 'Metro Hospital', 'Los Angeles, California', 'Yes', 'Yes', 'Yes'),
(3, 'Greenfield Clinic', 'Chicago, Illinois', 'Yes', 'Yes', 'Yes'),
(4, 'Sunrise Medical', 'Houston, Texax', 'Yes', 'Yes', 'Yes'),
(5, 'Oceanview Hospital', 'Miami, Florida', 'Yes', 'Yes', 'Yes'),
(6, 'Cedars Sinai', 'Orlando, Florida', 'Yes', 'Yes', 'Yes');

INSERT IGNORE INTO beds (HospitalID, ICU_Available, Bed_Available) VALUES
(1, 20, 110),
(2, 50, 500),
(3, 40, 800),
(4, 25, 500),
(5, 80, 300),
(6, 110, 1500);

INSERT IGNORE INTO healthtest (TestName, HospitalID, Availability, TestFees) VALUES
('Ultrasound', 1, 'Yes', 1080),
('Ultrasound', 2, 'Yes', 350),
('Ultrasound', 3, 'Yes', 400),
('Ultrasound', 4, 'Yes', 300),
('Ultrasound', 5, 'Yes', 500),
('MRI', 1, 'Yes', 30),
('MRI', 2, 'Yes', 50),
('MRI', 3, 'Yes', 30),
('MRI', 4, 'Yes', 50),
('MRI', 5, 'Yes', 20),
('COVID-19 PCR', 1, 'Yes', 60),
('COVID-19 PCR', 2, 'Yes', 50),
('COVID-19 PCR', 3, 'Yes', 70),
('COVID-19 PCR', 4, 'Yes', 90),
('COVID-19 PCR', 5, 'Yes', 50.5),
('Blood Culture Test', 1, 'Yes', 80),
('Blood Culture Test', 2, 'Yes', 70),
('Blood Culture Test', 3, 'Yes', 87),
('Blood Culture Test', 4, 'Yes', 100),
('Blood Culture Test', 5, 'Yes', 100),
('X-Ray', 1, 'Yes', 100),
('X-Ray', 2, 'Yes', 58),
('X-Ray', 3, 'Yes', 61.25),
('X-Ray', 4, 'Yes', 59.99),
('X-Ray', 5, 'Yes', 50.55);

INSERT IGNORE INTO parking_slot (Slot, Location, Area) VALUES
(1, 'Shohid Faruk Road', 'Jatrabari'),
(2, 'Malibag RailLine', 'Malibag'),
(3, 'Police Plaza Shopping Complex', 'Gulshan -2');

INSERT IGNORE INTO book_keeper (Book, Library, Availability, Price) VALUES
('The Steves', 'Royal Library', 'Yes', 500),
('Cook for Apple', 'National Library', 'No', 900);

INSERT IGNORE INTO hiretutors (Name, Subjects, Class, `Expected Salary`, Email, Adminverified) VALUES
('Akmal Zaman', 'Science', '10', 10000, '', 1),
('MD Karim', 'Science', '8', 6000, '', 0),
('Hasib Rahman', 'Commerce', '9', 7000, '', 0),
('Md Ahbab Hamid Khan', 'Physcis', '10, 11, 12', 12000, '', 1),
('Amzad Karim', 'Higher Maths', '11, 12', 6000, 'karimbhai@gmail.com', 1);

