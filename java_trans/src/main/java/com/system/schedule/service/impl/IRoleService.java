package com.system.schedule.service;

import com.system.schedule.dto.role.RoleAdd;
import com.system.schedule.dto.role.RoleEdit;
import com.system.schedule.dto.role.RoleReq;
import com.system.schedule.dto.role.RoleRes;
import com.system.schedule.dto.other.PageInfo;
import java.util.concurrent.CompletableFuture;

public interface IRoleService {
    // 根据命名，增删查改
    CompletableFuture<Boolean> addRole(RoleAdd req, String userId);
    CompletableFuture<Boolean> editRole(RoleEdit req, String userId);
    CompletableFuture<Boolean> deleteRole(String id);

    // 批量删除  
    CompletableFuture<Boolean> batchDelRole(String ids);

    // 获取菜单列表  
    CompletableFuture<PageInfo<RoleRes>> getRoles(RoleReq req, String userId);
}