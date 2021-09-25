package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.entity.UserRole;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.service.QuestionService;
import com.codegrade.restapi.utils.AuthContext;
import com.codegrade.restapi.utils.RBuilder;
import com.codegrade.restapi.utils.validator.ValidUUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
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

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @GetMapping(path = "/question/instructor")
    public ResponseEntity<?> getQuestionByInstructor() {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(questionService.getQuestionsByInstructor(context.getUserId()))
                .compactResponse();
    }

    @Secured({UserRole.ROLE_INSTRUCTOR, UserRole.ROLE_STUDENT})
    @GetMapping(path = "/question/{questionId}")
    public ResponseEntity<?> getQuestion(
            @PathVariable("questionId") @ValidUUID String questionId,
            @RequestParam(value = "complete", required = false) Optional<Boolean> complete
    ) {
        AuthContext context = AuthContext.fromContextHolder();
        Question question = questionService.getQuestionById(UUID.fromString(questionId));
        if (complete.isPresent() && complete.get()) {
           if (context.matchUserId(question.getInstructor().getUserId())) {
               return RBuilder.success()
                       .setData(Question.JustInstructorId.fromQuestion(question))
                       .compactResponse();
           } else {
               throw new ApiException(RBuilder.unauthorized());
           }
        }

        return RBuilder.success()
                .setData(Question.NoTestCase.fromQuestion(question))
                .compactResponse();
    }

    @Secured("ROLE_INSTRUCTOR")
    @PostMapping(path = "/question")
    public ResponseEntity<?> addQuestion(@Valid @RequestBody Question question) {
        AuthContext context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(
                        questionService.create(
                                new User(context.getUserId()),
                                question
                        )
                )
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
