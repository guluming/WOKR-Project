package com.slamdunk.WORK.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TeamMemberResponse {
    private boolean myInfo;
    private Long userId;
    private String email;
    private String name;
    private String team;
    private String teamposition;
    private int createToDoCount;

    @Builder
    public TeamMemberResponse(boolean myInfo, Long userId, String email, String name, String team, String teamposition,
                              int createToDoCount) {
        this.myInfo = myInfo;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.team = team;
        this.teamposition = teamposition;
        this.createToDoCount= createToDoCount;
    }
}
