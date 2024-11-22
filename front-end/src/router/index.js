import { createRouter, createWebHistory } from 'vue-router';
import { useToast } from 'vue-toastification';
import HomePage from '../components/HomePage.vue';
import LoginPage from '../components/LoginPage.vue';
import ShoppingCart from '@/components/ShoppingCart.vue';
import RegisterPage from '@/components/RegisterPage.vue';
import OrderComponent from '@/components/OrderComponent.vue';
import ProductPage from '@/components/ProductPage.vue';
import UserPage from '@/components/UserPage.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage
  },
  {
    path: '/cart/:userId',
    name: 'Cart',
    component: ShoppingCart,
    meta: { requiresAuth: true }
  },
  {
    path: '/orders/:userId',
    name: 'Orders',
    component: OrderComponent,
    meta: { requiresAuth: true }
  },
  {
    path: '/products',
    name: 'Products',
    component: ProductPage
  },
  {
    path: '/users/:userId',
    name: 'Users',
    component: UserPage,
    meta: { requiresAuth: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

const toast = useToast();

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      toast.error('您需要登入才能查看此頁面');
      next('http://localhost:8081/api/users/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
