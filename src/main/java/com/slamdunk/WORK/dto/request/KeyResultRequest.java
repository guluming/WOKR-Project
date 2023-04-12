package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@Getter
public class KeyResultRequest {
    private int krNumber;
    @NotBlank
    private String keyResult;
}
