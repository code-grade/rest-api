package com.codegrade.restapi.exception;

import com.codegrade.restapi.utils.RBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class AuthExceptHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException ex) throws IOException {

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();

        if (BadCredentialsException.class.equals(ex.getClass())) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out,
                    RBuilder.badRequest().setMsg("Invalid username or password").compact()
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if(DisabledException.class.equals(ex.getClass())) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out,
                    RBuilder.badRequest().setMsg("account is disabled").compact()
            );
        } else if (InternalAuthenticationServiceException.class.equals(ex.getClass())) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out,
                    RBuilder.badRequest().setMsg("account is disabled or email is not verified").compact()
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out,
                    RBuilder.error().setData("error", ex.toString()).compact()
            );
            log.info(ex.getMessage());
            ex.printStackTrace();
        }
    }
}

