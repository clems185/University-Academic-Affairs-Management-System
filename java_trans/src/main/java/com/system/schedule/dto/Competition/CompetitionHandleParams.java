package com.system.schedule.dto.competition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitionHandleParams {
    private String contestId;
    private String decision;
    private String reviewComments;
    private int reviewerId;
}