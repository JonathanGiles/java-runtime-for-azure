package com.microsoft.aspire.storage.explorer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@Controller
public class DateServiceController {

    @GetMapping("/time/{timezone}")
    public ResponseEntity<Long> getTimeInTimezone(@PathVariable String timezone) {
        try {
            ZoneId zoneId = ZoneId.of(timezone);
            ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
            long epochSeconds = zonedDateTime.toEpochSecond();
            return ResponseEntity.ok(epochSeconds);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}