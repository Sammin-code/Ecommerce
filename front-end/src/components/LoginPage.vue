<template>
  <div class="loginpage-container">
    <h1>登入介面</h1>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">使用者名稱</label>
        <input type="text" v-model="username" id="username" required />
      </div>
      <div class="form-group">
        <label for="password">密碼</label>
        <input type="password" v-model="password" id="password" required />
      </div>
      <button type="submit" :disabled="loading">
        {{ loading ? "Logging in..." : "登入" }}
      </button>
    </form>
    <p v-if="error" class="error-message">{{ error }}</p>
  </div>
</template>

<script>
import { useToast } from "vue-toastification";
export default {
  setup() {
    const toast = useToast();

    return { toast };
  },
  data() {
    return {
      username: "",
      password: "",
      error: "",
      loading: false,
    };
  },
  created() {
    const token = localStorage.getItem("token");
    if (token) {
      this.toast.info("您已經登入，將導向首頁。", {
        timeout: 3000,
      });
      this.$router.push("/");
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true;
      this.error = "";

      try {
        const response = await this.axios.post(
          "http://localhost:8081/api/users/login",
          {
            username: this.username,
            password: this.password,
          },
        );

        const { token, userId } = response.data;
        console.log("JWT Token:", token);
        localStorage.setItem("token", token);
        localStorage.setItem("userId", userId);

        this.username = "";
        this.password = "";

        this.$router.push("/");
      } catch (error) {
        console.error("Login error:", error);
        this.error = error.response?.data?.message || "登入失敗，請重新再試";
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.loginpage-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}
label {
  margin-bottom: 5px;
}
input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  width: 20%;
  padding: 10px;
  background-color: #2e27e1;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin: 0 auto;
}

.error-message {
  color: red;
}
</style>
