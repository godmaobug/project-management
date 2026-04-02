<template>
  <el-dialog
    v-model="visible"
    title="添加成员"
    width="500px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-form-item label="选择用户" prop="userId">
        <el-select v-model="form.userId" placeholder="请选择用户" filterable style="width: 100%">
          <el-option
            v-for="user in availableUsers"
            :key="user.id"
            :label="user.realName || user.username"
            :value="user.id"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="角色" prop="role">
        <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
          <el-option label="产品" value="PRODUCT" />
          <el-option label="开发" value="DEVELOPER" />
          <el-option label="测试" value="TESTER" />
          <el-option label="成员" value="MEMBER" />
          <el-option label="管理者" value="MANAGER" />
        </el-select>
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { addProjectMember } from '@/api/projects'
import { getAllUsers } from '@/api/users'

const props = defineProps({
  modelValue: Boolean,
  projectId: String
})
const emit = defineEmits(['update:modelValue', 'success'])

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const formRef = ref(null)
const loading = ref(false)
const users = ref([])
const currentMembers = ref([])

const form = reactive({
  userId: null,
  role: 'MEMBER'
})

const rules = {
  userId: [{ required: true, message: '请选择用户', trigger: 'change' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const availableUsers = computed(() => {
  const memberIds = currentMembers.value.map(m => m.user?.id)
  return users.value.filter(u => !memberIds.includes(u.id))
})

const fetchUsers = async () => {
  try {
    users.value = await getAllUsers()
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await addProjectMember(props.projectId, form.userId, form.role)
        ElMessage.success('添加成功')
        visible.value = false
        emit('success')
        resetForm()
      } catch (error) {
        console.error('添加失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const resetForm = () => {
  form.userId = null
  form.role = 'MEMBER'
}

watch(() => props.modelValue, (val) => {
  if (val) {
    fetchUsers()
  }
})

onMounted(() => {
  fetchUsers()
})
</script>
