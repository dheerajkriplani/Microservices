package com.microservices.report_service.controller;


import com.microservices.report_service.entity.Report;
import com.microservices.report_service.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/create")
    public Report createReport(@RequestBody Report report) {
        return reportService.add(report);
    }

    @GetMapping("/getAll")
    public List<Report> getAll() {
        return reportService.get();
    }

    @GetMapping("/get/{id}")
    public Report get(@PathVariable Long id) {
        return reportService.get(id);
    }

    @GetMapping("getByQuizId/{quizId}")
    public Report getByQuizId(@PathVariable Long quizId) {
        return reportService.getByQuizId(quizId);
    }
}
