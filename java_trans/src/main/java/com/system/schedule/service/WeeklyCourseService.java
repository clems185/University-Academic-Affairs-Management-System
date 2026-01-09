// WeeklyCourseService.java
package com.system.schedule.service;

import com.system.schedule.dto.WeeklyCourse.*;
import com.system.schedule.entity.*;
import com.system.schedule.mapper.*;
import com.system.schedule.util.ApiResult;
import com.system.schedule.util.ResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeeklyCourseService {

    @Autowired
    private TakesMapper takesMapper;
    
    @Autowired
    private ClassMapper classMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private TeacherMapper teacherMapper;
    
    @Autowired
    private ClassClassroomMapper classClassroomMapper;
    
    @Autowired
    private ClassroomMapper classroomMapper;
    
    @Autowired
    private BuildingMapper buildingMapper;
    
    @Autowired
    private CampusMapper campusMapper;
    
    @Autowired
    private ClassTimeMapper classTimeMapper;
    
    @Autowired
    private TimeSlotMapper timeSlotMapper;
    
    @Autowired
    private TeachesMapper teachesMapper;

    /**
     * 获取通用周课表
     */
    public ApiResult getWeeklyScheduleAsync(WeeklyScheduleSearchParams searchParams) {
        try {
            log.info("开始获取周课表，用户类型: {}, 用户ID: {}", searchParams.getUserType(), searchParams.getUserId());

            String userType = searchParams.getUserType();
            if ("student".equals(userType) || "3".equals(userType)) {
                return getStudentScheduleAsync(searchParams);
            } else if ("teacher".equals(userType) || "2".equals(userType)) {
                return getTeacherScheduleAsync(searchParams);
            } else if ("admin".equals(userType) || "0".equals(userType)) {
                return getAdminWeeklyScheduleAsync(searchParams);
            } else {
                return ResultHelper.error("无效的用户类型，请使用: student(3), teacher(2) 或 admin(0)");
            }
        } catch (Exception ex) {
            log.error("获取周课表异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取周课表失败: " + ex.getMessage());
        }
    }

    /**
     * 获取学生课表
     */
    public ApiResult getStudentScheduleAsync(WeeklyScheduleSearchParams searchParams) {
        try {
            log.info("开始获取学生课表，学生ID: {}, 学年: {}, 学期: {}", 
                searchParams.getUserId(), searchParams.getYear(), searchParams.getSemester());

            // 获取学生所选课程ID
            Map<String, Object> takesParams = new HashMap<>();
            takesParams.put("studentId", searchParams.getUserId());
            takesParams.put("year", searchParams.getYear());
            takesParams.put("semester", searchParams.getSemester());
            
            List<Takes> studentTakes = takesMapper.selectByMap(takesParams);
            
            log.info("学生 {} 选了 {} 门课程", searchParams.getUserId(), studentTakes.size());

            if (studentTakes.isEmpty()) {
                log.info("学生未选择任何课程");
                Map<String, Object> emptyResult = new HashMap<>();
                emptyResult.put("schedule", new ArrayList<WeeklyScheduleItem>());
                emptyResult.put("courseDetails", new ArrayList<CourseDetailDto>());
                return ResultHelper.success(emptyResult);
            }

            // 获取课程详细信息
            List<CourseInfoDto> courseList = getCourseInfoForStudent(searchParams, studentTakes);
            log.info("查询到 {} 条课程信息", courseList.size());

            // 获取所有时间段信息
            List<TimeSlot> timeSlots = timeSlotMapper.selectList(null);
            log.info("获取到 {} 个时间段信息", timeSlots.size());

            // 处理课程时间段信息
            List<WeeklyScheduleItem> result = processCourseSchedule(courseList, timeSlots, searchParams.getWeek());

            // 获取课程详细信息（去重）
            List<CourseDetailDto> courseDetails = courseList.stream()
                .collect(Collectors.groupingBy(CourseInfoDto::getCourseId))
                .values().stream()
                .map(list -> list.get(0))
                .map(c -> {
                    CourseDetailDto dto = new CourseDetailDto();
                    dto.setCourseId(c.getCourseId());
                    dto.setCourseName(c.getCourseName());
                    dto.setCourseType("Y".equals(c.getIsSelect()) ? "选修" : "必修");
                    dto.setAssessmentType("Y".equals(c.getIsExam()) ? "考试" : "考察");
                    dto.setCredits(c.getCredits());
                    dto.setInformation(c.getInformation());
                    return dto;
                })
                .collect(Collectors.toList());

            log.info("处理完成，生成 {} 个课表项, {} 条课程详情", result.size(), courseDetails.size());
            
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("schedule", result);
            resultMap.put("courseDetails", courseDetails);
            return ResultHelper.success(resultMap);
        } catch (Exception ex) {
            log.error("获取学生课表异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取学生课表失败: " + ex.getMessage());
        }
    }

    /**
     * 获取教师课表
     */
    public ApiResult getTeacherScheduleAsync(WeeklyScheduleSearchParams searchParams) {
        try {
            log.info("开始获取教师课表，教师ID: {}, 学年: {}, 学期: {}", 
                searchParams.getUserId(), searchParams.getYear(), searchParams.getSemester());

            // 获取教师所教课程
            Map<String, Object> teachesParams = new HashMap<>();
            teachesParams.put("teacherId", searchParams.getUserId());
            teachesParams.put("year", searchParams.getYear());
            teachesParams.put("semester", searchParams.getSemester());
            
            List<Teaches> teacherTeaches = teachesMapper.selectByMap(teachesParams);
            
            log.info("教师 {} 教授 {} 门课程", searchParams.getUserId(), teacherTeaches.size());

            if (teacherTeaches.isEmpty()) {
                log.info("教师未教授任何课程");
                Map<String, Object> emptyResult = new HashMap<>();
                emptyResult.put("schedule", new ArrayList<WeeklyScheduleItem>());
                emptyResult.put("courseDetails", new ArrayList<CourseDetailDto>());
                return ResultHelper.success(emptyResult);
            }

            // 获取课程详细信息
            List<CourseInfoDto> courseList = getCourseInfoForTeacher(searchParams, teacherTeaches);
            log.info("查询到 {} 条课程信息", courseList.size());

            // 获取所有时间段信息
            List<TimeSlot> timeSlots = timeSlotMapper.selectList(null);
            log.info("获取到 {} 个时间段信息", timeSlots.size());

            // 处理课程时间段信息
            List<WeeklyScheduleItem> result = processCourseSchedule(courseList, timeSlots, searchParams.getWeek());

            // 获取课程详细信息（去重）
            List<CourseDetailDto> courseDetails = courseList.stream()
                .collect(Collectors.groupingBy(CourseInfoDto::getCourseId))
                .values().stream()
                .map(list -> list.get(0))
                .map(c -> {
                    CourseDetailDto dto = new CourseDetailDto();
                    dto.setCourseId(c.getCourseId());
                    dto.setCourseName(c.getCourseName());
                    dto.setCourseType("Y".equals(c.getIsSelect()) ? "选修" : "必修");
                    dto.setAssessmentType("Y".equals(c.getIsExam()) ? "考试" : "考察");
                    dto.setCredits(c.getCredits());
                    dto.setInformation(c.getInformation());
                    return dto;
                })
                .collect(Collectors.toList());

            log.info("处理完成，生成 {} 个课表项, {} 条课程详情", result.size(), courseDetails.size());
            
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("schedule", result);
            resultMap.put("courseDetails", courseDetails);
            return ResultHelper.success(resultMap);
        } catch (Exception ex) {
            log.error("获取教师课表异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取教师课表失败: " + ex.getMessage());
        }
    }

    /**
     * 获取管理员课表
     */
    public ApiResult getAdminWeeklyScheduleAsync(WeeklyScheduleSearchParams searchParams) {
        try {
            log.info("开始获取管理员课表，学年: {}, 学期: {}", 
                searchParams.getYear(), searchParams.getSemester());

            // 获取所有课程信息
            List<CourseInfoDto> courseList = getCourseInfoForAdmin(searchParams);
            log.info("查询到 {} 条课程信息", courseList.size());

            // 获取所有时间段信息
            List<TimeSlot> timeSlots = timeSlotMapper.selectList(null);
            log.info("获取到 {} 个时间段信息", timeSlots.size());

            // 处理课程时间段信息
            List<WeeklyScheduleItem> result = processCourseSchedule(courseList, timeSlots, searchParams.getWeek());

            // 获取课程详细信息（去重）
            List<CourseDetailDto> courseDetails = courseList.stream()
                .collect(Collectors.groupingBy(CourseInfoDto::getCourseId))
                .values().stream()
                .map(list -> list.get(0))
                .map(c -> {
                    CourseDetailDto dto = new CourseDetailDto();
                    dto.setCourseId(c.getCourseId());
                    dto.setCourseName(c.getCourseName());
                    dto.setCourseType("Y".equals(c.getIsSelect()) ? "选修" : "必修");
                    dto.setAssessmentType("Y".equals(c.getIsExam()) ? "考试" : "考察");
                    dto.setCredits(c.getCredits());
                    dto.setInformation(c.getInformation());
                    return dto;
                })
                .collect(Collectors.toList());

            // 生成统计信息
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCourses", result.size());
            summary.put("totalHours", calculateTotalHours(result));
            summary.put("uniqueCourses", result.stream().map(WeeklyScheduleItem::getCourseId).distinct().count());
            summary.put("totalStudents", result.stream().mapToInt(WeeklyScheduleItem::getStudentCount).sum());

            log.info("处理完成，生成 {} 个课表项，统计信息: {}", result.size(), summary);
            
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("schedule", result);
            resultMap.put("courseDetails", courseDetails);
            resultMap.put("summary", summary);
            return ResultHelper.success(resultMap);
        } catch (Exception ex) {
            log.error("获取管理员课表异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取管理员课表失败: " + ex.getMessage());
        }
    }

    /**
     * 处理课程时间段信息
     */
    private List<WeeklyScheduleItem> processCourseSchedule(List<CourseInfoDto> courseList, List<TimeSlot> timeSlots, Integer week) {
        List<WeeklyScheduleItem> result = new ArrayList<>();

        for (CourseInfoDto course : courseList) {
            // 解析周次时间段数据
            ClassTime weekData = course.getWeekData();
            if (weekData == null) {
                log.info("课程 {}-{} 没有时间段数据", course.getCourseId(), course.getClassId());
                continue;
            }

            List<String> timeSlotIds = new ArrayList<>();

            // 获取指定周次或所有周次的时间段
            if (week != null && week > 0) {
                int weekNum = week;
                // 检查周次是否在课程的有效范围内
                if (weekNum < course.getCourseBeginWeek() || weekNum > course.getCourseEndWeek()) {
                    log.info("课程 {}-{} 第{}周不在有效范围内({}-{})", 
                        course.getCourseId(), course.getClassId(), weekNum, 
                        course.getCourseBeginWeek(), course.getCourseEndWeek());
                    continue;
                }

                try {
                    // 使用反射获取周次属性
                    String weekValue = (String) weekData.getClass()
                        .getMethod("getWeek" + weekNum)
                        .invoke(weekData);
                    
                    if (StringUtils.hasText(weekValue)) {
                        Collections.addAll(timeSlotIds, weekValue.split(","));
                        log.info("课程 {}-{} 第{}周时间段: {}", 
                            course.getCourseId(), course.getClassId(), weekNum, weekValue);
                    }
                } catch (Exception ex) {
                    log.error("获取周次时间段失败: {}", ex.getMessage());
                }
            } else {
                // 获取所有周次的时间段（只在课程有效周次范围内）
                for (int i = course.getCourseBeginWeek(); i <= course.getCourseEndWeek(); i++) {
                    try {
                        String weekValue = (String) weekData.getClass()
                            .getMethod("getWeek" + i)
                            .invoke(weekData);
                        
                        if (StringUtils.hasText(weekValue)) {
                            Collections.addAll(timeSlotIds, weekValue.split(","));
                        }
                    } catch (Exception ex) {
                        // 忽略不存在的周次属性
                    }
                }
                log.info("课程 {}-{} 全学期时间段: {}", 
                    course.getCourseId(), course.getClassId(), String.join(",", timeSlotIds));
            }

            // 去重并排序时间段
            timeSlotIds = timeSlotIds.stream()
                .filter(StringUtils::hasText)
                .distinct()
                .sorted(Comparator.comparingInt(Integer::parseInt))
                .collect(Collectors.toList());

            if (timeSlotIds.isEmpty()) {
                log.info("课程 {}-{} 没有有效的时间段ID", course.getCourseId(), course.getClassId());
                continue;
            }

            // 按天分组时间段
            Map<String, List<String>> dayGroups = timeSlotIds.stream()
                .collect(Collectors.groupingBy(id -> {
                    Optional<TimeSlot> timeSlot = timeSlots.stream()
                        .filter(ts -> id.equals(ts.getTimeSlotId()))
                        .findFirst();
                    return timeSlot.map(TimeSlot::getDay).orElse("0");
                }));

            for (Map.Entry<String, List<String>> dayGroup : dayGroups.entrySet()) {
                // 按连续时间段分组
                List<Integer> dayTimeSlotIds = dayGroup.getValue().stream()
                    .map(Integer::parseInt)
                    .sorted()
                    .collect(Collectors.toList());
                
                List<List<Integer>> timeSlotGroups = groupConsecutiveTimeSlots(dayTimeSlotIds);

                for (List<Integer> group : timeSlotGroups) {
                    if (group.isEmpty()) continue;

                    // 获取时间段信息
                    TimeSlot firstSlot = timeSlots.stream()
                        .filter(ts -> ts.getTimeSlotId().equals(group.get(0).toString()))
                        .findFirst()
                        .orElse(null);
                    
                    TimeSlot lastSlot = timeSlots.stream()
                        .filter(ts -> ts.getTimeSlotId().equals(group.get(group.size() - 1).toString()))
                        .findFirst()
                        .orElse(null);

                    if (firstSlot == null || lastSlot == null) continue;

                    // 计算节次范围
                    String periodRange = getPeriodRange(group, timeSlots);

                    WeeklyScheduleItem item = new WeeklyScheduleItem();
                    item.setCourseId(course.getCourseId());
                    item.setCourseName(course.getCourseName());
                    item.setClassName(course.getClassName());
                    item.setTeacherName(course.getTeacherName());
                    item.setClassroom(course.getCampusName() + course.getBuildingName() + course.getRoomNumber());
                    item.setDayOfWeek(Integer.parseInt(firstSlot.getDay()) + 1);
                    item.setStartTime(firstSlot.getStartTime().toString());
                    item.setEndTime(lastSlot.getEndTime().toString());
                    item.setCampus(course.getCampusName());
                    item.setBuilding(course.getBuildingName());
                    item.setRoomNumber(course.getRoomNumber());
                    item.setPeriod(periodRange);
                    item.setWeekPattern(week != null ? "第" + week + "周" : 
                        "第" + course.getCourseBeginWeek() + "-" + course.getCourseEndWeek() + "周");
                    item.setYear(course.getYear());
                    item.setSemester(course.getSemester());
                    item.setStudentCount(course.getStudentCount());
                    item.setCourseType("Y".equals(course.getIsSelect()) ? "选修" : "必修");
                    item.setAssessmentType("Y".equals(course.getIsExam()) ? "考试" : "考察");
                    item.setCredits(course.getCredits());
                    item.setCourseInformation(course.getInformation());

                    result.add(item);

                    log.info("添加课表项: {}({}) 周{} {}节 {}{}{}", 
                        course.getCourseName(), course.getTeacherName(), firstSlot.getDay(),
                        periodRange, course.getCampusName(), course.getBuildingName(), course.getRoomNumber());
                }
            }
        }

        return result;
    }

    /**
     * 将连续的时间段分组
     */
    private List<List<Integer>> groupConsecutiveTimeSlots(List<Integer> timeSlotIds) {
        List<List<Integer>> result = new ArrayList<>();
        if (timeSlotIds.isEmpty()) return result;

        Collections.sort(timeSlotIds);
        List<Integer> currentGroup = new ArrayList<>();
        currentGroup.add(timeSlotIds.get(0));

        for (int i = 1; i < timeSlotIds.size(); i++) {
            if (timeSlotIds.get(i) == timeSlotIds.get(i - 1) + 1) {
                currentGroup.add(timeSlotIds.get(i));
            } else {
                result.add(currentGroup);
                currentGroup = new ArrayList<>();
                currentGroup.add(timeSlotIds.get(i));
            }
        }

        result.add(currentGroup);
        return result;
    }

    /**
     * 获取节次范围字符串
     */
    private String getPeriodRange(List<Integer> timeSlotIds, List<TimeSlot> allTimeSlots) {
        if (timeSlotIds.size() == 1) {
            // 根据时间段的ID计算节次 (每12个时间段为一天)
            int period = (timeSlotIds.get(0) % 12) + 1;
            return String.valueOf(period);
        } else {
            int firstPeriod = (timeSlotIds.get(0) % 12) + 1;
            int lastPeriod = (timeSlotIds.get(timeSlotIds.size() - 1) % 12) + 1;
            return firstPeriod + "-" + lastPeriod;
        }
    }

    /**
     * 计算总学时
     */
    private int calculateTotalHours(List<WeeklyScheduleItem> schedule) {
        int totalHours = 0;
        for (WeeklyScheduleItem item : schedule) {
            if (item.getPeriod().contains("-")) {
                String[] periods = item.getPeriod().split("-");
                if (periods.length == 2) {
                    try {
                        int start = Integer.parseInt(periods[0]);
                        int end = Integer.parseInt(periods[1]);
                        totalHours += end - start + 1;
                    } catch (NumberFormatException e) {
                        log.error("解析节次范围失败: {}", item.getPeriod());
                    }
                }
            } else {
                totalHours += 1;
            }
        }
        return totalHours;
    }

    /**
     * 获取学生课程信息（需要根据实际情况实现）
     */
    private List<CourseInfoDto> getCourseInfoForStudent(WeeklyScheduleSearchParams searchParams, List<Takes> studentTakes) {
        // 实现具体的查询逻辑
        return new ArrayList<>();
    }

    /**
     * 获取教师课程信息（需要根据实际情况实现）
     */
    private List<CourseInfoDto> getCourseInfoForTeacher(WeeklyScheduleSearchParams searchParams, List<Teaches> teacherTeaches) {
        // 实现具体的查询逻辑
        return new ArrayList<>();
    }

    /**
     * 获取管理员课程信息（需要根据实际情况实现）
     */
    private List<CourseInfoDto> getCourseInfoForAdmin(WeeklyScheduleSearchParams searchParams) {
        // 实现具体的查询逻辑
        return new ArrayList<>();
    }
}