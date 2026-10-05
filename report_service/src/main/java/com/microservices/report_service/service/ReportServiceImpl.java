package com.microservices.report_service.service;

import com.microservices.report_service.entity.Report;
import com.microservices.report_service.repo.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportRepository reportRepository;


    @Override
    public Report add(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public List<Report> get() {
        return reportRepository.findAll();
    }

    @Override
    public Report get(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    @Override
    public Report getByQuizId(Long quizId) {
        return reportRepository.findByQuizId(quizId);
    }
}
