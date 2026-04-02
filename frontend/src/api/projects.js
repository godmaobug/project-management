import request from '@/utils/request'

export const getMyProjects = () => request.get('/projects')
export const getAllProjects = () => request.get('/projects/all')
export const getProject = (id) => request.get(`/projects/${id}`)
export const createProject = (data) => request.post('/projects', data)
export const updateProject = (id, data) => request.put(`/projects/${id}`, data)
export const deleteProject = (id) => request.delete(`/projects/${id}`)
export const archiveProject = (id) => request.post(`/projects/${id}/archive`)
export const addProjectMember = (projectId, userId, role) => 
  request.post(`/projects/${projectId}/members?userId=${userId}&role=${role}`)
export const removeProjectMember = (projectId, userId) => 
  request.delete(`/projects/${projectId}/members/${userId}`)
