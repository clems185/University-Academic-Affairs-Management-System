<template>
  <div class="grade-query-container">
    <el-card class="query-card">
      <template #header>
        <div class="card-header">
          <span>我的成绩</span>
        </div>
      </template>
      
             <!-- 学生基本信息 -->
       <div class="student-info" v-if="gradeSummary">
         <span class="label">学号：</span>
         <span class="value">{{ gradeSummary.studentId }}</span>
         <span class="label">姓名：</span>
         <span class="value">{{ gradeSummary.studentName }}</span>
         <span class="label">总学分：</span>
         <span class="value">{{ gradeSummary.totalCredits }}</span>
         <span class="label">通过学分：</span>
         <span class="value">{{ gradeSummary.passedCredits }}</span>
         <span class="label">通过率：</span>
         <span class="value">{{ gradeSummary.passRate ? gradeSummary.passRate.toFixed(1) : '0.0' }}%</span>
         <span class="label">平均绩点：</span>
         <span class="value">{{ gradeSummary.averageGpa ? gradeSummary.averageGpa.toFixed(2) : '0.00' }}</span>
       </div>
    </el-card>

    <!-- 按学期分组的成绩 -->
    <div v-for="(grades, semester) in semesterGrades" :key="semester" class="semester-section">
      <el-card class="semester-card">
        <template #header>
          <div class="card-header">
            <span class="semester-title">{{ semester }}</span>
            <span class="semester-credits">
              学分：{{ grades.totalCredits }}
            </span>
            <span class="semester-gpa">
              平均绩点：{{ grades.averageGpa ? grades.averageGpa.toFixed(2) : '0.00' }}
            </span>
          </div>
        </template>
        
                 <el-table :data="grades.grades" stripe style="width: 100%">
           <el-table-column prop="CourseName" label="课程名称" />
           <el-table-column prop="ClassName" label="班级名称" width="150" />
           <el-table-column prop="Grade" label="成绩等级" width="100">
             <template #default="scope">
               <el-tag v-if="scope.row.Grade" :type="getGradeTagType(scope.row.Grade)">
                 {{ scope.row.Grade }}
               </el-tag>
               <span v-else class="no-grade">未录入</span>
             </template>
           </el-table-column>
           <el-table-column prop="Credits" label="学分" width="80" />
           <el-table-column prop="State" label="修读状态" width="100">
             <template #default="scope">
               <el-tag :type="getStateTagType(scope.row.State)">
                 {{ scope.row.State }}
               </el-tag>
             </template>
           </el-table-column>
         </el-table>
      </el-card>
    </div>

    <!-- 加载状态 -->
    <el-card v-if="loading" class="loading-card">
      <div class="loading-content">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>正在加载成绩数据...</span>
      </div>
    </el-card>

    <!-- 无数据提示 -->
    <el-card v-if="!loading && Object.keys(semesterGrades).length === 0" class="empty-card">
      <div class="empty-content">
        <el-icon><Document /></el-icon>
        <span>暂无成绩数据</span>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getGradeSummary, getGradesBySemester } from '@/api/gradeQuery/gradeQuery'
import type { GradeQueryResult, GradeSummaryResult } from '@/api/gradeQuery/types'

// 成绩汇总
const gradeSummary = ref<GradeSummaryResult | null>(null)
// 按学期分组的成绩，包含每个学期的汇总信息
const semesterGrades = ref<Record<string, { grades: any[], averageGpa: number, totalCredits: number }>>({})
const loading = ref(false)

