<template>
  <div>
    <h1>購物車</h1>
    <ul v-if="cartitems.length">
      <li v-for="item in cartitems" :key="item.id">
        產品: {{ item.product.name }} - 數量: {{ item.quantity || 0 }} - 價錢:
        {{ item.product.price }}
        <button @click="removeFromCart(item.product.id)">移除商品</button>
        <button @click="confirmOrder(item.product.id)">確認訂單</button>
      </li>
      <button @click="removeAllFromCart">一鍵移除所有商品</button>
    </ul>
    <p v-else>當前購物車為空</p>
  </div>
</template>

<script>
import axios from "axios";
import { useToast } from "vue-toastification";

export default {
  data() {
    return {
      cartitems: [],
      toast: null,
      userId: this.$route.params.userId || localStorage.getItem("userId"),
    };
  },
  created() {
    this.toast = useToast();
    const userId = this.$route.params.userId;
    const token = localStorage.getItem("token");

    if (!token) {
      console.error("無法獲取到 token，請重新登入");
      return;
    }

    if (!this.isCartCleared) {
      axios
        .get(`http://localhost:8081/api/cart/${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((response) => {
          this.cartitems = response.data.cartItems || [];
        })
        .catch((error) => {
          console.error("獲取訂單失敗:", error);
        });
    }
  },
  methods: {
    async removeFromCart(productId) {
      const token = localStorage.getItem("token");

      try {
        await axios.delete(
          `http://localhost:8081/api/cart/remove/${productId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          },
        );
        this.cartitems = this.cartitems.filter(
          (item) => item.product.id !== productId,
        );
        this.toast.success("商品已成功移除");
      } catch (error) {
        console.error("移除商品失敗:", error);
        this.toast.error("移除商品失敗");
      }
    },
    async removeAllFromCart() {
      const token = localStorage.getItem("token");

      try {
        await axios.delete("http://localhost:8081/api/cart/removeAll", {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.cartitems = [];
        this.isCartCleared = true;
        localStorage.setItem("isCartCleared", JSON.stringify(true));
        this.toast.success("所有商品已成功移除");
      } catch (error) {
        console.error("移除所有商品失敗:", error);
        this.toast.error("移除所有商品失敗");
      }
    },
    async confirmOrder(productId) {
      const token = localStorage.getItem("token");
      const userId = localStorage.getItem("userId");

      try {
        const response = await axios.post(
          `http://localhost:8081/api/orders/confirm/${this.userId}/${productId}`,
          { productId, userId },
          {
            headers: { Authorization: `Bearer ${token}` },
          },
        );
        console.log("訂單確認成功:", response.data);
        this.cartitems = this.cartitems.filter(
          (item) => item.product.id !== productId,
        );
        this.toast.success("訂單已成功確認");
      } catch (error) {
        console.error("訂單確認失敗:", error);
        this.toast.error("訂單確認失敗");
      }
    },
  },
};
</script>

<style scoped>
.cart-container {
  width: 80%;
  margin: auto;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  color: #343a40;
}

ul {
  list-style: none;
  padding: 0;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffffff;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 4px;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
}

.product-info span {
  margin-right: 20px;
  font-size: 16px;
  color: #495057;
}

.actions button {
  margin-left: 10px;
  padding: 8px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.actions button:hover {
  background-color: #0056b3;
}

p {
  text-align: center;
  font-size: 18px;
  color: #6c757d;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>
