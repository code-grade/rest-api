package com.codegrade.restapi.service;

import com.codegrade.restapi.controller.reqres.ResItemSampleTestCase;
import com.codegrade.restapi.entity.SourceCode;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.runtime.ExecOutput;
import com.codegrade.restapi.runtime.ReqRunCode;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Source;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RuntimeService {

    private final RestTemplate restTemplate;

    public ExecOutput runCode(SourceCode source, String input, Double timeLimit) {
        if (Objects.equals(source.getLanguage().toUpperCase(), "PYTHON")) {
            return runPythonCode(source.getSource(), input);
        } else {
            throw new ApiException(RBuilder.badRequest("language is not supported"));
        }
    }

    public ExecOutput runCode(String source, String input, String language) {
        if (Objects.equals(language, "PYTHON")) {
            return runPythonCode(source, input);
        } else {
            throw new ApiException(RBuilder.badRequest("language is not supported"));
        }
    }

    public ExecOutput runPythonCode(String source, String input) {
        ResRtCodeRun response = restTemplate.postForObject(
                "http://python-runtime/runtime/python/run",
                new ReqRunCode(source, input, 6d, "PYTHON"),
                ResRtCodeRun.class
        );
        if (response == null) throw new ApiException(RBuilder.error("runtime server didn't respond"));
        return response.getData();
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResRtCodeRun {
        private ExecOutput data;
        private String message;
    }
}
