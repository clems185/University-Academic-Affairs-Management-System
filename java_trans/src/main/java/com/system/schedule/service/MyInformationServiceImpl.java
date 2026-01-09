package com.system.schedule.service.impl;

import com.system.schedule.dto.myinformation.MyInformationRes;
import com.system.schedule.dto.other.ApiResult;
import com.system.schedule.dto.other.PageResult;
import com.system.schedule.service.IMyInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MyInformationServiceImpl implements IMyInformationService {

    private static final Logger logger = LoggerFactory.getLogger(MyInformationServiceImpl.class);
    private static final List<String> VALID_TYPES = Arrays.asList("announcement", "notification", "reminder");

    @Override
    public CompletableFuture<ApiResult> getMyInformations(String types, LocalDateTime startTime, int page, int pageSize) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 确保分页参数有效
                page = Math.max(page, 1);
                pageSize = Math.max(pageSize, 10);
                pageSize = Math.min(pageSize, 100);

                // 模拟数据与过滤逻辑
                List<InformationMock> allInformations = mockInformations();
                
                // 类型过滤
                if (types != null && !types.trim().isEmpty()) {
                    List<String> typeList = Arrays.stream(types.split(","))
                            .map(String::trim)
                            .filter(t -> !t.isEmpty())
                            .collect(Collectors.toList());
                    
                    // 验证类型是否有效
                    List<String> invalidTypes = typeList.stream()
                            .filter(t -> !VALID_TYPES.contains(t))
                            .collect(Collectors.toList());
                    
                    if (!invalidTypes.isEmpty()) {
                        logger.warn("包含无效的消息类型: {}", String.join(", ", invalidTypes));
                        return ApiResult.error("包含无效的消息类型: " + String.join(", ", invalidTypes));
                    }
                    
                    // 应用类型过滤
                    allInformations = allInformations.stream()
                            .filter(i -> typeList.contains(i.getTypes()))
                            .collect(Collectors.toList());
                }
                
                // 时间过滤
                if (startTime != null) {
                    allInformations = allInformations.stream()
                            .filter(i -> i.getCreateTime().isAfter(startTime) || i.getCreateTime().isEqual(startTime))
                            .collect(Collectors.toList());
                }
                
                // 按创建时间倒序排序
                allInformations.sort((i1, i2) -> i2.getCreateTime().compareTo(i1.getCreateTime()));
                
                // 分页处理
                int totalCount = allInformations.size();
                int startIndex = (page - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, totalCount);
                
                List<InformationMock> paginatedInformations;
                if (startIndex < totalCount) {
                    paginatedInformations = allInformations.subList(startIndex, endIndex);
                } else {
                    paginatedInformations = new ArrayList<>();
                }
                
                // 转换为响应对象
                List<MyInformationRes> resultList = paginatedInformations.stream()
                        .map(this::toMyInformationRes)
                        .collect(Collectors.toList());
                
                PageResult<MyInformationRes> pageResult = new PageResult<>(resultList, totalCount, page, pageSize);
                
                logger.info("获取我的消息成功，类型: {}, 开始时间: {}, 页码: {}, 每页大小: {}, 总条数: {}", 
                        types, startTime, page, pageSize, totalCount);
                
                return ApiResult.success(pageResult);
                
            } catch (Exception e) {
                logger.error("获取我的消息失败: {}", e.getMessage(), e);
                return ApiResult.error("获取消息失败，请稍后重试");
            }
        });
    }
    
    /**
     * 模拟消息数据
     */
    private List<InformationMock> mockInformations() {
        List<InformationMock> informations = new ArrayList<>();
        Random random = new Random();
        
        // 模拟100条消息
        for (int i = 1; i <= 100; i++) {
            InformationMock info = new InformationMock();
            info.setId(String.valueOf(i));
            info.setTitle("消息标题 " + i);
            info.setContent("这是消息内容 " + i + "，包含了详细的信息描述。");
            info.setTypes(VALID_TYPES.get(random.nextInt(VALID_TYPES.size())));
            // 随机生成过去30天内的时间
            info.setCreateTime(LocalDateTime.now().minusDays(random.nextInt(30))
                    .minusHours(random.nextInt(24))
                    .minusMinutes(random.nextInt(60)));
            info.setIsRead(random.nextBoolean());
            informations.add(info);
        }
        
        return informations;
    }
    
    /**
     * 转换为MyInformationRes对象
     */
    private MyInformationRes toMyInformationRes(InformationMock mock) {
        MyInformationRes res = new MyInformationRes();
        res.setId(mock.getId());
        res.setTitle(mock.getTitle());
        res.setContent(mock.getContent());
        res.setTypes(mock.getTypes());
        res.setCreateTime(mock.getCreateTime());
        res.setIsRead(mock.getIsRead());
        return res;
    }
    
    /**
     * 模拟消息实体类
     */
    private static class InformationMock {
        private String id;
        private String title;
        private String content;
        private String types;
        private LocalDateTime createTime;
        private boolean isRead;
        
        // getter和setter方法
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getTypes() { return types; }
        public void setTypes(String types) { this.types = types; }
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
        public boolean getIsRead() { return isRead; }
        public void setIsRead(boolean isRead) { this.isRead = isRead; }
    }
}package com.system.schedule.service.impl;

