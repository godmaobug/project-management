package com.projmaster.service;

import com.projmaster.dto.request.IterationCreateRequest;
import com.projmaster.dto.response.IterationResponse;
import com.projmaster.entity.Iteration;

import java.util.List;

public interface IterationService {

    List<IterationResponse> getIterationsByProject(Long projectId);

    IterationResponse getIterationById(Long id);

    IterationResponse createIteration(IterationCreateRequest request);

    IterationResponse updateIteration(Long id, IterationCreateRequest request);

    void deleteIteration(Long id);

    void updateIterationStatus(Long id, Iteration.IterationStatus status);

    void startIteration(Long id);

    void completeIteration(Long id);
}
