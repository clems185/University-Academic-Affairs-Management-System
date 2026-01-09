package com.system.schedule.service;

import com.system.schedule.dto.allinfo.AllInfoRes;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IAllInfoService {
    CompletableFuture<Pair<List<AllInfoRes>, Integer>> getUserList(String name, String majorName, String gender,
            List<Integer> userTypes, int pageIndex, int pageSize);
    
    // 可选：提供默认参数的重载方法
    default CompletableFuture<Pair<List<AllInfoRes>, Integer>> getUserList(String name, String majorName, String gender, List<Integer> userTypes) {
        return getUserList(name, majorName, gender, userTypes, 1, 10);
    }
    
    default CompletableFuture<Pair<List<AllInfoRes>, Integer>> getUserList(String name, String majorName, String gender) {
        return getUserList(name, majorName, gender, null, 1, 10);
    }
    
    default CompletableFuture<Pair<List<AllInfoRes>, Integer>> getUserList() {
        return getUserList(null, null, null, null, 1, 10);
    }
    
    // 简单的Pair辅助类
    class Pair<T1, T2> {
        private T1 first;
        private T2 second;
        
        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }
        
        public T1 getFirst() { return first; }
        public T2 getSecond() { return second; }
    }
}