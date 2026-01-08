import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../components/DashboardView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/workbench',
    component: () => import('../layouts/MainLayout.vue'),
    children: [
      // 工作台首页
      {
        path: '',
        name: 'workbench',
        component: HomeView,
        meta: { 
          title: '工作台', 
          requiresAuth: true,
          roles: [0, 1, 2, 3] // 所有登录用户可访问
        }
      },
      { 
        path: '/settings', 
        name: 'settings', 
        component: () => import('../views/User/SettingsView.vue'), 
        meta: { 
          title: '系统设置',
          requiresAuth: true,
          roles: [0,1,2,3] // 仅一级管理员可访问
        } 
      },
      { 
        path: '/mymessage', 
        name: 'mymessage', 
        component: () => import('../views/Message/MyMessageView.vue'), 
        meta: { 
          title: '我的消息',
          requiresAuth: true,
          roles: [0, 1, 2, 3] // 所有用户可访问
        } 
      },
      { 
        path: '/handle', 
        name: 'handle', 
        component: () => import('../views/Handle/MessageHandleView.vue'), 
        meta: { 
          title: '消息处理',
          requiresAuth: true,
          roles: [0, 1] // 一级和二级管理员可访问
        } 
      },
      { 
        path: '/messageapply', 
        name: 'messageapply', 
        component: () => import('../views/Message/ApplyView.vue'), 
        meta: { 
          title: '消息申请',
          requiresAuth: true,
          roles: [1, 2, 3] // 二级管理员、教师和学生可访问
        } 
      },
      // 用户中心
      { 
        path: '/profile', 
        name: 'profile', 
        component: () => import('../views/User/ProfileView.vue'), 
        meta: { 
          title: '个人信息', 
          requiresAuth: true,
          roles: [0, 1, 2, 3] // 所有用户可访问
        } 
      },
      // 用户中心
      { 
        path: '/studentinfo', 
        name: 'studentinfo', 
        component: () => import('../views/User/StudentInfoView.vue'), 
        meta: { 
          title: '学生信息',
          requiresAuth: true,
          roles: [1] // 仅二级管理员可访问
        } 
      },
      { 
        path: '/teacherinfo', 
        name: 'teacherinfo', 
        component: () => import('../views/User/TeacherInfoView.vue'), 
        meta: { 
          title: '教师信息',
          requiresAuth: true,
          roles: [1] // 仅二级管理员可访问
        } 
      },
      { 
        path: '/allinfo', 
        name: 'allinfo', 
        component: () => import('../views/User/AllInfoView.vue'), 
        meta: { 
          title: '用户信息',
          requiresAuth: true,
          roles: [0] // 仅一级管理员可访问
        } 
      },
      
      // 申请处理
      { 
        path: '/competitionhandle', 
        name: 'competitionhandle', 
        component: () => import('../views/Handle/CompetitionHandleView.vue'), 
        meta: { 
          title: '竞赛处理',
          requiresAuth: true,
          roles: [1] // 仅二级管理员可访问
        } 
      },
      { 
        path: '/imagehandle', 
        name: 'imagehandle', 
        component: () => import('../views/Handle/ImageHandleView.vue'), 
        meta: { 
          title: '头像审核',
          requiresAuth: true,
          roles: [1] // 仅二级管理员可访问
        } 
      },
      { 
        path: '/deferrredhandle', 
        name: 'deferrredhandle', 
        component: () => import('../views/Handle/DeferredHandle.vue'), 
        meta: { 
          title: '缓考处理',
          requiresAuth: true,
          roles: [1] // 仅二级管理员可访问
        } 
      },
      { 
        path: '/advisorhandle', 
        name: 'advisorhandle', 
        component: () => import('../views/Handle/AdvisorHandleView.vue'), 
        meta: { 
          title: '导师处理',
          requiresAuth: true,
          roles: [1] // 仅二级管理员可访问
        } 
      },
      { 
        path: '/teachinghandle', 
        name: 'teachinghandle', 
        component: () => import('../views/Handle/TeachingHandleView.vue'), 
        meta: { 
          title: '授课处理',
          requiresAuth: true,
          roles: [1] // 仅二级管理员可访问
        } 
      },
      
      // 考试信息
      { 
        path: '/schedule', 
        name: 'schedule', 
        component: () => import('../views/Exam/ExamScheduleView.vue'), 
        meta: { 
          title: '考试安排',
          requiresAuth: true,
          roles: [0, 1, 2, 3] // 所有用户可访问
        } 
      },
      { 
        path: '/paper', 
        name: 'paper', 
        component: () => import('../views/Exam/PaperView.vue'), 
        meta: { 
          title: '试卷中心',
          requiresAuth: true,
          roles: [0, 1] // 一级和二级管理员可访问
        } 
      },
      { 
        path: '/myexam', 
        name: 'myexam', 
        component: () => import('../views/Exam/MyExamView.vue'), 
        meta: { 
          title: '我的考试',
          requiresAuth: true,
          roles: [3] // 仅学生可访问
        } 
      },
      { 
        path: '/deffered', 
        name: 'deffered', 
        component: () => import('../views/Exam/DefferedApplyView.vue'), 
        meta: { 
          title: '申请缓考',
          requiresAuth: true,
          roles: [3] // 仅学生可访问
        } 
      },
      { 
        path: '/myinvigilate', 
        name: 'myinvigilate', 
        component: () => import('../views/Exam/MyInvigilateView.vue'), 
        meta: { 
          title: '我的监考',
          requiresAuth: true,
          roles: [2] // 仅教师可访问
        } 
      },
      { 
        path: '/organize', 
        name: 'organize', 
        component: () => import('../views/Exam/OrganizeView.vue'), 
        meta: { 
          title: '组织考试',
          requiresAuth: true,
          roles: [2] // 仅教师可访问
        } 
      },
      
      // 竞赛信息
      { 
        path: '/competitionquery', 
        name: 'competitionquery', 
        component: () => import('../views/Competition/CompetitionQueryView.vue'), 
        meta: { 
          title: '竞赛查询',
          requiresAuth: true,
          roles: [0, 1, 2, 3] // 所有用户可访问
        } 
      },
      { 
        path: '/competitionapply', 
        name: 'competitionapply', 
        component: () => import('../views/Competition/CompetitionApplyView.vue'), 
        meta: { 
          title: '竞赛申请',
          requiresAuth: true,
          roles: [2,3] // 仅教师可访问
        } 
      },
      
      { 
        path: '/weeklycourse', 
        name: 'weeklycourse', 
        component: () => import('../views/Course/WeeklyCourseView.vue'), 
        meta: { 
          title: '每周课表',
          requiresAuth: true,
          roles: [0, 1, 2, 3] // 所有用户可访问
        } 
      },
      { 
        path: '/myteaching', 
        name: 'myteaching', 
        component: () => import('../views/Course/MyTeachingView.vue'), 
        meta: { 
          title: '我的授课',
          requiresAuth: true,
          roles: [2] // 仅教师可访问
        } 
      },
      {
        path: '/selection-rounds',
        name: 'studentselectcourse',
        component: () => import('@/views/Course/StudentSelectCourseView.vue'),
        meta: { 
          title: '学生选课',
          requiresAuth: true,
          roles: [3] // 仅学生可访问
        }
      },

      { 
        path: '/studentselectcourse', 
        name: 'SelectionRounds', 
        component: () => import('../views/Course/SelectionRoundView.vue'), 
        meta: { 
          title: '选课轮次', 
          requiresAuth: true,
          roles: [3] // 仅一级管理员可访问
        } 
      },
      { 
        path: '/qrcode', 
        name: 'qrcode', 
        component: () => import('../views/Course/QrcodeView.vue'), 
        meta: { 
          title: '扫码签到',
          requiresAuth: true,
          roles: [2] // 仅教师可访问
        } 
      },
      { 
        path: '/signlist', 
        name: 'signlist', 
        component: () => import('../views/Course/SignList.vue'), 
        meta: { 
          title: '签到列表',
          requiresAuth: true,
          roles: [ 2] // 所有用户可访问
        } 
      },
      { 
        path: '/checkin', 
        name: 'studentcheckin', 
        component: () => import('../views/Course/StudentSignInView.vue'), 
        meta: { 
          title: '学生签到', 
          requiresAuth: true,
          roles: [3] // 仅学生可访问
        } 
      },
      { 
        path: '/teachingapply', 
        name: 'teachingapply', 
        component: () => import('../views/Course/TeachingApplyView.vue'), 
        meta: { 
          title: '授课申请',
          requiresAuth: true,
          roles: [2] // 仅教师可访问
        } 
      },
      { 
        path: '/grade', 
        name: 'grade', 
        component: () => import('../views/Course/GradeView.vue'), 
        meta: { 
          title: '成绩登记',
          requiresAuth: true,
          roles: [2] // 仅教师可访问
        } 
      },
      
      // 成绩查询
      { 
        path: '/gradequery', 
        name: 'gradequery', 
        component: () => import('../views/Grade/GradeQueryView.vue'), 
        meta: { 
          title: '成绩查询',
          requiresAuth: true,
          roles: [3] // 仅学生可访问
        } 
      },
      
      // 教室使用情况
      { 
        path: '/classroom', 
        name: 'classroom', 
        component: () => import('../views/Classroom/ClassroomView.vue'), 
        meta: { 
          title: '教室使用情况',
          requiresAuth: true,
          roles: [0, 1, 2, 3] // 所有用户可访问
        } 
      },
      
      // 导师信息
      { 
        path: '/advisorinfo', 
        name: 'advisorinfo', 
        component: () => import('../views/Advisor/AdvisorInfoView.vue'), 
        meta: { 
          title: '信息编辑',
          requiresAuth: true,
          roles: [2] // 仅教师可访问
        } 
      },
      { 
        path: '/advisorapply', 
        name: 'advisorapply', 
        component: () => import('../views/Advisor/AdvisorApplyView.vue'), 
        meta: { 
          title: '导师申请',
          requiresAuth: true,
          roles: [2] // 仅教师可访问
        } 
      },
      { 
        path: '/advisorquery', 
        name: 'advisorquery', 
        component: () => import('../views/Advisor/AdvisorQueryView.vue'), 
        meta: { 
          title: '导师查询',
          requiresAuth: true,
          roles: [0, 1, 2, 3] // 所有用户可访问
        } 
      },
      { 
        path: '/advisorselect', 
        name: 'advisorselect', 
        component: () => import('../views/Advisor/StudentSelectAdvisorView.vue'), 
        meta: { 
          title: '选择导师',
          requiresAuth: true,
          roles: [3] // 仅学生可访问
        } 
      },
      {
        path: '/schedulecourse',
        name: 'schedulecourse',
        component: () => import('../views/Course/AdminScheduleCourseView.vue'),
        meta: { 
          title: '排课管理',
          requiresAuth: true,
          roles: [0] // 仅一级管理员可访问
        }
      },

      // 管理员选课选导师轮次创建
      { 
        path: '/adminpublishselection', 
        name: 'adminpublishselection', 
        component: () => import('../views/Admin/AdminPublicSelectionView.vue'), 
        meta: { 
          title: '管理员发布选择信息',
          requiresAuth: true,
          roles: [0] // 仅一级管理员可访问
        } 
      },
    ]
  },
  {
    path: '/',
    name: 'login',
    component: () => import('../views/Login/LoginView.vue'),
    meta: { 
      title: '登录',
      requiresAuth: false // 不需要认证
    }
  },
  {
    path: '/login',
    redirect: '/',
    meta: { 
      requiresAuth: false // 不需要认证
    }
  },
  // 消息中心
  {
    path: '/forget',
    name: 'forget',
    component: () => import('../views/Login/Forget.vue'),
    meta: { 
      title: '密码重置',
      requiresAuth: false // 不需要认证
    }
  },
  {
    path: '/information',
    name: 'information',
    component: () => import('../views/Login/Information.vue'),
    meta: { 
      title: '使用说明',
      requiresAuth: false // 不需要认证
    }
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router