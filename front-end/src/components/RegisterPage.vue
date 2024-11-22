<template>
  <div class="registerpage-container">
    <h1>註冊頁面</h1>
    <form @submit.prevent="handleRegister">
      <div>
        <label for="username">使用者名稱:</label>
        <input type="text" v-model="username" id="username" required />
      </div>
      <div>
        <label for="password">密碼:</label>
        <input type="password" v-model="password" id="password" required />
      </div>
      <div>
        <label for="email">Email:</label>
        <input type="email" v-model="email" id="email" required />
      </div>
      <button type="submit">註冊</button>
    </form>
    <p v-if="error">{{ error }}</p>
  </div>
</template>

<script>
import axios from 'axios';
import { useToast } from 'vue-toastification';

export default {
  data() {
    return {
      username: '',
      password: '',
      email: '',
      error: ''
    };
  },
  methods: {
    async handleRegister() {
      const toast = useToast();
      try {
        await axios.post('http://localhost:8081/api/users/register', {  
          username: this.username,
          password: this.password,
          email: this.email
        });
        toast.success('註冊成功！');
        this.$router.push('/login');
      } catch (error) {
        this.error = error.response && error.response.data.message 
                     ? error.response.data.message 
                     : '註冊失敗，請重新嘗試.';
      }
    }
  }
};
</script>

<style scoped>
.registerpage-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}
form div {
  margin-bottom: 15px;
}
label {
  display: block;
  margin-bottom: 5px;
}
input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #0056b3;
}
</style>
