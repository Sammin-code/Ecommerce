import { createApp } from 'vue';
import App from './App.vue';
import axios from 'axios';
import VueAxios from 'vue-axios';
import router from './router';
import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';

const app = createApp(App);


axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);


axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      console.error('未登入，跳轉到登入頁面');
      localStorage.removeItem('token');
      router.push('/login');
    }
    return Promise.reject(error);
  }
);

app.use(VueAxios, axios);
app.use(router);
app.use(Toast);
app.mount('#app');


