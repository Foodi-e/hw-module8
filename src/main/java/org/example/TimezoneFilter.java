package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(value = "/time")
public class TimezoneFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");
        if(timezone == null || timezone.matches("^UTC\\s*[+\\-]?(?:1[0-2]|[0-9])$")){
            chain.doFilter(req, res);
        } else {
            res.setStatus(400);

            res.setContentType("application/json");
            res.getWriter().write("{\"Error\": \"incorrect timezone\"}");
            res.getWriter().close();
        }

        super.doFilter(req, res, chain);
    }
}
