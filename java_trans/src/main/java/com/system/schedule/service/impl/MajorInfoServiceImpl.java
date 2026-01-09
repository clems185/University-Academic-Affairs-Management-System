package com.system.schedule.service.impl;

import com.system.schedule.dto.majorinfo.MajorInfoRes;
import com.system.schedule.service.IMajorInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MajorInfoServiceImpl implements IMajorInfoService {

    private static final Logger logger = LoggerFactory.getLogger(MajorInfoServiceImpl.class);

    @Override
    public CompletableFuture<List<MajorInfoRes>> getMajorList() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取专业信息列表");
                
                // 模拟数据
                List<MajorInfoRes> majors = mockMajors();
                
                logger.info("获取专业信息列表成功，共 {} 个专业", majors.size());
                return majors;
                
            } catch (Exception e) {
                logger.error("获取专业信息列表失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }
    
    // 模拟数据
    private List<MajorInfoRes> mockMajors() {
        List<MajorInfoRes> majors = new ArrayList<>();
        
        String[] majorIds = {"080901", "080902", "080903", "080701", "080702", "080601"};
        String[] majorNames = {"计算机科学与技术", "软件工程", "网络工程", "电子信息工程", "通信工程", "自动化"};
        String[] departmentIds = {"CS", "CS", "CS", "EE", "EE", "EE"};
        String[] departmentNames = {"计算机学院", "计算机学院", "计算机学院", "电子工程学院", "电子工程学院", "电子工程学院"};
        int[] totalCredits = {140, 142, 140, 145, 145, 148};
        
        for (int i = 0; i < majorIds.length; i++) {
            MajorInfoRes major = new MajorInfoRes();
            major.setMajorId(majorIds[i]);
            major.setMajorName(majorNames[i]);
            major.setDepartmentId(departmentIds[i]);
            major.setDepartmentName(departmentNames[i]);
            major.setTotalCredits(totalCredits[i]);
            majors.add(major);
        }
        
        return majors;
    }
}