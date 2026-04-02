<template>
  <div class="projects-page">
    <div class="page-header">
      <h2 class="page-title">项目管理</h2>
      <el-button type="primary" :icon="Plus" @click="showCreateDialog = true">创建项目</el-button>
    </div>

    <el-row :gutter="16">
      <el-col v-for="project in projects" :key="project.id" :span="8">
        <el-card class="project-card" shadow="hover" @click="goToDetail(project.id)">
          <div class="project-header">
            <div class="project-icon" :class="getProjectTypeClass(project.type)">
              <el-icon size="24"><Folder /></el-icon>
            </div>
            <div class="project-info">
              <h3 class="project-name">{{ project.name }}</h3>
              <el-tag size="small" :type="getTypeTag(project.type)">{{ getTypeLabel(project.type) }}</el-tag>
            </div>
          </div>
          
          <p class="project-desc">{{ project.description || '暂无描述' }}</p>
          
          <div class="project-meta">
            <div class="meta-item">
              <el-icon><User /></el-icon>
              <span>{{ project.members?.length || 0 }} 成员</span>
            </div>
            <div class="meta-item">
              <el-icon><Calendar /></el-icon>
              <span>{{ formatDate(project.startDate) }} ~ {{ formatDate(project.endDate) }}</span>
            </div>
          </div>
          
          <div class="project-progress">
            <div class="progress-header">
              <span>项目进度</span>
              <span class="progress-value">{{ Math.round(project.progress || 0) }}%</span>
            </div>
            <el-progress :percentage="Math.round(project.progress || 0)" :status="project.progress === 100 ? 'success' : ''" />
          </div>
          
          <div class="project-footer">
            <div class="owner">
              <el-avatar :size="24" :src="project.owner?.avatar">
                {{ project.owner?.realName?.charAt(0) || project.owner?.username?.charAt(0) }}
              </el-avatar>
              <span class="owner-name">{{ project.owner?.realName || project.owner?.username }}</span>
            </div>
            <el-tag :type="getStatusType(project.status)" size="small">
              {{ getStatusLabel(project.status) }}
            </el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="projects.length === 0" description="暂无项目" />

    <!-- 创建项目对话框 -->
    <CreateProjectDialog v-model="showCreateDialog" @success="fetchProjects" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyProjects, deleteProject, archiveProject } from '@/api/projects'
import CreateProjectDialog from './components/CreateProjectDialog.vue'
import dayjs from 'dayjs'

const router = useRouter()
const projects = ref([])
const loading = ref(false)
const showCreateDialog = ref(false)

const fetchProjects = async () => {
  loading.value = true
  try {
    projects.value = await getMyProjects()
  } catch (error) {
    console.error('获取项目列表失败:', error)
  } finally {
    loading.value = false
  }
}

const goToDetail = (id) => {
  router.push(`/projects/${id}`)
}

const formatDate = (date) => {
  return date ? dayjs(date).format('MM-DD') : '-'
}

const getProjectTypeClass = (type) => {
  const map = { SCRUM: 'scrum', KANBAN: 'kanban', GENERAL: 'general' }
  return map[type] || 'general'
}

const getTypeTag = (type) => {
  const map = { SCRUM: 'success', KANBAN: 'warning', GENERAL: 'info' }
  return map[type] || 'info'
}

const getTypeLabel = (type) => {
  const map = { SCRUM: 'Scrum', KANBAN: '看板', GENERAL: '通用' }
  return map[type] || type
}

const getStatusType = (status) => {
  const map = { ACTIVE: 'success', PAUSED: 'warning', COMPLETED: 'info', ARCHIVED: 'info' }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = { ACTIVE: '进行中', PAUSED: '已暂停', COMPLETED: '已完成', ARCHIVED: '已归档' }
  return map[status] || status
}

const handleDelete = async (project) => {
  try {
    await ElMessageBox.confirm(`确定要删除项目 "${project.name}" 吗？`, '提示', {
      type: 'warning'
    })
    await deleteProject(project.id)
    ElMessage.success('删除成功')
    fetchProjects()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleArchive = async (project) => {
  try {
    await ElMessageBox.confirm(`确定要归档项目 "${project.name}" 吗？`, '提示', {
      type: 'info'
    })
    await archiveProject(project.id)
    ElMessage.success('归档成功')
    fetchProjects()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('归档失败:', error)
    }
  }
}

onMounted(() => {
  fetchProjects()
})
</script>

<style scoped>
.projects-page {
  padding-bottom: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.project-card {
  margin-bottom: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.project-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.project-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
}

.project-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #fff;
}

.project-icon.scrum { background: linear-gradient(135deg, #52c41a 0%, #95de64 100%); }
.project-icon.kanban { background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%); }
.project-icon.general { background: linear-gradient(135deg, #722ed1 0%, #b37feb 100%); }

.project-info {
  flex: 1;
}

.project-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.project-desc {
  font-size: 13px;
  color: #606266;
  margin-bottom: 12px;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.project-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
  font-size: 12px;
  color: #909399;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.project-progress {
  margin-bottom: 16px;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 12px;
  color: #606266;
}

.progress-value {
  font-weight: 600;
  color: #409EFF;
}

.project-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.owner {
  display: flex;
  align-items: center;
  gap: 8px;
}

.owner-name {
  font-size: 13px;
  color: #606266;
}
</style>
