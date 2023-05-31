package org.example;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private TemplateEngine engine;

    @Override
    public void init(){
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("D:\\java\\hw-module8\\src\\templates\\");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //resp.setIntHeader("Refresh", 1);
        resp.setContentType("text/html; charset=utf-8");

        String timezone = req.getParameter("timezone");

        if(timezone != null){
            timezone = timezone.replace(" ", "");
            resp.addCookie(new Cookie("lastTimezone", timezone));
        }

        Cookie[] cookies = req.getCookies();
        String lastTimezone = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("lastTimezone")){
                lastTimezone = cookie.getValue();
            }
        }

        Context context = new Context(
                req.getLocale(),
                ParseDate(
                        Objects.requireNonNullElse(lastTimezone, "UTC+0")
                )
        );

        engine.process("test", context, resp.getWriter());

        resp.getWriter().close();
    }

    private Map<String,Object> ParseDate(String timezone){
        int hoursOffset = Integer.parseInt(timezone.replace(" ", "").substring(3));

        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        ZoneId zoneId = ZoneOffset.ofHours(hoursOffset);
        ZonedDateTime zonedDateTime = utcDateTime.withZoneSameInstant(zoneId);

        LocalDate localDate = zonedDateTime.toLocalDate();
        LocalTime localTime = zonedDateTime.toLocalTime();

        String formattedDate = localDate.format(DATE_FORMATTER);
        String formattedTime = localTime.format(TIME_FORMATTER);

        Map<String, Object> result = new HashMap<>();
        result.put("date", timezone + ": " + formattedDate);
        result.put("time", timezone + ": " + formattedTime);

        return  result;
    }


}
