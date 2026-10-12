
package org.example.bikeprontabackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/api/auth/csrf")
    public ResponseEntity<Map<String, String>> csrf(CsrfToken csrfToken) {
        return ResponseEntity.ok(Map.of(
                "token", csrfToken.getToken(),
                "headerName", csrfToken.getHeaderName(),
                "parameterName", csrfToken.getParameterName()
        ));
    }

    @GetMapping("/api/auth/status")
    public ResponseEntity<Map<String, Object>> status(
            Authentication authentication) {

        boolean autenticado =
                authentication != null
                        && authentication.isAuthenticated()
                        && !"anonymousUser".equals(authentication.getPrincipal());

        return ResponseEntity.ok(Map.of(
                "autenticado", autenticado,
                "usuario", autenticado ? authentication.getName() : ""
        ));
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Map<String, Object>> logout(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.setHeader(
                "Set-Cookie",
                "JSESSIONID=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax"
        );

        return ResponseEntity.ok(Map.of("autenticado", false));
    }
}