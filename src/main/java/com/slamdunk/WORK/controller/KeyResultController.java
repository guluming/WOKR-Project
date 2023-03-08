package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.service.KeyResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KeyResultController {
    private final KeyResultService keyResultService;


}
