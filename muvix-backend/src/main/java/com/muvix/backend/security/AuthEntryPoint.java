package com.muvix.backend.security;
import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.security.core.AuthenticationException; import org.springframework.security.web.AuthenticationEntryPoint; import org.springframework.stereotype.Component; import java.io.IOException;
@Component public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override public void commence(HttpServletRequest req,HttpServletResponse res,AuthenticationException ex)throws IOException {res.sendError(401,"Authentication required");}
}
