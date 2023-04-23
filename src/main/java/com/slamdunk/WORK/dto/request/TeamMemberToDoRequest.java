package com.slamdunk.WORK.dto.request;

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
    private List<Long> teamMembers;
    private List<Long> KeyResultIds;
    private String orderby;
    private String orderbyrole;
}
