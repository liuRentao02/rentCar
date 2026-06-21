/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : localhost:3306
 Source Schema         : campus_rent

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 31/05/2026 16:04:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for car_info
-- ----------------------------
DROP TABLE IF EXISTS `car_info`;
CREATE TABLE `car_info`
(
    `car_id`            bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT '单车主键ID',
    `model_id`          bigint(0)                                                      NOT NULL COMMENT '关联车型ID（外键，关联car_model表）',
    `plate_number`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '车牌号（唯一标识，如：京A88888）',
    `vin_code`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '车架号/VIN（车辆全球唯一身份证，用于保险年检）',
    `engine_no`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL     DEFAULT NULL COMMENT '发动机号（部分业务场景需要）',
    `vehicle_color`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '车身颜色（字典：dict_color，如：珍珠白）',
    `shop_id`           bigint(0)                                                      NULL     DEFAULT NULL COMMENT '所属门店ID（关联门店表，暂留空）',
    `current_mileage`   int(0)                                                         NULL     DEFAULT 0 COMMENT '当前里程数（公里，用于计算超里程费）',
    `current_fuel`      decimal(5, 2)                                                  NULL COMMENT '当前油量/电量百分比（如：80.00，用于还车结算）',
    `license_date`      date                                                           NULL     DEFAULT NULL COMMENT '上牌日期（用于计算车龄）',
    `insurance_expiry`  date                                                           NULL     DEFAULT NULL COMMENT '保险到期日（风控提醒字段，防止脱保）',
    `inspection_expiry` date                                                           NULL     DEFAULT NULL COMMENT '年检到期日（合规性字段，防止脱检）',
    `daily_rent`        decimal(10, 2)                                                 NULL COMMENT '日租金（元，运营定价）',
    `deposit_amount`    decimal(10, 2)                                                 NULL COMMENT '押金（元，租赁时冻结金额）',
    `rental_count`      int(0)                                                         NULL     DEFAULT 0 COMMENT '累计租赁次数（统计热度）',
    `status`            varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL DEFAULT 'available' COMMENT '车辆状态（字典：dict_car_status，如：待租/已租/维修）',
    `image_urls`        varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '车辆图片URL，多张逗号分隔',
    `create_time`       datetime(0)                                                    NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '入库时间',
    `update_time`       datetime(0)                                                    NULL     DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`car_id`) USING BTREE,
    UNIQUE INDEX `uk_plate_number` (`plate_number`) USING BTREE COMMENT '唯一索引：车牌号不可重复',
    UNIQUE INDEX `uk_vin_code` (`vin_code`) USING BTREE COMMENT '唯一索引：车架号不可重复',
    INDEX `idx_model_id` (`model_id`) USING BTREE COMMENT '普通索引：快速查询某车型下的所有车辆',
    INDEX `idx_status` (`status`) USING BTREE COMMENT '普通索引：快速筛选待租车辆'
) ENGINE = InnoDB
  AUTO_INCREMENT = 71
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '单车档案表（具体车辆资产）'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of car_info
-- ----------------------------
INSERT INTO `car_info`
VALUES (1, 1, '京A·D10001', 'LSGPC54U9GF123456', 'A123456', '珍珠白', 1, 1500, 85.50, '2023-05-10', '2026-05-09',
        '2025-05-10', 200.00, 2000.00, 12, 'available',
        'http://localhost:8080/api/uploads/2026/04/03/7f3415bd-e66a-4007-beea-df43c1221ee7.jpg', '2026-04-03 17:25:50',
        '2026-05-30 15:11:57');
INSERT INTO `car_info`
VALUES (2, 2, '京A·D10002', 'LBVNU3102GS987654', 'B654321', '曜石黑', 1, 20000, 100.00, '2022-08-15', '2025-08-14',
        '2024-08-15', 100.00, 1000.00, 8, 'available',
        'http://localhost:8080/api/uploads/2026/04/03/d74ca77d-c165-42bd-b95d-b330a5831373.jpg', '2026-04-03 17:25:50',
        '2026-05-30 15:10:45');
INSERT INTO `car_info`
VALUES (4, 3, '京A·D10004', 'WDDUG8FB5JA321654', 'D321654', '银色', 1, 15000, 72.00, '2022-06-01', '2025-05-31',
        '2024-06-01', 120.00, 1000.00, 5, 'available',
        'http://localhost:8080/api/uploads/2026/04/03/8397cb9e-9ee9-4fbe-9f2c-341dfa828902.webp', '2026-04-03 17:25:50',
        '2026-05-30 15:11:00');
INSERT INTO `car_info`
VALUES (6, 5, '京A·D10006', 'WP0AA2A97PS210987', 'F210987', '迈阿密蓝', 1, 200, 100.00, '2023-04-25', '2026-04-24',
        '2025-04-25', 120.00, 800.00, 3, 'available',
        'http://localhost:8080/api/uploads/2026/04/03/b8f0470d-fda7-4a2b-9fe7-072c7aa1beb3.jpg', '2026-04-03 17:25:50',
        '2026-05-30 15:10:35');
INSERT INTO `car_info`
VALUES (14, 8, '24355553', '1', '', '白色', NULL, 0, 0.00, NULL, NULL, NULL, 20.00, 500.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/c8386a11-b34d-4755-a5fc-556616a444e4.png', '2026-04-22 15:31:06',
        '2026-04-22 15:49:28');
INSERT INTO `car_info`
VALUES (25, 11, '自A·C0001', 'YONGJIUCC00000001', NULL, '蓝', 1, 0, 0.00, NULL, NULL, NULL, 10.00, 200.00, 0,
        'available',
        'http://localhost:8080/api/uploads/2026/04/22/c7f5563f-a435-4558-89b2-ada19b84339a.webp,http://localhost:8080/api/uploads/2026/04/22/8b9a1893-f10f-487d-bb73-86d8c657a3c8.webp',
        '2026-04-22 16:52:23', '2026-05-24 21:15:28');
INSERT INTO `car_info`
VALUES (40, 14, '自A·X0001', 'XDSYX0000000001', NULL, '橙', 1, 0, 0.00, NULL, NULL, NULL, 10.00, 200.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/cb99a3b2-ccd8-4167-96b7-4f11c2f9fe1f.jpg,http://localhost:8080/api/uploads/2026/04/22/e03061a4-0e7d-4921-9fe5-031f2dc18573.webp,http://localhost:8080/api/uploads/2026/04/22/f4c69003-0276-4d0d-a05f-cb91dcb6453e.jpg',
        '2026-04-22 16:52:23', '2026-05-23 22:14:54');
INSERT INTO `car_info`
VALUES (43, 14, '自A·X0004', 'XDSYX0000000004', NULL, '红', 1, 0, 0.00, NULL, NULL, NULL, 10.00, 200.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/dde2f9d9-e570-4224-9483-579959793b9a.webp,http://localhost:8080/api/uploads/2026/04/22/ba4488f6-3009-4a1d-ae51-ea054a2a08a3.webp,http://localhost:8080/api/uploads/2026/04/22/824b3933-02e8-43c9-9c33-63426500dd71.webp',
        '2026-04-22 16:52:23', '2026-05-23 22:15:07');
INSERT INTO `car_info`
VALUES (45, 16, '电A·YD001', 'YADIDE2000000001', 'YD001E', '白', 1, 0, 100.00, '2025-01-01', '2027-01-01', '2027-01-01',
        30.00, 300.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/58671f09-c0a1-4339-b69c-8e71e4b7e400.png', '2026-04-22 16:52:23',
        '2026-04-22 18:09:01');
INSERT INTO `car_info`
VALUES (47, 16, '电A·YD003', 'YADIDE2000000003', 'YD003E', '银', 1, 0, 100.00, '2025-01-01', '2027-01-01', '2027-01-01',
        30.00, 300.00, 0, 'rented',
        'http://localhost:8080/api/uploads/2026/04/22/a5585bc5-2708-403e-b5be-6146040ea78b.webp', '2026-04-22 16:52:23',
        '2026-04-22 18:09:18');
INSERT INTO `car_info`
VALUES (55, 19, '电A·XN002', 'XNIUGOVA00000002', 'XN002E', '灰', 1, 0, 100.00, '2025-04-01', '2027-04-01', '2027-04-01',
        35.00, 350.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/0df7e0f1-35fa-454a-9746-c4c869a2b1da.webp', '2026-04-22 16:52:23',
        '2026-04-22 18:11:05');
INSERT INTO `car_info`
VALUES (60, 21, '电A·LY001', 'LVYUANS300000001', 'LY001E', '黑', 1, 0, 100.00, '2025-06-01', '2027-06-01', '2027-06-01',
        50.00, 400.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/51c92cbe-1e03-4636-93eb-8faf15747226.webp', '2026-04-22 16:52:23',
        '2026-05-24 21:15:29');
INSERT INTO `car_info`
VALUES (62, 22, '电A·XR001', 'XINRIF500000001', 'XR001E', '蓝', 1, 2, 100.00, '2025-07-01', '2027-07-01', '2027-07-01',
        30.00, 300.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/f733b895-c21f-452d-9d02-8a7fb66209f5.jfif', '2026-04-22 16:52:23',
        '2026-05-23 22:13:46');
INSERT INTO `car_info`
VALUES (63, 22, '电A·XR002', 'XINRIF500000002', 'XR002E', '白', 1, 0, 100.00, '2025-07-01', '2027-07-01', '2027-07-01',
        30.00, 300.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/e523aecd-5ddf-4a5e-bae5-035f110980ac.jpg', '2026-04-22 16:52:23',
        '2026-05-23 22:14:04');
INSERT INTO `car_info`
VALUES (65, 23, '电A·LM002', 'LIMASD000000002', 'LM002E', '黑', 1, 15, 100.00, '2025-08-01', '2027-08-01', '2027-08-01',
        50.00, 500.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/9b0bd80e-9aec-4e2d-9011-691d5ececebc.jpg', '2026-04-22 16:52:23',
        '2026-05-23 22:13:31');
INSERT INTO `car_info`
VALUES (66, 24, '电A·SK001', 'SUKECU000000001', 'SK001E', '白', 1, 0, 100.00, '2025-09-01', '2027-09-01', '2027-09-01',
        20.00, 200.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/2a7ec2ee-35d2-4d49-aa63-7ff5a4c25736.jpg', '2026-04-22 16:52:23',
        '2026-05-23 22:15:31');
INSERT INTO `car_info`
VALUES (67, 24, '电A·SK002', 'SUKECU000000002', 'SK002E', '黑', 1, 0, 100.00, '2025-09-01', '2027-09-01', '2027-09-01',
        40.00, 400.00, 0, 'available',
        'http://localhost:8080/api/uploads/2026/04/22/701c2381-a7d7-4b3c-8eec-3256d71a6b6e.png,http://localhost:8080/api/uploads/2026/04/22/617aa677-00e3-4c09-8368-6bf2dbb8c549.webp,http://localhost:8080/api/uploads/2026/04/22/e6426716-e06c-444e-96ee-4b734d5925c4.jpg',
        '2026-04-22 16:52:23', '2026-05-23 22:14:17');

