package com.system.schedule.service;

import com.system.schedule.dto.other.PageInfo;
import com.system.schedule.dto.role.RoleAdd;
import com.system.schedule.dto.role.RoleEdit;
import com.system.schedule.dto.role.RoleReq;
import com.system.schedule.dto.role.RoleRes;
import com.system.schedule.service.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements IRoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    
    // 模拟角色数据存储
    private static final List<RoleMock> roles = new ArrayList<>();
    
    // 静态初始化一些模拟角色数据
    static {
        String[] roleNames = {"管理员", "教师", "学生", "辅导员", "教务处"};
        String[] descriptions = {"系统管理员", "教师用户", "学生用户", "辅导员用户", "教务处用户"};
        int[] orders = {1, 2, 3, 4, 5};
        
        for (int i = 0; i < roleNames.length; i++) {
            RoleMock role = new RoleMock();
            role.setId(UUID.randomUUID().toString());
            role.setName(roleNames[i]);
            role.setDescription(descriptions[i]);
            role.setOrder(orders[i]);
            role.setCreateUserId("admin");
            role.setCreateDate(LocalDateTime.now().minusDays(i));
            role.setIsDelete(false);
            roles.add(role);
        }
    }
    
    // 模拟用户数据
    private static final List<UserMock> users = new ArrayList<>();
    
    static {
        users.add(new UserMock("admin", "管理员"));
        users.add(new UserMock("user1", "用户1"));
        users.add(new UserMock("user2", "用户2"));
    }

    @Override
    public CompletableFuture<Boolean> addRole(RoleAdd req, String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始添加角色，名称: {}, 创建用户: {}", req.getName(), userId);
                
                // 检查角色名称是否已存在
                boolean exists = roles.stream()
                        .anyMatch(r -> r.getName().equals(req.getName()) && !r.isDelete());
                
                if (exists) {
                    logger.warn("角色名称 {} 已存在", req.getName());
                    return false;
                }
                
                // 创建新角色
                RoleMock role = new RoleMock();
                role.setId(UUID.randomUUID().toString());
                role.setName(req.getName());
                role.setDescription(req.getDescription());
                role.setOrder(req.getOrder());
                role.setCreateUserId(userId);
                role.setCreateDate(LocalDateTime.now());
                role.setIsDelete(false);
                
                roles.add(role);
                logger.info("角色添加成功，ID: {}", role.getId());
                return true;
                
            } catch (Exception e) {
                logger.error("添加角色时发生异常: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> editRole(RoleEdit req, String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始编辑角色，ID: {}, 修改用户: {}", req.getId(), userId);
                
                // 查找角色
                RoleMock role = roles.stream()
                        .filter(r -> r.getId().equals(req.getId()) && !r.isDelete())
                        .findFirst().orElse(null);
                
                if (role == null) {
                    logger.warn("未找到角色，ID: {}", req.getId());
                    return false;
                }
                
                // 检查角色名称是否已存在（排除当前角色）
                boolean nameExists = roles.stream()
                        .anyMatch(r -> r.getName().equals(req.getName()) && 
                                !r.getId().equals(req.getId()) && 
                                !r.isDelete());
                
                if (nameExists) {
                    logger.warn("角色名称 {} 已存在", req.getName());
                    return false;
                }
                
                // 更新角色信息
                role.setName(req.getName());
                role.setDescription(req.getDescription());
                role.setOrder(req.getOrder());
                role.setModifyUserId(userId);
                role.setModifyDate(LocalDateTime.now());
                
                logger.info("角色编辑成功，ID: {}", role.getId());
                return true;
                
            } catch (Exception e) {
                logger.error("编辑角色时发生异常: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteRole(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始删除角色，ID: {}", id);
                
                // 查找角色
                RoleMock role = roles.stream()
                        .filter(r -> r.getId().equals(id) && !r.isDelete())
                        .findFirst().orElse(null);
                
                if (role == null) {
                    logger.warn("未找到角色，ID: {}", id);
                    return false;
                }
                
                // 软删除角色
                role.setIsDelete(true);
                logger.info("角色删除成功，ID: {}", id);
                return true;
                
            } catch (Exception e) {
                logger.error("删除角色时发生异常: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> batchDelRole(String ids) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始批量删除角色，IDs: {}", ids);
                
                // 分割ID字符串
                List<String> idList = List.of(ids.split(","));
                int deleteCount = 0;
                
                // 批量软删除角色
                for (RoleMock role : roles) {
                    if (idList.contains(role.getId()) && !role.isDelete()) {
                        role.setIsDelete(true);
                        deleteCount++;
                    }
                }
                
                logger.info("批量删除角色成功，共删除 {} 个角色", deleteCount);
                return deleteCount > 0;
                
            } catch (Exception e) {
                logger.error("批量删除角色时发生异常: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<PageInfo<RoleRes>> getRoles(RoleReq req, String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取角色列表，分页: {}/{}", req.getPageIndex(), req.getPageSize());
                
                // 过滤未删除的角色
                List<RoleMock> activeRoles = roles.stream()
                        .filter(role -> !role.isDelete())
                        .collect(Collectors.toList());
                
                // 按名称过滤
                if (req.getName() != null && !req.getName().trim().isEmpty()) {
                    activeRoles = activeRoles.stream()
                            .filter(role -> role.getName().contains(req.getName()))
                            .collect(Collectors.toList());
                }
                
                // 按描述过滤
                if (req.getDescription() != null && !req.getDescription().trim().isEmpty()) {
                    activeRoles = activeRoles.stream()
                            .filter(role -> role.getDescription().contains(req.getDescription()))
                            .collect(Collectors.toList());
                }
                
                // 按排序字段排序
                activeRoles.sort((r1, r2) -> Integer.compare(r1.getOrder(), r2.getOrder()));
                
                // 计算分页
                int total = activeRoles.size();
                int pageIndex = Math.max(req.getPageIndex(), 1);
                int pageSize = Math.max(req.getPageSize(), 1);
                int startIndex = (pageIndex - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, total);
                
                List<RoleMock> paginatedRoles;
                if (startIndex < total) {
                    paginatedRoles = activeRoles.subList(startIndex, endIndex);
                } else {
                    paginatedRoles = new ArrayList<>();
                }
                
                // 转换为RoleRes
                List<RoleRes> roleResList = paginatedRoles.stream()
                        .map(role -> {
                            RoleRes res = new RoleRes();
                            res.setId(role.getId());
                            res.setName(role.getName());
                            res.setDescription(role.getDescription());
                            res.setOrder(role.getOrder());
                            
                            // 获取创建用户名称
                            String createUserName = users.stream()
                                    .filter(user -> user.getId().equals(role.getCreateUserId()))
                                    .map(UserMock::getName)
                                    .findFirst().orElse("未知");
                            res.setCreateUserName(createUserName);
                            
                            // 获取修改用户名称
                            String modifyUserName = "";
                            if (role.getModifyUserId() != null) {
                                modifyUserName = users.stream()
                                        .filter(user -> user.getId().equals(role.getModifyUserId()))
                                        .map(UserMock::getName)
                                        .findFirst().orElse("未知");
                            }
                            res.setModifyUserName(modifyUserName);
                            
                            res.setCreateDate(role.getCreateDate());
                            res.setModifyDate(role.getModifyDate());
                            
                            return res;
                        })
                        .collect(Collectors.toList());
                
                // 创建分页结果
                PageInfo<RoleRes> pageInfo = new PageInfo<>();
                pageInfo.setData(roleResList);
                pageInfo.setTotal(total);
                pageInfo.setPageIndex(pageIndex);
                pageInfo.setPageSize(pageSize);
                
                logger.info("获取角色列表成功，共 {} 条记录，当前第 {} 页", total, pageIndex);
                return pageInfo;
                
            } catch (Exception e) {
                logger.error("获取角色列表时发生异常: {}", e.getMessage(), e);
                return new PageInfo<>();
            }
        });
    }
    
    // 模拟角色实体类
    private static class RoleMock {
        private String id;
        private String name;
        private String description;
        private int order;
        private String createUserId;
        private LocalDateTime createDate;
        private String modifyUserId;
        private LocalDateTime modifyDate;
        private boolean isDelete;
        
        // getter和setter方法
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }
        public String getCreateUserId() { return createUserId; }
        public void setCreateUserId(String createUserId) { this.createUserId = createUserId; }
        public LocalDateTime getCreateDate() { return createDate; }
        public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }
        public String getModifyUserId() { return modifyUserId; }
        public void setModifyUserId(String modifyUserId) { this.modifyUserId = modifyUserId; }
        public LocalDateTime getModifyDate() { return modifyDate; }
        public void setModifyDate(LocalDateTime modifyDate) { this.modifyDate = modifyDate; }
        public boolean isDelete() { return isDelete; }
        public void setIsDelete(boolean isDelete) { this.isDelete = isDelete; }
    }
    
    // 模拟用户实体类
    private static class UserMock {
        private String id;
        private String name;
        
        public UserMock(String id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}