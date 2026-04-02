package com.projmaster.service;

import com.projmaster.dto.request.BugCreateRequest;
import com.projmaster.dto.response.BugResponse;
import com.projmaster.entity.Bug;

import java.util.List;

public interface BugService {

    List<BugResponse> getBugsByProject(Long projectId);

    List<BugResponse> getMyBugs();

    BugResponse getBugById(Long id);

    BugResponse createBug(BugCreateRequest request);

    BugResponse updateBug(Long id, BugCreateRequest request);

    void deleteBug(Long id);

    void updateBugStatus(Long id, Bug.BugStatus status);

    void updateBugAssignee(Long id, Long assigneeId);
}
