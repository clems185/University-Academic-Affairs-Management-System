package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class UpdateInformationApply {
    private String applyId;
    private String applicantId;
    private String applicantType;
    private Date applyTime;
    private String information;
    private String newProfile;
    private byte[] newPhoto;
    private Date reviewTime;
    private String reviewComments;
    private String finalDecision;
}