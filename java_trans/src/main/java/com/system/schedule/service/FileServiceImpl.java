package com.system.schedule.service;

import com.system.schedule.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileServiceImpl implements IFileService {
    // 目前IFileService接口是空的，这个实现类作为占位符
    // 当接口添加方法时，可以在这里实现相应的功能
    
    public FileServiceImpl() {
        log.info("FileService初始化完成");
    }
}