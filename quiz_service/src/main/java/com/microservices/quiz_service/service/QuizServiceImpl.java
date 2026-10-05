package com.microservices.quiz_service.service;

import com.microservices.quiz_service.entity.Quiz;
import com.microservices.quiz_service.entity.Report;
import com.microservices.quiz_service.repo.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private  QuizRepository quizRepository;

    @Autowired
    private QuestionClient questionClient;

    @Autowired
    private ReportClient reportClient;


    @Override
    public Quiz add(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public List<Quiz> get() {
        List<Quiz> quizzes= quizRepository.findAll();

        List<Quiz> quizList=quizzes.stream().map(quiz->{
            quiz.setQuestions(questionClient.getQuestionsOfQuiz(quiz.getId()));
            quiz.setReport(reportClient.getReportOfQuiz(quiz.getId()));
            return quiz;
        }).collect(Collectors.toList());

        return quizList;
    }

    @Override
    public Quiz get(Long id) {
        Quiz quiz=quizRepository.findById(id).orElse(null);
        quiz.setQuestions(questionClient.getQuestionsOfQuiz(quiz.getId()));
        quiz.setReport(reportClient.getReportOfQuiz(quiz.getId()));
        return quiz;
    }



}
