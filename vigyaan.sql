-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 30, 2016 at 08:35 AM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 7.0.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `vigyaan`
--

-- --------------------------------------------------------

--
-- Table structure for table `classbranch`
--

CREATE TABLE `classbranch` (
  `classid` varchar(10) NOT NULL,
  `branch` varchar(100) NOT NULL,
  `semester` int(11) NOT NULL,
  `start` bigint(20) NOT NULL,
  `end` bigint(20) NOT NULL,
  `sliderstart` bigint(20) NOT NULL DEFAULT '0',
  `sliderend` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `classbranch`
--

INSERT INTO `classbranch` (`classid`, `branch`, `semester`, `start`, `end`, `sliderstart`, `sliderend`) VALUES
('IT1', 'IT', 5, 14118001, 14118050, 0, 0),
('IT1', 'IT', 7, 13118001, 13118050, 0, 0),
('IT1', 'IT', 3, 15118001, 15118050, 0, 0),
('IT1', 'IT', 1, 16118001, 16118050, 0, 0),
('SN5', 'ELEX', 1, 101, 115, 0, 0),
('SN5', 'CS', 1, 201, 215, 0, 0),
('SN5', 'IT', 1, 301, 315, 0, 0),
('SN5', 'Arch', 1, 401, 415, 0, 0),
('SN7', 'IT', 3, 15118001, 15118022, 0, 0),
('SN7', 'IT', 5, 14118001, 14118022, 0, 0),
('SN7', 'IT', 1, 16118001, 16118022, 0, 0),
('SN7', 'IT', 7, 13118001, 13118022, 0, 0),
('SN1', 'IT', 3, 1001, 1020, 901, 910),
('SN1', 'BT', 5, 2001, 2020, 8001, 8010),
('SN1', 'Arch', 1, 3001, 3020, 7001, 7010),
('SN1', 'Arch', 1, 4001, 4010, 6001, 6020),
('RR', 'IT', 5, 101, 120, 901, 905),
('RR', 'META', 6, 201, 225, 0, 0),
('RR', 'CIVIL', 8, 301, 310, 801, 815);

-- --------------------------------------------------------

--
-- Table structure for table `classdetail`
--

CREATE TABLE `classdetail` (
  `classid` varchar(20) NOT NULL,
  `strength` int(11) NOT NULL,
  `collumn` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `classdetail`
--

INSERT INTO `classdetail` (`classid`, `strength`, `collumn`) VALUES
('IT1', 200, 10),
('RR', 100, 10),
('SN1', 120, 8),
('SN5', 60, 6),
('SN7', 90, 8);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `password`, `email`) VALUES
('abc', 'abc', 'abc'),
('kamta', 'kps', 'kps7111@gmail.com'),
('kps', 'kps', 'kps'),
('manumadarchod', 'manumadarchod', 'manumadarchod'),
('rht', 'rht', 'rht'),
('sonu', 'sonu', 'sonu');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `classdetail`
--
ALTER TABLE `classdetail`
  ADD PRIMARY KEY (`classid`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
