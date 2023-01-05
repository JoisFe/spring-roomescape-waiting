package com.nextstep.web.theme.controller;

import com.nextstep.web.theme.app.ThemeCommandService;
import com.nextstep.web.theme.app.ThemeQueryService;
import com.nextstep.web.theme.dto.CreateThemeRequest;
import com.nextstep.web.theme.dto.ThemeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping()
public class ThemeController {
    private final ThemeQueryService themeQueryService;
    private final ThemeCommandService themeCommandService;

    public ThemeController(ThemeQueryService themeQueryService, ThemeCommandService themeCommandService) {
        this.themeQueryService = themeQueryService;
        this.themeCommandService = themeCommandService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> read() {
        return ResponseEntity.ok(themeQueryService.findAll());
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<Void> create(@RequestBody CreateThemeRequest request) {
        Long id = themeCommandService.save(request);
        return ResponseEntity.created(URI.create("/theme/" + id)).build();
    }

    @DeleteMapping("/admin/themes/{id}")
    private ResponseEntity<Void> delete(@PathVariable Long id) {
        themeCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
