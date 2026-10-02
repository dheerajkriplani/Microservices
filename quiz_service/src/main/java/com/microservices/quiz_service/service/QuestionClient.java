package com.microservices.quiz_service.service;

import com.microservices.quiz_service.entity.Question;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(url="http://localhost:8082",value = "question-client")
@FeignClient(name="question-service")  //with load-balancing
public interface QuestionClient {

    @GetMapping("/question/getByQuizId/{quizId}")
    List<Question> getQuestionsOfQuiz(@PathVariable Long quizId);

}
