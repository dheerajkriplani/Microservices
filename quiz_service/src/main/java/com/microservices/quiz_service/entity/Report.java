package com.microservices.quiz_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    private Long min_marks;

    private Long max_marks;

    private Long quizId;
}
