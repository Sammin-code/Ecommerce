<template>
  <div class="user-profile">
    <h1 class="title">用戶資料</h1>
    <div class="user-info">
      <p v-if="user.username"><strong>使用者名稱:</strong> {{ user.username }}</p>
      <p v-if="user.email"><strong>Email:</strong> {{ user.email }}</p>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  data() {
    return {
      user: {}
    };
  },
  async created() {
    const userId = this.$route.params.userId;
    console.log(userId);
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('無法獲取到 token，請重新登入');
      return;
    }
    try {
      const response = await axios.get(`http://localhost:8081/api/users/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}` 
        }
      });
      this.user = response.data; 
    } catch (error) {
      console.error('獲取訂單失敗:', error);
    }
  }
};
</script>

<style scoped>
.user-profile {
  max-width: 500px;
  margin: 40px auto;
  padding: 20px;
  background-color: #fff; 
  border-radius: 10px; 
  border: 1px solid #e0e0e0; 
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05); 
}

.title {
  text-align: center;
  color: #333; 
  font-size: 28px; 
  margin-bottom: 20px;
}

.user-info {
  padding: 15px;
}

.user-info p {
  font-size: 18px; 
  margin: 10px 0;
  color: #555; 
}

.user-info strong {
  color: #000; 
  font-weight: 600; 
}

p {
  line-height: 1.5; 
}
</style>