import com.system.schedule.dto.other.ApiResult;
import com.system.schedule.dto.profile.MyInformationRes;
import com.system.schedule.service.IMyInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MyInformationServiceImpl implements IMyInformationService {

    private static final Logger logger = LoggerFactory.getLogger(MyInformationServiceImpl.class);

    // 定义有效的消息类型（中文名称）
    private static final List<String> VALID_TYPES = Arrays.asList("通知", "公告", "新闻", "文件", "活动");

    @Override
    public CompletableFuture<ApiResult> getMyInformations(String types, LocalDateTime startTime, int page, int pageSize) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始查询消息，参数: types={}, startTime={}, page={}, pageSize={}",
                        types, startTime, page, pageSize);

                // 确保分页参数有效
                page = page < 1 ? 1 : page;
                pageSize = pageSize < 1 ? 10 : pageSize;

                // 模拟数据
                List<InformationMock> allInformations = mockInformations();

                // 添加类型过滤条件
                if (types != null && !types.trim().isEmpty()) {
                    List<String> typeList = Arrays.stream(types.split(","))
                            .map(String::trim)
                            .filter(t -> !t.isEmpty())
                            .collect(Collectors.toList());

                    if (!typeList.isEmpty()) {
                        // 类型白名单验证
                        List<String> invalidTypes = typeList.stream()
                                .filter(t -> !VALID_TYPES.contains(t))
                                .collect(Collectors.toList());

                        if (!invalidTypes.isEmpty()) {
                            logger.warn("包含无效的消息类型: {}", String.join(", ", invalidTypes));
                            return ApiResult.error("包含无效的消息类型: " + String.join(", ", invalidTypes));
                        }

                        allInformations = allInformations.stream()
                                .filter(i -> typeList.contains(i.getTypes()))
                                .collect(Collectors.toList());

                        logger.info("添加类型过滤: {}", String.join(", ", typeList));
                    }
                }

                // 添加开始时间过滤
                if (startTime != null) {
                    // 时间范围限制（最多查询1年内的数据）
                    LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
                    if (startTime.isBefore(oneYearAgo)) {
                        logger.warn("时间范围超出限制: {}", startTime);
                        return ApiResult.error("时间范围不能超过1年");
                    }

                    allInformations = allInformations.stream()
                            .filter(i -> i.getPublishTime().isAfter(startTime))
                            .collect(Collectors.toList());

                    logger.info("添加开始时间过滤: {}", startTime);
                }

                // 执行分页
                int totalCount = allInformations.size();
                int startIndex = (page - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, totalCount);

                List<InformationMock> paginatedInformations = new ArrayList<>();
                if (startIndex < totalCount) {
                    paginatedInformations = allInformations.subList(startIndex, endIndex);
                }

                logger.info("查询到 {} 条消息, 总记录数: {}",
                        paginatedInformations.size(), totalCount);

                // 转换为DTO
                List<MyInformationRes> result = paginatedInformations.stream()
                        .map(this::toMyInformationRes)
                        .collect(Collectors.toList());

                // 返回分页结果
                return ApiResult.success(new PageResult(
                        result,
                        totalCount,
                        page,
                        pageSize
                ));
            } catch (Exception ex) {
                logger.error("查询消息失败: {}", ex.getMessage());
                return ApiResult.error("查询失败: " + ex.getMessage());
            }
        });
    }

    // 模拟数据类
    private static class InformationMock {
        private String infoId;
        private String types;
        private String title;
        private String content;
        private LocalDateTime publishTime;

        public InformationMock(String infoId, String types, String title, String content, LocalDateTime publishTime) {
            this.infoId = infoId;
            this.types = types;
            this.title = title;
            this.content = content;
            this.publishTime = publishTime;
        }

        public String getTypes() {
            return types;
        }

        public LocalDateTime getPublishTime() {
            return publishTime;
        }
    }

    // 分页结果类
    private static class PageResult {
        private List<MyInformationRes> data;
        private int total;
        private int page;
        private int pageSize;

        public PageResult(List<MyInformationRes> data, int total, int page, int pageSize) {
            this.data = data;
            this.total = total;
            this.page = page;
            this.pageSize = pageSize;
        }
    }

    // 模拟数据
    private List<InformationMock> mockInformations() {
        List<InformationMock> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        list.add(new InformationMock("1", "通知", "期末考试安排", "期末考试将在下周进行，请同学们做好准备", now.minusDays(1)));
        list.add(new InformationMock("2", "公告", "奖学金评定结果", "2025年奖学金评定结果已公布，请同学们查看", now.minusDays(2)));
        list.add(new InformationMock("3", "新闻", "校园招聘会", "本周将举行校园招聘会，欢迎毕业生参加", now.minusDays(3)));
        list.add(new InformationMock("4", "文件", "教学计划调整", "2025年春季学期教学计划调整通知", now.minusDays(4)));
        list.add(new InformationMock("5", "活动", "校园文化节", "2025年校园文化节将于下月举办", now.minusDays(5)));
        list.add(new InformationMock("6", "通知", "课程表更新", "部分课程表已更新，请同学们查看最新安排", now.minusDays(6)));
        list.add(new InformationMock("7", "公告", "图书馆开放时间调整", "寒假期间图书馆开放时间调整通知", now.minusDays(7)));

        return list;
    }

    // 转换为DTO
    private MyInformationRes toMyInformationRes(InformationMock information) {
        MyInformationRes res = new MyInformationRes();
        res.setInfoId(information.infoId);
        res.setTypes(information.types);
        res.setTitle(information.title);
        res.setContent(information.content);
        res.setPublishTime(information.publishTime);
        return res;
    }
}