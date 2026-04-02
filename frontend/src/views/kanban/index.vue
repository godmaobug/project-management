<template>
  <div class="kanban-page">
    <div class="page-header">
      <h2 class="page-title">看板</h2>
      <el-select v-model="selectedProject" placeholder="选择项目" @change="loadKanban">
        <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id" />
      </el-select>
    </div>

    <div v-if="selectedProject" class="kanban-board">
      <div class="kanban-column" v-for="column in columns" :key="column.status">
        <div class="column-header">
          <span class="column-title">{{ column.name }}</span>
          <el-tag size="small" type="info">{{ getTasksByStatus(column.status).length }}</el-tag>
        </div>
        <div class="column-content">
          <div
            v-for="task in getTasksByStatus(column.status)"
            :key="task.id"
            class="kanban-card"
            @click="handleTaskClick(task)"
          >
            <div class="card-header">
              <el-tag size="small" :type="getTypeType(task.type)">{{ getTypeLabel(task.type) }}</el-tag>
              <el-tag size="small" :type="getPriorityType(task.priority)">{{ task.priority }}</el-tag>
            </div>
            <div class="card-title">{{ task.title }}</div>
            <div class="card-footer">
              <el-avatar :size="24" :src="task.assignee?.avatar">
                {{ task.assignee?.realName?.charAt(0) || '?' }}
              </el-avatar>
              <span v-if="task.estimatedHours" class="hours">{{ task.estimatedHours }}h</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-empty v-else description="请先选择项目" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTasksByProject, updateTaskStatus } from '@/api/tasks'
import { getMyProjects } from '@/api/projects'

const projects = ref([])
const selectedProject = ref('')
const tasks = ref([])

const columns = [
  { name: '待办', status: 'TODO' },
  { name: '进行中', status: 'IN_PROGRESS' },
  { name: '开发完成', status: 'DEV_DONE' },
  { name: '测试中', status: 'TESTING' },
  { name: '已完成', status: 'DONE' }
]

const fetchProjects = async () => {
  try {
    projects.value = await getMyProjects()
    if (projects.value.length > 0) {
      selectedProject.value = projects.value[0].id
      loadKanban()
    }
  } catch (e) {}
}

const loadKanban = async () => {
  if (!selectedProject.value) return
  try {
    tasks.value = await getTasksByProject(selectedProject.value)
  } catch (e) {}
}

const getTasksByStatus = (status) => tasks.value.filter(t => t.status === status)

const getTypeType = (t) => ({ DEVELOPMENT: 'primary', TEST: 'success', DESIGN: 'warning', BUG_FIX: 'danger' }[t] || 'info')
const getTypeLabel = (t) => ({ DEVELOPMENT: '开发', TEST: '测试', DESIGN: '设计', BUG_FIX: 'Bug修复', REVIEW: '评审', OTHER: '其他' }[t])
const getPriorityType = (p) => ({ URGENT: 'danger', HIGH: 'warning', MEDIUM: 'info', LOW: 'success' }[p])

const handleTaskClick = (task) => {
  console.log('点击任务', task)
}

onMounted(fetchProjects)
</script>

<style scoped>
.kanban-page { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; }

.kanban-board {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.kanban-column {
  min-width: 280px;
  max-width: 280px;
  background: #f5f7fa;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
}

.column-header {
  padding: 12px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e4e7ed;
}

.column-title {
  font-weight: 600;
  color: #303133;
}

.column-content {
  padding: 12px;
  flex: 1;
  overflow-y: auto;
}

.kanban-card {
  background: #fff;
  border-radius: 6px;
  padding: 12px;
  margin-bottom: 10px;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  transition: all 0.2s;
}

.kanban-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.card-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 12px;
  line-height: 1.5;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hours {
  font-size: 12px;
  color: #909399;
}
</style>
