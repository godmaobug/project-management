<template>
  <el-dialog
    v-model="visible"
    title="提交Bug"
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
      
      <el-form-item label="Bug标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入Bug标题" />
      </el-form-item>
      
      <el-form-item label="严重程度" prop="severity">
        <el-select v-model="form.severity" placeholder="请选择严重程度" style="width: 100%">
          <el-option label="致命" value="FATAL" />
          <el-option label="严重" value="SERIOUS" />
          <el-option label="一般" value="NORMAL" />
          <el-option label="轻微" value="MINOR" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="发现环境" prop="environment">
        <el-select v-model="form.environment" placeholder="请选择发现环境" style="width: 100%">
          <el-option label="开发环境" value="DEV" />
          <el-option label="测试环境" value="TEST" />
          <el-option label="预发环境" value="STAGING" />
          <el-option label="生产环境" value="PROD" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="所属模块">
        <el-input v-model="form.module" placeholder="请输入所属模块" />
      </el-form-item>
      
      <el-form-item label="版本号">
        <el-input v-model="form.version" placeholder="请输入版本号" />
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
      
      <el-form-item label="Bug描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入Bug描述" />
      </el-form-item>
      
      <el-form-item label="重现步骤" prop="reproduceSteps">
        <el-input v-model="form.reproduceSteps" type="textarea" :rows="4" placeholder="请详细描述重现步骤" />
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
import { createBug } from '@/api/bugs'
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
  severity: 'NORMAL',
  environment: 'TEST',
  module: '',
  version: '',
  assigneeId: null,
  description: '',
  reproduceSteps: ''
})

const rules = {
  projectId: [{ required: true, message: '请选择项目', trigger: 'change' }],
  title: [{ required: true, message: '请输入Bug标题', trigger: 'blur' }],
  severity: [{ required: true, message: '请选择严重程度', trigger: 'change' }],
  environment: [{ required: true, message: '请选择发现环境', trigger: 'change' }]
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
        await createBug(form)
        ElMessage.success('提交成功')
        visible.value = false
        emit('success')
        resetForm()
      } catch (error) {
        console.error('提交失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const resetForm = () => {
  form.projectId = null
  form.title = ''
  form.severity = 'NORMAL'
  form.environment = 'TEST'
  form.module = ''
  form.version = ''
  form.assigneeId = null
  form.description = ''
  form.reproduceSteps = ''
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
