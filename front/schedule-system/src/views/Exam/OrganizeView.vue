<template>
  <div class="app-container">
    <div class="header-bar">
      <h2>我组织的考试</h2>
      <el-button type="primary" @click="openApplyDialog">
        <el-icon><Plus /></el-icon> 申请新考试
      </el-button>
    </div>

    <el-table :data="tableData" stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="courseName" label="课程名称" />
      <el-table-column prop="proposedTime" label="考试时间" />
      <el-table-column prop="location" label="考试地点">
        <template #default="scope">
          <span>{{ scope.row.location || '待定' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="paperFileName" label="试卷文件">
        <template #default="scope">
          <el-link 
            v-if="scope.row.paperFileName" 
            type="primary" 
            :href="getPaperDownloadUrl(scope.row.applyId)" 
            target="_blank"
          >
            {{ scope.row.paperFileName }}
          </el-link>
          <span v-else>未上传</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-button 
            size="small" 
            type="danger" 
            @click="handleCancel(scope.row)"
          >
            取消考试
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="申请新考试" width="50%" @close="resetForm">
      <el-form :model="form" ref="formRef" label-width="100px">
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学年">
              <el-input-number v-model="form.year" :min="2024" :controls="false" placeholder="例如: 2025" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学期">
              <el-select v-model="form.semester" placeholder="请选择学期" style="width: 100%;">
                <el-option label="第一学期" :value="1" />
                <el-option label="第二学期" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="选择课程">
              <el-select 
                v-model="form.courseId" 
                placeholder="请选择课程" 
                style="width: 100%;"
                @change="onCourseChange"
                filterable
              >
                <el-option 
                  v-for="course in teacherCourses"
                  :key="course.courseId"
                  :label="course.courseName"
                  :value="course.courseId" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          
        </el-row>

         <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="考试形式">
              <el-select v-model="form.bookType" placeholder="请选择开卷/闭卷" style="width: 100%;">
                <el-option label="开卷" value="开卷" />
                <el-option label="闭卷" value="闭卷" />
              </el-select>
            </el-form-item>
          </el-col>
           <el-col :span="12">
            <el-form-item label="考试类型">
              <el-select v-model="form.examType" placeholder="请选择考试类型" style="width: 100%;">
                <el-option label="期中" value="期中" />
                <el-option label="期末" value="期末" />
                <el-option label="缓考" value="缓考" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="考试时间">
              <el-date-picker
                v-model="form.examTime"
                type="datetime"
                placeholder="选择考试日期和时间"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="考试时长">
              <el-input-number v-model="form.examDuration" :min="30" :step="10" style="width: 100%;">
                 <template #append>分钟</template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
         <el-col :span="12">
           <el-form-item label="教学楼">
              <el-select v-model="form.buildingId" placeholder="请先选择教学楼" style="width: 100%;" @change="onBuildingChange">
                <el-option v-for="b in buildings" :key="b.buildingId" :label="b.name" :value="b.buildingId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选择教室">
              <el-select v-model="form.classroomId" placeholder="请选择教室" style="width: 100%;" :disabled="!form.buildingId || availableClassrooms.length === 0">
                <el-option 
                  v-for="c in availableClassrooms" 
                  :key="c.classroomId" 
                  :label="`${c.roomNumber} (容量: ${c.capacity}人)`" 
                  :value="c.classroomId" 
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="上传试卷">
           <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :limit="1"
            @change="handleFileChange"
            @remove="handleFileRemove"
            accept=".doc,.docx,.pdf"
          >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">提交申请</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { ElMessage, ElMessageBox, UploadInstance, UploadFile } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { getExamApplyList, createExamApply, getTeacherCourses, getPaperDownloadUrl, cancelExamApply, getBuildings, getAvailableClassrooms } from '@/api/organize/Organize';
import { ExamApplyFormParams, ExamApplyItem, TeacherCourse, ClassInfo, Building, Classroom } from '@/api/organize/types';

const toCamelCase = (obj: any): any => {
  if (Array.isArray(obj)) {
    return obj.map(v => toCamelCase(v));
  } else if (obj !== null && obj.constructor === Object) {
    return Object.keys(obj).reduce((result, key) => {
      const newKey = key.charAt(0).toLowerCase() + key.slice(1);
      result[newKey] = toCamelCase(obj[key]);
      return result;
    }, {} as any);
  }
  return obj;
};

const tableData = ref<ExamApplyItem[]>([]);
const loading = ref(false);
const submitLoading = ref(false);
const dialogVisible = ref(false);
const uploadRef = ref<UploadInstance>();

const teacherCourses = ref<TeacherCourse[]>([]);    // 存储当前老师的所有课程
const availableClasses = ref<ClassInfo[]>([]);      // 当选择一个课程后，这里存储该课程下的班级
const buildings = ref<Building[]>([]);              // 存储所有教学楼列表
const availableClassrooms = ref<Classroom[]>([]);   // 存储根据时间筛选出的可用教室列表
const fetchClassroomsLoading = ref(false);          // 控制教室下拉框的加载状态

// 定义一个函数来生成表单的初始状态
const createInitialFormState = (): ExamApplyFormParams => ({
  courseId: null,
  classId: null,
   buildingId: null,
  classroomId: null,
  semester: 1,
  year: new Date().getFullYear(),
  bookType: '请选择开卷/闭卷',
  examType: '请选择考试类型',
  examTime: null,
  examDuration: 120,
  paperFile: null,
});

// 响应式的表单数据对象
const form = ref<ExamApplyFormParams>(createInitialFormState());

// 获取主列表数据
const fetchList = async () => {
  loading.value = true;
  try {
    const teacherId = sessionStorage.getItem('userId');
    if (teacherId) {
      const response = await getExamApplyList(teacherId);
      if (response.data.IsSuccess) {
        tableData.value = toCamelCase(response.data.Result);
      } else {
        ElMessage.error(response.data.Message || "获取列表失败");
      }
    } else {
      ElMessage.error("无法获取用户信息，请重新登录");
    }
  } catch (error) {
    console.error("获取申请列表失败:", error);
    ElMessage.error("网络错误，获取列表失败");
  } finally {
    loading.value = false;
  }
};

// 打开“申请新考试”弹窗 
const openApplyDialog = async () => {
  resetForm();
  dialogVisible.value = true;
  try {
    const teacherId = sessionStorage.getItem('userId');
    if (teacherId) {
      const res = await getTeacherCourses(teacherId);
      if (res.data.IsSuccess) {
        teacherCourses.value = toCamelCase(res.data.Result);
      } else {
        ElMessage.error(res.data.Message || "获取课程列表失败");
      }
    } else {
      ElMessage.error("无法获取用户信息，请重新登录");
    }
    const buildingRes = await getBuildings();
        if (buildingRes.data.IsSuccess) {
            buildings.value = toCamelCase(buildingRes.data.Result);
        }
  } catch (error) {
    console.error("获取课程列表失败:", error);
    ElMessage.error("网络错误，获取课程列表失败");
  }
};

// 根据选中的课程，更新“可选班级”列表
const onCourseChange = (selectedCourseId: string) => {
  form.value.classId = null;
  const selectedCourse = teacherCourses.value.find(c => c.courseId === selectedCourseId);
  availableClasses.value = selectedCourse ? selectedCourse.classes : [];
};

// 监视教学楼、考试时间、考试时长的变化，自动获取可用教室
const fetchAvailableClassrooms = async () => {
  if (!form.value.examTime || !form.value.examDuration || !form.value.buildingId) {
    return;
  }
  fetchClassroomsLoading.value = true;
  availableClassrooms.value = []; // 清空旧数据
  form.value.classroomId = null; // 清空已选中的教室

  try {
    const params = {
      startTime: form.value.examTime.toISOString(),
      duration: form.value.examDuration,
      buildingId: form.value.buildingId,
    };
    const res = await getAvailableClassrooms(params);
    if (res.data.IsSuccess) {
      availableClassrooms.value = toCamelCase(res.data.Result);
      if(availableClassrooms.value.length === 0){
        ElMessage.warning('当前时间段该教学楼没有可用教室');
      }
    }
  } catch (error) {
    console.error("获取教室列表失败:", error);
  } finally {
    fetchClassroomsLoading.value = false;
  }
};

// 当教学楼改变时，也触发教室的重新获取
const onBuildingChange = () => {
  fetchAvailableClassrooms();
};

// 处理文件上传组件的变化
const handleFileChange = (uploadFile: UploadFile) => {
    form.value.paperFile = uploadFile.raw!;
};

const handleFileRemove = () => {
    form.value.paperFile = null;
};

// 处理表单提交
const handleSubmit = async () => {
  if (!form.value.courseId || !form.value.classId || !form.value.examTime || !form.value.paperFile) {
      ElMessage.warning('请填写所有必填项并上传试卷');
      return;
  }

  submitLoading.value = true;
  const formData = new FormData();

  Object.keys(form.value).forEach(keyStr => {
    const key = keyStr as keyof ExamApplyFormParams;
    const value = form.value[key];
    if (value) { // 确保值不为 null
      if (key === 'examTime' && value instanceof Date) {
        // 将日期对象转换为 ISO 字符串
        formData.append(key, value.toISOString());
      } else if (key !== 'paperFile') {
        // 其他非文件字段直接转为字符串
        formData.append(key, value.toString());
      }
    }
  });
  
  // 单独处理文件
  if (form.value.paperFile) {
    formData.append('paperFile', form.value.paperFile);
  }
  
  try {
    const response = await createExamApply(formData);
    if (response.data.IsSuccess) {
      ElMessage.success('申请提交成功！');
      dialogVisible.value = false;
      await fetchList();
    } else {
      ElMessage.error(response.data.Message || '提交失败');
    }
  } catch (error) {
    console.error("提交时发生网络错误:", error);
    ElMessage.error('网络请求失败或表单内容有误');
  } finally {
    submitLoading.value = false;
  }
};

const resetForm = () => {
    form.value = createInitialFormState();
    uploadRef.value?.clearFiles();
    teacherCourses.value = [];
    availableClasses.value = [];
};

// 取消申请
const handleCancel = async (row: ExamApplyItem) => {
  try {
    await ElMessageBox.confirm(
      '您确定要取消这次考试申请吗？',
      '确认取消',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );

    const response = await cancelExamApply(row.applyId);
    if (response.data.IsSuccess) {
       ElMessage.success('申请已取消');
       await fetchList();
    } else {
       ElMessage.error(response.data.Message || '取消失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error("取消申请失败:", error);
      ElMessage.error('操作失败，请重试');
    }
  }
};

const formatStatus = (status: string) => {
  if (status === 'Y') return '已通过';
  if (status === 'N') return '已驳回';
  return '审核中';
};

const getStatusTag = (status: string) => {
  if (status === 'Y') return 'success';
  if (status === 'N') return 'danger';
  return 'primary';
};

// 当组件第一次被渲染到页面上时执行
onMounted(() => {
  fetchList();// 页面一加载就去获取主列表数据
});

watch(() => form.value.examTime, () => {
  fetchAvailableClassrooms();// 侦听考试时间的变化，自动更新教室列表
});

watch(() => form.value.examDuration, () => {
  fetchAvailableClassrooms();// 侦听考试时长的变化，自动更新教室列表
});
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>