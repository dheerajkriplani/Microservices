package com.microservices.quiz_service.service;


import com.microservices.quiz_service.entity.Report;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ReportRestClient {

    @Autowired
    private RestClient restClient;

    @Value("${report.service.url}")
    private String reportServiceUrl;

    public Report getReportOfQuiz(Long quizId) {

        return restClient.get()
                .uri(reportServiceUrl+"/getByQuizId/{quizId}", quizId)
                .retrieve()
                .body(Report.class);
    }
}
