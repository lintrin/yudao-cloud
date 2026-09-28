# System Banner

## 范围与数据

Banner 由 system 管理，不依赖未启用的 mall/promotion。存储于 `system_banner`，通过现有租户拦截器隔离，删除采用逻辑删除。管理员在所属租户内共享配置。

字段：`title`（必填，最多 100 字）、`picUrl`（必填，最多 1024 字）、`url`（可选，最多 1024 字）、`position`（正整数，1 为首页，其他值由接入方约定）、`sort`（非负整数）、`status`（0 启用，1 停用）、`memo`（内部备注，最多 500 字）。图片上传复用 infra 文件接口，此处保存图片地址。

`url` 允许站内路径或外部链接，由客户端按其支持的协议解析并检查；服务端不抓取该地址。App 响应只包含 id、title、picUrl、url、position、sort。

## 接口

使用现有 CommonResult/PageResult 响应格式和默认 API 前缀：

| 方法 | 路径 | 权限/用途 |
| --- | --- | --- |
| POST | `/admin-api/system/banner/create` | `system:banner:create` |
| PUT | `/admin-api/system/banner/update` | `system:banner:update`，请求增加必填 id |
| DELETE | `/admin-api/system/banner/delete?id=1` | `system:banner:delete` |
| GET | `/admin-api/system/banner/get?id=1` | `system:banner:query` |
| GET | `/admin-api/system/banner/page` | `system:banner:query`，支持 title、position、status、pageNo、pageSize |
| GET | `/app-api/system/banner/list?position=1` | 免登录，仍须提供有效 `tenant-id` 请求头 |

新增请求示例：

```json
{
  "title": "首页活动",
  "picUrl": "https://example.com/banner.png",
  "url": "/pages/activity/index",
  "position": 1,
  "sort": 0,
  "status": 0,
  "memo": "首页运营位"
}
```

修改是全量修改：提供全部必填字段，省略 url/memo 时清空旧值。启停和调整排序均使用修改接口；并发修改采用最后写入者覆盖。创建没有业务去重键，不应自动重试创建请求。

列表按 sort 升序、id 降序排列。App 只查询启用且未删除的数据；管理分页可以查看停用项。详情不存在（含其他租户数据）返回空数据，修改/删除不存在的数据返回 `1002029000`。重复删除同样返回不存在。

## 部署与回滚

1. 当前应用配置使用 MySQL。首次安装或升级，均需在目标数据库手工执行 `sql/mysql/migrations/20260928_system_banner.sql`；项目不会自动执行此脚本。脚本不使用 DROP/IF NOT EXISTS，以便重复执行或同名表冲突时明确失败。
2. 部署 system 服务或装配它的 yudao-server，不需要启用 mall。
3. 在现有菜单管理中配置上表四个权限标识，将权限分配给需要的角色；使用租户套餐时同步配置套餐菜单范围。仓库没有前端 workspace，本次不创建指向不存在前端页面的菜单路由。
4. 回滚应用时可保留新增表。确需删除时先备份 Banner 数据，再执行 `sql/mysql/migrations/20260928_system_banner_rollback.sql`，并撤销为 Banner 新增的菜单及授权。

本次只提供 MySQL 迁移。其他生产方言需要对应 DDL 后才能部署；H2 测试不代表生产 MySQL 已验证。SQL 未自动应用到任何业务数据库。

## 验证

使用 JDK 25，在仓库根目录执行：

```sh
mvn -pl yudao-module-system/yudao-module-system-server -am -DskipTests compile
mvn -pl yudao-module-system/yudao-module-system-server -am -Dtest=BannerServiceImplTest -Dsurefire.failIfNoSpecifiedTests=false test
```

测试使用模块 H2 建表和清理脚本，并装配真实租户 SQL 拦截器，覆盖增改删、可选字段清空、分页筛选、启停和位置过滤、稳定排序、逻辑删除、跨租户读写隔离以及非法参数。
