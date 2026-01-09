package com.system.schedule.dto.invigilate;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

// 座位安排数据传输对象
@Getter
@Setter
public class SeatArrangementDto {
    // 一个二维列表，代表了整个教室的座位安排
    // 每个元素是一个 StudentSeatInfo 对象，表示该座位上的学生信息，也可能是空位
    private List<List<StudentSeatInfo>> seatingPlan;
}

// 座位上的学生信息
@Getter
@Setter
class StudentSeatInfo {
    private String studentId;
    private String studentName;
}