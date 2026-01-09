package com.system.schedule.service;

import com.system.schedule.dto.adminpublish.*;
import com.system.schedule.service.IAdminPublicSelectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminPublicSelectionServiceImpl implements IAdminPublicSelectionService {

    // 模拟数据存储
    private List<AdminPublicSelectionDto> selections = new ArrayList<>();
    private List<SelectionCourseDto> allCourses = new ArrayList<>();
    private List<SelectionMajorDto> allMajors = new ArrayList<>();
    
    public AdminPublicSelectionServiceImpl() {
        // 初始化模拟数据
        initMockData();
    }
    
    private void initMockData() {
        // 初始化课程数据
        SelectionCourseDto course1 = new SelectionCourseDto();
        course1.setCourseId("c001");
        course1.setCourseName("高等数学");
        course1.setCredits(4);
        course1.setIsSelect("N");
        allCourses.add(course1);
        
        SelectionCourseDto course2 = new SelectionCourseDto();
        course2.setCourseId("c002");
        course2.setCourseName("线性代数");
        course2.setCredits(3);
        course2.setIsSelect("N");
        allCourses.add(course2);
        
        SelectionCourseDto course3 = new SelectionCourseDto();
        course3.setCourseId("c003");
        course3.setCourseName("大学英语");
        course3.setCredits(4);
        course3.setIsSelect("N");
        allCourses.add(course3);
        
        SelectionCourseDto course4 = new SelectionCourseDto();
        course4.setCourseId("c004");
        course4.setCourseName("计算机组成原理");
        course4.setCredits(3);
        course4.setIsSelect("N");
        allCourses.add(course4);
        
        // 初始化专业数据
        SelectionMajorDto major1 = new SelectionMajorDto();
        major1.setMajorId("m001");
        major1.setMajorName("计算机科学与技术");
        major1.setDepartmentName("计算机学院");
        allMajors.add(major1);
        
        SelectionMajorDto major2 = new SelectionMajorDto();
        major2.setMajorId("m002");
        major2.setMajorName("软件工程");
        major2.setDepartmentName("计算机学院");
        allMajors.add(major2);
        
        SelectionMajorDto major3 = new SelectionMajorDto();
        major3.setMajorId("m003");
        major3.setMajorName("数据科学与大数据技术");
        major3.setDepartmentName("计算机学院");
        allMajors.add(major3);
        
        SelectionMajorDto major4 = new SelectionMajorDto();
        major4.setMajorId("m004");
        major4.setMajorName("电子信息工程");
        major4.setDepartmentName("电子工程学院");
        allMajors.add(major4);
        
        // 初始化选课数据
        AdminPublicSelectionDto selection1 = new AdminPublicSelectionDto();
        selection1.setSelectionId("s001");
        selection1.setInformation("2023-2024学年第一学期公共选修课");
        selection1.setBeginTime(LocalDateTime.now().minusDays(10));
        selection1.setEndTime(LocalDateTime.now().plusDays(5));
        selection1.setActive(true);
        selection1.setType(0); // 随机踢人
        selection1.setSemester(0); // 第一学期
        selection1.setYear(2023); // 学年
        selection1.setIsProcessed("0"); // 未处理
        selection1.setCourses(Arrays.asList(course1, course2));
        selection1.setMajors(Arrays.asList(major1, major2, major3));
        selections.add(selection1);
        
        AdminPublicSelectionDto selection2 = new AdminPublicSelectionDto();
        selection2.setSelectionId("s002");
        selection2.setInformation("2023-2024学年第一学期专业选修课");
        selection2.setBeginTime(LocalDateTime.now().minusDays(5));
        selection2.setEndTime(LocalDateTime.now().plusDays(10));
        selection2.setActive(true);
        selection2.setType(1); // 人满为止
        selection2.setSemester(0); // 第一学期
        selection2.setYear(2023); // 学年
        selection2.setIsProcessed("0"); // 未处理
        selection2.setCourses(Arrays.asList(course3, course4));
        selection2.setMajors(Arrays.asList(major1, major2));
        selections.add(selection2);
    }
    
    @Override
    public CompletableFuture<List<AdminPublicSelectionDto>> getAllSelectionsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("获取所有选课活动，共 {} 条数据", selections.size());
                return new ArrayList<>(selections);
            } catch (Exception e) {
                log.error("获取所有选课活动失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }
    
    @Override
    public CompletableFuture<AdminPublicSelectionDto> getSelectionByIdAsync(String selectionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<AdminPublicSelectionDto> selection = selections.stream()
                        .filter(s -> s.getSelectionId().equals(selectionId))
                        .findFirst();
                
                if (selection.isPresent()) {
                    log.info("获取选课活动成功，ID: {}", selectionId);
                    return selection.get();
                } else {
                    log.warn("未找到选课活动，ID: {}", selectionId);
                    return null;
                }
            } catch (Exception e) {
                log.error("获取选课活动失败: {}", e.getMessage(), e);
                return null;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> createSelectionAsync(CreateSelectionDto dto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                AdminPublicSelectionDto newSelection = new AdminPublicSelectionDto();
                newSelection.setSelectionId("s" + UUID.randomUUID().toString().substring(0, 8));
                newSelection.setInformation(dto.getInformation());
                newSelection.setBeginTime(dto.getBeginTime());
                newSelection.setEndTime(dto.getEndTime());
                newSelection.setActive(true);
                newSelection.setType(dto.getType());
                newSelection.setSemester(dto.getSemester());
                newSelection.setYear(dto.getYear());
                newSelection.setIsProcessed("0");
                
                // 添加课程
                List<SelectionCourseDto> selectedCourses = dto.getCourseIds().stream()
                        .map(courseId -> allCourses.stream()
                                .filter(c -> c.getCourseId().equals(courseId))
                                .findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                newSelection.setCourses(selectedCourses);
                
                // 添加专业
                List<SelectionMajorDto> selectedMajors = dto.getMajorIds().stream()
                        .map(majorId -> allMajors.stream()
                                .filter(m -> m.getMajorId().equals(majorId))
                                .findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                newSelection.setMajors(selectedMajors);
                
                selections.add(newSelection);
                log.info("创建选课活动成功，ID: {}", newSelection.getSelectionId());
                return true;
            } catch (Exception e) {
                log.error("创建选课活动失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> updateSelectionAsync(UpdateSelectionDto dto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<AdminPublicSelectionDto> optionalSelection = selections.stream()
                        .filter(s -> s.getSelectionId().equals(dto.getSelectionId()))
                        .findFirst();
                
                if (optionalSelection.isPresent()) {
                    AdminPublicSelectionDto selection = optionalSelection.get();
                    
                    // 更新基本信息
                    if (dto.getInformation() != null) {
                        selection.setInformation(dto.getInformation());
                    }
                    if (dto.getBeginTime() != null) {
                        selection.setBeginTime(dto.getBeginTime());
                    }
                    if (dto.getEndTime() != null) {
                        selection.setEndTime(dto.getEndTime());
                    }
                    if (dto.getType() != null) {
                        selection.setType(dto.getType());
                    }
                    if (dto.getSemester() != null) {
                        selection.setSemester(dto.getSemester());
                    }
                    if (dto.getYear() != null) {
                        selection.setYear(dto.getYear());
                    }
                    
                    // 更新课程
                    if (dto.getCourseIds() != null) {
                        List<SelectionCourseDto> updatedCourses = dto.getCourseIds().stream()
                                .map(courseId -> allCourses.stream()
                                        .filter(c -> c.getCourseId().equals(courseId))
                                        .findFirst().orElse(null))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        selection.setCourses(updatedCourses);
                    }
                    
                    // 更新专业
                    if (dto.getMajorIds() != null) {
                        List<SelectionMajorDto> updatedMajors = dto.getMajorIds().stream()
                                .map(majorId -> allMajors.stream()
                                        .filter(m -> m.getMajorId().equals(majorId))
                                        .findFirst().orElse(null))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        selection.setMajors(updatedMajors);
                    }
                    
                    log.info("更新选课活动成功，ID: {}", dto.getSelectionId());
                    return true;
                } else {
                    log.warn("未找到选课活动，ID: {}", dto.getSelectionId());
                    return false;
                }
            } catch (Exception e) {
                log.error("更新选课活动失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> deleteSelectionAsync(String selectionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean removed = selections.removeIf(s -> s.getSelectionId().equals(selectionId));
                
                if (removed) {
                    log.info("删除选课活动成功，ID: {}", selectionId);
                    return true;
                } else {
                    log.warn("未找到选课活动，ID: {}", selectionId);
                    return false;
                }
            } catch (Exception e) {
                log.error("删除选课活动失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> addCoursesToSelectionAsync(String selectionId, List<String> courseIds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<AdminPublicSelectionDto> optionalSelection = selections.stream()
                        .filter(s -> s.getSelectionId().equals(selectionId))
                        .findFirst();
                
                if (optionalSelection.isPresent()) {
                    AdminPublicSelectionDto selection = optionalSelection.get();
                    
                    // 获取要添加的课程
                    List<SelectionCourseDto> coursesToAdd = courseIds.stream()
                            .map(courseId -> allCourses.stream()
                                    .filter(c -> c.getCourseId().equals(courseId))
                                    .findFirst().orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    
                    // 添加新课程（去重）
                    Set<String> existingCourseIds = selection.getCourses().stream()
                            .map(SelectionCourseDto::getCourseId)
                            .collect(Collectors.toSet());
                    
                    for (SelectionCourseDto course : coursesToAdd) {
                        if (!existingCourseIds.contains(course.getCourseId())) {
                            selection.getCourses().add(course);
                            existingCourseIds.add(course.getCourseId());
                        }
                    }
                    
                    log.info("添加课程到选课活动成功，ID: {}，添加了 {} 门课程", selectionId, coursesToAdd.size());
                    return true;
                } else {
                    log.warn("未找到选课活动，ID: {}", selectionId);
                    return false;
                }
            } catch (Exception e) {
                log.error("添加课程到选课活动失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> removeCoursesFromSelectionAsync(String selectionId, List<String> courseIds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<AdminPublicSelectionDto> optionalSelection = selections.stream()
                        .filter(s -> s.getSelectionId().equals(selectionId))
                        .findFirst();
                
                if (optionalSelection.isPresent()) {
                    AdminPublicSelectionDto selection = optionalSelection.get();
                    
                    // 移除指定课程
                    int initialSize = selection.getCourses().size();
                    selection.getCourses().removeIf(course -> courseIds.contains(course.getCourseId()));
                    int removedCount = initialSize - selection.getCourses().size();
                    
                    log.info("从选课活动移除课程成功，ID: {}，移除了 {} 门课程", selectionId, removedCount);
                    return true;
                } else {
                    log.warn("未找到选课活动，ID: {}", selectionId);
                    return false;
                }
            } catch (Exception e) {
                log.error("从选课活动移除课程失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> addMajorsToSelectionAsync(String selectionId, List<String> majorIds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<AdminPublicSelectionDto> optionalSelection = selections.stream()
                        .filter(s -> s.getSelectionId().equals(selectionId))
                        .findFirst();
                
                if (optionalSelection.isPresent()) {
                    AdminPublicSelectionDto selection = optionalSelection.get();
                    
                    // 获取要添加的专业
                    List<SelectionMajorDto> majorsToAdd = majorIds.stream()
                            .map(majorId -> allMajors.stream()
                                    .filter(m -> m.getMajorId().equals(majorId))
                                    .findFirst().orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    
                    // 添加新专业（去重）
                    Set<String> existingMajorIds = selection.getMajors().stream()
                            .map(SelectionMajorDto::getMajorId)
                            .collect(Collectors.toSet());
                    
                    for (SelectionMajorDto major : majorsToAdd) {
                        if (!existingMajorIds.contains(major.getMajorId())) {
                            selection.getMajors().add(major);
                            existingMajorIds.add(major.getMajorId());
                        }
                    }
                    
                    log.info("添加专业到选课活动成功，ID: {}，添加了 {} 个专业", selectionId, majorsToAdd.size());
                    return true;
                } else {
                    log.warn("未找到选课活动，ID: {}", selectionId);
                    return false;
                }
            } catch (Exception e) {
                log.error("添加专业到选课活动失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> removeMajorsFromSelectionAsync(String selectionId, List<String> majorIds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<AdminPublicSelectionDto> optionalSelection = selections.stream()
                        .filter(s -> s.getSelectionId().equals(selectionId))
                        .findFirst();
                
                if (optionalSelection.isPresent()) {
                    AdminPublicSelectionDto selection = optionalSelection.get();
                    
                    // 移除指定专业
                    int initialSize = selection.getMajors().size();
                    selection.getMajors().removeIf(major -> majorIds.contains(major.getMajorId()));
                    int removedCount = initialSize - selection.getMajors().size();
                    
                    log.info("从选课活动移除专业成功，ID: {}，移除了 {} 个专业", selectionId, removedCount);
                    return true;
                } else {
                    log.warn("未找到选课活动，ID: {}", selectionId);
                    return false;
                }
            } catch (Exception e) {
                log.error("从选课活动移除专业失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> isSelectionTimeValidAsync(LocalDateTime beginTime, LocalDateTime endTime, String excludeSelectionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 检查时间有效性
                if (beginTime == null || endTime == null || beginTime.isAfter(endTime)) {
                    return false;
                }
                
                // 检查是否与现有选课活动时间冲突
                for (AdminPublicSelectionDto selection : selections) {
                    if (selection.getSelectionId().equals(excludeSelectionId)) {
                        continue; // 排除当前编辑的活动
                    }
                    
                    if ((beginTime.isBefore(selection.getEndTime()) && endTime.isAfter(selection.getBeginTime())) ||
                        beginTime.equals(selection.getBeginTime()) || endTime.equals(selection.getEndTime())) {
                        return false; // 时间冲突
                    }
                }
                
                log.info("选课活动时间有效");
                return true;
            } catch (Exception e) {
                log.error("检查选课活动时间有效性失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> processSelectionResultAsync(String selectionId, boolean forceProcess) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<AdminPublicSelectionDto> optionalSelection = selections.stream()
                        .filter(s -> s.getSelectionId().equals(selectionId))
                        .findFirst();
                
                if (optionalSelection.isPresent()) {
                    AdminPublicSelectionDto selection = optionalSelection.get();
                    
                    // 检查是否可以处理
                    if (!forceProcess && LocalDateTime.now().isBefore(selection.getEndTime())) {
                        log.warn("选课活动尚未结束，无法处理结果，ID: {}", selectionId);
                        return false;
                    }
                    
                    // 标记为已处理
                    selection.setIsProcessed("1");
                    log.info("处理选课结果成功，ID: {}", selectionId);
                    return true;
                } else {
                    log.warn("未找到选课活动，ID: {}", selectionId);
                    return false;
                }
            } catch (Exception e) {
                log.error("处理选课结果失败: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SelectionCourseDto>> getAllCoursesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("获取所有课程，共 {} 门", allCourses.size());
                return new ArrayList<>(allCourses);
            } catch (Exception e) {
                log.error("获取所有课程失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SelectionMajorDto>> getAllMajorsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("获取所有专业，共 {} 个", allMajors.size());
                return new ArrayList<>(allMajors);
            } catch (Exception e) {
                log.error("获取所有专业失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<String>> getAllCourseIdsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<String> courseIds = allCourses.stream()
                        .map(SelectionCourseDto::getCourseId)
                        .collect(Collectors.toList());
                log.info("获取所有课程ID，共 {} 个", courseIds.size());
                return courseIds;
            } catch (Exception e) {
                log.error("获取所有课程ID失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<String>> getAllMajorIdsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<String> majorIds = allMajors.stream()
                        .map(SelectionMajorDto::getMajorId)
                        .collect(Collectors.toList());
                log.info("获取所有专业ID，共 {} 个", majorIds.size());
                return majorIds;
            } catch (Exception e) {
                log.error("获取所有专业ID失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }
    
    // 辅助方法：根据CourseId获取课程
    private SelectionCourseDto getCourseById(String courseId) {
        return allCourses.stream()
                .filter(course -> course.getCourseId().equals(courseId))
                .findFirst().orElse(null);
    }
    
    // 辅助方法：根据MajorId获取专业
    private SelectionMajorDto getMajorById(String majorId) {
        return allMajors.stream()
                .filter(major -> major.getMajorId().equals(majorId))
                .findFirst().orElse(null);
    }
}