<template>
  <div class="app-container">
    <el-container v-if="showHeader">
      <el-header v-if="!isLotteryPage">
        <div class="header-content">
          <h2>抽奖系统</h2>
          <div class="user-info" v-if="isLoggedIn">
            <span>{{ username }}</span>
            <el-button type="text" @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      <el-main :class="{ 'lottery-main': isLotteryPage }">
        <!-- 抽奖页面的浮动头部 -->
        <div v-if="isLotteryPage" class="floating-header">
          <div class="header-content">
            <h2>抽奖系统</h2>
            <div class="user-info" v-if="isLoggedIn">
              <span>{{ username }}</span>
              <el-button type="text" @click="handleLogout">退出登录</el-button>
            </div>
          </div>
        </div>
        <router-view></router-view>
      </el-main>
    </el-container>
    <router-view v-else></router-view>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const username = computed(() => userStore.username)
const isLoggedIn = computed(() => userStore.isLoggedIn)

// 控制是否显示头部，登录和注册页面不显示
const showHeader = computed(() => {
  return !['login', 'register'].includes(route.name)
})

// 判断是否为抽奖页面
const isLotteryPage = computed(() => {
  return route.name === 'lottery'
})

const handleLogout = () => {
  userStore.logout()
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
  gap: 8px;
}

.el-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 抽奖页面特殊样式 */
.lottery-main {
  max-width: none !important;
  margin: 0 !important;
  padding: 0 !important;
  height: 100vh;
  overflow: hidden;
}

.floating-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  align-items: center;
}

.floating-header .header-content {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  padding: 0 20px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.floating-header h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
}

.floating-header .user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #333;
}
</style>