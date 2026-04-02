package com.projmaster.repository;

import com.projmaster.entity.BurnDownData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BurnDownDataRepository extends JpaRepository<BurnDownData, Long> {

    List<BurnDownData> findByIterationIdOrderByRecordDateAsc(Long iterationId);
}
