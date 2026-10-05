package com.microservices.report_service.service;

import com.microservices.report_service.entity.Report;

import java.util.List;

public interface ReportService {
        Report add(Report report);

        List<Report> get();

        Report get(Long id);

        Report getByQuizId(Long quizId);
}
