import request from '@/utils/request'

export const getMyTasks = () => request.get('/tasks/my')
export const getTasksByProject = (projectId) => request.get(`/tasks/project/${projectId}`)
export const getTasksByIteration = (iterationId) => request.get(`/tasks/iteration/${iterationId}`)
export const getTask = (id) => request.get(`/tasks/${id}`)
export const createTask = (data) => request.post('/tasks', data)
export const updateTask = (id, data) => request.put(`/tasks/${id}`, data)
export const deleteTask = (id) => request.delete(`/tasks/${id}`)
export const updateTaskStatus = (id, status) => request.patch(`/tasks/${id}/status?status=${status}`)
export const updateTaskAssignee = (id, assigneeId) => 
  request.patch(`/tasks/${id}/assignee?assigneeId=${assigneeId}`)
