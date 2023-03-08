package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.service.ObjectiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ObjectiveController {
    private final ObjectiveService objectiveService;


}
