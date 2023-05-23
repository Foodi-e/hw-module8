package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        String timezone = req.getParameter("timezone");

        if(timezone != null){
            resp.getWriter().write(ParseDate(timezone));
        } else {
            resp.getWriter().write(ParseDate("UTC+0"));
        }

        resp.getWriter().close();
    }

    private String ParseDate(String timezone){
        int hoursOffset = Integer.parseInt(timezone.replace(" ", "").substring(3));

        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        ZoneId zoneId = ZoneOffset.ofHours(hoursOffset);
        ZonedDateTime zonedDateTime = utcDateTime.withZoneSameInstant(zoneId);

        LocalDate localDate = zonedDateTime.toLocalDate();
        LocalTime localTime = zonedDateTime.toLocalTime();

        String formattedDate = localDate.format(DATE_FORMATTER);
        String formattedTime = localTime.format(TIME_FORMATTER);

        return  "Date in " + timezone + ": " + formattedDate
                + "<br/>" + 
                "Time in " + timezone + ": " + formattedTime;
    }
}
