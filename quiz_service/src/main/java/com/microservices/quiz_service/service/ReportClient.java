package com.microservices.quiz_service.service;

import com.microservices.quiz_service.entity.Report;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name="report-service")  //with load-balancing
public interface ReportClient {

    @GetMapping("/report/getByQuizId/{quizId}")
    Report getReportOfQuiz(@PathVariable Long quizId);
}
