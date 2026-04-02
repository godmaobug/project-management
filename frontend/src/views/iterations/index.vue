<template>
  <div class="iterations-page">
    <div class="page-header">
      <h2 class="page-title">迭代管理</h2>
      <el-button type="primary" :icon="Plus" @click="showCreateDialog = true">创建迭代</el-button>
    </div>

    <el-card shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="项目">
          <el-select v-model="searchForm.projectId" placeholder="选择项目" clearable @change="handleSearch">
            <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
      </el-form>

      <el-timeline>
        <el-timeline-item
          v-for="item in iterations"
          :key="item.id"
          :type="item.status === 'ACTIVE' ? 'primary' : item.status === 'COMPLETED' ? 'success' : 'info'"
          :timestamp="formatDate(item.startDate) + ' ~ ' + formatDate(item.endDate)"
        >
          <el-card shadow="hover" class="iteration-card">
            <div class="iteration-header">
              <h4 class="iteration-name">{{ item.name }}</h4>
              <el-tag :type="getStatusType(item.status)">{{ getStatusLabel(item.status) }}</el-tag>
            </div>
            <p class="iteration-goal">{{ item.goal || '暂无目标' }}</p>
            <div class="iteration-stats">
              <div class="stat-item">
                <span class="stat-label">进度</span>
                <el-progress :percentage="Math.round(item.progress)" style="width: 150px" />
              </div>
              <div class="stat-item">
                <span class="stat-label">故事点</span>
                <span class="stat-value">{{ item.completedStoryPoints }}/{{ item.totalStoryPoints }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">任务</span>
                <span class="stat-value">{{ item.completedTasks }}/{{ item.totalTasks }}</span>
              </div>
            </div>
            <div class="iteration-actions">
              <el-button v-if="item.status === 'PLANNING'" type="primary" size="small" @click="handleStart(item)">开始迭代</el-button>
              <el-button v-if="item.status === 'ACTIVE'" type="success" size="small" @click="handleComplete(item)">完成迭代</el-button>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>

      <el-empty v-if="iterations.length === 0" description="暂无迭代" />
    </el-card>

    <CreateIterationDialog v-model="showCreateDialog" :projects="projects" @success="fetchIterations" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getIterationsByProject, startIteration, completeIteration } from '@/api/iterations'
import { getMyProjects } from '@/api/projects'
import CreateIterationDialog from './components/CreateIterationDialog.vue'
import dayjs from 'dayjs'

const loading = ref(false)
const iterations = ref([])
const projects = ref([])
const showCreateDialog = ref(false)
const searchForm = reactive({ projectId: '' })

const fetchProjects = async () => {
  try { projects.value = await getMyProjects() } catch (e) {}
}

const fetchIterations = async () => {
  loading.value = true
  try {
    if (searchForm.projectId) {
      iterations.value = await getIterationsByProject(searchForm.projectId)
    } else if (projects.value.length > 0) {
      const all = await Promise.all(projects.value.map(p => getIterationsByProject(p.id)))
      iterations.value = all.flat().sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    }
  } catch (e) {} finally { loading.value = false }
}

const handleSearch = () => fetchIterations()
const formatDate = (d) => d ? dayjs(d).format('MM-DD') : '-'

const getStatusType = (s) => ({ PLANNING: 'info', ACTIVE: 'primary', COMPLETED: 'success' }[s])
const getStatusLabel = (s) => ({ PLANNING: '规划中', ACTIVE: '进行中', COMPLETED: '已完成' }[s])

const handleStart = async (item) => {
  try {
    await ElMessageBox.confirm(`确定要开始迭代 "${item.name}" 吗？`, '提示')
    await startIteration(item.id)
    ElMessage.success('已开始')
    fetchIterations()
  } catch (e) { if (e !== 'cancel') console.error(e) }
}

const handleComplete = async (item) => {
  try {
    await ElMessageBox.confirm(`确定要完成迭代 "${item.name}" 吗？`, '提示')
    await completeIteration(item.id)
    ElMessage.success('已完成')
    fetchIterations()
  } catch (e) { if (e !== 'cancel') console.error(e) }
}

onMounted(() => { fetchProjects().then(() => fetchIterations()) })
</script>

<style scoped>
.iterations-page { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; }
.search-form { margin-bottom: 20px; }
.iteration-card { margin-bottom: 10px; }
.iteration-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.iteration-name { font-size: 16px; font-weight: 600; margin: 0; }
.iteration-goal { color: #606266; font-size: 14px; margin: 10px 0; }
.iteration-stats { display: flex; gap: 30px; margin: 15px 0; }
.stat-item { display: flex; align-items: center; gap: 10px; }
.stat-label { color: #909399; font-size: 13px; }
.stat-value { font-weight: 600; color: #303133; }
.iteration-actions { margin-top: 15px; padding-top: 15px; border-top: 1px solid #ebeef5; }
</style>
