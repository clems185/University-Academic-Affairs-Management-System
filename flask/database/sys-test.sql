/*
 Navicat Premium Dump SQL

 Source Server         : schedule-system
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : sys-test

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 28/12/2025 18:05:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building`  (
  `c_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '所属校区ID',
  `building_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教学楼ID，唯一标识',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教学楼名称，如\"A楼\"',
  PRIMARY KEY (`building_id`) USING BTREE,
  INDEX `idx_building_campus`(`c_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '教学楼信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of building
-- ----------------------------
INSERT INTO `building` VALUES ('C0', 'B0', 'A楼');
INSERT INTO `building` VALUES ('C0', 'B1', 'B楼');
INSERT INTO `building` VALUES ('C1', 'B10', 'A楼');
INSERT INTO `building` VALUES ('C1', 'B11', 'B楼');
INSERT INTO `building` VALUES ('C1', 'B12', 'C楼');
INSERT INTO `building` VALUES ('C1', 'B13', 'D楼');
INSERT INTO `building` VALUES ('C1', 'B14', 'E楼');
INSERT INTO `building` VALUES ('C1', 'B15', 'F楼');
INSERT INTO `building` VALUES ('C1', 'B16', 'G楼');
INSERT INTO `building` VALUES ('C1', 'B17', 'H楼');
INSERT INTO `building` VALUES ('C1', 'B18', 'I楼');
INSERT INTO `building` VALUES ('C1', 'B19', 'J楼');
INSERT INTO `building` VALUES ('C0', 'B2', 'C楼');
INSERT INTO `building` VALUES ('C2', 'B20', 'A楼');
INSERT INTO `building` VALUES ('C2', 'B21', 'B楼');
INSERT INTO `building` VALUES ('C2', 'B22', 'C楼');
INSERT INTO `building` VALUES ('C2', 'B23', 'D楼');
INSERT INTO `building` VALUES ('C2', 'B24', 'E楼');
INSERT INTO `building` VALUES ('C2', 'B25', 'F楼');
INSERT INTO `building` VALUES ('C2', 'B26', 'G楼');
INSERT INTO `building` VALUES ('C2', 'B27', 'H楼');
INSERT INTO `building` VALUES ('C2', 'B28', 'I楼');
INSERT INTO `building` VALUES ('C2', 'B29', 'J楼');
INSERT INTO `building` VALUES ('C0', 'B3', 'D楼');
INSERT INTO `building` VALUES ('C0', 'B4', 'E楼');
INSERT INTO `building` VALUES ('C0', 'B5', 'F楼');
INSERT INTO `building` VALUES ('C0', 'B6', 'G楼');
INSERT INTO `building` VALUES ('C0', 'B7', 'H楼');
INSERT INTO `building` VALUES ('C0', 'B8', 'I楼');
INSERT INTO `building` VALUES ('C0', 'B9', 'J楼');

-- ----------------------------
-- Table structure for campus
-- ----------------------------
DROP TABLE IF EXISTS `campus`;
CREATE TABLE `campus`  (
  `c_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '校区ID，唯一标识',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '校区名称，如\"主校区\"',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '校区地址',
  PRIMARY KEY (`c_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '校区信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of campus
-- ----------------------------
INSERT INTO `campus` VALUES ('C0', '四平校区', 'C0');
INSERT INTO `campus` VALUES ('C1', '嘉定校区', 'C1');
INSERT INTO `campus` VALUES ('C2', '沪西校区', 'C2');

-- ----------------------------
-- Table structure for class_classroom
-- ----------------------------
DROP TABLE IF EXISTS `class_classroom`;
CREATE TABLE `class_classroom`  (
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `class_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '班级ID，外键',
  `semester` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `year` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `classroom_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教室ID，外键',
  PRIMARY KEY (`course_id`, `class_id`, `semester`, `year`, `classroom_id`) USING BTREE,
  INDEX `idx_class_room`(`classroom_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '教学班教室分配表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of class_classroom
-- ----------------------------
INSERT INTO `class_classroom` VALUES ('C000', 'CLS00000', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C000', 'CLS00001', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C000', 'CLS00002', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C000', 'CLS00003', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C000', 'CLS00004', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C001', 'CLS00100', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C001', 'CLS00101', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C001', 'CLS00102', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C001', 'CLS00103', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C001', 'CLS00104', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C002', 'CLS00200', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C002', 'CLS00201', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C002', 'CLS00202', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C002', 'CLS00203', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C002', 'CLS00204', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C003', 'CLS00300', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C003', 'CLS00301', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C003', 'CLS00302', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C003', 'CLS00303', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C003', 'CLS00304', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C004', 'CLS00400', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C004', 'CLS00401', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C004', 'CLS00402', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C004', 'CLS00403', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C004', 'CLS00404', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C005', 'CLS00500', '0', '2025', 'B4_108');
INSERT INTO `class_classroom` VALUES ('C005', 'CLS00501', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C005', 'CLS00502', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C005', 'CLS00503', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C005', 'CLS00504', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C006', 'CLS00600', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C006', 'CLS00601', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C006', 'CLS00602', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C006', 'CLS00603', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C006', 'CLS00604', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C007', 'CLS00700', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C007', 'CLS00701', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C007', 'CLS00702', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C007', 'CLS00703', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C007', 'CLS00704', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C008', 'CLS00800', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C008', 'CLS00801', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C008', 'CLS00802', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C008', 'CLS00803', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C008', 'CLS00804', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C009', 'CLS00900', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C009', 'CLS00901', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C009', 'CLS00902', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C009', 'CLS00903', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C009', 'CLS00904', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C010', 'CLS01000', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C010', 'CLS01001', '0', '2025', 'B4_109');
INSERT INTO `class_classroom` VALUES ('C010', 'CLS01002', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C010', 'CLS01003', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C010', 'CLS01004', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C011', 'CLS01100', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C011', 'CLS01101', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C011', 'CLS01102', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C011', 'CLS01103', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C011', 'CLS01104', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C012', 'CLS01200', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C012', 'CLS01201', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C012', 'CLS01202', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C012', 'CLS01203', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C012', 'CLS01204', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C013', 'CLS01300', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C013', 'CLS01301', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C013', 'CLS01302', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C013', 'CLS01303', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C013', 'CLS01304', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C014', 'CLS01400', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C014', 'CLS01401', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C014', 'CLS01402', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C014', 'CLS01403', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C014', 'CLS01404', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C015', 'CLS01500', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C015', 'CLS01501', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C015', 'CLS01502', '0', '2025', 'B4_200');
INSERT INTO `class_classroom` VALUES ('C015', 'CLS01503', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C015', 'CLS01504', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C016', 'CLS01600', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C016', 'CLS01601', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C016', 'CLS01602', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C016', 'CLS01603', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C016', 'CLS01604', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C017', 'CLS01700', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C017', 'CLS01701', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C017', 'CLS01702', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C017', 'CLS01703', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C017', 'CLS01704', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C018', 'CLS01800', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C018', 'CLS01801', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C018', 'CLS01802', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C018', 'CLS01803', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C018', 'CLS01804', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C019', 'CLS01900', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C019', 'CLS01901', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C019', 'CLS01902', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C019', 'CLS01903', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C019', 'CLS01904', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C020', 'CLS02000', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C020', 'CLS02001', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C020', 'CLS02002', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C020', 'CLS02003', '0', '2025', 'B4_201');
INSERT INTO `class_classroom` VALUES ('C020', 'CLS02004', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C021', 'CLS02100', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C021', 'CLS02101', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C021', 'CLS02102', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C021', 'CLS02103', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C021', 'CLS02104', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C022', 'CLS02200', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C022', 'CLS02201', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C022', 'CLS02202', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C022', 'CLS02203', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C022', 'CLS02204', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C023', 'CLS02300', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C023', 'CLS02301', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C023', 'CLS02302', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C023', 'CLS02303', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C023', 'CLS02304', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C024', 'CLS02400', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C024', 'CLS02401', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C024', 'CLS02402', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C024', 'CLS02403', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C024', 'CLS02404', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C025', 'CLS02500', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C025', 'CLS02501', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C025', 'CLS02502', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C025', 'CLS02503', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C025', 'CLS02504', '0', '2025', 'B4_202');
INSERT INTO `class_classroom` VALUES ('C026', 'CLS02600', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C026', 'CLS02601', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C026', 'CLS02602', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C026', 'CLS02603', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C026', 'CLS02604', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C027', 'CLS02700', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C027', 'CLS02701', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C027', 'CLS02702', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C027', 'CLS02703', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C027', 'CLS02704', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C028', 'CLS02800', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C028', 'CLS02801', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C028', 'CLS02802', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C028', 'CLS02803', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C028', 'CLS02804', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C029', 'CLS02900', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C029', 'CLS02901', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C029', 'CLS02902', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C029', 'CLS02903', '0', '2025', 'B4_203');
INSERT INTO `class_classroom` VALUES ('C029', 'CLS02904', '0', '2025', 'B4_203');

-- ----------------------------
-- Table structure for class_time
-- ----------------------------
DROP TABLE IF EXISTS `class_time`;
CREATE TABLE `class_time`  (
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `class_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '班级ID，外键',
  `semester` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `year` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `week_1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '第一周的时间安排，后面同理',
  `week_2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_4` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_6` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_7` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_8` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_9` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_10` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_11` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_12` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_13` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_14` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_15` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_16` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_17` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_18` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_19` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_20` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_21` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week_22` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  PRIMARY KEY (`course_id`, `class_id`, `semester`, `year`) USING BTREE,
  INDEX `idx_class_time_slot`(`week_1` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '教学班时间段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of class_time
-- ----------------------------
INSERT INTO `class_time` VALUES ('C000', 'CLS00000', '0', '2025', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C000', 'CLS00001', '0', '2025', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C000', 'CLS00002', '0', '2025', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C000', 'CLS00003', '0', '2025', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C000', 'CLS00004', '0', '2025', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C001', 'CLS00100', '0', '2025', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C001', 'CLS00101', '0', '2025', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C001', 'CLS00102', '0', '2025', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C001', 'CLS00103', '0', '2025', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C001', 'CLS00104', '0', '2025', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C002', 'CLS00200', '0', '2025', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C002', 'CLS00201', '0', '2025', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C002', 'CLS00202', '0', '2025', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C002', 'CLS00203', '0', '2025', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C002', 'CLS00204', '0', '2025', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C003', 'CLS00300', '0', '2025', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C003', 'CLS00301', '0', '2025', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C003', 'CLS00302', '0', '2025', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C003', 'CLS00303', '0', '2025', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C003', 'CLS00304', '0', '2025', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C004', 'CLS00400', '0', '2025', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C004', 'CLS00401', '0', '2025', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C004', 'CLS00402', '0', '2025', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C004', 'CLS00403', '0', '2025', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C004', 'CLS00404', '0', '2025', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C005', 'CLS00500', '0', '2025', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C005', 'CLS00501', '0', '2025', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C005', 'CLS00502', '0', '2025', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C005', 'CLS00503', '0', '2025', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C005', 'CLS00504', '0', '2025', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C006', 'CLS00600', '0', '2025', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C006', 'CLS00601', '0', '2025', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C006', 'CLS00602', '0', '2025', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C006', 'CLS00603', '0', '2025', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C006', 'CLS00604', '0', '2025', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C007', 'CLS00700', '0', '2025', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C007', 'CLS00701', '0', '2025', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C007', 'CLS00702', '0', '2025', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C007', 'CLS00703', '0', '2025', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C007', 'CLS00704', '0', '2025', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C008', 'CLS00800', '0', '2025', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C008', 'CLS00801', '0', '2025', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C008', 'CLS00802', '0', '2025', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C008', 'CLS00803', '0', '2025', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C008', 'CLS00804', '0', '2025', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C009', 'CLS00900', '0', '2025', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C009', 'CLS00901', '0', '2025', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C009', 'CLS00902', '0', '2025', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C009', 'CLS00903', '0', '2025', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C009', 'CLS00904', '0', '2025', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C010', 'CLS01000', '0', '2025', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C010', 'CLS01001', '0', '2025', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C010', 'CLS01002', '0', '2025', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C010', 'CLS01003', '0', '2025', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C010', 'CLS01004', '0', '2025', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C011', 'CLS01100', '0', '2025', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C011', 'CLS01101', '0', '2025', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C011', 'CLS01102', '0', '2025', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C011', 'CLS01103', '0', '2025', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C011', 'CLS01104', '0', '2025', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C012', 'CLS01200', '0', '2025', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C012', 'CLS01201', '0', '2025', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C012', 'CLS01202', '0', '2025', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C012', 'CLS01203', '0', '2025', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C012', 'CLS01204', '0', '2025', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C013', 'CLS01300', '0', '2025', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C013', 'CLS01301', '0', '2025', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C013', 'CLS01302', '0', '2025', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C013', 'CLS01303', '0', '2025', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C013', 'CLS01304', '0', '2025', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C014', 'CLS01400', '0', '2025', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C014', 'CLS01401', '0', '2025', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C014', 'CLS01402', '0', '2025', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C014', 'CLS01403', '0', '2025', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C014', 'CLS01404', '0', '2025', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C015', 'CLS01500', '0', '2025', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C015', 'CLS01501', '0', '2025', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C015', 'CLS01502', '0', '2025', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C015', 'CLS01503', '0', '2025', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C015', 'CLS01504', '0', '2025', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C016', 'CLS01600', '0', '2025', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C016', 'CLS01601', '0', '2025', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C016', 'CLS01602', '0', '2025', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C016', 'CLS01603', '0', '2025', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C016', 'CLS01604', '0', '2025', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C017', 'CLS01700', '0', '2025', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C017', 'CLS01701', '0', '2025', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C017', 'CLS01702', '0', '2025', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C017', 'CLS01703', '0', '2025', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C017', 'CLS01704', '0', '2025', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C018', 'CLS01800', '0', '2025', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C018', 'CLS01801', '0', '2025', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C018', 'CLS01802', '0', '2025', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C018', 'CLS01803', '0', '2025', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C018', 'CLS01804', '0', '2025', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C019', 'CLS01900', '0', '2025', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C019', 'CLS01901', '0', '2025', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C019', 'CLS01902', '0', '2025', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C019', 'CLS01903', '0', '2025', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C019', 'CLS01904', '0', '2025', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C020', 'CLS02000', '0', '2025', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C020', 'CLS02001', '0', '2025', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C020', 'CLS02002', '0', '2025', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C020', 'CLS02003', '0', '2025', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C020', 'CLS02004', '0', '2025', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C021', 'CLS02100', '0', '2025', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C021', 'CLS02101', '0', '2025', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C021', 'CLS02102', '0', '2025', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C021', 'CLS02103', '0', '2025', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C021', 'CLS02104', '0', '2025', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C022', 'CLS02200', '0', '2025', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C022', 'CLS02201', '0', '2025', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C022', 'CLS02202', '0', '2025', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C022', 'CLS02203', '0', '2025', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C022', 'CLS02204', '0', '2025', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C023', 'CLS02300', '0', '2025', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C023', 'CLS02301', '0', '2025', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C023', 'CLS02302', '0', '2025', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C023', 'CLS02303', '0', '2025', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C023', 'CLS02304', '0', '2025', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C024', 'CLS02400', '0', '2025', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C024', 'CLS02401', '0', '2025', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C024', 'CLS02402', '0', '2025', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C024', 'CLS02403', '0', '2025', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C024', 'CLS02404', '0', '2025', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', '48,49', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C025', 'CLS02500', '0', '2025', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', '50,51', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C025', 'CLS02501', '0', '2025', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', '54,55', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C025', 'CLS02502', '0', '2025', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', '52,53', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C025', 'CLS02503', '0', '2025', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', '56,57', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C025', 'CLS02504', '0', '2025', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', '58,59', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C026', 'CLS02600', '0', '2025', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', '0,1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C026', 'CLS02601', '0', '2025', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', '2,3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C026', 'CLS02602', '0', '2025', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', '4,5', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C026', 'CLS02603', '0', '2025', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', '6,7', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C026', 'CLS02604', '0', '2025', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', '8,9', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C027', 'CLS02700', '0', '2025', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', '10,11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C027', 'CLS02701', '0', '2025', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', '12,13', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C027', 'CLS02702', '0', '2025', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', '14,15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C027', 'CLS02703', '0', '2025', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', '24,25', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C027', 'CLS02704', '0', '2025', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', '26,27', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C028', 'CLS02800', '0', '2025', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', '32,33', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C028', 'CLS02801', '0', '2025', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', '34,35', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C028', 'CLS02802', '0', '2025', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', '28,29', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C028', 'CLS02803', '0', '2025', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', '30,31', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C028', 'CLS02804', '0', '2025', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', '36,37', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C029', 'CLS02900', '0', '2025', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', '38,39', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C029', 'CLS02901', '0', '2025', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', '40,41', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C029', 'CLS02902', '0', '2025', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', '42,43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C029', 'CLS02903', '0', '2025', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', '44,45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `class_time` VALUES ('C029', 'CLS02904', '0', '2025', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', '46,47', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for classroom
-- ----------------------------
DROP TABLE IF EXISTS `classroom`;
CREATE TABLE `classroom`  (
  `building_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '所属教学楼ID',
  `classroom_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教室ID，唯一标识',
  `room_number` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教室编号，如101、203',
  `capacity` decimal(3, 0) NOT NULL COMMENT '教室容量，范围10-300人',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教室类型，限定为固定枚举值',
  PRIMARY KEY (`classroom_id`) USING BTREE,
  INDEX `idx_classroom_building`(`building_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '教室信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of classroom
-- ----------------------------
INSERT INTO `classroom` VALUES ('B0', 'B0_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B0', 'B0_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B10', 'B10_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B11', 'B11_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B12', 'B12_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B13', 'B13_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B14', 'B14_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B15', 'B15_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B16', 'B16_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B17', 'B17_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B18', 'B18_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B19', 'B19_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B1', 'B1_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B20', 'B20_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B21', 'B21_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B22', 'B22_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B23', 'B23_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B24', 'B24_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B25', 'B25_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B26', 'B26_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B27', 'B27_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B28', 'B28_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B29', 'B29_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B2', 'B2_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B3', 'B3_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B4', 'B4_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B5', 'B5_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B6', 'B6_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B7', 'B7_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B8', 'B8_509', '509', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_100', '100', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_101', '101', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_102', '102', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_103', '103', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_104', '104', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_105', '105', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_106', '106', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_107', '107', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_108', '108', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_109', '109', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_200', '200', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_201', '201', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_202', '202', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_203', '203', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_204', '204', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_205', '205', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_206', '206', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_207', '207', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_208', '208', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_209', '209', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_300', '300', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_301', '301', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_302', '302', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_303', '303', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_304', '304', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_305', '305', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_306', '306', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_307', '307', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_308', '308', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_309', '309', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_400', '400', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_401', '401', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_402', '402', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_403', '403', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_404', '404', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_405', '405', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_406', '406', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_407', '407', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_408', '408', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_409', '409', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_500', '500', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_501', '501', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_502', '502', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_503', '503', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_504', '504', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_505', '505', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_506', '506', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_507', '507', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_508', '508', 60, '普通教室');
INSERT INTO `classroom` VALUES ('B9', 'B9_509', '509', 60, '普通教室');

-- ----------------------------
-- Table structure for contest
-- ----------------------------
DROP TABLE IF EXISTS `contest`;
CREATE TABLE `contest`  (
  `contest_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '竞赛ID，主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '竞赛名称，唯一约束',
  `contest_time` datetime NOT NULL COMMENT '竞赛举办时间',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '竞赛等级（国家级/省级/市级/校级/其他）',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '竞赛官网链接',
  `information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '竞赛描述信息',
  `numbers` decimal(65, 30) NULL DEFAULT NULL COMMENT '报名人数，默认0',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间，默认当前时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间，默认当前时间',
  `state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '最终决定（Y=同意/N=拒绝/P=处理中）',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`contest_id`) USING BTREE,
  INDEX `idx_contest_grade`(`grade` ASC) USING BTREE,
  INDEX `idx_contest_time`(`contest_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '竞赛信息表，记录各类竞赛的基本信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of contest
-- ----------------------------
INSERT INTO `contest` VALUES ('CON000', 'CON000', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON001', 'CON001', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON002', 'CON002', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON003', 'CON003', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON004', 'CON004', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON005', 'CON005', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON006', 'CON006', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON007', 'CON007', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON008', 'CON008', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON009', 'CON009', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'Y', NULL, NULL);
INSERT INTO `contest` VALUES ('CON010', 'CON010', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'P', NULL, NULL);
INSERT INTO `contest` VALUES ('CON011', 'CON011', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'P', NULL, NULL);
INSERT INTO `contest` VALUES ('CON012', 'CON012', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'P', NULL, NULL);
INSERT INTO `contest` VALUES ('CON013', 'CON013', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'P', NULL, NULL);
INSERT INTO `contest` VALUES ('CON014', 'CON014', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'P', NULL, NULL);
INSERT INTO `contest` VALUES ('CON015', 'CON015', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'P', NULL, NULL);
INSERT INTO `contest` VALUES ('CON016', 'CON016', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'N', NULL, NULL);
INSERT INTO `contest` VALUES ('CON017', 'CON017', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'N', NULL, NULL);
INSERT INTO `contest` VALUES ('CON018', 'CON018', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'N', NULL, NULL);
INSERT INTO `contest` VALUES ('CON019', 'CON019', '2023-01-03 00:00:00', '国家级', NULL, NULL, 0.000000000000000000000000000000, '2025-08-30 20:28:42', '2025-08-30 20:28:42', 'N', NULL, NULL);

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程名称',
  `is_select` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '是否选修课（Y/N）',
  `is_exam` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '是否考试课（Y/N）',
  `information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '课程描述',
  `credits` decimal(1, 0) NULL DEFAULT NULL COMMENT '学分（1-5）',
  `course_begin_week` decimal(2, 0) NULL DEFAULT NULL COMMENT '课程起始周',
  `course_end_week` decimal(2, 0) NULL DEFAULT NULL COMMENT '课程结束周',
  `first_score_information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '第一部分分数组成',
  `first_score` decimal(2, 0) NULL DEFAULT NULL COMMENT '第一部分分数占比',
  `second_score_information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '第二部分分数组成',
  `second_score` decimal(2, 0) NULL DEFAULT NULL COMMENT '第二部分分数占比',
  `third_score_information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '第三部分分数组成',
  `third_score` decimal(2, 0) NULL DEFAULT NULL COMMENT '第三部分分数占比',
  `forth_score_information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '第四部分分数组成',
  `forth_score` decimal(2, 0) NULL DEFAULT NULL COMMENT '第四部分分数占比',
  `times_per_week` decimal(65, 30) NULL DEFAULT NULL COMMENT '每周课时',
  PRIMARY KEY (`course_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '课程信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('C000', '高等数学', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C001', '线性代数', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C002', '大学物理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C003', '计算机基础', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C004', '程序设计', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C005', '数据结构', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C006', '操作系统', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C007', '计算机网络', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C008', '数据库原理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C009', '人工智能', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C010', '马克思主义原理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C011', '大学英语', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C012', '概率论与数理统计', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C013', '电路原理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C014', '数字逻辑', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C015', '信号与系统', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C016', '通信原理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C017', '软件工程', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C018', '算法分析', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C019', '编译原理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C020', '计算机组成原理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C021', '微型计算机技术', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C022', '嵌入式系统', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C023', '机器学习', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C024', '深度学习', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C025', '模式识别', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C026', '数字图像处理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C027', '自然语言处理', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C028', '计算机图形学', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);
INSERT INTO `course` VALUES ('C029', '物联网技术', 'Y', 'N', NULL, 5, 1, 17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2.000000000000000000000000000000);

-- ----------------------------
-- Table structure for course_class
-- ----------------------------
DROP TABLE IF EXISTS `course_class`;
CREATE TABLE `course_class`  (
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `class_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '班级编号',
  `semester` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `year` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教学班名称',
  `information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '班级描述',
  `capacity` decimal(3, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`course_id`, `class_id`, `semester`, `year`) USING BTREE,
  INDEX `idx_class_course`(`course_id` ASC) USING BTREE,
  INDEX `idx_class_year_sem`(`year` ASC, `semester` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '课程教学班信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course_class
-- ----------------------------
INSERT INTO `course_class` VALUES ('C000', 'CLS00000', '0', '2025', '高等数学-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C000', 'CLS00001', '0', '2025', '高等数学-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C000', 'CLS00002', '0', '2025', '高等数学-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C000', 'CLS00003', '0', '2025', '高等数学-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C000', 'CLS00004', '0', '2025', '高等数学-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C001', 'CLS00100', '0', '2025', '线性代数-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C001', 'CLS00101', '0', '2025', '线性代数-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C001', 'CLS00102', '0', '2025', '线性代数-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C001', 'CLS00103', '0', '2025', '线性代数-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C001', 'CLS00104', '0', '2025', '线性代数-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C002', 'CLS00200', '0', '2025', '大学物理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C002', 'CLS00201', '0', '2025', '大学物理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C002', 'CLS00202', '0', '2025', '大学物理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C002', 'CLS00203', '0', '2025', '大学物理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C002', 'CLS00204', '0', '2025', '大学物理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C003', 'CLS00300', '0', '2025', '计算机基础-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C003', 'CLS00301', '0', '2025', '计算机基础-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C003', 'CLS00302', '0', '2025', '计算机基础-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C003', 'CLS00303', '0', '2025', '计算机基础-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C003', 'CLS00304', '0', '2025', '计算机基础-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C004', 'CLS00400', '0', '2025', '程序设计-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C004', 'CLS00401', '0', '2025', '程序设计-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C004', 'CLS00402', '0', '2025', '程序设计-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C004', 'CLS00403', '0', '2025', '程序设计-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C004', 'CLS00404', '0', '2025', '程序设计-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C005', 'CLS00500', '0', '2025', '数据结构-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C005', 'CLS00501', '0', '2025', '数据结构-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C005', 'CLS00502', '0', '2025', '数据结构-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C005', 'CLS00503', '0', '2025', '数据结构-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C005', 'CLS00504', '0', '2025', '数据结构-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C006', 'CLS00600', '0', '2025', '操作系统-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C006', 'CLS00601', '0', '2025', '操作系统-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C006', 'CLS00602', '0', '2025', '操作系统-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C006', 'CLS00603', '0', '2025', '操作系统-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C006', 'CLS00604', '0', '2025', '操作系统-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C007', 'CLS00700', '0', '2025', '计算机网络-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C007', 'CLS00701', '0', '2025', '计算机网络-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C007', 'CLS00702', '0', '2025', '计算机网络-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C007', 'CLS00703', '0', '2025', '计算机网络-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C007', 'CLS00704', '0', '2025', '计算机网络-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C008', 'CLS00800', '0', '2025', '数据库原理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C008', 'CLS00801', '0', '2025', '数据库原理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C008', 'CLS00802', '0', '2025', '数据库原理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C008', 'CLS00803', '0', '2025', '数据库原理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C008', 'CLS00804', '0', '2025', '数据库原理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C009', 'CLS00900', '0', '2025', '人工智能-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C009', 'CLS00901', '0', '2025', '人工智能-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C009', 'CLS00902', '0', '2025', '人工智能-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C009', 'CLS00903', '0', '2025', '人工智能-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C009', 'CLS00904', '0', '2025', '人工智能-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C010', 'CLS01000', '0', '2025', '马克思主义原理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C010', 'CLS01001', '0', '2025', '马克思主义原理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C010', 'CLS01002', '0', '2025', '马克思主义原理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C010', 'CLS01003', '0', '2025', '马克思主义原理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C010', 'CLS01004', '0', '2025', '马克思主义原理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C011', 'CLS01100', '0', '2025', '大学英语-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C011', 'CLS01101', '0', '2025', '大学英语-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C011', 'CLS01102', '0', '2025', '大学英语-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C011', 'CLS01103', '0', '2025', '大学英语-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C011', 'CLS01104', '0', '2025', '大学英语-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C012', 'CLS01200', '0', '2025', '概率论与数理统计-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C012', 'CLS01201', '0', '2025', '概率论与数理统计-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C012', 'CLS01202', '0', '2025', '概率论与数理统计-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C012', 'CLS01203', '0', '2025', '概率论与数理统计-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C012', 'CLS01204', '0', '2025', '概率论与数理统计-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C013', 'CLS01300', '0', '2025', '电路原理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C013', 'CLS01301', '0', '2025', '电路原理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C013', 'CLS01302', '0', '2025', '电路原理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C013', 'CLS01303', '0', '2025', '电路原理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C013', 'CLS01304', '0', '2025', '电路原理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C014', 'CLS01400', '0', '2025', '数字逻辑-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C014', 'CLS01401', '0', '2025', '数字逻辑-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C014', 'CLS01402', '0', '2025', '数字逻辑-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C014', 'CLS01403', '0', '2025', '数字逻辑-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C014', 'CLS01404', '0', '2025', '数字逻辑-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C015', 'CLS01500', '0', '2025', '信号与系统-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C015', 'CLS01501', '0', '2025', '信号与系统-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C015', 'CLS01502', '0', '2025', '信号与系统-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C015', 'CLS01503', '0', '2025', '信号与系统-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C015', 'CLS01504', '0', '2025', '信号与系统-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C016', 'CLS01600', '0', '2025', '通信原理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C016', 'CLS01601', '0', '2025', '通信原理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C016', 'CLS01602', '0', '2025', '通信原理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C016', 'CLS01603', '0', '2025', '通信原理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C016', 'CLS01604', '0', '2025', '通信原理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C017', 'CLS01700', '0', '2025', '软件工程-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C017', 'CLS01701', '0', '2025', '软件工程-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C017', 'CLS01702', '0', '2025', '软件工程-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C017', 'CLS01703', '0', '2025', '软件工程-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C017', 'CLS01704', '0', '2025', '软件工程-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C018', 'CLS01800', '0', '2025', '算法分析-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C018', 'CLS01801', '0', '2025', '算法分析-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C018', 'CLS01802', '0', '2025', '算法分析-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C018', 'CLS01803', '0', '2025', '算法分析-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C018', 'CLS01804', '0', '2025', '算法分析-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C019', 'CLS01900', '0', '2025', '编译原理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C019', 'CLS01901', '0', '2025', '编译原理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C019', 'CLS01902', '0', '2025', '编译原理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C019', 'CLS01903', '0', '2025', '编译原理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C019', 'CLS01904', '0', '2025', '编译原理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C020', 'CLS02000', '0', '2025', '计算机组成原理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C020', 'CLS02001', '0', '2025', '计算机组成原理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C020', 'CLS02002', '0', '2025', '计算机组成原理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C020', 'CLS02003', '0', '2025', '计算机组成原理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C020', 'CLS02004', '0', '2025', '计算机组成原理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C021', 'CLS02100', '0', '2025', '微型计算机技术-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C021', 'CLS02101', '0', '2025', '微型计算机技术-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C021', 'CLS02102', '0', '2025', '微型计算机技术-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C021', 'CLS02103', '0', '2025', '微型计算机技术-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C021', 'CLS02104', '0', '2025', '微型计算机技术-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C022', 'CLS02200', '0', '2025', '嵌入式系统-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C022', 'CLS02201', '0', '2025', '嵌入式系统-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C022', 'CLS02202', '0', '2025', '嵌入式系统-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C022', 'CLS02203', '0', '2025', '嵌入式系统-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C022', 'CLS02204', '0', '2025', '嵌入式系统-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C023', 'CLS02300', '0', '2025', '机器学习-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C023', 'CLS02301', '0', '2025', '机器学习-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C023', 'CLS02302', '0', '2025', '机器学习-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C023', 'CLS02303', '0', '2025', '机器学习-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C023', 'CLS02304', '0', '2025', '机器学习-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C024', 'CLS02400', '0', '2025', '深度学习-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C024', 'CLS02401', '0', '2025', '深度学习-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C024', 'CLS02402', '0', '2025', '深度学习-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C024', 'CLS02403', '0', '2025', '深度学习-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C024', 'CLS02404', '0', '2025', '深度学习-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C025', 'CLS02500', '0', '2025', '模式识别-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C025', 'CLS02501', '0', '2025', '模式识别-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C025', 'CLS02502', '0', '2025', '模式识别-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C025', 'CLS02503', '0', '2025', '模式识别-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C025', 'CLS02504', '0', '2025', '模式识别-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C026', 'CLS02600', '0', '2025', '数字图像处理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C026', 'CLS02601', '0', '2025', '数字图像处理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C026', 'CLS02602', '0', '2025', '数字图像处理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C026', 'CLS02603', '0', '2025', '数字图像处理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C026', 'CLS02604', '0', '2025', '数字图像处理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C027', 'CLS02700', '0', '2025', '自然语言处理-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C027', 'CLS02701', '0', '2025', '自然语言处理-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C027', 'CLS02702', '0', '2025', '自然语言处理-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C027', 'CLS02703', '0', '2025', '自然语言处理-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C027', 'CLS02704', '0', '2025', '自然语言处理-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C028', 'CLS02800', '0', '2025', '计算机图形学-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C028', 'CLS02801', '0', '2025', '计算机图形学-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C028', 'CLS02802', '0', '2025', '计算机图形学-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C028', 'CLS02803', '0', '2025', '计算机图形学-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C028', 'CLS02804', '0', '2025', '计算机图形学-5班', NULL, 45);
INSERT INTO `course_class` VALUES ('C029', 'CLS02900', '0', '2025', '物联网技术-1班', NULL, 45);
INSERT INTO `course_class` VALUES ('C029', 'CLS02901', '0', '2025', '物联网技术-2班', NULL, 45);
INSERT INTO `course_class` VALUES ('C029', 'CLS02902', '0', '2025', '物联网技术-3班', NULL, 45);
INSERT INTO `course_class` VALUES ('C029', 'CLS02903', '0', '2025', '物联网技术-4班', NULL, 45);
INSERT INTO `course_class` VALUES ('C029', 'CLS02904', '0', '2025', '物联网技术-5班', NULL, 45);

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `dept_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '部门ID，主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '部门名称',
  `building_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '部门所在大楼',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '部门信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('DEPT000', '计算机学院', 'B22');
INSERT INTO `department` VALUES ('DEPT001', '电子信息工程学院', 'B5');
INSERT INTO `department` VALUES ('DEPT002', '工商管理学院', 'B27');
INSERT INTO `department` VALUES ('DEPT003', '外国语学院', 'B18');
INSERT INTO `department` VALUES ('DEPT004', '文学院', 'B0');
INSERT INTO `department` VALUES ('DEPT005', '数学与统计学院', 'B3');
INSERT INTO `department` VALUES ('DEPT006', '物理学院', 'B5');
INSERT INTO `department` VALUES ('DEPT007', '化学学院', 'B17');
INSERT INTO `department` VALUES ('DEPT008', '生命科学学院', 'B16');
INSERT INTO `department` VALUES ('DEPT009', '环境科学学院', 'B4');
INSERT INTO `department` VALUES ('DEPT010', '材料科学与工程学院', 'B23');
INSERT INTO `department` VALUES ('DEPT011', '机械工程学院', 'B25');
INSERT INTO `department` VALUES ('DEPT012', '土木工程学院', 'B22');
INSERT INTO `department` VALUES ('DEPT013', '经济学院', 'B18');
INSERT INTO `department` VALUES ('DEPT014', '法学院', 'B16');

-- ----------------------------
-- Table structure for exam_apply_ttoa
-- ----------------------------
DROP TABLE IF EXISTS `exam_apply_ttoa`;
CREATE TABLE `exam_apply_ttoa`  (
  `apply_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请ID，主键',
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `time_slot_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请课程时间段',
  `classroom_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '申请课程教室',
  `semester` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `year` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `paper` longblob NULL COMMENT '考试安排试卷',
  `apply_date` datetime NOT NULL COMMENT '申请时间，默认当前时间',
  `information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请内容',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '审核意见',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '状态（Y=同意/N=拒绝/P=处理中）',
  `paper_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `week` decimal(65, 30) NOT NULL COMMENT '考试周'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_apply_ttoa
-- ----------------------------
INSERT INTO `exam_apply_ttoa` VALUES ('A0', 'C008', '59', 'B15_206', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'P', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 0.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A1', 'C013', '10', 'B10_504', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'P', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 1.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A2', 'C014', '23', 'B26_401', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'Y', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 0.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A3', 'C025', '9', 'B13_504', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'P', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 1.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A4', 'C029', '65', 'B15_401', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'P', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 1.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A5', 'C018', '32', 'B11_507', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'N', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 0.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A6', 'C002', '62', 'B0_501', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'Y', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 0.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A7', 'C028', '60', 'B28_400', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'Y', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 1.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A8', 'C019', '11', 'B10_205', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'Y', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 1.000000000000000000000000000000);
INSERT INTO `exam_apply_ttoa` VALUES ('A9', 'C015', '70', 'B14_504', '0', '2025', NULL, '1900-01-01 00:00:00', '666', NULL, NULL, 'N', 'E:\\desktop\\2023年全国乙卷高考理综试题.docx', 1.000000000000000000000000000000);

-- ----------------------------
-- Table structure for exam_delay_apply_stoa
-- ----------------------------
DROP TABLE IF EXISTS `exam_delay_apply_stoa`;
CREATE TABLE `exam_delay_apply_stoa`  (
  `apply_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请ID，主键',
  `student_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '学生ID，外键',
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `apply_date` datetime NOT NULL COMMENT '申请时间，默认当前时间',
  `information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请内容',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '审核意见',
  `state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '状态（Y=同意/N=拒绝/P=处理中）',
  PRIMARY KEY (`apply_id`) USING BTREE,
  INDEX `idx_stoa_course`(`course_id` ASC) USING BTREE,
  INDEX `idx_stoa_state`(`state` ASC) USING BTREE,
  INDEX `idx_stoa_student`(`student_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '学生缓考申请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_delay_apply_stoa
-- ----------------------------
INSERT INTO `exam_delay_apply_stoa` VALUES ('2025091301', 'S2023001', 'C005', '2025-09-13 17:06:57', 'WOCHAOCAO\n', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D0', 'S2023172', 'C012', '2025-08-30 20:28:44', '555', NULL, NULL, 'Y');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D1', 'S2023129', 'C000', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D10', 'S2023065', 'C001', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D11', 'S2023161', 'C001', '2025-08-30 20:28:44', '555', NULL, NULL, 'Y');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D12', 'S2023035', 'C024', '2025-08-30 20:28:44', '555', NULL, NULL, 'Y');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D13', 'S2023087', 'C022', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D14', 'S2023091', 'C009', '2025-08-30 20:28:44', '555', NULL, NULL, 'Y');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D15', 'S2023006', 'C025', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D16', 'S2023147', 'C007', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D17', 'S2023139', 'C019', '2025-08-30 20:28:44', '555', NULL, NULL, 'Y');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D18', 'S2023114', 'C001', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D19', 'S2023025', 'C002', '2025-08-30 20:28:44', '555', NULL, NULL, 'Y');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D2', 'S2023192', 'C008', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D20', 'S2023094', 'C026', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D21', 'S2023038', 'C017', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D22', 'S2023185', 'C020', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D23', 'S2023140', 'C004', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D24', 'S2023054', 'C013', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D25', 'S2023036', 'C025', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D26', 'S2023165', 'C019', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D27', 'S2023141', 'C012', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D28', 'S2023044', 'C029', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D29', 'S2023013', 'C005', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D3', 'S2023151', 'C015', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D4', 'S2023129', 'C029', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D5', 'S2023148', 'C010', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D6', 'S2023081', 'C005', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D7', 'S2023038', 'C026', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D8', 'S2023086', 'C010', '2025-08-30 20:28:44', '555', NULL, NULL, 'N');
INSERT INTO `exam_delay_apply_stoa` VALUES ('D9', 'S2023013', 'C000', '2025-08-30 20:28:44', '555', NULL, NULL, 'P');

-- ----------------------------
-- Table structure for exam_papers
-- ----------------------------
DROP TABLE IF EXISTS `exam_papers`;
CREATE TABLE `exam_papers`  (
  `exam_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '考试ID',
  `paper` longblob NOT NULL COMMENT '试卷信息',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  PRIMARY KEY (`exam_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_papers
-- ----------------------------

-- ----------------------------
-- Table structure for exams
-- ----------------------------
DROP TABLE IF EXISTS `exams`;
CREATE TABLE `exams`  (
  `exam_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '考试ID，主键',
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `time_slot_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '时间段ID，外键',
  `classroom_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教室ID，外键',
  `semester` int NULL DEFAULT NULL,
  `year` int NULL DEFAULT NULL,
  `week` int NOT NULL COMMENT '表示是第几个考试周',
  PRIMARY KEY (`exam_id`) USING BTREE,
  INDEX `idx_exams_course`(`course_id` ASC) USING BTREE,
  INDEX `idx_exams_time`(`time_slot_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '考试安排表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exams
-- ----------------------------
INSERT INTO `exams` VALUES ('E000000000', 'C028', '25', 'B6_208', 0, 2025, 1);
INSERT INTO `exams` VALUES ('E000000001', 'C001', '13', 'B21_407', 0, 2025, 0);
INSERT INTO `exams` VALUES ('E000000002', 'C016', '19', 'B28_101', 0, 2025, 0);
INSERT INTO `exams` VALUES ('E000000003', 'C004', '70', 'B23_504', 0, 2025, 0);
INSERT INTO `exams` VALUES ('E000000004', 'C024', '15', 'B21_202', 0, 2025, 15);
INSERT INTO `exams` VALUES ('E000000005', 'C005', '42', 'B3_208', 0, 2025, 17);
INSERT INTO `exams` VALUES ('E000000006', 'C003', '66', 'B9_301', 0, 2025, 17);
INSERT INTO `exams` VALUES ('E000000007', 'C019', '26', 'B28_402', 0, 2025, 13);
INSERT INTO `exams` VALUES ('E000000008', 'C014', '3', 'B18_302', 0, 2025, 1);
INSERT INTO `exams` VALUES ('E000000009', 'C022', '65', 'B8_407', 0, 2025, 0);
INSERT INTO `exams` VALUES ('E000000010', 'C000', 'TS00001', 'B2_103', 1, 2025, 2);

-- ----------------------------
-- Table structure for information
-- ----------------------------
DROP TABLE IF EXISTS `information`;
CREATE TABLE `information`  (
  `info_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '通知ID，主键',
  `types` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '类型（公告/新闻/通知/文件/活动）',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL COMMENT '内容',
  `publish_time` datetime NOT NULL COMMENT '发布时间，默认当前时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_info_pubtime`(`publish_time` ASC) USING BTREE,
  INDEX `idx_info_type`(`types` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '通知信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of information
-- ----------------------------
INSERT INTO `information` VALUES ('INFO000001', '公告', '关于2025年秋季学期开学安排的通知', '根据校历安排，2025年秋季学期将于9月1日正式开学。请各位同学按时返校，做好开学准备。', '2025-08-25 09:00:00');
INSERT INTO `information` VALUES ('INFO000002', '通知', '2025级新生入学教育安排', '请所有2025级新生于8月30日上午9点在主校区体育馆参加入学教育大会。', '2025-08-20 14:30:00');
INSERT INTO `information` VALUES ('INFO000003', '活动', '“创新杯”大学生创业大赛报名启动', '2025年“创新杯”大学生创业大赛现已开始报名，截止日期为9月15日。欢迎同学们踊跃参加！', '2025-08-28 10:00:00');
INSERT INTO `information` VALUES ('INFO000004', '新闻', '我校计算机学院团队在国际竞赛中荣获金奖', '在刚刚结束的国际大学生程序设计竞赛（ICPC）亚洲区比赛中，我校计算机学院“星辰”队荣获金奖。', '2025-08-22 16:45:00');
INSERT INTO `information` VALUES ('INFO000005', '文件', '《本科生学籍管理规定》修订版发布', '教务处已发布最新修订的《本科生学籍管理规定》，请全体师生下载查阅。', '2025-08-18 11:20:00');
INSERT INTO `information` VALUES ('INFO000006', '通知', '图书馆闭馆维护通知', '因系统升级，图书馆将于9月2日全天闭馆，9月3日起正常开放。', '2025-08-29 08:50:00');
INSERT INTO `information` VALUES ('INFO000007', '活动', '秋季校园招聘会企业名单公布', '2025年秋季校园招聘会将于9月20日举行，参与企业名单已公布，详见附件。', '2025-08-26 13:15:00');
INSERT INTO `information` VALUES ('INFO000008', '公告', '关于调整教学楼空调开放时间的通知', '即日起，教学楼空调开放时间调整为7:00-22:00，请同学们注意。', '2025-08-24 15:30:00');
INSERT INTO `information` VALUES ('INFO000009', '新闻', '我校与海外三所高校签署合作协议', '近日，我校与美国、英国、澳大利亚三所知名高校签署了联合培养协议。', '2025-08-21 09:40:00');
INSERT INTO `information` VALUES ('INFO000010', '文件', '2025-2026学年校历正式发布', '教务处已发布2025-2026学年校历，请各单位依此安排教学与活动。', '2025-08-19 10:10:00');
INSERT INTO `information` VALUES ('SDK0010001', '公告', '期末考试通知', '软件工程课程将于2026年1月4日进行期末考试', '2025-12-28 17:48:17');

-- ----------------------------
-- Table structure for information_apply_ttoa
-- ----------------------------
DROP TABLE IF EXISTS `information_apply_ttoa`;
CREATE TABLE `information_apply_ttoa`  (
  `info_apply_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请id',
  `applicant_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请人ID',
  `apply_time` datetime NOT NULL COMMENT '申请时间',
  `types` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '类型',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL COMMENT '内容',
  `apply_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '申请信息（原因）',
  `review_time` datetime NOT NULL COMMENT '审核时间',
  `review_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '审核意见',
  `final_decision` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '最终决定（Y=同意/N=拒绝/P=处理中）',
  `review_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '审核ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of information_apply_ttoa
-- ----------------------------
INSERT INTO `information_apply_ttoa` VALUES ('APP000001', 'T001', '2025-08-25 10:30:00', '活动', '申请发布“人工智能前沿讲座”通知', '本次讲座特邀清华大学李教授主讲，面向全校师生开放。', '讲座对提升学生专业视野很有帮助', '2025-08-26 14:00:00', '内容符合要求，同意发布', 'Y', 'R001');
INSERT INTO `information_apply_ttoa` VALUES ('APP000002', 'T002', '2025-08-24 11:20:00', '通知', '申请发布“实验室安全培训”通知', '定于9月5日下午2点进行实验室安全培训，请相关师生参加。', '每学期例行培训', '2025-08-25 09:30:00', '培训时间需与教务处确认', 'P', 'R002');
INSERT INTO `information_apply_ttoa` VALUES ('APP000003', 'S001', '2025-08-23 15:45:00', '活动', '学生社团“音乐节”活动申请', '计划于9月10日晚举办校园音乐节，申请发布活动通知。', '丰富校园文化生活', '2025-08-25 16:20:00', '活动方案需补充安全预案', 'N', 'R003');
INSERT INTO `information_apply_ttoa` VALUES ('APP000004', 'T003', '2025-08-22 08:50:00', '新闻', '申请发布“学院科研项目获批”新闻', '我院张老师团队获国家自然科学基金重点项目资助。', '展示学院科研实力', '2025-08-23 10:15:00', '同意发布，请补充项目简介', 'Y', 'R004');
INSERT INTO `information_apply_ttoa` VALUES ('APP000005', 'T004', '2025-08-21 13:10:00', '文件', '申请发布“研究生开题报告模板”', '更新后的研究生开题报告模板，请研究生下载使用。', '模板有重要更新', '2025-08-22 11:00:00', '模板格式需统一调整', 'P', 'R005');
INSERT INTO `information_apply_ttoa` VALUES ('APP000006', 'S002', '2025-08-20 16:30:00', '活动', '“环保公益行”活动通知申请', '计划组织学生于9月15日前往西山开展环保公益活动。', '培养学生社会责任感', '2025-08-21 14:50:00', '活动需签署安全责任书', 'Y', 'R006');
INSERT INTO `information_apply_ttoa` VALUES ('APP000007', 'T005', '2025-08-19 09:25:00', '公告', '申请发布“机房改造暂停使用”公告', '因机房改造，9月1日-5日期间暂停使用，请师生谅解。', '保障改造顺利进行', '2025-08-20 08:45:00', '同意，请注明备用机房位置', 'Y', 'R007');
INSERT INTO `information_apply_ttoa` VALUES ('APP000008', 'T006', '2025-08-18 14:00:00', '通知', '“青年教师教学竞赛”报名通知', '2025年青年教师教学竞赛开始报名，截止日期9月10日。', '提升青年教师教学能力', '2025-08-19 15:30:00', '竞赛规则需进一步明确', 'N', 'R008');
INSERT INTO `information_apply_ttoa` VALUES ('APP000009', 'S003', '2025-08-17 17:20:00', '活动', '“读书分享会”活动申请', '每周五晚在图书馆三楼举办读书分享会，欢迎参加。', '营造书香校园氛围', '2025-08-18 10:10:00', '活动时间与图书馆闭馆冲突', 'N', 'R009');
INSERT INTO `information_apply_ttoa` VALUES ('APP000010', 'T007', '2025-08-16 10:40:00', '新闻', '申请发布“校企合作签约仪式”新闻', '我校与华为技术有限公司签署战略合作协议。', '深化产教融合', '2025-08-17 11:50:00', '新闻稿需经宣传部审核', 'P', 'R010');

-- ----------------------------
-- Table structure for instructor_apply
-- ----------------------------
DROP TABLE IF EXISTS `instructor_apply`;
CREATE TABLE `instructor_apply`  (
  `teacher_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请成为导师的教师',
  `apply_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请id',
  `information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请原因',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '审核意见',
  `final_decision` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '最终决定（Y=同意/N=拒绝/P=处理中）',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of instructor_apply
-- ----------------------------

-- ----------------------------
-- Table structure for instructor_selection
-- ----------------------------
DROP TABLE IF EXISTS `instructor_selection`;
CREATE TABLE `instructor_selection`  (
  `selection_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '选导师时间段ID，主键',
  `begin_time` datetime NOT NULL COMMENT '选导师开始时间',
  `end_time` datetime NOT NULL COMMENT '选导师结束时间',
  `information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '选导师说明信息',
  PRIMARY KEY (`selection_id`) USING BTREE,
  INDEX `idx_is_time`(`begin_time` ASC, `end_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '选导师时间段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of instructor_selection
-- ----------------------------

-- ----------------------------
-- Table structure for is_major
-- ----------------------------
DROP TABLE IF EXISTS `is_major`;
CREATE TABLE `is_major`  (
  `selection_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '选导师时间段ID，外键',
  `major_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '专业ID，外键',
  INDEX `idx_is_major_major`(`major_id` ASC) USING BTREE,
  INDEX `idx_is_major_sel`(`selection_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '选导师时间段与专业关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of is_major
-- ----------------------------
INSERT INTO `is_major` VALUES ('SEI001', 'MAJ001');
INSERT INTO `is_major` VALUES ('SEI002', 'MAJ001');
INSERT INTO `is_major` VALUES ('SEI003', 'MAJ001');
INSERT INTO `is_major` VALUES ('SEI004', 'MAJ001');
INSERT INTO `is_major` VALUES ('SEI005', 'MAJ001');
INSERT INTO `is_major` VALUES ('SEI006', 'MAJ001');
INSERT INTO `is_major` VALUES ('SEI007', 'MAJ001');

-- ----------------------------
-- Table structure for is_student
-- ----------------------------
DROP TABLE IF EXISTS `is_student`;
CREATE TABLE `is_student`  (
  `student_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '选导师期间学生信息',
  `instructor_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '选导师期间导师信息',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '选导师状态信息'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of is_student
-- ----------------------------
INSERT INTO `is_student` VALUES ('S2023001', 'T2023001', 'C');
INSERT INTO `is_student` VALUES ('S2023048', 'T2023000', 'Y');
INSERT INTO `is_student` VALUES ('S2023154', 'T2023000', 'Y');
INSERT INTO `is_student` VALUES ('S2023071', 'T2023000', 'Y');
INSERT INTO `is_student` VALUES ('S2023185', 'T2023000', 'Y');
INSERT INTO `is_student` VALUES ('S2023107', 'T2023000', 'Y');
INSERT INTO `is_student` VALUES ('S2023065', 'T2023000', 'Y');
INSERT INTO `is_student` VALUES ('S2023050', 'T2023001', 'Y');
INSERT INTO `is_student` VALUES ('S2023078', 'T2023001', 'Y');
INSERT INTO `is_student` VALUES ('S2023115', 'T2023001', 'Y');
INSERT INTO `is_student` VALUES ('S2023188', 'T2023001', 'Y');
INSERT INTO `is_student` VALUES ('S2023191', 'T2023001', 'Y');
INSERT INTO `is_student` VALUES ('S2023181', 'T2023001', 'Y');
INSERT INTO `is_student` VALUES ('S2023083', 'T2023002', 'Y');
INSERT INTO `is_student` VALUES ('S2023045', 'T2023002', 'Y');
INSERT INTO `is_student` VALUES ('S2023165', 'T2023002', 'Y');
INSERT INTO `is_student` VALUES ('S2023164', 'T2023002', 'Y');
INSERT INTO `is_student` VALUES ('S2023064', 'T2023002', 'Y');
INSERT INTO `is_student` VALUES ('S2023075', 'T2023002', 'Y');
INSERT INTO `is_student` VALUES ('S2023000', 'T2023001', 'C');
INSERT INTO `is_student` VALUES ('S2023025', 'T2023003', 'Y');
INSERT INTO `is_student` VALUES ('S2023091', 'T2023003', 'Y');
INSERT INTO `is_student` VALUES ('S2023045', 'T2023003', 'Y');
INSERT INTO `is_student` VALUES ('S2023077', 'T2023003', 'Y');
INSERT INTO `is_student` VALUES ('S2023129', 'T2023003', 'Y');
INSERT INTO `is_student` VALUES ('S2023198', 'T2023004', 'Y');
INSERT INTO `is_student` VALUES ('S2023192', 'T2023004', 'Y');
INSERT INTO `is_student` VALUES ('S2023099', 'T2023004', 'Y');
INSERT INTO `is_student` VALUES ('S2023151', 'T2023004', 'Y');
INSERT INTO `is_student` VALUES ('S2023154', 'T2023004', 'Y');
INSERT INTO `is_student` VALUES ('S2023183', 'T2023004', 'Y');
INSERT INTO `is_student` VALUES ('S2023157', 'T2023005', 'Y');
INSERT INTO `is_student` VALUES ('S2023115', 'T2023005', 'Y');
INSERT INTO `is_student` VALUES ('S2023133', 'T2023005', 'Y');
INSERT INTO `is_student` VALUES ('S2023167', 'T2023005', 'Y');
INSERT INTO `is_student` VALUES ('S2023034', 'T2023005', 'Y');
INSERT INTO `is_student` VALUES ('S2023118', 'T2023005', 'Y');
INSERT INTO `is_student` VALUES ('S2023078', 'T2023006', 'Y');
INSERT INTO `is_student` VALUES ('S2023188', 'T2023006', 'Y');
INSERT INTO `is_student` VALUES ('S2023142', 'T2023006', 'Y');
INSERT INTO `is_student` VALUES ('S2023143', 'T2023006', 'Y');
INSERT INTO `is_student` VALUES ('S2023103', 'T2023006', 'Y');
INSERT INTO `is_student` VALUES ('S2023052', 'T2023006', 'Y');
INSERT INTO `is_student` VALUES ('S2023032', 'T2023007', 'Y');
INSERT INTO `is_student` VALUES ('S2023190', 'T2023007', 'Y');
INSERT INTO `is_student` VALUES ('S2023031', 'T2023007', 'Y');
INSERT INTO `is_student` VALUES ('S2023064', 'T2023007', 'Y');
INSERT INTO `is_student` VALUES ('S2023156', 'T2023007', 'Y');
INSERT INTO `is_student` VALUES ('S2023071', 'T2023007', 'Y');
INSERT INTO `is_student` VALUES ('S2023108', 'T2023008', 'Y');
INSERT INTO `is_student` VALUES ('S2023060', 'T2023008', 'Y');
INSERT INTO `is_student` VALUES ('S2023068', 'T2023008', 'Y');
INSERT INTO `is_student` VALUES ('S2023027', 'T2023008', 'Y');
INSERT INTO `is_student` VALUES ('S2023193', 'T2023008', 'Y');
INSERT INTO `is_student` VALUES ('S2023041', 'T2023008', 'Y');
INSERT INTO `is_student` VALUES ('S2023081', 'T2023009', 'Y');
INSERT INTO `is_student` VALUES ('S2023060', 'T2023009', 'Y');
INSERT INTO `is_student` VALUES ('S2023000', 'T2023001', 'C');
INSERT INTO `is_student` VALUES ('S2023127', 'T2023009', 'Y');
INSERT INTO `is_student` VALUES ('S2023034', 'T2023009', 'Y');
INSERT INTO `is_student` VALUES ('S2023010', 'T2023009', 'Y');
INSERT INTO `is_student` VALUES ('S2023108', 'T2023010', 'Y');
INSERT INTO `is_student` VALUES ('S2023191', 'T2023010', 'Y');
INSERT INTO `is_student` VALUES ('S2023059', 'T2023010', 'Y');
INSERT INTO `is_student` VALUES ('S2023052', 'T2023010', 'Y');
INSERT INTO `is_student` VALUES ('S2023093', 'T2023010', 'Y');
INSERT INTO `is_student` VALUES ('S2023186', 'T2023010', 'Y');
INSERT INTO `is_student` VALUES ('S2023195', 'T2023011', 'Y');
INSERT INTO `is_student` VALUES ('S2023142', 'T2023011', 'Y');
INSERT INTO `is_student` VALUES ('S2023148', 'T2023011', 'Y');
INSERT INTO `is_student` VALUES ('S2023038', 'T2023011', 'Y');
INSERT INTO `is_student` VALUES ('S2023082', 'T2023011', 'Y');
INSERT INTO `is_student` VALUES ('S2023179', 'T2023011', 'Y');
INSERT INTO `is_student` VALUES ('S2023147', 'T2023012', 'Y');
INSERT INTO `is_student` VALUES ('S2023081', 'T2023012', 'Y');
INSERT INTO `is_student` VALUES ('S2023171', 'T2023012', 'Y');
INSERT INTO `is_student` VALUES ('S2023148', 'T2023012', 'Y');
INSERT INTO `is_student` VALUES ('S2023078', 'T2023012', 'Y');
INSERT INTO `is_student` VALUES ('S2023102', 'T2023012', 'Y');
INSERT INTO `is_student` VALUES ('S2023100', 'T2023013', 'Y');
INSERT INTO `is_student` VALUES ('S2023059', 'T2023013', 'Y');
INSERT INTO `is_student` VALUES ('S2023197', 'T2023013', 'Y');
INSERT INTO `is_student` VALUES ('S2023062', 'T2023013', 'Y');
INSERT INTO `is_student` VALUES ('S2023183', 'T2023013', 'Y');
INSERT INTO `is_student` VALUES ('S2023156', 'T2023013', 'Y');
INSERT INTO `is_student` VALUES ('S2023021', 'T2023014', 'Y');
INSERT INTO `is_student` VALUES ('S2023053', 'T2023014', 'Y');
INSERT INTO `is_student` VALUES ('S2023105', 'T2023014', 'Y');
INSERT INTO `is_student` VALUES ('S2023177', 'T2023014', 'Y');
INSERT INTO `is_student` VALUES ('S2023039', 'T2023014', 'Y');
INSERT INTO `is_student` VALUES ('S2023080', 'T2023014', 'Y');
INSERT INTO `is_student` VALUES ('S2023169', 'T2023015', 'Y');
INSERT INTO `is_student` VALUES ('S2023017', 'T2023015', 'Y');
INSERT INTO `is_student` VALUES ('S2023125', 'T2023015', 'Y');
INSERT INTO `is_student` VALUES ('S2023014', 'T2023015', 'Y');
INSERT INTO `is_student` VALUES ('S2023182', 'T2023015', 'Y');
INSERT INTO `is_student` VALUES ('S2023051', 'T2023015', 'Y');
INSERT INTO `is_student` VALUES ('S2023079', 'T2023016', 'Y');
INSERT INTO `is_student` VALUES ('S2023040', 'T2023016', 'Y');
INSERT INTO `is_student` VALUES ('S2023165', 'T2023016', 'Y');
INSERT INTO `is_student` VALUES ('S2023154', 'T2023016', 'Y');
INSERT INTO `is_student` VALUES ('S2023045', 'T2023016', 'Y');
INSERT INTO `is_student` VALUES ('S2023167', 'T2023016', 'Y');
INSERT INTO `is_student` VALUES ('S2023064', 'T2023017', 'Y');
INSERT INTO `is_student` VALUES ('S2023157', 'T2023017', 'Y');
INSERT INTO `is_student` VALUES ('S2023149', 'T2023017', 'Y');
INSERT INTO `is_student` VALUES ('S2023065', 'T2023017', 'Y');
INSERT INTO `is_student` VALUES ('S2023139', 'T2023017', 'Y');
INSERT INTO `is_student` VALUES ('S2023150', 'T2023017', 'Y');
INSERT INTO `is_student` VALUES ('S2023038', 'T2023018', 'Y');
INSERT INTO `is_student` VALUES ('S2023174', 'T2023018', 'Y');
INSERT INTO `is_student` VALUES ('S2023142', 'T2023018', 'Y');
INSERT INTO `is_student` VALUES ('S2023022', 'T2023018', 'Y');
INSERT INTO `is_student` VALUES ('S2023118', 'T2023018', 'Y');
INSERT INTO `is_student` VALUES ('S2023161', 'T2023018', 'Y');
INSERT INTO `is_student` VALUES ('S2023016', 'T2023019', 'Y');
INSERT INTO `is_student` VALUES ('S2023037', 'T2023019', 'Y');
INSERT INTO `is_student` VALUES ('S2023145', 'T2023019', 'Y');
INSERT INTO `is_student` VALUES ('S2023191', 'T2023019', 'Y');
INSERT INTO `is_student` VALUES ('S2023105', 'T2023019', 'Y');
INSERT INTO `is_student` VALUES ('S2023052', 'T2023019', 'Y');

-- ----------------------------
-- Table structure for is_teacher
-- ----------------------------
DROP TABLE IF EXISTS `is_teacher`;
CREATE TABLE `is_teacher`  (
  `selection_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '选导师时间段ID，外键',
  `teacher_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教师ID，外键',
  `capacity` decimal(65, 30) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '选导师时间段与教师关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of is_teacher
-- ----------------------------
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023000', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023001', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023002', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023003', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023004', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023005', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023006', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023007', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023008', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023009', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023010', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023011', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023012', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023013', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023014', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023015', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023016', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023017', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023018', 12.000000000000000000000000000000);
INSERT INTO `is_teacher` VALUES ('SEL001', 'T2023019', 12.000000000000000000000000000000);

-- ----------------------------
-- Table structure for major
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major`  (
  `major_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '专业ID，主键',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '专业名称',
  `edu_sys` decimal(1, 0) NULL DEFAULT NULL COMMENT '学制（0：普通教育，1：职业教育）',
  `dept_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '所属部门ID，外键',
  PRIMARY KEY (`major_id`) USING BTREE,
  INDEX `idx_major_dept`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '专业信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES ('MAJ000', '核工程与核技术', 2, 'DEPT006');
INSERT INTO `major` VALUES ('MAJ001', '会计学', 3, 'DEPT002');
INSERT INTO `major` VALUES ('MAJ002', '环境科学', 2, 'DEPT009');
INSERT INTO `major` VALUES ('MAJ003', '财务管理', 3, 'DEPT002');
INSERT INTO `major` VALUES ('MAJ004', '广告学', 1, 'DEPT004');
INSERT INTO `major` VALUES ('MAJ005', '德语', 1, 'DEPT003');
INSERT INTO `major` VALUES ('MAJ006', '自动化', 3, 'DEPT001');
INSERT INTO `major` VALUES ('MAJ007', '应用物理学', 2, 'DEPT006');
INSERT INTO `major` VALUES ('MAJ008', '生态学', 2, 'DEPT009');
INSERT INTO `major` VALUES ('MAJ009', '社会工作', 3, 'DEPT014');
INSERT INTO `major` VALUES ('MAJ010', '历史学', 2, 'DEPT004');
INSERT INTO `major` VALUES ('MAJ011', '社会工作', 1, 'DEPT014');
INSERT INTO `major` VALUES ('MAJ012', '环境科学', 1, 'DEPT009');
INSERT INTO `major` VALUES ('MAJ013', '生态学', 2, 'DEPT009');
INSERT INTO `major` VALUES ('MAJ014', '环境科学', 3, 'DEPT009');
INSERT INTO `major` VALUES ('MAJ015', '广告学', 3, 'DEPT004');
INSERT INTO `major` VALUES ('MAJ016', '计算机科学与技术', 3, 'DEPT000');
INSERT INTO `major` VALUES ('MAJ017', '生物技术', 1, 'DEPT008');
INSERT INTO `major` VALUES ('MAJ018', '应用化学', 1, 'DEPT007');
INSERT INTO `major` VALUES ('MAJ019', '生物科学', 2, 'DEPT008');
INSERT INTO `major` VALUES ('MAJ020', '社会工作', 1, 'DEPT014');
INSERT INTO `major` VALUES ('MAJ021', '自动化', 1, 'DEPT001');
INSERT INTO `major` VALUES ('MAJ022', '人工智能', 1, 'DEPT000');
INSERT INTO `major` VALUES ('MAJ023', '数学与应用数学', 3, 'DEPT005');
INSERT INTO `major` VALUES ('MAJ024', '生物科学', 3, 'DEPT008');
INSERT INTO `major` VALUES ('MAJ025', '德语', 3, 'DEPT003');
INSERT INTO `major` VALUES ('MAJ026', '历史学', 1, 'DEPT004');
INSERT INTO `major` VALUES ('MAJ027', '生物科学', 2, 'DEPT008');
INSERT INTO `major` VALUES ('MAJ028', '机械电子工程', 1, 'DEPT011');
INSERT INTO `major` VALUES ('MAJ029', '汉语言文学', 3, 'DEPT004');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `index` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `filepath` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `parentid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `order` decimal(65, 30) NOT NULL,
  `isenable` decimal(1, 0) NOT NULL,
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `createdate` datetime NOT NULL,
  `createuserid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `isdeleted` decimal(1, 0) NOT NULL,
  `modifydate` datetime NOT NULL,
  `modifyuserid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------

-- ----------------------------
-- Table structure for photos
-- ----------------------------
DROP TABLE IF EXISTS `photos`;
CREATE TABLE `photos`  (
  `id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT 'id信息',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '图片所属人类型（教师，学生，其他）',
  `photo` longblob NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of photos
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `roleid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `createuserid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `createdate` datetime NOT NULL,
  `isdelete` decimal(1, 0) NOT NULL,
  `modifyuserid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `modifydate` datetime NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `discription` decimal(65, 30) NOT NULL,
  `order` decimal(65, 30) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for selection
-- ----------------------------
DROP TABLE IF EXISTS `selection`;
CREATE TABLE `selection`  (
  `selection_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '选课时间段ID，主键',
  `begin_time` datetime NOT NULL COMMENT '选课开始时间',
  `end_time` datetime NOT NULL COMMENT '选课结束时间',
  `information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '选课说明信息',
  `type` decimal(1, 0) NULL DEFAULT NULL COMMENT '处理类型（0：随机踢人，1：选满为止）',
  `semester` int NULL DEFAULT NULL,
  `year` int NULL DEFAULT NULL,
  `is_processed` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '是否已处理结果（0：未处理，1：已处理）',
  PRIMARY KEY (`selection_id`) USING BTREE,
  INDEX `idx_selection_time`(`begin_time` ASC, `end_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '选课时间段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of selection
-- ----------------------------
INSERT INTO `selection` VALUES ('SEL000', '2025-08-30 20:28:41', '2025-09-06 20:28:41', '【2025学年第二学期选课】', 1, 0, 2025, '0');
INSERT INTO `selection` VALUES ('SEL001', '2025-09-09 20:28:41', '2025-09-16 20:28:41', '【2025学年第二学期选课时间段】处理规则：选满为止，时间范围：2025-09-09 20:28 ~ 2025-09-16 20:28', 1, 1, 2025, '0');
INSERT INTO `selection` VALUES ('SEL002', '2025-09-19 20:28:41', '2025-09-26 20:28:41', '【2025学年第一学期选课时间段】处理规则：选满为止，时间范围：2025-09-19 20:28 ~ 2025-09-26 20:28', 1, 0, 2025, '0');
INSERT INTO `selection` VALUES ('SEL003', '2025-09-29 20:28:41', '2025-10-06 20:28:41', '【2025学年第二学期选课时间段】处理规则：选满为止，时间范围：2025-09-29 20:28 ~ 2025-10-06 20:28', 1, 1, 2025, '0');
INSERT INTO `selection` VALUES ('SEL861', '2025-09-12 00:00:00', '2025-09-15 00:00:00', NULL, 0, 0, 2025, '0');
INSERT INTO `selection` VALUES ('SEL968', '2025-09-11 00:00:00', '2026-07-12 00:00:00', '选课测试', 0, 0, 2025, '1');

-- ----------------------------
-- Table structure for selection_course
-- ----------------------------
DROP TABLE IF EXISTS `selection_course`;
CREATE TABLE `selection_course`  (
  `selection_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '选课时间段ID，外键',
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  PRIMARY KEY (`selection_id`, `course_id`) USING BTREE,
  INDEX `idx_sc_course`(`course_id` ASC) USING BTREE,
  INDEX `idx_sc_selection`(`selection_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '选课时间段与课程关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of selection_course
-- ----------------------------
INSERT INTO `selection_course` VALUES ('SEL001', 'C000');
INSERT INTO `selection_course` VALUES ('SEL002', 'C000');
INSERT INTO `selection_course` VALUES ('SEL861', 'C000');
INSERT INTO `selection_course` VALUES ('SEL968', 'C000');
INSERT INTO `selection_course` VALUES ('SEL001', 'C001');
INSERT INTO `selection_course` VALUES ('SEL861', 'C001');
INSERT INTO `selection_course` VALUES ('SEL968', 'C001');
INSERT INTO `selection_course` VALUES ('SEL000', 'C002');
INSERT INTO `selection_course` VALUES ('SEL001', 'C002');
INSERT INTO `selection_course` VALUES ('SEL002', 'C002');
INSERT INTO `selection_course` VALUES ('SEL861', 'C002');
INSERT INTO `selection_course` VALUES ('SEL968', 'C002');
INSERT INTO `selection_course` VALUES ('SEL861', 'C003');
INSERT INTO `selection_course` VALUES ('SEL968', 'C003');
INSERT INTO `selection_course` VALUES ('SEL861', 'C004');
INSERT INTO `selection_course` VALUES ('SEL968', 'C004');
INSERT INTO `selection_course` VALUES ('SEL001', 'C005');
INSERT INTO `selection_course` VALUES ('SEL003', 'C005');
INSERT INTO `selection_course` VALUES ('SEL861', 'C005');
INSERT INTO `selection_course` VALUES ('SEL968', 'C005');
INSERT INTO `selection_course` VALUES ('SEL000', 'C006');
INSERT INTO `selection_course` VALUES ('SEL003', 'C006');
INSERT INTO `selection_course` VALUES ('SEL861', 'C006');
INSERT INTO `selection_course` VALUES ('SEL968', 'C006');
INSERT INTO `selection_course` VALUES ('SEL000', 'C007');
INSERT INTO `selection_course` VALUES ('SEL002', 'C007');
INSERT INTO `selection_course` VALUES ('SEL861', 'C007');
INSERT INTO `selection_course` VALUES ('SEL968', 'C007');
INSERT INTO `selection_course` VALUES ('SEL000', 'C008');
INSERT INTO `selection_course` VALUES ('SEL003', 'C008');
INSERT INTO `selection_course` VALUES ('SEL861', 'C008');
INSERT INTO `selection_course` VALUES ('SEL968', 'C008');
INSERT INTO `selection_course` VALUES ('SEL002', 'C009');
INSERT INTO `selection_course` VALUES ('SEL861', 'C009');
INSERT INTO `selection_course` VALUES ('SEL968', 'C009');
INSERT INTO `selection_course` VALUES ('SEL000', 'C010');
INSERT INTO `selection_course` VALUES ('SEL001', 'C010');
INSERT INTO `selection_course` VALUES ('SEL002', 'C010');
INSERT INTO `selection_course` VALUES ('SEL861', 'C010');
INSERT INTO `selection_course` VALUES ('SEL968', 'C010');
INSERT INTO `selection_course` VALUES ('SEL002', 'C011');
INSERT INTO `selection_course` VALUES ('SEL861', 'C011');
INSERT INTO `selection_course` VALUES ('SEL968', 'C011');
INSERT INTO `selection_course` VALUES ('SEL002', 'C012');
INSERT INTO `selection_course` VALUES ('SEL003', 'C012');
INSERT INTO `selection_course` VALUES ('SEL861', 'C012');
INSERT INTO `selection_course` VALUES ('SEL968', 'C012');
INSERT INTO `selection_course` VALUES ('SEL001', 'C013');
INSERT INTO `selection_course` VALUES ('SEL003', 'C013');
INSERT INTO `selection_course` VALUES ('SEL861', 'C013');
INSERT INTO `selection_course` VALUES ('SEL968', 'C013');
INSERT INTO `selection_course` VALUES ('SEL000', 'C014');
INSERT INTO `selection_course` VALUES ('SEL001', 'C014');
INSERT INTO `selection_course` VALUES ('SEL861', 'C014');
INSERT INTO `selection_course` VALUES ('SEL968', 'C014');
INSERT INTO `selection_course` VALUES ('SEL000', 'C015');
INSERT INTO `selection_course` VALUES ('SEL003', 'C015');
INSERT INTO `selection_course` VALUES ('SEL861', 'C015');
INSERT INTO `selection_course` VALUES ('SEL968', 'C015');
INSERT INTO `selection_course` VALUES ('SEL000', 'C016');
INSERT INTO `selection_course` VALUES ('SEL001', 'C016');
INSERT INTO `selection_course` VALUES ('SEL003', 'C016');
INSERT INTO `selection_course` VALUES ('SEL861', 'C016');
INSERT INTO `selection_course` VALUES ('SEL968', 'C016');
INSERT INTO `selection_course` VALUES ('SEL000', 'C017');
INSERT INTO `selection_course` VALUES ('SEL861', 'C017');
INSERT INTO `selection_course` VALUES ('SEL968', 'C017');
INSERT INTO `selection_course` VALUES ('SEL003', 'C018');
INSERT INTO `selection_course` VALUES ('SEL861', 'C018');
INSERT INTO `selection_course` VALUES ('SEL968', 'C018');
INSERT INTO `selection_course` VALUES ('SEL002', 'C019');
INSERT INTO `selection_course` VALUES ('SEL861', 'C019');
INSERT INTO `selection_course` VALUES ('SEL968', 'C019');
INSERT INTO `selection_course` VALUES ('SEL000', 'C020');
INSERT INTO `selection_course` VALUES ('SEL001', 'C020');
INSERT INTO `selection_course` VALUES ('SEL002', 'C020');
INSERT INTO `selection_course` VALUES ('SEL003', 'C020');
INSERT INTO `selection_course` VALUES ('SEL861', 'C020');
INSERT INTO `selection_course` VALUES ('SEL968', 'C020');
INSERT INTO `selection_course` VALUES ('SEL001', 'C021');
INSERT INTO `selection_course` VALUES ('SEL003', 'C021');
INSERT INTO `selection_course` VALUES ('SEL861', 'C021');
INSERT INTO `selection_course` VALUES ('SEL968', 'C021');
INSERT INTO `selection_course` VALUES ('SEL000', 'C022');
INSERT INTO `selection_course` VALUES ('SEL001', 'C022');
INSERT INTO `selection_course` VALUES ('SEL003', 'C022');
INSERT INTO `selection_course` VALUES ('SEL861', 'C022');
INSERT INTO `selection_course` VALUES ('SEL968', 'C022');
INSERT INTO `selection_course` VALUES ('SEL000', 'C023');
INSERT INTO `selection_course` VALUES ('SEL002', 'C023');
INSERT INTO `selection_course` VALUES ('SEL003', 'C023');
INSERT INTO `selection_course` VALUES ('SEL861', 'C023');
INSERT INTO `selection_course` VALUES ('SEL968', 'C023');
INSERT INTO `selection_course` VALUES ('SEL000', 'C024');
INSERT INTO `selection_course` VALUES ('SEL001', 'C024');
INSERT INTO `selection_course` VALUES ('SEL002', 'C024');
INSERT INTO `selection_course` VALUES ('SEL861', 'C024');
INSERT INTO `selection_course` VALUES ('SEL968', 'C024');
INSERT INTO `selection_course` VALUES ('SEL001', 'C025');
INSERT INTO `selection_course` VALUES ('SEL003', 'C025');
INSERT INTO `selection_course` VALUES ('SEL861', 'C025');
INSERT INTO `selection_course` VALUES ('SEL968', 'C025');
INSERT INTO `selection_course` VALUES ('SEL000', 'C026');
INSERT INTO `selection_course` VALUES ('SEL002', 'C026');
INSERT INTO `selection_course` VALUES ('SEL003', 'C026');
INSERT INTO `selection_course` VALUES ('SEL861', 'C026');
INSERT INTO `selection_course` VALUES ('SEL968', 'C026');
INSERT INTO `selection_course` VALUES ('SEL002', 'C027');
INSERT INTO `selection_course` VALUES ('SEL003', 'C027');
INSERT INTO `selection_course` VALUES ('SEL861', 'C027');
INSERT INTO `selection_course` VALUES ('SEL968', 'C027');
INSERT INTO `selection_course` VALUES ('SEL000', 'C028');
INSERT INTO `selection_course` VALUES ('SEL002', 'C028');
INSERT INTO `selection_course` VALUES ('SEL861', 'C028');
INSERT INTO `selection_course` VALUES ('SEL968', 'C028');
INSERT INTO `selection_course` VALUES ('SEL001', 'C029');
INSERT INTO `selection_course` VALUES ('SEL861', 'C029');
INSERT INTO `selection_course` VALUES ('SEL968', 'C029');

-- ----------------------------
-- Table structure for selection_major
-- ----------------------------
DROP TABLE IF EXISTS `selection_major`;
CREATE TABLE `selection_major`  (
  `selection_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '选课时间段ID，外键',
  `major_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '专业ID，外键',
  INDEX `idx_sm_major`(`major_id` ASC) USING BTREE,
  INDEX `idx_sm_selection`(`selection_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '选课时间段与专业关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of selection_major
-- ----------------------------
INSERT INTO `selection_major` VALUES ('SEL000', 'MAJ001');
INSERT INTO `selection_major` VALUES ('SEL000', 'MAJ004');
INSERT INTO `selection_major` VALUES ('SEL000', 'MAJ001');
INSERT INTO `selection_major` VALUES ('SEL000', 'MAJ017');
INSERT INTO `selection_major` VALUES ('SEL000', 'MAJ013');
INSERT INTO `selection_major` VALUES ('SEL001', 'MAJ018');
INSERT INTO `selection_major` VALUES ('SEL001', 'MAJ004');
INSERT INTO `selection_major` VALUES ('SEL001', 'MAJ014');
INSERT INTO `selection_major` VALUES ('SEL001', 'MAJ007');
INSERT INTO `selection_major` VALUES ('SEL001', 'MAJ021');
INSERT INTO `selection_major` VALUES ('SEL002', 'MAJ012');
INSERT INTO `selection_major` VALUES ('SEL002', 'MAJ008');
INSERT INTO `selection_major` VALUES ('SEL002', 'MAJ001');
INSERT INTO `selection_major` VALUES ('SEL002', 'MAJ020');
INSERT INTO `selection_major` VALUES ('SEL002', 'MAJ028');
INSERT INTO `selection_major` VALUES ('SEL003', 'MAJ019');
INSERT INTO `selection_major` VALUES ('SEL003', 'MAJ026');
INSERT INTO `selection_major` VALUES ('SEL003', 'MAJ025');
INSERT INTO `selection_major` VALUES ('SEL003', 'MAJ012');
INSERT INTO `selection_major` VALUES ('SEL003', 'MAJ013');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ000');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ001');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ002');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ003');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ004');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ005');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ006');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ007');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ008');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ009');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ010');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ011');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ012');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ013');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ014');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ015');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ016');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ017');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ018');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ019');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ020');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ021');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ022');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ023');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ024');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ025');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ026');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ027');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ028');
INSERT INTO `selection_major` VALUES ('SEL861', 'MAJ029');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ016');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ022');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ006');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ021');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ001');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ003');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ005');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ025');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ004');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ010');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ015');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ026');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ029');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ023');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ000');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ007');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ018');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ017');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ019');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ024');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ027');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ002');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ008');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ012');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ013');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ014');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ028');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ009');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ011');
INSERT INTO `selection_major` VALUES ('SEL968', 'MAJ020');

-- ----------------------------
-- Table structure for selection_student
-- ----------------------------
DROP TABLE IF EXISTS `selection_student`;
CREATE TABLE `selection_student`  (
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '学生ID',
  `course_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID',
  `class_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '班级ID（0=未选班）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '状态：N-未选择/P-预选择/C-已选择',
  PRIMARY KEY (`student_id`, `course_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '学生选课表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of selection_student
-- ----------------------------
INSERT INTO `selection_student` VALUES ('S2023000', 'C000', 'CLS00000', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C001', 'CLS00103', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C002', 'CLS00203', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C003', 'CLS00304', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C004', 'CLS00402', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C005', 'CLS00500', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C006', 'CLS00603', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C007', 'CLS00702', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C008', 'CLS00800', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C009', 'CLS00902', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C010', 'CLS01004', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C011', 'CLS01101', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C012', 'CLS01204', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C013', 'CLS01303', 'C');
INSERT INTO `selection_student` VALUES ('S2023000', 'C014', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C015', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C016', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C017', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C018', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C019', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C020', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C021', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C022', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C023', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C024', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C025', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C026', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C027', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C028', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023000', 'C029', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C000', 'CLS00003', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C001', 'CLS00103', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C002', 'CLS00201', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C003', 'CLS00304', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C004', 'CLS00403', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C005', 'CLS00502', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C006', 'CLS00603', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C007', 'CLS00703', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C008', 'CLS00803', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C009', 'CLS00903', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C010', 'CLS01001', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C011', 'CLS01103', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C012', 'CLS01201', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C013', 'CLS01301', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C014', 'CLS01404', 'C');
INSERT INTO `selection_student` VALUES ('S2023001', 'C015', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C016', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C017', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C018', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C019', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C020', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C021', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C022', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C023', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C024', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C025', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C026', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C027', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C028', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023001', 'C029', '0', 'N');
INSERT INTO `selection_student` VALUES ('S2023002', 'C002', 'CLS00202', 'C');
INSERT INTO `selection_student` VALUES ('S2023002', 'C006', 'CLS00602', 'C');
INSERT INTO `selection_student` VALUES ('S2023002', 'C009', 'CLS00900', 'C');
INSERT INTO `selection_student` VALUES ('S2023002', 'C011', 'CLS01101', 'C');
INSERT INTO `selection_student` VALUES ('S2023002', 'C014', 'CLS01404', 'C');
INSERT INTO `selection_student` VALUES ('S2023002', 'C015', 'CLS01504', 'C');
INSERT INTO `selection_student` VALUES ('S2023002', 'C021', 'CLS02103', 'C');
INSERT INTO `selection_student` VALUES ('S2023002', 'C022', 'CLS02202', 'C');
INSERT INTO `selection_student` VALUES ('S2023002', 'C026', 'CLS02604', 'C');

-- ----------------------------
-- Table structure for sign
-- ----------------------------
DROP TABLE IF EXISTS `sign`;
CREATE TABLE `sign`  (
  `sign_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '签到日志记录id',
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程id',
  `class_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程班id',
  `student_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '签到学生id',
  `sign_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '签到时间',
  `student_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '签到学生姓名',
  PRIMARY KEY (`sign_id`, `course_id`, `class_id`, `student_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sign
-- ----------------------------
INSERT INTO `sign` VALUES ('1757409832327-2389', 'C000', 'CLS00000', 'S2023000', '2025-09-09T09:32:55.566Z', '黄洋');
INSERT INTO `sign` VALUES ('1757410431192-4343', 'C014', 'CLS01403', 'S2023001', '2025-09-09T09:33:58.089Z', '周秀英');
INSERT INTO `sign` VALUES ('1757410431192-4343', 'C014', 'CLS01403', 'S2023002', '2025-09-09T09:36:21.500Z', '高艳');
INSERT INTO `sign` VALUES ('1757410431192-4343', 'C014', 'CLS01403', 'S2023003', '2025-09-09T09:36:54.145Z', '何敏');
INSERT INTO `sign` VALUES ('1757410691990-7909', 'C018', 'CLS01800', 'S2023000', '2025-09-09T09:38:16.587Z', '黄洋');
INSERT INTO `sign` VALUES ('1757410920831-9620', 'C018', 'CLS01800', 'S2023000', '2025-09-09T09:42:57.469Z', '黄洋');
INSERT INTO `sign` VALUES ('1757499882176-9314', 'C004', 'CLS00401', 'S2023000', '2025-09-10T10:27:11.258Z', '黄洋');
INSERT INTO `sign` VALUES ('1757502259910-2247', 'C000', 'CLS00000', 'S2023000', '2025-09-10T11:10:11.447Z', '黄洋');
INSERT INTO `sign` VALUES ('1757726983924-2160', 'C014', 'CLS01403', 'S2023076', '2025-09-13T01:30:22.867Z', '吴艳');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '学生ID，主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '学生姓名',
  `major_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '所属专业ID，外键',
  `start_year` decimal(4, 0) NULL DEFAULT NULL COMMENT '入学年份',
  `end_year` decimal(4, 0) NULL DEFAULT NULL COMMENT '毕业年份',
  `telephone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '电子邮箱',
  `information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '附加信息',
  `gpa` decimal(7, 6) NULL DEFAULT NULL,
  `attempted_credit` decimal(5, 2) NULL DEFAULT NULL,
  `earned_credit` decimal(5, 2) NULL DEFAULT NULL,
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '性别信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_student_major`(`major_id` ASC) USING BTREE,
  INDEX `idx_student_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '学生信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('S2023000', '黄洋', 'MAJ001', 2023, 2027, '19997738257', 'student000@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023001', '周秀英', 'MAJ001', 2023, 2027, '19997738257', 'student001@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023002', '高艳', 'MAJ001', 2023, 2027, '19997738257', 'student002@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023003', '何敏', 'MAJ001', 2023, 2027, '19997738257', 'student003@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023004', '周宇', 'MAJ001', 2023, 2027, '19997738257', 'student004@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023005', '赵宇', 'MAJ001', 2023, 2027, '19997738257', 'student005@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023006', '马强', 'MAJ001', 2023, 2027, '19997738257', 'student006@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023007', '胡秀英', 'MAJ001', 2023, 2027, '19997738257', 'student007@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023008', '何建', 'MAJ001', 2023, 2027, '19997738257', 'student008@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023009', '马旭', 'MAJ001', 2023, 2027, '19997738257', 'student009@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023010', '杨伟', 'MAJ001', 2023, 2027, '19997738257', 'student010@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023011', '徐平', 'MAJ001', 2023, 2027, '19997738257', 'student011@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023012', '王亮', 'MAJ001', 2023, 2027, '19997738257', 'student012@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023013', '朱军', 'MAJ001', 2023, 2027, '19997738257', 'student013@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023014', '王霞', 'MAJ001', 2023, 2027, '19997738257', 'student014@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023015', '马强', 'MAJ001', 2023, 2027, '19997738257', 'student015@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023016', '徐静', 'MAJ001', 2023, 2027, '19997738257', 'student016@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023017', '何鹏', 'MAJ001', 2023, 2027, '19997738257', 'student017@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023018', '胡军', 'MAJ001', 2023, 2027, '19997738257', 'student018@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023019', '徐鹏', 'MAJ001', 2023, 2027, '19997738257', 'student019@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023020', '张洋', 'MAJ001', 2023, 2027, '19997738257', 'student020@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023021', '胡婷', 'MAJ001', 2023, 2027, '19997738257', 'student021@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023022', '李宁', 'MAJ001', 2023, 2027, '19997738257', 'student022@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023023', '吴霞', 'MAJ001', 2023, 2027, '19997738257', 'student023@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023024', '郭强', 'MAJ001', 2023, 2027, '19997738257', 'student024@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023025', '高明', 'MAJ001', 2023, 2027, '19997738257', 'student025@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023026', '杨鹏', 'MAJ001', 2023, 2027, '19997738257', 'student026@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023027', '周超', 'MAJ001', 2023, 2027, '19997738257', 'student027@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023028', '徐旭', 'MAJ001', 2023, 2027, '19997738257', 'student028@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023029', '何超', 'MAJ001', 2023, 2027, '19997738257', 'student029@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023030', '杨艳', 'MAJ001', 2023, 2027, '19997738257', 'student030@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023031', '赵鹏', 'MAJ001', 2023, 2027, '19997738257', 'student031@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023032', '陈亮', 'MAJ001', 2023, 2027, '19997738257', 'student032@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023033', '吴健', 'MAJ001', 2023, 2027, '19997738257', 'student033@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023034', '朱娟', 'MAJ001', 2023, 2027, '19997738257', 'student034@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023035', '杨鑫', 'MAJ001', 2023, 2027, '19997738257', 'student035@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023036', '罗霞', 'MAJ001', 2023, 2027, '19997738257', 'student036@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023037', '陈艳', 'MAJ001', 2023, 2027, '19997738257', 'student037@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023038', '刘亮', 'MAJ001', 2023, 2027, '19997738257', 'student038@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023039', '孙波', 'MAJ001', 2023, 2027, '19997738257', 'student039@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023040', '陈宇', 'MAJ001', 2023, 2027, '19997738257', 'student040@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023041', '陈鹏', 'MAJ001', 2023, 2027, '19997738257', 'student041@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023042', '郭明', 'MAJ001', 2023, 2027, '19997738257', 'student042@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023043', '刘明', 'MAJ001', 2023, 2027, '19997738257', 'student043@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023044', '黄杰', 'MAJ001', 2023, 2027, '19997738257', 'student044@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023045', '李宁', 'MAJ001', 2023, 2027, '19997738257', 'student045@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023046', '王伟', 'MAJ001', 2023, 2027, '19997738257', 'student046@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023047', '何艳', 'MAJ001', 2023, 2027, '19997738257', 'student047@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023048', '胡强', 'MAJ001', 2023, 2027, '19997738257', 'student048@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023049', '胡军', 'MAJ001', 2023, 2027, '19997738257', 'student049@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023050', '赵敏', 'MAJ001', 2023, 2027, '19997738257', 'student050@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023051', '吴强', 'MAJ001', 2023, 2027, '19997738257', 'student051@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023052', '李宁', 'MAJ001', 2023, 2027, '19997738257', 'student052@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023053', '胡健', 'MAJ001', 2023, 2027, '19997738257', 'student053@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023054', '何波', 'MAJ001', 2023, 2027, '19997738257', 'student054@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023055', '赵伟', 'MAJ001', 2023, 2027, '19997738257', 'student055@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023056', '黄波', 'MAJ001', 2023, 2027, '19997738257', 'student056@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023057', '陈明', 'MAJ001', 2023, 2027, '19997738257', 'student057@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023058', '林勇', 'MAJ001', 2023, 2027, '19997738257', 'student058@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023059', '杨波', 'MAJ001', 2023, 2027, '19997738257', 'student059@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023060', '胡娟', 'MAJ001', 2023, 2027, '19997738257', 'student060@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023061', '周旭', 'MAJ001', 2023, 2027, '19997738257', 'student061@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023062', '林勇', 'MAJ001', 2023, 2027, '19997738257', 'student062@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023063', '胡勇', 'MAJ001', 2023, 2027, '19997738257', 'student063@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023064', '刘强', 'MAJ001', 2023, 2027, '19997738257', 'student064@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023065', '朱旭', 'MAJ001', 2023, 2027, '19997738257', 'student065@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023066', '罗鹏', 'MAJ001', 2023, 2027, '19997738257', 'student066@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023067', '王伟', 'MAJ001', 2023, 2027, '19997738257', 'student067@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023068', '刘艳', 'MAJ001', 2023, 2027, '19997738257', 'student068@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023069', '周磊', 'MAJ001', 2023, 2027, '19997738257', 'student069@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023070', '罗秀英', 'MAJ001', 2023, 2027, '19997738257', 'student070@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023071', '马勇', 'MAJ001', 2023, 2027, '19997738257', 'student071@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023072', '王波', 'MAJ001', 2023, 2027, '19997738257', 'student072@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023073', '孙杰', 'MAJ001', 2023, 2027, '19997738257', 'student073@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023074', '孙宇', 'MAJ001', 2023, 2027, '19997738257', 'student074@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023075', '黄波', 'MAJ001', 2023, 2027, '19997738257', 'student075@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023076', '吴艳', 'MAJ001', 2023, 2027, '19997738257', 'student076@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023077', '杨娟', 'MAJ001', 2023, 2027, '19997738257', 'student077@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023078', '吴宁', 'MAJ001', 2023, 2027, '19997738257', 'student078@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023079', '马霞', 'MAJ001', 2023, 2027, '19997738257', 'student079@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023080', '郭艳', 'MAJ001', 2023, 2027, '19997738257', 'student080@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023081', '林杰', 'MAJ001', 2023, 2027, '19997738257', 'student081@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023082', '胡军', 'MAJ001', 2023, 2027, '19997738257', 'student082@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023083', '郭磊', 'MAJ001', 2023, 2027, '19997738257', 'student083@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023084', '王宇', 'MAJ001', 2023, 2027, '19997738257', 'student084@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023085', '何伟', 'MAJ001', 2023, 2027, '19997738257', 'student085@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023086', '吴超', 'MAJ001', 2023, 2027, '19997738257', 'student086@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023087', '赵敏', 'MAJ001', 2023, 2027, '19997738257', 'student087@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023088', '郭帅', 'MAJ001', 2023, 2027, '19997738257', 'student088@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023089', '周婷', 'MAJ001', 2023, 2027, '19997738257', 'student089@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023090', '胡秀英', 'MAJ001', 2023, 2027, '19997738257', 'student090@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023091', '高波', 'MAJ001', 2023, 2027, '19997738257', 'student091@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023092', '张帅', 'MAJ001', 2023, 2027, '19997738257', 'student092@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023093', '胡波', 'MAJ001', 2023, 2027, '19997738257', 'student093@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023094', '李伟', 'MAJ001', 2023, 2027, '19997738257', 'student094@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023095', '朱帅', 'MAJ001', 2023, 2027, '19997738257', 'student095@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023096', '何超', 'MAJ001', 2023, 2027, '19997738257', 'student096@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023097', '高帅', 'MAJ001', 2023, 2027, '19997738257', 'student097@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023098', '黄平', 'MAJ001', 2023, 2027, '19997738257', 'student098@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023099', '吴帅', 'MAJ001', 2023, 2027, '19997738257', 'student099@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023100', '吴宁', 'MAJ001', 2023, 2027, '19997738257', 'student100@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023101', '孙宁', 'MAJ001', 2023, 2027, '19997738257', 'student101@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023102', '朱平', 'MAJ001', 2023, 2027, '19997738257', 'student102@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023103', '刘艳', 'MAJ001', 2023, 2027, '19997738257', 'student103@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023104', '胡霞', 'MAJ001', 2023, 2027, '19997738257', 'student104@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023105', '张鹏', 'MAJ001', 2023, 2027, '19997738257', 'student105@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023106', '刘超', 'MAJ001', 2023, 2027, '19997738257', 'student106@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023107', '李亮', 'MAJ001', 2023, 2027, '19997738257', 'student107@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023108', '林秀英', 'MAJ001', 2023, 2027, '19997738257', 'student108@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023109', '刘鑫', 'MAJ001', 2023, 2027, '19997738257', 'student109@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023110', '黄艳', 'MAJ001', 2023, 2027, '19997738257', 'student110@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023111', '赵帅', 'MAJ001', 2023, 2027, '19997738257', 'student111@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023112', '朱宁', 'MAJ001', 2023, 2027, '19997738257', 'student112@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023113', '黄娜', 'MAJ001', 2023, 2027, '19997738257', 'student113@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023114', '郭婷', 'MAJ001', 2023, 2027, '19997738257', 'student114@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023115', '何洋', 'MAJ001', 2023, 2027, '19997738257', 'student115@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023116', '杨艳', 'MAJ001', 2023, 2027, '19997738257', 'student116@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023117', '罗亮', 'MAJ001', 2023, 2027, '19997738257', 'student117@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023118', '罗帅', 'MAJ001', 2023, 2027, '19997738257', 'student118@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023119', '马敏', 'MAJ001', 2023, 2027, '19997738257', 'student119@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023120', '杨平', 'MAJ001', 2023, 2027, '19997738257', 'student120@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023121', '徐旭', 'MAJ001', 2023, 2027, '19997738257', 'student121@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023122', '黄艳', 'MAJ001', 2023, 2027, '19997738257', 'student122@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023123', '罗宁', 'MAJ001', 2023, 2027, '19997738257', 'student123@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023124', '郭艳', 'MAJ001', 2023, 2027, '19997738257', 'student124@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023125', '王亮', 'MAJ001', 2023, 2027, '19997738257', 'student125@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023126', '马平', 'MAJ001', 2023, 2027, '19997738257', 'student126@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023127', '吴宁', 'MAJ001', 2023, 2027, '19997738257', 'student127@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023128', '何亮', 'MAJ001', 2023, 2027, '19997738257', 'student128@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023129', '黄杰', 'MAJ001', 2023, 2027, '19997738257', 'student129@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023130', '赵建', 'MAJ001', 2023, 2027, '19997738257', 'student130@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023131', '朱洋', 'MAJ001', 2023, 2027, '19997738257', 'student131@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023132', '黄娟', 'MAJ001', 2023, 2027, '19997738257', 'student132@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023133', '杨宇', 'MAJ001', 2023, 2027, '19997738257', 'student133@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023134', '孙宇', 'MAJ001', 2023, 2027, '19997738257', 'student134@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023135', '胡芳', 'MAJ001', 2023, 2027, '19997738257', 'student135@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023136', '黄强', 'MAJ001', 2023, 2027, '19997738257', 'student136@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023137', '李娜', 'MAJ001', 2023, 2027, '19997738257', 'student137@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023138', '郭旭', 'MAJ001', 2023, 2027, '19997738257', 'student138@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023139', '郭波', 'MAJ001', 2023, 2027, '19997738257', 'student139@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023140', '陈超', 'MAJ001', 2023, 2027, '19997738257', 'student140@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023141', '吴超', 'MAJ001', 2023, 2027, '19997738257', 'student141@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023142', '吴强', 'MAJ001', 2023, 2027, '19997738257', 'student142@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023143', '朱艳', 'MAJ001', 2023, 2027, '19997738257', 'student143@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023144', '郭帅', 'MAJ001', 2023, 2027, '19997738257', 'student144@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023145', '黄宇', 'MAJ001', 2023, 2027, '19997738257', 'student145@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023146', '胡军', 'MAJ001', 2023, 2027, '19997738257', 'student146@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023147', '王艳', 'MAJ001', 2023, 2027, '19997738257', 'student147@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023148', '林波', 'MAJ001', 2023, 2027, '19997738257', 'student148@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023149', '胡磊', 'MAJ001', 2023, 2027, '19997738257', 'student149@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023150', '朱建', 'MAJ001', 2023, 2027, '19997738257', 'student150@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023151', '杨静', 'MAJ001', 2023, 2027, '19997738257', 'student151@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023152', '高杰', 'MAJ001', 2023, 2027, '19997738257', 'student152@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023153', '罗雪', 'MAJ001', 2023, 2027, '19997738257', 'student153@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023154', '高宁', 'MAJ001', 2023, 2027, '19997738257', 'student154@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023155', '陈健', 'MAJ001', 2023, 2027, '19997738257', 'student155@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023156', '李鹏', 'MAJ001', 2023, 2027, '19997738257', 'student156@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023157', '周勇', 'MAJ001', 2023, 2027, '19997738257', 'student157@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023158', '何婷', 'MAJ001', 2023, 2027, '19997738257', 'student158@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023159', '孙健', 'MAJ001', 2023, 2027, '19997738257', 'student159@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023160', '郭洋', 'MAJ001', 2023, 2027, '19997738257', 'student160@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023161', '吴平', 'MAJ001', 2023, 2027, '19997738257', 'student161@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023162', '朱勇', 'MAJ001', 2023, 2027, '19997738257', 'student162@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023163', '王婷', 'MAJ001', 2023, 2027, '19997738257', 'student163@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023164', '黄秀英', 'MAJ001', 2023, 2027, '19997738257', 'student164@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023165', '张磊', 'MAJ001', 2023, 2027, '19997738257', 'student165@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023166', '郭芳', 'MAJ001', 2023, 2027, '19997738257', 'student166@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023167', '吴勇', 'MAJ001', 2023, 2027, '19997738257', 'student167@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023168', '胡鑫', 'MAJ001', 2023, 2027, '19997738257', 'student168@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023169', '黄军', 'MAJ001', 2023, 2027, '19997738257', 'student169@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023170', '黄军', 'MAJ001', 2023, 2027, '19997738257', 'student170@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023171', '陈军', 'MAJ001', 2023, 2027, '19997738257', 'student171@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023172', '赵静', 'MAJ001', 2023, 2027, '19997738257', 'student172@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023173', '张鑫', 'MAJ001', 2023, 2027, '19997738257', 'student173@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023174', '周艳', 'MAJ001', 2023, 2027, '19997738257', 'student174@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023175', '马勇', 'MAJ001', 2023, 2027, '19997738257', 'student175@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023176', '赵明', 'MAJ001', 2023, 2027, '19997738257', 'student176@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023177', '刘杰', 'MAJ001', 2023, 2027, '19997738257', 'student177@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023178', '张健', 'MAJ001', 2023, 2027, '19997738257', 'student178@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023179', '赵秀英', 'MAJ001', 2023, 2027, '19997738257', 'student179@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023180', '郭娜', 'MAJ001', 2023, 2027, '19997738257', 'student180@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023181', '罗明', 'MAJ001', 2023, 2027, '19997738257', 'student181@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023182', '胡鑫', 'MAJ001', 2023, 2027, '19997738257', 'student182@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023183', '吴静', 'MAJ001', 2023, 2027, '19997738257', 'student183@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023184', '马军', 'MAJ001', 2023, 2027, '19997738257', 'student184@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023185', '林娟', 'MAJ001', 2023, 2027, '19997738257', 'student185@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023186', '马雪', 'MAJ001', 2023, 2027, '19997738257', 'student186@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023187', '赵强', 'MAJ001', 2023, 2027, '19997738257', 'student187@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023188', '孙军', 'MAJ001', 2023, 2027, '19997738257', 'student188@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023189', '郭平', 'MAJ001', 2023, 2027, '19997738257', 'student189@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023190', '李超', 'MAJ001', 2023, 2027, '19997738257', 'student190@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023191', '朱帅', 'MAJ001', 2023, 2027, '19997738257', 'student191@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023192', '朱建', 'MAJ001', 2023, 2027, '19997738257', 'student192@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023193', '胡军', 'MAJ001', 2023, 2027, '19997738257', 'student193@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023194', '周伟', 'MAJ001', 2023, 2027, '19997738257', 'student194@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023195', '吴雪', 'MAJ001', 2023, 2027, '19997738257', 'student195@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023196', '周芳', 'MAJ001', 2023, 2027, '19997738257', 'student196@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023197', '胡娜', 'MAJ001', 2023, 2027, '19997738257', 'student197@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023198', '马霞', 'MAJ001', 2023, 2027, '19997738257', 'student198@school.com', NULL, 0.000000, 0.00, 0.00, '男');
INSERT INTO `student` VALUES ('S2023199', '徐宇', 'MAJ001', 2023, 2027, '19997738257', 'student199@school.com', NULL, 0.000000, 0.00, 0.00, '男');

-- ----------------------------
-- Table structure for student_contest
-- ----------------------------
DROP TABLE IF EXISTS `student_contest`;
CREATE TABLE `student_contest`  (
  `student_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '学生ID，外键',
  `contest_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '竞赛ID，外键',
  PRIMARY KEY (`student_id`, `contest_id`) USING BTREE,
  INDEX `idx_sc_contest`(`contest_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '学生-竞赛关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student_contest
-- ----------------------------
INSERT INTO `student_contest` VALUES ('S202301', 'CT2025001');
INSERT INTO `student_contest` VALUES ('S202309', 'CT2025001');
INSERT INTO `student_contest` VALUES ('S202302', 'CT2025002');
INSERT INTO `student_contest` VALUES ('S202303', 'CT2025003');
INSERT INTO `student_contest` VALUES ('S202304', 'CT2025004');
INSERT INTO `student_contest` VALUES ('S202313', 'CT2025004');
INSERT INTO `student_contest` VALUES ('S202305', 'CT2025006');
INSERT INTO `student_contest` VALUES ('S202306', 'CT2025007');
INSERT INTO `student_contest` VALUES ('S202301', 'CT2025008');
INSERT INTO `student_contest` VALUES ('S202311', 'CT2025009');
INSERT INTO `student_contest` VALUES ('S202314', 'CT2025009');
INSERT INTO `student_contest` VALUES ('S202307', 'CT2025010');
INSERT INTO `student_contest` VALUES ('S202308', 'CT2025010');
INSERT INTO `student_contest` VALUES ('S202310', 'CT2025011');
INSERT INTO `student_contest` VALUES ('S202312', 'CT2025013');

-- ----------------------------
-- Table structure for takes
-- ----------------------------
DROP TABLE IF EXISTS `takes`;
CREATE TABLE `takes`  (
  `student_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '学生ID，外键',
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `class_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '班级ID，外键',
  `semester` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `year` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `grade` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '成绩等级',
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '修读状态',
  `paper` longblob NULL COMMENT '试卷信息',
  PRIMARY KEY (`student_id`, `course_id`, `class_id`, `semester`, `year`) USING BTREE,
  INDEX `idx_takes_grade`(`grade` ASC) USING BTREE,
  INDEX `idx_takes_student`(`student_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '学生选课表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of takes
-- ----------------------------
INSERT INTO `takes` VALUES ('S2023000', 'C000', 'CLS00000', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C001', 'CLS00103', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C002', 'CLS00203', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C003', 'CLS00304', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C004', 'CLS00402', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C005', 'CLS00500', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C006', 'CLS00603', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C007', 'CLS00702', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C008', 'CLS00800', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C009', 'CLS00902', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C010', 'CLS01004', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C011', 'CLS01101', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C012', 'CLS01204', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023000', 'C013', 'CLS01303', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C000', 'CLS00000', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C001', 'CLS00103', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C002', 'CLS00201', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C003', 'CLS00304', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C004', 'CLS00403', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C005', 'CLS00502', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C006', 'CLS00603', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C007', 'CLS00703', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C008', 'CLS00803', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C009', 'CLS00903', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C010', 'CLS01001', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C011', 'CLS01103', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023001', 'C012', 'CLS01201', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C002', 'CLS00202', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C006', 'CLS00602', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C009', 'CLS00900', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C011', 'CLS01101', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C014', 'CLS01404', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C015', 'CLS01504', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C021', 'CLS02103', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C022', 'CLS02202', '0', '2025', NULL, '正在修读', NULL);
INSERT INTO `takes` VALUES ('S2023002', 'C026', 'CLS02604', '0', '2025', NULL, '正在修读', NULL);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `teacher_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教师ID，主键',
  `teacher_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教师姓名',
  `major_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '所属专业ID，外键',
  `start_year` decimal(4, 0) NULL DEFAULT NULL COMMENT '入职年份',
  `work_year` decimal(4, 0) NULL DEFAULT NULL,
  `telephone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '电子邮箱',
  `information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '附加信息',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '教师信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('T2023000', '吴雪', 'MAJ001', 2020, 5, '13800138000', 'teacher00@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023001', '林健', 'MAJ001', 2010, 15, '13800138001', 'teacher01@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023002', '李娜', 'MAJ001', 2021, 4, '13800138002', 'teacher02@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023003', '朱旭', 'MAJ001', 2011, 14, '13800138003', 'teacher03@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023004', '杨芳', 'MAJ001', 2022, 3, '13800138004', 'teacher04@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023005', '马平', 'MAJ001', 2012, 13, '13800138005', 'teacher05@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023006', '李亮', 'MAJ001', 2023, 2, '13800138006', 'teacher06@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023007', '周建', 'MAJ001', 2013, 12, '13800138007', 'teacher07@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023008', '孙秀英', 'MAJ001', 2024, 1, '13800138008', 'teacher08@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023009', '李磊', 'MAJ001', 2013, 12, '13800138009', 'teacher09@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023010', '何帅', 'MAJ001', 2024, 1, '138001380010', 'teacher10@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023011', '朱磊', 'MAJ001', 2014, 11, '138001380011', 'teacher11@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023012', '罗勇', 'MAJ001', 2025, 0, '138001380012', 'teacher12@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023013', '周宁', 'MAJ001', 2015, 10, '138001380013', 'teacher13@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023014', '何伟', 'MAJ001', 2005, 20, '138001380014', 'teacher14@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023015', '杨霞', 'MAJ001', 2016, 9, '138001380015', 'teacher15@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023016', '何平', 'MAJ001', 2006, 19, '138001380016', 'teacher16@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023017', '赵亮', 'MAJ001', 2017, 8, '138001380017', 'teacher17@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023018', '吴霞', 'MAJ001', 2007, 18, '138001380018', 'teacher18@school.com', '系统初始化生成的教师数据', '男', '讲师');
INSERT INTO `teacher` VALUES ('T2023019', '周艳', 'MAJ001', 2018, 7, '138001380019', 'teacher19@school.com', '系统初始化生成的教师数据', '男', '讲师');

-- ----------------------------
-- Table structure for teacher_exam
-- ----------------------------
DROP TABLE IF EXISTS `teacher_exam`;
CREATE TABLE `teacher_exam`  (
  `teacher_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '监考教师id',
  `exam_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '考试id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher_exam
-- ----------------------------
INSERT INTO `teacher_exam` VALUES ('T2023016', 'E0');
INSERT INTO `teacher_exam` VALUES ('T2023002', 'E0');
INSERT INTO `teacher_exam` VALUES ('T2023005', 'E1');
INSERT INTO `teacher_exam` VALUES ('T2023006', 'E1');
INSERT INTO `teacher_exam` VALUES ('T2023010', 'E2');
INSERT INTO `teacher_exam` VALUES ('T2023013', 'E2');
INSERT INTO `teacher_exam` VALUES ('T2023000', 'E3');
INSERT INTO `teacher_exam` VALUES ('T2023005', 'E3');
INSERT INTO `teacher_exam` VALUES ('T2023002', 'E4');
INSERT INTO `teacher_exam` VALUES ('T2023013', 'E4');
INSERT INTO `teacher_exam` VALUES ('T2023011', 'E5');
INSERT INTO `teacher_exam` VALUES ('T2023014', 'E5');
INSERT INTO `teacher_exam` VALUES ('T2023009', 'E6');
INSERT INTO `teacher_exam` VALUES ('T2023015', 'E6');
INSERT INTO `teacher_exam` VALUES ('T2023014', 'E7');
INSERT INTO `teacher_exam` VALUES ('T2023016', 'E7');
INSERT INTO `teacher_exam` VALUES ('T2023007', 'E8');
INSERT INTO `teacher_exam` VALUES ('T2023016', 'E8');
INSERT INTO `teacher_exam` VALUES ('T2023002', 'E9');
INSERT INTO `teacher_exam` VALUES ('T2023004', 'E9');

-- ----------------------------
-- Table structure for teaches
-- ----------------------------
DROP TABLE IF EXISTS `teaches`;
CREATE TABLE `teaches`  (
  `teacher_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教师ID，外键',
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `class_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '班级ID，外键',
  `semester` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `year` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  PRIMARY KEY (`teacher_id`, `course_id`, `class_id`, `semester`, `year`) USING BTREE,
  INDEX `idx_teaches_class`(`course_id` ASC, `class_id` ASC, `semester` ASC, `year` ASC) USING BTREE,
  INDEX `idx_teaches_teacher`(`teacher_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '教师授课关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teaches
-- ----------------------------
INSERT INTO `teaches` VALUES ('T2023000', 'C000', 'CLS00000', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023000', 'C004', 'CLS00401', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023000', 'C010', 'CLS01003', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023000', 'C014', 'CLS01403', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023000', 'C017', 'CLS01702', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023000', 'C018', 'CLS01800', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023000', 'C022', 'CLS02201', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023000', 'C024', 'CLS02403', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023000', 'C024', 'CLS02404', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C006', 'CLS00604', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C007', 'CLS00700', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C011', 'CLS01100', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C013', 'CLS01300', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C014', 'CLS01400', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C021', 'CLS02104', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C022', 'CLS02200', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C023', 'CLS02302', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C029', 'CLS02900', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023001', 'C029', 'CLS02901', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C004', 'CLS00400', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C004', 'CLS00402', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C006', 'CLS00602', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C007', 'CLS00704', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C016', 'CLS01603', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C016', 'CLS01604', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C018', 'CLS01804', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C019', 'CLS01904', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C023', 'CLS02301', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C024', 'CLS02400', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C024', 'CLS02402', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023002', 'C025', 'CLS02504', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023003', 'C004', 'CLS00403', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023003', 'C004', 'CLS00404', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023003', 'C006', 'CLS00600', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023003', 'C017', 'CLS01700', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023003', 'C017', 'CLS01704', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023003', 'C024', 'CLS02401', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023004', 'C008', 'CLS00800', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023004', 'C010', 'CLS01001', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023004', 'C010', 'CLS01004', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023004', 'C014', 'CLS01401', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023004', 'C026', 'CLS02600', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023004', 'C026', 'CLS02603', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023004', 'C029', 'CLS02903', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C005', 'CLS00504', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C009', 'CLS00903', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C015', 'CLS01503', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C015', 'CLS01504', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C019', 'CLS01900', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C020', 'CLS02000', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C021', 'CLS02103', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C022', 'CLS02202', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C027', 'CLS02702', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C028', 'CLS02802', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C028', 'CLS02804', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023005', 'C029', 'CLS02904', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023006', 'C005', 'CLS00501', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023006', 'C007', 'CLS00702', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C001', 'CLS00104', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C005', 'CLS00500', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C006', 'CLS00603', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C008', 'CLS00803', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C009', 'CLS00901', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C009', 'CLS00904', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C013', 'CLS01304', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C022', 'CLS02204', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023007', 'C026', 'CLS02602', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C000', 'CLS00002', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C001', 'CLS00101', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C005', 'CLS00503', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C007', 'CLS00701', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C010', 'CLS01002', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C013', 'CLS01301', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C013', 'CLS01303', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C015', 'CLS01500', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C017', 'CLS01701', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C020', 'CLS02002', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C023', 'CLS02300', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C028', 'CLS02800', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023008', 'C028', 'CLS02801', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023009', 'C001', 'CLS00103', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023009', 'C008', 'CLS00801', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023009', 'C011', 'CLS01103', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023009', 'C018', 'CLS01802', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023009', 'C028', 'CLS02803', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023010', 'C006', 'CLS00601', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023010', 'C029', 'CLS02902', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023011', 'C016', 'CLS01600', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023011', 'C018', 'CLS01801', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023011', 'C023', 'CLS02304', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023012', 'C002', 'CLS00202', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023012', 'C007', 'CLS00703', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023012', 'C011', 'CLS01104', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023012', 'C012', 'CLS01203', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023012', 'C013', 'CLS01302', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023012', 'C022', 'CLS02203', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023012', 'C027', 'CLS02703', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023013', 'C000', 'CLS00001', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023013', 'C001', 'CLS00102', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023013', 'C003', 'CLS00301', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023013', 'C010', 'CLS01000', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023013', 'C014', 'CLS01404', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023013', 'C015', 'CLS01502', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023013', 'C018', 'CLS01803', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023013', 'C019', 'CLS01902', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023014', 'C009', 'CLS00900', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023014', 'C015', 'CLS01501', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023014', 'C027', 'CLS02700', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C000', 'CLS00003', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C001', 'CLS00100', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C002', 'CLS00203', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C005', 'CLS00502', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C008', 'CLS00802', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C012', 'CLS01201', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C012', 'CLS01202', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C019', 'CLS01901', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023015', 'C021', 'CLS02100', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023016', 'C000', 'CLS00004', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023016', 'C003', 'CLS00302', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023016', 'C003', 'CLS00304', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023016', 'C011', 'CLS01101', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023016', 'C011', 'CLS01102', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023016', 'C020', 'CLS02003', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023016', 'C023', 'CLS02303', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C002', 'CLS00204', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C008', 'CLS00804', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C012', 'CLS01204', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C014', 'CLS01402', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C016', 'CLS01601', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C016', 'CLS01602', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C019', 'CLS01903', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C020', 'CLS02001', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C021', 'CLS02101', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C021', 'CLS02102', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C025', 'CLS02501', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023017', 'C027', 'CLS02701', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023018', 'C002', 'CLS00201', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023018', 'C017', 'CLS01703', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023018', 'C020', 'CLS02004', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023018', 'C025', 'CLS02500', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023018', 'C025', 'CLS02502', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023018', 'C026', 'CLS02601', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023018', 'C027', 'CLS02704', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023019', 'C002', 'CLS00200', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023019', 'C003', 'CLS00300', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023019', 'C003', 'CLS00303', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023019', 'C009', 'CLS00902', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023019', 'C012', 'CLS01200', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023019', 'C025', 'CLS02503', '0', '2025');
INSERT INTO `teaches` VALUES ('T2023019', 'C026', 'CLS02604', '0', '2025');

-- ----------------------------
-- Table structure for time_slot
-- ----------------------------
DROP TABLE IF EXISTS `time_slot`;
CREATE TABLE `time_slot`  (
  `time_slot_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '时间段ID，主键',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '星期几',
  PRIMARY KEY (`time_slot_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '时间段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of time_slot
-- ----------------------------
INSERT INTO `time_slot` VALUES ('0', '2025-01-01 08:00:00', '2025-01-01 08:45:00', '0');
INSERT INTO `time_slot` VALUES ('1', '2025-01-01 08:55:00', '2025-01-01 09:40:00', '0');
INSERT INTO `time_slot` VALUES ('10', '2025-01-01 17:10:00', '2025-01-01 17:55:00', '0');
INSERT INTO `time_slot` VALUES ('11', '2025-01-01 18:05:00', '2025-01-01 18:50:00', '0');
INSERT INTO `time_slot` VALUES ('12', '2025-01-01 08:00:00', '2025-01-01 08:45:00', '1');
INSERT INTO `time_slot` VALUES ('13', '2025-01-01 08:55:00', '2025-01-01 09:40:00', '1');
INSERT INTO `time_slot` VALUES ('14', '2025-01-01 09:50:00', '2025-01-01 10:35:00', '1');
INSERT INTO `time_slot` VALUES ('15', '2025-01-01 10:45:00', '2025-01-01 11:30:00', '1');
INSERT INTO `time_slot` VALUES ('16', '2025-01-01 11:40:00', '2025-01-01 12:25:00', '1');
INSERT INTO `time_slot` VALUES ('17', '2025-01-01 12:35:00', '2025-01-01 13:20:00', '1');
INSERT INTO `time_slot` VALUES ('18', '2025-01-01 13:30:00', '2025-01-01 14:15:00', '1');
INSERT INTO `time_slot` VALUES ('19', '2025-01-01 14:25:00', '2025-01-01 15:10:00', '1');
INSERT INTO `time_slot` VALUES ('2', '2025-01-01 09:50:00', '2025-01-01 10:35:00', '0');
INSERT INTO `time_slot` VALUES ('20', '2025-01-01 15:20:00', '2025-01-01 16:05:00', '1');
INSERT INTO `time_slot` VALUES ('21', '2025-01-01 16:15:00', '2025-01-01 17:00:00', '1');
INSERT INTO `time_slot` VALUES ('22', '2025-01-01 17:10:00', '2025-01-01 17:55:00', '1');
INSERT INTO `time_slot` VALUES ('23', '2025-01-01 18:05:00', '2025-01-01 18:50:00', '1');
INSERT INTO `time_slot` VALUES ('24', '2025-01-01 08:00:00', '2025-01-01 08:45:00', '2');
INSERT INTO `time_slot` VALUES ('25', '2025-01-01 08:55:00', '2025-01-01 09:40:00', '2');
INSERT INTO `time_slot` VALUES ('26', '2025-01-01 09:50:00', '2025-01-01 10:35:00', '2');
INSERT INTO `time_slot` VALUES ('27', '2025-01-01 10:45:00', '2025-01-01 11:30:00', '2');
INSERT INTO `time_slot` VALUES ('28', '2025-01-01 11:40:00', '2025-01-01 12:25:00', '2');
INSERT INTO `time_slot` VALUES ('29', '2025-01-01 12:35:00', '2025-01-01 13:20:00', '2');
INSERT INTO `time_slot` VALUES ('3', '2025-01-01 10:45:00', '2025-01-01 11:30:00', '0');
INSERT INTO `time_slot` VALUES ('30', '2025-01-01 13:30:00', '2025-01-01 14:15:00', '2');
INSERT INTO `time_slot` VALUES ('31', '2025-01-01 14:25:00', '2025-01-01 15:10:00', '2');
INSERT INTO `time_slot` VALUES ('32', '2025-01-01 15:20:00', '2025-01-01 16:05:00', '2');
INSERT INTO `time_slot` VALUES ('33', '2025-01-01 16:15:00', '2025-01-01 17:00:00', '2');
INSERT INTO `time_slot` VALUES ('34', '2025-01-01 17:10:00', '2025-01-01 17:55:00', '2');
INSERT INTO `time_slot` VALUES ('35', '2025-01-01 18:05:00', '2025-01-01 18:50:00', '2');
INSERT INTO `time_slot` VALUES ('36', '2025-01-01 08:00:00', '2025-01-01 08:45:00', '3');
INSERT INTO `time_slot` VALUES ('37', '2025-01-01 08:55:00', '2025-01-01 09:40:00', '3');
INSERT INTO `time_slot` VALUES ('38', '2025-01-01 09:50:00', '2025-01-01 10:35:00', '3');
INSERT INTO `time_slot` VALUES ('39', '2025-01-01 10:45:00', '2025-01-01 11:30:00', '3');
INSERT INTO `time_slot` VALUES ('4', '2025-01-01 11:40:00', '2025-01-01 12:25:00', '0');
INSERT INTO `time_slot` VALUES ('40', '2025-01-01 11:40:00', '2025-01-01 12:25:00', '3');
INSERT INTO `time_slot` VALUES ('41', '2025-01-01 12:35:00', '2025-01-01 13:20:00', '3');
INSERT INTO `time_slot` VALUES ('42', '2025-01-01 13:30:00', '2025-01-01 14:15:00', '3');
INSERT INTO `time_slot` VALUES ('43', '2025-01-01 14:25:00', '2025-01-01 15:10:00', '3');
INSERT INTO `time_slot` VALUES ('44', '2025-01-01 15:20:00', '2025-01-01 16:05:00', '3');
INSERT INTO `time_slot` VALUES ('45', '2025-01-01 16:15:00', '2025-01-01 17:00:00', '3');
INSERT INTO `time_slot` VALUES ('46', '2025-01-01 17:10:00', '2025-01-01 17:55:00', '3');
INSERT INTO `time_slot` VALUES ('47', '2025-01-01 18:05:00', '2025-01-01 18:50:00', '3');
INSERT INTO `time_slot` VALUES ('48', '2025-01-01 08:00:00', '2025-01-01 08:45:00', '4');
INSERT INTO `time_slot` VALUES ('49', '2025-01-01 08:55:00', '2025-01-01 09:40:00', '4');
INSERT INTO `time_slot` VALUES ('5', '2025-01-01 12:35:00', '2025-01-01 13:20:00', '0');
INSERT INTO `time_slot` VALUES ('50', '2025-01-01 09:50:00', '2025-01-01 10:35:00', '4');
INSERT INTO `time_slot` VALUES ('51', '2025-01-01 10:45:00', '2025-01-01 11:30:00', '4');
INSERT INTO `time_slot` VALUES ('52', '2025-01-01 11:40:00', '2025-01-01 12:25:00', '4');
INSERT INTO `time_slot` VALUES ('53', '2025-01-01 12:35:00', '2025-01-01 13:20:00', '4');
INSERT INTO `time_slot` VALUES ('54', '2025-01-01 13:30:00', '2025-01-01 14:15:00', '4');
INSERT INTO `time_slot` VALUES ('55', '2025-01-01 14:25:00', '2025-01-01 15:10:00', '4');
INSERT INTO `time_slot` VALUES ('56', '2025-01-01 15:20:00', '2025-01-01 16:05:00', '4');
INSERT INTO `time_slot` VALUES ('57', '2025-01-01 16:15:00', '2025-01-01 17:00:00', '4');
INSERT INTO `time_slot` VALUES ('58', '2025-01-01 17:10:00', '2025-01-01 17:55:00', '4');
INSERT INTO `time_slot` VALUES ('59', '2025-01-01 18:05:00', '2025-01-01 18:50:00', '4');
INSERT INTO `time_slot` VALUES ('6', '2025-01-01 13:30:00', '2025-01-01 14:15:00', '0');
INSERT INTO `time_slot` VALUES ('60', '2025-01-01 08:00:00', '2025-01-01 08:45:00', '5');
INSERT INTO `time_slot` VALUES ('61', '2025-01-01 08:55:00', '2025-01-01 09:40:00', '5');
INSERT INTO `time_slot` VALUES ('62', '2025-01-01 09:50:00', '2025-01-01 10:35:00', '5');
INSERT INTO `time_slot` VALUES ('63', '2025-01-01 10:45:00', '2025-01-01 11:30:00', '5');
INSERT INTO `time_slot` VALUES ('64', '2025-01-01 11:40:00', '2025-01-01 12:25:00', '5');
INSERT INTO `time_slot` VALUES ('65', '2025-01-01 12:35:00', '2025-01-01 13:20:00', '5');
INSERT INTO `time_slot` VALUES ('66', '2025-01-01 13:30:00', '2025-01-01 14:15:00', '5');
INSERT INTO `time_slot` VALUES ('67', '2025-01-01 14:25:00', '2025-01-01 15:10:00', '5');
INSERT INTO `time_slot` VALUES ('68', '2025-01-01 15:20:00', '2025-01-01 16:05:00', '5');
INSERT INTO `time_slot` VALUES ('69', '2025-01-01 16:15:00', '2025-01-01 17:00:00', '5');
INSERT INTO `time_slot` VALUES ('7', '2025-01-01 14:25:00', '2025-01-01 15:10:00', '0');
INSERT INTO `time_slot` VALUES ('70', '2025-01-01 17:10:00', '2025-01-01 17:55:00', '5');
INSERT INTO `time_slot` VALUES ('71', '2025-01-01 18:05:00', '2025-01-01 18:50:00', '5');
INSERT INTO `time_slot` VALUES ('72', '2025-01-01 08:00:00', '2025-01-01 08:45:00', '6');
INSERT INTO `time_slot` VALUES ('73', '2025-01-01 08:55:00', '2025-01-01 09:40:00', '6');
INSERT INTO `time_slot` VALUES ('74', '2025-01-01 09:50:00', '2025-01-01 10:35:00', '6');
INSERT INTO `time_slot` VALUES ('75', '2025-01-01 10:45:00', '2025-01-01 11:30:00', '6');
INSERT INTO `time_slot` VALUES ('76', '2025-01-01 11:40:00', '2025-01-01 12:25:00', '6');
INSERT INTO `time_slot` VALUES ('77', '2025-01-01 12:35:00', '2025-01-01 13:20:00', '6');
INSERT INTO `time_slot` VALUES ('78', '2025-01-01 13:30:00', '2025-01-01 14:15:00', '6');
INSERT INTO `time_slot` VALUES ('79', '2025-01-01 14:25:00', '2025-01-01 15:10:00', '6');
INSERT INTO `time_slot` VALUES ('8', '2025-01-01 15:20:00', '2025-01-01 16:05:00', '0');
INSERT INTO `time_slot` VALUES ('80', '2025-01-01 15:20:00', '2025-01-01 16:05:00', '6');
INSERT INTO `time_slot` VALUES ('81', '2025-01-01 16:15:00', '2025-01-01 17:00:00', '6');
INSERT INTO `time_slot` VALUES ('82', '2025-01-01 17:10:00', '2025-01-01 17:55:00', '6');
INSERT INTO `time_slot` VALUES ('83', '2025-01-01 18:05:00', '2025-01-01 18:50:00', '6');
INSERT INTO `time_slot` VALUES ('9', '2025-01-01 16:15:00', '2025-01-01 17:00:00', '0');
INSERT INTO `time_slot` VALUES ('TS00001', '2025-09-13 00:00:00', '2025-09-13 02:00:00', '星期六');
INSERT INTO `time_slot` VALUES ('TS00010', '2025-09-12 09:04:50', '2025-09-12 11:04:50', '4');

-- ----------------------------
-- Table structure for update_class_apply_ttoa
-- ----------------------------
DROP TABLE IF EXISTS `update_class_apply_ttoa`;
CREATE TABLE `update_class_apply_ttoa`  (
  `apply_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请ID，主键',
  `teacher_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '教师ID，外键',
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '课程ID，外键',
  `apply_time` datetime NOT NULL COMMENT '申请时间，默认当前时间',
  `information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请原因',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '审核意见',
  `state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '状态（Y=同意/N=拒绝/P=处理中）',
  PRIMARY KEY (`apply_id`) USING BTREE,
  INDEX `idx_ttoa_course`(`course_id` ASC) USING BTREE,
  INDEX `idx_ttoa_state`(`state` ASC) USING BTREE,
  INDEX `idx_ttoa_teacher`(`teacher_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '教师调课申请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of update_class_apply_ttoa
-- ----------------------------

-- ----------------------------
-- Table structure for update_information_apply
-- ----------------------------
DROP TABLE IF EXISTS `update_information_apply`;
CREATE TABLE `update_information_apply`  (
  `apply_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请ID，主键',
  `applicant_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请人ID',
  `applicant_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '申请人类型（学生/教师）',
  `apply_time` datetime NOT NULL COMMENT '申请时间，默认当前时间',
  `information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '修改原因',
  `new_profile` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL COMMENT '新个人简介',
  `new_photo` longblob NULL COMMENT '新照片',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '审核意见',
  `final_decision` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '最终决定（Y=同意/N=拒绝/P=处理中）',
  PRIMARY KEY (`apply_id`) USING BTREE,
  INDEX `idx_uia_applicant`(`applicant_id` ASC, `applicant_type` ASC) USING BTREE,
  INDEX `idx_uia_decision`(`final_decision` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '个人信息修改申请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of update_information_apply
-- ----------------------------

-- ----------------------------
-- Table structure for user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log`  (
  `log_id` decimal(65, 30) NOT NULL COMMENT '日志ID，自增主键',
  `user_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '用户ID，外键',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '登录IP地址',
  `login_status` decimal(1, 0) NOT NULL COMMENT '登录状态(0=失败,1=成功)',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_login_status`(`login_status` ASC) USING BTREE,
  INDEX `idx_login_time`(`login_time` ASC) USING BTREE,
  INDEX `idx_login_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin COMMENT = '用户登录日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_login_log
-- ----------------------------
INSERT INTO `user_login_log` VALUES (1.000000000000000000000000000000, 'S2023001', '2025-02-20 08:05:23', '192.168.1.101', 1);
INSERT INTO `user_login_log` VALUES (2.000000000000000000000000000000, 'T2023001', '2025-02-20 08:15:42', '192.168.1.102', 1);
INSERT INTO `user_login_log` VALUES (3.000000000000000000000000000000, 'ADM001 ', '2025-02-20 08:20:15', '192.168.1.100', 1);
INSERT INTO `user_login_log` VALUES (4.000000000000000000000000000000, 'S2023002', '2025-02-20 08:25:37', '192.168.1.103', 1);
INSERT INTO `user_login_log` VALUES (5.000000000000000000000000000000, 'S2023003', '2025-02-20 08:30:18', '192.168.1.104', 0);
INSERT INTO `user_login_log` VALUES (6.000000000000000000000000000000, 'S2023001', '2025-02-20 12:05:11', '192.168.1.105', 1);
INSERT INTO `user_login_log` VALUES (7.000000000000000000000000000000, 'T2023002', '2025-02-20 13:20:05', '192.168.1.106', 1);
INSERT INTO `user_login_log` VALUES (8.000000000000000000000000000000, 'S2023004', '2025-02-20 14:15:33', '192.168.1.107', 1);
INSERT INTO `user_login_log` VALUES (9.000000000000000000000000000000, 'ADM002 ', '2025-02-20 15:40:22', '192.168.1.108', 1);
INSERT INTO `user_login_log` VALUES (10.000000000000000000000000000000, 'T2023003', '2025-02-20 16:25:14', '192.168.1.109', 0);
INSERT INTO `user_login_log` VALUES (11.000000000000000000000000000000, 'S2023005', '2025-02-20 18:30:45', '192.168.1.110', 1);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `usertype` int NOT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `verification` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `salt` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('ADM1000', '丰川若麦', NULL, 'lqls+Qs435jS7o81SzoHSg==', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'aoAixO/krq1TA+pmVGAdFQ==');
INSERT INTO `users` VALUES ('ADM1001', '椎名若麦', NULL, '$2a$10$snexsXULeEVntR/Uz/cO7ujEVTBOV..CpQrndaQOb8/b7ZP/2QbIq', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'RPvF5TlkS4kzeJGWitRvXsk5PXoSxinsbucnRZKVi08=');
INSERT INTO `users` VALUES ('ADM1002', '天佑寺初华', NULL, '$2a$10$pkwT6xaKMsDv/4UXUYPVqumj980urX5Mw3Jt4SVtVnQc2j5wW6lxu', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'yy32M4eWy4PeUfLCyvLx6zQ/fsvhqOC4Aen8pXtP9So=');
INSERT INTO `users` VALUES ('ADM1003', '若叶若麦', NULL, '$2a$10$qAuIStzaK.rvxxSLrwUX3eylEbFWetrzUzWz.usY9hTjiAoTp0PT.', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'kMkaXfZ4cO29vg3sEn1EiQGL2THiYqIIx6fkM73KdEA=');
INSERT INTO `users` VALUES ('ADM1004', '要立希', NULL, '$2a$10$MVQnYvRGaqGVHmOX1tH0lOgdvOn45lQu4aTEjKSMeEaqYi.twe9Z.', 0, 'mygo@tongji.edu.cn', NULL, NULL, '/z0uqDWbpRj3N/hP2KI/gQass7ngaT8alLQDcCZ3G48=');
INSERT INTO `users` VALUES ('ADM1005', '长崎立希', NULL, '$2a$10$hjQkkFhkJtuD9SAh57YLR.AL3aU0V4rH4.p66MrKWZDJQfWMAim72', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'CJzXAn1o58CJEu4a2+Y/HBYXcfmetlbaPlMzrz9FNo8=');
INSERT INTO `users` VALUES ('ADM1006', '要初华', NULL, '$2a$10$Xcu.Sk69RqnMZk3Jm702We.pYf8HJWQUJRqDv8nzs0dqFfTEYfX.q', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'Y7Uv7Sm+Rg1q7w2OEpf0qpVZ1+GFQ4iQ+V+fLonIJ6c=');
INSERT INTO `users` VALUES ('ADM1007', '天佑寺睦', NULL, '$2a$10$Vq1GO.6KKqjoz9JvAfxuEuCI7qzB0ZyH3FZX9tOtv6nAzz3WxuE2K', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'WChcloQHbap/nnabj6pxmvZadC/79g3SLclSrkA7Yvk=');
INSERT INTO `users` VALUES ('ADM1008', '八幡爱音', NULL, '$2a$10$6L74znlyE/YLICJTrRw4OuYOmbgacdfiR/tqXgvIrYAViEh4tJfyq', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'lPVQAPxQR5J8sYkuFRUMzb4mGHlD+JqiWA7SsJ0D644=');
INSERT INTO `users` VALUES ('ADM1009', '要灯', NULL, '$2a$10$rWp3/x4MKnCRoeHgJnS/7eTyhj9mDa97.GariDD9i1tmn1WmQiwx2', 0, 'mygo@tongji.edu.cn', NULL, NULL, 'puO+pLwL9pnPPAwADf1/1K/TF2giQv4YbZc8Hqf/7cE=');
INSERT INTO `users` VALUES ('ADM2000', '千早立希', NULL, '$2a$10$N3Ox5eQ2pnNmyXhzUYPmsus00HAeKpRmdiuLxXMsA5Td6YFMveCai', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'iwIgZ+AdNFzlYqxUXfOx2pWzd1DeRJiEJ9SR8f/EnXc=');
INSERT INTO `users` VALUES ('ADM2001', '丰川初华', NULL, '$2a$10$mV8vFvRMGNQn33qsYQxYcuWu.jmI0H8mY6A7DAP.eI4PxhMwFakNq', 1, 'mygo@tongji.edu.cn', NULL, NULL, '9HO7fpHVohwo8lXS0xwyznRS4gmQfxjzuXwbkeHOqf8=');
INSERT INTO `users` VALUES ('ADM2002', '天佑寺海玲', NULL, '$2a$10$EUA33NEHVHIlI7NtVy4A5OS27MbhXea8s00pypOaM/Ql7sowucueS', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'OSOOOy87yK1TzQN+6b5V8lOiVHBOzO2ZKA1nEOfoCBA=');
INSERT INTO `users` VALUES ('ADM2003', '天佑寺初华', NULL, '$2a$10$h8DEFQ8zE1Kq0t0Qi60YJ.jTlo.3kOlhounb/FMC7j5LFaOWBlNg6', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'bsJ/c1z4xXYbE6Mxpj+sm+ta4QRwYeGK2ojRNccSbl0=');
INSERT INTO `users` VALUES ('ADM2004', '要灯', NULL, '$2a$10$DOJWdkICE7FOrUEO6Dk7Ae6AIZo63VC3u03StdCWX2d3Wm29Yqqqa', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'lpzRly21RYMhkFnKH4FOJpxNtsqjNE6us2CxqcBOcKc=');
INSERT INTO `users` VALUES ('ADM2005', '长崎素世', NULL, '$2a$10$JR32e22lv.D2YbsV3tJB9OOVQL81CANrWkivBWOU7MhvTcrC8VFXq', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'EDQ8xw3iJUJRn36Y0CcvOr5KwQwuopDgPVFFYtwn4Hs=');
INSERT INTO `users` VALUES ('ADM2006', '千早素世', NULL, '$2a$10$1eCrz999TyCX0nNo0VReQ.MFS0X2HBhXu3LmlJHUsyyYF6tkTjlL.', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'cztbqoZL8BYc5B6A75W1ev7ZERtXJQ5bgv4MD74PQB0=');
INSERT INTO `users` VALUES ('ADM2007', '椎名爱音', NULL, '$2a$10$85qudE1NRs6gEH1VJE2uTugAYfRtHckpaW4yehcHkAsATJz.Jz2/W', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'TZJw9g/WjHgG+IQtoMVwsgH5MEuCGWWSZZTO5LvPpfA=');
INSERT INTO `users` VALUES ('ADM2008', '椎名若麦', NULL, '$2a$10$tScWaW6Z5nIp9gn9p8rzh.HQA6L1xhnnU6VhFmR.R/KXE76eO9WFq', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'H0FTm8ykerIDCOpuVw/kN68NcZfdl3HL95xv9fkpEEw=');
INSERT INTO `users` VALUES ('ADM2009', '天佑寺乐奈', NULL, '$2a$10$y8.hzTKgSvMtC0Mk/UFzVedQEbqc663FO4I/Ux7DvzUkFt5q3D/kW', 1, 'mygo@tongji.edu.cn', NULL, NULL, 'VO6k0ytlrYYxCLN6fQMFAfvJnZF/7Aj6Kl9uCqbF6vo=');
INSERT INTO `users` VALUES ('S2023000', '八幡素世', NULL, '$2a$10$swrlQ7tNsanXcGUZP//ILecb20eAVI3DSGpRcp40/creDtdstfz16', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'w6AeEjS/0nIXbAM2bbgsFzTe6/OBXx+5iKrwbiNOR3o=');
INSERT INTO `users` VALUES ('S2023001', '八幡乐奈', NULL, 'lqls+Qs435jS7o81SzoHSg==', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'aoAixO/krq1TA+pmVGAdFQ==');
INSERT INTO `users` VALUES ('S2023002', '要立希', NULL, '$2a$10$DV7/QikQwieg8vVzdyG6nuflGc69F.YssjHafyceKN/pbsERQ/0ei', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'mvl3Hm2HGXb7D7Z1cIDmB8sVroWDoFNOvAa7rh4ODQ4=');
INSERT INTO `users` VALUES ('S2023003', '八幡乐奈', NULL, '$2a$10$9zaY0BZne/rWK.aTMaVmhuUTH6OJ4k/rqk3tmrkWtp4.UKw0cCQN2', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'LnWXt02Kp6gNmWcpRK9TVHg8p8eyEp2CmhClcjzl3Ss=');
INSERT INTO `users` VALUES ('S2023004', '若叶灯', NULL, '$2a$10$5Hpl2gmkdIaZcEo02o.B7.0.hqf.zqlzdsq.cvOzGV9uFRvNyNej6', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'uJb9nRRzjJyCVTuNjNpax45E6n7SxiJoeAr4bzlqSFk=');
INSERT INTO `users` VALUES ('S2023005', '天佑寺立希', NULL, '$2a$10$8Rh71B1f3t7KjPoSNeptVuiLoLEjK08hj8YW2fusGlyll9QgYXFSK', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'yoCli7gBKHCChk3U8MB1Im1MzaCIfy0vQBOyhIsxhWo=');
INSERT INTO `users` VALUES ('S2023006', '长崎立希', NULL, '$2a$10$imzznBJysp4qAiowFsdR3e10rZ4XncBz1OtjcEkLsJT5m1hRDkHNW', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'lFkeonOieypZ3YMuXRCxGCMnJvOOJ8uClfG6+KVSLqs=');
INSERT INTO `users` VALUES ('S2023007', '八幡素世', NULL, '$2a$10$1BbiqQTQjNuVRgkMt6ttLur5XYh9/EGvP85ohMXP0QGkOGOxLMXKu', 3, 'mygo@tongji.edu.cn', NULL, NULL, '/+L0vPlhEklHaV0Sju+AkzlVIutitspKCwVTUco5Gco=');
INSERT INTO `users` VALUES ('S2023008', '若叶乐奈', NULL, '$2a$10$thFl8atGed/EHuI1WeRY/OuhUtOTtqNE3U4PFM4iK01KZAJ0d1TXq', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'zPVlYqGSe7j/nlgP50MPYBJ+J8fJk2OthgEqz7qQiuw=');
INSERT INTO `users` VALUES ('S2023009', '天佑寺祥子', NULL, '$2a$10$LiFZegH.xjYDeAgK9diwMerK1EQOUz0Wd6tSHYSvVR9aYzcDPPv7i', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'ku8eLH1x0xpwREjp1l+igYljwKYWL8rmCdkaQeSJP1w=');
INSERT INTO `users` VALUES ('S2023010', '高松睦', NULL, '$2a$10$DUMYloZ3wVcaGF75TdVtreiBEywtxyonANTcbQNI97uWxf6i03w8m', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'eYJBebUaRz7oinoK1OHHU0G6cjpOfI7d9pTFro4JViE=');
INSERT INTO `users` VALUES ('S2023011', '要爱音', NULL, '$2a$10$e.z5LMZ3BJ0Y8er3qFFJ.u1ImHkAkRrGa/JsdFguABRGL9ytbFvxe', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'jsXnbr5XaXKoECEA+Sq0Fj8LzsniDZv7nULRr0Ls750=');
INSERT INTO `users` VALUES ('S2023012', '天佑寺若麦', NULL, '$2a$10$QcrYiWVNw9FfR.bfi0WNk.RbS9tWD6i8VdBsQnqhAGI5XN15VncVm', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'zmf37DXWOJScwHVEpgRbOEjvWTHdiuyCIWD+5gwD0Wg=');
INSERT INTO `users` VALUES ('S2023013', '要海玲', NULL, '$2a$10$8297upF9sXxF3Px3e8AY1.mVTvnZ5UIfDLfAtEQ.WyamZ9VXXZmra', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'WgqODLZDc+Fn1wju9LlxrkXXINSVx+kWiHcfyTvQ66I=');
INSERT INTO `users` VALUES ('S2023014', '椎名灯', NULL, '$2a$10$AQ1IqRR3kotWf44cw4P5d.qGupNDL2YyFafDxv.zqWZje2IHuqXPu', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'dtmL8pT2STs9x6SMUt6MjjdpIlflaOyYEHJs6kcKLkw=');
INSERT INTO `users` VALUES ('S2023015', '椎名若麦', NULL, '$2a$10$iFn4xSAEvAi/74I.lz0N6uVehXfdJXM8ODzKkwZfdUqFQHzdsspZe', 3, 'mygo@tongji.edu.cn', NULL, NULL, '0SB/vrQzyEDDMOD75o0Ftz8sBI1ii39711wLYsqx5Zo=');
INSERT INTO `users` VALUES ('S2023016', '要立希', NULL, '$2a$10$wMEKtNfDXU4A6YVYn.z4vueR/vK4C4.ZCFc6oBL6QGavA.ywHmZhC', 3, 'mygo@tongji.edu.cn', NULL, NULL, '7AV7A9IIQjsQwm3m3RLz2Je6I86ylSk1UrMcynT6bRg=');
INSERT INTO `users` VALUES ('S2023017', '丰川爱音', NULL, '$2a$10$qHgD9RaffO5hIwXK11J1oOJO.7ZcruJZ7nI6pQTgIlhwNOtm..H/e', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'cB9FmYh/nUZ4dgP6e5Yv7vs37xU0yc8lrNpeNUWtEiU=');
INSERT INTO `users` VALUES ('S2023018', '八幡乐奈', NULL, '$2a$10$bR/nsQpWK4sciyOpWIbJB./HRjyMNLLClgGNEqmE8.PyWG89Myd4W', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'E8VTq51QVL8GsB76B+uYq5cF6It8ZJfSIlrEani/ieM=');
INSERT INTO `users` VALUES ('S2023019', '千早素世', NULL, '$2a$10$W9KPJceHGU5Srp0VBbzYH.ISEfXmrLzaSJvzKdC5NNwGn66SPv4pS', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'KXP3LCUBjElQjS/3w3rcqUJ2RYkiZkgSYFqCjkqjApc=');
INSERT INTO `users` VALUES ('S2023020', '丰川素世', NULL, '$2a$10$W.Y.hPij2Q0XMZF2jOA24uTmOn5WGqDcs.hxVvVhFbWhZ2sMnniE.', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'Y7db+BGN0dj0k9vwpju5LPatyFL3DdAcubk7QwaeMNc=');
INSERT INTO `users` VALUES ('S2023021', '千早初华', NULL, '$2a$10$TL/e8vrg6swSNlKZ7CrRK.g3ediU9ZW4ZYsb5pmGfSyLyZea7kSDe', 3, 'mygo@tongji.edu.cn', NULL, NULL, '0fXGj2PmKhH2D93e9R7GlY81vxQDXznAz1KFQmqVpbA=');
INSERT INTO `users` VALUES ('S2023022', '丰川爱音', NULL, '$2a$10$DTG6mg5oqtTk5jo/ROdUQ.VLjJXBAxA7uq9p.StyUhvp5YgkP03re', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'vuS8gx9TS52eHSN2z/jwlZSIOZyktTYubAM4u7sIon4=');
INSERT INTO `users` VALUES ('S2023023', '高松立希', NULL, '$2a$10$kgb//hggLtEz.3RoBQAKru5Rj2TgWAie6/6WgjyUeiPozpcMvQWcG', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'poqv47Zw1CwNYKY7uPxJT1FUq3ranHP2ml28If+Anj4=');
INSERT INTO `users` VALUES ('S2023024', '千早乐奈', NULL, '$2a$10$VqS2JzUtg2iliMQqrgAOO.rBiqfO4xQyv4bTkvVWs7HpIpMW8hoT6', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'wdDrI/SDbcDJFzu/9s+ozPfF72Q6Y4EwaLJ/CY2/3cY=');
INSERT INTO `users` VALUES ('S2023025', '长崎爱音', NULL, '$2a$10$1WbfUlxJpwznutQWT0POEOhgP7p0xcz/vEt7YZjyNrurPreesYva.', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'XWHJjbmO3rwjaKB80pdwL1hXs6mQpcK6rkpIitt8vP8=');
INSERT INTO `users` VALUES ('S2023026', '要祥子', NULL, '$2a$10$pDUocLKP8rK6Yc6ZrHL57e1XxUNi8VWnOjKbzVKH7Tp.Jsv8HMo5.', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'm1o56GMuu/URwqaB5xuP4v1YnzE1Bl4q3DTIVC09qFg=');
INSERT INTO `users` VALUES ('S2023027', '千早立希', NULL, '$2a$10$NLdtMBMpyUNv1maMvHi89OtEO6bhN23JoYK0Foy9aY.TJp5gfBdg.', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'x9kzoD0mdNKGUaSnY7sAk5nWA7+/8F0EhQnvmeRjTm8=');
INSERT INTO `users` VALUES ('S2023028', '天佑寺若麦', NULL, '$2a$10$lhRPvf/nKGcOxR5PJCG0/O8vy1IZZ6DUzbJtFpvGQ6rbCsQ59gSMK', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'ycqh7VfbWb053vsUvY9nrpepWVO6Qou3p2fX2N0gttk=');
INSERT INTO `users` VALUES ('S2023029', '八幡素世', NULL, '$2a$10$Lt.wk/6qY.qejlJ9RA22EuRSddx/9bw.LNP6HD8bv0yEl3sHlJjAy', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'PuRqKbg2QWufpeG3kicytNeZNBMswmoI/k2AhyA7Kt4=');
INSERT INTO `users` VALUES ('S2023030', '椎名爱音', NULL, '$2a$10$FuivAF45oPjOkpQ7AayRceNWsH72BHO5DbR99cvpDXMCBRkmnrm5O', 3, 'mygo@tongji.edu.cn', NULL, NULL, '5JlMuap4mneztpy6nx6F7dgBpgGHlvTxFkCE/KaMHDk=');
INSERT INTO `users` VALUES ('S2023031', '千早初华', NULL, '$2a$10$5x401Pc4sgO1loLDHZKQcuZ5BsVfMB5Pb01PpM6LiQhx9i1p2vcXy', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'TXoWtgjDeYRgp9mKr3kvs+bTf5hQgtzzEWRqPl7EQUU=');
INSERT INTO `users` VALUES ('S2023032', '高松祥子', NULL, '$2a$10$sR7afcE3ywaoKX0l66PqUOtIfgYcB/.yT0yBgDX1R9pnh5bpD2OCu', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'V4AKx6WFMDanl4aW79ZpN0mQyuVsV/JoBGxuYqRut3E=');
INSERT INTO `users` VALUES ('S2023033', '要睦', NULL, '$2a$10$UsTR6Z6L.2ulKHkEqoptG.Kf7qrkYLGRsZiSjcj2qmJd9AmzVZxj6', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'KlhkNt7fL5KrdXvB95+CAveriTO0tXQFquqmdvqz/z0=');
INSERT INTO `users` VALUES ('S2023034', '千早睦', NULL, '$2a$10$kbTD41uG6OapAzD6QqRY6ujrPD3L5rlvriK7RAZt0.PemxzDVJLmS', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'rxCm36fDlzLZaeaijAn8yUzDoMyYKBzFyMTO18/kRHA=');
INSERT INTO `users` VALUES ('S2023035', '要乐奈', NULL, '$2a$10$.QNlhzJOYeCqUhcwChf4T.t.59/0v2FB/zs.g1jAWCt9YQ8Am9DLu', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'sKkeVv76G+EucKOzrGYI66yNPmjMOLZSAgnoMX+cWCM=');
INSERT INTO `users` VALUES ('S2023036', '长崎睦', NULL, '$2a$10$cwFvTGsQCx.5yoJkdYUZD.M3jVItuU9qr7Gscbw7aLGP862/hEzEW', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'D9vnjhBqFWuMmF8re5ZK9vPU/KUOX4vao5e73x/QktQ=');
INSERT INTO `users` VALUES ('S2023037', '高松立希', NULL, '$2a$10$cJPp5Ki0y4kyz8w98d/PkuZVcJjYf9UahUPpjdbOhCSZXo6Zl3mTO', 3, 'mygo@tongji.edu.cn', NULL, NULL, '2swljdoN2EViCwcKqcuQlaJRZUQCgtbL1yFhWR85ykU=');
INSERT INTO `users` VALUES ('S2023038', '高松若麦', NULL, '$2a$10$u.wHzIR7QI7gSiKkthjCHuuTHtd1jx2dJDLueZG242OHhqpX1ycMS', 3, 'mygo@tongji.edu.cn', NULL, NULL, '6jD6zwIBwT5Eg6+g4rXQR7Jj33hCxom6o7BBxSOvSc0=');
INSERT INTO `users` VALUES ('S2023039', '长崎乐奈', NULL, '$2a$10$jUm.Nj6Vb5JbCWrshei98uQ6jLEOAmSRokQ4jMWJPuqVllL1a70A6', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'rSjP8e2iWgK+gmA8Ic+NS50nkNYYZVnjjkXWSTZtA2Y=');
INSERT INTO `users` VALUES ('S2023040', '八幡乐奈', NULL, '$2a$10$JlBNVV9V4U0u7W6brpDxK.yXR7KXzJAxgNzyML2C4SLiqGi72PAty', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'rbkqD6fWpqBGc6eP0Z6SzLT41LiNSQOdoYSIohxRbeg=');
INSERT INTO `users` VALUES ('S2023041', '天佑寺初华', NULL, '$2a$10$h3V.vsWV6S3sAiDT57C7pe9B3KcaEgcNLILezMXxV2OcJ3FYotPMe', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'A96ah+pHlK8RZpkioU1qBMm45rc+wglACjeOjDYpUXU=');
INSERT INTO `users` VALUES ('S2023042', '三角灯', NULL, '$2a$10$.8XJZBLofyjPMhZipaVNVOphHeSqpPqhi36y7obqOPIA6Mlzifyz6', 3, 'mygo@tongji.edu.cn', NULL, NULL, '7dfdB71ZlPAaqljQEVLu61JVBcvruXLN2CzIdo/DQTU=');
INSERT INTO `users` VALUES ('S2023043', '长崎爱音', NULL, '$2a$10$Wun1MWSaZthMgPRaxvC7z.EWK85WNr.5P5LZiS.1r1mrr4KGp9YbO', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'dee38UhFqkwzPi+TW4QDfbYrZyXFTPDBhE0BFBrbrYQ=');
INSERT INTO `users` VALUES ('S2023044', '八幡睦', NULL, '$2a$10$MJugsRPNS3r5j46L3asYSuUo1Ahvof/Z8LnlIpETNRESG264pxWZK', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'clG1TLxFfSRtSsnzeLYmmeDS8fgJOX+tKStaqxuBJJs=');
INSERT INTO `users` VALUES ('S2023045', '高松祥子', NULL, '$2a$10$/Wkk0zwrBbzlE5qgVjfK0.OpesMBCpHJ2eULVbd8Mx6H2PvrAGO/K', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'ZAaZLZta1mrputx3/CkV9ngTIsBB/ns5JAXfL2LBQ5s=');
INSERT INTO `users` VALUES ('S2023046', '椎名初华', NULL, '$2a$10$W2yIiS1oSBUeiut2B910setb7DZlUwiGahWHC6mjQrvY6kaOicVxe', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'wdYWtRI1VAsQ/wfX7DqP/gvMXZDsMw/akP1wpIza4Hg=');
INSERT INTO `users` VALUES ('S2023047', '要祥子', NULL, '$2a$10$Et86dPWYqrKLpoTxdmGV7eQX4JjcJAfGI8qTw1UBjOA9gz49S8IHK', 3, 'mygo@tongji.edu.cn', NULL, NULL, '6FlbMskYz27buw2Kqzf50kxgJDZ/rx4IVptlORXJ8SE=');
INSERT INTO `users` VALUES ('S2023048', '长崎爱音', NULL, '$2a$10$VKRH2eyJGPNmQpt35.epLe2nthHSgvvwRFZWYmuV4V0NSfy/Y06g2', 3, 'mygo@tongji.edu.cn', NULL, NULL, '7KsOKwyNycuK7xnggfR6RuDuN/Nb/i4lpWAX6jA62cs=');
INSERT INTO `users` VALUES ('S2023049', '千早灯', NULL, '$2a$10$F8.Z91Mtqp8Qkch966iPEO/0vQUacwR4S3pRzJhOl7lJseoUO9ywS', 3, 'mygo@tongji.edu.cn', NULL, NULL, 'qsz8C2w2A8/YMeZ6i2LjjKVWSzMSjbk9GbYcoq3jrNc=');
INSERT INTO `users` VALUES ('T2023000', '长崎立希', NULL, '$2a$10$SNQbwmsbM6rbpRLmUtZ/uuHwscT2i00KaCjWON7Zs0pkqOyZWKeme', 2, 'mygo@tongji.edu.cn', NULL, NULL, '0k3s+Gz+tLXqc1Qx4sJzqU4JoszC8QKp6lvxhN2XHz0=');
INSERT INTO `users` VALUES ('T2023001', '千早睦', NULL, '$2a$10$eKLcYWx07jWfPx.GhmNXmOCNKjmmzn3bJxEA8rHWNhjKXQFwCLVvS', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'HFvCxadAUVhjLLGDSC1zB3FE4ASCkuKVy5PnAXSQjgQ=');
INSERT INTO `users` VALUES ('T2023002', '长崎乐奈', NULL, '$2a$10$g22dLFjtg4AXC2xPEhqDS.i9whxg0AjSuu1DfsEGnbJtw/oJIYpry', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'rJm2JeZUFnGJMI6Y92/rH8kGubUpJc+6oOlzAP76KQ4=');
INSERT INTO `users` VALUES ('T2023003', '要立希', NULL, '$2a$10$pwBLWeHfeG78KCIhQLNTJuvo/k6VisO8s6cH.dFf.x5fiV3qCQLUC', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'Fb/ouBsevh6r6B7Idz8BXRAS8p3s8r5gHp1zhyHBmYI=');
INSERT INTO `users` VALUES ('T2023004', '天佑寺海玲', NULL, '$2a$10$T7MEPNsqioip8jmj2bkWsuJQEC0/HMVzs35lFOh74xk0x7cEdSwSW', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'r2cQvLuj4AWFsx4YIKLPD//nftfJ3A4Rf8Zy7F4z8G4=');
INSERT INTO `users` VALUES ('T2023005', '高松初华', NULL, '$2a$10$vgk0Nm1AB6rq4trGoOKQB.roc1dKBq4M8a.U/zvfnPRY768x//lMe', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'EHe8/BHrvYJmbvIldhAjv8uoSXls4M9efuidXSdJEYI=');
INSERT INTO `users` VALUES ('T2023006', '八幡爱音', NULL, '$2a$10$pOAMvlcg2RHP0OURsZbSIejS6kkJMkqbMH/qTIvqyecBpDkDwUjuq', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'eXLOP8UDdkXIvBoHwt+e9+x8p9O/lr3y4dBATVfX+io=');
INSERT INTO `users` VALUES ('T2023007', '千早爱音', NULL, '$2a$10$nYKw4wY1RWthWYcnLOGupOGfK8TjNuwwcF7w478qBoX/jc2KGBZ1i', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'lrKTNz5nyZocXQdk6svXQlQMENfPuGSz/f0Oyt5hocY=');
INSERT INTO `users` VALUES ('T2023008', '要若麦', NULL, '$2a$10$Xw0IiPMKgchLrgvnKLK7zuhxYKZjBA27p8A0MckFgVGoeutS6iZNG', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'c4uBuxGWtyJH7SZ0DWHKNpdGDCBeXTAlJZbCCNL8rYY=');
INSERT INTO `users` VALUES ('T2023009', '椎名海玲', NULL, '$2a$10$0wzXkmfpA5/Flxf9FGuH7OLN/AY7BEYGOZ1PvMZO3AUstrV1.nDT2', 2, 'mygo@tongji.edu.cn', NULL, NULL, 'MRkX5HZe5nsL5lv2Mp0B1MwS0HL8UiIxuP+V/T+i8No=');

-- ----------------------------
-- Table structure for year_semester_starttime
-- ----------------------------
DROP TABLE IF EXISTS `year_semester_starttime`;
CREATE TABLE `year_semester_starttime`  (
  `year` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL,
  `semester` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '学期',
  `start_date` datetime NULL DEFAULT NULL COMMENT '学期第一周周一的日期',
  PRIMARY KEY (`year`, `semester`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of year_semester_starttime
-- ----------------------------
INSERT INTO `year_semester_starttime` VALUES ('2025', '0', '2025-09-01 16:24:54');

SET FOREIGN_KEY_CHECKS = 1;
