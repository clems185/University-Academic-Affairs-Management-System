<template>
  <div class="selection-round-container">
    <!-- 主卡片容器 -->
    <el-card class="main-card">
      <!-- 顶部标题区域 -->
      <div class="header">
        <h1 class="main-title">选课轮次选择</h1>
        <p class="sub-title">请选择您要参与的选课轮次</p>
      </div>
      
      <!-- 注意事项卡片 -->
      <el-card class="notice-card">
        <div class="notice-header">
          <el-icon class="notice-icon"><InfoFilled /></el-icon>
          <h3 class="notice-title">选课注意事项</h3>
        </div>
        <ul class="notice-list">
          <li>请仔细阅读课程说明，确保课程符合您的专业要求</li>
          <li>选课轮次结束后将无法修改已选课程</li>
          <li>每个选课轮次只能参与一次</li>
          <li>课程冲突将导致选课失败，请合理安排时间</li>
        </ul>
      </el-card>
      
      <!-- 轮次卡片网格 -->
      <div class="rounds-section">
        <h2 class="section-title">可选轮次</h2>
        <div class="rounds-grid">
          <el-card 
            v-for="round in selectionRounds" 
            :key="round.SelectionId"
            class="round-card"
            :class="{ 'active-round': round.IsActive }"
            @click="selectRound(round)">
            <div class="card-content">
              <div class="card-header">
                <h3 class="card-title">{{ round.Information }}</h3>
                <el-tag 
                  :type="round.IsActive ? 'success' : 'info'"
                  class="status-tag">
                  {{ round.IsActive ? '进行中' : '已结束' }}
                </el-tag>
              </div>
              
              <div class="time-info">
                <div class="time-item">
                  <el-icon class="time-icon"><Clock /></el-icon>
                  <span class="time-label">开始时间:</span>
                  <span class="time-value">{{ formatDate(round.BeginTime) }}</span>
                </div>
                <div class="time-item">
                  <el-icon class="time-icon"><Clock /></el-icon>
                  <span class="time-label">结束时间:</span>
                  <span class="time-value">{{ formatDate(round.EndTime) }}</span>
                </div>
              </div>
              
              <div class="card-footer">
                <el-button 
                  type="primary" 
                  plain
                  :disabled="!round.IsActive"
                  class="action-button">
                  进入选课
                </el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>
      
      <!-- 底部提示 -->
      <div class="footer-note">
        <el-icon class="tip-icon"><Warning /></el-icon>
        <span>请务必在选课轮次结束前完成选课操作</span>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAvailableSelectionRounds } from '@/api/selectionRound';
import type { SelectionRound } from '@/api/selectionRound/types';
import axios from 'axios';
import { 
  InfoFilled, 
  Clock, 
  Warning 
} from '@element-plus/icons-vue';

const router = useRouter();
const selectionRounds = ref<SelectionRound[]>([]);

const loadSelectionRounds = async () => {
  try {
    const currentStudentId = sessionStorage.getItem('userId')||''
    const res = await getAvailableSelectionRounds(currentStudentId);
    selectionRounds.value = res.data.Result|| [];
  } catch (error) {
    console.error('加载选课轮次失败:', error);
  }
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const selectRound = (round: SelectionRound) => {
  if (round.IsActive) {
    router.push({
      name: 'studentselectcourse',
      params: { selectionId: round.SelectionId }
    });
  }
};

onMounted(() => {
  loadSelectionRounds();
});
</script>

<style scoped>
.selection-round-container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  overflow: auto;
  min-height: calc(100vh - 80px);
  margin-top: 10px;
}

.main-card {
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  padding: 0px;
  background: #fff;
  margin-top: -25px; 
}

.header {
  text-align: center;
  margin-bottom: 25px;
  padding-bottom: 2px;
  border-bottom: 1px solid #eee;
  margin-top: -20px; 
}

.main-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}

.sub-title {
  font-size: 16px;
  color: #606266;
}

.notice-card {
  margin-bottom: 30px;
  border-left: 4px solid #409eff;
  border-radius: 8px;
}

.notice-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.notice-icon {
  color: #409eff;
  font-size: 20px;
  margin-right: 10px;
}


.notice-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin: 0;
}

.notice-list {
  padding-left: 25px;
  margin: 0;
}

.notice-list li {
  margin-bottom: 8px;
  color: #606266;
  line-height: 1.6;
}

.rounds-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 20px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.rounds-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 24px;
}

.round-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 10px;
  overflow: hidden;
  height: 220px;
  border: 1px solid #ebeef5;
}

.round-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  border-color: #c0c4cc;
}

.active-round {
  border-top: 4px solid #409eff;
}

.card-content {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.card-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin: 0;
  flex: 1;
}

.status-tag {
  font-weight: 500;
  margin-left: 10px;
}

.time-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.time-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.time-icon {
  color: #909399;
  margin-right: 8px;
  font-size: 16px;
}

.time-label {
  color: #606266;
  font-size: 14px;
  min-width: 70px;
}

.time-value {
  color: #303133;
  font-weight: 500;
  font-size: 14px;
}

.card-footer {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.action-button {
  width: 100%;
  font-weight: 500;
}

.footer-note {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
  background-color: #f0f7ff;
  border-radius: 8px;
  color: #606266;
  font-size: 14px;
    margin-bottom: 30px; /* 增加底部距离，确保最后内容能完全显示 */

}

.tip-icon {
  color: #e6a23c;
  margin-right: 8px;
  font-size: 18px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .rounds-grid {
    grid-template-columns: 1fr;
  }
  
  .main-card {
    padding: 20px;
  }
  
  .main-title {
    font-size: 24px;
  }
}
</style>