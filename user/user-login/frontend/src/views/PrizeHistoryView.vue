<template>
  <div class="prize-history-container">
    <el-card class="history-card">
      <template #header>
        <div class="card-header">
          <h2>我的中奖记录</h2>
          <el-button @click="refreshHistory" :loading="loading" type="primary" size="small">
            刷新
          </el-button>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="prizeHistory.length === 0" class="empty-container">
        <el-empty description="暂无中奖记录" />
      </div>
      
      <div v-else>
        <el-table :data="prizeHistory" style="width: 100%" stripe>
          <el-table-column prop="prizeName" label="奖品名称" width="150">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.prizeName }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="prizeDescription" label="奖品描述" width="250" show-overflow-tooltip />
          
          <el-table-column prop="winTime" label="中奖时间" width="180">
            <template #default="scope">
              {{ formatTime(scope.row.winTime) }}
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button size="small" @click="viewDetail(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>
    
    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="中奖详情" width="500px">
      <div v-if="selectedPrize">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="奖品名称">
            <el-tag type="success">{{ selectedPrize.prizeName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="奖品描述">
            {{ selectedPrize.prizeDescription }}
          </el-descriptions-item>
          <el-descriptions-item label="中奖时间">
            {{ formatTime(selectedPrize.winTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(selectedPrize.status)">{{ getStatusText(selectedPrize.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="备注">
            {{ selectedPrize.remark || '无' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'

import request from '@/utils/request'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const prizeHistory = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const selectedPrize = ref(null)



// 获取中奖历史
const getPrizeHistory = async () => {
  loading.value = true
  try {
    const response = await request.get('/lottery/history', {
      params: {
        userId: userStore.userId,
        page: currentPage.value,
        size: pageSize.value
      }
    })
    
    if (response.data.code === 200) {
      prizeHistory.value = response.data.data.records || []
      total.value = response.data.data.total || 0
    } else {
      ElMessage.error(response.data.message || '获取中奖记录失败')
    }
  } catch (error) {
    console.error('获取中奖记录失败:', error)
    ElMessage.error('获取中奖记录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 刷新历史记录
const refreshHistory = () => {
  currentPage.value = 1
  getPrizeHistory()
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  getPrizeHistory()
}

// 当前页改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  getPrizeHistory()
}

// 查看详情
const viewDetail = (prize) => {
  selectedPrize.value = prize
  detailVisible.value = true
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case 'PENDING': return 'warning'
    case 'DELIVERED': return 'success'
    case 'PROCESSED': return 'success'
    case 'CLAIMED': return 'success'
    case 'EXPIRED': return 'danger'
    default: return 'info'
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 'PENDING': return '待发放'
    case 'DELIVERED': return '已发放'
    case 'PROCESSED': return '已处理'
    case 'CLAIMED': return '已领取'
    case 'EXPIRED': return '已过期'
    default: return '未知'
  }
}

// 组件挂载时获取数据
onMounted(() => {
  getPrizeHistory()
})
</script>

<style scoped>
.prize-history-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.history-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  color: #303133;
}

.loading-container {
  padding: 20px;
}

.empty-container {
  padding: 40px 20px;
  text-align: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.el-table {
  margin-bottom: 20px;
}

.el-tag {
  font-weight: 500;
}

.el-descriptions {
  margin-top: 20px;
}
</style>