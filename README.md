# E-commerce 系統

## 簡介
本專案是一個全端電子商務系統，提供用戶註冊、商品瀏覽、購物車管理與訂單功能。前端使用 **Vue.js**，後端基於 **Spring Boot** 開發。

---

# 功能

## 用戶功能
- 用戶註冊與登入（JWT 認證）
- 商品瀏覽與搜尋
- 購物車管理
- 訂單確認與追蹤

## 管理員功能
- 商品管理（新增、修改、刪除）
- 訂單管理

---
# 安裝與啟動

## 1. 環境需求
- **後端**：Java 17, Maven 3.6+, MySQL
- **前端**：Node.js 16+, Vue CLI

## 2. 後端設置
1. 配置 `application.properties`：
   ```properties
   server.port=8081
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
   spring.datasource.username=你的用戶名
   spring.datasource.password=你的密碼
