<template>
  <div class="login-container">
    <div class="login-card-wrapper">
      <el-card class="login-card" :body-style="{ padding: '30px' }">
        <template #header>
          <h2 class="login-title">登录</h2>
        </template>
        
        <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-position="top" class="login-form">
          <el-form-item label="用户名" prop="username">
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入用户名"
              class="custom-input"
            ></el-input>
          </el-form-item>
          
          <el-form-item label="密码" prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="请输入密码" 
              @keyup.enter="handleLogin"
              show-password
              class="custom-input"
            ></el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleLogin" :loading="loading" class="login-button">登录</el-button>
          </el-form-item>

          <div class="login-options">
            <span class="forgot-password" @click="goToForgotPassword">忘记密码？</span>
            <span class="dot-separator">•</span>
            <span class="sign-up" @click="goToRegister">注册</span>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        console.log('尝试登录，用户名:', loginForm.username, '密码:', loginForm.password)
        
        // 使用JSON格式发送请求
        const data = {
          username: loginForm.username,
          password: loginForm.password
        }
        
        console.log('发送登录请求，参数:', data)
        const response = await request.post('/user/login-json', data)
        console.log('登录响应:', response)
        
        if (response.code === 0) {
          // 后端返回的用户信息在data字段中
          const userInfo = response.data
          if (userInfo && userInfo.token) {
            localStorage.setItem('token', userInfo.token)
            localStorage.setItem('userInfo', JSON.stringify({
              id: userInfo.id,
              username: userInfo.username
            }))
            
            ElMessage.success('登录成功')
            router.push('/lottery')
          } else {
            ElMessage.error('登录失败：未获取到用户信息')
          }
        } else {
          ElMessage.error(response.message || '登录失败')
        }
      } catch (error) {
        console.error('登录错误：', error)
        ElMessage.error(error.response?.data?.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToForgotPassword = () => {
  router.push('/forgot-password')
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
}

.login-card-wrapper {
  width: 380px;
  filter: drop-shadow(0 10px 20px rgba(0, 0, 0, 0.1));
}

.login-card {
  border-radius: 16px;
  overflow: hidden;
  background-color: white;
  border: none;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.07);
}

.login-card :deep(.el-card__header) {
  padding: 25px 30px;
  border-bottom: none;
  text-align: center;
}

.login-title {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #333;
}

.login-form {
  margin-top: 10px;
}

.login-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
  padding-bottom: 5px;
}

.custom-input :deep(.el-input__inner) {
  height: 45px;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  background-color: #f5f7fa;
  transition: all 0.3s;
  padding-left: 15px;
}

.custom-input :deep(.el-input__inner:focus) {
  border-color: #6366f1;
  background-color: white;
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
}

.login-button {
  width: 100%;
  height: 45px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 1px;
  background: #6366f1;
  border: none;
  margin-top: 10px;
  transition: all 0.3s;
}

.login-button:hover {
  background: #4f46e5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.login-options {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  color: #6366f1;
  font-size: 14px;
}

.forgot-password, .sign-up {
  cursor: pointer;
  transition: color 0.3s;
}

.forgot-password:hover, .sign-up:hover {
  color: #4f46e5;
  text-decoration: underline;
}

.dot-separator {
  margin: 0 10px;
  color: #d1d5db;
}
</style>