package com.system.schedule.mapper;

import com.system.schedule.entity.Teaches;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachesMapper  {
    List<Teaches> findByTeacherId(String teacherId);
}