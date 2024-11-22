<template>
  <div>
    <div class="product-header">
      <h1>商品</h1>
    </div>

    <div v-if="isAdmin" class="add-product-section">
      <h2>新增商品</h2>
      <form @submit.prevent="addProduct">
        <div class="form-group">
          <label for="productName">商品名稱：</label>
          <input
            id="productName"
            v-model="newProduct.name"
            placeholder="商品名稱"
            required
          />
        </div>
        <div class="form-group">
          <label for="productDescription">商品描述：</label>
          <input
            id="productDescription"
            v-model="newProduct.description"
            placeholder="商品描述"
          />
        </div>
        <div class="form-group">
          <label for="productPrice">價格：</label>
          <input
            id="productPrice"
            v-model="newProduct.price"
            type="number"
            min="1"
            step="1"
            placeholder="價格"
            required
          />
        </div>
        <div class="form-group">
          <label for="productQuantity">數量：</label>
          <input
            id="productQuantity"
            v-model="newProduct.quantity"
            type="number"
            min="1"
            step="1"
            placeholder="數量"
            required
          />
        </div>
        <button type="submit">新增商品</button>
      </form>
    </div>

    <ul class="product-list">
      <li v-for="product in products" :key="product.id">
        <p>商品名稱: {{ product.name }}</p>
        <p>商品描述: {{ product.description }}</p>
        <p>價格: {{ product.price }}</p>
        <p>數量： {{ product.quantity }}</p>
        <button
          v-if="!isAdmin && isLoggedIn"
          @click="openQuantityModal(product)"
        >
          加到購物車
        </button>
        <button v-if="isAdmin" @click="deleteProduct(product.id)">
          刪除商品
        </button>
      </li>
    </ul>

    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <h2>選擇數量</h2>
        <label for="quantity">數量：</label>
        <input type="number" v-model="selectedQuantity" min="1" id="quantity" />
        <button @click="addToCart(selectedProduct.id)">確認</button>
      </div>
    </div>

    <p v-if="products.length === 0">暫無商品。</p>
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
      products: [],
      newProduct: {
        name: "",
        description: "",
        price: 0,
        quantity: 0,
      },
      isAdmin: false,
      isLoggedIn: false,
      isModalOpen: false,
      selectedProduct: null,
      selectedQuantity: 1,
    };
  },
  created() {
    console.log("Component created, checking admin role...");
    this.fetchProducts();
    this.checkLoginStatus();
    this.checkAdminRole();
  },
  methods: {
    fetchProducts() {
      axios
        .get("http://localhost:8081/api/products")
        .then((response) => {
          this.products = response.data;
        })
        .catch((error) => {
          console.error("Error fetching products:", error);
        });
    },

    checkLoginStatus() {
      const token = localStorage.getItem("token");
      this.isLoggedIn = !!token;
    },

    checkAdminRole() {
      const token = localStorage.getItem("token");
      console.log("Token:", token);
      if (!token) return;

      axios
        .get("/api/products/me", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((response) => {
          const roles = response.data.roles;
          console.log("User roles:", roles);
          this.isAdmin = roles.includes("ROLE_ADMIN");
        })
        .catch((error) => {
          console.error("Error response data:", error.response.data);
          console.error("Error response status:", error.response.status);
        });
    },
    openQuantityModal(product) {
      this.selectedProduct = product;
      this.selectedQuantity = 1;
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
    },

    addProduct() {
      if (!this.isAdmin) {
        this.toast.error("您沒有權限新增商品。");
        return;
      }

      axios
        .get(`/api/products/checkName?name=${this.newProduct.name}`)
        .then((response) => {
          if (response.data.exists) {
            this.toast.error("商品名稱已存在，請使用不同的名稱。");
          } else {
            axios
              .post("/api/products/add", this.newProduct)
              .then((response) => {
                const newProduct = response.data;
                this.toast.success("商品新增成功，產品為：" + newProduct.name);
                this.fetchProducts();
                this.newProduct = {
                  name: "",
                  description: "",
                  price: 0,
                  quantity: 0,
                };
              })
              .catch((error) => {
                console.error("Error adding product:", error);
                this.toast.error("新增商品失敗，請稍後再試。");
              });
          }
        })
        .catch((error) => {
          if (error.response && error.response.status === 409) {
            this.toast.error("商品名稱已存在，請使用不同的名稱。");
          } else {
            this.toast.error("檢查商品名稱失敗，請稍後再試。");
          }
        });
    },

    deleteProduct(productId) {
      if (!this.isAdmin) {
        this.toast.error("您沒有權限刪除商品。");
        return;
      }

      axios
        .delete(`/api/products/delete/${productId}`)
        .then(() => {
          this.toast.success("商品已刪除");
          this.fetchProducts();
        })
        .catch((error) => {
          console.error("Error deleting product:", error);
          this.toast.error("刪除商品失敗，請稍後再試。");
        });
    },

    addToCart(productId) {
      const token = localStorage.getItem("token");
      const userId = localStorage.getItem("userId");

      if (!token) {
        this.toast.error("請先登入後再加入購物車。");
        this.$router.push("/login");
        return;
      }

      axios
        .post(
          `/api/cart/add/${userId}`,
          { productId, quantity: this.selectedQuantity },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        )
        .then(() => {
          this.toast.success("已添加到購物車");
          this.closeModal();
        })
        .catch((error) => {
          console.error("Error adding product to cart:", error);
          this.toast.error("添加至購物車失敗，請稍後再試。");
        });
    },
  },
};
</script>

<style scoped>
.product-header {
  text-align: center;
  margin-bottom: 20px;
}

.add-product-section {
  margin-bottom: 30px;
  border: 1px solid #ccc;
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 200px;
  height: 30px;
  margin-bottom: 10px;
  padding: 5px;
}

.product-list {
  list-style: none;
  padding: 0;
}

.product-list li {
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
  padding-bottom: 15px;
}
</style>