// 获取当前学生的成绩
const loadStudentGrades = async () => {
  try {
    loading.value = true
    
    // 这里应该从登录状态获取当前学生ID，暂时使用示例ID
    const currentStudentId = sessionStorage.getItem("userId")||'' // 需要根据实际登录状态获取
    
    console.log('开始加载学生成绩，学生ID:', currentStudentId)
    
    // 获取按学期分组的成绩
    const semesterResult = await getGradesBySemester(currentStudentId)
    console.log('学期分组成绩API响应:', semesterResult)
    
    if (semesterResult.data.success) {
      // 处理每个学期的数据，计算汇总信息
      const processedSemesterData: Record<string, { grades: any[], averageGpa: number, totalCredits: number }> = {}
      
      Object.entries(semesterResult.data.data).forEach(([semester, grades]) => {
        const semesterGrades = grades as any[]
        let totalCredits = 0
        let totalGpa = 0
        let validGradeCount = 0
        
        semesterGrades.forEach((grade: any) => {
          totalCredits += grade.Credits || 0
          if (grade.Grade) {
            const gpa = getGpaByGrade(grade.Grade)
            totalGpa += gpa * (grade.Credits || 0)
            validGradeCount += (grade.Credits || 0)
          }
        })
        
        const averageGpa = validGradeCount > 0 ? totalGpa / validGradeCount : 0
        
        processedSemesterData[semester] = {
          grades: semesterGrades,
          averageGpa,
          totalCredits
        }
      })
      
      semesterGrades.value = processedSemesterData
      console.log('处理后的学期分组成绩数据:', semesterGrades.value)
      
      // 从学期数据中提取学生信息和计算汇总
      if (Object.keys(processedSemesterData).length > 0) {
        const firstSemester = Object.values(processedSemesterData)[0]
        if (firstSemester.grades && firstSemester.grades.length > 0) {
          const firstRecord = firstSemester.grades[0]
          
          // 计算汇总信息
          let totalCredits = 0
          let passedCredits = 0
          let totalGrades = 0
          let totalGpa = 0
          let validGradeCount = 0
          
          Object.values(processedSemesterData).forEach((semesterData) => {
            semesterData.grades.forEach((grade: any) => {
              totalCredits += grade.Credits || 0
              totalGrades++
              // 修复：按学分计算通过学分，而不是按课程数量
              if (grade.Grade && ['优', '良', '中', '及格'].includes(grade.Grade)) {
                passedCredits += grade.Credits || 0
              }
              // 计算GPA
              if (grade.Grade) {
                const gpa = getGpaByGrade(grade.Grade)
                totalGpa += gpa * (grade.Credits || 0)
                validGradeCount += (grade.Credits || 0)
              }
            })
          })
          
          const passRate = totalCredits > 0 ? (passedCredits / totalCredits) * 100 : 0
          const averageGpa = validGradeCount > 0 ? totalGpa / validGradeCount : 0
          
          gradeSummary.value = {
            studentId: firstRecord.StudentId,
            studentName: firstRecord.StudentName,
            grades: [],
            totalCredits,
            passedCredits,
            passRate,
            averageGpa
          }
          
          console.log('计算出的成绩汇总:', gradeSummary.value)
        }
      }
    } else {
      console.error('获取学期分组成绩失败:', semesterResult.data.message)
    }
    
    ElMessage.success('成绩加载成功')
  } catch (error) {
    console.error('加载成绩失败:', error)
    ElMessage.error('加载成绩失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 获取成绩等级标签类型
const getGradeTagType = (grade: string) => {
  switch (grade) {
    case '优':
      return 'success'
    case '良':
      return 'warning'
    case '中':
      return 'info'
    case '及格':
      return 'primary'
    case '不及格':
      return 'danger'
    default:
      return 'info'
  }
}

// 获取修读状态标签类型
const getStateTagType = (state: string) => {
  switch (state) {
    case '正在修读':
      return 'warning'
    case '重修':
      return 'danger'
    case '修读结束':
      return 'success'
    default:
      return 'info'
  }
}

// 获取绩点
const getGpaByGrade = (grade: string) => {
  switch (grade) {
    case '优':
      return 5.0
    case '良':
      return 4.0
    case '中':
      return 3.0
    case '及格':
      return 2.0
    case '不及格':
      return 0.0
    default:
      return 0.0
  }
}

// 页面加载时执行
onMounted(() => {
  loadStudentGrades()
})
</script>

<style scoped>
.grade-query-container {
  padding: 20px;
}

.query-card {
  margin-bottom: 20px;
}

.semester-section {
  margin-bottom: 20px;
}

.semester-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.semester-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.semester-credits {
  color: #67c23a;
  font-weight: 500;
}

.semester-gpa {
  color: #409eff;
  font-weight: 500;
}

.student-info {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
  padding: 10px 0;
}

.label {
  font-weight: bold;
  color: #606266;
}

.value {
  color: #409eff;
  font-weight: 500;
}

.loading-card,
.empty-card {
  text-align: center;
  padding: 40px;
}

.loading-content,
.empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  color: #909399;
}

.loading-content .el-icon {
  font-size: 24px;
  color: #409eff;
}

.empty-content .el-icon {
  font-size: 48px;
  color: #c0c4cc;
}

.no-grade {
  color: #909399;
  font-style: italic;
}
</style> 