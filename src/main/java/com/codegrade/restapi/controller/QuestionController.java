package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.entity.UserAccount;
import com.codegrade.restapi.service.QuestionService;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
}
