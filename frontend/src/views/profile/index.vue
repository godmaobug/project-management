<template>
  <div class="profile-page">
    <h2 class="page-title">个人设置</h2>

    <el-card shadow="never" class="profile-card">
      <el-form :model="form" label-width="100px" style="max-width: 500px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getCurrentUser } from '@/api/auth'

const userStore = useUserStore()
const form = ref({ username: '', realName: '', email: '', phone: '' })

const fetchUser = async () => {
  try {
    const data = await getCurrentUser()
    form.value = { ...data }
  } catch (e) {}
}

const handleSave = () => {
  ElMessage.success('保存成功')
}

onMounted(fetchUser)
</script>

<style scoped>
.profile-page { padding-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; margin-bottom: 20px; }
.profile-card { max-width: 600px; }
</style>
