<template>
  <div class="history-container">
    <el-card class="history-card">
      <template #header>
        <div class="card-header">
          <h3>中奖记录</h3>
          <el-button type="primary" @click="goToLottery">去抽奖</el-button>
        </div>
      </template>
      
      <el-table
        :data="prizeHistory"
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="prizeName"
          label="奖品名称"
          width="180"
        />
        <el-table-column
          prop="prizeDescription"
          label="奖品描述"
        />
        <el-table-column
          prop="createTime"
          label="中奖时间"
          width="180"
        >
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="120"
        >
          <template #default="scope">
            <el-tag :type="scope.row.status === 'CLAIMED' ? 'success' : 'warning'">
              {{ scope.row.status === 'CLAIMED' ? '已领取' : '未领取' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="120"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              @click="claimPrize(scope.row)"
              :disabled="scope.row.status === 'CLAIMED'"
            >
              {{ scope.row.status === 'CLAIMED' ? '已领取' : '领取奖品' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 领奖确认弹窗 -->
    <el-dialog
      v-model="showClaimDialog"
      title="领取奖品"
      width="30%"
    >
      <div class="claim-dialog-content">
        <p>请确认领取以下奖品：</p>
        <h3>{{ selectedPrize?.prizeName }}</h3>
        <p>{{ selectedPrize?.prizeDescription }}</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showClaimDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmClaim" :loading="claimLoading">
            确认领取
          </el-button>
        </span>
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
const loading = ref(false)
const prizeHistory = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const showClaimDialog = ref(false)
const selectedPrize = ref(null)
const claimLoading = ref(false)

// 格式化日期
const formatDate = (date) => {
  return new Date(date).toLocaleString()
}

// 获取中奖记录
const fetchPrizeHistory = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/lottery/history', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      },
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    prizeHistory.value = response.data.data.records
    total.value = response.data.data.total
  } catch (error) {
    ElMessage.error('获取中奖记录失败')
  } finally {
    loading.value = false
  }
}

// 领取奖品
const claimPrize = (prize) => {
  selectedPrize.value = prize
  showClaimDialog.value = true
}

// 确认领取
const confirmClaim = async () => {
  if (!selectedPrize.value) return
  
  claimLoading.value = true
  try {
    await axios.post(`/api/lottery/claim/${selectedPrize.value.id}`, {}, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    ElMessage.success('领取成功')
    showClaimDialog.value = false
    fetchPrizeHistory() // 刷新列表
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '领取失败')
  } finally {
    claimLoading.value = false
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchPrizeHistory()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchPrizeHistory()
}

const goToLottery = () => {
  router.push('/lottery')
}

onMounted(() => {
  fetchPrizeHistory()
})
</script>

<style scoped>
.history-container {
  padding: 20px;
}

.history-card {
  max-width: 1000px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.claim-dialog-content {
  text-align: center;
}

.claim-dialog-content h3 {
  color: #e6a23c;
  margin: 20px 0;
}
</style>