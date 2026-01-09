package com.system.schedule.service;

import com.system.schedule.dto.allinfo.AllInfoRes;
import com.system.schedule.service.IAllInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AllInfoServiceImpl implements IAllInfoService {

    // 模拟数据
    private List<AllInfoRes> mockUsers() {
        List<AllInfoRes> users = new ArrayList<>();
        
        AllInfoRes user1 = new AllInfoRes();
        user1.setId("1");
        user1.setName("张三");
        user1.setGender("男");
        user1.setMajorId("001");
        user1.setMajorName("计算机科学与技术");
        user1.setEmail("zhangsan@example.com");
        user1.setPhone("13800138001");
        user1.setUserType(1);
        user1.setTypeDetail("本科生");
        users.add(user1);
        
        AllInfoRes user2 = new AllInfoRes();
        user2.setId("2");
        user2.setName("李四");
        user2.setGender("女");
        user2.setMajorId("002");
        user2.setMajorName("软件工程");
        user2.setEmail("lisi@example.com");
        user2.setPhone("13800138002");
        user2.setUserType(1);
        user2.setTypeDetail("本科生");
        users.add(user2);
        
        AllInfoRes user3 = new AllInfoRes();
        user3.setId("3");
        user3.setName("王五");
        user3.setGender("男");
        user3.setMajorId("003");
        user3.setMajorName("数据科学与大数据技术");
        user3.setEmail("wangwu@example.com");
        user3.setPhone("13800138003");
        user3.setUserType(2);
        user3.setTypeDetail("讲师");
        users.add(user3);
        
        AllInfoRes user4 = new AllInfoRes();
        user4.setId("4");
        user4.setName("赵六");
        user4.setGender("男");
        user4.setMajorId("001");
        user4.setMajorName("计算机科学与技术");
        user4.setEmail("zhaoliu@example.com");
        user4.setPhone("13800138004");
        user4.setUserType(3);
        user4.setTypeDetail("系统管理员");
        users.add(user4);
        
        AllInfoRes user5 = new AllInfoRes();
        user5.setId("5");
        user5.setName("孙七");
        user5.setGender("女");
        user5.setMajorId("002");
        user5.setMajorName("软件工程");
        user5.setEmail("sunqi@example.com");
        user5.setPhone("13800138005");
        user5.setUserType(2);
        user5.setTypeDetail("副教授");
        users.add(user5);
        
        return users;
    }
    
    @Override
    public CompletableFuture<Pair<List<AllInfoRes>, Integer>> getUserList(String name, String majorName, String gender, List<Integer> userTypes, int pageIndex, int pageSize) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<AllInfoRes> allUsers = mockUsers();
                
                // 过滤逻辑
                List<AllInfoRes> filteredUsers = allUsers.stream()
                        .filter(user -> (name == null || user.getName().contains(name)))
                        .filter(user -> (majorName == null || user.getMajorName().contains(majorName)))
                        .filter(user -> (gender == null || user.getGender().equals(gender)))
                        .filter(user -> (userTypes == null || userTypes.contains(user.getUserType())))
                        .collect(Collectors.toList());
                
                // 分页逻辑
                int totalCount = filteredUsers.size();
                int startIndex = (pageIndex - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, totalCount);
                
                List<AllInfoRes> paginatedUsers;
                if (startIndex >= endIndex) {
                    paginatedUsers = new ArrayList<>();
                } else {
                    paginatedUsers = filteredUsers.subList(startIndex, endIndex);
                }
                
                log.info("获取用户列表成功，共 {} 条数据，当前页 {} 条", totalCount, paginatedUsers.size());
                return new Pair<>(paginatedUsers, totalCount);
                
            } catch (Exception e) {
                log.error("获取用户列表失败: {}", e.getMessage(), e);
                return new Pair<>(new ArrayList<>(), 0);
            }
        });
    }
}