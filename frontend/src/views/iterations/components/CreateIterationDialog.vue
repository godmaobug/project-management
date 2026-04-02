<template>
  <el-dialog
    v-model="visible"
    title="创建迭代"
    width="500px"
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
      
      <el-form-item label="迭代名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入迭代名称，如：Sprint 1" />
      </el-form-item>
      
      <el-form-item label="迭代目标" prop="goal">
        <el-input v-model="form.goal" type="textarea" :rows="3" placeholder="请输入迭代目标" />
      </el-form-item>
      
      <el-form-item label="迭代周期" prop="dateRange">
        <el-date-picker
          v-model="form.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 100%"
        />
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
import { createIteration } from '@/api/iterations'

const props = defineProps({
  modelValue: Boolean,
  projects: Array
})
const emit = defineEmits(['update:modelValue', 'success'])

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  projectId: null,
  name: '',
  goal: '',
  dateRange: []
})

const rules = {
  projectId: [{ required: true, message: '请选择项目', trigger: 'change' }],
  name: [{ required: true, message: '请输入迭代名称', trigger: 'blur' }],
  dateRange: [{ required: true, message: '请选择迭代周期', trigger: 'change' }]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = {
          projectId: form.projectId,
          name: form.name,
          goal: form.goal,
          startDate: form.dateRange[0],
          endDate: form.dateRange[1]
        }
        await createIteration(data)
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
  form.name = ''
  form.goal = ''
  form.dateRange = []
}

watch(() => props.modelValue, (val) => {
  if (!val) resetForm()
})
</script>
