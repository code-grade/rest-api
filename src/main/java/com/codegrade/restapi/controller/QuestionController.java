package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.service.QuestionService;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RestController
public class QuestionController {

    private final QuestionService questionService;

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/auth/question/all")
    public Map<String, Object> getAllQuestions() {

        return RBuilder.success()
                .setData(
                        questionService.getAllQuestions()
                )
                .compact();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/auth/question/{questionId}")
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

    @Secured("ROLE_ADMIN")
    @PostMapping(path = "/auth/question")
    public Map<String, Object> addQuestion(@Valid @RequestBody Question question) {

        return RBuilder.success()
                .setData(
                       "questionId", questionService.addQuestion(question)
                )
                .compact();
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(path = "/auth/question/{questionId}")
    public Map<String,Object> editQuestion(@Valid @PathVariable String questionId, @RequestBody Question question){

        return RBuilder.success()
                .setData(
                        questionService.editQuestion(UUID.fromString(questionId),question)
                )
                .compact();
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(path = "/auth/question/{questionId}")
    public void deleteQuestion(@Valid @PathVariable String questionId){

        questionService.deleteQuestion(UUID.fromString(questionId));
    }
}
