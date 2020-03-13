/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : book

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 13/03/2020 17:28:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action`  (
  `action_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `token` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作token',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回信息',
  `ip` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作ip地址',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `username` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户名',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`action_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `adminname` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员账号',
  `password` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员密码',
  `salt` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐值',
  `role` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role`  (
  `admin_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员角色ID',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`admin_role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES (1, '超级管理员', '2019-11-13 14:43:12', '2019-11-13 14:43:12', '正常');

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `book_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '书籍ID',
  `name` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍名称',
  `head_image` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍封面',
  `introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '简介',
  `book_explain` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '说明',
  `author` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `publiser` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布人',
  `browse` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '浏览人数',
  `praise` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '点赞人数',
  `book_check` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '下架' COMMENT '审核状态：上架，下架',
  `book_type` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `upload_url` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传地址',
  `book_status` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍状态：连载，完结',
  `serial_num` int(11) NULL DEFAULT 0 COMMENT '章回数',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, '测试', '/outimg/admin/bookHead/c1066312-7180-4cda-875f-aa1bc3ed5140.jpg', '一群小女孩的故事', '一群小女孩的故事', 'admin', 'admin', '4', '0', '上架', '章回', '', '连载', 4, '2019-11-18 13:40:26', '2020-03-10 09:15:12', '正常');
INSERT INTO `book` VALUES (2, '测试pdf', '/outimg/admin/bookHead/b05edd18-e69e-4f8f-a407-4773a362347d.jpg', '测试pdf', '测试pdf', 'admin', 'admin', '14', '1', '上架', 'pdf', '/outimg/admin/book/pdf/adb01172-82fa-49bf-8af6-bdf08fc530e7.pdf', '完结', 0, '2019-11-08 11:46:22', '2020-03-07 01:15:09', '正常');
INSERT INTO `book` VALUES (3, '测试测试', '/outimg/admin/bookHead/test.jpg', '这是测试简介', '这是测试说明', 'admin', 'admin', '0', '0', '上架', '章回', NULL, '连载', 0, '2019-12-13 14:34:00', '2020-03-11 17:15:06', '正常');
INSERT INTO `book` VALUES (4, '刀剑圣域', '/outimg/admin/bookHead/test.jpg', '这是测试简介', '这是测试说明', 'admin', 'admin', '0', '0', '上架', '章回', NULL, '连载', 0, '2019-12-14 08:08:45', '2020-03-11 17:15:02', '正常');
INSERT INTO `book` VALUES (5, '测试', '/outimg/admin/bookHead/066c56d9-5a2c-4dbf-b431-5e108f76becf.png', '测试', '测试', 'admin', 'admin', '0', '0', '下架', 'pdf', '/outimg/admin/book/pdf/e7b55233-dc69-48fc-9c6a-e08f2ea57a8b.pdf', '完结', 0, '2020-02-29 08:58:55', '2020-03-11 17:15:04', '正常');

