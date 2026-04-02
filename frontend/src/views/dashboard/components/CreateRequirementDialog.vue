<template>
  <el-dialog
    v-model="visible"
    title="创建需求"
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
      
      <el-form-item label="需求标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入需求标题" />
      </el-form-item>
      
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="form.priority" placeholder="请选择优先级" style="width: 100%">
          <el-option label="紧急" value="URGENT" />
          <el-option label="高" value="HIGH" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="低" value="LOW" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="故事点" prop="storyPoints">
        <el-input-number v-model="form.storyPoints" :min="0" :max="100" />
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
      
      <el-form-item label="需求描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入需求描述" />
      </el-form-item>
      
      <el-form-item label="验收标准" prop="acceptanceCriteria">
        <el-input v-model="form.acceptanceCriteria" type="textarea" :rows="3" placeholder="请输入验收标准" />
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
import { createRequirement } from '@/api/requirements'
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
  priority: 'MEDIUM',
  storyPoints: 0,
  assigneeId: null,
  description: '',
  acceptanceCriteria: ''
})

const rules = {
  projectId: [{ required: true, message: '请选择项目', trigger: 'change' }],
  title: [{ required: true, message: '请输入需求标题', trigger: 'blur' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
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
        await createRequirement(form)
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
  form.priority = 'MEDIUM'
  form.storyPoints = 0
  form.assigneeId = null
  form.description = ''
  form.acceptanceCriteria = ''
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
