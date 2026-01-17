-- Local test database bootstrap for SIS
-- Create DB and load tables/data in dependency order

CREATE DATABASE IF NOT EXISTS sis_db;
USE sis_db;

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
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `dept_id` int NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(100) NOT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Computer Science'),(2,'Electronics'),(3,'Mechanical Engineering'),(4,'Civil Engineering'),(5,'Business Administration');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colleges`
--

DROP TABLE IF EXISTS `colleges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `colleges` (
  `college_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `college_name` varchar(150) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` int DEFAULT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `status` enum('APPROVED','PENDING','REJECTED') DEFAULT NULL,
  PRIMARY KEY (`college_id`),
  UNIQUE KEY `UKfxbkgllcqsq3qrcgwwr9r7gg8` (`college_name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colleges`
--

LOCK TABLES `colleges` WRITE;
/*!40000 ALTER TABLE `colleges` DISABLE KEYS */;
INSERT INTO `colleges` VALUES (1,'85 Lakefront Avenue, Chicago, USA','Lakeside University','2025-11-17 14:46:45.117703',NULL,'https://example.com/logos/lakeside.png','APPROVED'),(2,'77 Marine Research Lane, Sydney, Australia','Oceanside Institute of Science','2025-11-17 15:12:43.529655',NULL,'https://example.com/logos/oceanside.png','APPROVED'),(3,'Channasandra, Bengaluru','RNS Institute of Technology','2025-11-17 18:13:02.436974',NULL,'https://www.rnsit.ac.in/wp-content/themes/rnsit/img/sticky-header-logo.png','APPROVED'),(5,'blore','PESIT','2025-11-18 04:55:10.210945',NULL,'','APPROVED'),(6,'77 Marine Research Lane, Sydney, Australia','BMS Institute of Science','2025-11-18 05:29:51.652156',NULL,'https://example.com/logos/oceanside.png','APPROVED'),(7,'77 Marine Research Lane, Sydney, Australia','RV Institute of Science','2025-11-24 13:02:01.047593',NULL,'https://example.com/logos/oceanside.png','APPROVED'),(8,'Blore India','Ramaiah College','2025-11-25 06:01:20.436931',NULL,'https://example.com/logos/oceanside.png','APPROVED');
/*!40000 ALTER TABLE `colleges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','STUDENT','SUPERADMIN') NOT NULL,
  `status` enum('APPROVED','PENDING','REJECTED') DEFAULT NULL,
  `college_id` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKq8c77pl7fllv195wbwqn13375` (`college_id`),
  CONSTRAINT `FKq8c77pl7fllv195wbwqn13375` FOREIGN KEY (`college_id`) REFERENCES `colleges` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2025-11-17 14:13:30.000000','superadmin@sis.com','Super Admin','$2a$10$JFKbTs9dUjjlrZlUUYBXeO7phgKupZo/TfZ1ahvkqCAatHjAHXqhq','SUPERADMIN','APPROVED',NULL),(3,'2025-11-17 14:45:35.473118','emily.carter@lakesideuniv.org','Emily Carter','$2a$10$cSDz2rDL9KLUPcaNRvUWc.QHtfmDiHdJGDyMilb6RzMx6aNVsJXk.','ADMIN','APPROVED',1),(5,'2025-11-17 15:12:35.603721','sam@oceanside.edu','Daniel Kim','$2a$10$hg5yy9358oqLDMNC5qsbDOBZocCmi8HzggmFB8AoAPstAQpfluoWC','ADMIN','APPROVED',2),(6,'2025-11-17 15:13:14.859335','admin@rns.edu','RNS Admin','$2a$10$6nl0LUnnasgcoDFqkRysiOXs.yws2Aa9vdcjD9AM4Xpsxcj2Zqisi','ADMIN','REJECTED',NULL),(7,'2025-11-17 18:12:34.898500','rnsadmin@rnsit.ac.in','RNS Admin','$2a$10$qBfBkftaf1ketmzRWYMTo.TUrno/EeNDZybCgooMJhQkZzjgxN90y','ADMIN','APPROVED',3),(8,'2025-11-17 18:13:48.112441','admin@pes.edu','PES Admin','$2a$10$/CzUlW9zPS47NJ.Ufi6iT.rRw1eIIXGRo2qLH/yourn8Mwgzgu6H6','ADMIN','APPROVED',NULL),(9,'2025-11-17 19:00:24.176135','san@rnsit.ac.in','Santhosh A','$2a$10$UUKs.TKGFES4c9or/u82C.Oqb9ntRq.AYNbniYVg8LKMnh5RaUhWG','STUDENT','APPROVED',3),(10,'2025-11-18 03:46:51.962663','pes@edu.in','PES admin','$2a$10$lQWBgdom0GAaxnz07USi5.DQo6LVGiHZlrykaiVyaObQhHWSCSPby','ADMIN','APPROVED',5),(11,'2025-11-18 05:29:12.631649','admin@bms.edu','BMS Admin','$2a$10$GNNH3p71QgnX1fiYp9B0K.JJS9v3uuqLlcmRZJDJ91TQRmSgTjJ5i','ADMIN','APPROVED',6),(12,'2025-11-18 05:33:47.611020','rv@bms.edu','RV Admin','$2a$10$nEskTNMVikSZZJyl5NrdHOQ23JspeKp5Bt3WB58OqjIYlWc0yh.3S','ADMIN','REJECTED',NULL),(13,'2025-11-18 07:19:09.532741','sjbit@bms.edu','sjbit Admin','$2a$10$12oWM8YL0XTNoXE/smdjhO8BlDFiZ71fKSqYK8cujYV4m/lQqOlnq','ADMIN','APPROVED',7),(14,'2025-11-24 12:29:06.033345','ramya@gmail.com','ramya','$2a$10$z4GWBxXfum.r1NLihUnW/.DXAIEB94E25AB1GlyRuk3hrertU0kEu','STUDENT','APPROVED',3),(15,'2025-11-24 12:34:53.967624','sham@gmail.com','SHAM','$2a$10$ISNUuCmcmEevo4YWlPpFT.TNwonUqbQLQgVneqCtp6.3z9w8YaozS','STUDENT','APPROVED',3),(16,'2025-11-25 05:18:49.897834','ramaihadmin@gmail.com','Ramaiah Admin','$2a$10$VgsK.Vb85JHBmhEtQ/GH/.RNKd6I8wxfro2nLgB9LPVbMmyDx7pqW','ADMIN','APPROVED',8),(17,'2025-11-25 05:19:34.332635','jssadmin@gmail.com','JSS Admin','$2a$10$j1MuzHdL2Gw5ofkUT4nXGufJ/zSjs6vI7TPwROYyS2q012E/xqgse','ADMIN','REJECTED',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `college_requests`
--

DROP TABLE IF EXISTS `college_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `college_requests` (
  `request_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `approval_date` datetime(6) DEFAULT NULL,
  `college_name` varchar(150) NOT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `request_date` datetime(6) DEFAULT NULL,
  `status` enum('APPROVED','PENDING','REJECTED') NOT NULL,
  `approved_by` int DEFAULT NULL,
  `requested_by` int NOT NULL,
  PRIMARY KEY (`request_id`),
  KEY `FKcxw78n90ocawoqctcj3ujjyyu` (`approved_by`),
  KEY `FK4ngjpiodghevbalk79sa7pk70` (`requested_by`),
  CONSTRAINT `FK4ngjpiodghevbalk79sa7pk70` FOREIGN KEY (`requested_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKcxw78n90ocawoqctcj3ujjyyu` FOREIGN KEY (`approved_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `college_requests`
--

LOCK TABLES `college_requests` WRITE;
/*!40000 ALTER TABLE `college_requests` DISABLE KEYS */;
INSERT INTO `college_requests` VALUES (1,'85 Lakefront Avenue, Chicago, USA','2025-11-17 14:46:48.471205','Lakeside University','https://example.com/logos/lakeside.png','2025-11-17 14:45:36.862705','APPROVED',1,3),(2,'77 Marine Research Lane, Sydney, Australia','2025-11-17 15:12:46.079224','Oceanside Institute of Science','https://example.com/logos/oceanside.png','2025-11-17 15:12:35.951161','APPROVED',1,5),(3,'77 Marine Research Lane, Sydney, Australia','2025-11-17 15:13:41.744940','RNS Institute of Science','https://example.com/logos/oceanside.png','2025-11-17 15:13:15.122142','REJECTED',1,6),(4,'Channasandra, Bengaluru','2025-11-17 18:13:03.070398','RNS Institute of Technology','https://www.rnsit.ac.in/wp-content/themes/rnsit/img/sticky-header-logo.png','2025-11-17 18:12:35.211032','APPROVED',1,7),(6,'blore','2025-11-18 04:55:13.026603','PESIT','','2025-11-18 03:46:54.510932','APPROVED',1,10),(7,'77 Marine Research Lane, Sydney, Australia','2025-11-18 05:29:54.846356','BMS Institute of Science','https://example.com/logos/oceanside.png','2025-11-18 05:29:14.003931','APPROVED',1,11),(8,'77 Marine Research Lane, Sydney, Australia','2025-11-18 05:34:21.789781','RV Institute of Science','https://example.com/logos/oceanside.png','2025-11-18 05:33:48.199189','REJECTED',1,12),(9,'77 Marine Research Lane, Sydney, Australia','2025-11-24 13:02:03.278210','RV Institute of Science','https://example.com/logos/oceanside.png','2025-11-18 07:19:11.908265','APPROVED',1,13),(10,'Blore India','2025-11-25 06:01:21.369553','Ramaiah College','https://example.com/logos/oceanside.png','2025-11-25 05:18:50.391813','APPROVED',1,16),(11,'Blore India','2025-11-25 06:01:36.998382','JSS College','https://example.com/logos/oceanside.png','2025-11-25 05:19:34.677431','REJECTED',1,17);
/*!40000 ALTER TABLE `college_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `student_id` varchar(15) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `approval_status` enum('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
  `created_at` datetime(6) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `college_id` int DEFAULT NULL,
  `dept_id` int DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `UKg4fwvutq09fjdlb4bb0byp7t` (`user_id`),
  KEY `FK5m8jbc0pre8wg5u8wfgylnfv7` (`college_id`),
  KEY `FKjqf570o03a1ixomcf4opukjcs` (`dept_id`),
  CONSTRAINT `FK5m8jbc0pre8wg5u8wfgylnfv7` FOREIGN KEY (`college_id`) REFERENCES `colleges` (`college_id`),
  CONSTRAINT `FKdt1cjx5ve5bdabmuuf3ibrwaq` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKjqf570o03a1ixomcf4opukjcs` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('7fda18c6-4594-4','JP Nagar, Bangalore','APPROVED','2025-11-24 12:29:06.513069','8660684201','2025-11-24 12:29:06.513069',3,2,14),('cecb36d6-7c91-4','padmanabhanagar, bangalore - 70','APPROVED','2025-11-17 19:00:24.316558','123456789','2025-11-17 19:01:10.758820',3,1,9),('cfa5affb-50fa-4','MG Road bangalore','APPROVED','2025-11-24 12:34:54.463685','8660770987','2025-11-24 12:34:54.463685',3,1,15);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `course_id` int NOT NULL AUTO_INCREMENT,
  `course_name` varchar(100) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `credits` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `seat_limit` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `college_id` int NOT NULL,
  `created_by` int DEFAULT NULL,
  `dept_id` int DEFAULT NULL,
  PRIMARY KEY (`course_id`),
  KEY `FKoq3thf0kavtm1uigtqxpu5o8t` (`college_id`),
  KEY `FK4u40nf46n1nqa5h38sn5g17ac` (`created_by`),
  KEY `FK96f4mk6bnjrgp9laqqdkwvcjs` (`dept_id`),
  CONSTRAINT `FK4u40nf46n1nqa5h38sn5g17ac` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK96f4mk6bnjrgp9laqqdkwvcjs` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`),
  CONSTRAINT `FKoq3thf0kavtm1uigtqxpu5o8t` FOREIGN KEY (`college_id`) REFERENCES `colleges` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (8,'Introduction to Programming','2025-11-24 11:56:55.737808',3,'Covers programming fundamentals using Python, including variables, loops, functions, and problem-solving techniques.','2026-01-05',60,'2025-11-24',3,7,1),(9,'Data Structures & Algorithms','2025-11-24 11:58:44.832803',4,'Focuses on algorithm design, time complexity, and data structures like stacks, queues, trees, and graphs.','2026-01-12',60,'2025-11-24',3,7,1),(10,'Database Management Systems','2025-11-24 11:59:37.916427',3,'Introduction to relational databases, SQL queries, normalization, transactions, and DB architecture.','2026-01-19',60,'2025-11-24',3,7,1),(12,'Digital Electronics','2025-11-24 12:16:02.614070',4,'Covers logic gates, combinational circuits, sequential circuits, flip-flops, counters, and number systems','2026-02-23',59,'2025-12-01',3,7,2),(13,'Operating Systems','2025-11-24 12:17:40.404050',4,'Introduces process management, memory management, file systems, threading, deadlocks, and OS architectures with practical examples.','2026-02-16',60,'2025-12-01',3,7,1),(14,'Signals & Systems','2025-11-24 12:19:08.037981',4,'Continuous and discrete-time signals, convolution, Fourier series, Fourier transform, and LTI system analysis.','2026-02-17',60,'2025-11-24',3,7,2),(15,'Engineering Mechanics','2025-11-24 12:19:53.630852',3,'Statics, dynamics, friction, force systems, equilibrium, and structural basics.','2026-02-10',60,'2025-11-24',3,7,3),(16,'Fluid Mechanics','2025-11-24 12:20:45.794262',4,'Fluid properties, pressure, laminar/turbulent flow, Bernoulli’s equation, and fluid machinery concepts.','2026-02-16',60,'2025-12-01',3,7,3),(17,'Structural Analysis','2025-11-24 12:22:05.839730',4,'Analysis of beams, trusses, frames, and loads using classical and modern methods.','2026-02-01',46,'2025-12-01',3,7,4),(18,'Construction Materials','2025-11-24 12:22:48.400179',4,'Concrete, steel, timber, building materials testing, mix design, and material properties.','2026-02-23',60,'2025-11-24',3,7,4),(19,'Principles of Management  ','2025-11-24 12:23:29.949790',4,'Planning, organizing, staffing, directing, controlling, and managerial decision-making.','2026-02-23',60,'2025-12-01',3,7,5),(20,'Marketing Management ','2025-11-24 12:24:11.870274',3,'Market research, branding, consumer behavior, product strategies, and digital marketing fundamentals.','2026-02-23',60,'2025-12-01',3,7,5),(21,'Networking','2025-11-25 06:12:44.255756',4,'networks','2025-03-13',60,'2025-02-04',3,7,1);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollments` (
  `enrollment_id` int NOT NULL AUTO_INCREMENT,
  `approval_date` datetime(6) DEFAULT NULL,
  `requested_at` datetime(6) DEFAULT NULL,
  `status` enum('APPROVED','REJECTED','REQUESTED') NOT NULL,
  `approved_by` int DEFAULT NULL,
  `course_id` int NOT NULL,
  `student_id` varchar(15) NOT NULL,
  PRIMARY KEY (`enrollment_id`),
  KEY `FK61msounc7ovex8f7muqqjds7b` (`approved_by`),
  KEY `FKho8mcicp4196ebpltdn9wl6co` (`course_id`),
  KEY `FK8kf1u1857xgo56xbfmnif2c51` (`student_id`),
  CONSTRAINT `FK61msounc7ovex8f7muqqjds7b` FOREIGN KEY (`approved_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK8kf1u1857xgo56xbfmnif2c51` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `FKho8mcicp4196ebpltdn9wl6co` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollments`
--

LOCK TABLES `enrollments` WRITE;
/*!40000 ALTER TABLE `enrollments` DISABLE KEYS */;
INSERT INTO `enrollments` VALUES (4,'2025-11-24 12:14:00.009320','2025-11-24 12:14:00.009320','APPROVED',NULL,8,'cecb36d6-7c91-4'),(6,'2025-11-24 12:30:22.678249','2025-11-24 12:30:22.679274','APPROVED',NULL,12,'7fda18c6-4594-4'),(7,'2025-11-24 12:32:27.407666','2025-11-24 12:32:27.407666','APPROVED',NULL,9,'cecb36d6-7c91-4'),(8,'2025-11-24 12:35:27.700929','2025-11-24 12:35:27.700928','APPROVED',NULL,8,'cfa5affb-50fa-4'),(9,'2025-11-24 12:35:37.028855','2025-11-24 12:35:37.028854','APPROVED',NULL,9,'cfa5affb-50fa-4');
/*!40000 ALTER TABLE `enrollments` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
