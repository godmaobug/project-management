<template>
  <div class="users-page">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <el-button type="primary" :icon="Plus" @click="showCreateDialog = true">创建用户</el-button>
    </div>

    <el-card shadow="never">
      <el-table :data="users" stripe v-loading="loading">
        <el-table-column label="用户名" prop="username" width="120" />
        <el-table-column label="真实姓名" prop="realName" width="120" />
        <el-table-column label="邮箱" prop="email" min-width="180" />
        <el-table-column label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)">{{ getRoleLabel(row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'info'">{{ row.enabled ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="160">
          <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showCreateDialog" title="创建用户" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码" required>
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="真实姓名" required>
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAllUsers, createUser } from '@/api/users'
import dayjs from 'dayjs'

const users = ref([])
const loading = ref(false)
const showCreateDialog = ref(false)
const form = ref({ username: '', password: '', realName: '', email: '', phone: '' })

const fetchUsers = async () => {
  loading.value = true
  try {
    users.value = await getAllUsers()
  } catch (e) {} finally { loading.value = false }
}

const handleCreate = async () => {
  try {
    await createUser(form.value)
    ElMessage.success('创建成功')
    showCreateDialog.value = false
    fetchUsers()
    form.value = { username: '', password: '', realName: '', email: '', phone: '' }
  } catch (e) {}
}

const getRoleType = (r) => ({ SUPER_ADMIN: 'danger', PROJECT_MANAGER: 'warning', PRODUCT_OWNER: 'success', MEMBER: 'info' }[r])
const getRoleLabel = (r) => ({ SUPER_ADMIN: '超级管理员', PROJECT_MANAGER: '项目经理', PRODUCT_OWNER: '产品负责人', MEMBER: '成员' }[r])
const formatDate = (d) => d ? dayjs(d).format('YYYY-MM-DD HH:mm') : '-'

onMounted(fetchUsers)
</script>

<style scoped>
.users-page { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; }
</style>