-- ----------------------------
-- Table structure for car_models
-- ----------------------------
DROP TABLE IF EXISTS `car_models`;
CREATE TABLE `car_models`
(
    `model_id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '车型主键ID',
    `brand_name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '品牌名称（如：丰田）',
    `series_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '车系名称（如：凯美瑞）',
    `model_name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '具体型号全称（如：2022款 2.5S 锋尚版）',
    `vehicle_type`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'CAR' COMMENT '车辆大类：CAR（汽车）, MOTORCYCLE（摩托车）, EBIKE（电动自行车）, BIKE（自行车）, SCOOTER（滑板车）',
    `vehicle_level`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '车辆级别（字典：dict_vehicle_level，如：中型车）',
    `category`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '车辆类别（字典：dict_category，如：轿车/SUV）',
    `energy_type`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '能源类型（字典：dict_energy_type，如：汽油/纯电动）',
    `emission_standard` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL,
    `gearbox_type`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL,
    `drive_mode`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL,
    `fuel_grade`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL,
    `displacement`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL,
    `seat_count`        int(0)                                                        NOT NULL COMMENT '座位数（如：5座，用于客户筛选）',
    `sunroof_type`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '天窗类型（字典：dict_sunroof，如：全景天窗）',
    `interior_color`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '内饰颜色（出厂标配，如：黑色）',
    `guide_price`       decimal(10, 2)                                                NULL     DEFAULT NULL COMMENT '厂商指导价（万元，用于折旧计算）',
    `status`            tinyint(1)                                                    NULL     DEFAULT 1 COMMENT '车型状态（1-正常 0-停用，停用后不可新增该车型车辆）',
    `create_time`       datetime(0)                                                   NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`       datetime(0)                                                   NULL     DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`model_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 25
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '车型配置表（车辆档案模板）'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of car_models
-- ----------------------------
INSERT INTO `car_models`
VALUES (1, '特斯拉', 'Model 3', '2023款 后轮驱动版', 'CAR', '中型车', '轿车', '纯电动', '国四', '自动挡', '后驱', '无',
        '500', 5, '全景天窗', '深色', 23.19, 1, '2026-04-03 16:48:22', '2026-04-03 16:48:22');
INSERT INTO `car_models`
VALUES (2, '宝马', '5系', '2022款 改款二 530Li 领先型 豪华套装', 'CAR', '中大型车', '轿车', '汽油', '国六', '自动挡',
        '后驱', '95号', '2.0', 5, '单天窗', '棕色', 48.89, 1, '2026-04-03 16:48:22', '2026-04-03 16:48:22');
INSERT INTO `car_models`
VALUES (3, '丰田', 'RAV4荣放', '2023款 2.0L CVT两驱都市版', 'CAR', '紧凑型SUV', 'SUV', '汽油', '国六', '自动挡', '前驱',
        '92号', '2.0', 5, '无天窗', '黑色', 17.58, 1, '2026-04-03 16:48:22', '2026-04-03 16:48:22');
INSERT INTO `car_models`
VALUES (4, '别克', 'GL8', '2023款 ES陆尊 尊享型', 'CAR', '大型车', 'MPV', '汽油', '国六', '自动挡', '前驱', '95号',
        '2.0', 7, '单天窗', '米色', 34.39, 1, '2026-04-03 16:48:22', '2026-04-03 16:48:22');
INSERT INTO `car_models`
VALUES (5, '本田', 'CR-V', '2023款 240TURBO CVT两驱锋尚5座版', 'CAR', '紧凑型SUV', 'SUV', '汽油', '国六', '自动挡',
        '前驱', '92号', '1.5', 5, '全景天窗', '黑色', 20.19, 1, '2026-04-03 16:48:22', '2026-04-03 16:48:22');
INSERT INTO `car_models`
VALUES (6, '比亚迪', '宋PLUS DM-i', '2024款 荣耀版 DM-i 110km 旗舰型', 'CAR', '紧凑型SUV', 'SUV', '插电混动', '国六',
        '自动挡', '前驱', '92号', '1.5', 5, '全景天窗', '浅色', 13.98, 1, '2026-04-03 16:48:22', '2026-04-03 16:48:22');
INSERT INTO `car_models`
VALUES (8, '凤凰牌', '无', '凤凰牌自行车', 'BIKE', '公路车', '自行车', '人力', '', '手动挡', '后驱', '', '', 1, '无',
        '黑', 0.05, 1, '2026-04-15 14:52:49', '2026-04-26 22:14:58');
INSERT INTO `car_models`
VALUES (11, '永久', '城市车', '永久 24寸 城市通勤车', 'BIKE', '城市车', '自行车', '人力', '', '手动挡', '后驱', '', '',
        1, '无', '蓝', 0.06, 1, '2026-04-22 16:52:23', '2026-04-26 22:16:04');
INSERT INTO `car_models`
VALUES (14, '喜德盛', '英雄', '喜德盛 英雄300 山地车', 'BIKE', '山地车', '自行车', '人力', '', '手动挡', '后驱', '', '',
        1, '无', '橙', 0.11, 1, '2026-04-22 16:52:23', '2026-04-26 22:15:58');
INSERT INTO `car_models`
VALUES (16, '雅迪', 'DE2', '雅迪 DE2 新国标电动自行车', 'EBIKE', '电动自行车', '电动车', '纯电动', '无', '自动挡',
        '后驱', '', '400', 1, '无', '白', 0.30, 1, '2026-04-22 16:52:23', '2026-05-22 22:50:36');
INSERT INTO `car_models`
VALUES (19, '小牛', 'GOVA', '小牛 GOVA F0 电动自行车', 'EBIKE', '电动自行车', '电动车', '纯电动', '无', '自动挡',
        '后驱', '', '400', 1, '无', '红', 0.35, 1, '2026-04-22 16:52:23', '2026-05-22 22:50:43');
INSERT INTO `car_models`
VALUES (21, '绿源', 'S30', '绿源 S30 电动轻便摩托车', 'MOTORCYCLE', '电动轻便摩托车', '电动车', '纯电动', '无',
        '自动挡', '后驱', '', '80', 1, '无', '黑', 0.45, 1, '2026-04-22 16:52:23', '2026-04-26 22:16:52');
INSERT INTO `car_models`
VALUES (22, '新日', '幻影', '新日 幻影 F5 电动自行车', 'EBIKE', '电动自行车', '电动车', '纯电动', '无', '自动挡',
        '后驱', '', '40', 1, '无', '蓝', 0.33, 1, '2026-04-22 16:52:23', '2026-04-26 22:16:37');
INSERT INTO `car_models`
VALUES (23, '立马', '闪电', '立马 闪电 电动摩托车', 'MOTORCYCLE', '电动摩托车', '电动车', '纯电动', '无', '自动挡',
        '后驱', '', '120', 1, '无', '黄', 0.50, 1, '2026-04-22 16:52:23', '2026-04-26 22:15:22');
INSERT INTO `car_models`
VALUES (24, '速珂', 'CU', '速珂 CU 智能电动自行车', 'EBIKE', '电动自行车', '电动车', '纯电动', '无', '自动挡', '后驱',
        '', '40', 1, '无', '白', 0.42, 1, '2026-04-22 16:52:23', '2026-04-26 22:16:44');

-- ----------------------------
-- Table structure for contact_message
-- ----------------------------
DROP TABLE IF EXISTS `contact_message`;
CREATE TABLE `contact_message`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     bigint(0)                                                     NOT NULL COMMENT '用户ID',
    `subject`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '留言主题',
    `message`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '留言内容',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户留言表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contact_message
-- ----------------------------
INSERT INTO `contact_message`
VALUES (4, 22, 'other', '请问你家有没有电动自行车？', '2026-05-20 17:30:14');

-- ----------------------------
-- Table structure for conversation
-- ----------------------------
DROP TABLE IF EXISTS `conversation`;
CREATE TABLE `conversation`
(
    `id`      bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `time`    datetime(0)        NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `user_id` json               NOT NULL COMMENT '参与用户ID列表，如 [101,102]',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '聊天对话表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conversation
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_record
-- ----------------------------
DROP TABLE IF EXISTS `maintenance_record`;
CREATE TABLE `maintenance_record`
(
    `id`               bigint(0)                                                     NOT NULL AUTO_INCREMENT,
    `vehicle_id`       bigint(0)                                                     NOT NULL COMMENT '车辆ID，关联car表',
    `maintenance_item` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '维修项目',
    `cost`             decimal(10, 2)                                                NOT NULL COMMENT '维修费用',
    `maintenance_date` date                                                          NOT NULL COMMENT '维修日期',
    `status`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待处理，ongoing-进行中，completed-已完成',
    `remarks`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '备注说明',
    `create_time`      datetime(0)                                                   NULL     DEFAULT NULL,
    `update_time`      datetime(0)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_vehicle_id` (`vehicle_id`) USING BTREE,
    INDEX `idx_maintenance_date` (`maintenance_date`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 21
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '车辆维修记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of maintenance_record
-- ----------------------------
INSERT INTO `maintenance_record`
VALUES (20, 1, '保养', 150.00, '2026-05-20', 'completed', '', '2026-05-30 15:11:53', '2026-05-30 15:11:53');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`              bigint(0) UNSIGNED                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `conversation_id` bigint(0) UNSIGNED                                    NOT NULL COMMENT '对话ID，关联 conversation.id',
    `user_id`         bigint(0) UNSIGNED                                    NOT NULL COMMENT '发送者用户ID',
    `text`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容（文本或JSON格式的附件信息）',
    `type`            tinyint(0)                                            NOT NULL DEFAULT 1 COMMENT '消息类型：1-文本，2-图片，3-文件，4-语音',
    `time`            datetime(0)                                           NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '发送时间',
    `state`           bigint(0)                                             NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_conversation_time` (`conversation_id`, `time`) USING BTREE COMMENT '按对话和时间查询消息'
) ENGINE = InnoDB
  AUTO_INCREMENT = 44
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '聊天消息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`
(
    `id`             bigint(0)                                                     NOT NULL AUTO_INCREMENT,
    `title`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
    `content`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '公告内容',
    `type`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '类型：system/activity/maintenance/other',
    `status`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'draft' COMMENT '状态：draft/published/archived',
    `priority`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'normal' COMMENT '优先级：normal/important/urgent',
    `publisher_id`   bigint(0)                                                     NULL     DEFAULT NULL COMMENT '发布者用户ID',
    `publisher_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '发布者姓名（冗余）',
    `view_count`     int(0)                                                        NOT NULL DEFAULT 0 COMMENT '浏览量',
    `publish_time`   datetime(0)                                                   NULL     DEFAULT NULL COMMENT '发布时间',
    `scheduled_time` datetime(0)                                                   NULL     DEFAULT NULL COMMENT '定时发布时间',
    `expire_time`    datetime(0)                                                   NULL     DEFAULT NULL COMMENT '过期时间，NULL表示永不过期',
    `create_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `update_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_status` (`status`) USING BTREE,
    INDEX `idx_type` (`type`) USING BTREE,
    INDEX `idx_publish_time` (`publish_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 32
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统公告表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice`
VALUES (31, '系统升级', '系统将于下月1号升级！', 'system', 'published', 'normal', 1, 'admin', 0, '2026-05-30 00:44:11',
        NULL, NULL, '2026-05-30 00:44:09', '2026-05-30 00:44:09');

-- ----------------------------
-- Table structure for notice_attachment
-- ----------------------------
DROP TABLE IF EXISTS `notice_attachment`;
CREATE TABLE `notice_attachment`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT,
    `notice_id`   bigint(0)                                                     NULL     DEFAULT NULL,
    `file_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
    `file_url`    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件URL',
    `file_size`   bigint(0)                                                     NULL     DEFAULT NULL COMMENT '文件大小（字节）',
    `create_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_notice_id` (`notice_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 45
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告附件关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`
(
    `id`                     bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键',
    `order_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '对外展示的订单号，唯一',
    `user_id`                bigint(0)                                                     NOT NULL COMMENT '租车用户ID',
    `car_id`                 bigint(0)                                                     NOT NULL COMMENT '租赁车辆ID',
    `role_at_rental`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '租车时的用户角色快照',
    `daily_rent_snapshot`    decimal(10, 2)                                                NOT NULL COMMENT '租车时的日租金快照',
    `deposit_snapshot`       decimal(10, 2)                                                NOT NULL COMMENT '租车时的押金快照',
    `contact_name`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '联系人姓名',
    `contact_phone`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '联系电话',
    `contact_email`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '联系邮箱',
    `pickup_location`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '取车地点',
    `return_location`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '还车地点',
    `rent_start_time`        datetime(0)                                                   NOT NULL COMMENT '租赁开始时间',
    `rent_end_time`          datetime(0)                                                   NOT NULL COMMENT '预计归还时间',
    `actual_return_time`     datetime(0)                                                   NULL     DEFAULT NULL COMMENT '实际归还时间',
    `total_days`             int(0)                                                        NOT NULL COMMENT '租用天数',
    `total_amount`           decimal(10, 2)                                                NOT NULL COMMENT '订单总金额',
    `overdue_fee`            decimal(10, 2)                                                NOT NULL COMMENT '超时费',
    `penalty_amount`         decimal(10, 2)                                                NOT NULL COMMENT '罚金',
    `inspection_description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '检测说明',
    `final_car_state`        tinyint(0)                                                    NULL     DEFAULT NULL COMMENT '检测后车辆状态：0-可租，2-维修中',
    `payment_time`           datetime(0)                                                   NULL     DEFAULT NULL COMMENT '支付时间',
    `status`                 varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '订单状态：待支付，已支付，待取车审核, 租赁中，待归还，待还车审核, 已完成，已取消，异常',
    `create_time`            datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`            datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
    `payment_method`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '支付方式',
    `rent_amount`            decimal(10, 2)                                                NULL COMMENT '实际支付的租金金额（折扣后，不含服务费和押金）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_order_no` (`order_no`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_car_id` (`car_id`) USING BTREE,
    INDEX `idx_status` (`status`) USING BTREE,
    INDEX `idx_rent_start_time` (`rent_start_time`) USING BTREE,
    INDEX `idx_rent_end_time` (`rent_end_time`) USING BTREE,
    CONSTRAINT `fk_order_car` FOREIGN KEY (`car_id`) REFERENCES `car_info` (`car_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 108
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '租赁订单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order`
VALUES (97, 'ORD202605232233247CB8', 22, 25, 'user', 10.00, 200.00, '鲍勃', '15583183189', '123321@123.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-05-24 09:00:00', '2026-06-08 18:00:00',
        '2026-05-24 22:10:50', 15, 350.00, 0.00, 50.00, '', NULL, '2026-05-23 22:33:28', '已完成',
        '2026-05-23 22:33:25', '2026-05-24 22:10:50', NULL, 20.00);
INSERT INTO `order`
VALUES (99, 'ORD202605242151440117', 24, 62, 'user', 30.00, 300.00, '123', '18898798798', '123@123.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-05-25 09:00:00', '2026-05-27 18:00:00',
        '2026-05-24 21:52:34', 2, 360.00, 0.00, 50.00, '', NULL, '2026-05-24 21:51:51', '已完成', '2026-05-24 21:51:45',
        '2026-05-24 21:52:34', NULL, 60.00);
INSERT INTO `order`
VALUES (100, 'ORD20260530002527897D', 25, 40, 'user', 10.00, 200.00, '123', '15589089089', '3050155111174@qq.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-05-31 14:00:00', '2026-06-02 18:00:00',
        '2026-05-30 00:28:35', 2, 220.00, 0.00, 0.00, '', NULL, '2026-05-30 00:25:35', '已完成', '2026-05-30 00:25:28',
        '2026-05-30 00:28:35', 'wechat', 20.00);
INSERT INTO `order`
VALUES (101, 'ORD20260530003239F128', 25, 40, 'student', 10.00, 140.00, '123', '15589089089', '3050155111174@qq.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-05-31 09:00:00', '2026-06-02 18:00:00',
        '2026-05-30 00:33:16', 2, 157.00, 0.00, 0.00, '', NULL, '2026-05-30 00:32:43', '已完成', '2026-05-30 00:32:39',
        '2026-05-30 00:33:16', 'wechat', 17.00);
INSERT INTO `order`
VALUES (102, 'ORD20260530003335B629', 25, 40, 'student', 10.00, 140.00, '123', '15589089089', '3050155111174@qq.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-05-31 09:00:00', '2026-06-06 18:00:00',
        '2026-05-30 00:34:27', 6, 191.00, 0.00, 0.00, '', NULL, '2026-05-30 00:33:40', '已完成', '2026-05-30 00:33:36',
        '2026-05-30 00:34:27', 'wechat', 17.00);
INSERT INTO `order`
VALUES (103, 'ORD2026053000352196CD', 25, 40, 'student', 10.00, 140.00, '123', '15589089089', '3050155111174@qq.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-05-31 09:00:00', '2026-06-04 18:00:00',
        '2026-05-30 00:36:05', 4, 174.00, 0.00, 0.00, '', NULL, '2026-05-30 00:35:26', '已完成', '2026-05-30 00:35:21',
        '2026-05-30 00:36:05', 'wechat', 17.00);
INSERT INTO `order`
VALUES (104, 'ORD2026053000423255B6', 25, 40, 'student', 10.00, 140.00, '123', '15589089089', '3050155111174@qq.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-05-31 09:00:00', '2026-06-02 18:00:00',
        '2026-05-30 00:43:12', 2, 157.00, 0.00, 10.00, '', NULL, '2026-05-30 00:42:35', '已完成', '2026-05-30 00:42:32',
        '2026-05-30 00:43:12', 'wechat', 17.00);
INSERT INTO `order`
VALUES (105, 'ORD202605301951288F22', 24, 40, 'user', 10.00, 200.00, '123', '18898798798', '123@123.com',
        '四川省宜宾市翠屏区四川轻化工大学东门', '四川轻化工大学（宜宾校区）西门', '2026-05-31 09:00:00',
        '2026-06-02 18:00:00', NULL, 2, 220.00, 0.00, 0.00, NULL, NULL, NULL, '已取消', '2026-05-30 19:51:29',
        '2026-05-30 19:51:29', NULL, 20.00);
INSERT INTO `order`
VALUES (106, 'ORD202605301951513B09', 24, 45, 'user', 30.00, 300.00, '123', '18898798798', '123@123.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-05-31 09:00:00', '2026-06-02 18:00:00',
        '2026-05-30 19:54:53', 2, 360.00, 0.00, 0.00, '', NULL, '2026-05-30 19:52:00', '已完成', '2026-05-30 19:51:51',
        '2026-05-30 19:54:53', 'wechat', 60.00);
INSERT INTO `order`
VALUES (107, 'ORD20260531153729C5F0', 1, 47, 'admin', 30.00, 0.00, '螺纹钢123', '15583183180', '30501551274@qq.com',
        '四川轻化工大学（宜宾校区）西门', '四川轻化工大学（宜宾校区）西门', '2026-06-01 09:00:00', '2026-06-03 18:00:00',
        NULL, 2, 100.00, 0.00, 0.00, NULL, NULL, '2026-05-31 15:37:33', '待取车', '2026-05-31 15:37:30',
        '2026-05-31 15:37:30', 'wechat', 0.00);

-- ----------------------------
-- Table structure for order_log
-- ----------------------------
DROP TABLE IF EXISTS `order_log`;
CREATE TABLE `order_log`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT,
    `order_id`    bigint(0)                                                     NOT NULL COMMENT '订单ID',
    `operator`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '操作人（管理员用户名）',
    `action`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '操作类型',
    `content`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志内容',
    `create_time` datetime(0)                                                   NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_order_id` (`order_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 423
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单操作日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_log
-- ----------------------------
INSERT INTO `order_log`
VALUES (369, 97, 'bob', '新增订单', '提交订单', '2026-05-23 22:33:25');
INSERT INTO `order_log`
VALUES (370, 97, 'bob', '已支付', '用户支付订单费用', '2026-05-23 22:33:28');
INSERT INTO `order_log`
VALUES (371, 97, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-23 22:33:51');
INSERT INTO `order_log`
VALUES (372, 97, 'bob', 'EXTEND_PAY', '用户支付延长租期2天费用，金额：20.00，方式：微信支付', '2026-05-23 22:34:07');
INSERT INTO `order_log`
VALUES (373, 97, 'bob', 'EXTEND_PAY', '用户支付延长租期10天费用，金额：100.00，方式：微信支付', '2026-05-24 19:15:04');
INSERT INTO `order_log`
VALUES (374, 97, 'bob', 'EXTEND_PAY', '用户支付延长租期1天费用，金额：10.00，方式：支付宝', '2026-05-24 19:21:54');
INSERT INTO `order_log`
VALUES (377, 98, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-24 21:12:17');
INSERT INTO `order_log`
VALUES (379, 98, 'admin', 'RETURN', '管理员确认还车，罚金：100，超时费：0，押金退还：300.00，租金调整：97.9200',
        '2026-05-24 21:32:43');
INSERT INTO `order_log`
VALUES (380, 99, 'users', '新增订单', '提交订单', '2026-05-24 21:51:45');
INSERT INTO `order_log`
VALUES (381, 99, 'users', '已支付', '用户支付订单费用', '2026-05-24 21:51:51');
INSERT INTO `order_log`
VALUES (382, 99, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-24 21:52:03');
INSERT INTO `order_log`
VALUES (383, 99, 'users', '已申请还车', '用户申请还车', '2026-05-24 21:52:09');
INSERT INTO `order_log`
VALUES (384, 99, 'admin', 'RETURN',
        '管理员确认还车，罚金总额：50，押金退还：250.00，实际还车时间：2026-05-24T21:52:34.418921800', '2026-05-24 21:52:34');
INSERT INTO `order_log`
VALUES (385, 97, 'bob', '已申请还车', '用户申请还车', '2026-05-24 22:10:24');
INSERT INTO `order_log`
VALUES (386, 97, 'admin', 'RETURN',
        '管理员确认还车，罚金总额：50，押金退还：150.00，实际还车时间：2026-05-24T22:10:50.438881', '2026-05-24 22:10:50');
INSERT INTO `order_log`
VALUES (387, 100, 'alice', '新增订单', '提交订单', '2026-05-30 00:25:28');
INSERT INTO `order_log`
VALUES (388, 100, 'alice', '已支付', '用户支付订单费用', '2026-05-30 00:25:35');
INSERT INTO `order_log`
VALUES (389, 100, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-30 00:28:14');
INSERT INTO `order_log`
VALUES (390, 100, 'alice', '已申请还车', '用户申请还车', '2026-05-30 00:28:26');
INSERT INTO `order_log`
VALUES (391, 100, 'admin', 'RETURN',
        '管理员确认还车，罚金总额：0，超时费：0，押金退还：200.00，实际还车时间：2026-05-30T00:28:35.049383300',
        '2026-05-30 00:28:35');
INSERT INTO `order_log`
VALUES (392, 101, 'alice', '新增订单', '提交订单', '2026-05-30 00:32:39');
INSERT INTO `order_log`
VALUES (393, 101, 'alice', '已支付', '用户支付订单费用', '2026-05-30 00:32:43');
INSERT INTO `order_log`
VALUES (394, 101, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-30 00:32:58');
INSERT INTO `order_log`
VALUES (395, 101, 'alice', '已申请还车', '用户申请还车', '2026-05-30 00:33:09');
INSERT INTO `order_log`
VALUES (396, 101, 'admin', 'RETURN',
        '管理员确认还车，罚金总额：0，超时费：0，押金退还：140.00，实际还车时间：2026-05-30T00:33:16.362314200',
        '2026-05-30 00:33:16');
INSERT INTO `order_log`
VALUES (397, 102, 'alice', '新增订单', '提交订单', '2026-05-30 00:33:36');
INSERT INTO `order_log`
VALUES (398, 102, 'alice', '已支付', '用户支付订单费用', '2026-05-30 00:33:40');
INSERT INTO `order_log`
VALUES (399, 102, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-30 00:33:47');
INSERT INTO `order_log`
VALUES (400, 102, 'alice', 'EXTEND_PAY', '用户支付延长租期4天费用，金额：34.00，方式：微信支付', '2026-05-30 00:33:58');
INSERT INTO `order_log`
VALUES (401, 102, 'alice', '已申请还车', '用户申请还车', '2026-05-30 00:34:18');
INSERT INTO `order_log`
VALUES (402, 102, 'admin', 'RETURN',
        '管理员确认还车，罚金总额：0，超时费：0，押金退还：140.00，实际还车时间：2026-05-30T00:34:26.929911900',
        '2026-05-30 00:34:27');
INSERT INTO `order_log`
VALUES (403, 103, 'alice', '新增订单', '提交订单', '2026-05-30 00:35:21');
INSERT INTO `order_log`
VALUES (404, 103, 'alice', '已支付', '用户支付订单费用', '2026-05-30 00:35:26');
INSERT INTO `order_log`
VALUES (405, 103, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-30 00:35:40');
INSERT INTO `order_log`
VALUES (406, 103, 'alice', 'EXTEND_PAY', '用户支付延长租期2天费用，金额：17.00，方式：微信支付', '2026-05-30 00:35:51');
INSERT INTO `order_log`
VALUES (407, 103, 'alice', '已申请还车', '用户申请还车', '2026-05-30 00:35:59');
INSERT INTO `order_log`
VALUES (408, 103, 'admin', 'RETURN',
        '管理员确认还车，罚金总额：0，超时费：0，押金退还：140.00，实际还车时间：2026-05-30T00:36:05.439380400',
        '2026-05-30 00:36:05');
INSERT INTO `order_log`
VALUES (409, 104, 'alice', '新增订单', '提交订单', '2026-05-30 00:42:32');
INSERT INTO `order_log`
VALUES (410, 104, 'alice', '已支付', '用户支付订单费用', '2026-05-30 00:42:35');
INSERT INTO `order_log`
VALUES (411, 104, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-30 00:42:44');
INSERT INTO `order_log`
VALUES (412, 104, 'alice', '已申请还车', '用户申请还车', '2026-05-30 00:42:51');
INSERT INTO `order_log`
VALUES (413, 104, 'admin', 'RETURN',
        '管理员确认还车，罚金总额：10，超时费：0，押金退还：130.00，实际还车时间：2026-05-30T00:43:12.132103600',
        '2026-05-30 00:43:12');
INSERT INTO `order_log`
VALUES (414, 105, 'users', '新增订单', '提交订单', '2026-05-30 19:51:29');
INSERT INTO `order_log`
VALUES (415, 105, 'users', '已取消', '用户取消订单', '2026-05-30 19:51:43');
INSERT INTO `order_log`
VALUES (416, 106, 'users', '新增订单', '提交订单', '2026-05-30 19:51:51');
INSERT INTO `order_log`
VALUES (417, 106, 'users', '已支付', '用户支付订单费用', '2026-05-30 19:52:00');
INSERT INTO `order_log`
VALUES (418, 106, 'admin', 'PICKUP', '管理员确认取车，订单进入租赁中', '2026-05-30 19:52:34');
INSERT INTO `order_log`
VALUES (419, 106, 'users', '已申请还车', '用户申请还车', '2026-05-30 19:53:44');
INSERT INTO `order_log`
VALUES (420, 106, 'admin', 'RETURN',
        '管理员确认还车，罚金总额：0，超时费：0，押金退还：300.00，实际还车时间：2026-05-30T19:54:52.577783200',
        '2026-05-30 19:54:53');
INSERT INTO `order_log`
VALUES (421, 107, 'admin', '新增订单', '提交订单', '2026-05-31 15:37:30');
INSERT INTO `order_log`
VALUES (422, 107, 'admin', '已支付', '用户支付订单费用', '2026-05-31 15:37:33');

-- ----------------------------
-- Table structure for order_penalty
-- ----------------------------
DROP TABLE IF EXISTS `order_penalty`;
CREATE TABLE `order_penalty`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT,
    `order_id`    bigint(0)                                                     NOT NULL COMMENT '订单ID',
    `reason`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '罚金原因（如：超时费、车损费、清洁费等）',
    `amount`      decimal(10, 2)                                                NOT NULL COMMENT '罚金金额',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_order_id` (`order_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单罚金明细表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_penalty
-- ----------------------------
INSERT INTO `order_penalty`
VALUES (8, 99, '车壳损坏', 50.00, '2026-05-24 21:52:34');
INSERT INTO `order_penalty`
VALUES (9, 97, '超时费', 50.00, '2026-05-24 22:10:50');
INSERT INTO `order_penalty`
VALUES (10, 104, '车辆损坏', 10.00, '2026-05-30 00:43:12');

-- ----------------------------
-- Table structure for order_service
-- ----------------------------
DROP TABLE IF EXISTS `order_service`;
CREATE TABLE `order_service`
(
    `id`                    bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `order_id`              bigint(0)                                                     NOT NULL COMMENT '订单ID',
    `service_id`            bigint(0)                                                     NOT NULL COMMENT '服务ID',
    `service_name_snapshot` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务名称快照',
    `price_snapshot`        decimal(10, 2)                                                NOT NULL COMMENT '服务单价快照',
    `quantity`              int(0)                                                        NOT NULL DEFAULT 1 COMMENT '数量（通常为租车天数）',
    `total_price`           decimal(10, 2)                                                NOT NULL COMMENT '该项服务总价',
    `create_time`           datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_order_id` (`order_id`) USING BTREE,
    INDEX `idx_service_id` (`service_id`) USING BTREE,
    CONSTRAINT `fk_order_service_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_order_service_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 120
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '订单增值服务关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_service
-- ----------------------------
INSERT INTO `order_service`
VALUES (118, 107, 2, '儿童安全座椅', 30.00, 2, 60.00, '2026-05-31 15:37:30');
INSERT INTO `order_service`
VALUES (119, 107, 3, '车载Wi-Fi + 手机支架', 20.00, 2, 40.00, '2026-05-31 15:37:30');

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`
(
    `id`             bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `user_id`        bigint(0)                                                      NOT NULL COMMENT '评价用户ID',
    `car_id`         bigint(0)                                                      NOT NULL COMMENT '关联车辆ID',
    `order_id`       bigint(0)                                                      NULL     DEFAULT NULL COMMENT '关联订单ID（可选）',
    `rating`         tinyint(0)                                                     NOT NULL COMMENT '评分：1-5星',
    `content`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          NOT NULL COMMENT '评价内容',
    `images`         varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '评价图片URL，多个用逗号分隔',
    `likes_count`    int(0)                                                         NOT NULL DEFAULT 0 COMMENT '点赞数',
    `comments_count` int(0)                                                         NOT NULL DEFAULT 0 COMMENT '评论数',
    `status`         tinyint(0)                                                     NOT NULL DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    `featured`       tinyint(0)                                                     NOT NULL DEFAULT 0 COMMENT '是否为精选评价',
    `create_time`    datetime(0)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `update_time`    datetime(0)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_car_id` (`car_id`) USING BTREE,
    INDEX `idx_order_id` (`order_id`) USING BTREE,
    INDEX `idx_rating` (`rating`) USING BTREE,
    CONSTRAINT `fk_review_car` FOREIGN KEY (`car_id`) REFERENCES `car_info` (`car_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `fk_review_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户评价表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review`
VALUES (18, 25, 40, 100, 5, '好',
        'http://localhost:8080/api/uploads/2026/05/30/243282c4-3ed6-4ed5-8787-0ca067e72780.jpg', 0, 0, 1, 0,
        '2026-05-30 00:31:00', '2026-05-30 00:31:00');

-- ----------------------------
-- Table structure for review_like
-- ----------------------------
DROP TABLE IF EXISTS `review_like`;
CREATE TABLE `review_like`
(
    `id`          bigint(0)   NOT NULL AUTO_INCREMENT,
    `review_id`   bigint(0)   NOT NULL,
    `user_id`     bigint(0)   NOT NULL,
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_review_user` (`review_id`, `user_id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    CONSTRAINT `fk_like_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '评价点赞记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review_like
-- ----------------------------

-- ----------------------------
-- Table structure for role_benefits
-- ----------------------------
DROP TABLE IF EXISTS `role_benefits`;
CREATE TABLE `role_benefits`
(
    `id`                   bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role`                 varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '角色标识，如ordinary, student, vip, admin',
    `deposit_rate`         decimal(3, 2)                                                 NOT NULL DEFAULT 1.00 COMMENT '押金比例，1.0=100%押金，0=免押金',
    `rent_discount`        decimal(3, 2)                                                 NOT NULL DEFAULT 1.00 COMMENT '租金折扣，1.0=无折扣，0.8=八折',
    `free_extension_count` int(0)                                                        NOT NULL DEFAULT 0 COMMENT '每订单免费延长次数',
    `overdue_fee_rate`     decimal(3, 2)                                                 NOT NULL DEFAULT 1.00 COMMENT '超时费率系数，如1.5表示1.5倍',
    `description`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '角色权益描述',
    `points_threshold`     int(0)                                                        NULL     DEFAULT -1 COMMENT '升级所需积分，-1表示不适用',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_role` (`role`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色权益配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_benefits
-- ----------------------------
INSERT INTO `role_benefits`
VALUES (1, 'admin', 0.00, 0.00, 5, 1.00, '管理员', 0);
INSERT INTO `role_benefits`
VALUES (2, 'user', 1.00, 1.00, 0, 1.50, '普通用户', -1);
INSERT INTO `role_benefits`
VALUES (5, 'student', 0.70, 0.85, 1, 1.20, '校园用户', -1);

-- ----------------------------
-- Table structure for service
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '服务ID',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '服务描述',
    `price`       decimal(10, 2)                                                NOT NULL COMMENT '每日单价',
    `type`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '服务类型：insurance/child_seat/wifi等',
    `status`      tinyint(0)                                                    NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `update_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_type` (`type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '增值服务表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service
-- ----------------------------
INSERT INTO `service`
VALUES (1, '全面保障升级 (高级版)', '第三者升至50万，免赔额500元，含道路救援', 35.00, 'insurance', 1,
        '2026-03-08 16:04:26', '2026-03-08 16:04:26');
INSERT INTO `service`
VALUES (2, '儿童安全座椅', '适合9个月-12岁，ISOFIX接口', 30.00, 'child_seat', 1, '2026-03-08 16:04:26',
        '2026-03-08 16:04:26');
INSERT INTO `service`
VALUES (3, '车载Wi-Fi + 手机支架', '4G无限流量，出行更便捷', 20.00, 'wifi', 1, '2026-03-08 16:04:26',
        '2026-03-08 16:04:26');

-- ----------------------------
-- Table structure for station
-- ----------------------------
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station`
(
    `id`             bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '网点ID',
    `name`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网点名称',
    `address`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详细地址',
    `latitude`       decimal(10, 8)                                                NULL     DEFAULT NULL COMMENT '纬度',
    `longitude`      decimal(11, 8)                                                NULL     DEFAULT NULL COMMENT '经度',
    `contact_phone`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '联系电话',
    `business_hours` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '营业时间',
    `status`         tinyint(0)                                                    NOT NULL DEFAULT 1 COMMENT '状态：0-关闭，1-营业中',
    `sort_order`     int(0)                                                        NOT NULL DEFAULT 0 COMMENT '排序序号',
    `create_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `update_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '取还车网点表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of station
-- ----------------------------
INSERT INTO `station`
VALUES (1, '轻化工西门', '四川轻化工大学（宜宾校区）西门', 0.00010000, 0.00010000, '15583283287', '08:00-22:00', 1, 1,
        '2026-03-17 11:52:35', '2026-03-17 11:52:35');
INSERT INTO `station`
VALUES (3, '轻化工东门', '四川省宜宾市翠屏区四川轻化工大学东门', 0.00060000, 0.00080000, '155587687685', '09：00-15：00',
        1, 3, '2026-03-28 14:04:49', '2026-03-28 14:04:49');

-- ----------------------------
-- Table structure for system_configuration
-- ----------------------------
DROP TABLE IF EXISTS `system_configuration`;
CREATE TABLE `system_configuration`
(
    `id`             bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '配置ID，主键',
    `type`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类别',
    `config_key`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键，唯一',
    `config_value`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置值',
    `description`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '配置描述',
    `effective_time` datetime(0)                                                   NULL     DEFAULT NULL COMMENT '生效时间',
    `expire_time`    datetime(0)                                                   NULL     DEFAULT NULL COMMENT '过期时间',
    `create_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_config_key` (`config_key`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 61
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '系统动态配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_configuration
-- ----------------------------
INSERT INTO `system_configuration`
VALUES (17, '0', 'logo', 'http://localhost:8080/api/uploads/2026/04/22/78140ae1-e02f-4fbf-b3c2-6546d2d02f0a.png',
        'logo', NULL, NULL, '2026-03-21 16:21:54', '2026-03-21 16:21:54');
INSERT INTO `system_configuration`
VALUES (40, '0', 'platformName', '宜出行 校园租车平台', 'platformName', NULL, NULL, '2026-04-02 16:18:08',
        '2026-04-02 16:18:08');
INSERT INTO `system_configuration`
VALUES (41, '0', 'platformText', '致力于为用户提供最便捷、最安全的租车服务。', 'platformText', NULL, NULL,
        '2026-04-02 16:18:08', '2026-04-02 16:18:08');
INSERT INTO `system_configuration`
VALUES (42, '0', 'platformUrl', 'https://yichuxing.edu.cn', 'platformUrl', NULL, NULL, '2026-04-02 16:18:08',
        '2026-04-02 16:18:08');
INSERT INTO `system_configuration`
VALUES (43, '0', 'platformEmail', 'support@yichuxing.edu.cn', 'platformEmail', NULL, NULL, '2026-04-02 16:18:08',
        '2026-04-02 16:18:08');
INSERT INTO `system_configuration`
VALUES (44, '0', 'platformPhone', '400-888-9999', 'platformPhone', NULL, NULL, '2026-04-02 16:18:08',
        '2026-04-02 16:18:08');
INSERT INTO `system_configuration`
VALUES (45, '0', 'platformAddress', '北京市朝阳区科技园路88号 宜出行 大厦 12层', 'platformAddress', NULL, NULL,
        '2026-04-02 16:18:08', '2026-04-02 16:18:08');
INSERT INTO `system_configuration`
VALUES (46, '0', 'platformWorkTime', '周一至周日 8:00 - 22:00', 'platformWorkTime', NULL, NULL, '2026-04-02 16:18:08',
        '2026-04-02 16:18:08');
INSERT INTO `system_configuration`
VALUES (47, '0', 'footerText', '© 2026 宜出行校园租车平台', 'footerText', NULL, NULL, '2026-04-02 16:18:08',
        '2026-04-02 16:18:08');

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `level`       varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '日志级别：DEBUG, INFO, WARN, ERROR',
    `category`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '日志分类：SYSTEM, USER, DATABASE',
    `content`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '日志内容摘要',
    `detail`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '参数信息',
    `user_id`     bigint(0)                                                     NULL     DEFAULT NULL COMMENT '关联用户ID',
    `username`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '用户名冗余',
    `create_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_level` (`level`) USING BTREE,
    INDEX `idx_category` (`category`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1018
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '系统日志表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_log
-- ----------------------------
INSERT INTO `system_log`
VALUES (1, 'INFO', 'SYSTEM', '测试', '测试日志', 1, 'admin', '2026-04-02 11:15:17');
INSERT INTO `system_log`
VALUES (2, 'INFO', 'USER', '测试', '用户日志', 4, 'user', '2026-04-02 12:37:32');
INSERT INTO `system_log`
VALUES (3, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 24ms', 1, 'admin', '2026-04-02 14:38:16');
INSERT INTO `system_log`
VALUES (4, 'INFO', 'NOTICE', '下架公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-02 14:38:20');
INSERT INTO `system_log`
VALUES (5, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-02 14:42:20');
INSERT INTO `system_log`
VALUES (6, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 48ms', 1, 'admin', '2026-04-02 15:39:55');
INSERT INTO `system_log`
VALUES (7, 'ERROR', 'SYSTEM', '保存系统配置',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLException: Field \'config_value\' doesn\'t have a default value\r\n### The error may exist in com/suse/www/campus_rent/mapper/SystemConfigurationMapper.java (best guess)\r\n### The error may involve com.suse.www.campus_rent.mapper.SystemConfigurationMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO system_configuration  ( type, config_key,  description,   create_time, update_time )  VALUES (  ?, ?,  ?,   ?, ?  )\r\n### Cause: java.sql.SQLException: Field \'config_value\' doesn\'t have a default value\n; Field \'config_value\' doesn\'t have a default value',
        1, 'admin', '2026-04-02 16:14:31');
INSERT INTO `system_log`
VALUES (8, 'ERROR', 'SYSTEM', '保存系统配置',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLException: Field \'config_value\' doesn\'t have a default value\r\n### The error may exist in com/suse/www/campus_rent/mapper/SystemConfigurationMapper.java (best guess)\r\n### The error may involve com.suse.www.campus_rent.mapper.SystemConfigurationMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO system_configuration  ( type, config_key,  description,   create_time, update_time )  VALUES (  ?, ?,  ?,   ?, ?  )\r\n### Cause: java.sql.SQLException: Field \'config_value\' doesn\'t have a default value\n; Field \'config_value\' doesn\'t have a default value',
        1, 'admin', '2026-04-02 16:14:39');
INSERT INTO `system_log`
VALUES (9, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 106ms', 1, 'admin', '2026-04-02 16:18:08');
INSERT INTO `system_log`
VALUES (10, 'ERROR', 'AUTH', '用户登录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'ip\' in \'field list\'\r\n### The error may exist in com/suse/www/campus_rent/mapper/UserOperationLogMapper.java (best guess)\r\n### The error may involve com.suse.www.campus_rent.mapper.UserOperationLogMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO user_operation_log  ( user_id, username, action, content, ip, result, create_time )  VALUES (  ?, ?, ?, ?, ?, ?, ?  )\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'ip\' in \'field list\'\n; bad SQL grammar []',
        0, '系统/匿名', '2026-04-02 16:24:45');
INSERT INTO `system_log`
VALUES (11, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 183ms', 0, '系统/匿名', '2026-04-02 16:29:14');
INSERT INTO `system_log`
VALUES (12, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 22ms', 1, 'admin', '2026-04-02 17:17:47');
INSERT INTO `system_log`
VALUES (13, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-02 17:35:50');
INSERT INTO `system_log`
VALUES (14, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:35:50');
INSERT INTO `system_log`
VALUES (15, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:35:50');
INSERT INTO `system_log`
VALUES (16, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-02 17:35:50');
INSERT INTO `system_log`
VALUES (17, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:37:16');
INSERT INTO `system_log`
VALUES (18, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-02 17:37:16');
INSERT INTO `system_log`
VALUES (19, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:37:16');
INSERT INTO `system_log`
VALUES (20, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-02 17:37:16');
INSERT INTO `system_log`
VALUES (21, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-02 17:37:16');
INSERT INTO `system_log`
VALUES (22, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:38:40');
INSERT INTO `system_log`
VALUES (23, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:38:40');
INSERT INTO `system_log`
VALUES (24, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-02 17:38:40');
INSERT INTO `system_log`
VALUES (25, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:38:40');
INSERT INTO `system_log`
VALUES (26, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:38:40');
INSERT INTO `system_log`
VALUES (27, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-02 17:38:40');
INSERT INTO `system_log`
VALUES (28, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-02 17:38:40');
INSERT INTO `system_log`
VALUES (29, 'INFO', 'USER', '上传头像', '执行成功，耗时: 28ms', 1, 'admin', '2026-04-02 17:52:23');
INSERT INTO `system_log`
VALUES (30, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-02 17:52:33');
INSERT INTO `system_log`
VALUES (31, 'INFO', 'USER', '上传头像', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-02 17:54:52');
INSERT INTO `system_log`
VALUES (32, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-02 17:55:01');
INSERT INTO `system_log`
VALUES (33, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 198ms', 0, '系统/匿名', '2026-04-02 17:59:17');
INSERT INTO `system_log`
VALUES (34, 'WARN', 'USER', '修改密码', '执行成功，耗时: 168ms', 1, 'admin', '2026-04-02 17:59:57');
INSERT INTO `system_log`
VALUES (35, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 89ms', 0, '系统/匿名', '2026-04-02 18:00:31');
INSERT INTO `system_log`
VALUES (36, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 91ms', 0, '系统/匿名', '2026-04-02 18:00:43');
INSERT INTO `system_log`
VALUES (37, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 25ms', 1, 'admin', '2026-04-02 18:02:47');
INSERT INTO `system_log`
VALUES (38, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-02 18:04:38');
INSERT INTO `system_log`
VALUES (39, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-02 18:05:42');
INSERT INTO `system_log`
VALUES (40, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 24ms', 1, 'admin', '2026-04-02 18:07:32');
INSERT INTO `system_log`
VALUES (41, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-02 18:09:13');
INSERT INTO `system_log`
VALUES (42, 'INFO', 'ROLE', '修改角色权益', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-03 14:01:28');
INSERT INTO `system_log`
VALUES (43, 'INFO', 'ROLE', '修改角色权益', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-03 14:01:44');
INSERT INTO `system_log`
VALUES (44, 'INFO', 'ROLE', '修改角色权益', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-03 14:02:10');
INSERT INTO `system_log`
VALUES (45, 'ERROR', 'USER', '修改用户', '执行失败: 手机号或邮箱已被其他用户使用', 1, 'admin', '2026-04-03 14:03:00');
INSERT INTO `system_log`
VALUES (46, 'INFO', 'USER', '修改用户', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-03 14:03:07');
INSERT INTO `system_log`
VALUES (47, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-03 14:03:28');
INSERT INTO `system_log`
VALUES (48, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 403ms', 0, '系统/匿名', '2026-04-03 18:29:13');
INSERT INTO `system_log`
VALUES (49, 'ERROR', 'VEHICLE', '新增车辆型号', '执行失败: 该品牌下型号名称已存在', 1, 'admin', '2026-04-03 22:07:17');
INSERT INTO `system_log`
VALUES (50, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-03 22:09:27');
INSERT INTO `system_log`
VALUES (51, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-03 22:10:46');
INSERT INTO `system_log`
VALUES (52, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-03 22:10:46');
INSERT INTO `system_log`
VALUES (53, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-03 22:11:00');
INSERT INTO `system_log`
VALUES (54, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 22:11:00');
INSERT INTO `system_log`
VALUES (55, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 22:11:00');
INSERT INTO `system_log`
VALUES (56, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 22:11:00');
INSERT INTO `system_log`
VALUES (57, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 22:11:00');
INSERT INTO `system_log`
VALUES (58, 'ERROR', 'VEHICLE', '修改车辆',
        '执行失败: \r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'image_urls\' at row 1\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET model_id=?, plate_number=?, vin_code=?, engine_no=?, vehicle_color=?,  current_mileage=?, current_fuel=?, license_date=?, insurance_expiry=?, inspection_expiry=?, daily_rent=?, deposit_amount=?,  status=?, image_urls=?,  update_time=?  WHERE car_id=?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'image_urls\' at row 1\n; Data truncation: Data too long for column \'image_urls\' at row 1',
        1, 'admin', '2026-04-03 22:11:00');
INSERT INTO `system_log`
VALUES (59, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 22:14:35');
INSERT INTO `system_log`
VALUES (60, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 22:14:35');
INSERT INTO `system_log`
VALUES (61, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 22:14:36');
INSERT INTO `system_log`
VALUES (62, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 22:14:36');
INSERT INTO `system_log`
VALUES (63, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 22:14:36');
INSERT INTO `system_log`
VALUES (64, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-03 22:14:36');
INSERT INTO `system_log`
VALUES (65, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-03 22:15:51');
INSERT INTO `system_log`
VALUES (66, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 22:15:51');
INSERT INTO `system_log`
VALUES (67, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 22:15:51');
INSERT INTO `system_log`
VALUES (68, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 22:15:51');
INSERT INTO `system_log`
VALUES (69, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-03 22:15:51');
INSERT INTO `system_log`
VALUES (70, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-03 22:15:51');
INSERT INTO `system_log`
VALUES (71, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-03 22:15:57');
INSERT INTO `system_log`
VALUES (72, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-03 22:16:20');
INSERT INTO `system_log`
VALUES (73, 'ERROR', 'VEHICLE', '修改车辆型号', '执行失败: 车型不存在', 1, 'admin', '2026-04-03 22:18:19');
INSERT INTO `system_log`
VALUES (74, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 20ms', 1, 'admin', '2026-04-03 22:28:55');
INSERT INTO `system_log`
VALUES (75, 'INFO', 'VEHICLE', '新增车辆型号', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-03 22:33:35');
INSERT INTO `system_log`
VALUES (76, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-03 23:02:15');
INSERT INTO `system_log`
VALUES (77, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:15');
INSERT INTO `system_log`
VALUES (78, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:15');
INSERT INTO `system_log`
VALUES (79, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:15');
INSERT INTO `system_log`
VALUES (80, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-03 23:02:15');
INSERT INTO `system_log`
VALUES (81, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-03 23:02:24');
INSERT INTO `system_log`
VALUES (82, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:24');
INSERT INTO `system_log`
VALUES (83, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:24');
INSERT INTO `system_log`
VALUES (84, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:24');
INSERT INTO `system_log`
VALUES (85, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 23:02:24');
INSERT INTO `system_log`
VALUES (86, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-03 23:02:24');
INSERT INTO `system_log`
VALUES (87, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:33');
INSERT INTO `system_log`
VALUES (88, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:33');
INSERT INTO `system_log`
VALUES (89, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 23:02:33');
INSERT INTO `system_log`
VALUES (90, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:33');
INSERT INTO `system_log`
VALUES (91, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:33');
INSERT INTO `system_log`
VALUES (92, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:33');
INSERT INTO `system_log`
VALUES (93, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-03 23:02:33');
INSERT INTO `system_log`
VALUES (94, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:42');
INSERT INTO `system_log`
VALUES (95, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:42');
INSERT INTO `system_log`
VALUES (96, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:42');
INSERT INTO `system_log`
VALUES (97, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:42');
INSERT INTO `system_log`
VALUES (98, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:42');
INSERT INTO `system_log`
VALUES (99, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:42');
INSERT INTO `system_log`
VALUES (100, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-03 23:02:42');
INSERT INTO `system_log`
VALUES (101, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-03 23:02:50');
INSERT INTO `system_log`
VALUES (102, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:50');
INSERT INTO `system_log`
VALUES (103, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:50');
INSERT INTO `system_log`
VALUES (104, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:50');
INSERT INTO `system_log`
VALUES (105, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:50');
INSERT INTO `system_log`
VALUES (106, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:50');
INSERT INTO `system_log`
VALUES (107, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-03 23:02:50');
INSERT INTO `system_log`
VALUES (108, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:02:58');
INSERT INTO `system_log`
VALUES (109, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:58');
INSERT INTO `system_log`
VALUES (110, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 23:02:58');
INSERT INTO `system_log`
VALUES (111, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 23:02:58');
INSERT INTO `system_log`
VALUES (112, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 23:02:58');
INSERT INTO `system_log`
VALUES (113, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:02:58');
INSERT INTO `system_log`
VALUES (114, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-03 23:02:58');
INSERT INTO `system_log`
VALUES (115, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-03 23:03:05');
INSERT INTO `system_log`
VALUES (116, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:03:05');
INSERT INTO `system_log`
VALUES (117, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:03:05');
INSERT INTO `system_log`
VALUES (118, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:03:05');
INSERT INTO `system_log`
VALUES (119, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-03 23:03:05');
INSERT INTO `system_log`
VALUES (120, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-03 23:03:05');
INSERT INTO `system_log`
VALUES (121, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:03:11');
INSERT INTO `system_log`
VALUES (122, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:03:11');
INSERT INTO `system_log`
VALUES (123, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-03 23:03:11');
INSERT INTO `system_log`
VALUES (124, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-03 23:03:11');
INSERT INTO `system_log`
VALUES (125, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-03 23:03:11');
INSERT INTO `system_log`
VALUES (126, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 25ms', 1, 'admin', '2026-04-03 23:08:02');
INSERT INTO `system_log`
VALUES (127, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-03 23:08:12');
INSERT INTO `system_log`
VALUES (128, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-03 23:08:19');
INSERT INTO `system_log`
VALUES (129, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-03 23:08:23');
INSERT INTO `system_log`
VALUES (130, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:08:45');
INSERT INTO `system_log`
VALUES (131, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:05');
INSERT INTO `system_log`
VALUES (132, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:07');
INSERT INTO `system_log`
VALUES (133, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:07');
INSERT INTO `system_log`
VALUES (134, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:09');
INSERT INTO `system_log`
VALUES (135, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:09');
INSERT INTO `system_log`
VALUES (136, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:10');
INSERT INTO `system_log`
VALUES (137, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:10');
INSERT INTO `system_log`
VALUES (138, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:10');
INSERT INTO `system_log`
VALUES (139, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:11');
INSERT INTO `system_log`
VALUES (140, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:11');
INSERT INTO `system_log`
VALUES (141, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:11');
INSERT INTO `system_log`
VALUES (142, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:11');
INSERT INTO `system_log`
VALUES (143, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'id\' in \'where clause\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:11:20');
INSERT INTO `system_log`
VALUES (144, 'ERROR', 'MAINTENANCE', '新增维修记录',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'state\' in \'field list\'\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.CarInfoMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE car_info  SET state=?       WHERE  (car_id = ?)\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'state\' in \'field list\'\n; bad SQL grammar []',
        1, 'admin', '2026-04-03 23:12:15');
INSERT INTO `system_log`
VALUES (145, 'INFO', 'MAINTENANCE', '新增维修记录', '执行成功，耗时: 18ms', 1, 'admin', '2026-04-03 23:17:31');
INSERT INTO `system_log`
VALUES (146, 'INFO', 'MAINTENANCE', '新增维修记录', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-03 23:18:28');
INSERT INTO `system_log`
VALUES (147, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-03 23:18:45');
INSERT INTO `system_log`
VALUES (148, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-03 23:18:57');
INSERT INTO `system_log`
VALUES (149, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 27ms', 1, 'admin', '2026-04-04 13:24:03');
INSERT INTO `system_log`
VALUES (150, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 34ms', 1, 'admin', '2026-04-04 14:25:04');
INSERT INTO `system_log`
VALUES (151, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 33ms', 1, 'admin', '2026-04-04 14:25:09');
INSERT INTO `system_log`
VALUES (152, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-04 14:25:14');
INSERT INTO `system_log`
VALUES (153, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-04 14:25:24');
INSERT INTO `system_log`
VALUES (154, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-04 14:25:39');
INSERT INTO `system_log`
VALUES (155, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-04 14:25:49');
INSERT INTO `system_log`
VALUES (156, 'INFO', 'UPLOAD', '上传图片(RImg)', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-04 14:33:22');
INSERT INTO `system_log`
VALUES (157, 'INFO', 'REVIEW', '提交评价', '执行成功，耗时: 18ms', 1, 'admin', '2026-04-04 14:33:22');
INSERT INTO `system_log`
VALUES (158, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 32ms', 1, 'admin', '2026-04-04 15:17:27');
INSERT INTO `system_log`
VALUES (159, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-04 15:17:34');
INSERT INTO `system_log`
VALUES (160, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-04 15:17:42');
INSERT INTO `system_log`
VALUES (161, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 354ms', 0, '系统/匿名', '2026-04-04 20:28:47');
INSERT INTO `system_log`
VALUES (162, 'INFO', 'NOTICE', '新增公告', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-05 15:02:49');
INSERT INTO `system_log`
VALUES (163, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-05 15:32:56');
INSERT INTO `system_log`
VALUES (164, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-05 15:49:30');
INSERT INTO `system_log`
VALUES (165, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-05 15:49:34');
INSERT INTO `system_log`
VALUES (166, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-05 15:49:39');
INSERT INTO `system_log`
VALUES (167, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-05 15:49:52');
INSERT INTO `system_log`
VALUES (168, 'INFO', 'NOTICE', '新增公告', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-05 15:53:01');
INSERT INTO `system_log`
VALUES (169, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-05 15:53:12');
INSERT INTO `system_log`
VALUES (170, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-05 15:53:16');
INSERT INTO `system_log`
VALUES (171, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-05 15:56:42');
INSERT INTO `system_log`
VALUES (172, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-05 15:57:26');
INSERT INTO `system_log`
VALUES (173, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-05 16:06:16');
INSERT INTO `system_log`
VALUES (174, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-05 16:06:22');
INSERT INTO `system_log`
VALUES (175, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-05 16:13:40');
INSERT INTO `system_log`
VALUES (176, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-05 16:13:46');
INSERT INTO `system_log`
VALUES (177, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-05 16:13:54');
INSERT INTO `system_log`
VALUES (178, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-05 16:14:03');
INSERT INTO `system_log`
VALUES (179, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-05 16:14:07');
INSERT INTO `system_log`
VALUES (180, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-05 16:14:13');
INSERT INTO `system_log`
VALUES (181, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-05 16:14:17');
INSERT INTO `system_log`
VALUES (182, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-05 16:14:22');
INSERT INTO `system_log`
VALUES (183, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-05 16:14:28');
INSERT INTO `system_log`
VALUES (184, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-05 16:15:03');
INSERT INTO `system_log`
VALUES (185, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-05 16:15:51');
INSERT INTO `system_log`
VALUES (186, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-05 16:15:56');
INSERT INTO `system_log`
VALUES (187, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-05 16:16:00');
INSERT INTO `system_log`
VALUES (188, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-05 16:16:09');
INSERT INTO `system_log`
VALUES (189, 'ERROR', 'NOTICE', '发布公告',
        '执行失败: Cannot invoke \"java.time.LocalDateTime.isBefore(java.time.chrono.ChronoLocalDateTime)\" because the return value of \"com.suse.campus_rent.entity.Notice.getExpireTime()\" is null',
        1, 'admin', '2026-04-05 16:18:57');
INSERT INTO `system_log`
VALUES (190, 'INFO', 'NOTICE', '发布公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-05 16:20:00');
INSERT INTO `system_log`
VALUES (191, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 182ms', 0, '系统/匿名', '2026-04-05 17:08:35');
INSERT INTO `system_log`
VALUES (192, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 117ms', 0, '系统/匿名', '2026-04-05 17:08:53');
INSERT INTO `system_log`
VALUES (193, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 204ms', 0, '系统/匿名', '2026-04-05 17:49:49');
INSERT INTO `system_log`
VALUES (194, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-05 17:50:08');
INSERT INTO `system_log`
VALUES (195, 'INFO', 'NOTICE', '新增公告', '执行成功，耗时: 27ms', 1, 'admin', '2026-04-05 17:55:01');
INSERT INTO `system_log`
VALUES (196, 'INFO', 'NOTICE', '发布公告', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-05 17:55:06');
INSERT INTO `system_log`
VALUES (197, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 22ms', 1, 'admin', '2026-04-05 17:55:45');
INSERT INTO `system_log`
VALUES (198, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 250ms', 0, '系统/匿名', '2026-04-09 10:27:13');
INSERT INTO `system_log`
VALUES (199, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 32ms', 4, 'user', '2026-04-09 19:17:14');
INSERT INTO `system_log`
VALUES (200, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 18ms', 4, 'user', '2026-04-09 19:20:42');
INSERT INTO `system_log`
VALUES (201, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 21ms', 4, 'user', '2026-04-09 19:53:45');
INSERT INTO `system_log`
VALUES (202, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 33ms', 4, 'user', '2026-04-09 20:13:20');
INSERT INTO `system_log`
VALUES (203, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 33ms', 4, 'user', '2026-04-09 20:13:48');
INSERT INTO `system_log`
VALUES (204, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 16ms', 4, 'user', '2026-04-09 20:16:07');
INSERT INTO `system_log`
VALUES (205, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 16ms', 4, 'user', '2026-04-09 20:16:34');
INSERT INTO `system_log`
VALUES (206, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 12ms', 4, 'user', '2026-04-09 20:22:57');
INSERT INTO `system_log`
VALUES (207, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 15ms', 4, 'user', '2026-04-09 20:23:45');
INSERT INTO `system_log`
VALUES (208, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 34ms', 4, 'user', '2026-04-09 20:28:16');
INSERT INTO `system_log`
VALUES (209, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 11ms', 4, 'user', '2026-04-09 20:29:33');
INSERT INTO `system_log`
VALUES (210, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 190ms', 0, '系统/匿名', '2026-04-09 20:30:02');
INSERT INTO `system_log`
VALUES (211, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-09 20:30:18');
INSERT INTO `system_log`
VALUES (212, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 8ms', 4, 'user', '2026-04-09 20:30:32');
INSERT INTO `system_log`
VALUES (213, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-09 20:30:40');
INSERT INTO `system_log`
VALUES (214, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 11ms', 4, 'user', '2026-04-09 20:32:31');
INSERT INTO `system_log`
VALUES (215, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 16ms', 4, 'user', '2026-04-09 20:32:38');
INSERT INTO `system_log`
VALUES (216, 'INFO', 'USER', '修改用户', '执行成功，耗时: 19ms', 1, 'admin', '2026-04-09 21:05:30');
INSERT INTO `system_log`
VALUES (217, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 15ms', 4, 'user', '2026-04-09 21:05:55');
INSERT INTO `system_log`
VALUES (218, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-09 21:06:07');
INSERT INTO `system_log`
VALUES (219, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 15ms', 4, 'user', '2026-04-09 21:06:15');
INSERT INTO `system_log`
VALUES (220, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 22ms', 1, 'admin', '2026-04-09 21:06:21');
INSERT INTO `system_log`
VALUES (221, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 43ms', 4, 'user', '2026-04-09 21:18:39');
INSERT INTO `system_log`
VALUES (222, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 35ms', 4, 'user', '2026-04-09 21:18:45');
INSERT INTO `system_log`
VALUES (223, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 35ms', 4, 'user', '2026-04-09 21:24:42');
INSERT INTO `system_log`
VALUES (224, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 18ms', 1, 'admin', '2026-04-09 21:24:53');
INSERT INTO `system_log`
VALUES (225, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 10ms', 4, 'user', '2026-04-09 21:25:03');
INSERT INTO `system_log`
VALUES (226, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 29ms', 1, 'admin', '2026-04-09 21:25:07');
INSERT INTO `system_log`
VALUES (227, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 25ms', 4, 'user', '2026-04-09 21:26:52');
INSERT INTO `system_log`
VALUES (228, 'ERROR', 'ORDER', '支付订单', '执行失败: 余额不足，请选择其他支付方式', 4, 'user', '2026-04-09 21:27:08');
INSERT INTO `system_log`
VALUES (229, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 18ms', 4, 'user', '2026-04-09 21:27:17');
INSERT INTO `system_log`
VALUES (230, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 32ms', 4, 'user', '2026-04-09 21:31:25');
INSERT INTO `system_log`
VALUES (231, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-09 21:31:33');
INSERT INTO `system_log`
VALUES (232, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 13ms', 4, 'user', '2026-04-09 21:31:39');
INSERT INTO `system_log`
VALUES (233, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 24ms', 1, 'admin', '2026-04-09 21:31:43');
INSERT INTO `system_log`
VALUES (234, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 30ms', 4, 'user', '2026-04-09 21:35:52');
INSERT INTO `system_log`
VALUES (235, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 21ms', 4, 'user', '2026-04-09 21:35:55');
INSERT INTO `system_log`
VALUES (236, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 9ms', 4, 'user', '2026-04-09 21:36:03');
INSERT INTO `system_log`
VALUES (237, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-09 21:36:10');
INSERT INTO `system_log`
VALUES (238, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 8ms', 4, 'user', '2026-04-09 21:36:16');
INSERT INTO `system_log`
VALUES (239, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-09 21:36:22');
INSERT INTO `system_log`
VALUES (240, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 18ms', 4, 'user', '2026-04-09 21:38:52');
INSERT INTO `system_log`
VALUES (241, 'ERROR', 'ORDER', '支付订单', '执行失败: 余额不足，请选择其他支付方式', 4, 'user', '2026-04-09 21:38:57');
INSERT INTO `system_log`
VALUES (242, 'ERROR', 'ORDER', '支付订单', '执行失败: 余额不足，请选择其他支付方式', 4, 'user', '2026-04-09 21:39:17');
INSERT INTO `system_log`
VALUES (243, 'ERROR', 'ORDER', '支付订单', '执行失败: 余额不足，请选择其他支付方式', 4, 'user', '2026-04-09 21:41:29');
INSERT INTO `system_log`
VALUES (244, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 59ms', 4, 'user', '2026-04-09 21:43:03');
INSERT INTO `system_log`
VALUES (245, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 202ms', 0, '系统/匿名', '2026-04-09 21:49:26');
INSERT INTO `system_log`
VALUES (246, 'INFO', 'CERT', '升级VIP会员', '执行成功，耗时: 13ms', 4, 'user', '2026-04-09 21:49:41');
INSERT INTO `system_log`
VALUES (247, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 28ms', 4, 'user', '2026-04-09 21:51:44');
INSERT INTO `system_log`
VALUES (248, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 19ms', 4, 'user', '2026-04-09 21:51:51');
INSERT INTO `system_log`
VALUES (249, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 9ms', 4, 'user', '2026-04-09 21:51:56');
INSERT INTO `system_log`
VALUES (250, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-09 21:53:49');
INSERT INTO `system_log`
VALUES (251, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 10ms', 4, 'user', '2026-04-09 21:53:56');
INSERT INTO `system_log`
VALUES (252, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 20ms', 1, 'admin', '2026-04-09 21:54:13');
INSERT INTO `system_log`
VALUES (253, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 21ms', 4, 'user', '2026-04-09 21:55:57');
INSERT INTO `system_log`
VALUES (254, 'ERROR', 'ORDER', '支付订单', '执行失败: 余额不足，请选择其他支付方式', 4, 'user', '2026-04-09 21:56:31');
INSERT INTO `system_log`
VALUES (255, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 19ms', 4, 'user', '2026-04-09 21:56:37');
INSERT INTO `system_log`
VALUES (256, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 10ms', 4, 'user', '2026-04-09 22:24:54');
INSERT INTO `system_log`
VALUES (257, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-09 22:25:00');
INSERT INTO `system_log`
VALUES (258, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 8ms', 4, 'user', '2026-04-09 22:25:06');
INSERT INTO `system_log`
VALUES (259, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-09 22:25:38');
INSERT INTO `system_log`
VALUES (260, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 44ms', 4, 'user', '2026-04-09 22:47:29');
INSERT INTO `system_log`
VALUES (261, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 43ms', 4, 'user', '2026-04-09 22:47:34');
INSERT INTO `system_log`
VALUES (262, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 11ms', 4, 'user', '2026-04-09 22:49:22');
INSERT INTO `system_log`
VALUES (263, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-09 22:49:29');
INSERT INTO `system_log`
VALUES (264, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 9ms', 4, 'user', '2026-04-09 22:49:35');
INSERT INTO `system_log`
VALUES (265, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 23ms', 1, 'admin', '2026-04-09 22:50:57');
INSERT INTO `system_log`
VALUES (266, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 37ms', 4, 'user', '2026-04-10 16:25:41');
INSERT INTO `system_log`
VALUES (267, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 34ms', 4, 'user', '2026-04-10 16:25:48');
INSERT INTO `system_log`
VALUES (268, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 7ms', 4, 'user', '2026-04-10 16:25:54');
INSERT INTO `system_log`
VALUES (269, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-10 16:26:32');
INSERT INTO `system_log`
VALUES (270, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 7ms', 4, 'user', '2026-04-10 16:26:50');
INSERT INTO `system_log`
VALUES (271, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-10 16:26:57');
INSERT INTO `system_log`
VALUES (272, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 30ms', 4, 'user', '2026-04-10 16:36:27');
INSERT INTO `system_log`
VALUES (273, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 26ms', 4, 'user', '2026-04-10 16:36:35');
INSERT INTO `system_log`
VALUES (274, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 40ms', 4, 'user', '2026-04-10 17:28:34');
INSERT INTO `system_log`
VALUES (275, 'ERROR', 'ORDER', '支付订单', '执行失败: 余额不足，请选择其他支付方式', 4, 'user', '2026-04-10 17:28:39');
INSERT INTO `system_log`
VALUES (276, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 32ms', 4, 'user', '2026-04-10 17:28:48');
INSERT INTO `system_log`
VALUES (277, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 11ms', 4, 'user', '2026-04-10 17:28:54');
INSERT INTO `system_log`
VALUES (278, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 33ms', 1, 'admin', '2026-04-10 17:45:04');
INSERT INTO `system_log`
VALUES (279, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 8ms', 4, 'user', '2026-04-10 17:45:22');
INSERT INTO `system_log`
VALUES (280, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 24ms', 1, 'admin', '2026-04-10 17:45:33');
INSERT INTO `system_log`
VALUES (281, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 26ms', 4, 'user', '2026-04-10 17:56:22');
INSERT INTO `system_log`
VALUES (282, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 12ms', 4, 'user', '2026-04-10 17:57:24');
INSERT INTO `system_log`
VALUES (283, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 17ms', 4, 'user', '2026-04-10 17:58:04');
INSERT INTO `system_log`
VALUES (284, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 24ms', 4, 'user', '2026-04-10 17:58:19');
INSERT INTO `system_log`
VALUES (285, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 8ms', 4, 'user', '2026-04-10 17:58:23');
INSERT INTO `system_log`
VALUES (286, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-10 17:58:33');
INSERT INTO `system_log`
VALUES (287, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 9ms', 4, 'user', '2026-04-10 17:58:39');
INSERT INTO `system_log`
VALUES (288, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 21ms', 1, 'admin', '2026-04-10 17:58:45');
INSERT INTO `system_log`
VALUES (289, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-10 18:19:54');
INSERT INTO `system_log`
VALUES (290, 'INFO', 'UPLOAD', '上传通知附件', '执行成功，耗时: 8ms', 0, '系统/匿名', '2026-04-10 19:23:21');
INSERT INTO `system_log`
VALUES (291, 'INFO', 'NOTICE', '新增公告', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-10 19:23:23');
INSERT INTO `system_log`
VALUES (292, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 23ms', 1, 'admin', '2026-04-10 19:23:32');
INSERT INTO `system_log`
VALUES (293, 'INFO', 'NOTICE', '发布公告', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-10 19:23:37');
INSERT INTO `system_log`
VALUES (294, 'INFO', 'UPLOAD', '上传通知附件', '执行成功，耗时: 5ms', 0, '系统/匿名', '2026-04-10 19:23:58');
INSERT INTO `system_log`
VALUES (295, 'INFO', 'NOTICE', '修改公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-10 19:23:59');
INSERT INTO `system_log`
VALUES (296, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-10 19:24:17');
INSERT INTO `system_log`
VALUES (297, 'INFO', 'UPLOAD', '上传通知附件', '执行成功，耗时: 6ms', 0, '系统/匿名', '2026-04-10 19:24:25');
INSERT INTO `system_log`
VALUES (298, 'INFO', 'NOTICE', '新增公告', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-10 19:24:26');
INSERT INTO `system_log`
VALUES (299, 'INFO', 'NOTICE', '发布公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-10 19:24:29');
INSERT INTO `system_log`
VALUES (300, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-10 19:27:03');
INSERT INTO `system_log`
VALUES (301, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 19ms', 4, 'user', '2026-04-10 19:39:55');
INSERT INTO `system_log`
VALUES (302, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 21ms', 4, 'user', '2026-04-10 19:39:57');
INSERT INTO `system_log`
VALUES (303, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 11ms', 4, 'user', '2026-04-10 19:40:35');
INSERT INTO `system_log`
VALUES (304, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-10 19:40:38');
INSERT INTO `system_log`
VALUES (305, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 8ms', 4, 'user', '2026-04-10 19:40:44');
INSERT INTO `system_log`
VALUES (306, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-10 19:40:49');
INSERT INTO `system_log`
VALUES (307, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 15ms', 4, 'user', '2026-04-10 19:41:07');
INSERT INTO `system_log`
VALUES (308, 'ERROR', 'ORDER', '支付订单', '执行失败: 余额不足，请选择其他支付方式', 4, 'user', '2026-04-10 19:41:11');
INSERT INTO `system_log`
VALUES (309, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 16ms', 4, 'user', '2026-04-10 19:41:17');
INSERT INTO `system_log`
VALUES (310, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 9ms', 4, 'user', '2026-04-10 19:41:21');
INSERT INTO `system_log`
VALUES (311, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-10 19:41:25');
INSERT INTO `system_log`
VALUES (312, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 6ms', 4, 'user', '2026-04-10 19:41:30');
INSERT INTO `system_log`
VALUES (313, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-10 19:42:19');
INSERT INTO `system_log`
VALUES (314, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 10ms', 4, 'user', '2026-04-10 19:45:48');
INSERT INTO `system_log`
VALUES (315, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 16ms', 4, 'user', '2026-04-10 19:46:12');
INSERT INTO `system_log`
VALUES (316, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-10 19:46:28');
INSERT INTO `system_log`
VALUES (317, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-10 20:02:53');
INSERT INTO `system_log`
VALUES (318, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-10 20:03:06');
INSERT INTO `system_log`
VALUES (319, 'INFO', 'MAINTENANCE', '新增维修记录', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-10 20:03:27');
INSERT INTO `system_log`
VALUES (320, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-10 20:03:40');
INSERT INTO `system_log`
VALUES (321, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-10 20:03:44');
INSERT INTO `system_log`
VALUES (322, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-10 20:03:53');
INSERT INTO `system_log`
VALUES (323, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-10 20:04:03');
INSERT INTO `system_log`
VALUES (324, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-10 20:26:18');
INSERT INTO `system_log`
VALUES (325, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-10 20:26:18');
INSERT INTO `system_log`
VALUES (326, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-10 20:26:18');
INSERT INTO `system_log`
VALUES (327, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-10 20:26:18');
INSERT INTO `system_log`
VALUES (328, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-10 20:26:18');
INSERT INTO `system_log`
VALUES (329, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-10 20:26:18');
INSERT INTO `system_log`
VALUES (330, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 184ms', 0, '系统/匿名', '2026-04-10 20:30:46');
INSERT INTO `system_log`
VALUES (331, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 245ms', 0, '系统/匿名', '2026-04-11 11:12:38');
INSERT INTO `system_log`
VALUES (332, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 530ms', 0, '系统/匿名', '2026-04-11 12:04:53');
INSERT INTO `system_log`
VALUES (333, 'INFO', 'USER', '修改用户', '执行成功，耗时: 38ms', 1, 'admin', '2026-04-11 15:35:58');
INSERT INTO `system_log`
VALUES (334, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 5ms', 0, '系统/匿名', '2026-04-11 15:43:57');
INSERT INTO `system_log`
VALUES (335, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 97ms', 0, '系统/匿名', '2026-04-11 15:44:05');
INSERT INTO `system_log`
VALUES (336, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 213ms', 0, '系统/匿名', '2026-04-11 15:44:21');
INSERT INTO `system_log`
VALUES (337, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 4ms', 0, '系统/匿名', '2026-04-11 15:48:13');
INSERT INTO `system_log`
VALUES (338, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 88ms', 0, '系统/匿名', '2026-04-11 15:48:28');
INSERT INTO `system_log`
VALUES (339, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 87ms', 0, '系统/匿名', '2026-04-11 15:48:38');
INSERT INTO `system_log`
VALUES (340, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 1ms', 0, '系统/匿名', '2026-04-11 15:49:18');
INSERT INTO `system_log`
VALUES (341, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 91ms', 0, '系统/匿名', '2026-04-11 15:49:37');
INSERT INTO `system_log`
VALUES (342, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 197ms', 0, '系统/匿名', '2026-04-11 15:54:39');
INSERT INTO `system_log`
VALUES (343, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 27ms', 4, 'user', '2026-04-11 16:01:20');
INSERT INTO `system_log`
VALUES (344, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 28ms', 4, 'user', '2026-04-11 16:01:26');
INSERT INTO `system_log`
VALUES (345, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 8ms', 4, 'user', '2026-04-11 16:01:33');
INSERT INTO `system_log`
VALUES (346, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-11 16:02:06');
INSERT INTO `system_log`
VALUES (347, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 8ms', 4, 'user', '2026-04-11 16:02:20');
INSERT INTO `system_log`
VALUES (348, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 20ms', 1, 'admin', '2026-04-11 16:02:35');
INSERT INTO `system_log`
VALUES (349, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 184ms', 0, '系统/匿名', '2026-04-11 20:40:12');
INSERT INTO `system_log`
VALUES (350, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 28ms', 4, 'user', '2026-04-11 20:41:05');
INSERT INTO `system_log`
VALUES (351, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 21ms', 4, 'user', '2026-04-11 20:41:12');
INSERT INTO `system_log`
VALUES (352, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 17ms', 4, 'user', '2026-04-11 20:41:17');
INSERT INTO `system_log`
VALUES (353, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-11 20:41:24');
INSERT INTO `system_log`
VALUES (354, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 16ms', 4, 'user', '2026-04-11 20:41:57');
INSERT INTO `system_log`
VALUES (355, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-11 20:42:03');
INSERT INTO `system_log`
VALUES (356, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 42ms', 4, 'user', '2026-04-11 21:11:41');
INSERT INTO `system_log`
VALUES (357, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 33ms', 4, 'user', '2026-04-11 21:11:46');
INSERT INTO `system_log`
VALUES (358, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 15ms', 4, 'user', '2026-04-11 21:11:50');
INSERT INTO `system_log`
VALUES (359, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-11 21:11:59');
INSERT INTO `system_log`
VALUES (360, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 15ms', 4, 'user', '2026-04-11 21:12:44');
INSERT INTO `system_log`
VALUES (361, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 19ms', 1, 'admin', '2026-04-11 21:12:48');
INSERT INTO `system_log`
VALUES (362, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 31ms', 1, 'admin', '2026-04-11 21:47:47');
INSERT INTO `system_log`
VALUES (363, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 209ms', 0, '系统/匿名', '2026-04-11 22:27:11');
INSERT INTO `system_log`
VALUES (364, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 21ms', 4, 'user', '2026-04-11 22:27:56');
INSERT INTO `system_log`
VALUES (365, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 20ms', 4, 'user', '2026-04-11 22:28:03');
INSERT INTO `system_log`
VALUES (366, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 9ms', 4, 'user', '2026-04-11 22:28:10');
INSERT INTO `system_log`
VALUES (367, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-11 22:28:22');
INSERT INTO `system_log`
VALUES (368, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 9ms', 4, 'user', '2026-04-11 22:28:33');
INSERT INTO `system_log`
VALUES (369, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 19ms', 1, 'admin', '2026-04-11 22:28:41');
INSERT INTO `system_log`
VALUES (370, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 241ms', 0, '系统/匿名', '2026-04-14 15:22:20');
INSERT INTO `system_log`
VALUES (371, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 37ms', 4, 'user', '2026-04-14 15:23:25');
INSERT INTO `system_log`
VALUES (372, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 34ms', 4, 'user', '2026-04-14 15:23:38');
INSERT INTO `system_log`
VALUES (373, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 19ms', 4, 'user', '2026-04-14 15:23:45');
INSERT INTO `system_log`
VALUES (374, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 94ms', 0, '系统/匿名', '2026-04-14 15:24:19');
INSERT INTO `system_log`
VALUES (375, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 95ms', 0, '系统/匿名', '2026-04-14 15:24:42');
INSERT INTO `system_log`
VALUES (376, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-14 15:25:16');
INSERT INTO `system_log`
VALUES (377, 'INFO', 'USER', '修改用户', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-14 15:27:47');
INSERT INTO `system_log`
VALUES (378, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 90ms', 0, '系统/匿名', '2026-04-14 15:28:24');
INSERT INTO `system_log`
VALUES (379, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-14 15:31:51');
INSERT INTO `system_log`
VALUES (380, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-14 15:31:51');
INSERT INTO `system_log`
VALUES (381, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-14 15:31:51');
INSERT INTO `system_log`
VALUES (382, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-14 15:31:51');
INSERT INTO `system_log`
VALUES (383, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-14 15:32:13');
INSERT INTO `system_log`
VALUES (384, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-14 15:32:28');
INSERT INTO `system_log`
VALUES (385, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-14 15:32:28');
INSERT INTO `system_log`
VALUES (386, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-14 15:32:28');
INSERT INTO `system_log`
VALUES (387, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-14 15:32:28');
INSERT INTO `system_log`
VALUES (388, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-14 15:32:28');
INSERT INTO `system_log`
VALUES (389, 'INFO', 'CERT', '升级VIP会员', '执行成功，耗时: 33ms', 4, 'user', '2026-04-15 10:32:48');
INSERT INTO `system_log`
VALUES (390, 'ERROR', 'CERT', '升级VIP会员', '执行失败: 您已经是VIP会员', 4, 'user', '2026-04-15 10:36:36');
INSERT INTO `system_log`
VALUES (391, 'ERROR', 'CERT', '升级VIP会员', '执行失败: 您已经是VIP会员', 4, 'user', '2026-04-15 10:42:59');
INSERT INTO `system_log`
VALUES (392, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 202ms', 0, '系统/匿名', '2026-04-15 10:43:18');
INSERT INTO `system_log`
VALUES (393, 'INFO', 'USER', '修改用户', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-15 10:43:33');
INSERT INTO `system_log`
VALUES (394, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 91ms', 0, '系统/匿名', '2026-04-15 10:43:48');
INSERT INTO `system_log`
VALUES (395, 'INFO', 'CERT', '升级VIP会员', '执行成功，耗时: 9ms', 4, 'user', '2026-04-15 10:44:00');
INSERT INTO `system_log`
VALUES (396, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 96ms', 0, '系统/匿名', '2026-04-15 10:44:35');
INSERT INTO `system_log`
VALUES (397, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-15 14:49:29');
INSERT INTO `system_log`
VALUES (398, 'ERROR', 'CATEGORY', '新增分类', '执行失败: 分类名称已存在', 1, 'admin', '2026-04-15 14:49:43');
INSERT INTO `system_log`
VALUES (399, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-15 14:50:07');
INSERT INTO `system_log`
VALUES (400, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-15 14:50:13');
INSERT INTO `system_log`
VALUES (401, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-15 14:50:18');
INSERT INTO `system_log`
VALUES (402, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-15 14:50:50');
INSERT INTO `system_log`
VALUES (403, 'INFO', 'VEHICLE', '新增车辆型号', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-15 14:52:49');
INSERT INTO `system_log`
VALUES (404, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-15 14:53:01');
INSERT INTO `system_log`
VALUES (405, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-15 14:53:30');
INSERT INTO `system_log`
VALUES (406, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-15 14:57:56');
INSERT INTO `system_log`
VALUES (407, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-15 14:57:56');
INSERT INTO `system_log`
VALUES (408, 'INFO', 'CATEGORY', '新增分类', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-15 14:58:47');
INSERT INTO `system_log`
VALUES (409, 'WARN', 'CATEGORY', '删除分类', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-15 14:59:05');
INSERT INTO `system_log`
VALUES (410, 'WARN', 'CATEGORY', '删除分类', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-15 14:59:12');
INSERT INTO `system_log`
VALUES (411, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 194ms', 0, '系统/匿名', '2026-04-15 16:31:08');
INSERT INTO `system_log`
VALUES (412, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 87ms', 0, '系统/匿名', '2026-04-15 17:07:29');
INSERT INTO `system_log`
VALUES (413, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 208ms', 0, '系统/匿名', '2026-04-16 16:33:07');
INSERT INTO `system_log`
VALUES (414, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 181ms', 0, '系统/匿名', '2026-04-16 17:09:40');
INSERT INTO `system_log`
VALUES (415, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 43ms', 1, 'admin', '2026-04-16 21:11:17');
INSERT INTO `system_log`
VALUES (416, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 35ms', 1, 'admin', '2026-04-16 21:12:16');
INSERT INTO `system_log`
VALUES (417, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 22ms', 1, 'admin', '2026-04-16 21:12:22');
INSERT INTO `system_log`
VALUES (418, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-16 21:12:27');
INSERT INTO `system_log`
VALUES (419, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-16 21:12:36');
INSERT INTO `system_log`
VALUES (420, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-16 21:12:49');
INSERT INTO `system_log`
VALUES (421, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-16 21:12:56');
INSERT INTO `system_log`
VALUES (422, 'INFO', 'UPLOAD', '上传图片(RImg)', '执行成功，耗时: 0ms', 1, 'admin', '2026-04-16 21:13:23');
INSERT INTO `system_log`
VALUES (423, 'INFO', 'REVIEW', '提交评价', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-16 21:13:25');
INSERT INTO `system_log`
VALUES (424, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 196ms', 0, '系统/匿名', '2026-04-16 22:45:23');
INSERT INTO `system_log`
VALUES (425, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 210ms', 0, '系统/匿名', '2026-04-17 22:24:20');
INSERT INTO `system_log`
VALUES (426, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 175ms', 0, '系统/匿名', '2026-04-17 22:45:44');
INSERT INTO `system_log`
VALUES (427, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 330ms', 0, '系统/匿名', '2026-04-22 14:26:02');
INSERT INTO `system_log`
VALUES (428, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 32ms', 1, 'admin', '2026-04-22 14:54:01');
INSERT INTO `system_log`
VALUES (429, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 20ms', 1, 'admin', '2026-04-22 14:54:06');
INSERT INTO `system_log`
VALUES (430, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 20ms', 1, 'admin', '2026-04-22 15:29:47');
INSERT INTO `system_log`
VALUES (431, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 15:30:00');
INSERT INTO `system_log`
VALUES (432, 'ERROR', 'VEHICLE', '新增车辆', '执行失败: 车牌号已存在', 1, 'admin', '2026-04-22 15:30:59');
INSERT INTO `system_log`
VALUES (433, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 15:31:06');
INSERT INTO `system_log`
VALUES (434, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-22 15:31:19');
INSERT INTO `system_log`
VALUES (435, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 15:31:19');
INSERT INTO `system_log`
VALUES (436, 'INFO', 'VEHICLE', '新增车辆型号', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-22 15:46:25');
INSERT INTO `system_log`
VALUES (437, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 15:46:44');
INSERT INTO `system_log`
VALUES (438, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 15:49:22');
INSERT INTO `system_log`
VALUES (439, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 15:49:28');
INSERT INTO `system_log`
VALUES (440, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-22 15:56:57');
INSERT INTO `system_log`
VALUES (441, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 26ms', 1, 'admin', '2026-04-22 15:58:48');
INSERT INTO `system_log`
VALUES (442, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-22 16:01:16');
INSERT INTO `system_log`
VALUES (443, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-22 16:01:34');
INSERT INTO `system_log`
VALUES (444, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 16:02:01');
INSERT INTO `system_log`
VALUES (445, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-22 16:02:19');
INSERT INTO `system_log`
VALUES (446, 'INFO', 'UPLOAD', '上传Logo', '执行成功，耗时: 8ms', 0, '系统/匿名', '2026-04-22 16:03:07');
INSERT INTO `system_log`
VALUES (447, 'INFO', 'UPLOAD', '上传Logo', '执行成功，耗时: 7ms', 0, '系统/匿名', '2026-04-22 16:03:31');
INSERT INTO `system_log`
VALUES (448, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-22 16:54:36');
INSERT INTO `system_log`
VALUES (449, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 16:54:36');
INSERT INTO `system_log`
VALUES (450, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 16:54:36');
INSERT INTO `system_log`
VALUES (451, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 16:54:36');
INSERT INTO `system_log`
VALUES (452, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 26ms', 1, 'admin', '2026-04-22 16:54:36');
INSERT INTO `system_log`
VALUES (453, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 16:54:50');
INSERT INTO `system_log`
VALUES (454, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 16:54:51');
INSERT INTO `system_log`
VALUES (455, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 16:54:51');
INSERT INTO `system_log`
VALUES (456, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 16:56:29');
INSERT INTO `system_log`
VALUES (457, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 16:56:29');
INSERT INTO `system_log`
VALUES (458, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 16:56:29');
INSERT INTO `system_log`
VALUES (459, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 30ms', 1, 'admin', '2026-04-22 17:13:26');
INSERT INTO `system_log`
VALUES (460, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-22 17:13:46');
INSERT INTO `system_log`
VALUES (461, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:13:46');
INSERT INTO `system_log`
VALUES (462, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-22 17:13:46');
INSERT INTO `system_log`
VALUES (463, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-22 17:13:52');
INSERT INTO `system_log`
VALUES (464, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:14:00');
INSERT INTO `system_log`
VALUES (465, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-22 17:14:00');
INSERT INTO `system_log`
VALUES (466, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:14:13');
INSERT INTO `system_log`
VALUES (467, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:14:13');
INSERT INTO `system_log`
VALUES (468, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:14:13');
INSERT INTO `system_log`
VALUES (469, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:14:13');
INSERT INTO `system_log`
VALUES (470, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-22 17:14:13');
INSERT INTO `system_log`
VALUES (471, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-22 17:43:57');
INSERT INTO `system_log`
VALUES (472, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:43:57');
INSERT INTO `system_log`
VALUES (473, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 26ms', 1, 'admin', '2026-04-22 17:43:57');
INSERT INTO `system_log`
VALUES (474, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-22 17:51:48');
INSERT INTO `system_log`
VALUES (475, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 17:51:48');
INSERT INTO `system_log`
VALUES (476, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:52:13');
INSERT INTO `system_log`
VALUES (477, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-22 17:52:13');
INSERT INTO `system_log`
VALUES (478, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:52:27');
INSERT INTO `system_log`
VALUES (479, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 17:52:27');
INSERT INTO `system_log`
VALUES (480, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:52:40');
INSERT INTO `system_log`
VALUES (481, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:52:40');
INSERT INTO `system_log`
VALUES (482, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:52:40');
INSERT INTO `system_log`
VALUES (483, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 17:52:40');
INSERT INTO `system_log`
VALUES (484, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:52:52');
INSERT INTO `system_log`
VALUES (485, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:52:52');
INSERT INTO `system_log`
VALUES (486, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:52:52');
INSERT INTO `system_log`
VALUES (487, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-04-22 17:52:52');
INSERT INTO `system_log`
VALUES (488, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:53:45');
INSERT INTO `system_log`
VALUES (489, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:53:45');
INSERT INTO `system_log`
VALUES (490, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:53:45');
INSERT INTO `system_log`
VALUES (491, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:53:45');
INSERT INTO `system_log`
VALUES (492, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:53:45');
INSERT INTO `system_log`
VALUES (493, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 17:53:45');
INSERT INTO `system_log`
VALUES (494, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:53:55');
INSERT INTO `system_log`
VALUES (495, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:53:55');
INSERT INTO `system_log`
VALUES (496, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:53:55');
INSERT INTO `system_log`
VALUES (497, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 17:53:55');
INSERT INTO `system_log`
VALUES (498, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:54:05');
INSERT INTO `system_log`
VALUES (499, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:05');
INSERT INTO `system_log`
VALUES (500, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-22 17:54:05');
INSERT INTO `system_log`
VALUES (501, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 17:54:05');
INSERT INTO `system_log`
VALUES (502, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:17');
INSERT INTO `system_log`
VALUES (503, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:54:17');
INSERT INTO `system_log`
VALUES (504, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-22 17:54:17');
INSERT INTO `system_log`
VALUES (505, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:54:23');
INSERT INTO `system_log`
VALUES (506, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:23');
INSERT INTO `system_log`
VALUES (507, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:54:23');
INSERT INTO `system_log`
VALUES (508, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:54:29');
INSERT INTO `system_log`
VALUES (509, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:29');
INSERT INTO `system_log`
VALUES (510, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:29');
INSERT INTO `system_log`
VALUES (511, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-22 17:54:29');
INSERT INTO `system_log`
VALUES (512, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:54:40');
INSERT INTO `system_log`
VALUES (513, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:54:40');
INSERT INTO `system_log`
VALUES (514, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:40');
INSERT INTO `system_log`
VALUES (515, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 17:54:40');
INSERT INTO `system_log`
VALUES (516, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:56');
INSERT INTO `system_log`
VALUES (517, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:56');
INSERT INTO `system_log`
VALUES (518, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:56');
INSERT INTO `system_log`
VALUES (519, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:54:56');
INSERT INTO `system_log`
VALUES (520, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 17:54:56');
INSERT INTO `system_log`
VALUES (521, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:10');
INSERT INTO `system_log`
VALUES (522, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:10');
INSERT INTO `system_log`
VALUES (523, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:10');
INSERT INTO `system_log`
VALUES (524, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:55:10');
INSERT INTO `system_log`
VALUES (525, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 17:55:10');
INSERT INTO `system_log`
VALUES (526, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:55:27');
INSERT INTO `system_log`
VALUES (527, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:27');
INSERT INTO `system_log`
VALUES (528, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:27');
INSERT INTO `system_log`
VALUES (529, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 17:55:27');
INSERT INTO `system_log`
VALUES (530, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:39');
INSERT INTO `system_log`
VALUES (531, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:55:39');
INSERT INTO `system_log`
VALUES (532, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:55:39');
INSERT INTO `system_log`
VALUES (533, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 17:55:39');
INSERT INTO `system_log`
VALUES (534, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:52');
INSERT INTO `system_log`
VALUES (535, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:52');
INSERT INTO `system_log`
VALUES (536, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:55:52');
INSERT INTO `system_log`
VALUES (537, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:55:52');
INSERT INTO `system_log`
VALUES (538, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 17:55:52');
INSERT INTO `system_log`
VALUES (539, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 4ms', 1, 'admin', '2026-04-22 17:56:06');
INSERT INTO `system_log`
VALUES (540, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:56:06');
INSERT INTO `system_log`
VALUES (541, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:56:06');
INSERT INTO `system_log`
VALUES (542, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 17:56:06');
INSERT INTO `system_log`
VALUES (543, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:56:37');
INSERT INTO `system_log`
VALUES (544, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:56:37');
INSERT INTO `system_log`
VALUES (545, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:56:37');
INSERT INTO `system_log`
VALUES (546, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:56:37');
INSERT INTO `system_log`
VALUES (547, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 17:56:37');
INSERT INTO `system_log`
VALUES (548, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:56:51');
INSERT INTO `system_log`
VALUES (549, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:56:51');
INSERT INTO `system_log`
VALUES (550, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:56:51');
INSERT INTO `system_log`
VALUES (551, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:56:51');
INSERT INTO `system_log`
VALUES (552, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 17:56:51');
INSERT INTO `system_log`
VALUES (553, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:57:00');
INSERT INTO `system_log`
VALUES (554, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:57:00');
INSERT INTO `system_log`
VALUES (555, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:57:00');
INSERT INTO `system_log`
VALUES (556, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 17:57:00');
INSERT INTO `system_log`
VALUES (557, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:57:15');
INSERT INTO `system_log`
VALUES (558, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:57:15');
INSERT INTO `system_log`
VALUES (559, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:57:15');
INSERT INTO `system_log`
VALUES (560, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-22 17:57:15');
INSERT INTO `system_log`
VALUES (561, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:57:28');
INSERT INTO `system_log`
VALUES (562, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:57:28');
INSERT INTO `system_log`
VALUES (563, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 17:57:28');
INSERT INTO `system_log`
VALUES (564, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:57:28');
INSERT INTO `system_log`
VALUES (565, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 17:57:28');
INSERT INTO `system_log`
VALUES (566, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:57:40');
INSERT INTO `system_log`
VALUES (567, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 17:57:40');
INSERT INTO `system_log`
VALUES (568, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 17:57:40');
INSERT INTO `system_log`
VALUES (569, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 17:57:40');
INSERT INTO `system_log`
VALUES (570, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 18:09:01');
INSERT INTO `system_log`
VALUES (571, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 18:09:01');
INSERT INTO `system_log`
VALUES (572, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 18:09:10');
INSERT INTO `system_log`
VALUES (573, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 18:09:10');
INSERT INTO `system_log`
VALUES (574, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:09:18');
INSERT INTO `system_log`
VALUES (575, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-22 18:09:18');
INSERT INTO `system_log`
VALUES (576, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:09:28');
INSERT INTO `system_log`
VALUES (577, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 18:09:28');
INSERT INTO `system_log`
VALUES (578, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:09:37');
INSERT INTO `system_log`
VALUES (579, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 18:09:37');
INSERT INTO `system_log`
VALUES (580, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:09:45');
INSERT INTO `system_log`
VALUES (581, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-22 18:09:45');
INSERT INTO `system_log`
VALUES (582, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:09:54');
INSERT INTO `system_log`
VALUES (583, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 18:09:54');
INSERT INTO `system_log`
VALUES (584, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:10:30');
INSERT INTO `system_log`
VALUES (585, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 18:10:30');
INSERT INTO `system_log`
VALUES (586, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 18:10:30');
INSERT INTO `system_log`
VALUES (587, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:10:39');
INSERT INTO `system_log`
VALUES (588, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 27ms', 1, 'admin', '2026-04-22 18:10:39');
INSERT INTO `system_log`
VALUES (589, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:10:53');
INSERT INTO `system_log`
VALUES (590, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 18:10:53');
INSERT INTO `system_log`
VALUES (591, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 18:10:53');
INSERT INTO `system_log`
VALUES (592, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 18:11:05');
INSERT INTO `system_log`
VALUES (593, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 27ms', 1, 'admin', '2026-04-22 18:11:05');
INSERT INTO `system_log`
VALUES (594, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:11:16');
INSERT INTO `system_log`
VALUES (595, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 18:11:16');
INSERT INTO `system_log`
VALUES (596, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:11:28');
INSERT INTO `system_log`
VALUES (597, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 18:11:28');
INSERT INTO `system_log`
VALUES (598, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:11:54');
INSERT INTO `system_log`
VALUES (599, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-22 18:11:54');
INSERT INTO `system_log`
VALUES (600, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:12:03');
INSERT INTO `system_log`
VALUES (601, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-22 18:12:03');
INSERT INTO `system_log`
VALUES (602, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:12:12');
INSERT INTO `system_log`
VALUES (603, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 18:12:12');
INSERT INTO `system_log`
VALUES (604, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 18:12:22');
INSERT INTO `system_log`
VALUES (605, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 18:12:22');
INSERT INTO `system_log`
VALUES (606, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-22 18:12:22');
INSERT INTO `system_log`
VALUES (607, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:12:30');
INSERT INTO `system_log`
VALUES (608, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 26ms', 1, 'admin', '2026-04-22 18:12:30');
INSERT INTO `system_log`
VALUES (609, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:12:40');
INSERT INTO `system_log`
VALUES (610, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 18:12:40');
INSERT INTO `system_log`
VALUES (611, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:12:51');
INSERT INTO `system_log`
VALUES (612, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-22 18:12:51');
INSERT INTO `system_log`
VALUES (613, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:13:02');
INSERT INTO `system_log`
VALUES (614, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-22 18:13:02');
INSERT INTO `system_log`
VALUES (615, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:13:12');
INSERT INTO `system_log`
VALUES (616, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-22 18:13:12');
INSERT INTO `system_log`
VALUES (617, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-22 18:13:23');
INSERT INTO `system_log`
VALUES (618, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 1ms', 1, 'admin', '2026-04-22 18:13:23');
INSERT INTO `system_log`
VALUES (619, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 3ms', 1, 'admin', '2026-04-22 18:13:23');
INSERT INTO `system_log`
VALUES (620, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-22 18:13:23');
INSERT INTO `system_log`
VALUES (621, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 328ms', 0, '系统/匿名', '2026-04-26 18:19:15');
INSERT INTO `system_log`
VALUES (622, 'INFO', 'USER', '修改用户', '执行成功，耗时: 19ms', 1, 'admin', '2026-04-26 18:33:52');
INSERT INTO `system_log`
VALUES (623, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 24ms', 1, 'admin', '2026-04-26 18:39:03');
INSERT INTO `system_log`
VALUES (624, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 26ms', 1, 'admin', '2026-04-26 18:39:08');
INSERT INTO `system_log`
VALUES (625, 'INFO', 'ORDER', '申请取车', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-26 18:39:12');
INSERT INTO `system_log`
VALUES (626, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 173ms', 0, '系统/匿名', '2026-04-26 18:55:48');
INSERT INTO `system_log`
VALUES (627, 'INFO', 'AUTH', '用户注册', '执行成功，耗时: 103ms', 0, '系统/匿名', '2026-04-26 18:56:16');
INSERT INTO `system_log`
VALUES (628, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 88ms', 0, '系统/匿名', '2026-04-26 18:56:37');
INSERT INTO `system_log`
VALUES (629, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 108ms', 0, '系统/匿名', '2026-04-26 18:57:50');
INSERT INTO `system_log`
VALUES (630, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-26 22:21:39');
INSERT INTO `system_log`
VALUES (631, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-26 22:21:44');
INSERT INTO `system_log`
VALUES (632, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-26 22:22:16');
INSERT INTO `system_log`
VALUES (633, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-26 22:22:28');
INSERT INTO `system_log`
VALUES (634, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-26 22:22:36');
INSERT INTO `system_log`
VALUES (635, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-26 22:22:56');
INSERT INTO `system_log`
VALUES (636, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-04-26 22:23:19');
INSERT INTO `system_log`
VALUES (637, 'WARN', 'ROLE', '删除角色权益', '执行成功，耗时: 21ms', 1, 'admin', '2026-04-27 11:38:11');
INSERT INTO `system_log`
VALUES (638, 'WARN', 'ROLE', '删除角色权益', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-27 11:38:13');
INSERT INTO `system_log`
VALUES (639, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 8ms', 4, 'user', '2026-04-27 12:50:24');
INSERT INTO `system_log`
VALUES (640, 'ERROR', 'CERT', '申请学生认证', '执行失败: 已有进行中的学生认证申请或已认证通过', 4, 'user',
        '2026-04-27 12:50:25');
INSERT INTO `system_log`
VALUES (641, 'INFO', 'UPLOAD', '上传图片(image)', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-27 12:55:29');
INSERT INTO `system_log`
VALUES (642, 'INFO', 'USER', '新增用户', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-27 12:55:30');
INSERT INTO `system_log`
VALUES (643, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 187ms', 0, '系统/匿名', '2026-04-27 14:04:04');
INSERT INTO `system_log`
VALUES (644, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 11ms', 4, 'user', '2026-04-27 14:04:13');
INSERT INTO `system_log`
VALUES (645, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-27 14:04:22');
INSERT INTO `system_log`
VALUES (646, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-27 14:07:41');
INSERT INTO `system_log`
VALUES (647, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-27 14:07:43');
INSERT INTO `system_log`
VALUES (648, 'INFO', 'UPLOAD', '上传图片(image)', '执行成功，耗时: 5ms', 1, 'admin', '2026-04-27 14:08:09');
INSERT INTO `system_log`
VALUES (649, 'INFO', 'USER', '新增用户', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-27 14:08:10');
INSERT INTO `system_log`
VALUES (650, 'INFO', 'UPLOAD', '上传图片(image)', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-27 14:09:41');
INSERT INTO `system_log`
VALUES (651, 'ERROR', 'USER', '新增用户', '执行失败: 已经是学生了', 1, 'admin', '2026-04-27 14:09:42');
INSERT INTO `system_log`
VALUES (652, 'INFO', 'UPLOAD', '上传图片(image)', '执行成功，耗时: 6ms', 1, 'admin', '2026-04-27 14:15:23');
INSERT INTO `system_log`
VALUES (653, 'INFO', 'USER', '新增用户', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-27 14:15:24');
INSERT INTO `system_log`
VALUES (654, 'INFO', 'UPLOAD', '上传图片(image)', '执行成功，耗时: 2ms', 1, 'admin', '2026-04-27 14:15:52');
INSERT INTO `system_log`
VALUES (655, 'INFO', 'USER', '新增用户', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-27 14:15:53');
INSERT INTO `system_log`
VALUES (656, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 14ms', 1, 'admin', '2026-04-27 14:25:38');
INSERT INTO `system_log`
VALUES (657, 'ERROR', 'ORDER', '提交订单', '执行失败: 您当前已有 1 个进行中的订单，已达上限，请完成后再下单', 1, 'admin',
        '2026-04-27 15:46:25');
INSERT INTO `system_log`
VALUES (658, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 19ms', 1, 'admin', '2026-04-27 15:46:42');
INSERT INTO `system_log`
VALUES (659, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 19ms', 1, 'admin', '2026-04-27 15:46:52');
INSERT INTO `system_log`
VALUES (660, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-27 15:47:03');
INSERT INTO `system_log`
VALUES (661, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 12ms', 1, 'admin', '2026-04-27 15:47:07');
INSERT INTO `system_log`
VALUES (662, 'INFO', 'REVIEW', '点赞评价', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-27 15:49:11');
INSERT INTO `system_log`
VALUES (663, 'INFO', 'REVIEW', '修改评价状态', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-27 16:29:51');
INSERT INTO `system_log`
VALUES (664, 'INFO', 'REVIEW', '修改评价状态', '执行成功，耗时: 7ms', 1, 'admin', '2026-04-27 16:29:52');
INSERT INTO `system_log`
VALUES (665, 'ERROR', 'ORDER', '提交订单', '执行失败: 您当前已有 1 个进行中的订单，已达上限，请完成后再下单', 1, 'admin',
        '2026-04-27 16:42:06');
INSERT INTO `system_log`
VALUES (666, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-27 16:49:04');
INSERT INTO `system_log`
VALUES (667, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-27 16:49:29');
INSERT INTO `system_log`
VALUES (668, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 17ms', 1, 'admin', '2026-04-27 16:49:32');
INSERT INTO `system_log`
VALUES (669, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 9ms', 1, 'admin', '2026-04-27 16:49:57');
INSERT INTO `system_log`
VALUES (670, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-27 16:51:02');
INSERT INTO `system_log`
VALUES (671, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-27 17:40:03');
INSERT INTO `system_log`
VALUES (672, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 21ms', 1, 'admin', '2026-04-27 18:04:34');
INSERT INTO `system_log`
VALUES (673, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 21ms', 1, 'admin', '2026-04-27 18:08:54');
INSERT INTO `system_log`
VALUES (674, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 22ms', 1, 'admin', '2026-04-27 18:08:58');
INSERT INTO `system_log`
VALUES (675, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-27 18:16:35');
INSERT INTO `system_log`
VALUES (676, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 18ms', 1, 'admin', '2026-04-27 18:16:38');
INSERT INTO `system_log`
VALUES (677, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 16ms', 1, 'admin', '2026-04-27 18:18:27');
INSERT INTO `system_log`
VALUES (678, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 13ms', 1, 'admin', '2026-04-27 18:18:30');
INSERT INTO `system_log`
VALUES (679, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 199ms', 0, '系统/匿名', '2026-04-27 18:23:03');
INSERT INTO `system_log`
VALUES (680, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 27ms', 1, 'admin', '2026-04-28 12:19:57');
INSERT INTO `system_log`
VALUES (681, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 11ms', 1, 'admin', '2026-04-28 12:20:11');
INSERT INTO `system_log`
VALUES (682, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 15ms', 1, 'admin', '2026-04-28 12:20:25');
INSERT INTO `system_log`
VALUES (683, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 246ms', 0, '系统/匿名', '2026-04-30 21:43:46');
INSERT INTO `system_log`
VALUES (684, 'INFO', 'UPLOAD', '上传图片(image)', '执行成功，耗时: 0ms', 1, 'admin', '2026-04-30 21:50:08');
INSERT INTO `system_log`
VALUES (685, 'INFO', 'USER', '新增用户', '执行成功，耗时: 21ms', 1, 'admin', '2026-04-30 21:50:08');
INSERT INTO `system_log`
VALUES (686, 'INFO', 'NOTICE', '新增公告', '执行成功，耗时: 18ms', 1, 'admin', '2026-04-30 21:51:17');
INSERT INTO `system_log`
VALUES (687, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 88ms', 0, '系统/匿名', '2026-04-30 22:01:37');
INSERT INTO `system_log`
VALUES (688, 'INFO', 'AUTH', '用户注册', '执行成功，耗时: 139ms', 0, '系统/匿名', '2026-05-01 20:58:23');
INSERT INTO `system_log`
VALUES (689, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 15ms', 0, '系统/匿名', '2026-05-02 16:33:16');
INSERT INTO `system_log`
VALUES (690, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 305ms', 0, '系统/匿名', '2026-05-02 17:07:09');
INSERT INTO `system_log`
VALUES (691, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 34ms', 4, 'user', '2026-05-02 17:08:05');
INSERT INTO `system_log`
VALUES (692, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 25ms', 4, 'user', '2026-05-02 17:09:14');
INSERT INTO `system_log`
VALUES (693, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 410ms', 0, '系统/匿名', '2026-05-02 23:06:48');
INSERT INTO `system_log`
VALUES (694, 'INFO', 'AUTH', '用户注册', '执行成功，耗时: 144ms', 0, '系统/匿名', '2026-05-20 15:20:17');
INSERT INTO `system_log`
VALUES (695, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 5ms', 0, '系统/匿名', '2026-05-20 15:20:49');
INSERT INTO `system_log`
VALUES (696, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 127ms', 0, '系统/匿名', '2026-05-20 15:20:57');
INSERT INTO `system_log`
VALUES (697, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 131ms', 0, '系统/匿名', '2026-05-20 15:21:19');
INSERT INTO `system_log`
VALUES (698, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 385ms', 0, '系统/匿名', '2026-05-20 15:24:11');
INSERT INTO `system_log`
VALUES (699, 'WARN', 'USER', '删除用户', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-20 15:24:52');
INSERT INTO `system_log`
VALUES (700, 'INFO', 'USER', '修改用户', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-20 15:27:25');
INSERT INTO `system_log`
VALUES (701, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 17ms', 1, 'admin', '2026-05-20 15:43:21');
INSERT INTO `system_log`
VALUES (702, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-20 15:43:25');
INSERT INTO `system_log`
VALUES (703, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-20 15:43:29');
INSERT INTO `system_log`
VALUES (704, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-20 15:43:34');
INSERT INTO `system_log`
VALUES (705, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 180ms', 0, '系统/匿名', '2026-05-20 17:29:48');
INSERT INTO `system_log`
VALUES (706, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 5ms', 22, '用户2026-05-20', '2026-05-20 17:30:30');
INSERT INTO `system_log`
VALUES (707, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 9ms', 22, '用户2026-05-20', '2026-05-20 17:30:35');
INSERT INTO `system_log`
VALUES (708, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 6ms', 22, '用户2026-05-20', '2026-05-20 17:44:45');
INSERT INTO `system_log`
VALUES (709, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 170ms', 0, '系统/匿名', '2026-05-20 17:45:08');
INSERT INTO `system_log`
VALUES (710, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 81ms', 0, '系统/匿名', '2026-05-20 17:45:41');
INSERT INTO `system_log`
VALUES (711, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 92ms', 0, '系统/匿名', '2026-05-20 17:45:58');
INSERT INTO `system_log`
VALUES (712, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 95ms', 0, '系统/匿名', '2026-05-20 17:47:51');
INSERT INTO `system_log`
VALUES (713, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 6ms', 22, '用户2026-05-20', '2026-05-20 17:48:01');
INSERT INTO `system_log`
VALUES (714, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 98ms', 0, '系统/匿名', '2026-05-20 17:51:29');
INSERT INTO `system_log`
VALUES (715, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 19ms', 22, 'alice', '2026-05-20 18:10:35');
INSERT INTO `system_log`
VALUES (716, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 205ms', 0, '系统/匿名', '2026-05-20 18:11:25');
INSERT INTO `system_log`
VALUES (717, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 257ms', 0, '系统/匿名', '2026-05-22 19:35:06');
INSERT INTO `system_log`
VALUES (718, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 25ms', 1, 'admin', '2026-05-22 20:40:32');
INSERT INTO `system_log`
VALUES (719, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-22 20:40:45');
INSERT INTO `system_log`
VALUES (720, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-22 20:40:58');
INSERT INTO `system_log`
VALUES (721, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-22 20:41:12');
INSERT INTO `system_log`
VALUES (722, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 33ms', 1, 'admin', '2026-05-22 20:41:17');
INSERT INTO `system_log`
VALUES (723, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 16ms', 1, 'admin', '2026-05-22 20:42:49');
INSERT INTO `system_log`
VALUES (724, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 18ms', 1, 'admin', '2026-05-22 21:17:54');
INSERT INTO `system_log`
VALUES (725, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 21ms', 1, 'admin', '2026-05-22 21:29:06');
INSERT INTO `system_log`
VALUES (726, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 14ms', 1, 'admin', '2026-05-22 21:29:12');
INSERT INTO `system_log`
VALUES (727, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 19ms', 1, 'admin', '2026-05-22 21:29:17');
INSERT INTO `system_log`
VALUES (728, 'INFO', 'SYSTEM', '保存系统配置', '执行成功，耗时: 15ms', 1, 'admin', '2026-05-22 21:29:28');
INSERT INTO `system_log`
VALUES (729, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-22 22:48:22');
INSERT INTO `system_log`
VALUES (730, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-22 22:48:27');
INSERT INTO `system_log`
VALUES (731, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-22 22:48:33');
INSERT INTO `system_log`
VALUES (732, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-22 22:48:38');
INSERT INTO `system_log`
VALUES (733, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-22 22:48:41');
INSERT INTO `system_log`
VALUES (734, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 3ms', 1, 'admin', '2026-05-22 22:48:45');
INSERT INTO `system_log`
VALUES (735, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-22 22:48:50');
INSERT INTO `system_log`
VALUES (736, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-22 22:49:04');
INSERT INTO `system_log`
VALUES (737, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-22 22:49:33');
INSERT INTO `system_log`
VALUES (738, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-22 22:49:37');
INSERT INTO `system_log`
VALUES (739, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-22 22:49:41');
INSERT INTO `system_log`
VALUES (740, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-22 22:49:45');
INSERT INTO `system_log`
VALUES (741, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-22 22:49:51');
INSERT INTO `system_log`
VALUES (742, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 19ms', 1, 'admin', '2026-05-23 12:35:20');
INSERT INTO `system_log`
VALUES (743, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 12:35:28');
INSERT INTO `system_log`
VALUES (744, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 3ms', 1, 'admin', '2026-05-23 12:35:38');
INSERT INTO `system_log`
VALUES (745, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 12:35:49');
INSERT INTO `system_log`
VALUES (746, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 12:57:08');
INSERT INTO `system_log`
VALUES (747, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 1, 'admin', '2026-05-23 12:57:14');
INSERT INTO `system_log`
VALUES (748, 'ERROR', 'USER', '更新个人资料',
        '执行失败: \r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\r\n### The error may exist in com/suse/campus_rent/mapper/UserMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.UserMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE user  SET username=?, iphone=?, email=?, nickname=?, real_name=?, avatar=?, driving_license=?, total_rentals=?, total_mileage=?, points=?, password=?, state=?, role=?,  total_spent=?, balance=?,  gender=?,  id_card=?, create_time=?, update_time=?  WHERE id=?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\n; Data truncation: Data too long for column \'avatar\' at row 1',
        1, 'admin', '2026-05-23 13:05:09');
INSERT INTO `system_log`
VALUES (749, 'ERROR', 'USER', '更新个人资料',
        '执行失败: \r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\r\n### The error may exist in com/suse/campus_rent/mapper/UserMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.UserMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE user  SET username=?, iphone=?, email=?, nickname=?, real_name=?, avatar=?, driving_license=?, total_rentals=?, total_mileage=?, points=?, password=?, state=?, role=?,  total_spent=?, balance=?,  gender=?,  id_card=?, create_time=?, update_time=?  WHERE id=?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\n; Data truncation: Data too long for column \'avatar\' at row 1',
        1, 'admin', '2026-05-23 13:05:39');
INSERT INTO `system_log`
VALUES (750, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 13:15:56');
INSERT INTO `system_log`
VALUES (751, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-23 13:16:06');
INSERT INTO `system_log`
VALUES (752, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-23 13:16:11');
INSERT INTO `system_log`
VALUES (753, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 13:16:16');
INSERT INTO `system_log`
VALUES (754, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 13:16:20');
INSERT INTO `system_log`
VALUES (755, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 195ms', 0, '系统/匿名', '2026-05-23 13:20:00');
INSERT INTO `system_log`
VALUES (756, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 8ms', 1, 'admin1', '2026-05-23 13:20:08');
INSERT INTO `system_log`
VALUES (757, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 87ms', 0, '系统/匿名', '2026-05-23 13:20:21');
INSERT INTO `system_log`
VALUES (758, 'ERROR', 'USER', '修改密码', '执行失败: 密码错误', 1, 'admin', '2026-05-23 13:40:41');
INSERT INTO `system_log`
VALUES (759, 'ERROR', 'USER', '修改密码', '执行失败: 密码错误', 1, 'admin', '2026-05-23 13:41:51');
INSERT INTO `system_log`
VALUES (760, 'ERROR', 'USER', '修改密码', '执行失败: 密码错误', 1, 'admin', '2026-05-23 13:42:03');
INSERT INTO `system_log`
VALUES (761, 'ERROR', 'USER', '修改密码', '执行失败: 密码错误', 1, 'admin', '2026-05-23 13:45:54');
INSERT INTO `system_log`
VALUES (762, 'INFO', 'USER', '修改密码', '执行成功，耗时: 168ms', 1, 'admin', '2026-05-23 13:50:38');
INSERT INTO `system_log`
VALUES (763, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 174ms', 0, '系统/匿名', '2026-05-23 13:51:05');
INSERT INTO `system_log`
VALUES (764, 'INFO', 'UPLOAD', '上传通知附件', '执行成功，耗时: 11ms', 0, '系统/匿名', '2026-05-23 13:55:54');
INSERT INTO `system_log`
VALUES (765, 'INFO', 'NOTICE', '新增公告', '执行成功，耗时: 15ms', 1, 'admin', '2026-05-23 13:56:05');
INSERT INTO `system_log`
VALUES (766, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 177ms', 0, '系统/匿名', '2026-05-23 14:26:35');
INSERT INTO `system_log`
VALUES (767, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 84ms', 0, '系统/匿名', '2026-05-23 14:26:46');
INSERT INTO `system_log`
VALUES (768, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 83ms', 0, '系统/匿名', '2026-05-23 14:27:12');
INSERT INTO `system_log`
VALUES (769, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 108ms', 0, '系统/匿名', '2026-05-23 14:27:21');
INSERT INTO `system_log`
VALUES (770, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 24ms', 22, 'bob', '2026-05-23 15:30:18');
INSERT INTO `system_log`
VALUES (771, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 16ms', 22, 'bob', '2026-05-23 15:30:37');
INSERT INTO `system_log`
VALUES (772, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 7ms', 22, 'bob', '2026-05-23 15:31:26');
INSERT INTO `system_log`
VALUES (773, 'INFO', 'CERT', '申请学生认证', '执行成功，耗时: 8ms', 22, 'bob', '2026-05-23 15:31:27');
INSERT INTO `system_log`
VALUES (774, 'INFO', 'USER', '审核实名认证', '执行成功，耗时: 13ms', 1, 'admin', '2026-05-23 15:31:55');
INSERT INTO `system_log`
VALUES (775, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 21ms', 22, 'bob', '2026-05-23 15:33:03');
INSERT INTO `system_log`
VALUES (776, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 10ms', 22, 'bob', '2026-05-23 15:33:09');
INSERT INTO `system_log`
VALUES (777, 'INFO', 'REVIEW', '修改评价状态', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 15:46:48');
INSERT INTO `system_log`
VALUES (778, 'INFO', 'REVIEW', '修改评价状态', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 15:46:49');
INSERT INTO `system_log`
VALUES (779, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 168ms', 0, '系统/匿名', '2026-05-23 18:49:07');
INSERT INTO `system_log`
VALUES (780, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 83ms', 0, '系统/匿名', '2026-05-23 18:49:21');
INSERT INTO `system_log`
VALUES (781, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 102ms', 0, '系统/匿名', '2026-05-23 18:50:46');
INSERT INTO `system_log`
VALUES (782, 'INFO', 'USER', '新增用户', '执行成功，耗时: 90ms', 1, 'admin', '2026-05-23 20:28:21');
INSERT INTO `system_log`
VALUES (783, 'INFO', 'USER', '修改用户', '执行成功，耗时: 18ms', 1, 'admin', '2026-05-23 20:28:39');
INSERT INTO `system_log`
VALUES (784, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 31ms', 1, 'admin', '2026-05-23 21:03:53');
INSERT INTO `system_log`
VALUES (785, 'INFO', 'VEHICLE', '修改车辆型号', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 21:05:10');
INSERT INTO `system_log`
VALUES (786, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 19ms', 1, 'admin', '2026-05-23 21:06:44');
INSERT INTO `system_log`
VALUES (787, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-05-23 21:08:58');
INSERT INTO `system_log`
VALUES (788, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-05-23 21:14:11');
INSERT INTO `system_log`
VALUES (789, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 21:14:14');
INSERT INTO `system_log`
VALUES (790, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 21:14:16');
INSERT INTO `system_log`
VALUES (791, 'ERROR', 'VEHICLE', '删除车辆',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`campus_rent`.`order`, CONSTRAINT `fk_order_car` FOREIGN KEY (`car_id`) REFERENCES `car_info` (`car_id`) ON DELETE RESTRICT ON UPDATE CASCADE)\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM car_info WHERE car_id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`campus_rent`.`order`, CONSTRAINT `fk_order_car` FOREIGN KEY (`car_id`) REFERENCES `car_info` (`car_id`) ON DELETE RESTRICT ON UPDATE CASCADE)\n; Cannot delete or update a parent row: a foreign key constraint fails (`campus_rent`.`order`, CONSTRAINT `fk_order_car` FOREIGN KEY (`car_id`) REFERENCES `car_info` (`car_id`) ON DELETE RESTRICT ON UPDATE CASCADE)',
        1, 'admin', '2026-05-23 21:14:23');
INSERT INTO `system_log`
VALUES (792, 'ERROR', 'VEHICLE', '删除车辆',
        '执行失败: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`campus_rent`.`order`, CONSTRAINT `fk_order_car` FOREIGN KEY (`car_id`) REFERENCES `car_info` (`car_id`) ON DELETE RESTRICT ON UPDATE CASCADE)\r\n### The error may exist in com/suse/campus_rent/mapper/CarInfoMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM car_info WHERE car_id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`campus_rent`.`order`, CONSTRAINT `fk_order_car` FOREIGN KEY (`car_id`) REFERENCES `car_info` (`car_id`) ON DELETE RESTRICT ON UPDATE CASCADE)\n; Cannot delete or update a parent row: a foreign key constraint fails (`campus_rent`.`order`, CONSTRAINT `fk_order_car` FOREIGN KEY (`car_id`) REFERENCES `car_info` (`car_id`) ON DELETE RESTRICT ON UPDATE CASCADE)',
        1, 'admin', '2026-05-23 21:14:30');
INSERT INTO `system_log`
VALUES (793, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 206ms', 0, '系统/匿名', '2026-05-23 22:01:06');
INSERT INTO `system_log`
VALUES (794, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:01:39');
INSERT INTO `system_log`
VALUES (795, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:01:42');
INSERT INTO `system_log`
VALUES (796, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 22:01:44');
INSERT INTO `system_log`
VALUES (797, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:01:46');
INSERT INTO `system_log`
VALUES (798, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:01:49');
INSERT INTO `system_log`
VALUES (799, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:01:51');
INSERT INTO `system_log`
VALUES (800, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:01:53');
INSERT INTO `system_log`
VALUES (801, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:01:55');
INSERT INTO `system_log`
VALUES (802, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:01:58');
INSERT INTO `system_log`
VALUES (803, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:02:00');
INSERT INTO `system_log`
VALUES (804, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:02:02');
INSERT INTO `system_log`
VALUES (805, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:04');
INSERT INTO `system_log`
VALUES (806, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 22:02:06');
INSERT INTO `system_log`
VALUES (807, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:02:08');
INSERT INTO `system_log`
VALUES (808, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:12');
INSERT INTO `system_log`
VALUES (809, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:02:18');
INSERT INTO `system_log`
VALUES (810, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:21');
INSERT INTO `system_log`
VALUES (811, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:02:23');
INSERT INTO `system_log`
VALUES (812, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 4ms', 1, 'admin', '2026-05-23 22:02:25');
INSERT INTO `system_log`
VALUES (813, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 22:02:28');
INSERT INTO `system_log`
VALUES (814, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:39');
INSERT INTO `system_log`
VALUES (815, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:43');
INSERT INTO `system_log`
VALUES (816, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:45');
INSERT INTO `system_log`
VALUES (817, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:48');
INSERT INTO `system_log`
VALUES (818, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:02:50');
INSERT INTO `system_log`
VALUES (819, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:51');
INSERT INTO `system_log`
VALUES (820, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:02:54');
INSERT INTO `system_log`
VALUES (821, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 4ms', 1, 'admin', '2026-05-23 22:02:59');
INSERT INTO `system_log`
VALUES (822, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 22:03:02');
INSERT INTO `system_log`
VALUES (823, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:03:04');
INSERT INTO `system_log`
VALUES (824, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:03:06');
INSERT INTO `system_log`
VALUES (825, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:03:09');
INSERT INTO `system_log`
VALUES (826, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:03:11');
INSERT INTO `system_log`
VALUES (827, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:03:13');
INSERT INTO `system_log`
VALUES (828, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 22:10:11');
INSERT INTO `system_log`
VALUES (829, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 13ms', 1, 'admin', '2026-05-23 22:10:57');
INSERT INTO `system_log`
VALUES (830, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:11:20');
INSERT INTO `system_log`
VALUES (831, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:11:57');
INSERT INTO `system_log`
VALUES (832, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:12:00');
INSERT INTO `system_log`
VALUES (833, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 4ms', 1, 'admin', '2026-05-23 22:12:03');
INSERT INTO `system_log`
VALUES (834, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:12:13');
INSERT INTO `system_log`
VALUES (835, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 22:13:17');
INSERT INTO `system_log`
VALUES (836, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-05-23 22:13:31');
INSERT INTO `system_log`
VALUES (837, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 22:13:46');
INSERT INTO `system_log`
VALUES (838, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-05-23 22:14:04');
INSERT INTO `system_log`
VALUES (839, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 22:14:17');
INSERT INTO `system_log`
VALUES (840, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-05-23 22:14:32');
INSERT INTO `system_log`
VALUES (841, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 16ms', 1, 'admin', '2026-05-23 22:14:54');
INSERT INTO `system_log`
VALUES (842, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-23 22:15:07');
INSERT INTO `system_log`
VALUES (843, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 22:15:31');
INSERT INTO `system_log`
VALUES (844, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 22:16:01');
INSERT INTO `system_log`
VALUES (845, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 22:16:20');
INSERT INTO `system_log`
VALUES (846, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 13ms', 1, 'admin', '2026-05-23 22:16:36');
INSERT INTO `system_log`
VALUES (847, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-05-23 22:16:45');
INSERT INTO `system_log`
VALUES (848, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 22:16:52');
INSERT INTO `system_log`
VALUES (849, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-23 22:16:56');
INSERT INTO `system_log`
VALUES (850, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:02');
INSERT INTO `system_log`
VALUES (851, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:04');
INSERT INTO `system_log`
VALUES (852, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:06');
INSERT INTO `system_log`
VALUES (853, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:08');
INSERT INTO `system_log`
VALUES (854, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:10');
INSERT INTO `system_log`
VALUES (855, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:12');
INSERT INTO `system_log`
VALUES (856, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-23 22:17:13');
INSERT INTO `system_log`
VALUES (857, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:17:15');
INSERT INTO `system_log`
VALUES (858, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:17:18');
INSERT INTO `system_log`
VALUES (859, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:17:20');
INSERT INTO `system_log`
VALUES (860, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:22');
INSERT INTO `system_log`
VALUES (861, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:17:24');
INSERT INTO `system_log`
VALUES (862, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:27');
INSERT INTO `system_log`
VALUES (863, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:29');
INSERT INTO `system_log`
VALUES (864, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:35');
INSERT INTO `system_log`
VALUES (865, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:37');
INSERT INTO `system_log`
VALUES (866, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:40');
INSERT INTO `system_log`
VALUES (867, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:42');
INSERT INTO `system_log`
VALUES (868, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-23 22:17:44');
INSERT INTO `system_log`
VALUES (869, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 22:17:46');
INSERT INTO `system_log`
VALUES (870, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-23 22:17:48');
INSERT INTO `system_log`
VALUES (871, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:50');
INSERT INTO `system_log`
VALUES (872, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:52');
INSERT INTO `system_log`
VALUES (873, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:17:54');
INSERT INTO `system_log`
VALUES (874, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:00');
INSERT INTO `system_log`
VALUES (875, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:02');
INSERT INTO `system_log`
VALUES (876, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:04');
INSERT INTO `system_log`
VALUES (877, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:05');
INSERT INTO `system_log`
VALUES (878, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:07');
INSERT INTO `system_log`
VALUES (879, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:09');
INSERT INTO `system_log`
VALUES (880, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:11');
INSERT INTO `system_log`
VALUES (881, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:16');
INSERT INTO `system_log`
VALUES (882, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:18');
INSERT INTO `system_log`
VALUES (883, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:20');
INSERT INTO `system_log`
VALUES (884, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:22');
INSERT INTO `system_log`
VALUES (885, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:28');
INSERT INTO `system_log`
VALUES (886, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:32');
INSERT INTO `system_log`
VALUES (887, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:35');
INSERT INTO `system_log`
VALUES (888, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:37');
INSERT INTO `system_log`
VALUES (889, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:38');
INSERT INTO `system_log`
VALUES (890, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:40');
INSERT INTO `system_log`
VALUES (891, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:44');
INSERT INTO `system_log`
VALUES (892, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:46');
INSERT INTO `system_log`
VALUES (893, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:48');
INSERT INTO `system_log`
VALUES (894, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:50');
INSERT INTO `system_log`
VALUES (895, 'WARN', 'VEHICLE', '删除车辆型号', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:18:52');
INSERT INTO `system_log`
VALUES (896, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:56');
INSERT INTO `system_log`
VALUES (897, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:18:59');
INSERT INTO `system_log`
VALUES (898, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:19:01');
INSERT INTO `system_log`
VALUES (899, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:19:03');
INSERT INTO `system_log`
VALUES (900, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:19:05');
INSERT INTO `system_log`
VALUES (901, 'ERROR', 'VEHICLE', '删除车辆型号', '执行失败: 该车型下存在车辆，无法删除', 1, 'admin',
        '2026-05-23 22:19:07');
INSERT INTO `system_log`
VALUES (902, 'INFO', 'USER', '修改用户', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-23 22:19:28');
INSERT INTO `system_log`
VALUES (903, 'INFO', 'USER', '修改用户', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:19:38');
INSERT INTO `system_log`
VALUES (904, 'INFO', 'USER', '修改用户', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:19:43');
INSERT INTO `system_log`
VALUES (905, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 22:20:16');
INSERT INTO `system_log`
VALUES (906, 'WARN', 'NOTICE', '删除公告', '执行成功，耗时: 6ms', 1, 'admin', '2026-05-23 22:20:18');
INSERT INTO `system_log`
VALUES (907, 'WARN', 'MAINTENANCE', '删除维修记录', '执行成功，耗时: 4ms', 1, 'admin', '2026-05-23 22:20:49');
INSERT INTO `system_log`
VALUES (908, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 9ms', 22, 'bob', '2026-05-23 22:31:43');
INSERT INTO `system_log`
VALUES (909, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 18ms', 22, 'bob', '2026-05-23 22:33:25');
INSERT INTO `system_log`
VALUES (910, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 24ms', 22, 'bob', '2026-05-23 22:33:28');
INSERT INTO `system_log`
VALUES (911, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-23 22:33:51');
INSERT INTO `system_log`
VALUES (912, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-24 16:16:39');
INSERT INTO `system_log`
VALUES (913, 'INFO', 'VEHICLE', '新增车辆', '执行成功，耗时: 25ms', 1, 'admin', '2026-05-24 16:16:39');
INSERT INTO `system_log`
VALUES (914, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 16ms', 1, 'admin', '2026-05-24 16:16:48');
INSERT INTO `system_log`
VALUES (915, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 172ms', 0, '系统/匿名', '2026-05-24 18:16:35');
INSERT INTO `system_log`
VALUES (916, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 88ms', 0, '系统/匿名', '2026-05-24 18:16:52');
INSERT INTO `system_log`
VALUES (917, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 169ms', 0, '系统/匿名', '2026-05-24 19:14:24');
INSERT INTO `system_log`
VALUES (918, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 119ms', 0, '系统/匿名', '2026-05-24 19:14:44');
INSERT INTO `system_log`
VALUES (919, 'ERROR', 'ORDER', '提交订单', '执行失败: 您当前已有 1 个进行中的订单，已达上限，请完成后再下单', 22, 'bob',
        '2026-05-24 19:33:18');
INSERT INTO `system_log`
VALUES (920, 'ERROR', 'ORDER', '提交订单', '执行失败: 您当前已有 1 个进行中的订单，已达上限，请完成后再下单', 22, 'bob',
        '2026-05-24 19:34:03');
INSERT INTO `system_log`
VALUES (921, 'ERROR', 'ORDER', '提交订单', '执行失败: 您当前已有 1 个进行中的订单，已达上限，请完成后再下单', 22, 'bob',
        '2026-05-24 19:35:03');
INSERT INTO `system_log`
VALUES (922, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 171ms', 0, '系统/匿名', '2026-05-24 19:42:35');
INSERT INTO `system_log`
VALUES (923, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 85ms', 0, '系统/匿名', '2026-05-24 19:43:08');
INSERT INTO `system_log`
VALUES (924, 'INFO', 'AUTH', '用户注册', '执行成功，耗时: 97ms', 0, '系统/匿名', '2026-05-24 20:28:55');
INSERT INTO `system_log`
VALUES (925, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 184ms', 0, '系统/匿名', '2026-05-24 20:29:22');
INSERT INTO `system_log`
VALUES (926, 'ERROR', 'USER', '更新个人资料',
        '执行失败: \r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\r\n### The error may exist in com/suse/campus_rent/mapper/UserMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.UserMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE user  SET username=?, iphone=?, email=?, nickname=?, real_name=?, avatar=?, driving_license=?, total_rentals=?, total_mileage=?, points=?, password=?, state=?, role=?, last_login_time=?, total_spent=?, balance=?,     create_time=?, update_time=?  WHERE id=?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\n; Data truncation: Data too long for column \'avatar\' at row 1',
        24, '用户2026-05-24', '2026-05-24 20:30:06');
INSERT INTO `system_log`
VALUES (927, 'ERROR', 'USER', '更新个人资料',
        '执行失败: \r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\r\n### The error may exist in com/suse/campus_rent/mapper/UserMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.UserMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE user  SET username=?, iphone=?, email=?, nickname=?, real_name=?, avatar=?, driving_license=?, total_rentals=?, total_mileage=?, points=?, password=?, state=?, role=?, last_login_time=?, total_spent=?, balance=?,     create_time=?, update_time=?  WHERE id=?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\n; Data truncation: Data too long for column \'avatar\' at row 1',
        24, '用户2026-05-24', '2026-05-24 20:31:50');
INSERT INTO `system_log`
VALUES (928, 'ERROR', 'USER', '更新个人资料',
        '执行失败: \r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\r\n### The error may exist in com/suse/campus_rent/mapper/UserMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.UserMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE user  SET username=?, iphone=?, email=?, nickname=?, real_name=?, avatar=?, driving_license=?, total_rentals=?, total_mileage=?, points=?, password=?, state=?, role=?, last_login_time=?, total_spent=?, balance=?,     create_time=?, update_time=?  WHERE id=?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\n; Data truncation: Data too long for column \'avatar\' at row 1',
        24, '用户2026-05-24', '2026-05-24 20:33:13');
INSERT INTO `system_log`
VALUES (929, 'ERROR', 'USER', '更新个人资料',
        '执行失败: \r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\r\n### The error may exist in com/suse/campus_rent/mapper/UserMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.UserMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE user  SET username=?, iphone=?, email=?, nickname=?, real_name=?, avatar=?, driving_license=?, total_rentals=?, total_mileage=?, points=?, password=?, state=?, role=?, last_login_time=?, total_spent=?, balance=?,     create_time=?, update_time=?  WHERE id=?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\n; Data truncation: Data too long for column \'avatar\' at row 1',
        24, '用户2026-05-24', '2026-05-24 20:42:06');
INSERT INTO `system_log`
VALUES (930, 'ERROR', 'USER', '更新个人资料',
        '执行失败: \r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\r\n### The error may exist in com/suse/campus_rent/mapper/UserMapper.java (best guess)\r\n### The error may involve com.suse.campus_rent.mapper.UserMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE user  SET username=?, iphone=?, email=?, nickname=?, real_name=?, avatar=?, driving_license=?, total_rentals=?, total_mileage=?, points=?, password=?, state=?, role=?, last_login_time=?, total_spent=?, balance=?,     create_time=?, update_time=?  WHERE id=?\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'avatar\' at row 1\n; Data truncation: Data too long for column \'avatar\' at row 1',
        24, '用户2026-05-24', '2026-05-24 21:07:28');
INSERT INTO `system_log`
VALUES (931, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 32ms', 24, '用户2026-05-24', '2026-05-24 21:08:48');
INSERT INTO `system_log`
VALUES (932, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 216ms', 0, '系统/匿名', '2026-05-24 21:09:14');
INSERT INTO `system_log`
VALUES (933, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 21ms', 24, 'users', '2026-05-24 21:11:48');
INSERT INTO `system_log`
VALUES (934, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 23ms', 24, 'users', '2026-05-24 21:11:56');
INSERT INTO `system_log`
VALUES (935, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 13ms', 1, 'admin', '2026-05-24 21:12:17');
INSERT INTO `system_log`
VALUES (936, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 12ms', 24, 'users', '2026-05-24 21:29:42');
INSERT INTO `system_log`
VALUES (937, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 20ms', 1, 'admin', '2026-05-24 21:32:43');
INSERT INTO `system_log`
VALUES (938, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 17ms', 24, 'users', '2026-05-24 21:51:45');
INSERT INTO `system_log`
VALUES (939, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 17ms', 24, 'users', '2026-05-24 21:51:51');
INSERT INTO `system_log`
VALUES (940, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 11ms', 1, 'admin', '2026-05-24 21:52:03');
INSERT INTO `system_log`
VALUES (941, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 10ms', 24, 'users', '2026-05-24 21:52:09');
INSERT INTO `system_log`
VALUES (942, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 16ms', 1, 'admin', '2026-05-24 21:52:34');
INSERT INTO `system_log`
VALUES (943, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 178ms', 0, '系统/匿名', '2026-05-24 22:05:03');
INSERT INTO `system_log`
VALUES (944, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 91ms', 0, '系统/匿名', '2026-05-24 22:05:32');
INSERT INTO `system_log`
VALUES (945, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 93ms', 0, '系统/匿名', '2026-05-24 22:05:41');
INSERT INTO `system_log`
VALUES (946, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 96ms', 0, '系统/匿名', '2026-05-24 22:06:24');
INSERT INTO `system_log`
VALUES (947, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 16ms', 22, 'bob', '2026-05-24 22:10:24');
INSERT INTO `system_log`
VALUES (948, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 22ms', 1, 'admin', '2026-05-24 22:10:50');
INSERT INTO `system_log`
VALUES (949, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 203ms', 0, '系统/匿名', '2026-05-29 21:44:14');
INSERT INTO `system_log`
VALUES (950, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 236ms', 0, '系统/匿名', '2026-05-29 21:44:31');
INSERT INTO `system_log`
VALUES (951, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 87ms', 0, '系统/匿名', '2026-05-29 21:46:12');
INSERT INTO `system_log`
VALUES (952, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 100ms', 0, '系统/匿名', '2026-05-29 21:46:25');
INSERT INTO `system_log`
VALUES (953, 'INFO', 'AUTH', '用户注册', '执行成功，耗时: 119ms', 0, '系统/匿名', '2026-05-30 00:16:52');
INSERT INTO `system_log`
VALUES (954, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 5ms', 0, '系统/匿名', '2026-05-30 00:22:29');
INSERT INTO `system_log`
VALUES (955, 'WARN', 'AUTH', '忘记密码', '执行成功，耗时: 103ms', 0, '系统/匿名', '2026-05-30 00:22:34');
INSERT INTO `system_log`
VALUES (956, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 102ms', 0, '系统/匿名', '2026-05-30 00:22:49');
INSERT INTO `system_log`
VALUES (957, 'INFO', 'USER', '更新个人资料', '执行成功，耗时: 31ms', 25, '用户2026-05-30', '2026-05-30 00:23:26');
INSERT INTO `system_log`
VALUES (958, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 100ms', 0, '系统/匿名', '2026-05-30 00:23:42');
INSERT INTO `system_log`
VALUES (959, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 23ms', 25, 'alice', '2026-05-30 00:25:28');
INSERT INTO `system_log`
VALUES (960, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 22ms', 25, 'alice', '2026-05-30 00:25:35');
INSERT INTO `system_log`
VALUES (961, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 111ms', 0, '系统/匿名', '2026-05-30 00:28:02');
INSERT INTO `system_log`
VALUES (962, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 11ms', 1, 'admin', '2026-05-30 00:28:14');
INSERT INTO `system_log`
VALUES (963, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 8ms', 25, 'alice', '2026-05-30 00:28:26');
INSERT INTO `system_log`
VALUES (964, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 14ms', 1, 'admin', '2026-05-30 00:28:35');
INSERT INTO `system_log`
VALUES (965, 'INFO', 'UPLOAD', '上传图片(RImg)', '执行成功，耗时: 5ms', 25, 'alice', '2026-05-30 00:30:59');
INSERT INTO `system_log`
VALUES (966, 'INFO', 'REVIEW', '提交评价', '执行成功，耗时: 8ms', 25, 'alice', '2026-05-30 00:31:00');
INSERT INTO `system_log`
VALUES (967, 'INFO', 'REVIEW', '修改评价状态', '执行成功，耗时: 5ms', 25, 'alice', '2026-05-30 00:31:06');
INSERT INTO `system_log`
VALUES (968, 'INFO', 'REVIEW', '修改评价状态', '执行成功，耗时: 6ms', 25, 'alice', '2026-05-30 00:31:07');
INSERT INTO `system_log`
VALUES (969, 'INFO', 'UPLOAD', '上传头像', '执行成功，耗时: 2ms', 25, 'alice', '2026-05-30 00:31:58');
INSERT INTO `system_log`
VALUES (970, 'INFO', 'CERT', '申请学生认证', '执行成功，耗时: 10ms', 25, 'alice', '2026-05-30 00:31:59');
INSERT INTO `system_log`
VALUES (971, 'INFO', 'USER', '审核实名认证', '执行成功，耗时: 11ms', 1, 'admin', '2026-05-30 00:32:17');
INSERT INTO `system_log`
VALUES (972, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 14ms', 25, 'alice', '2026-05-30 00:32:39');
INSERT INTO `system_log`
VALUES (973, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 13ms', 25, 'alice', '2026-05-30 00:32:43');
INSERT INTO `system_log`
VALUES (974, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-30 00:32:58');
INSERT INTO `system_log`
VALUES (975, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 6ms', 25, 'alice', '2026-05-30 00:33:09');
INSERT INTO `system_log`
VALUES (976, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 13ms', 1, 'admin', '2026-05-30 00:33:16');
INSERT INTO `system_log`
VALUES (977, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 8ms', 25, 'alice', '2026-05-30 00:33:36');
INSERT INTO `system_log`
VALUES (978, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 12ms', 25, 'alice', '2026-05-30 00:33:40');
INSERT INTO `system_log`
VALUES (979, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-30 00:33:47');
INSERT INTO `system_log`
VALUES (980, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 6ms', 25, 'alice', '2026-05-30 00:34:18');
INSERT INTO `system_log`
VALUES (981, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 13ms', 1, 'admin', '2026-05-30 00:34:27');
INSERT INTO `system_log`
VALUES (982, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 10ms', 25, 'alice', '2026-05-30 00:35:21');
INSERT INTO `system_log`
VALUES (983, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 11ms', 25, 'alice', '2026-05-30 00:35:26');
INSERT INTO `system_log`
VALUES (984, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-30 00:35:40');
INSERT INTO `system_log`
VALUES (985, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 6ms', 25, 'alice', '2026-05-30 00:35:59');
INSERT INTO `system_log`
VALUES (986, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 9ms', 1, 'admin', '2026-05-30 00:36:05');
INSERT INTO `system_log`
VALUES (987, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 11ms', 25, 'alice', '2026-05-30 00:42:32');
INSERT INTO `system_log`
VALUES (988, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 10ms', 25, 'alice', '2026-05-30 00:42:35');
INSERT INTO `system_log`
VALUES (989, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-30 00:42:44');
INSERT INTO `system_log`
VALUES (990, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 8ms', 25, 'alice', '2026-05-30 00:42:51');
INSERT INTO `system_log`
VALUES (991, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 16ms', 1, 'admin', '2026-05-30 00:43:12');
INSERT INTO `system_log`
VALUES (992, 'INFO', 'NOTICE', '新增公告', '执行成功，耗时: 7ms', 1, 'admin', '2026-05-30 00:44:09');
INSERT INTO `system_log`
VALUES (993, 'INFO', 'NOTICE', '发布公告', '执行成功，耗时: 5ms', 1, 'admin', '2026-05-30 00:44:11');
INSERT INTO `system_log`
VALUES (994, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 256ms', 0, '系统/匿名', '2026-05-30 15:09:20');
INSERT INTO `system_log`
VALUES (995, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 27ms', 1, 'admin', '2026-05-30 15:09:46');
INSERT INTO `system_log`
VALUES (996, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-30 15:09:54');
INSERT INTO `system_log`
VALUES (997, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 10ms', 1, 'admin', '2026-05-30 15:10:03');
INSERT INTO `system_log`
VALUES (998, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 16ms', 1, 'admin', '2026-05-30 15:10:26');
INSERT INTO `system_log`
VALUES (999, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 11ms', 1, 'admin', '2026-05-30 15:10:35');
INSERT INTO `system_log`
VALUES (1000, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 15ms', 1, 'admin', '2026-05-30 15:10:45');
INSERT INTO `system_log`
VALUES (1001, 'INFO', 'VEHICLE', '修改车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-05-30 15:11:00');
INSERT INTO `system_log`
VALUES (1002, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 12ms', 1, 'admin', '2026-05-30 15:11:12');
INSERT INTO `system_log`
VALUES (1003, 'WARN', 'VEHICLE', '删除车辆', '执行成功，耗时: 8ms', 1, 'admin', '2026-05-30 15:11:23');
INSERT INTO `system_log`
VALUES (1004, 'INFO', 'MAINTENANCE', '新增维修记录', '执行成功，耗时: 15ms', 1, 'admin', '2026-05-30 15:11:53');
INSERT INTO `system_log`
VALUES (1005, 'INFO', 'MAINTENANCE', '修改维修记录', '执行成功，耗时: 12ms', 1, 'admin', '2026-05-30 15:11:57');
INSERT INTO `system_log`
VALUES (1006, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 234ms', 0, '系统/匿名', '2026-05-30 18:23:12');
INSERT INTO `system_log`
VALUES (1007, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 35ms', 24, 'users', '2026-05-30 19:51:29');
INSERT INTO `system_log`
VALUES (1008, 'WARN', 'ORDER', '取消订单', '执行成功，耗时: 16ms', 24, 'users', '2026-05-30 19:51:43');
INSERT INTO `system_log`
VALUES (1009, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 22ms', 24, 'users', '2026-05-30 19:51:51');
INSERT INTO `system_log`
VALUES (1010, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 21ms', 24, 'users', '2026-05-30 19:52:00');
INSERT INTO `system_log`
VALUES (1011, 'INFO', 'ORDER', '确认取车', '执行成功，耗时: 12ms', 1, 'admin', '2026-05-30 19:52:34');
INSERT INTO `system_log`
VALUES (1012, 'INFO', 'ORDER', '申请还车', '执行成功，耗时: 9ms', 24, 'users', '2026-05-30 19:53:44');
INSERT INTO `system_log`
VALUES (1013, 'INFO', 'ORDER', '确认还车', '执行成功，耗时: 21ms', 1, 'admin', '2026-05-30 19:54:53');
INSERT INTO `system_log`
VALUES (1014, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 251ms', 0, '系统/匿名', '2026-05-31 12:10:53');
INSERT INTO `system_log`
VALUES (1015, 'INFO', 'AUTH', '用户登录', '执行成功，耗时: 106ms', 0, '系统/匿名', '2026-05-31 12:18:22');
INSERT INTO `system_log`
VALUES (1016, 'INFO', 'ORDER', '提交订单', '执行成功，耗时: 44ms', 1, 'admin', '2026-05-31 15:37:30');
INSERT INTO `system_log`
VALUES (1017, 'INFO', 'ORDER', '支付订单', '执行成功，耗时: 23ms', 1, 'admin', '2026-05-31 15:37:33');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`                  bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
    `username`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '登录用户名',
    `iphone`              varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '手机号，可用于登录',
    `email`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '邮箱，可用于登录',
    `nickname`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '用户昵称',
    `real_name`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '真实姓名',
    `avatar`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '头像URL',
    `driving_license`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '驾驶证信息',
    `total_rentals`       int(0)                                                        NOT NULL DEFAULT 0 COMMENT '总租车次数',
    `total_mileage`       int(0)                                                        NOT NULL DEFAULT 0 COMMENT '累计里程(公里)',
    `points`              int(0)                                                        NOT NULL DEFAULT 0 COMMENT '可用积分',
    `password`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '加密存储的密码',
    `state`               tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '状态：0-正常，1-禁用',
    `role`                varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT 'ordinary' COMMENT '用户角色：ordinary, student, vip, admin',
    `create_time`         datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`         datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
    `is_deleted`          tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '软删除标记：0-未删除，1-已删除',
    `total_spent`         decimal(10, 2)                                                NULL COMMENT '累计消费',
    `balance`             decimal(10, 2)                                                NULL COMMENT '账户余额',
    `student_expire_time` date                                                          NULL     DEFAULT NULL COMMENT '学生认证过期日期',
    `gender`              varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '性别',
    `address`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '联系地址',
    `id_card`             varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '身份证号',
    `last_login_time`     datetime(0)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_username` (`username`) USING BTREE,
    UNIQUE INDEX `uk_iphone` (`iphone`) USING BTREE,
    UNIQUE INDEX `uk_email` (`email`) USING BTREE,
    INDEX `idx_role` (`role`) USING BTREE,
    CONSTRAINT `fk_user_role` FOREIGN KEY (`role`) REFERENCES `role_benefits` (`role`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 26
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户基本信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, 'admin', '15583183180', '30501551274@qq.com', '管理员', '螺纹钢123',
        'http://localhost:8080/api/uploads/2026/04/02/ede406e7-cdb5-4781-b286-2a7d7e6d8fe2.jpg', '111', 0, 0, 100,
        '$2a$10$dTTYGyMzkSbTGD/JZGtMi.hI7tQMHaIYinSZGkwVZPFhw.WmPDS.e', 1, 'admin', '2026-03-02 00:05:17',
        '2026-05-31 12:18:22', 0, 1415.00, 0.00, NULL, 'male', NULL, '123456', '2026-05-31 12:18:22');
INSERT INTO `user`
VALUES (4, 'user', '15583181812', '30501551714@qq.com', 'abc2026-03-02', '123',
        'http://localhost:8080/api/uploads/2026/03/17/ff1daacb-75ba-4aa4-bb58-84250b3633c6.jfif', '2030-09-09到期', 0,
        0, 0, '$2a$10$L74AkUb/suz2ReLz8xFT7e6WvHTsxb9HMgQyWJ9jIOv6BCnd1dHDK', 1, 'user', '2026-03-02 16:19:51',
        '2026-05-23 22:33:01', 0, 22457.75, 3637.79, '2030-04-30', 'male', '爱戴省欧克市崩坏县虎牙镇丛林社区83组240号',
        '', NULL);
INSERT INTO `user`
VALUES (19, '用户2026-03-28', '15565234521', '305015005174@qq.com', 'abc2026-03-28', '测试', NULL, NULL, 0, 0, 0,
        '$2a$10$dlYadfva7Pefnd4vLymeOO9yq.WNhj13QiF4sx7CsQu5G0r.b64ES', 1, 'user', '2026-03-28 19:51:07',
        '2026-05-23 22:33:04', 0, 0.00, 0.00, '2030-04-27', 'male', '四川省广元市剑阁镇灌社区113组1110号', NULL, NULL);
INSERT INTO `user`
VALUES (22, 'bob', '15583183189', '123321@123.com', 'abc2026-05-20', '鲍勃',
        'http://localhost:8080/api/uploads/2026/05/20/abf9c2d1-6fbd-40fb-a771-f5f03e9c33c4.jpg', '29978396397', 1, 0,
        220, '$2a$10$8i9z1DWjLI.OTM5e9zTkNefwBMZH7p6GachHojlHgLzBBjbVcUemK', 1, 'user', '2026-05-20 15:20:17',
        '2026-05-24 22:06:23', 0, 220.00, 150.00, '2030-05-23', 'male', NULL, NULL, '2026-05-24 22:06:24');
INSERT INTO `user`
VALUES (23, 'admin123', '15543243242', '305015235174@qq.com', '管理员', '管理员', NULL, '123456', 0, 0, 0,
        '$2a$10$NUK5D5YeYLoajDrtDD4QXuo2PIXuoe2Qdm0PSXYEpCMCZDfbgDC/i', 1, 'admin', '2026-05-23 20:28:21',
        '2026-05-23 20:28:39', 0, 0.00, 0.00, NULL, 'male', '四川省', '3245335', NULL);
INSERT INTO `user`
VALUES (24, 'users', '18898798798', '123@123.com', '用户2026-05-24', '123',
        'http://localhost:8080/api/uploads/2026/05/24/12787cdc-6d06-4220-934f-b7e61e9a9cb6.jpg', '123', 2, 0, 720,
        '$2a$10$pbVXT/4K0cXIOauY8GMJSuNNO47e3ghLDl7P5VCXdfBBIWiQQuB/S', 1, 'user', '2026-05-24 20:28:55',
        '2026-05-31 12:10:52', 0, 720.00, 550.00, NULL, NULL, NULL, NULL, '2026-05-31 12:10:53');
INSERT INTO `user`
VALUES (25, 'alice', '15589089089', '3050155111174@qq.com', '用户2026-05-30', '123',
        'http://localhost:8080/api/uploads/2026/05/30/6cc52809-a091-4b8a-951c-c43ddb55d43d.jpg', '123456', 5, 0, 848,
        '$2a$10$Weot3eUDDGtjySWxGDg7W.ukpMkIo2EZP79lqABCKnVXa38isCxn.', 1, 'student', '2026-05-30 00:16:52',
        '2026-05-30 00:23:41', 0, 848.00, 750.00, '2030-05-30', NULL, NULL, NULL, '2026-05-30 00:23:42');

-- ----------------------------
-- Table structure for user_certification
-- ----------------------------
DROP TABLE IF EXISTS `user_certification`;
CREATE TABLE `user_certification`
(
    `id`            bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`       bigint(0)                                                      NOT NULL COMMENT '用户ID',
    `cert_type`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '认证类型：student-学生认证',
    `real_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '真实姓名',
    `student_id`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '学号（学生认证）',
    `school`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '学校（学生认证）',
    `id_card`       varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '身份证号（可选）',
    `image_urls`    varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '证明材料图片URL，多个用逗号分隔',
    `status`        tinyint(0)                                                     NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-审核通过，2-审核拒绝',
    `audit_remark`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '审核备注',
    `apply_time`    datetime(0)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '申请时间',
    `audit_time`    datetime(0)                                                    NULL     DEFAULT NULL COMMENT '审核时间',
    `audit_user_id` bigint(0)                                                      NULL     DEFAULT NULL COMMENT '审核人ID',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_status` (`status`) USING BTREE,
    CONSTRAINT `fk_cert_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户认证申请表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_certification
-- ----------------------------
INSERT INTO `user_certification`
VALUES (9, 25, 'student', '喜羊羊', '2210900909', '四川轻化工大学', '',
        'http://localhost:8080/api/uploads/2026/05/30/5471b836-afb0-49f9-bcfa-dd1b167ba581.jpg', 1, '',
        '2026-05-30 00:31:59', '2026-05-30 00:32:17', 1);

-- ----------------------------
-- Table structure for user_notice_read
-- ----------------------------
DROP TABLE IF EXISTS `user_notice_read`;
CREATE TABLE `user_notice_read`
(
    `id`          bigint(0)   NOT NULL AUTO_INCREMENT,
    `user_id`     bigint(0)   NOT NULL COMMENT '用户ID',
    `notice_id`   bigint(0)   NOT NULL COMMENT '公告ID',
    `read_time`   datetime(0) NOT NULL COMMENT '读取时间',
    `delete_time` datetime(0) NULL     DEFAULT NULL COMMENT '删除时间（用户移除公告时间）',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_notice` (`user_id`, `notice_id`) USING BTREE,
    INDEX `idx_notice_id` (`notice_id`) USING BTREE,
    INDEX `idx_read_time` (`read_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户公告读取记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_notice_read
-- ----------------------------

-- ----------------------------
-- Table structure for user_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `user_operation_log`;
CREATE TABLE `user_operation_log`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT,
    `user_id`     bigint(0)                                                     NOT NULL COMMENT '用户ID',
    `username`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '用户名',
    `action`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '操作类型：LOGIN, UPDATE_PROFILE, CHANGE_PASSWORD, UPLOAD_AVATAR, BIND_PHONE, BIND_EMAIL等',
    `content`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作内容',
    `result`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT 'SUCCESS' COMMENT '结果：SUCCESS/FAIL',
    `create_time` datetime(0)                                                   NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_username` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 126
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户操作日志表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_operation_log
-- ----------------------------
INSERT INTO `user_operation_log`
VALUES (4, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-16 17:00:50');
INSERT INTO `user_operation_log`
VALUES (5, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-16 17:04:35');
INSERT INTO `user_operation_log`
VALUES (6, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-16 17:05:14');
INSERT INTO `user_operation_log`
VALUES (7, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-16 17:05:56');
INSERT INTO `user_operation_log`
VALUES (8, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-16 19:17:39');
INSERT INTO `user_operation_log`
VALUES (9, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-16 21:58:20');
INSERT INTO `user_operation_log`
VALUES (10, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-16 21:58:48');
INSERT INTO `user_operation_log`
VALUES (11, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-17 16:16:22');
INSERT INTO `user_operation_log`
VALUES (12, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-17 17:29:14');
INSERT INTO `user_operation_log`
VALUES (13, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-17 22:00:11');
INSERT INTO `user_operation_log`
VALUES (14, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-18 12:34:37');
INSERT INTO `user_operation_log`
VALUES (15, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-18 20:53:45');
INSERT INTO `user_operation_log`
VALUES (16, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-18 20:54:39');
INSERT INTO `user_operation_log`
VALUES (17, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-18 20:57:36');
INSERT INTO `user_operation_log`
VALUES (18, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-18 20:58:02');
INSERT INTO `user_operation_log`
VALUES (19, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-18 21:34:30');
INSERT INTO `user_operation_log`
VALUES (20, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-19 12:07:58');
INSERT INTO `user_operation_log`
VALUES (21, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-21 14:38:50');
INSERT INTO `user_operation_log`
VALUES (22, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-22 15:15:15');
INSERT INTO `user_operation_log`
VALUES (23, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-24 16:25:31');
INSERT INTO `user_operation_log`
VALUES (24, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-25 12:28:44');
INSERT INTO `user_operation_log`
VALUES (25, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-27 14:15:34');
INSERT INTO `user_operation_log`
VALUES (26, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-27 19:07:45');
INSERT INTO `user_operation_log`
VALUES (27, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-28 19:29:07');
INSERT INTO `user_operation_log`
VALUES (28, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-28 19:36:47');
INSERT INTO `user_operation_log`
VALUES (29, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-28 19:51:39');
INSERT INTO `user_operation_log`
VALUES (30, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-28 20:08:03');
INSERT INTO `user_operation_log`
VALUES (31, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-28 23:20:10');
INSERT INTO `user_operation_log`
VALUES (32, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-29 11:00:23');
INSERT INTO `user_operation_log`
VALUES (33, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-29 11:08:18');
INSERT INTO `user_operation_log`
VALUES (34, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-29 12:19:16');
INSERT INTO `user_operation_log`
VALUES (35, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-29 12:24:33');
INSERT INTO `user_operation_log`
VALUES (36, 4, 'user', 'ROLE_UPGRADE', '积分达到 2164，角色从 student 升级为 svip', 'SUCCESS', '2026-03-29 12:24:53');
INSERT INTO `user_operation_log`
VALUES (37, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-29 12:25:26');
INSERT INTO `user_operation_log`
VALUES (38, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-29 12:31:32');
INSERT INTO `user_operation_log`
VALUES (39, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-29 12:32:39');
INSERT INTO `user_operation_log`
VALUES (40, 4, 'user', 'ROLE_UPGRADE', '积分达到 1368，角色从 vip 升级为 svip', 'SUCCESS', '2026-03-29 13:55:40');
INSERT INTO `user_operation_log`
VALUES (41, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-03-30 21:46:22');
INSERT INTO `user_operation_log`
VALUES (42, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-01 10:45:50');
INSERT INTO `user_operation_log`
VALUES (43, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-01 16:23:39');
INSERT INTO `user_operation_log`
VALUES (44, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-02 16:29:13');
INSERT INTO `user_operation_log`
VALUES (45, 1, 'admin', 'UPLOAD_AVATAR', '更新了头像', 'SUCCESS', '2026-04-02 17:52:23');
INSERT INTO `user_operation_log`
VALUES (46, 1, 'admin', 'UPDATE_PROFILE', '更新了个人资料', 'SUCCESS', '2026-04-02 17:52:33');
INSERT INTO `user_operation_log`
VALUES (47, 1, 'admin', 'UPLOAD_AVATAR', '更新了头像', 'SUCCESS', '2026-04-02 17:54:52');
INSERT INTO `user_operation_log`
VALUES (48, 1, 'admin', 'UPDATE_PROFILE', '更新了个人资料', 'SUCCESS', '2026-04-02 17:55:01');
INSERT INTO `user_operation_log`
VALUES (49, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-02 17:59:17');
INSERT INTO `user_operation_log`
VALUES (50, 1, 'admin', 'CHANGE_PASSWORD', '修改了登录密码', 'SUCCESS', '2026-04-02 17:59:57');
INSERT INTO `user_operation_log`
VALUES (51, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-02 18:00:43');
INSERT INTO `user_operation_log`
VALUES (52, 1, 'admin', 'UPDATE_PROFILE', '更新了个人资料', 'SUCCESS', '2026-04-02 18:02:47');
INSERT INTO `user_operation_log`
VALUES (53, 1, 'admin', 'UPDATE_PROFILE', '更新了个人资料', 'SUCCESS', '2026-04-02 18:04:38');
INSERT INTO `user_operation_log`
VALUES (54, 1, 'admin', 'UPDATE_PROFILE', '更新了个人资料', 'SUCCESS', '2026-04-02 18:05:42');
INSERT INTO `user_operation_log`
VALUES (55, 1, 'admin', 'UPDATE_PROFILE', '更新了个人资料', 'SUCCESS', '2026-04-02 18:07:32');
INSERT INTO `user_operation_log`
VALUES (56, 1, 'admin', 'UPDATE_PROFILE', '更新了个人资料', 'SUCCESS', '2026-04-02 18:09:13');
INSERT INTO `user_operation_log`
VALUES (57, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-03 18:29:13');
INSERT INTO `user_operation_log`
VALUES (58, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-04 20:28:47');
INSERT INTO `user_operation_log`
VALUES (59, 19, '用户2026-03-28', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-05 17:08:53');
INSERT INTO `user_operation_log`
VALUES (60, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-05 17:49:49');
INSERT INTO `user_operation_log`
VALUES (61, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-09 10:27:13');
INSERT INTO `user_operation_log`
VALUES (62, 4, 'user', 'ROLE_UPGRADE', '积分达到 7086，角色从 svip 升级为 admin', 'SUCCESS', '2026-04-09 20:28:16');
INSERT INTO `user_operation_log`
VALUES (63, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-09 20:30:02');
INSERT INTO `user_operation_log`
VALUES (64, 4, 'user', 'ROLE_UPGRADE', '积分达到 7626，角色从 vip 升级为 admin', 'SUCCESS', '2026-04-09 20:32:38');
INSERT INTO `user_operation_log`
VALUES (65, 4, 'user', 'ROLE_UPGRADE', '积分达到 8826，角色从 vip 升级为 admin', 'SUCCESS', '2026-04-09 21:18:45');
INSERT INTO `user_operation_log`
VALUES (66, 4, 'user', 'ROLE_UPGRADE', '积分达到 12216，角色从 vip 升级为 admin', 'SUCCESS', '2026-04-09 21:27:17');
INSERT INTO `user_operation_log`
VALUES (67, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-09 21:49:26');
INSERT INTO `user_operation_log`
VALUES (68, 4, 'user', 'ROLE_UPGRADE', '积分达到 2268825，角色从 vip 升级为 svip', 'SUCCESS', '2026-04-09 22:47:34');
INSERT INTO `user_operation_log`
VALUES (69, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-10 20:30:46');
INSERT INTO `user_operation_log`
VALUES (70, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-11 11:12:38');
INSERT INTO `user_operation_log`
VALUES (71, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-11 12:04:53');
INSERT INTO `user_operation_log`
VALUES (72, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-11 15:44:21');
INSERT INTO `user_operation_log`
VALUES (73, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-11 15:48:38');
INSERT INTO `user_operation_log`
VALUES (74, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-11 15:54:39');
INSERT INTO `user_operation_log`
VALUES (75, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-11 20:40:12');
INSERT INTO `user_operation_log`
VALUES (76, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-11 22:27:11');
INSERT INTO `user_operation_log`
VALUES (77, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-14 15:22:20');
INSERT INTO `user_operation_log`
VALUES (78, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-14 15:24:42');
INSERT INTO `user_operation_log`
VALUES (79, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-14 15:28:24');
INSERT INTO `user_operation_log`
VALUES (80, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-15 10:43:18');
INSERT INTO `user_operation_log`
VALUES (81, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-15 10:43:48');
INSERT INTO `user_operation_log`
VALUES (82, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-15 10:44:35');
INSERT INTO `user_operation_log`
VALUES (83, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-15 16:31:08');
INSERT INTO `user_operation_log`
VALUES (84, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-15 17:07:29');
INSERT INTO `user_operation_log`
VALUES (85, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-16 16:33:07');
INSERT INTO `user_operation_log`
VALUES (86, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-16 17:09:40');
INSERT INTO `user_operation_log`
VALUES (87, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-16 22:45:22');
INSERT INTO `user_operation_log`
VALUES (88, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-17 22:24:20');
INSERT INTO `user_operation_log`
VALUES (89, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-17 22:45:44');
INSERT INTO `user_operation_log`
VALUES (90, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-22 14:26:02');
INSERT INTO `user_operation_log`
VALUES (91, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-26 18:19:15');
INSERT INTO `user_operation_log`
VALUES (92, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-26 18:57:50');
INSERT INTO `user_operation_log`
VALUES (93, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-27 14:04:04');
INSERT INTO `user_operation_log`
VALUES (94, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-27 18:23:03');
INSERT INTO `user_operation_log`
VALUES (95, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-30 21:43:46');
INSERT INTO `user_operation_log`
VALUES (96, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-04-30 22:01:37');
INSERT INTO `user_operation_log`
VALUES (97, 4, 'user', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-02 17:07:09');
INSERT INTO `user_operation_log`
VALUES (98, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-02 23:06:48');
INSERT INTO `user_operation_log`
VALUES (99, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-20 15:24:11');
INSERT INTO `user_operation_log`
VALUES (100, 22, '用户2026-05-20', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-20 17:29:48');
INSERT INTO `user_operation_log`
VALUES (101, 22, '用户2026-05-20', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-20 17:47:51');
INSERT INTO `user_operation_log`
VALUES (102, 22, 'alice', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-20 17:51:29');
INSERT INTO `user_operation_log`
VALUES (103, 22, 'bob', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-20 18:11:25');
INSERT INTO `user_operation_log`
VALUES (104, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-22 19:35:06');
INSERT INTO `user_operation_log`
VALUES (105, 1, 'admin1', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-23 13:20:00');
INSERT INTO `user_operation_log`
VALUES (106, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-23 13:20:21');
INSERT INTO `user_operation_log`
VALUES (107, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-23 13:51:05');
INSERT INTO `user_operation_log`
VALUES (108, 22, 'bob', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-23 14:27:21');
INSERT INTO `user_operation_log`
VALUES (109, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-23 18:50:46');
INSERT INTO `user_operation_log`
VALUES (110, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-23 22:01:06');
INSERT INTO `user_operation_log`
VALUES (111, 22, 'bob', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-24 19:14:44');
INSERT INTO `user_operation_log`
VALUES (112, 24, '用户2026-05-24', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-24 20:29:22');
INSERT INTO `user_operation_log`
VALUES (113, 24, 'users', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-24 21:09:14');
INSERT INTO `user_operation_log`
VALUES (114, 24, 'users', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-24 22:05:03');
INSERT INTO `user_operation_log`
VALUES (115, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-24 22:05:41');
INSERT INTO `user_operation_log`
VALUES (116, 22, 'bob', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-24 22:06:24');
INSERT INTO `user_operation_log`
VALUES (117, 24, 'users', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-29 21:44:31');
INSERT INTO `user_operation_log`
VALUES (118, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-29 21:46:25');
INSERT INTO `user_operation_log`
VALUES (119, 25, '用户2026-05-30', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-30 00:22:49');
INSERT INTO `user_operation_log`
VALUES (120, 25, 'alice', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-30 00:23:42');
INSERT INTO `user_operation_log`
VALUES (121, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-30 00:28:02');
INSERT INTO `user_operation_log`
VALUES (122, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-30 15:09:20');
INSERT INTO `user_operation_log`
VALUES (123, 24, 'users', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-30 18:23:12');
INSERT INTO `user_operation_log`
VALUES (124, 24, 'users', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-31 12:10:53');
INSERT INTO `user_operation_log`
VALUES (125, 1, 'admin', 'LOGIN', '登录成功', 'SUCCESS', '2026-05-31 12:18:22');

SET FOREIGN_KEY_CHECKS = 1;
