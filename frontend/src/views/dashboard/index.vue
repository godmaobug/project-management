<template>
  <div class="dashboard">
    <h2 class="page-title">工作台</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon blue"><el-icon><Folder /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalProjects }}</div>
            <div class="stat-label">我的项目</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon green"><el-icon><List /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalTasks }}</div>
            <div class="stat-label">全部任务</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon orange"><el-icon><Timer /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.pendingTasks + stats.inProgressTasks }}</div>
            <div class="stat-label">待办任务</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon red"><el-icon><Warning /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.newBugs }}</div>
            <div class="stat-label">待处理Bug</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷入口 -->
    <el-card class="quick-actions" shadow="never">
      <template #header>
        <span>快捷入口</span>
      </template>
      <el-space :size="16">
        <el-button type="primary" :icon="Plus" @click="showCreateRequirement = true">创建需求</el-button>
        <el-button type="success" :icon="Plus" @click="showCreateTask = true">创建任务</el-button>
        <el-button type="danger" :icon="Plus" @click="showCreateBug = true">提交Bug</el-button>
        <el-button :icon="Folder" @click="$router.push('/projects')">查看项目</el-button>
      </el-space>
    </el-card>

    <el-row :gutter="16" class="content-row">
      <!-- 待办事项 -->
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>我的待办</span>
              <el-link type="primary" @click="$router.push('/tasks')">查看全部</el-link>
            </div>
          </template>
          
          <el-table :data="todoList" stripe>
            <el-table-column label="标题" min-width="200">
              <template #default="{ row }">
                <el-link type="primary" @click="handleTodoClick(row)">{{ row.title }}</el-link>
              </template>
            </el-table-column>
            <el-table-column label="类型" width="100">
              <template #default="{ row }">
                <el-tag :type="getTypeTag(row.type)">{{ getTypeLabel(row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row)">{{ getStatusLabel(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="优先级" width="100">
              <template #default="{ row }">
                <el-tag :type="getPriorityType(row.priority)">{{ row.priority }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          
          <el-empty v-if="todoList.length === 0" description="暂无待办事项" />
        </el-card>
      </el-col>

      <!-- 项目进度 -->
      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>项目进度</span>
            </div>
          </template>
          
          <div v-for="item in projectProgresses" :key="item.projectId" class="progress-item">
            <div class="progress-header">
              <span class="project-name">{{ item.projectName }}</span>
              <span class="progress-text">{{ item.completedTasks }}/{{ item.totalTasks }}</span>
            </div>
            <el-progress :percentage="Math.round(item.progress)" :status="item.progress === 100 ? 'success' : ''" />
          </div>
          
          <el-empty v-if="projectProgresses.length === 0" description="暂无项目" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 创建需求对话框 -->
    <CreateRequirementDialog v-model="showCreateRequirement" @success="refreshData" />
    
    <!-- 创建任务对话框 -->
    <CreateTaskDialog v-model="showCreateTask" @success="refreshData" />
    
    <!-- 创建Bug对话框 -->
    <CreateBugDialog v-model="showCreateBug" @success="refreshData" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getDashboardStats } from '@/api/dashboard'
import CreateRequirementDialog from './components/CreateRequirementDialog.vue'
import CreateTaskDialog from './components/CreateTaskDialog.vue'
import CreateBugDialog from './components/CreateBugDialog.vue'

const router = useRouter()
const loading = ref(false)
const stats = ref({
  totalProjects: 0,
  totalTasks: 0,
  pendingTasks: 0,
  inProgressTasks: 0,
  newBugs: 0,
  todoItems: [],
  projectProgresses: []
})

const showCreateRequirement = ref(false)
const showCreateTask = ref(false)
const showCreateBug = ref(false)

const todoList = computed(() => stats.value.todoItems || [])
const projectProgresses = computed(() => stats.value.projectProgresses || [])

const fetchData = async () => {
  loading.value = true
  try {
    const data = await getDashboardStats()
    stats.value = data
  } catch (error) {
    console.error('获取统计数据失败:', error)
  } finally {
    loading.value = false
  }
}

const refreshData = () => {
  fetchData()
}

const getTypeTag = (type) => {
  const map = { TASK: 'primary', REQUIREMENT: 'success', BUG: 'danger' }
  return map[type] || 'info'
}

const getTypeLabel = (type) => {
  const map = { TASK: '任务', REQUIREMENT: '需求', BUG: 'Bug' }
  return map[type] || type
}

const getStatusType = (row) => {
  const status = row.status
  if (status === 'TODO' || status === 'PENDING' || status === 'NEW') return 'info'
  if (status === 'IN_PROGRESS' || status === 'CONFIRMED') return 'warning'
  if (status === 'DONE' || status === 'CLOSED') return 'success'
  return 'info'
}

const getStatusLabel = (status) => {
  const map = {
    TODO: '待办', IN_PROGRESS: '进行中', DONE: '已完成',
    PENDING: '待处理', CONFIRMED: '已确认', CLOSED: '已关闭', NEW: '新建'
  }
  return map[status] || status
}

const getPriorityType = (priority) => {
  const map = { URGENT: 'danger', HIGH: 'warning', MEDIUM: 'info', LOW: 'success', FATAL: 'danger', SERIOUS: 'warning', NORMAL: 'info', MINOR: 'success' }
  return map[priority] || 'info'
}

const handleTodoClick = (row) => {
  const map = { TASK: '/tasks', REQUIREMENT: '/requirements', BUG: '/bugs' }
  router.push(`${map[row.type]}`)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.dashboard {
  padding-bottom: 20px;
}

.page-title {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 8px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
  margin-right: 16px;
}

.stat-icon.blue { background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%); }
.stat-icon.green { background: linear-gradient(135deg, #52c41a 0%, #95de64 100%); }
.stat-icon.orange { background: linear-gradient(135deg, #fa8c16 0%, #ffc53d 100%); }
.stat-icon.red { background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%); }

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.quick-actions {
  margin-bottom: 20px;
}

.content-row {
  margin-top: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.progress-item {
  margin-bottom: 16px;
}

.progress-item:last-child {
  margin-bottom: 0;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.project-name {
  font-size: 14px;
  color: #303133;
}

.progress-text {
  font-size: 12px;
  color: #909399;
}
</style>
