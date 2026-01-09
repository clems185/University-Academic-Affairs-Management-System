package com.system.schedule.service;

import com.system.schedule.dto.menu.MenuAdd;
import com.system.schedule.dto.menu.MenuEdit;
import com.system.schedule.dto.menu.MenuReq;
import com.system.schedule.dto.menu.MenuRes;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IMenuService {
    // 添加菜单
    CompletableFuture<Boolean> addMenu(MenuAdd req, String userId);
    
    CompletableFuture<Boolean> editMenu(MenuEdit req, String userId);
    
    CompletableFuture<Boolean> deleteMenu(String id);

    // 批量删除
    CompletableFuture<Boolean> batchDelMenu(String ids);

    // 获取菜单列表
    CompletableFuture<List<MenuRes>> getMenus(MenuReq req, String userId);

    CompletableFuture<Boolean> settingMenu(String rid, String mids);
}