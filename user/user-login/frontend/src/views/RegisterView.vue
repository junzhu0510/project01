<template>
  <div class="register-container">
    <div class="register-card-wrapper">
      <el-card class="register-card" :body-style="{ padding: '30px' }">
        <template #header>
          <h2 class="register-title">注册</h2>
        </template>
        
        <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-position="top" class="register-form">
          <el-form-item label="用户名" prop="username">
            <el-input 
              v-model="registerForm.username" 
              placeholder="请输入用户名"
              class="custom-input"
            ></el-input>
          </el-form-item>
          
          <el-form-item label="手机号" prop="phone">
            <el-input 
              v-model="registerForm.phone" 
              placeholder="请输入手机号"
              class="custom-input"
            ></el-input>
          </el-form-item>
          
          <el-form-item label="密码" prop="password">
            <el-input 
              v-model="registerForm.password" 
              type="password" 
              placeholder="请输入密码"
              show-password
              class="custom-input"
            ></el-input>
          </el-form-item>
          
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input 
              v-model="registerForm.confirmPassword" 
              type="password" 
              placeholder="请再次输入密码" 
              @keyup.enter="handleRegister"
              show-password
              class="custom-input"
            ></el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleRegister" :loading="loading" class="register-button">注册</el-button>
          </el-form-item>

          <div class="register-options">
            <span class="login-link" @click="goToLogin">已有账号？去登录</span>
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
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePass2, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = {
          username: registerForm.username,
          phone: registerForm.phone,
          password: registerForm.password
        }
        
        await request.post('/user/register-json', data)
        
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
}

.register-card-wrapper {
  width: 380px;
  filter: drop-shadow(0 10px 20px rgba(0, 0, 0, 0.1));
}

.register-card {
  border-radius: 16px;
  overflow: hidden;
  background-color: white;
  border: none;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.07);
}

.register-card :deep(.el-card__header) {
  padding: 25px 30px;
  border-bottom: none;
  text-align: center;
}

.register-title {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #333;
}

.register-form {
  margin-top: 10px;
}

.register-form :deep(.el-form-item__label) {
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

.register-button {
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

.register-button:hover {
  background: #4f46e5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.register-options {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  color: #6366f1;
  font-size: 14px;
}

.login-link {
  cursor: pointer;
  transition: color 0.3s;
}

.login-link:hover {
  color: #4f46e5;
  text-decoration: underline;
}
</style>