package com.system.schedule.mapper;

import com.system.schedule.dto.user.UserRes;
import com.system.schedule.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@SuppressWarnings({"all"})
public interface UserMapper {

    Users findUserById(@Param("userId") String name);

    List<UserRes> findAllUsers();

    Users findByNameAndEmail(@Param("userId") String name, @Param("email") String email);

    int updatePassword(@Param("userId") String userId, @Param("password") String password);

    int updateVerification(@Param("userId") String userId, @Param("verification") String verification);
}