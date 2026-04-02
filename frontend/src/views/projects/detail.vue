<template>
  <div class="project-detail">
    <el-page-header @back="$router.back()" title="返回项目列表" />
    
    <el-card v-if="project" class="project-info-card" shadow="never">
      <div class="project-header">
        <div>
          <h2 class="project-title">{{ project.name }}</h2>
          <p class="project-desc">{{ project.description || '暂无描述' }}</p>
        </div>
        <el-space>
          <el-button :icon="Setting">设置</el-button>
          <el-button type="primary" :icon="Plus" @click="showAddMember = true">添加成员</el-button>
        </el-space>
      </div>
      
      <el-descriptions :column="4" border>
        <el-descriptions-item label="项目类型">{{ getTypeLabel(project.type) }}</el-descriptions-item>
        <el-descriptions-item label="项目负责人">{{ project.owner?.realName || project.owner?.username }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ formatDate(project.startDate) }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ formatDate(project.endDate) }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <el-statistic title="需求数" :value="stats.requirements" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="任务数" :value="stats.tasks" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="Bug数" :value="stats.bugs" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="成员数" :value="project?.members?.length || 0" />
      </el-col>
    </el-row>

    <el-card shadow="never">
      <template #header>
        <span>项目成员</span>
      </template>
      <el-table :data="project?.members || []" stripe>
        <el-table-column label="成员" min-width="150">
          <template #default="{ row }">
            <div class="member-info">
              <el-avatar :size="32" :src="row.user?.avatar">
                {{ row.user?.realName?.charAt(0) || row.user?.username?.charAt(0) }}
              </el-avatar>
              <span>{{ row.user?.realName || row.user?.username }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="角色" width="120">
          <template #default="{ row }">
            <el-tag>{{ getRoleLabel(row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="加入时间" width="180">
          <template #default="{ row }">{{ formatDateTime(row.joinedAt) }}</template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加成员对话框 -->
    <AddMemberDialog v-model="showAddMember" :project-id="route.params.id" @success="fetchProject" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Plus, Setting } from '@element-plus/icons-vue'
import { getProject } from '@/api/projects'
import { getRequirementsByProject } from '@/api/requirements'
import { getTasksByProject } from '@/api/tasks'
import { getBugsByProject } from '@/api/bugs'
import AddMemberDialog from './components/AddMemberDialog.vue'
import dayjs from 'dayjs'

const route = useRoute()
const project = ref(null)
const stats = ref({ requirements: 0, tasks: 0, bugs: 0 })
const showAddMember = ref(false)

const fetchProject = async () => {
  try {
    const projectId = route.params.id
    project.value = await getProject(projectId)
    
    // 获取统计数据
    const [reqs, tasks, bugs] = await Promise.all([
      getRequirementsByProject(projectId),
      getTasksByProject(projectId),
      getBugsByProject(projectId)
    ])
    
    stats.value = {
      requirements: reqs.length,
      tasks: tasks.length,
      bugs: bugs.length
    }
  } catch (error) {
    console.error('获取项目详情失败:', error)
  }
}

const formatDate = (date) => {
  return date ? dayjs(date).format('YYYY-MM-DD') : '-'
}

const formatDateTime = (date) => {
  return date ? dayjs(date).format('YYYY-MM-DD HH:mm') : '-'
}

const getTypeLabel = (type) => {
  const map = { SCRUM: 'Scrum敏捷', KANBAN: '看板', GENERAL: '通用项目' }
  return map[type] || type
}

const getRoleLabel = (role) => {
  const map = { PRODUCT: '产品', DEVELOPER: '开发', TESTER: '测试', MEMBER: '成员', MANAGER: '管理者' }
  return map[role] || role
}

onMounted(() => {
  fetchProject()
})
</script>

<style scoped>
.project-detail {
  padding-bottom: 20px;
}

.project-info-card {
  margin: 20px 0;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.project-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.project-desc {
  font-size: 14px;
  color: #606266;
}

.stats-row {
  margin-bottom: 20px;
}

.member-info {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
