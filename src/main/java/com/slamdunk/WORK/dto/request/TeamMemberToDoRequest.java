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
public class TeamMemberToDoRequest {
    private LocalDate targetDate;
    @JsonProperty("teamMembers")
    private List<Long> teamMembers;
    @JsonProperty("KeyResultIds")
    private List<Long> KeyResultIds;
    private String orderby;
    private String orderbyrole;
}
