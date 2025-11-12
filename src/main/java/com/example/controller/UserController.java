package com.example.controller;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    // 硬编码的值
    @GetMapping("/user/{id}")
    public Map<String, Object> getUser(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        
        // 没有输入验证
        // 魔法数字
        if (id.length() > 100) {
            result.put("error", "id too long");
            return result;
        }
        
        // 直接使用字符串拼接，没有参数化
        String userInfo = userService.getUserInfo(id);
        
        // 命名不规范
        String a = userInfo;
        String b = "success";
        String c = "200";
        
        result.put("data", a);
        result.put("status", b);
        result.put("code", c);
        
        return result;
    }

    @PostMapping("/user")
    public Map<String, Object> createUser(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        
        // 没有输入验证
        String name = request.get("name");
        String email = request.get("email");
        
        // 硬编码
        if (name == null || name.equals("")) {
            result.put("error", "name is required");
            result.put("code", "400");
            return result;
        }
        
        // 重复的代码逻辑
        if (email == null || email.equals("")) {
            result.put("error", "email is required");
            result.put("code", "400");
            return result;
        }
        
        // 没有异常处理
        String userId = userService.createUser(name, email);
        
        result.put("userId", userId);
        result.put("message", "created");
        result.put("code", "200");
        
        return result;
    }

    @GetMapping("/users")
    public Map<String, Object> getAllUsers(@RequestParam(required = false) String page, 
                                           @RequestParam(required = false) String size) {
        Map<String, Object> result = new HashMap<>();
        
        // 魔法数字，没有默认值处理
        int pageNum = 1;
        int pageSize = 10;
        
        // 没有异常处理，如果page不是数字会崩溃
        if (page != null) {
            pageNum = Integer.parseInt(page);
        }
        if (size != null) {
            pageSize = Integer.parseInt(size);
        }
        
        // 没有边界检查
        // 硬编码的分页逻辑
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        
        result.put("data", userService.getAllUsers(pageNum, pageSize));
        result.put("page", pageNum);
        result.put("size", pageSize);
        
        return result;
    }

    @PutMapping("/user/{id}")
    public Map<String, Object> updateUser(@PathVariable String id, @RequestBody Map<String, String> request) {
        // 没有输入验证
        // 没有检查用户是否存在
        String name = request.get("name");
        String email = request.get("email");
        
        // 重复的验证逻辑
        if (name == null || name.equals("")) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "name is required");
            error.put("code", "400");
            return error;
        }
        
        if (email == null || email.equals("")) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "email is required");
            error.put("code", "400");
            return error;
        }
        
        // 没有异常处理
        userService.updateUser(id, name, email);
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "updated");
        result.put("code", "200");
        
        return result;
    }

    @DeleteMapping("/user/{id}")
    public Map<String, Object> deleteUser(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        
        // 没有检查用户是否存在
        // 没有异常处理
        userService.deleteUser(id);
        
        result.put("message", "deleted");
        result.put("code", "200");
        
        return result;
    }
}

