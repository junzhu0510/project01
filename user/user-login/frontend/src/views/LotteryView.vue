<template>
  <div class="lottery-container">
    <el-card class="lottery-card">
      <template #header>
        <div class="card-header">
          <h3>抽奖</h3>
          <div class="user-chances">
            剩余抽奖次数：{{ chances }}
          </div>
        </div>
      </template>
      
      <div class="lottery-content">
        <div class="prize-grid">
          <div
            v-for="(prize, index) in prizes"
            :key="index"
            :class="['prize-item', { active: currentIndex === index }]"
          >
            {{ prize.name }}
          </div>
        </div>
        
        <el-button
          type="primary"
          class="draw-button"
          @click="startLottery"
          :disabled="isDrawing || chances <= 0"
        >
          {{ isDrawing ? '抽奖中...' : '开始抽奖' }}
        </el-button>
      </div>
    </el-card>

    <!-- 中奖弹窗 -->
    <el-dialog
      v-model="showPrizeDialog"
      title="恭喜中奖！"
      width="30%"
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="prize-dialog-content">
        <h2>{{ currentPrize?.name }}</h2>
        <p>{{ currentPrize?.description }}</p>
      </div>
      <template #footer>
        <el-button @click="closePrizeDialog">确定</el-button>
        <el-button type="primary" @click="viewPrizeHistory">查看中奖记录</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'

import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import infinityBg from '@/assets/infinity-2289585.jpeg'

const router = useRouter()
const userStore = useUserStore()
const chances = ref(1) // 每天抽奖次数
const isDrawing = ref(false)
const currentIndex = ref(-1)
const showPrizeDialog = ref(false)
const currentPrize = ref(null)



const prizes = [
  { id: 1, name: '一等奖', probability: 0.01, description: '获得iPhone 15 Pro Max一台' },
  { id: 2, name: '二等奖', probability: 0.05, description: '获得AirPods Pro一副' },
  { id: 3, name: '三等奖', probability: 0.1, description: '获得100元京东购物卡' },
  { id: 4, name: '四等奖', probability: 0.2, description: '获得20元话费充值卡' },
  { id: 5, name: '五等奖', probability: 0.3, description: '获得5元现金红包' },
  { id: 6, name: '谢谢参与', probability: 0.34, description: '很遗憾，未中奖' }
]

const startLottery = async () => {
  if (isDrawing.value || chances.value <= 0) return
  
  isDrawing.value = true
  let timer = null
  let times = 0
  const totalTimes = 30 // 转动次数
  
  try {
    // 获取用户信息
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.id
    const username = userInfo.username
    
    if (!userId || !username) {
      ElMessage.error('用户信息不存在，请重新登录')
      isDrawing.value = false
      return
    }
    
    // 发送抽奖请求
    const response = await request.post('/lottery/draw-json', {
      userId: userId,
      username: username
    })
    
    console.log('抽奖响应：', response)
    
    // 检查响应状态
    if (response.code !== 200) {
      ElMessage.error(response.message || '抽奖失败')
      isDrawing.value = false
      return
    }
    
    const prizeId = response.prize ? response.prize.id : null
    let prize
    if (response.isWinner && prizeId) {
      // 根据后端返回的奖品信息创建奖品对象
      prize = {
        id: prizeId,
        name: response.prize.name || '未知奖品',
        description: response.prize.description || response.prize.remark || '恭喜中奖！'
      }
    } else {
      prize = prizes.find(p => p.name === '谢谢参与')
    }
    
    // 模拟转盘效果
    timer = setInterval(() => {
      times++
      currentIndex.value = (currentIndex.value + 1) % prizes.length
      
      // 最后一圈减速并停在中奖位置
      if (times >= totalTimes) {
        clearInterval(timer)
        // 根据是否中奖设置停止位置
        if (response.isWinner && prizeId) {
          // 中奖时随机选择一个非"谢谢参与"的位置
          const winningIndexes = prizes.map((p, index) => p.name !== '谢谢参与' ? index : -1).filter(i => i !== -1)
          currentIndex.value = winningIndexes[Math.floor(Math.random() * winningIndexes.length)]
        } else {
          // 未中奖时停在"谢谢参与"
          currentIndex.value = prizes.findIndex(p => p.name === '谢谢参与')
        }
        isDrawing.value = false
        chances.value--
        
        // 显示中奖信息
        currentPrize.value = prize
        showPrizeDialog.value = true
      }
    }, 100)
  } catch (error) {
    console.error('抽奖请求失败：', error)
    console.error('错误详情：', {
      status: error.response?.status,
      statusText: error.response?.statusText,
      data: error.response?.data,
      message: error.message
    })
    isDrawing.value = false
    ElMessage.error(error.response?.data?.message || error.message || '请求失败')
  }
}

const closePrizeDialog = () => {
  showPrizeDialog.value = false
  currentPrize.value = null
}

const viewPrizeHistory = () => {
  router.push('/prize-history')
}

