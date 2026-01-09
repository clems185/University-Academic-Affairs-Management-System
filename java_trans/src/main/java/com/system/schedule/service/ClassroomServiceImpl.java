package com.system.schedule.service;

import com.system.schedule.dto.classroom.ClassroomQueryReq;
import com.system.schedule.dto.classroom.ClassroomUsageRes;
import com.system.schedule.service.IClassroomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements IClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class);

    @Override
    public CompletableFuture<List<ClassroomUsageRes>> getClassroomUsageAsync(ClassroomQueryReq req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取教室使用情况，校区: {}, 教学楼: {}, 日期: {}", 
                        req.getCampusId(), req.getBuildingId(), req.getDate());
                
                // 模拟数据
                List<ClassroomUsageRes> classroomUsages = mockClassroomUsages();
                
                // 过滤数据
                List<ClassroomUsageRes> filteredUsages = classroomUsages.stream()
                        .filter(usage -> req.getCampusId() == null || usage.getCampusId().equals(req.getCampusId()))
                        .filter(usage -> req.getBuildingId() == null || usage.getBuildingId().equals(req.getBuildingId()))
                        .filter(usage -> req.getDate() == null || usage.getDate().equals(req.getDate()))
                        .filter(usage -> req.getTimeSlotId() == null || usage.getTimeSlotId().equals(req.getTimeSlotId()))
                        .collect(Collectors.toList());
                
                logger.info("获取教室使用情况成功，共 {} 条记录", filteredUsages.size());
                return filteredUsages;
                
            } catch (Exception e) {
                logger.error("获取教室使用情况失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<List<ClassroomUsageRes>> getCampusListAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取校区列表");
                
                // 模拟数据
                List<ClassroomUsageRes> campuses = mockCampuses();
                
                logger.info("获取校区列表成功，共 {} 个校区", campuses.size());
                return campuses;
                
            } catch (Exception e) {
                logger.error("获取校区列表失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<List<ClassroomUsageRes>> getBuildingListAsync(String campusId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取校区 {} 的教学楼列表", campusId);
                
                // 模拟数据
                List<ClassroomUsageRes> buildings = mockBuildings();
                
                // 过滤数据
                List<ClassroomUsageRes> filteredBuildings = buildings.stream()
                        .filter(building -> building.getCampusId().equals(campusId))
                        .collect(Collectors.toList());
                
                logger.info("获取校区 {} 的教学楼列表成功，共 {} 栋教学楼", campusId, filteredBuildings.size());
                return filteredBuildings;
                
            } catch (Exception e) {
                logger.error("获取教学楼列表失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<List<ClassroomUsageRes>> getTimeSlotListAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取时间段列表");
                
                // 模拟数据
                List<ClassroomUsageRes> timeSlots = mockTimeSlots();
                
                logger.info("获取时间段列表成功，共 {} 个时间段", timeSlots.size());
                return timeSlots;
                
            } catch (Exception e) {
                logger.error("获取时间段列表失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<ClassroomUsageRes> getClassroomInfoAsync(String classroomId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取教室 {} 的详细信息", classroomId);
                
                // 模拟数据
                List<ClassroomUsageRes> classrooms = mockClassrooms();
                
                // 查找指定教室
                ClassroomUsageRes classroomInfo = classrooms.stream()
                        .filter(classroom -> classroom.getClassroomId().equals(classroomId))
                        .findFirst()
                        .orElse(null);
                
                if (classroomInfo != null) {
                    logger.info("获取教室 {} 的详细信息成功", classroomId);
                } else {
                    logger.warn("未找到教室 {} 的详细信息", classroomId);
                }
                
                return classroomInfo;
                
            } catch (Exception e) {
                logger.error("获取教室详细信息失败: {}", e.getMessage(), e);
                return null;
            }
        });
    }
    
    // 模拟数据
    private List<ClassroomUsageRes> mockClassroomUsages() {
        List<ClassroomUsageRes> usages = new ArrayList<>();
        
        String[] campusIds = {"CAMPUS1", "CAMPUS2"};
        String[] buildingIds = {"BUILDING1", "BUILDING2", "BUILDING3", "BUILDING4"};
        String[] classroomIds = {"CR101", "CR102", "CR201", "CR202", "CR301", "CR302"};
        String[] timeSlotIds = {"TS1", "TS2", "TS3", "TS4", "TS5", "TS6"};
        String[] dates = {"2024-05-20", "2024-05-21", "2024-05-22", "2024-05-23", "2024-05-24"};
        String[] courses = {"计算机基础", "编程入门", "高等数学", "大学物理", "大学英语"};
        
        Random random = new Random();
        
        for (String campusId : campusIds) {
            for (String buildingId : buildingIds) {
                for (String classroomId : classroomIds) {
                    for (String date : dates) {
                        for (String timeSlotId : timeSlotIds) {
                            // 不是每个时间段都有课程
                            if (random.nextBoolean()) {
                                ClassroomUsageRes usage = new ClassroomUsageRes();
                                usage.setCampusId(campusId);
                                usage.setBuildingId(buildingId);
                                usage.setClassroomId(classroomId);
                                usage.setTimeSlotId(timeSlotId);
                                usage.setDate(date);
                                usage.setCourseName(courses[random.nextInt(courses.length)]);
                                usage.setTeacherName("教师" + random.nextInt(100));
                                usage.setIsAvailable(false);
                                usages.add(usage);
                            } else {
                                ClassroomUsageRes usage = new ClassroomUsageRes();
                                usage.setCampusId(campusId);
                                usage.setBuildingId(buildingId);
                                usage.setClassroomId(classroomId);
                                usage.setTimeSlotId(timeSlotId);
                                usage.setDate(date);
                                usage.setCourseName(null);
                                usage.setTeacherName(null);
                                usage.setIsAvailable(true);
                                usages.add(usage);
                            }
                        }
                    }
                }
            }
        }
        
        return usages;
    }
    
    private List<ClassroomUsageRes> mockCampuses() {
        List<ClassroomUsageRes> campuses = new ArrayList<>();
        
        String[] campusIds = {"CAMPUS1", "CAMPUS2"};
        String[] campusNames = {"主校区", "分校区"};
        
        for (int i = 0; i < campusIds.length; i++) {
            ClassroomUsageRes campus = new ClassroomUsageRes();
            campus.setCampusId(campusIds[i]);
            campus.setCampusName(campusNames[i]);
            campuses.add(campus);
        }
        
        return campuses;
    }
    
    private List<ClassroomUsageRes> mockBuildings() {
        List<ClassroomUsageRes> buildings = new ArrayList<>();
        
        String[] campusIds = {"CAMPUS1", "CAMPUS1", "CAMPUS2", "CAMPUS2"};
        String[] buildingIds = {"BUILDING1", "BUILDING2", "BUILDING3", "BUILDING4"};
        String[] buildingNames = {"第一教学楼", "第二教学楼", "第三教学楼", "第四教学楼"};
        
        for (int i = 0; i < buildingIds.length; i++) {
            ClassroomUsageRes building = new ClassroomUsageRes();
            building.setCampusId(campusIds[i]);
            building.setBuildingId(buildingIds[i]);
            building.setBuildingName(buildingNames[i]);
            buildings.add(building);
        }
        
        return buildings;
    }
    
    private List<ClassroomUsageRes> mockTimeSlots() {
        List<ClassroomUsageRes> timeSlots = new ArrayList<>();
        
        String[] timeSlotIds = {"TS1", "TS2", "TS3", "TS4", "TS5", "TS6"};
        String[] timeSlotNames = {"08:00-09:40", "10:00-11:40", "14:00-15:40", "16:00-17:40", "19:00-20:40", "21:00-22:40"};
        
        for (int i = 0; i < timeSlotIds.length; i++) {
            ClassroomUsageRes timeSlot = new ClassroomUsageRes();
            timeSlot.setTimeSlotId(timeSlotIds[i]);
            timeSlot.setTimeSlotName(timeSlotNames[i]);
            timeSlots.add(timeSlot);
        }
        
        return timeSlots;
    }
    
    private List<ClassroomUsageRes> mockClassrooms() {
        List<ClassroomUsageRes> classrooms = new ArrayList<>();
        
        String[] campusIds = {"CAMPUS1", "CAMPUS1", "CAMPUS2", "CAMPUS2"};
        String[] buildingIds = {"BUILDING1", "BUILDING2", "BUILDING3", "BUILDING4"};
        String[] classroomIds = {"CR101", "CR102", "CR201", "CR202", "CR301", "CR302"};
        int[] capacities = {50, 60, 70, 80, 90, 100};
        
        for (int i = 0; i < classroomIds.length; i++) {
            ClassroomUsageRes classroom = new ClassroomUsageRes();
            classroom.setCampusId(campusIds[i % campusIds.length]);
            classroom.setBuildingId(buildingIds[i % buildingIds.length]);
            classroom.setClassroomId(classroomIds[i]);
            classroom.setClassroomName(classroomIds[i]);
            classroom.setCapacity(capacities[i]);
            classrooms.add(classroom);
        }
        
        return classrooms;
    }
}