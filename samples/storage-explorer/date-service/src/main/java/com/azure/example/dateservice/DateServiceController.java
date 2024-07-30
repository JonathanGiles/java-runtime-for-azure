package com.azure.example.dateservice;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

@Controller
public class DateServiceController {

    @GetMapping("/time")
    public ResponseEntity<OffsetDateTime> getTime(@RequestParam(required = false) String timezone) {
        try {
            OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneId.of("UTC"));
            if (timezone != null && !timezone.isEmpty()) {
                ZoneId zoneId = ZoneId.of(timezone);
                currentDateTime = OffsetDateTime.now(zoneId);
            }
            return ResponseEntity.ok(currentDateTime);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}