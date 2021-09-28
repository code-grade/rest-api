package com.codegrade.restapi.service;

import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.runtime.ExecOutput;
import com.codegrade.restapi.runtime.ReqRunCode;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class RuntimeService {

    private final RestTemplate restTemplate;
    private final EurekaDiscoveryClient eurekaDiscoveryClient;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResRtCodeRun {
        private ExecOutput data;
        private String message;
    }

    public ExecOutput runPythonCode(String source, String input) {
        ResRtCodeRun response = restTemplate.postForObject(
                "http://python-runtime/runtime/python/run",
                new ReqRunCode(source, input, 6d),
                ResRtCodeRun.class
        );
        if (response == null) throw new ApiException(RBuilder.error("runtime server didn't respond"));
        return response.getData();
    }

}
