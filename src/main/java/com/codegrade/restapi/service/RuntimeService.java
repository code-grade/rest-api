package com.codegrade.restapi.service;

import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.runtime.ExecOutput;
import com.codegrade.restapi.runtime.ReqRunCode;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@AllArgsConstructor
public class RuntimeService {

    private final RestTemplate restTemplate;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResRtCodeRun {
        private ExecOutput data;
        private String message;
    }

    public ExecOutput runCode(String source, String input, String language) {
        if (Objects.equals(language, "PYTHON")) {
            return runPythonCode(source, input);
        } else {
            throw new ApiException(RBuilder.badRequest("language is not support"));
        }
    }

    public ExecOutput runPythonCode(String source, String input) {
        ResRtCodeRun response = restTemplate.postForObject(
                "http://52.226.86.76:8101/runtime/python/run",
                new ReqRunCode(source, input, 6d, "PYTHON"),
                ResRtCodeRun.class
        );
        if (response == null) throw new ApiException(RBuilder.error("runtime server didn't respond"));
        return response.getData();
    }

}
