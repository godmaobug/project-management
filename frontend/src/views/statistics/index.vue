<template>
  <div class="statistics-page">
    <h2 class="page-title">统计报表</h2>

    <el-row :gutter="16" class="charts-row">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>任务状态分布</template>
          <div ref="taskChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>Bug严重程度分布</template>
          <div ref="bugChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="project-stats">
      <template #header>
        <div class="card-header">
          <span>项目统计</span>
          <el-select v-model="selectedProject" placeholder="选择项目" clearable @change="loadProjectStats">
            <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </div>
      </template>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="需求数">{{ projectStats.requirements }}</el-descriptions-item>
        <el-descriptions-item label="任务数">{{ projectStats.tasks }}</el-descriptions-item>
        <el-descriptions-item label="Bug数">{{ projectStats.bugs }}</el-descriptions-item>
        <el-descriptions-item label="已完成任务">{{ projectStats.completedTasks }}</el-descriptions-item>
        <el-descriptions-item label="已关闭Bug">{{ projectStats.closedBugs }}</el-descriptions-item>
        <el-descriptions-item label="任务完成率">{{ projectStats.taskRate }}%</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { getMyProjects } from '@/api/projects'
import { getRequirementsByProject } from '@/api/requirements'
import { getTasksByProject } from '@/api/tasks'
import { getBugsByProject } from '@/api/bugs'

const projects = ref([])
const selectedProject = ref('')
const taskChartRef = ref(null)
const bugChartRef = ref(null)
const projectStats = ref({ requirements: 0, tasks: 0, bugs: 0, completedTasks: 0, closedBugs: 0, taskRate: 0 })

let taskChart = null
let bugChart = null

const initCharts = () => {
  taskChart = echarts.init(taskChartRef.value)
  bugChart = echarts.init(bugChartRef.value)
  
  taskChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      type: 'pie',
      radius: '50%',
      data: [
        { value: 0, name: '待办' },
        { value: 0, name: '进行中' },
        { value: 0, name: '开发完成' },
        { value: 0, name: '测试中' },
        { value: 0, name: '已完成' }
      ],
      emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' } }
    }]
  })
  
  bugChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: { show: false, position: 'center' },
      emphasis: { label: { show: true, fontSize: 20, fontWeight: 'bold' } },
      data: [
        { value: 0, name: '致命' },
        { value: 0, name: '严重' },
        { value: 0, name: '一般' },
        { value: 0, name: '轻微' }
      ]
    }]
  })
}

const loadProjectStats = async () => {
  if (!selectedProject.value) return
  try {
    const [reqs, tasks, bugs] = await Promise.all([
      getRequirementsByProject(selectedProject.value),
      getTasksByProject(selectedProject.value),
      getBugsByProject(selectedProject.value)
    ])
    
    const completedTasks = tasks.filter(t => t.status === 'DONE').length
    const closedBugs = bugs.filter(b => b.status === 'CLOSED').length
    
    projectStats.value = {
      requirements: reqs.length,
      tasks: tasks.length,
      bugs: bugs.length,
      completedTasks,
      closedBugs,
      taskRate: tasks.length ? Math.round(completedTasks / tasks.length * 100) : 0
    }
    
    // 更新图表
    taskChart.setOption({
      series: [{
        data: [
          { value: tasks.filter(t => t.status === 'TODO').length, name: '待办' },
          { value: tasks.filter(t => t.status === 'IN_PROGRESS').length, name: '进行中' },
          { value: tasks.filter(t => t.status === 'DEV_DONE').length, name: '开发完成' },
          { value: tasks.filter(t => t.status === 'TESTING').length, name: '测试中' },
          { value: completedTasks, name: '已完成' }
        ]
      }]
    })
    
    bugChart.setOption({
      series: [{
        data: [
          { value: bugs.filter(b => b.severity === 'FATAL').length, name: '致命' },
          { value: bugs.filter(b => b.severity === 'SERIOUS').length, name: '严重' },
          { value: bugs.filter(b => b.severity === 'NORMAL').length, name: '一般' },
          { value: bugs.filter(b => b.severity === 'MINOR').length, name: '轻微' }
        ]
      }]
    })
  } catch (e) {}
}

onMounted(async () => {
  projects.value = await getMyProjects()
  initCharts()
  if (projects.value.length > 0) {
    selectedProject.value = projects.value[0].id
    loadProjectStats()
  }
})

onUnmounted(() => {
  taskChart?.dispose()
  bugChart?.dispose()
})
</script>

<style scoped>
.statistics-page { padding-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; margin-bottom: 20px; }
.charts-row { margin-bottom: 20px; }
.chart-container { height: 300px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.project-stats { margin-top: 20px; }
</style>