-- ----------------------------
-- Table structure for book_label
-- ----------------------------
DROP TABLE IF EXISTS `book_label`;
CREATE TABLE `book_label`  (
  `book_label_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '书籍标签ID',
  `book_id` int(11) NULL DEFAULT NULL COMMENT '书籍ID',
  `label_id` int(11) NULL DEFAULT NULL COMMENT '标签ID',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`book_label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_label
-- ----------------------------
INSERT INTO `book_label` VALUES (1, 1, 1, '2019-11-20 13:40:26', '2019-11-20 13:40:26', '正常');
INSERT INTO `book_label` VALUES (2, 2, 1, '2019-11-22 11:46:22', '2019-11-22 11:46:22', '正常');
INSERT INTO `book_label` VALUES (3, 1, 2, '2019-12-09 17:08:40', '2019-12-09 17:08:40', '正常');
INSERT INTO `book_label` VALUES (4, 5, 3, '2020-02-29 08:58:55', '2020-02-29 08:58:55', '正常');
INSERT INTO `book_label` VALUES (5, 5, 4, '2020-02-29 08:58:55', '2020-02-29 08:58:55', '正常');

-- ----------------------------
-- Table structure for book_serial
-- ----------------------------
DROP TABLE IF EXISTS `book_serial`;
CREATE TABLE `book_serial`  (
  `serial_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '章回ID',
  `book_id` int(11) NULL DEFAULT NULL COMMENT '书籍ID',
  `serial_num` int(11) NULL DEFAULT NULL COMMENT '章回数',
  `title` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '章回名',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '章回内容',
  `browse` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '浏览人数',
  `serial_check` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '未通过' COMMENT '审核状态:通过，未通过',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态:正常，删除',
  PRIMARY KEY (`serial_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_serial
-- ----------------------------
INSERT INTO `book_serial` VALUES (1, 1, 1, '天选之子', '<p>在此输入章回内容…</p><p><br></p>', '0', '通过', '2019-11-20 09:46:23', '2019-11-22 17:48:10', '正常');
INSERT INTO `book_serial` VALUES (2, 1, 2, '菠菜王', '<p>在此输入章回内容…</p><p>123</p>', '0', '通过', '2019-11-20 09:48:02', '2019-11-22 17:42:10', '正常');
INSERT INTO `book_serial` VALUES (3, 1, 3, '最终', '<p>在此输入章回内容…</p><p><br></p>', '0', '通过', '2019-11-20 09:53:57', '2019-11-22 17:42:10', '正常');
INSERT INTO `book_serial` VALUES (4, 1, 4, '番外篇', '<p>在此输入章回内容<br>…番外</p><p><br></p>', '0', '通过', '2019-11-22 10:11:26', '2019-11-22 17:55:10', '正常');
INSERT INTO `book_serial` VALUES (5, 3, 1, '天选之子', '<p>在此输入章回内容…0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000</p>', '0', '通过', '2019-12-14 14:34:00', '2019-12-14 15:51:55', '正常');
INSERT INTO `book_serial` VALUES (6, 3, 2, '菠菜王', '<p>在此输入章</p><p>回内容…123</p>', '0', '通过', '2019-12-14 14:34:00', '2019-12-14 15:51:57', '正常');
INSERT INTO `book_serial` VALUES (7, 3, 3, '最终', '<p>在此输入章回内容…</p>', '0', '通过', '2019-12-14 14:34:00', '2019-12-14 15:51:59', '正常');
INSERT INTO `book_serial` VALUES (8, 3, 4, '番外篇', '<p>在此输入章回内容</p><p>…番外</p>', '0', '通过', '2019-12-14 14:34:00', '2019-12-14 15:52:01', '正常');
INSERT INTO `book_serial` VALUES (9, 4, 1, '天选之子', '<p>在此输入章回内容…0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000<br></p>', '0', '通过', '2019-12-14 16:08:45', '2019-12-14 16:09:54', '正常');
INSERT INTO `book_serial` VALUES (10, 4, 2, '菠菜王', '<p>在此输入章<br></p><p>回内容…123<br></p>', '0', '通过', '2019-12-14 16:08:45', '2019-12-14 16:09:54', '正常');
INSERT INTO `book_serial` VALUES (11, 4, 3, '最终', '<p>在此输入章回内容…<br></p>', '0', '通过', '2019-12-14 16:08:45', '2019-12-14 16:09:54', '正常');
INSERT INTO `book_serial` VALUES (12, 4, 4, '番外篇', '<p>在此输入章回内容<br></p><p>…番外<br></p>', '0', '通过', '2019-12-14 16:08:45', '2019-12-14 16:09:54', '正常');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `collect_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收藏书籍id',
  `book_id` int(11) NULL DEFAULT NULL COMMENT '书籍id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (11, 2, 1, '2020-03-11 06:15:16', '2020-03-11 06:15:16', '删除');
INSERT INTO `collect` VALUES (12, 2, 1, '2020-03-11 06:15:21', '2020-03-11 06:15:21', '删除');
INSERT INTO `collect` VALUES (13, 2, 1, '2020-03-11 06:15:25', '2020-03-11 06:15:25', '删除');
INSERT INTO `collect` VALUES (14, 2, 1, '2020-03-11 06:16:47', '2020-03-11 06:16:47', '删除');
INSERT INTO `collect` VALUES (15, 2, 1, '2020-03-11 06:16:50', '2020-03-11 06:16:50', '删除');
INSERT INTO `collect` VALUES (16, 2, 1, '2020-03-11 06:17:36', '2020-03-11 06:17:36', '删除');
INSERT INTO `collect` VALUES (17, 2, 1, '2020-03-11 06:17:39', '2020-03-11 06:17:39', '删除');
INSERT INTO `collect` VALUES (18, 2, 1, '2020-03-11 06:23:26', '2020-03-11 06:23:26', '删除');
INSERT INTO `collect` VALUES (19, 2, 1, '2020-03-11 06:23:30', '2020-03-11 06:23:30', '删除');
INSERT INTO `collect` VALUES (20, 2, 1, '2020-03-11 06:24:18', '2020-03-11 06:24:18', '删除');
INSERT INTO `collect` VALUES (21, 2, 1, '2020-03-11 06:27:49', '2020-03-11 06:27:49', '删除');
INSERT INTO `collect` VALUES (22, 2, 1, '2020-03-11 14:28:05', '2020-03-11 14:28:05', '正常');
INSERT INTO `collect` VALUES (23, 3, 1, '2020-03-11 16:34:33', '2020-03-11 16:34:33', '正常');

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label`  (
  `label_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名',
  `introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '简介',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of label
-- ----------------------------
INSERT INTO `label` VALUES (1, '温馨', '', '2019-11-20 13:39:37', '2019-11-20 13:39:37', '正常');
INSERT INTO `label` VALUES (2, '教育', '', '2019-12-02 13:59:35', '2019-12-02 13:59:35', '正常');
INSERT INTO `label` VALUES (3, '感人', '', '2019-12-02 13:59:43', '2019-12-02 13:59:43', '正常');
INSERT INTO `label` VALUES (4, '文学', '', '2019-12-02 13:59:57', '2019-12-02 13:59:57', '正常');
INSERT INTO `label` VALUES (5, '玄幻', '', '2019-12-02 14:00:25', '2019-12-02 14:00:25', '正常');
INSERT INTO `label` VALUES (6, '仙侠', '', '2019-12-02 14:00:45', '2019-12-02 14:00:45', '正常');
INSERT INTO `label` VALUES (7, '爱国', '', '2019-12-02 14:01:03', '2019-12-02 14:01:03', '正常');
INSERT INTO `label` VALUES (8, '离别', '', '2019-12-02 14:01:09', '2019-12-02 14:01:09', '正常');
INSERT INTO `label` VALUES (9, '数学', '', '2019-12-02 14:01:16', '2019-12-02 14:01:16', '正常');
INSERT INTO `label` VALUES (10, '英语', '', '2019-12-02 14:01:23', '2019-12-02 14:01:23', '正常');
INSERT INTO `label` VALUES (11, '化学', '', '2019-12-02 14:01:53', '2019-12-02 14:01:53', '正常');

-- ----------------------------
-- Table structure for likes
-- ----------------------------
DROP TABLE IF EXISTS `likes`;
CREATE TABLE `likes`  (
  `like_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '点赞id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `book_id` int(11) NULL DEFAULT NULL COMMENT '点赞id',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`like_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rotation
-- ----------------------------
DROP TABLE IF EXISTS `rotation`;
CREATE TABLE `rotation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `url` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `image_path` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `num` int(11) NULL DEFAULT 0 COMMENT '排序',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态：正常，删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rotation
-- ----------------------------
INSERT INTO `rotation` VALUES (1, '测试', 'http://www.baidu.com', '/outimg/admin/rotation/eb3c2b05-f540-4d5b-9d11-cd2e9d89d82b.jpg', 1, '2019-12-12 05:47:04', '2019-12-12 05:47:04', '正常');
INSERT INTO `rotation` VALUES (2, '测试1', 'https://www.qidian.com/', '/outimg/admin/rotation/acb8a12d-2f5d-414c-ac3f-9012fe22f156.jpg', 2, '2019-12-12 13:54:11', '2019-12-12 13:54:11', '正常');
INSERT INTO `rotation` VALUES (3, '测试2', 'http://www.zongheng.com/', '/outimg/admin/rotation/27a803f4-083d-4142-b1bd-bbcc4ca1b495.jpg', 3, '2019-12-12 13:55:33', '2019-12-12 13:55:33', '正常');
INSERT INTO `rotation` VALUES (4, '测试', 'http://www.baidu.com', '/outimg/admin/rotation/eb3c2b05-f540-4d5b-9d11-cd2e9d89d82b.jpg', 3, '2019-12-13 17:26:57', '2019-12-14 01:46:57', '删除');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态：正常，删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'fujing', 'fujing', '2020-03-13 17:20:00', '2020-03-13 17:20:00', NULL);
INSERT INTO `user` VALUES (2, 'fujing1', 'fujing1', '2020-03-13 17:22:57', '2020-03-13 17:22:57', NULL);

SET FOREIGN_KEY_CHECKS = 1;
