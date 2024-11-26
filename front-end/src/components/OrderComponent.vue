<template>
  <div>
    <h1>我的訂單</h1>
    <ul v-if="orders.length">
      <li v-for="order in orders" :key="order.id">
        <h2>訂單號碼: {{ order.id }}</h2>
        <p>總金額: {{ order.totalAmount }}</p>
        <p>訂單日期: {{ new Date(order.orderDate).toLocaleDateString() }}</p>

        <h3>訂單項目:</h3>
        <ul>
          <li v-for="item in order.orderItems" :key="item.id">
            <p>產品名稱: {{ item.product.name }}</p>
            <p>數量: {{ item.quantity }}</p>
            <p>價格: {{ item.price }}</p>
          </li>
        </ul>
      </li>
    </ul>
    <p v-else>沒有找到訂單。</p>
  </div>
</template>
<script>
import axios from "axios";
import { useToast } from "vue-toastification";

export default {
  setup() {
    const toast = useToast();
    return { toast };
  },
  data() {
    return {
      orders: [],
      isAdmin: false, // 用來檢查是否是管理員
    };
  },
  async created() {
    const token = localStorage.getItem("token");

    if (!token) {
      this.toast.error("請重新登入");
      console.error("無法獲取到 token");
      return;
    }

    try {
      // 從 localStorage 獲取 Token
      const token = localStorage.getItem("token");

      if (!token) {
        this.toast.error("請先登入");
        return;
      }

      // 發送請求以獲取用戶資訊
      const userInfoResponse = await axios.get(
        "http://localhost:8081/api/users/me",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      const { id: userId, role: userRole } = userInfoResponse.data;

      console.log(userId, userRole);

      // 判斷是否是 Admin
      this.isAdmin = userRole === "ROLE_ADMIN";

      // 根據角色獲取訂單
      const ordersResponse = await axios.get(
        this.isAdmin
          ? "http://localhost:8081/api/orders/all"
          : `http://localhost:8081/api/orders/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      this.orders = ordersResponse.data;
    } catch (error) {
      this.toast.error("獲取訂單失敗");
      console.error("獲取訂單失敗:", error);
    }
  },
};
</script>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}

li {
  margin: 10px 0;
}

h1 {
  color: #333;
}
</style>
