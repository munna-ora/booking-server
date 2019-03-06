-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 02, 2019 at 04:23 PM
-- Server version: 5.7.9
-- PHP Version: 5.6.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ora_booking`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking_info`
--

DROP TABLE IF EXISTS `booking_info`;
CREATE TABLE IF NOT EXISTS `booking_info` (
  `booking_info_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `checkin_pref_time` varchar(255) DEFAULT NULL,
  `checkout_pref_time` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gstin` varchar(255) DEFAULT NULL,
  `id_file_url` varchar(255) DEFAULT NULL,
  `identity_id` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `booking_id` bigint(20) NOT NULL,
  PRIMARY KEY (`booking_info_id`),
  KEY `FKo1b17kgui5bkhpme9y5n1hnnd` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `booking_vs_payment`
--

DROP TABLE IF EXISTS `booking_vs_payment`;
CREATE TABLE IF NOT EXISTS `booking_vs_payment` (
  `booking_vs_payment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `amount_paid` varchar(255) DEFAULT NULL,
  `orderAmount` varchar(255) DEFAULT NULL,
  `orderId` varchar(255) DEFAULT NULL,
  `other_info` varchar(255) DEFAULT NULL,
  `paymentMode` varchar(255) DEFAULT NULL,
  `percentage` varchar(255) DEFAULT NULL,
  `referenceId` varchar(255) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `txMsg` varchar(255) DEFAULT NULL,
  `txStatus` varchar(255) DEFAULT NULL,
  `txTime` varchar(255) DEFAULT NULL,
  `booking_id` bigint(20) NOT NULL,
  `gateway_id` bigint(20) NOT NULL,
  PRIMARY KEY (`booking_vs_payment_id`),
  KEY `FK7o4e18jl8hkm5t6t1xs00gtr8` (`booking_id`),
  KEY `FKs3qn4233lkx83a4rimjvfs563` (`gateway_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `booking_vs_room`
--

DROP TABLE IF EXISTS `booking_vs_room`;
CREATE TABLE IF NOT EXISTS `booking_vs_room` (
  `booking_vs_room_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `cgst` varchar(255) DEFAULT NULL,
  `gst_amt` varchar(255) DEFAULT NULL,
  `host_base_price` varchar(255) DEFAULT NULL,
  `host_discount` varchar(255) DEFAULT NULL,
  `host_price` varchar(255) DEFAULT NULL,
  `igst` varchar(255) DEFAULT NULL,
  `num_of_adult` varchar(255) DEFAULT NULL,
  `num_of_child` varchar(255) DEFAULT NULL,
  `num_of_cot` varchar(255) DEFAULT NULL,
  `num_of_shared_bed` varchar(255) DEFAULT NULL,
  `num_of_shared_cot` varchar(255) DEFAULT NULL,
  `ora_discount` varchar(255) DEFAULT NULL,
  `ora_final_price` varchar(255) DEFAULT NULL,
  `ora_mark_up` varchar(255) DEFAULT NULL,
  `ora_price` varchar(255) DEFAULT NULL,
  `ora_room_name` varchar(255) DEFAULT NULL,
  `room_id` varchar(255) DEFAULT NULL,
  `sgst` varchar(255) DEFAULT NULL,
  `total_amt` varchar(255) DEFAULT NULL,
  `booking_id` bigint(20) NOT NULL,
  `sac_code_id` bigint(20) NOT NULL,
  PRIMARY KEY (`booking_vs_room_id`),
  KEY `FK5ufallrpmt0lxf4fgp4u0vudm` (`booking_id`),
  KEY `FKdeiy7awthvtj76bw7kiescxqp` (`sac_code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `cancellation_vs_room`
--

DROP TABLE IF EXISTS `cancellation_vs_room`;
CREATE TABLE IF NOT EXISTS `cancellation_vs_room` (
  `cancellation_vs_room_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `cancellation_slab_id` varchar(255) DEFAULT NULL,
  `booking_vs_room_id` bigint(20) NOT NULL,
  `cancellation_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cancellation_vs_room_id`),
  KEY `FK6swa3fl9vk3sx659lb3j0n095` (`booking_vs_room_id`),
  KEY `FKsn71db4exhm6rar3t4xsn95hd` (`cancellation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `master_booking`
--

DROP TABLE IF EXISTS `master_booking`;
CREATE TABLE IF NOT EXISTS `master_booking` (
  `booking_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `accomodation_type` varchar(255) DEFAULT NULL,
  `booking_approval` varchar(255) DEFAULT NULL,
  `checkin_date` varchar(255) DEFAULT NULL,
  `checkout_date` varchar(255) DEFAULT NULL,
  `convenience_amt_wgst` varchar(255) DEFAULT NULL,
  `convenience_fee_amt` varchar(255) DEFAULT NULL,
  `convenience_fee_perc` varchar(255) DEFAULT NULL,
  `convenience_gst_amt` varchar(255) DEFAULT NULL,
  `failure_url` varchar(255) DEFAULT NULL,
  `num_of_days` varchar(255) DEFAULT NULL,
  `ora_special_offer_amt` varchar(255) DEFAULT NULL,
  `ora_special_offer_perc` varchar(255) DEFAULT NULL,
  `orabooking_id` varchar(255) DEFAULT NULL,
  `property_id` bigint(20) DEFAULT NULL,
  `property_loc` varchar(255) DEFAULT NULL,
  `success_url` varchar(255) DEFAULT NULL,
  `total_price` varchar(255) DEFAULT NULL,
  `user_final_price` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `master_cancellation`
--

DROP TABLE IF EXISTS `master_cancellation`;
CREATE TABLE IF NOT EXISTS `master_cancellation` (
  `cancellation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `reason_for_cancellation` varchar(255) DEFAULT NULL,
  `total_amount_paid` varchar(255) DEFAULT NULL,
  `total_amount_refunded` varchar(255) DEFAULT NULL,
  `total_payble_without_gst` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `booking_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cancellation_id`),
  KEY `FKn4ln85ds3dvicd3f41q3jvxt4` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `master_gateway`
--

DROP TABLE IF EXISTS `master_gateway`;
CREATE TABLE IF NOT EXISTS `master_gateway` (
  `gateway_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `gateway_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`gateway_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `master_sac_code`
--

DROP TABLE IF EXISTS `master_sac_code`;
CREATE TABLE IF NOT EXISTS `master_sac_code` (
  `sac_code_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `sac_name` varchar(255) DEFAULT NULL,
  `sac_code_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sac_code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking_info`
--
ALTER TABLE `booking_info`
  ADD CONSTRAINT `FKo1b17kgui5bkhpme9y5n1hnnd` FOREIGN KEY (`booking_id`) REFERENCES `master_booking` (`booking_id`);

--
-- Constraints for table `booking_vs_payment`
--
ALTER TABLE `booking_vs_payment`
  ADD CONSTRAINT `FK7o4e18jl8hkm5t6t1xs00gtr8` FOREIGN KEY (`booking_id`) REFERENCES `master_booking` (`booking_id`),
  ADD CONSTRAINT `FKs3qn4233lkx83a4rimjvfs563` FOREIGN KEY (`gateway_id`) REFERENCES `master_gateway` (`gateway_id`);

--
-- Constraints for table `booking_vs_room`
--
ALTER TABLE `booking_vs_room`
  ADD CONSTRAINT `FK5ufallrpmt0lxf4fgp4u0vudm` FOREIGN KEY (`booking_id`) REFERENCES `master_booking` (`booking_id`),
  ADD CONSTRAINT `FKdeiy7awthvtj76bw7kiescxqp` FOREIGN KEY (`sac_code_id`) REFERENCES `master_sac_code` (`sac_code_id`);

--
-- Constraints for table `cancellation_vs_room`
--
ALTER TABLE `cancellation_vs_room`
  ADD CONSTRAINT `FK6swa3fl9vk3sx659lb3j0n095` FOREIGN KEY (`booking_vs_room_id`) REFERENCES `booking_vs_room` (`booking_vs_room_id`),
  ADD CONSTRAINT `FKsn71db4exhm6rar3t4xsn95hd` FOREIGN KEY (`cancellation_id`) REFERENCES `master_cancellation` (`cancellation_id`);

--
-- Constraints for table `master_cancellation`
--
ALTER TABLE `master_cancellation`
  ADD CONSTRAINT `FKn4ln85ds3dvicd3f41q3jvxt4` FOREIGN KEY (`booking_id`) REFERENCES `master_booking` (`booking_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
