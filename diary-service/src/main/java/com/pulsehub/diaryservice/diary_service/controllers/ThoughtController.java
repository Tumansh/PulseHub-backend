package com.pulsehub.diaryservice.diary_service.controllers;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/thought")
@CrossOrigin(origins = "${app.cors.allowed-origin}")
public class ThoughtController {

    private static final List<String> THOUGHTS = List.of(
            "Stay consistent — small steps matter.",
            "Progress, not perfection.",
            "Gratitude makes ordinary days shine.",
            "Discipline beats motivation.",
            "Every day is a chance to grow."
    );

    @GetMapping("/today")
    public Map<String, String> getThought() {
        int i = LocalDate.now().getDayOfMonth() % THOUGHTS.size();
        return Map.of("message", THOUGHTS.get(i));
    }
}