// 获取用户剩余抽奖次数
const getChances = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.id
    if (!userId) {
      ElMessage.error('用户信息不存在，请重新登录')
      return
    }
    const response = await request.get(`/lottery/chances?userId=${userId}`)
    console.log('获取抽奖次数响应：', response)
    
    // 检查响应格式，适配不同的返回结构
    if (response.data && response.data.remainingChances !== undefined) {
      chances.value = response.data.remainingChances
    } else if (response.remainingChances !== undefined) {
      chances.value = response.remainingChances
    } else {
      chances.value = 0
    }
  } catch (error) {
    console.error('获取抽奖次数失败：', error)
    ElMessage.error('获取抽奖次数失败')
  }
}

onMounted(() => {
  getChances()
})
</script>

<style scoped>
.lottery-container {
  min-height: 100vh;
  background: url('@/assets/infinity-2289585.jpeg') center/cover no-repeat;
  padding: 100px 20px 40px;
  position: relative;
  overflow: hidden;
}

.lottery-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.2);
  pointer-events: none;
  z-index: 1;
}

@keyframes twinkle {
  0% { transform: translateY(0); }
  100% { transform: translateY(-100px); }
}

.lottery-card {
  max-width: 900px;
  margin: 0 auto;
  background: rgba(0, 0, 0, 0.85);
  border-radius: 24px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  border: none;
  overflow: hidden;
  position: relative;
  transition: all 0.3s ease;
}

.lottery-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.4);
}



@keyframes rainbow {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.15) 0%, rgba(255, 255, 255, 0.1) 100%);
  color: white;
  margin: -20px -20px 30px -20px;
}





.card-header h3 {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  background: linear-gradient(135deg, #ffffff, #e0e0e0, #ffffff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 20px rgba(255, 255, 255, 0.5);
  animation: shimmer 3s ease-in-out infinite;
}

@keyframes shimmer {
  0%, 100% { filter: brightness(1); }
  50% { filter: brightness(1.2); }
}

.user-chances {
  background: rgba(255, 255, 255, 0.2);
  padding: 12px 20px;
  border-radius: 25px;
  font-weight: 600;
  font-size: 16px;
  border: none;
  color: #ffffff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.7);
}

.lottery-content {
  padding: 0 30px 30px;
}

.prize-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin: 30px 0;
  perspective: 1000px;
}

.prize-item {
  padding: 25px 15px;
  text-align: center;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.15) 0%, rgba(255, 255, 255, 0.1) 100%);
  color: white;
  font-weight: 600;
  font-size: 16px;
  position: relative;
  overflow: hidden;
  transform-style: preserve-3d;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
}

.prize-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.prize-item:hover::before {
  left: 100%;
}

.prize-item:nth-child(1) { background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%); }
.prize-item:nth-child(2) { background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); }
.prize-item:nth-child(3) { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); }
.prize-item:nth-child(4) { background: linear-gradient(135deg, #ff8a80 0%, #ea4c89 100%); }
.prize-item:nth-child(5) { background: linear-gradient(135deg, #8fd3f4 0%, #84fab0 100%); }
.prize-item:nth-child(6) { background: linear-gradient(135deg, #d299c2 0%, #fef9d7 100%); }

.prize-item:hover {
  transform: translateY(-8px) rotateX(5deg);
  box-shadow: 0 15px 35px rgba(255, 255, 255, 0.2);
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%);
}

.prize-item.active {
  transform: translateY(-12px) rotateX(10deg) scale(1.05);
  box-shadow: 0 25px 50px rgba(255, 255, 255, 0.3), 0 0 30px rgba(255, 255, 255, 0.5);
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0.2) 100%);
  color: #000000;
  animation: infinityPulse 0.6s ease-in-out infinite alternate;
}

@keyframes infinityPulse {
  0% { box-shadow: 0 25px 50px rgba(255, 255, 255, 0.3), 0 0 30px rgba(255, 255, 255, 0.5); }
  100% { box-shadow: 0 25px 50px rgba(255, 255, 255, 0.4), 0 0 50px rgba(255, 255, 255, 0.8); }
}

.draw-button {
  width: 200px;
  height: 50px;
  font-size: 18px;
  margin: 40px auto;
  display: block;
  padding: 15px 40px;
  font-weight: 700;
  border-radius: 50px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.85) 100%);
  border: none;
  color: #000000;
  box-shadow: 0 10px 30px rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.draw-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s;
}

.draw-button:hover::before {
  left: 100%;
}

.draw-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 40px rgba(255, 255, 255, 0.3);
  background: linear-gradient(135deg, rgba(255, 255, 255, 1) 0%, rgba(255, 255, 255, 0.9) 100%);
}

.draw-button:active {
  transform: translateY(-1px);
}

.prize-dialog-content {
  text-align: center;
}

.prize-dialog-content h2 {
  color: #667eea;
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: 700;
}

.prize-dialog-content p {
  color: #666;
  margin: 10px 0;
  font-size: 16px;
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .lottery-container {
    padding: 20px 10px;
  }
  
  .prize-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }
  
  .prize-item {
    padding: 20px 10px;
    font-size: 14px;
  }
  
  .card-header {
    padding: 20px;
    flex-direction: column;
    gap: 15px;
  }
  
  .card-header h3 {
    font-size: 24px;
  }
}

@media (max-width: 480px) {
  .prize-grid {
    grid-template-columns: 1fr;
  }
}
</style>