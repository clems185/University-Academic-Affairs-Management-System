<template>
  <div class="signin-list-container">
    <!-- 顶部筛选区域（仿 CompetitionApplyView.vue 的卡片 + 栅格布局） -->
    <div class="filter-section">
      <div class="filter-card">
        <h3 class="filter-title">签到列表</h3>
        <div class="filter-controls">
          <div class="filter-item">
            <span class="filter-label">班级：</span>
            <el-select
              v-model="filters.classId"
              placeholder="选择班级"
              clearable
              filterable
              style="width: 220px"
            >
              <el-option
                v-for="cl in classOptionsFiltered"
                :key="cl.classId + '_' + cl.courseId"
                :label="cl.classId + ' / ' + cl.name"
                :value="cl.classId"
              />
            </el-select>
          </div>

          <div class="filter-item">
            <span class="filter-label">签到批次ID：</span>
            <el-input
              v-model="filters.signId"
              placeholder="请输入 SignId"
              clearable
              style="width: 220px"
            />
          </div>

          <div class="filter-actions">
            <el-button type="primary" :loading="loading" @click="handleSearch">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
            <el-button type="success" :disabled="tableData.length === 0" @click="exportCSV">
              <el-icon><Download /></el-icon>
              导出 CSV
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 列表区域（卡片式容器 + 表头操作行） -->
    <div class="list-section">
      <div class="list-card">
        <div class="list-header">
          <div class="list-header-left">
            <h3 class="list-title">签到明细</h3>
            <div class="list-subtitle" v-if="filters.courseId || filters.classId || filters.signId">
              <span v-if="filters.classId">班级：{{ filters.classId }}</span>
              <span v-if="filters.signId">批次：{{ filters.signId }}</span>
            </div>
          </div>
        </div>

        <el-table
          :data="pagedData"
          stripe
          border
          height="600"
          v-loading="loading"
          style="width: 100%"
          :header-cell-style="{ textAlign: 'center' }"
          :cell-style="{ textAlign: 'center' }"
          empty-text="暂无数据"
        >
          <el-table-column prop="index" label="签到批次Id" min-width="120" />
          <el-table-column prop="studentId" label="学号" min-width="120" />
          <el-table-column prop="studentName" label="姓名" min-width="120" />
          <el-table-column prop="className" label="签到班级" min-width="120" />
          <el-table-column prop="signTime" label="签到时间" min-width="120">
            <template #default="scope">
              {{ formatDate(scope.row.signTime) }}
            </template>
          </el-table-column>
        </el-table>

        <div class="table-footer">
          <div class="table-meta">共 {{ filteredData.length }} 条</div>
          <el-pagination
            background
            layout="prev, pager, next, sizes, jumper"
            :page-sizes="[10, 20, 30, 50]"
            :current-page="pagination.page"
            :page-size="pagination.pageSize"
            :total="filteredData.length"
            @size-change="onSizeChange"
            @current-change="onCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Download } from '@element-plus/icons-vue'
import axios from 'axios'

/** ========================
 * 类型定义
 * ======================= */
interface ClassItem {
  classId: string
  courseId: string
  name: string
}

interface StudentSignIn {
  studentId: string
  studentName: string
  signTime: string | number | Date
}

/** ========================
 * 状态
 * ======================= */
const loading = ref(false)
const keyword = ref('')

const filters = reactive({
  teacherId: '',
  courseId: '',
  classId: '',
  signId: ''
})

const allClassOptions = ref<ClassItem[]>([])

const classOptionsFiltered = computed(() => allClassOptions.value)

const tableData = ref<StudentSignIn[]>([])

const pagination = reactive({
  page: 1,
  pageSize: 20
})

const filteredData = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  if (!k) return tableData.value
  return tableData.value.filter(r =>
    String(r.studentId ?? '').toLowerCase().includes(k) ||
    String(r.studentName ?? '').toLowerCase().includes(k)
  )
})

const pagedData = computed(() => {
  const start = (pagination.page - 1) * pagination.pageSize
  const end = start + pagination.pageSize
  return filteredData.value.slice(start, end)
})

// 选择班级后，自动带出对应的 CourseId（后端需要）
watch(() => filters.classId, (val) => {
  if (!val) {
    filters.courseId = ''
    return
  }
  const hit = allClassOptions.value.find(c => c.classId === val)
  filters.courseId = hit?.courseId || ''
})


/** ========================
 * 生命周期
 * ======================= */
onMounted(async () => {
  // 从 SessionStorage 获取 TeacherId
  try {
    
    const tid = sessionStorage.getItem('userId') || sessionStorage.getItem('teacherId')
    console.log(tid)
    if (tid) {
      filters.teacherId = String(tid)
    } else {
      // 兼容一些系统将用户对象存在 sessionStorage 的情况
      const s1 = sessionStorage.getItem('user') || sessionStorage.getItem('userInfo') || sessionStorage.getItem('loginUser')
      if (s1) {
        try {
          const obj = JSON.parse(s1)
          if (obj?.TeacherId || obj?.teacherId) filters.teacherId = String(obj.TeacherId ?? obj.teacherId)
        } catch {}
      }
    }
  } catch {}

  await loadClassList()
})

