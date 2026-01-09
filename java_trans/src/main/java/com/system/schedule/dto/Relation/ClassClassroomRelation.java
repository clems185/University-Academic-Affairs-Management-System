package com.system.schedule.dto.relation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassClassroomRelation {
    private String courseId;
    private String classId;
    private String semester;
    private String year;
    private String classroomId;
    private String equipmentInfo;
}