package com.codegrade.restapi.exception;

import com.codegrade.restapi.utils.RBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException ex) throws IOException, ServletException {

        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();

        if (BadCredentialsException.class.equals(ex.getClass())) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out,
                    RBuilder.badRequest().setMsg("Invalid username or password").compact()
            );
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out,
                    RBuilder.error().setData("error", ex.toString()).compact()
            );
            log.info(ex.toString());
            ex.printStackTrace();
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


        out.flush();
    }
}
