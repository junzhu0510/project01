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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const chances = ref(3) // 每天抽奖次数
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
    // 发送抽奖请求
    const response = await axios.post('/api/lottery/draw', {}, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    const prizeId = response.data.data.prizeId
    const prize = prizes.find(p => p.id === prizeId)
    
    // 模拟转盘效果
    timer = setInterval(() => {
      times++
      currentIndex.value = (currentIndex.value + 1) % prizes.length
      
      // 最后一圈减速并停在中奖位置
      if (times >= totalTimes) {
        clearInterval(timer)
        currentIndex.value = prizes.findIndex(p => p.id === prizeId)
        isDrawing.value = false
        chances.value--
        
        // 显示中奖信息
        currentPrize.value = prize
        showPrizeDialog.value = true
      }
    }, 100)
  } catch (error) {
    isDrawing.value = false
    ElMessage.error(error.response?.data?.message || '抽奖失败')
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
    const response = await axios.get('/api/lottery/chances', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    chances.value = response.data.data.chances
  } catch (error) {
    ElMessage.error('获取抽奖次数失败')
  }
}

onMounted(() => {
  getChances()
})
</script>

<style scoped>
.lottery-container {
  padding: 20px;
}

.lottery-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.prize-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-bottom: 20px;
}

.prize-item {
  height: 100px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  transition: all 0.3s;
}

.prize-item.active {
  background-color: #409eff;
  color: white;
  transform: scale(1.05);
}

.draw-button {
  width: 200px;
  height: 50px;
  font-size: 18px;
  margin: 0 auto;
  display: block;
}

.prize-dialog-content {
  text-align: center;
}

.prize-dialog-content h2 {
  color: #e6a23c;
  margin-bottom: 20px;
}
</style>