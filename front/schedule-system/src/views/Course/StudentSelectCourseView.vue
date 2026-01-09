<template>
  <div class="student-select-course-container">
    <!-- 选课轮次信息横幅 -->
    <div class="selection-round-banner" v-if="currentRound">
      <div class="banner-content">
        <div class="banner-title">当前选课轮次</div>
        <div class="banner-info">{{ currentRound.Information }}</div>
        <div class="banner-time">
          时间: {{ formatDateTime(currentRound.BeginTime) }} - {{ formatDateTime(currentRound.EndTime) }}
        </div>
        <div class="banner-type">
          处理规则: {{ currentRound.Type === 0 ? '人数超限随机踢人' : '选满为止' }}
        </div>
      </div>
    </div>

    <div class="course-selection-layout" v-if="currentRound">
      <!-- 左侧：学生选课列表 -->
      <div class="left-panel">
        <div class="panel-header">
          <h3 class="panel-title">我的选课列表</h3>
          <div class="panel-actions">
            <el-button type="primary" @click="openCourseDialog">
              <el-icon><Plus /></el-icon>选择课程
            </el-button>
            <el-button type="success" @click="saveCourseTable">
              <el-icon><Check /></el-icon>保存课表
            </el-button>
          </div>
        </div>
        
        <div class="panel-content">
          <el-table 
            :data="studentCourses" 
            height="100%"
            highlight-current-row
            stripe
            border
            @current-change="handleCourseSelect"
            style="width: 100%;">
            <el-table-column prop="CourseName" label="课程名称" width="120" />
            <el-table-column label="班号" width="90">
              <template #default="{ row }">
                {{ row.ClassId === '0' ? '未选择' : row.CourseId + row.ClassId }}
              </template>
            </el-table-column>
            <el-table-column prop="TeacherName" label="授课教师" width="92" />
            <el-table-column prop="ClassInfo" label="上课信息" width="260">
              <template #default="{ row }">
                <div v-if="row.ClassInfo">{{ row.ClassInfo }}</div>
                <div v-else class="no-info">未选择</div>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag 
                  :type="statusTagType(row.Status)"
                  size="small"
                  class="status-tag">
                  {{ statusText(row.Status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template #default="{ row }">
                <el-button 
                  type="danger" 
                  size="small"
                  @click.stop="withdrawCourse(row.CourseId)">
                  退课
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      
      <div class="divider"></div>
      
      <div class="right-panel">
        <div class="panel-header">
          <h3 class="panel-title">可选班级</h3>
        </div>
        
        <div class="panel-content">
          <el-table 
            :data="classList" 
            height="100%" 
            stripe
            border
            style="width: 100%">
            <el-table-column label="班号" width="90">
              <template #default="{ row }">
                {{ row.CourseId + row.ClassId }}
              </template>
            </el-table-column>
            <el-table-column prop="CourseName" label="课程名称" width="125" />
            <el-table-column prop="TeacherName" label="授课教师" width="97" />
            <el-table-column label="上课信息" width="220">
              <template #default="{ row }">
                <div v-if="row.ClassInfo">{{ row.ClassInfo }}</div>
                <div v-else class="no-info">未安排</div>
              </template>
            </el-table-column>
            <el-table-column label="容量" width="100">
              <template #default="{ row }">
                <div class="capacity-container">
                  <el-progress 
                    :percentage="Math.round((row.SelectedCount / row.Capacity) * 100)"
                    :color="customColors"
                    :show-text="false"
                    class="capacity-progress" />
                  <span class="capacity-text">{{ row.SelectedCount }}/{{ row.Capacity }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center">
              <template #default="{ row }">
                <el-button 
                  type="primary" 
                  size="small"
                  @click="selectClassCourse(row.CourseId, row.ClassId)"
                  :disabled="isClassSelected(row.CourseId + row.ClassId)">
                  {{ isClassSelected(row.CourseId + row.ClassId) ? '已选' : '选择' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>

    <div v-else class="no-selection-round">
      <el-empty description="当前没有可用的选课轮次" />
    </div>
    
    <!-- 选择课程对话框 -->
    <el-dialog v-model="courseDialogVisible" title="选择课程" width="70%">
      <el-table 
        :data="availableCourses" 
        height="400"
        stripe
        border
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="CourseName" label="课程名称" width="180" />
        <el-table-column label="状态">
          <template #default>
            <el-tag type="info" size="small">未选择</el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <template #footer>
        <el-button @click="courseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addSelectedCourses">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import { 
  getStudentCourses, 
  getAvailableCourses, 
  getClassesByCourse,
  addCourse,
  selectClass,
  removeCourse,
  saveCourses
} from '@/api/studentSelectCourse';
import { getAvailableSelectionRounds } from '@/api/selectionRound';
import type { SelectCourseItem, ClassInfo } from '@/api/studentSelectCourse/types';
import type { SelectionRound } from '@/api/selectionRound/types';
import { Plus, Check } from '@element-plus/icons-vue';

const studentCourses = ref<SelectCourseItem[]>([]);
const availableCourses = ref<SelectCourseItem[]>([]);
const classList = ref<ClassInfo[]>([]);
const selectedCourse = ref<SelectCourseItem | null>(null);
const selectedCourses = ref<SelectCourseItem[]>([]);
const courseDialogVisible = ref(false);
const currentStudentId = sessionStorage.getItem('userId') || ' ';
const currentRound = ref<SelectionRound | null>(null);
const currentRowKey = ref<string>(''); // 新增：用于保持当前选中的行

// 状态标签类型
const statusTagType = (status: string) => {
  switch (status) {
    case 'N': return 'danger'; // 未选择（红色）
    case 'P': return 'warning'; // 预选择（橙色）
    case 'C': return 'success'; // 已选择（绿色）
    default: return 'info';
  }
};

// 状态文本
const statusText = (status: string) => {
  switch (status) {
    case 'N': return '未选择';
    case 'P': return '预选择';
    case 'C': return '已选择';
    default: return '';
  }
};

// 进度条颜色
const customColors = [
  { color: '#5cb87a', percentage: 60 },
  { color: '#e6a23c', percentage: 80 },
  { color: '#f56c6c', percentage: 90 }
];

// 检查班级是否已选
const isClassSelected = (courseClassId: string) => {
  return studentCourses.value.some(item => 
    item.CourseClassId === courseClassId && 
    (item.Status === 'P' || item.Status === 'C')
  );
};

// 格式化日期时间
const formatDateTime = (dateTimeStr: string) => {
  const date = new Date(dateTimeStr);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 加载选课轮次
const loadSelectionRounds = async () => {
  try {
    const res = await getAvailableSelectionRounds(currentStudentId);
    if (res.data.IsSuccess && res.data.Result && res.data.Result.length > 0) {
      currentRound.value = res.data.Result[0]; // 取第一个可用选课轮次
      await loadStudentCourses();
    } else {
      currentRound.value = null;
      ElMessage.warning('当前没有可用的选课轮次');
    }
  } catch (error) {
    ElMessage.error('加载选课轮次失败: ' + (error as Error).message);
    console.error('加载选课轮次失败详情:', error);
  }
};

// 加载学生选课数据
const loadStudentCourses = async () => {
  if (!currentRound.value) return;
  
  try {
    const res = await getStudentCourses(currentStudentId, currentRound.value.SelectionId);
    studentCourses.value = processCourseData(res.data.Result);
    
    // 保持当前选中的课程
    if (currentRowKey.value) {
      // 在下一次DOM更新后设置当前行
      nextTick(() => {
        const currentRow = studentCourses.value.find(
          item => item.CourseId + item.ClassId === currentRowKey.value
        );
        if (currentRow) {
          selectedCourse.value = currentRow;
        } else if (studentCourses.value.length > 0) {
          // 如果找不到之前选中的行，默认选中第一门课程
          selectedCourse.value = studentCourses.value[0];
          currentRowKey.value = studentCourses.value[0].CourseId + studentCourses.value[0].ClassId;
        }
      });
    } else if (studentCourses.value.length > 0) {
      // 默认选中第一门课程
      selectedCourse.value = studentCourses.value[0];
      currentRowKey.value = studentCourses.value[0].CourseId + studentCourses.value[0].ClassId;
    } else {
      classList.value = [];
    }
  } catch (error) {
    ElMessage.error('加载选课数据失败: ' + (error as Error).message);
    console.error('加载选课数据失败详情:', error);
  }
};

// 加载可选课程
const loadAvailableCourses = async () => {
  if (!currentRound.value) return;
  
  try {
    const res = await getAvailableCourses(currentStudentId, currentRound.value.SelectionId);
    availableCourses.value = res.data.Result;
  } catch (error) {
    ElMessage.error('加载可选课程失败: ' + (error as Error).message);
  }
};

// 打开课程选择对话框
const openCourseDialog = async () => {
  if (!currentRound.value) {
    ElMessage.warning('当前没有可用的选课轮次');
    return;
  }
  
  await loadAvailableCourses();
  courseDialogVisible.value = true;
  selectedCourses.value = [];
};

// 处理选择变化
const handleSelectionChange = (selection: SelectCourseItem[]) => {
  selectedCourses.value = selection;
};

// 添加选中的课程
const addSelectedCourses = async () => {
  if (!currentRound.value) return;
  
  if (selectedCourses.value.length === 0) {
    ElMessage.warning('请选择至少一门课程');
    return;
  }

  try {
    for (const course of selectedCourses.value) {
      await addCourse(currentStudentId, course.CourseId, currentRound.value.SelectionId);
    }
    await loadStudentCourses();
    courseDialogVisible.value = false;
    ElMessage.success('课程已添加到选课列表');
  } catch (error) {
    ElMessage.error('添加课程失败: ' + (error as Error).message);
  }
};

// 处理课程选择
const handleCourseSelect = async (course: SelectCourseItem) => {
  if (!course) return;
  
  selectedCourse.value = course;
  currentRowKey.value = course.CourseId + course.ClassId; // 更新当前选中的行key
  try {
    const res = await getClassesByCourse(currentStudentId, course.CourseId);
    classList.value = processCourseData(res.data.Result);
  } catch (error) {
    ElMessage.error('加载班级信息失败: ' + (error as Error).message);
  }
};

// 选择班级
const selectClassCourse = async (courseId: string, classId: string) => {
  try {
    console.log('选择班级参数:', courseId, classId);
    const response = await selectClass(currentStudentId, courseId, classId);
    
    // 只有当请求成功且后端返回成功时才显示成功消息
    if (response.data.IsSuccess) {
      ElMessage.success('班级选择成功');
      
      // 刷新数据
      await loadStudentCourses();
      
      // 刷新班级列表
      if (selectedCourse.value) {
        const res = await getClassesByCourse(currentStudentId, selectedCourse.value.CourseId);
        classList.value = processCourseData(res.data.Result);
      }
    } else {
      // 后端返回了成功状态码但IsSuccess为false
      ElMessage.error('选择班级失败: ' + response.data.Msg);
    }
  } catch (error: any) {
    console.error('选择班级错误详情:', error);
    
    // 检查是否是时间冲突错误
    if (error.response && error.response.data && error.response.data.Msg) {
      const errorMsg = error.response.data.Msg;
      if (errorMsg.includes('时间冲突')) {
        ElMessage.error('选择失败：该班级上课时间与已选课程冲突');
      } else if (errorMsg.includes('已满')) {
        ElMessage.error('选择失败：该班级已满');
      } else {
        ElMessage.error('选择班级失败: ' + errorMsg);
      }
    } else if (error.message) {
      ElMessage.error('选择班级失败: ' + error.message);
    } else {
      ElMessage.error('选择班级失败: 未知错误');
    }
  }
};

// 修改处理课程数据的方法
const processCourseData = (courses: any[]) => {
  return courses.map(course => ({
    ...course,
    // 确保正确显示班级号
    ClassDisplayId: course.ClassId === '0' ? '未选择' : `${course.CourseId}${course.ClassId}`,
    // 组合上课信息
    ClassInfo: course.TimeSlotInfo && course.Location 
      ? `${course.TimeSlotInfo} ${course.Location}` 
      : null,
    CourseClassId: course.CourseId + course.ClassId
  }));
};

// 退课
const withdrawCourse = async (courseId: string) => {
  try {
    await removeCourse(currentStudentId, courseId);
    await loadStudentCourses();
    ElMessage.success('退课成功');
    
    // 刷新班级列表
    if (selectedCourse.value && selectedCourse.value.CourseId === courseId) {
      classList.value = [];
    }
  } catch (error) {
    ElMessage.error('退课失败: ' + (error as Error).message);
  }
};

// 保存课表
const saveCourseTable = async () => {
  try {
    await saveCourses(currentStudentId);
    await loadStudentCourses();
    ElMessage.success('课表保存成功');
  } catch (error: any) {
    if (error.response && error.response.data && error.response.data.Msg) {
      ElMessage.error('课表保存失败: ' + error.response.data.Msg);
    } else {
      ElMessage.error('课表保存失败: ' + (error as Error).message);
    }
  }
};

onMounted(() => {
  loadSelectionRounds();
});
</script>

<style scoped>
.student-select-course-container {
  padding: 20px;
  height: calc(100vh - 200px);
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: #f5f7fa;
}

.selection-round-banner {
  background: linear-gradient(135deg, #95a7f8 0%, #f0edf4 100%);
  color: white;
  padding: 16px 24px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.banner-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.banner-title {
  font-size: 18px;
  font-weight: 600;
}

.banner-info {
  font-size: 16px;
}

.banner-time,
.banner-type {
  font-size: 14px;
  opacity: 0.9;
}

.course-selection-layout {
  display: flex;
  height: 100%;
  width: 100%;
  max-width: 1600px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  margin: 0 auto;
}

.no-selection-round {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.left-panel, .right-panel {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.left-panel {
  flex: 1;
  min-width: 0; 
}

.right-panel {
  flex: 1;
  min-width: 0; 
}

.divider {
  width: 1px;
  background-color: #e8e8e8;
  margin: 0 10px;
}

.panel-header {
  padding: 16px 20px;
  background-color: #f9fafc;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.panel-actions {
  display: flex;
  gap: 10px;
}

.panel-content {
  flex: 1;
  padding: 15px;
  overflow: auto;
  min-height: 0;
}

.capacity-container {
  display: flex;
  align-items: center;
  gap: 0px;
  width: 50px;
}

.capacity-progress {
  flex: 1;
  min-width: 50px;
}

.capacity-text {
  font-size: 12px;
  color: #606266;
}

.status-tag {
  border-radius: 4px;
  font-weight: 500;
}

.no-info {
  color: #909399;
  font-style: italic;
}

/* 表格样式增强 */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

:deep(.el-table__inner-wrapper) {
  height: 100%;
}

:deep(.el-table--border) {
  border: 1px solid #ebeef5;
}

:deep(.el-table--border th) {
  border-right: 1px solid #ebeef5;
}

:deep(.el-table--border td) {
  border-right: 1px solid #ebeef5;
}

:deep(.el-table__row) {
  transition: background-color 0.2s;
}

:deep(.el-table__row:hover) {
  background-color: #f5f7fa !important;
}

.el-button {
  font-size: 14px;
}
</style>