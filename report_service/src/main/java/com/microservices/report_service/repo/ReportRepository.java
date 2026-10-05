package com.microservices.report_service.repo;

import com.microservices.report_service.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findByQuizId(Long quizId);
}
