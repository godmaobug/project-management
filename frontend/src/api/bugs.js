import request from '@/utils/request'

export const getMyBugs = () => request.get('/bugs/my')
export const getBugsByProject = (projectId) => request.get(`/bugs/project/${projectId}`)
export const getBug = (id) => request.get(`/bugs/${id}`)
export const createBug = (data) => request.post('/bugs', data)
export const updateBug = (id, data) => request.put(`/bugs/${id}`, data)
export const deleteBug = (id) => request.delete(`/bugs/${id}`)
export const updateBugStatus = (id, status) => request.patch(`/bugs/${id}/status?status=${status}`)
export const updateBugAssignee = (id, assigneeId) => 
  request.patch(`/bugs/${id}/assignee?assigneeId=${assigneeId}`)
