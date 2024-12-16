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
- 內建管理員帳號密碼皆為admin
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
   設置資料庫相關
   server.port=8081
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
   spring.datasource.username=你的用戶名
   spring.datasource.password=你的密碼
## 3. 前端設置
1. 下載專案
   ```bash
   git clone https://your-repo-url.git
   cd ecommerce-frontend
2. 安裝依賴
   ```bash
   npm install
4. 啟動開發伺服器
   ```bash
   npm run serve

啟動後，在瀏覽器打開 http://localhost:8080/
   

