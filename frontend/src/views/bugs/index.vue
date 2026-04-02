<template>
  <div class="bugs-page">
    <div class="page-header">
      <h2 class="page-title">缺陷管理</h2>
      <el-button type="primary" :icon="Plus" @click="showCreateDialog = true">提交Bug</el-button>
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
            <el-option label="新建" value="NEW" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已解决" value="FIXED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="严重程度">
          <el-select v-model="searchForm.severity" placeholder="选择严重程度" clearable @change="handleSearch">
            <el-option label="致命" value="FATAL" />
            <el-option label="严重" value="SERIOUS" />
            <el-option label="一般" value="NORMAL" />
            <el-option label="轻微" value="MINOR" />
          </el-select>
        </el-form-item>
      </el-form>

      <el-table :data="filteredBugs" stripe v-loading="loading">
        <el-table-column label="标题" min-width="200">
          <template #default="{ row }">
            <el-link type="primary">{{ row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="项目" width="120" prop="projectName" />
        <el-table-column label="严重程度" width="90">
          <template #default="{ row }">
            <el-tag :type="getSeverityType(row.severity)" size="small">{{ getSeverityLabel(row.severity) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="报告人" width="120">
          <template #default="{ row }">{{ row.reporter?.realName || row.reporter?.username }}</template>
        </el-table-column>
        <el-table-column label="指派给" width="120">
          <template #default="{ row }">{{ row.assignee?.realName || row.assignee?.username || '-' }}</template>
        </el-table-column>
        <el-table-column label="创建时间" width="160">
          <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
        </el-table-column>
      </el-table>
    </el-card>

    <CreateBugDialog v-model="showCreateDialog" @success="fetchBugs" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { getBugsByProject, getMyBugs } from '@/api/bugs'
import { getMyProjects } from '@/api/projects'
import CreateBugDialog from '@/views/dashboard/components/CreateBugDialog.vue'
import dayjs from 'dayjs'

const loading = ref(false)
const bugs = ref([])
const projects = ref([])
const showCreateDialog = ref(false)
const searchForm = reactive({ projectId: '', status: '', severity: '' })

const filteredBugs = computed(() => {
  let result = bugs.value
  if (searchForm.status) result = result.filter(b => b.status === searchForm.status)
  if (searchForm.severity) result = result.filter(b => b.severity === searchForm.severity)
  return result
})

const fetchProjects = async () => {
  try { projects.value = await getMyProjects() } catch (e) {}
}

const fetchBugs = async () => {
  loading.value = true
  try {
    if (searchForm.projectId) {
      bugs.value = await getBugsByProject(searchForm.projectId)
    } else {
      bugs.value = await getMyBugs()
    }
  } catch (e) {} finally { loading.value = false }
}

const handleSearch = () => fetchBugs()
const formatDate = (d) => d ? dayjs(d).format('MM-DD HH:mm') : '-'

const getSeverityType = (s) => ({ FATAL: 'danger', SERIOUS: 'warning', NORMAL: 'info', MINOR: 'success' }[s])
const getSeverityLabel = (s) => ({ FATAL: '致命', SERIOUS: '严重', NORMAL: '一般', MINOR: '轻微' }[s])
const getStatusType = (s) => ({ NEW: 'danger', CONFIRMED: 'warning', FIXED: 'primary', CLOSED: 'success', REJECTED: 'info' }[s])
const getStatusLabel = (s) => ({ NEW: '新建', CONFIRMED: '已确认', FIXED: '已解决', CLOSED: '已关闭', REJECTED: '已拒绝' }[s])

onMounted(() => { fetchProjects().then(() => fetchBugs()) })
</script>

<style scoped>
.bugs-page { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; }
.search-form { margin-bottom: 20px; }
</style>
