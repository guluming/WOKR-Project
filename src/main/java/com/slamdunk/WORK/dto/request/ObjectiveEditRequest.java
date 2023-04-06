package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ObjectiveEditRequest {
    @NotBlank
    private String objective;
    @NotBlank
    private LocalDate startdate;
    @NotBlank
    private LocalDate enddate;
    @NotBlank
    private String color;
}
