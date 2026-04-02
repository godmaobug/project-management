<template>
  <div class="tasks-page">
    <div class="page-header">
      <h2 class="page-title">任务管理</h2>
      <el-button type="primary" :icon="Plus" @click="showCreateDialog = true">创建任务</el-button>
    </div>

    <el-card shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="项目">
          <el-select v-model="searchForm.projectId" placeholder="选择项目" clearable @change="handleSearch">
            <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable @change="handleSearch">
            <el-option label="待办" value="TODO" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="开发完成" value="DEV_DONE" />
            <el-option label="测试中" value="TESTING" />
            <el-option label="已完成" value="DONE" />
          </el-select>
        </el-form-item>
      </el-form>

      <el-table :data="filteredTasks" stripe v-loading="loading">
        <el-table-column label="标题" min-width="200">
          <template #default="{ row }">
            <el-link type="primary">{{ row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="项目" width="120" prop="projectName" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getTypeLabel(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="优先级" width="90">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)" size="small">{{ row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="指派给" width="120">
          <template #default="{ row }">{{ row.assignee?.realName || row.assignee?.username || '-' }}</template>
        </el-table-column>
        <el-table-column label="预估工时" width="100">
          <template #default="{ row }">{{ row.estimatedHours }}h</template>
        </el-table-column>
      </el-table>
    </el-card>

    <CreateTaskDialog v-model="showCreateDialog" @success="fetchTasks" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { getTasksByProject, getMyTasks } from '@/api/tasks'
import { getMyProjects } from '@/api/projects'
import CreateTaskDialog from '@/views/dashboard/components/CreateTaskDialog.vue'

const loading = ref(false)
const tasks = ref([])
const projects = ref([])
const showCreateDialog = ref(false)
const searchForm = reactive({ projectId: '', status: '' })

const filteredTasks = computed(() => {
  let result = tasks.value
  if (searchForm.status) result = result.filter(t => t.status === searchForm.status)
  return result
})

const fetchProjects = async () => {
  try { projects.value = await getMyProjects() } catch (e) {}
}

const fetchTasks = async () => {
  loading.value = true
  try {
    if (searchForm.projectId) {
      tasks.value = await getTasksByProject(searchForm.projectId)
    } else {
      tasks.value = await getMyTasks()
    }
  } catch (e) {} finally { loading.value = false }
}

const handleSearch = () => fetchTasks()

const getTypeLabel = (t) => ({ DEVELOPMENT: '开发', TEST: '测试', DESIGN: '设计', REVIEW: '评审', BUG_FIX: 'Bug修复', OTHER: '其他' }[t])
const getStatusType = (s) => ({ TODO: 'info', IN_PROGRESS: 'warning', DEV_DONE: 'primary', TESTING: 'warning', DONE: 'success' }[s])
const getStatusLabel = (s) => ({ TODO: '待办', IN_PROGRESS: '进行中', DEV_DONE: '开发完成', TESTING: '测试中', DONE: '已完成' }[s])
const getPriorityType = (p) => ({ URGENT: 'danger', HIGH: 'warning', MEDIUM: 'info', LOW: 'success' }[p])

onMounted(() => { fetchProjects().then(() => fetchTasks()) })
</script>

<style scoped>
.tasks-page { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; }
.search-form { margin-bottom: 20px; }
</style>