/** ========================
 * 方法
 * ======================= */
function formatDate(v: any) {
  if (!v) return '-'
  const d = new Date(v)
  if (Number.isNaN(d.getTime())) return String(v)
  return (
    d.getFullYear() + '-' +
    String(d.getMonth() + 1).padStart(2, '0') + '-' +
    String(d.getDate()).padStart(2, '0') + ' ' +
    String(d.getHours()).padStart(2, '0') + ':' +
    String(d.getMinutes()).padStart(2, '0') + ':' +
    String(d.getSeconds()).padStart(2, '0')
  )
}

async function loadClassList() {
  if (!filters.teacherId) return
  try {
    loading.value = true
    // GET /api/qrcode/class-list?TeacherId=xxx
    const { data } = await axios.get('api/qrcode/class-list', {
      params: { TeacherId: filters.teacherId }
    })
    if (data?.isSuccess || data?.IsSuccess) {
      const result = data.result ?? data.Result ?? []
      allClassOptions.value = (result || []).map((x: any) => ({
        classId: x.classId ?? x.ClassId,
        courseId: x.courseId ?? x.CourseId,
        name: x.name ?? x.Name
      }))
    } else {
      throw new Error(data?.message || '获取班级列表失败')
    }
  } catch (e: any) {
    console.error(e)
    ElMessage.error(e?.message || '获取班级列表失败')
  } finally {
    loading.value = false
  }
}


async function handleSearch() {
  if (!filters.teacherId) {
    ElMessage.warning('请先输入教师ID')
    return
  }
 
  await fetchSignInList()
}

async function refresh() {
  if (!filters.courseId || !filters.classId || !filters.signId) return
  await fetchSignInList()
}

async function fetchSignInList() {
  try {
    loading.value = true
    // GET /api/qrcode/signin-list?SignId=&ClassId=&CourseId=
    const { data } = await axios.get('api/qrcode/sign-list', {
      params: {
        SignId: filters.signId,
        ClassId: filters.classId,
        TeacherId: filters.teacherId
      }
    })
    console.log("获取到的数据")
    console.log(data)
    if (data?.isSuccess || data?.IsSuccess) {
      const list = data.result ?? data.Data ?? []
      tableData.value = (list || []).map((x: any) => ({
        index:x.SignId,
        className:x.ClassName,
        studentId: x.studentId ?? x.StudentId,
        studentName: x.studentName ?? x.StudentName,
        signTime: x.signTime ?? x.SignTime
      })
    )
    console.log(tableData.value)
      // 查询完成后重置分页到第一页
      pagination.page = 1
    } else {
      throw new Error(data?.message || '查询失败')
    }
  } catch (e: any) {
    console.error(e)
    ElMessage.error(e?.message || '查询失败')
  } finally {
    loading.value = false
  }
}

function handleReset() {
  keyword.value = ''
  filters.courseId = ''
  filters.classId = ''
  filters.signId = ''
  tableData.value = []
  pagination.page = 1
}

function onSizeChange(size: number) {
  pagination.pageSize = size
  pagination.page = 1
}

function onCurrentChange(page: number) {
  pagination.page = page
}

function exportCSV() {
  const rows = [['学号', '姓名', '签到时间']]
  tableData.value.forEach(r => {
    rows.push([
      String(r.studentId ?? ''),
      String(r.studentName ?? ''),
      formatDate(r.signTime)
    ])
  })
  const csv = rows.map(r => r.map(field => `"${String(field).replace(/"/g, '""')}"`).join(',')).join('\n')
  const blob = new Blob(["\ufeff" + csv], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  const title = `签到明细_${filters.courseId || '课程'}_${filters.classId || '班级'}_${filters.signId || '批次'}.csv`
  a.download = title
  a.click()
  URL.revokeObjectURL(url)
}
</script>

<style scoped>
/* 容器基底配色与布局，仿 CompetitionApplyView.vue */
.signin-list-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
  box-sizing: border-box;
}

/* 顶部筛选卡片 */
.filter-section { margin-bottom: 20px; }

.filter-card {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.filter-title {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 600;
}

.filter-controls {
  display: grid;
  grid-template-columns: repeat(4, minmax(240px, 1fr));
  gap: 16px 20px;
  align-items: center;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-label {
  white-space: nowrap;
  color: #666;
}

.filter-actions {
  display: flex;
  gap: 12px;
}

/* 列表卡片 */
.list-card {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  margin-bottom: 12px;
}

.list-title {  margin: 0; font-size: 18px; font-weight: 600; }

.list-subtitle { color: #909399; display: flex; gap: 12px; font-size: 13px; }

.list-header-right { display: flex; gap: 12px; }

.table-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 12px;
}

.table-meta { color: #909399; font-size: 13px; }

/* 响应式适配 */
@media (max-width: 1200px) {
  .filter-controls { grid-template-columns: repeat(2, minmax(240px, 1fr)); }
}

@media (max-width: 768px) {
  .filter-controls { grid-template-columns: 1fr; }
  .list-header { flex-direction: column; align-items: stretch; }
}
</style>