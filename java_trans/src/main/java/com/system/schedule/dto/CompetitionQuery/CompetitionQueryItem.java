package com.system.schedule.dto.competitionquery;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

// 竞赛查询结果项
@Getter
@Setter
public class CompetitionQueryItem {
    private String contestId = "";        // 竞赛ID
    private String name = "";             // 竞赛名称
    private LocalDateTime contestTime;     // 竞赛举办时间
    private String grade = "";            // 竞赛等级
    private String url = "";              // 竞赛官网链接
    private String information = "";      // 竞赛描述信息
    private int numbers;                   // 报名人数
    private LocalDateTime createTime;      // 创建时间
    private String state = "";            // 处理状态
    private String stateText = "";        // 状态文本
}
package com.system.schedule.dto.competitionquery;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// 竞赛查询结果项
@Getter
@Setter
public class CompetitionQueryItem {
    private String contestId;        // 竞赛ID
    private String name;             // 竞赛名称
    private LocalDateTime contestTime;                    // 竞赛举办时间
    private String grade;            // 竞赛等级
    private String url;              // 竞赛官网链接
    private String information;      // 竞赛描述信息
    private int numbers;                             // 报名人数
    private LocalDateTime createTime;                     // 创建时间
    private String state;            // 处理状态
    private String stateText;        // 状态文本
}