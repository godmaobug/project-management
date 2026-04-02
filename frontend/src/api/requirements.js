import request from '@/utils/request'

export const getRequirementsByProject = (projectId) => 
  request.get(`/requirements/project/${projectId}`)
export const getUnplannedRequirements = (projectId) => 
  request.get(`/requirements/project/${projectId}/unplanned`)
export const getRequirement = (id) => request.get(`/requirements/${id}`)
export const createRequirement = (data) => request.post('/requirements', data)
export const updateRequirement = (id, data) => request.put(`/requirements/${id}`, data)
export const deleteRequirement = (id) => request.delete(`/requirements/${id}`)
export const updateRequirementStatus = (id, status) => 
  request.patch(`/requirements/${id}/status?status=${status}`)
export const assignToIteration = (requirementId, iterationId) => 
  request.post(`/requirements/${requirementId}/assign-to-iteration?iterationId=${iterationId}`)
