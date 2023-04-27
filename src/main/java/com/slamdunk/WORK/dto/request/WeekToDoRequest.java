package com.slamdunk.WORK.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class WeekToDoRequest {
    private LocalDate Sunday;
    private LocalDate Saturday;
    @JsonProperty("teamMembers")
    private List<Long> teamMembers;
}
