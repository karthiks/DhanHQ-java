package co.dhan.helper;

import java.time.*;
import java.time.format.DateTimeFormatter;

public interface DateUtils {

    static String epochSecondsInUTC(int seconds) {
        Instant instant = Instant.ofEpochSecond(seconds);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ldt.format(formatter);
    }

    static String epochSecondsInIST(int seconds) {
        Instant instant = Instant.ofEpochSecond(seconds);
        ZoneId ist = ZoneId.of("Asia/Kolkata");
        ZonedDateTime zdt = instant.atZone(ist);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss zzz");
        return zdt.format(formatter);
    }
}
