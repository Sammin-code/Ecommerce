<template>
  <div>
    <nav class="navbar">
      <button @click="goToHome">首頁</button>
      <button @click="goToProducts">瀏覽商品</button>
      <button @click="goToCart">查看購物車</button>
      <button @click="goToOrders">查看訂單</button>
      <button @click="goToUsers">用戶資料</button>
      <button @click="goToLogin">登入介面</button>
      <button @click="goToRegister">註冊</button>
      <button @click="logout">登出</button>
    </nav>

    <router-view></router-view>
  </div>
</template>

<script>
import { useToast } from "vue-toastification";
export default {
  data() {
    return {
      toast: null,
    };
  },
  mounted() {
    this.toast = useToast();
  },

  methods: {
    goToHome() {
      this.$router.push("/");
    },
    goToProducts() {
      this.$router.push("/products");
    },
    goToCart() {
      const userId = localStorage.getItem("userId");
      if (userId) {
        this.$router.push(`/cart/${userId}`);
      } else {
        this.toast.error("未能獲取用戶ID，請先登入");
        this.$router.push("/login");
      }
    },
    goToOrders() {
      const userId = localStorage.getItem("userId");
      if (userId) {
        this.$router.push(`/orders/${userId}`);
      } else {
        this.toast.error("未能獲取用戶ID，請先登入");
        this.$router.push("/login");
      }
    },
    goToUsers() {
      const userId = localStorage.getItem("userId");
      if (userId) {
        this.$router.push(`/users/${userId}`);
      } else {
        this.toast.error("未能獲取用戶ID，請先登入");
        this.$router.push("/login");
      }
    },
    goToLogin() {
      this.$router.push("/login");
    },
    goToRegister() {
      this.$router.push("/register");
    },
    logout() {
      localStorage.removeItem("token");
      this.toast.success("成功登出！");
      this.$router.push("/login");
    },
  },
};
</script>

<style>
.navbar {
  display: flex;
  justify-content: center;
  padding: 10px;
  background-color: #f8f9fa;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}

button {
  margin: 0 10px;
  padding: 10px 20px;
  font-size: 18px;
  cursor: pointer;
  background-color: #78aae0;
  color: #fff;
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
}
</style>
