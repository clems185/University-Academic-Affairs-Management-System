// MajorInfoController.java
package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.service.IMajorInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/major")
@Api(tags = "专业信息控制器")
public class MajorInfoController {
    
    @Autowired
    private IMajorInfoService majorService;
    
    @GetMapping("/list")
    @ApiOperation("获取专业列表")
    public ApiResult<?> getMajorList() {
        logger.info("开始获取专业列表");
        try {
            Object list = majorService.getMajorList();
            logger.info("成功获取专业列表，数量：{}", list != null ? ((java.util.List<?>)list).size() : 0);
            return ApiResult.success(list);
        } catch (Exception ex) {
            logger.error("获取专业列表时发生异常", ex);
            return ApiResult.error("服务器内部错误");
        }
    }
}