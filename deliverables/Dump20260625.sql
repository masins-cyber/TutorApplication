-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: tutorapplication_db
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `lesson_id` int DEFAULT NULL,
  `student_email` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT 'booked',
  PRIMARY KEY (`booking_id`),
  KEY `lesson_id` (`lesson_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (14,25,'maso20@gmail.com','rejected'),(20,34,'fili@gmail.com','accepted'),(21,31,'fili@gmail.com','rejected'),(22,35,'student_1782136945165@test.it','booked'),(23,36,'student_1782137603871@test.it','booked'),(29,23,'maso20@gmail.com','rejected'),(30,25,'maso20@gmail.com','accepted'),(33,42,'maso20@gmail.com','rejected'),(36,49,'student_1782300747275@test.it','booked'),(37,50,'maso20@gmail.com','rejected'),(39,42,'maso20@gmail.com','accepted'),(40,57,'student_1782311439409@test.it','booked'),(41,50,'maso20@gmail.com','accepted'),(42,61,'student_1782317104774@test.it','booked'),(43,62,'student_1782317415086@test.it','booked');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lessons`
--

DROP TABLE IF EXISTS `lessons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lessons` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subject` varchar(100) NOT NULL,
  `day` varchar(20) NOT NULL,
  `time_slot` varchar(20) NOT NULL,
  `price` double NOT NULL,
  `tutor_email` varchar(255) NOT NULL,
  `available` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_lesson_slot` (`tutor_email`,`day`,`time_slot`),
  CONSTRAINT `fk_tutor_lessons` FOREIGN KEY (`tutor_email`) REFERENCES `users` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lessons`
--

LOCK TABLES `lessons` WRITE;
/*!40000 ALTER TABLE `lessons` DISABLE KEYS */;
INSERT INTO `lessons` VALUES (23,'geometry','MONDAY','10-12',35,'simix@gmail.com',1),(25,'maths','THURSDAY','10-12',40,'simix@gmail.com',0),(26,'physics','WEDNESDAY','10-12',30,'simix@gmail.com',1),(27,'maths','FRIDAY','10-11',25,'max@gmail.com',1),(30,'geometry','THURSDAY','10-12',25,'max@gmail.com',1),(31,'geometry','THURSDAY','17-18',30,'bea@gmail.com',1),(33,'maths','TUESDAY','17-18',25,'bea@gmail.com',1),(34,'theory','MONDAY','10-11',24,'bea@gmail.com',0),(35,'Test','MONDAY','T_5165',50,'simix@gmail.com',0),(36,'Test','MONDAY','T_3871',50,'simix@gmail.com',0),(40,'geometry','MONDAY','10-11',40,'simix@gmail.com',1),(42,'geometry','MONDAY','11-12',40,'simix@gmail.com',0),(48,'theory','FRIDAY','10-11',40,'simix@gmail.com',1),(49,'Test','MONDAY','T_7275',50,'simix@gmail.com',0),(50,'maths','SUNDAY','10-11',25,'simix@gmail.com',0),(53,'maths','MONDAY','17-18',40,'simix@gmail.com',1),(57,'Test','MONDAY','T_9409',50,'simix@gmail.com',0),(59,'test','TUESDAY','13-14',25,'simix@gmail.com',1),(61,'Test','MONDAY','T_4775',50,'simix@gmail.com',0),(62,'Test','MONDAY','T_5086',50,'simix@gmail.com',0);
/*!40000 ALTER TABLE `lessons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `role` varchar(20) NOT NULL,
  `student_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('bea@gmail.com','$2a$10$A.RYq04HFYh9.AvYenzSs.Fir3AylzsoJaM3oWYtBJz3CJSEHuG06','Beatrice','Maricchiolo','TUTOR',NULL),('cigno@gmail.com','$2a$10$zYxa0bxdWt4r8rsyRWp.beBPTTl4hVdzVV3LvruWpb6IGFvcWCwuO','Valerio','Cignitti','STUDENT','00111'),('fili@gmail.com','$2a$10$xz4TQudI7jvsAnHHK96ZDeRhSrf4V4UYWQm0IHoOcafoaYANOmxu6','Filippo','Savi','STUDENT','0310543'),('filips@gmail.com','$2a$10$BkBgsvNb6re35h74Qzp8p.KzKx2TRQJo.RXq0Dv88MrAcXLf7HGPG','Filippo','Savi','STUDENT','0410731'),('gemex@gmail.com','$2a$10$JrfTlmcTqjqATuWqXKOSZON1YUYyPGshKEl2Z2dsE2W/oNOjoyBwC','Matteo','Pinfildi','TUTOR',NULL),('lelex@gmail.com','$2a$10$IJedjAdzr59zvHURN14LPOUtrpC30ita6X.MX4ynlUIRetiK3gzx2','Emanuele','Italiani','TUTOR',NULL),('maso20@gmail.com','$2a$10$Xm16sE2Q95eQOVNA3Rx4nOZTWf8Lm6utKekyKmX2s43EkWimbMjFy','Matteo','Masini','STUDENT','0310935'),('max@gmail.com','$2a$10$gRJZibRk8IiFayd9dKpKqe4x3tUXl7yuJ6hcPTNOhzN3ad2Kca5nO','Massimiliano','Masini','TUTOR',NULL),('simix@gmail.com','$2a$10$jk4rZxKM49JWF6No5Mn1zuaBT9BO9lhFmiogdTe0ZbP0iswvsKBE6','Simone','Piras','TUTOR',NULL),('test@gmail.com','$2a$10$OO9uMxTwBLcP8gbAq6STvuQBcvC7OG1Am2/IiDDPOhxeyDuOPEXoq','Test','Testi','TUTOR',NULL),('tutor_1782061905920@test.it','$2a$10$E3k4keMEJI/4ffZvo4LmAe5Q/YsdjYMl30bHjhHXawxlMHJnagCMW','LeBron','James','TUTOR',NULL),('tutor_1782062022603@test.it','$2a$10$d2Vghp2G7YezgpZ8RCut2e6Yv4yluemus.bheQihBcSZbMU5KH23S','LeBron','James','TUTOR',NULL),('tutor_1782062062334@test.it','$2a$10$tzm6xfgGqeGR6Iq8.3WjdeMGHNg8NG23aFEY7wA4MQp14EjXaLPBy','LeBron','James','TUTOR',NULL);
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

-- Dump completed on 2026-06-25 11:06:11
