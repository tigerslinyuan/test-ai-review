package com.example.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    // 使用静态变量存储数据，没有线程安全
    private static Map<String, Map<String, String>> users = new HashMap<>();
    private static int counter = 1;

    public String getUserInfo(String id) {
        // 没有空值检查
        Map<String, String> user = users.get(id);
        
        // 直接返回，没有检查是否存在
        return user.get("name") + "," + user.get("email");
    }

    public String createUser(String name, String email) {
        // 硬编码的ID生成逻辑
        String userId = "user_" + counter;
        counter++;
        
        // 没有检查email格式
        // 没有检查name长度
        
        Map<String, String> user = new HashMap<>();
        user.put("id", userId);
        user.put("name", name);
        user.put("email", email);
        user.put("createdAt", String.valueOf(System.currentTimeMillis()));
        
        users.put(userId, user);
        
        return userId;
    }

    public List<Map<String, String>> getAllUsers(int page, int size) {
        List<Map<String, String>> allUsers = new ArrayList<>();
        
        // 低效的遍历方式
        for (String key : users.keySet()) {
            allUsers.add(users.get(key));
        }
        
        // 没有排序
        // 简单的分页实现，没有考虑性能
        int start = (page - 1) * size;
        int end = start + size;
        
        // 没有边界检查
        if (start > allUsers.size()) {
            return new ArrayList<>();
        }
        
        if (end > allUsers.size()) {
            end = allUsers.size();
        }
        
        // 创建新列表，浪费内存
        List<Map<String, String>> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(allUsers.get(i));
        }
        
        return result;
    }

    public void updateUser(String id, String name, String email) {
        // 没有检查用户是否存在
        Map<String, String> user = users.get(id);
        
        // 直接修改，没有验证
        user.put("name", name);
        user.put("email", email);
        user.put("updatedAt", String.valueOf(System.currentTimeMillis()));
        
        users.put(id, user);
    }

    public void deleteUser(String id) {
        // 没有检查用户是否存在
        // 没有返回值，调用者不知道是否成功
        users.remove(id);
    }

    // 过长的函数，包含多个职责
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 重复遍历
        int total = users.size();
        stats.put("total", total);
        
        // 不必要的复杂逻辑
        int count = 0;
        for (String key : users.keySet()) {
            Map<String, String> user = users.get(key);
            String email = user.get("email");
            if (email != null && email.contains("@")) {
                count++;
            }
        }
        stats.put("validEmails", count);
        
        // 硬编码的统计逻辑
        long now = System.currentTimeMillis();
        int recentCount = 0;
        for (String key : users.keySet()) {
            Map<String, String> user = users.get(key);
            String createdAt = user.get("createdAt");
            if (createdAt != null) {
                long created = Long.parseLong(createdAt);
                // 魔法数字：7天的毫秒数
                if (now - created < 604800000) {
                    recentCount++;
                }
            }
        }
        stats.put("recentUsers", recentCount);
        
        return stats;
    }

    // 未使用的变量
    private String unusedField = "test";
    
    // 注释掉的代码
    // public void oldMethod() {
    //     System.out.println("old code");
    // }
}

