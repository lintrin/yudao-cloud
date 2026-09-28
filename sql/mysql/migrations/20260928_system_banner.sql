-- MySQL 8：先执行此脚本，再部署包含 Banner 功能的 system 服务。
CREATE TABLE `system_banner` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `pic_url` varchar(1024) NOT NULL COMMENT '图片地址',
  `url` varchar(1024) NOT NULL DEFAULT '' COMMENT '跳转链接',
  `position` int NOT NULL COMMENT '展示位置，1 为首页',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序，越小越靠前',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态，0 启用，1 停用',
  `memo` varchar(500) NOT NULL DEFAULT '' COMMENT '内部备注',
  `creator` varchar(64) DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_banner_tenant_position_status` (`tenant_id`, `position`, `status`, `deleted`, `sort`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统 Banner';
