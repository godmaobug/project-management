<template>
  <el-dialog
    v-model="visible"
    title="创建任务"
    width="600px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="所属项目" prop="projectId">
        <el-select v-model="form.projectId" placeholder="请选择项目" style="width: 100%">
          <el-option
            v-for="project in projects"
            :key="project.id"
            :label="project.name"
            :value="project.id"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="任务标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入任务标题" />
      </el-form-item>
      
      <el-form-item label="任务类型" prop="type">
        <el-select v-model="form.type" placeholder="请选择任务类型" style="width: 100%">
          <el-option label="开发任务" value="DEVELOPMENT" />
          <el-option label="测试任务" value="TEST" />
          <el-option label="设计任务" value="DESIGN" />
          <el-option label="评审任务" value="REVIEW" />
          <el-option label="Bug修复" value="BUG_FIX" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="form.priority" placeholder="请选择优先级" style="width: 100%">
          <el-option label="紧急" value="URGENT" />
          <el-option label="高" value="HIGH" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="低" value="LOW" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="预估工时">
        <el-input-number v-model="form.estimatedHours" :min="0" :max="999" :precision="1" />
        <span class="unit">小时</span>
      </el-form-item>
      
      <el-form-item label="截止日期">
        <el-date-picker v-model="form.dueDate" type="date" placeholder="选择截止日期" style="width: 100%" />
      </el-form-item>
      
      <el-form-item label="指派给">
        <el-select v-model="form.assigneeId" placeholder="请选择指派人" clearable style="width: 100%">
          <el-option
            v-for="user in users"
            :key="user.id"
            :label="user.realName || user.username"
            :value="user.id"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="任务描述">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入任务描述" />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { createTask } from '@/api/tasks'
import { getMyProjects } from '@/api/projects'
import { getAllUsers } from '@/api/users'

const props = defineProps({
  modelValue: Boolean
})
const emit = defineEmits(['update:modelValue', 'success'])

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const formRef = ref(null)
const loading = ref(false)
const projects = ref([])
const users = ref([])

const form = reactive({
  projectId: null,
  title: '',
  type: 'DEVELOPMENT',
  priority: 'MEDIUM',
  estimatedHours: 0,
  dueDate: null,
  assigneeId: null,
  description: ''
})

const rules = {
  projectId: [{ required: true, message: '请选择项目', trigger: 'change' }],
  title: [{ required: true, message: '请输入任务标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择任务类型', trigger: 'change' }]
}

const fetchData = async () => {
  try {
    projects.value = await getMyProjects()
    users.value = await getAllUsers()
  } catch (error) {
    console.error('获取数据失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await createTask(form)
        ElMessage.success('创建成功')
        visible.value = false
        emit('success')
        resetForm()
      } catch (error) {
        console.error('创建失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const resetForm = () => {
  form.projectId = null
  form.title = ''
  form.type = 'DEVELOPMENT'
  form.priority = 'MEDIUM'
  form.estimatedHours = 0
  form.dueDate = null
  form.assigneeId = null
  form.description = ''
}

watch(() => props.modelValue, (val) => {
  if (val) {
    fetchData()
  }
})

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.unit {
  margin-left: 8px;
  color: #909399;
}
</style>
