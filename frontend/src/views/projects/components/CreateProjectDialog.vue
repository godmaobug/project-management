<template>
  <el-dialog
    v-model="visible"
    title="创建项目"
    width="600px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="项目名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入项目名称" />
      </el-form-item>
      
      <el-form-item label="项目类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio-button label="SCRUM">Scrum敏捷</el-radio-button>
          <el-radio-button label="KANBAN">看板</el-radio-button>
          <el-radio-button label="GENERAL">通用项目</el-radio-button>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="项目周期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="可见性">
        <el-radio-group v-model="form.visibility">
          <el-radio-button label="PRIVATE">私有</el-radio-button>
          <el-radio-button label="PUBLIC">公开</el-radio-button>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="项目描述">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入项目描述" />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { createProject } from '@/api/projects'

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
const dateRange = ref([])

const form = reactive({
  name: '',
  type: 'SCRUM',
  visibility: 'PRIVATE',
  description: '',
  startDate: null,
  endDate: null
})

const rules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择项目类型', trigger: 'change' }]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = { ...form }
        if (dateRange.value && dateRange.value.length === 2) {
          data.startDate = dateRange.value[0]
          data.endDate = dateRange.value[1]
        }
        await createProject(data)
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
  form.name = ''
  form.type = 'SCRUM'
  form.visibility = 'PRIVATE'
  form.description = ''
  form.startDate = null
  form.endDate = null
  dateRange.value = []
}

watch(() => props.modelValue, (val) => {
  if (!val) {
    resetForm()
  }
})
</script>
