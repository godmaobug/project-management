import request from '@/utils/request'

export const getIterationsByProject = (projectId) => request.get(`/iterations/project/${projectId}`)
export const getIteration = (id) => request.get(`/iterations/${id}`)
export const createIteration = (data) => request.post('/iterations', data)
export const updateIteration = (id, data) => request.put(`/iterations/${id}`, data)
export const deleteIteration = (id) => request.delete(`/iterations/${id}`)
export const updateIterationStatus = (id, status) => 
  request.patch(`/iterations/${id}/status?status=${status}`)
export const startIteration = (id) => request.post(`/iterations/${id}/start`)
export const completeIteration = (id) => request.post(`/iterations/${id}/complete`)
