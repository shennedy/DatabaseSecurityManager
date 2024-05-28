-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 25, 2024 at 11:28 PM
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
-- Database: `secdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `sdb_group`
--

CREATE TABLE `sdb_group` (
  `GrpCode` char(4) NOT NULL,
  `GrpName` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sdb_group`
--

INSERT INTO `sdb_group` (`GrpCode`, `GrpName`) VALUES
('1', 'Group1');

-- --------------------------------------------------------

--
-- Table structure for table `sdb_groupresaccess`
--

CREATE TABLE `sdb_groupresaccess` (
  `GRA_GrpCode` char(4) NOT NULL,
  `GRA_SeqN` decimal(3,0) NOT NULL,
  `GRA_ResCode` char(8) DEFAULT NULL,
  `GRA_RightsList` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sdb_resource`
--

CREATE TABLE `sdb_resource` (
  `ResCode` char(8) NOT NULL,
  `ResName` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sdb_user`
--

CREATE TABLE `sdb_user` (
  `Usr_Code` char(8) NOT NULL,
  `Usr_Name` varchar(30) DEFAULT NULL,
  `Usr_Pass` varchar(20) DEFAULT NULL,
  `Usr_PassChgDays` decimal(2,0) DEFAULT NULL,
  `Usr_PassChgDate` decimal(8,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sdb_user`
--

INSERT INTO `sdb_user` (`Usr_Code`, `Usr_Name`, `Usr_Pass`, `Usr_PassChgDays`, `Usr_PassChgDate`) VALUES
('1', 'Sam', '123', 0, 4172024),
('2', 'Brady', '667', 0, 4222024),
('3', 'James', '444', 0, 4242024);

-- --------------------------------------------------------

--
-- Table structure for table `sdb_usrgrpaccess`
--

CREATE TABLE `sdb_usrgrpaccess` (
  `UGA_UsrCode` char(8) NOT NULL,
  `UGA_GrpCode` char(4) DEFAULT NULL,
  `UGA_SeqN` decimal(3,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sdb_usrresaccess`
--

CREATE TABLE `sdb_usrresaccess` (
  `URA_UsrCode` char(8) NOT NULL,
  `URA_SeqN` decimal(3,0) NOT NULL,
  `URA_ResCode` char(8) DEFAULT NULL,
  `URA_RightsList` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `sdb_group`
--
ALTER TABLE `sdb_group`
  ADD PRIMARY KEY (`GrpCode`);

--
-- Indexes for table `sdb_groupresaccess`
--
ALTER TABLE `sdb_groupresaccess`
  ADD PRIMARY KEY (`GRA_GrpCode`,`GRA_SeqN`),
  ADD KEY `GRA_ResCode` (`GRA_ResCode`);

--
-- Indexes for table `sdb_resource`
--
ALTER TABLE `sdb_resource`
  ADD PRIMARY KEY (`ResCode`);

--
-- Indexes for table `sdb_user`
--
ALTER TABLE `sdb_user`
  ADD PRIMARY KEY (`Usr_Code`);

--
-- Indexes for table `sdb_usrgrpaccess`
--
ALTER TABLE `sdb_usrgrpaccess`
  ADD PRIMARY KEY (`UGA_UsrCode`,`UGA_SeqN`),
  ADD KEY `UGA_GrpCode` (`UGA_GrpCode`);

--
-- Indexes for table `sdb_usrresaccess`
--
ALTER TABLE `sdb_usrresaccess`
  ADD PRIMARY KEY (`URA_UsrCode`,`URA_SeqN`),
  ADD KEY `URA_ResCode` (`URA_ResCode`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `sdb_groupresaccess`
--
ALTER TABLE `sdb_groupresaccess`
  ADD CONSTRAINT `sdb_groupresaccess_ibfk_1` FOREIGN KEY (`GRA_GrpCode`) REFERENCES `sdb_group` (`GrpCode`),
  ADD CONSTRAINT `sdb_groupresaccess_ibfk_2` FOREIGN KEY (`GRA_ResCode`) REFERENCES `sdb_resource` (`ResCode`);

--
-- Constraints for table `sdb_usrgrpaccess`
--
ALTER TABLE `sdb_usrgrpaccess`
  ADD CONSTRAINT `sdb_usrgrpaccess_ibfk_1` FOREIGN KEY (`UGA_UsrCode`) REFERENCES `sdb_user` (`Usr_Code`),
  ADD CONSTRAINT `sdb_usrgrpaccess_ibfk_2` FOREIGN KEY (`UGA_GrpCode`) REFERENCES `sdb_group` (`GrpCode`);

--
-- Constraints for table `sdb_usrresaccess`
--
ALTER TABLE `sdb_usrresaccess`
  ADD CONSTRAINT `sdb_usrresaccess_ibfk_1` FOREIGN KEY (`URA_UsrCode`) REFERENCES `sdb_user` (`Usr_Code`),
  ADD CONSTRAINT `sdb_usrresaccess_ibfk_2` FOREIGN KEY (`URA_ResCode`) REFERENCES `sdb_resource` (`ResCode`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
