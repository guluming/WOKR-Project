package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ObjectiveRequest {
    @NotBlank
    private String objective;
    private LocalDate startdate;
    private LocalDate enddate;
    @NotBlank
    private String color;
}
