package com.muvix.backend.security;
import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.security.access.AccessDeniedException; import org.springframework.security.web.access.AccessDeniedHandler; import org.springframework.stereotype.Component; import java.io.IOException;
@Component public class ApiAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest req,HttpServletResponse res,AccessDeniedException ex)throws IOException {
        res.sendError(403,"Access denied");
    }
}
