<!-- TeacherInfoView.vue -->
<template>
  <div class="teacher-info-container">
    <!-- 页面标题 -->
    <div class="page-title">教师信息管理</div>

    <!-- 搜索区域 -->
    <div class="search-panel">
      <el-row :gutter="16">
        <el-col :span="5">
          <el-input 
            v-model="searchForm.name" 
            placeholder="请输入教师姓名" 
            clearable
            prefix-icon="Search"
          />
        </el-col>
        <el-col :span="5">
          <el-select 
            v-model="searchForm.majorName" 
            placeholder="请选择专业" 
            clearable
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="major in majorList"
              :key="major.MajorId"
              :label="major.MajorName"
              :value="major.MajorName"
            />
          </el-select>
        </el-col>
        <el-col :span="5">
          <el-select 
            v-model="searchForm.gender" 
            placeholder="请选择性别" 
            clearable
            style="width: 100%"
          >
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-col>
        <el-col :span="9" class="search-actions">
          <el-button type="primary" @click="handleSearch">
            <Search /> 搜索
          </el-button>
          <el-button @click="handleReset">
            <RefreshLeft /> 重置
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 表格区域 -->
    <div class="table-wrapper">
      <el-table 
        :data="tableData" 
        border 
        stripe
        v-loading="loading"
        element-loading-text="正在加载数据..."
        :header-cell-style="{ 'background-color': '#f5f7fa' }"
      >
        <el-table-column prop="Id" label="工号" width="120" align="center" />
        <el-table-column prop="Name" label="姓名" width="180" align="center" />
        <el-table-column prop="Gender" label="性别" width="80" align="center" />
        <el-table-column prop="MajorName" label="专业" width="180" align="center" />
        <el-table-column prop="TypeDetail" label="职称" width="100" align="center" />
        <el-table-column prop="Email" label="邮箱" min-width="180" align="center" />
        <el-table-column prop="Phone" label="电话" width="150" align="center" />
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-bar">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        :small="false"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handlePageSizeChange"
        @current-change="handlePageNumChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Search, RefreshLeft } from '@element-plus/icons-vue';
import { getTeacherInfoList, getMajorList } from '@/api/teacherInfo/teacherInfo';
import type { MajorInfo, TeacherInfoResult } from '@/api/teacherInfo/types';
import { ElMessage } from 'element-plus';

// 状态管理
const loading = ref(false);
const tableData = ref<TeacherInfoResult[]>([]);
const majorList = ref<MajorInfo[]>([]);

// 搜索表单
const searchForm = reactive({
  name: '',
  majorName: '',
  gender: ''
});

// 分页配置
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 获取专业列表
const loadMajorList = async () => {
  try {
    const res = await getMajorList();
    if (res.data.IsSuccess) {
      majorList.value = res.data.Result || [];
    } else {
      ElMessage.error(res.data.Message || '获取专业列表失败');
    }
  } catch (err) {
    console.error('加载专业列表错误:', err);
    ElMessage.error('加载专业列表失败，请稍后重试');
  }
};

// 获取教师列表数据
const loadTeacherList = async () => {
  try {
    loading.value = true;
    
    const params = {
      name: searchForm.name,
      majorName: searchForm.majorName,
      gender: searchForm.gender,
      pageIndex: pagination.currentPage,
      pageSize: pagination.pageSize
    };

    const res = await getTeacherInfoList(params);
    
    if (res.data.IsSuccess) {
      tableData.value = res.data.Result || [];
      pagination.total = res.data.Total || 0;
    } else {
      tableData.value = [];
      pagination.total = 0;
      ElMessage.error(res.data.Message || '获取教师信息失败');
    }
  } catch (err) {
    console.error('加载教师列表错误:', err);
    ElMessage.error('加载教师信息失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 搜索处理
const handleSearch = () => {
  pagination.currentPage = 1;
  loadTeacherList();
};

// 重置搜索
const handleReset = () => {
  searchForm.name = '';
  searchForm.majorName = '';
  searchForm.gender = '';
  pagination.currentPage = 1;
  loadTeacherList();
};

// 分页大小变更
const handlePageSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  loadTeacherList();
};

// 页码变更
const handlePageNumChange = (page: number) => {
  pagination.currentPage = page;
  loadTeacherList();
};

// 初始化
onMounted(() => {
  loadMajorList();
  loadTeacherList();
});
</script>

<style scoped>
.teacher-info-container {
  padding: 20px;
  padding-bottom: 80px;
  background-color: #fff;
  border-radius: 6px;
  min-height: 600px;
  box-sizing: border-box;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.search-panel {
  background-color: #f9fafb;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.search-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.table-wrapper {
  margin-bottom: 20px;
}

.pagination-bar {
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
}

:deep(.el-input__wrapper) {
  border-radius: 4px;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 4px;
}
</style>