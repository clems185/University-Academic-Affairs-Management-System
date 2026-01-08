<template>
  <div class="user-info-container">
    <!-- 页面标题 -->
    <div class="page-title">用户信息管理</div>

    <!-- 用户类型切换 - 四个独立按钮 -->
    <div class="user-type-buttons">
      <el-button 
        :type="activeUserType === 'all' ? 'primary' : ''" 
        @click="setUserType('all')"
        class="user-type-btn"
      >
        所有用户
      </el-button>
      <el-button 
        :type="activeUserType === 'student' ? 'primary' : ''" 
        @click="setUserType('student')"
        class="user-type-btn"
      >
        学生
      </el-button>
      <el-button 
        :type="activeUserType === 'teacher' ? 'primary' : ''" 
        @click="setUserType('teacher')"
        class="user-type-btn"
      >
        教师
      </el-button>
      <el-button 
        :type="activeUserType === 'admin' ? 'primary' : ''" 
        @click="setUserType('admin')"
        class="user-type-btn"
      >
        管理员
      </el-button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-panel">
      <el-row :gutter="16">
        <el-col :span="5">
          <el-input 
            v-model="searchForm.name" 
            placeholder="请输入姓名" 
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
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshLeft /></el-icon> 重置
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
        <el-table-column prop="Id" label="ID" width="120" align="center" />
        <el-table-column prop="Name" label="姓名" width="180" align="center" />
        <el-table-column prop="Gender" label="性别" width="80" align="center" />
        <el-table-column prop="MajorName" label="专业" width="180" align="center" />
        <el-table-column prop="TypeDetail" label="类型详情" width="100" align="center" />
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
import { ref, reactive, onMounted,computed } from 'vue';
import { Search, RefreshLeft } from '@element-plus/icons-vue';
import { getUserList, getMajorList } from '@/api/allInfo/allinfo'; // 修改导入
import type { UserInfoResult, MajorInfo } from '@/api/allInfo/types';
import { ElMessage } from 'element-plus';

// 状态管理
const loading = ref(false);
const tableData = ref<UserInfoResult[]>([]);
const majorList = ref<MajorInfo[]>([]);
const activeUserType = ref('all'); // 默认选择所有用户


// 计算用户类型参数
const userTypesParam = computed(() => {
  switch (activeUserType.value) {
    case 'student': return '1';
    case 'teacher': return '2';
    case 'admin': return '3';
    default: return ''; // 所有用户不传递userTypes参数
  }
});

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

// 设置用户类型
const setUserType = (type: string) => {
  activeUserType.value = type;
  pagination.currentPage = 1;
  loadUserList();
};

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

// 获取用户列表数据
const loadUserList = async () => {
  try {
    loading.value = true;
    
    const params: any = {
      name: searchForm.name,
      majorName: searchForm.majorName,
      gender: searchForm.gender,
      pageIndex: pagination.currentPage,
      pageSize: pagination.pageSize
    };

    // 只有非全部类型时才添加userTypes参数
    if (userTypesParam.value) {
      params.userTypes = userTypesParam.value;
    }

    const res = await getUserList(params);

    if (res.data.IsSuccess) {
      tableData.value = res.data.Result || [];
      pagination.total = res.data.Total || 0;
    } else {
      tableData.value = [];
      pagination.total = 0;
      ElMessage.error(res.data.Message || '获取用户信息失败');
    }
  } catch (err) {
    console.error('加载用户列表错误:', err);
    ElMessage.error('加载用户信息失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 搜索处理
const handleSearch = () => {
  pagination.currentPage = 1;
  loadUserList();
};

// 重置搜索
const handleReset = () => {
  searchForm.name = '';
  searchForm.majorName = '';
  searchForm.gender = '';
  pagination.currentPage = 1;
  loadUserList();
};

// 分页大小变更
const handlePageSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  loadUserList();
};

// 页码变更
const handlePageNumChange = (page: number) => {
  pagination.currentPage = page;
  loadUserList();
};

// 初始化
onMounted(() => {
  loadMajorList();
  loadUserList();
});
</script>

<style scoped>
.user-info-container {
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

.user-type-buttons {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.user-type-btn {
  min-width: 90px;
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