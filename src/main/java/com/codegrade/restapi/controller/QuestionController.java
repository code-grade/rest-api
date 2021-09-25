package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.service.QuestionService;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;


@Getter
@Setter
@AllArgsConstructor
@RestController
@Validated
public class QuestionController {

    private final QuestionService questionService;

    @Secured("ROLE_INSTRUCTOR")
    @GetMapping(path = "/question/all")
    public Map<String, Object> getAllQuestions() {

        return RBuilder.success()
                .setData(
                        questionService.getAllQuestions()
                )
                .compact();
    }

    @Secured("ROLE_INSTRUCTOR")
    @GetMapping(path = "/question/{questionId}")
    public Map<String, Object> getQuestion(@PathVariable String questionId) {

        return RBuilder.success()
                .setData(
                        questionService.getQuestion(UUID.fromString(questionId))
                                .orElseThrow(() -> ApiException.withRBuilder(
                                        RBuilder.notFound().setMsg("Question not found")
                                ))
                )
                .compact();
    }

    @Secured("ROLE_INSTRUCTOR")
    @PostMapping(path = "/question")
    public ResponseEntity<?> addQuestion(@Valid @RequestBody Question question) {
        return RBuilder.success()
//                .setData(
//                       "questionId", questionService.create(question)
//                )
                .compactResponse();
    }

    @Secured("ROLE_INSTRUCTOR")
    @PutMapping(path = "/question/{questionId}")
    public Map<String,Object> editQuestion(@Valid @PathVariable String questionId, @RequestBody Question question){
        question.setQuestionId(UUID.fromString(questionId));
        return RBuilder.success()
                .setData(
                        questionService.editQuestion(question)
                )
                .compact();
    }

    @Secured("ROLE_INSTRUCTOR")
    @DeleteMapping(path = "/question/{questionId}")
    public void deleteQuestion(@Valid @PathVariable String questionId){

        questionService.deleteQuestion(UUID.fromString(questionId));
    }

}
