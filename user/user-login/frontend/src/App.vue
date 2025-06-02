<template>
  <div class="app-container">
    <el-container v-if="showHeader">
      <el-header>
        <div class="header-content">
          <h2>抽奖系统</h2>
          <div class="user-info" v-if="isLoggedIn">
            <span>{{ username }}</span>
            <el-button type="text" @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
    <router-view v-else></router-view>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const username = ref(localStorage.getItem('username') || '')
const isLoggedIn = computed(() => !!localStorage.getItem('token'))

// 控制是否显示头部，登录和注册页面不显示
const showHeader = computed(() => {
  return !['login', 'register'].includes(route.name)
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}
</script>

<style scoped>
.app-container {
  height: 100vh;
  background-color: #f5f7fa;
}

.el-header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12);
  position: relative;
  z-index: 1;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.el-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}
</style>