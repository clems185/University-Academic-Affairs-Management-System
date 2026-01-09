package com.system.schedule.dto.competition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitionHandleSearchParams {
    private String contestId;
    private String name;
    private String grade;
    private String state;
}