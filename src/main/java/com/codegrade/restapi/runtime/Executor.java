package com.codegrade.restapi.runtime;

import java.io.IOException;

public interface Executor {
    ExecOutput execute(String sourceCode, String input, Double timeLimit) throws IOException, InterruptedException;
}
