-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: hex_database
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `beds`
--

DROP TABLE IF EXISTS `beds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beds` (
  `HospitalID` int NOT NULL,
  `ICU_Available` int NOT NULL,
  `Bed_Available` int NOT NULL,
  PRIMARY KEY (`HospitalID`),
  CONSTRAINT `fk_beds_hospital` FOREIGN KEY (`HospitalID`) REFERENCES `hospital` (`HospitalID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beds`
--

LOCK TABLES `beds` WRITE;
/*!40000 ALTER TABLE `beds` DISABLE KEYS */;
INSERT INTO `beds` VALUES (1,20,110),(2,50,500),(3,40,800),(4,25,500),(5,80,300),(6,110,1500);
/*!40000 ALTER TABLE `beds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_keeper`
--

DROP TABLE IF EXISTS `book_keeper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_keeper` (
  `Book` mediumtext COLLATE utf8mb4_general_ci,
  `Library` mediumtext COLLATE utf8mb4_general_ci,
  `Availability` text COLLATE utf8mb4_general_ci,
  `Price` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_keeper`
--

LOCK TABLES `book_keeper` WRITE;
/*!40000 ALTER TABLE `book_keeper` DISABLE KEYS */;
INSERT INTO `book_keeper` VALUES ('The Steves','Royal Library','Yes',500),('Cook for Apple','National Library','No',900);
/*!40000 ALTER TABLE `book_keeper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_records`
--

DROP TABLE IF EXISTS `health_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_records` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `record_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `data` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `health_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_records`
--

LOCK TABLES `health_records` WRITE;
/*!40000 ALTER TABLE `health_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `healthtest`
--

DROP TABLE IF EXISTS `healthtest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `healthtest` (
  `TestName` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `HospitalID` int NOT NULL,
  `Availability` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Available',
  `TestFees` double NOT NULL,
  KEY `idx_healthtest` (`TestName`,`HospitalID`),
  KEY `fk_healthtest_hospital` (`HospitalID`),
  CONSTRAINT `fk_healthtest_hospital` FOREIGN KEY (`HospitalID`) REFERENCES `hospital` (`HospitalID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `healthtest`
--

LOCK TABLES `healthtest` WRITE;
/*!40000 ALTER TABLE `healthtest` DISABLE KEYS */;
INSERT INTO `healthtest` VALUES ('Ultrasound',1,'Yes',1080),('Ultrasound',2,'Yes',350),('Ultrasound',3,'Yes',400),('Ultrasound',4,'Yes',300),('Ultrasound',5,'Yes',500),('MRI',1,'Yes',30),('MRI',2,'Yes',50),('MRI',3,'Yes',30),('MRI',4,'Yes',50),('MRI',5,'Yes',20),('COVID-19 PCR',1,'Yes',60),('COVID-19 PCR',2,'Yes',50),('COVID-19 PCR',3,'Yes',70),('COVID-19 PCR',4,'Yes',90),('COVID-19 PCR',5,'Yes',50.5),('Blood Culture Test',1,'Yes',80),('Blood Culture Test',2,'Yes',70),('Blood Culture Test',3,'Yes',87),('Blood Culture Test',4,'Yes',100),('Blood Culture Test',5,'Yes',100),('X-Ray',1,'Yes',100),('X-Ray',2,'Yes',58),('X-Ray',3,'Yes',61.25),('X-Ray',4,'Yes',59.99),('X-Ray',5,'Yes',50.55);
/*!40000 ALTER TABLE `healthtest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hiretutors`
--

DROP TABLE IF EXISTS `hiretutors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hiretutors` (
  `Name` mediumtext COLLATE utf8mb4_general_ci,
  `Subjects` mediumtext COLLATE utf8mb4_general_ci,
  `Class` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Expected Salary` decimal(20,0) DEFAULT NULL,
  `Email` text COLLATE utf8mb4_general_ci NOT NULL,
  `Adminverified` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hiretutors`
--

LOCK TABLES `hiretutors` WRITE;
/*!40000 ALTER TABLE `hiretutors` DISABLE KEYS */;
INSERT INTO `hiretutors` VALUES ('Akmal Zaman','Science','10',10000,'',1),('MD Karim','Science','8',6000,'',1),('Hasib Rahman','Commerce','9',7000,'',0),('Md Ahbab Hamid Khan','Physcis','10, 11, 12',12000,'',1),('Amzad Karim','Higher Maths','11, 12',6000,'karimbhai@gmail.com',1);
/*!40000 ALTER TABLE `hiretutors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hospital`
--

DROP TABLE IF EXISTS `hospital`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hospital` (
  `HospitalID` int NOT NULL,
  `HospitalName` text COLLATE utf8mb4_general_ci NOT NULL,
  `Location` mediumtext COLLATE utf8mb4_general_ci NOT NULL,
  `HasBlood` mediumtext COLLATE utf8mb4_general_ci NOT NULL,
  `HasPharmacy` tinytext COLLATE utf8mb4_general_ci NOT NULL,
  `ICUAvailability` text COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`HospitalID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hospital`
--

LOCK TABLES `hospital` WRITE;
/*!40000 ALTER TABLE `hospital` DISABLE KEYS */;
INSERT INTO `hospital` VALUES (1,'City general','New York City, New York','Yes','Yes','No'),(2,'Metro Hospital','Los Angeles, California','Yes','Yes','Yes'),(3,'Greenfield Clinic','Chicago, Illinois','Yes','Yes','Yes'),(4,'Sunrise Medical','Houston, Texax','Yes','Yes','Yes'),(5,'Oceanview Hospital','Miami, Florida','Yes','Yes','Yes'),(6,'Cedars Sinai','Orlando, Florida','Yes','Yes','Yes');
/*!40000 ALTER TABLE `hospital` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `message` text COLLATE utf8mb4_general_ci NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_read` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_messages_sender` (`sender_id`),
  KEY `fk_messages_receiver` (`receiver_id`),
  CONSTRAINT `fk_messages_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,1,8,'asfcas','2025-10-03 21:44:43',0);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_slot`
--

DROP TABLE IF EXISTS `parking_slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_slot` (
  `Slot` int NOT NULL,
  `Location` mediumtext COLLATE utf8mb4_general_ci NOT NULL,
  `Area` mediumtext COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`Slot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_slot`
--

LOCK TABLES `parking_slot` WRITE;
/*!40000 ALTER TABLE `parking_slot` DISABLE KEYS */;
INSERT INTO `parking_slot` VALUES (1,'Shohid Faruk Road','Jatrabari'),(2,'Malibag RailLine','Malibag'),(3,'Police Plaza Shopping Complex','Gulshan -2');
/*!40000 ALTER TABLE `parking_slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `location` text COLLATE utf8mb4_general_ci,
  `role` enum('USER','ADMIN') COLLATE utf8mb4_general_ci DEFAULT 'USER',
  `department` enum('HEALTH','EDUCATION','OTHERS') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Md Ahbab Hamid Khan','123456','159/A, Senpara Parbata, Mirpur -10, Dhaka','USER',NULL,'2025-10-03 21:21:27',NULL),(2,'Sarah Johnson','pass123','42 Elm Street, Springfield','USER',NULL,'2025-10-03 21:21:27',NULL),(3,'Alex Wang','secret456','789 Pine Avenue, Sunnydale','USER',NULL,'2025-10-03 21:21:27',NULL),(4,'Education Admin 1','Education Admin 1',NULL,'ADMIN','EDUCATION','2025-10-03 21:21:27',NULL),(5,'Education Admin 2','Education Admin 2',NULL,'ADMIN','EDUCATION','2025-10-03 21:21:27',NULL),(6,'Health Admin 1','Health Admin 1',NULL,'ADMIN','HEALTH','2025-10-03 21:21:27',NULL),(7,'Health Admin 2','Health Admin 2',NULL,'ADMIN','HEALTH','2025-10-03 21:21:27',NULL),(8,'Others Admin 1','Others Admin 1',NULL,'ADMIN','OTHERS','2025-10-03 21:21:27',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-23 22:32:58
