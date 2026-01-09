package com.system.schedule.controller;

import com.system.schedule.dto.AllInfoQueryDto;
import com.system.schedule.service.IAllInfoService;
import com.system.schedule.common.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/all-info")
public class AllInfoController {

    private static final Logger logger = LoggerFactory.getLogger(AllInfoController.class);
    
    private final IAllInfoService allInfoService;

    @Autowired
    public AllInfoController(IAllInfoService allInfoService) {
        this.allInfoService = allInfoService;
    }

    @GetMapping("/list")
    public ApiResult getUserList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String majorName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String userTypes,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            // 解析userTypes参数
            List<Integer> userTypeList = null;
            if (userTypes != null && !userTypes.isEmpty()) {
                userTypeList = List.of(userTypes.split(","))
                        .stream()
                        .map(s -> {
                            try {
                                return Integer.parseInt(s.trim());
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        })
                        .filter(i -> i != null)
                        .toList();
            }

            var result = allInfoService.getUserList(name, majorName, gender, userTypeList, pageIndex, pageSize);
            return ApiResult.success()
                    .data("list", result.getList())
                    .data("total", result.getTotal())
                    .message("查询成功");
        } catch (Exception ex) {
            logger.error("获取用户列表时发生错误", ex);
            return ApiResult.error(500, "服务器内部错误");
        }
    }
}