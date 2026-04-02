package com.projmaster.service;

import com.projmaster.dto.request.RequirementCreateRequest;
import com.projmaster.dto.response.RequirementResponse;
import com.projmaster.entity.Requirement;

import java.util.List;

public interface RequirementService {

    List<RequirementResponse> getRequirementsByProject(Long projectId);

    List<RequirementResponse> getUnplannedRequirements(Long projectId);

    RequirementResponse getRequirementById(Long id);

    RequirementResponse createRequirement(RequirementCreateRequest request);

    RequirementResponse updateRequirement(Long id, RequirementCreateRequest request);

    void deleteRequirement(Long id);

    void updateRequirementStatus(Long id, Requirement.RequirementStatus status);

    void assignToIteration(Long requirementId, Long iterationId);
}
