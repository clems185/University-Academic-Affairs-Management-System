<template>
  <div class="admin-public-selection-container">
    <div class="header">
      <h2>选课时间段管理</h2>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>创建选课时间段
      </el-button>
    </div>

    <div class="selection-list">
      <el-table :data="selections" stripe border style="width: 100%" max-height="calc(100vh - 240px)">
        <el-table-column prop="SelectionId" label="ID" width="100" />
        <el-table-column prop="Information" label="描述" min-width="140" />
        <el-table-column label="开始时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.BeginTime) }}
          </template>
        </el-table-column>
        <el-table-column label="结束时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.EndTime) }}
          </template>
        </el-table-column>
        <el-table-column label="学年学期" width="190">
          <template #default="{ row }">
            {{ row.Year }}-{{ row.Year + 1 }}学年 {{ row.Semester === 0 ? '第一学期' : '第二学期' }}
          </template>
        </el-table-column>
        <el-table-column label="处理类型" width="95">
          <template #default="{ row }">
            <el-tag :type="row.Type === 0 ? 'warning' : 'success'">
              {{ row.Type === 0 ? '随机踢人' : '人满为止' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="125">
          <template #default="{ row }">
            <el-tag :type="row.IsActive ? 'success' : 'info'">
              {{ row.IsActive ? '进行中' : '未开始/已结束' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="已处理" width="80">
          <template #default="{ row }">
            <el-tag :type="row.IsProcessed ? 'success' : 'info'">
              {{ row.IsProcessed ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="课程数" width="70">
          <template #default="{ row }">
            {{ row.Courses.length }}
          </template>
        </el-table-column>
        <el-table-column label="专业数" width="70">
          <template #default="{ row }">
            {{ row.Majors.length }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click="handleManageCourses(row)"
            >管理课程</el-button>
            <el-button 
              size="small" 
              type="success"
              :disabled="row.IsActive || row.IsProcessed"
              @click="handleProcessResult(row)"
            >处理结果</el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="handleDelete(row.SelectionId)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 创建/编辑对话框 -->
    <el-dialog 
      v-model="showEditDialog" 
      :title="isEditing ? '编辑选课时间段' : '创建选课时间段'" 
      width="700px"
    >
      <div class="dialog-content">
        <el-form :model="editForm" label-width="120px" :rules="formRules" ref="editFormRef">
          <el-form-item label="描述信息" prop="Information">
            <el-input 
              v-model="editForm.Information" 
              placeholder="请输入选课时间段描述"
            />
          </el-form-item>
          
          <el-form-item label="学年" prop="Year">
            <el-input-number
              v-model="editForm.Year"
              :min="2020"
              :max="2030"
              controls-position="right"
              placeholder="请输入学年"
            />
          </el-form-item>
          
          <el-form-item label="学期" prop="Semester">
            <el-radio-group v-model="editForm.Semester">
              <el-radio :label="0">第一学期</el-radio>
              <el-radio :label="1">第二学期</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="处理类型" prop="Type">
            <el-radio-group v-model="editForm.Type">
              <el-radio :label="0">随机踢人</el-radio>
              <el-radio :label="1">人满为止</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="开始时间" prop="BeginTime">
            <el-date-picker
              v-model="editForm.BeginTime"
              type="datetime"
              placeholder="选择开始时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
          
          <el-form-item label="结束时间" prop="EndTime">
            <el-date-picker
              v-model="editForm.EndTime"
              type="datetime"
              placeholder="选择结束时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
          
          <el-form-item label="关联课程" prop="CourseIds">
            <div class="select-all-container">
              <el-checkbox 
                v-model="selectAllCourses" 
                @change="handleSelectAllCourses"
                :indeterminate="isIndeterminateCourses"
              >全选所有课程</el-checkbox>
              <span class="selected-count">已选择 {{ editForm.CourseIds.length }} 个课程</span>
            </div>
            <el-select
              v-model="editForm.CourseIds"
              multiple
              filterable
              placeholder="请选择课程"
              style="width: 100%"
              @change="handleCourseSelectChange"
            >
              <el-option
                v-for="course in availableCourses"
                :key="course.CourseId"
                :label="course.CourseName"
                :value="course.CourseId"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="关联专业" prop="MajorIds">
            <div class="select-all-container">
              <el-checkbox 
                v-model="selectAllMajors" 
                @change="handleSelectAllMajors"
                :indeterminate="isIndeterminateMajors"
              >全选所有专业</el-checkbox>
              <span class="selected-count">已选择 {{ editForm.MajorIds.length }} 个专业</span>
            </div>
            <el-select
              v-model="editForm.MajorIds"
              multiple
              filterable
              placeholder="请选择专业"
              style="width: 100%"
              @change="handleMajorSelectChange"
            >
              <el-option
                v-for="major in availableMajors"
                :key="major.MajorId"
                :label="`${major.MajorName} (${major.DepartmentName})`"
                :value="major.MajorId"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 管理课程对话框 -->
    <el-dialog 
      v-model="showManageCoursesDialog" 
      title="管理课程" 
      width="800px"
      v-if="currentSelection"
    >
      <div class="manage-content">
        <div class="section">
          <h4>已关联课程 ({{ currentSelection.Courses.length }})</h4>
          <el-table :data="currentSelection.Courses" height="300" border>
            <el-table-column prop="CourseId" label="课程ID" width="100" />
            <el-table-column prop="CourseName" label="课程名称" width="200" />
            <el-table-column prop="Credits" label="学分" width="80" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button 
                  size="small" 
                  type="danger" 
                  @click="removeCourseFromSelection(row.CourseId)"
                >移除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="section">
          <h4>添加新课程</h4>
          <div class="add-controls">
            <el-select
              v-model="newCourseIds"
              multiple
              filterable
              placeholder="选择要添加的课程"
              style="width: 300px; margin-right: 10px"
            >
              <el-option
                v-for="course in unselectedCourses"
                :key="course.CourseId"
                :label="course.CourseName"
                :value="course.CourseId"
              />
            </el-select>
            <el-button 
              type="primary" 
              @click="addCoursesToSelection" 
              :disabled="!newCourseIds.length"
            >添加选中课程</el-button>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 处理选课结果对话框 -->
    <el-dialog
      v-model="showProcessDialog"
      title="处理选课结果"
      width="500px"
      v-if="currentSelection"
    >
      <div class="process-content">
        <p>确定要处理选课时间段 <strong>{{ currentSelection.Information }}</strong> 的选课结果吗？</p>
        <p>处理类型：<strong>{{ currentSelection.Type === 0 ? '随机踢人' : '人满为止' }}</strong></p>
        <p>学年学期：<strong>{{ currentSelection.Year }}-{{ currentSelection.Year + 1 }}学年 {{ currentSelection.Semester === 0 ? '第一学期' : '第二学期' }}</strong></p>
        <p>处理状态：<strong>{{ currentSelection.IsProcessed ? '已处理' : '未处理' }}</strong></p>
        
        <el-alert
          v-if="currentSelection.Type === 0"
          title="随机踢人模式：将从超出容量的课程中随机踢出多余学生"
          type="warning"
          :closable="false"
          style="margin: 15px 0"
        />
        <el-alert
          v-else
          title="人满为止模式：只处理未超员的课程，超员课程需要手动处理"
          type="info"
          :closable="false"
          style="margin: 15px 0"
        />
        
        <el-checkbox v-model="forceProcess">强制处理（即使选课未结束）</el-checkbox>
      </div>
      
      <template #footer>
        <el-button @click="showProcessDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmProcessResult" :loading="processing">确定处理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, computed, watch } from 'vue';
import { ElMessage, ElMessageBox, FormRules } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import {
  getAllSelections,
  createSelection,
  updateSelection,
  deleteSelection,
  addCoursesToSelection as apiAddCourses,
  removeCoursesFromSelection as apiRemoveCourses,
  processSelectionResult,
  getAllCourses,
  getAllMajors,
  getAllCourseIds,
  getAllMajorIds
} from '@/api/AdminPublicSelection';
import type { 
  AdminPublicSelectionDto, 
  CreateSelectionDto, 
  UpdateSelectionDto,
  SelectionCourseDto,
  SelectionMajorDto,
  ProcessSelectionResultDto
} from '@/api/AdminPublicSelection/types';

// 数据状态
const selections = ref<AdminPublicSelectionDto[]>([]);
const availableCourses = ref<SelectionCourseDto[]>([]);
const availableMajors = ref<SelectionMajorDto[]>([]);
const currentSelection = ref<AdminPublicSelectionDto | null>(null);
const newCourseIds = ref<string[]>([]);
const forceProcess = ref(false);

// 新增状态 - 全选功能
const selectAllCourses = ref(false);
const selectAllMajors = ref(false);
const isIndeterminateCourses = ref(false);
const isIndeterminateMajors = ref(false);
const allCourseIds = ref<string[]>([]);
const allMajorIds = ref<string[]>([]);

// 对话框状态
const showEditDialog = ref(false);
const showManageCoursesDialog = ref(false);
const showProcessDialog = ref(false);
const isEditing = ref(false);
const saving = ref(false);
const processing = ref(false);

// 表单数据
const editForm = reactive({
  SelectionId: '',
  Information: '',
  BeginTime: '',
  EndTime: '',
  Type: 0, // 0:随机踢人 1:人满为止
  Semester: 0, // 0:第一学期 1:第二学期
  Year: new Date().getFullYear(), // 默认当前年份
  CourseIds: [] as string[],
  MajorIds: [] as string[]
});

// 表单验证规则
const formRules: FormRules = {
  Information: [{ required: true, message: '请输入描述信息', trigger: 'blur' }],
  BeginTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  EndTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  Year: [{ required: true, message: '请输入学年', trigger: 'blur' }],
  Type: [{ required: true, message: '请选择处理类型', trigger: 'change' }],
  Semester: [{ required: true, message: '请选择学期', trigger: 'change' }]
};

// 计算属性 - 检查是否已选择所有课程
const isAllCoursesSelected = computed(() => {
  return editForm.CourseIds.length === allCourseIds.value.length && allCourseIds.value.length > 0;
});

// 计算属性 - 检查是否已选择所有专业
const isAllMajorsSelected = computed(() => {
  return editForm.MajorIds.length === allMajorIds.value.length && allMajorIds.value.length > 0;
});

// 计算未选择的课程
const unselectedCourses = computed(() => {
  if (!currentSelection.value) return availableCourses.value;
  
  const selectedCourseIds = currentSelection.value.Courses.map(c => c.CourseId);
  return availableCourses.value.filter(course => !selectedCourseIds.includes(course.CourseId));
});

// 监听课程选择变化
watch(() => editForm.CourseIds, (newVal) => {
  selectAllCourses.value = newVal.length === allCourseIds.value.length && allCourseIds.value.length > 0;
  isIndeterminateCourses.value = newVal.length > 0 && newVal.length < allCourseIds.value.length;
});

// 监听专业选择变化
watch(() => editForm.MajorIds, (newVal) => {
  selectAllMajors.value = newVal.length === allMajorIds.value.length && allMajorIds.value.length > 0;
  isIndeterminateMajors.value = newVal.length > 0 && newVal.length < allMajorIds.value.length;
});

// 处理课程全选
const handleSelectAllCourses = (value: boolean) => {
  if (value) {
    editForm.CourseIds = [...allCourseIds.value];
  } else {
    editForm.CourseIds = [];
  }
};

// 处理专业全选
const handleSelectAllMajors = (value: boolean) => {
  if (value) {
    editForm.MajorIds = [...allMajorIds.value];
  } else {
    editForm.MajorIds = [];
  }
};

// 处理课程选择变化
const handleCourseSelectChange = () => {
  selectAllCourses.value = editForm.CourseIds.length === allCourseIds.value.length;
  isIndeterminateCourses.value = editForm.CourseIds.length > 0 && editForm.CourseIds.length < allCourseIds.value.length;
};

// 处理专业选择变化
const handleMajorSelectChange = () => {
  selectAllMajors.value = editForm.MajorIds.length === allMajorIds.value.length;
  isIndeterminateMajors.value = editForm.MajorIds.length > 0 && editForm.MajorIds.length < allMajorIds.value.length;
};

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  return new Date(dateTime).toLocaleString('zh-CN');
};

// 加载选课时间段列表
const loadSelections = async () => {
  try {
    const response = await getAllSelections();
    if (response.IsSuccess) {
      selections.value = response.Result;
    } else {
      ElMessage.error('加载选课时间段失败: ' + response.Msg);
    }
  } catch (error) {
    ElMessage.error('加载选课时间段失败: ' + (error as Error).message);
  }
};

// 加载可用课程和专业
const loadAvailableData = async () => {
  try {
    const [coursesResponse, majorsResponse] = await Promise.all([
      getAllCourses(),
      getAllMajors()
    ]);

    if (coursesResponse.IsSuccess) {
      availableCourses.value = coursesResponse.Result;
    } else {
      ElMessage.error('加载课程失败: ' + coursesResponse.Msg);
    }

    if (majorsResponse.IsSuccess) {
      availableMajors.value = majorsResponse.Result;
    } else {
      ElMessage.error('加载专业失败: ' + majorsResponse.Msg);
    }
  } catch (error) {
    ElMessage.error('加载数据失败: ' + (error as Error).message);
  }
};

// 加载所有课程ID
const loadAllCourseIds = async () => {
  try {
    const response = await getAllCourseIds();
    if (response.IsSuccess) {
      allCourseIds.value = response.Result;
    } else {
      ElMessage.error('加载课程ID失败: ' + response.Msg);
    }
  } catch (error) {
    ElMessage.error('加载课程ID失败: ' + (error as Error).message);
  }
};

// 加载所有专业ID
const loadAllMajorIds = async () => {
  try {
    const response = await getAllMajorIds();
    if (response.IsSuccess) {
      allMajorIds.value = response.Result;
    } else {
      ElMessage.error('加载专业ID失败: ' + response.Msg);
    }
  } catch (error) {
    ElMessage.error('加载专业ID失败: ' + (error as Error).message);
  }
};

// 处理创建
const handleCreate = () => {
  isEditing.value = false;
  Object.assign(editForm, {
    SelectionId: '',
    Information: '',
    BeginTime: '',
    EndTime: '',
    Type: 0,
    Semester: 0,
    Year: new Date().getFullYear(),
    CourseIds: [],
    MajorIds: []
  });
  showEditDialog.value = true;
};

// 处理编辑
const handleEdit = (selection: AdminPublicSelectionDto) => {
  isEditing.value = true;
  Object.assign(editForm, {
    SelectionId: selection.SelectionId,
    Information: selection.Information,
    BeginTime: selection.BeginTime,
    EndTime: selection.EndTime,
    Type: selection.Type,
    Semester: selection.Semester,
    Year: selection.Year,
    CourseIds: selection.Courses.map(c => c.CourseId),
    MajorIds: selection.Majors.map(m => m.MajorId)
  });
  showEditDialog.value = true;
};

// 处理管理课程
const handleManageCourses = (selection: AdminPublicSelectionDto) => {
  currentSelection.value = selection;
  showManageCoursesDialog.value = true;
  newCourseIds.value = [];
};

// 处理选课结果
const handleProcessResult = (selection: AdminPublicSelectionDto) => {
  currentSelection.value = selection;
  forceProcess.value = false;
  showProcessDialog.value = true;
};

// 确认处理选课结果
const confirmProcessResult = async () => {
  if (!currentSelection.value) return;
  
  processing.value = true;
  try {
    const data: ProcessSelectionResultDto = {
      SelectionId: currentSelection.value.SelectionId,
      ForceProcess: forceProcess.value
    };
    
    const response = await processSelectionResult(data);
    if (response.IsSuccess) {
      ElMessage.success('处理选课结果成功');
      showProcessDialog.value = false;
      loadSelections(); // 刷新列表
    } else {
      ElMessage.error('处理选课结果失败: ' + response.Msg);
    }
  } catch (error) {
    ElMessage.error('处理选课结果失败: ' + (error as Error).message);
  } finally {
    processing.value = false;
  }
};

// 处理删除
const handleDelete = async (selectionId: string) => {
  try {
    await ElMessageBox.confirm('确定要删除这个选课时间段吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    const response = await deleteSelection(selectionId);
    if (response.IsSuccess) {
      ElMessage.success('删除成功');
      loadSelections();
    } else {
      ElMessage.error('删除失败: ' + response.Msg);
    }
  } catch (error) {
    if ((error as Error).message !== 'cancel') {
      ElMessage.error('删除失败: ' + (error as Error).message);
    }
  }
};

// 保存表单
const handleSave = async () => {
  saving.value = true;
  try {
    if (isEditing.value) {
      const updateData: UpdateSelectionDto = {
        SelectionId: editForm.SelectionId,
        Information: editForm.Information,
        BeginTime: editForm.BeginTime,
        EndTime: editForm.EndTime,
        Type: editForm.Type,
        Semester: editForm.Semester,
        Year: editForm.Year,
        CourseIds: editForm.CourseIds,
        MajorIds: editForm.MajorIds
      };
      
      const response = await updateSelection(updateData);
      if (response.IsSuccess) {
        ElMessage.success('更新成功');
        showEditDialog.value = false;
        loadSelections();
      } else {
        ElMessage.error('更新失败: ' + response.Msg);
      }
    } else {
      const createData: CreateSelectionDto = {
        Information: editForm.Information,
        BeginTime: editForm.BeginTime,
        EndTime: editForm.EndTime,
        Type: editForm.Type,
        Semester: editForm.Semester,
        Year: editForm.Year,
        CourseIds: editForm.CourseIds,
        MajorIds: editForm.MajorIds
      };
      
      const response = await createSelection(createData);
      if (response.IsSuccess) {
        ElMessage.success('创建成功');
        showEditDialog.value = false;
        loadSelections();
      } else {
        ElMessage.error('创建失败: ' + response.Msg);
      }
    }
  } catch (error) {
    ElMessage.error('操作失败: ' + (error as Error).message);
  } finally {
    saving.value = false;
  }
};

// 添加课程到选课时间段
const addCoursesToSelection = async () => {
  if (!currentSelection.value || !newCourseIds.value.length) return;
  
  try {
    const response = await apiAddCourses(currentSelection.value.SelectionId, newCourseIds.value);
    if (response.IsSuccess) {
      ElMessage.success('添加课程成功');
      loadSelections();
      // 刷新当前选课时间段数据
      const updated = selections.value.find(s => s.SelectionId === currentSelection.value?.SelectionId);
      if (updated) {
        currentSelection.value = updated;
      }
      newCourseIds.value = [];
    } else {
      ElMessage.error('添加课程失败: ' + response.Msg);
    }
  } catch (error) {
    ElMessage.error('添加课程失败: ' + (error as Error).message);
  }
};

// 从选课时间段移除课程
const removeCourseFromSelection = async (courseId: string) => {
  if (!currentSelection.value) return;
  
  try {
    const response = await apiRemoveCourses(currentSelection.value.SelectionId, [courseId]);
    if (response.IsSuccess) {
      ElMessage.success('移除课程成功');
      loadSelections();
      // 刷新当前选课时间段数据
      const updated = selections.value.find(s => s.SelectionId === currentSelection.value?.SelectionId);
      if (updated) {
        currentSelection.value = updated;
      }
    } else {
      ElMessage.error('移除课程失败: ' + response.Msg);
    }
  } catch (error) {
    ElMessage.error('移除课程失败: ' + (error as Error).message);
  }
};

// 初始化
onMounted(() => {
  loadSelections();
  loadAvailableData();
  loadAllCourseIds();
  loadAllMajorIds();
});
</script>

<style scoped>
.admin-public-selection-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 100px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.header h2 {
  margin: 0;
  color: #303133;
}

.selection-list {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  flex: 1;
  overflow: auto;
}

.manage-content {
  max-height: 60vh;
  overflow-y: auto;
}

.section {
  margin-bottom: 20px;
}

.section h4 {
  margin: 0 0 10px 0;
  color: #606266;
}

.add-controls {
  display: flex;
  align-items: center;
}

.process-content {
  line-height: 1.8;
}

.dialog-content {
  max-height: 60vh;
  overflow-y: auto;
  padding-right: 10px;
}

/* 新增样式 */
.select-all-container {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.select-all-container .el-checkbox {
  margin-right: 10px;
}

.selected-count {
  font-size: 12px;
  color: #909399;
  margin-left: auto;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  font-weight: 600;
}

:deep(.el-table--border) {
  border: 1px solid #ebeef5;
}

/* 自定义滚动条样式 */
:deep(.selection-list)::-webkit-scrollbar,
:deep(.manage-content)::-webkit-scrollbar,
:deep(.dialog-content)::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

:deep(.selection-list)::-webkit-scrollbar-track,
:deep(.manage-content)::-webkit-scrollbar-track,
:deep(.dialog-content)::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

:deep(.selection-list)::-webkit-scrollbar-thumb,
:deep(.manage-content)::-webkit-scrollbar-thumb,
:deep(.dialog-content)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

:deep(.selection-list)::-webkit-scrollbar-thumb:hover,
:deep(.manage-content)::-webkit-scrollbar-thumb:hover,
:deep(.dialog-content)::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>