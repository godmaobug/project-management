<template>
  <div class="requirements-page">
    <div class="page-header">
      <h2 class="page-title">需求管理</h2>
      <el-button type="primary" :icon="Plus" @click="showCreateDialog = true">创建需求</el-button>
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
            <el-option label="未开始" value="PENDING" />
            <el-option label="评审中" value="REVIEWING" />
            <el-option label="已规划" value="PLANNED" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchForm.keyword" placeholder="搜索需求标题" clearable @keyup.enter="handleSearch">
            <template #suffix>
              <el-icon @click="handleSearch"><Search /></el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>

      <el-table :data="filteredRequirements" stripe v-loading="loading">
        <el-table-column type="index" width="50" />
        <el-table-column label="标题" min-width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">{{ row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="项目" width="120" prop="projectName" />
        <el-table-column label="优先级" width="90">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)">{{ getPriorityLabel(row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="指派给" width="120">
          <template #default="{ row }">{{ row.assignee?.realName || row.assignee?.username || '-' }}</template>
        </el-table-column>
        <el-table-column label="故事点" width="80" prop="storyPoints" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="filteredRequirements.length === 0" description="暂无数据" />
    </el-card>

    <CreateRequirementDialog v-model="showCreateDialog" @success="fetchRequirements" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { Plus, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRequirementsByProject, deleteRequirement } from '@/api/requirements'
import { getMyProjects } from '@/api/projects'
import CreateRequirementDialog from '@/views/dashboard/components/CreateRequirementDialog.vue'

const loading = ref(false)
const requirements = ref([])
const projects = ref([])
const showCreateDialog = ref(false)
const searchForm = reactive({
  projectId: '',
  status: '',
  keyword: ''
})

const filteredRequirements = computed(() => {
  let result = requirements.value
  if (searchForm.status) {
    result = result.filter(r => r.status === searchForm.status)
  }
  if (searchForm.keyword) {
    result = result.filter(r => r.title.includes(searchForm.keyword))
  }
  return result
})

const fetchProjects = async () => {
  try {
    projects.value = await getMyProjects()
  } catch (error) {
    console.error(error)
  }
}

const fetchRequirements = async () => {
  loading.value = true
  try {
    if (searchForm.projectId) {
      requirements.value = await getRequirementsByProject(searchForm.projectId)
    } else if (projects.value.length > 0) {
      const allReqs = await Promise.all(
        projects.value.map(p => getRequirementsByProject(p.id))
      )
      requirements.value = allReqs.flat()
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  fetchRequirements()
}

const handleView = (row) => {
  console.log('查看', row)
}

const handleEdit = (row) => {
  console.log('编辑', row)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除此需求吗？', '提示', { type: 'warning' })
    await deleteRequirement(row.id)
    ElMessage.success('删除成功')
    fetchRequirements()
  } catch (e) {
    if (e !== 'cancel') console.error(e)
  }
}

const getPriorityType = (p) => ({ URGENT: 'danger', HIGH: 'warning', MEDIUM: 'info', LOW: 'success' }[p])
const getPriorityLabel = (p) => ({ URGENT: '紧急', HIGH: '高', MEDIUM: '中', LOW: '低' }[p])
const getStatusType = (s) => ({ PENDING: 'info', REVIEWING: 'warning', PLANNED: 'primary', IN_PROGRESS: 'warning', COMPLETED: 'success', REJECTED: 'danger' }[s])
const getStatusLabel = (s) => ({ PENDING: '未开始', REVIEWING: '评审中', PLANNED: '已规划', IN_PROGRESS: '进行中', COMPLETED: '已完成', REJECTED: '已拒绝' }[s])

onMounted(() => {
  fetchProjects().then(() => fetchRequirements())
})
</script>

<style scoped>
.requirements-page { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; }
.search-form { margin-bottom: 20px; }
</style>
