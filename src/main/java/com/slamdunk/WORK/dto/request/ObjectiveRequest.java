package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class ObjectiveRequest {
    private String objective;
    private Date startDate;
    private Date endDate;
    private int color;
}
