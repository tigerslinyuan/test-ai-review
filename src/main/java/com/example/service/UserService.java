package com.example.service;

import com.example.constants.AppConstants;
import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务类
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // 使用线程安全的ConcurrentHashMap和AtomicLong
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户对象
     * @throws ResourceNotFoundException 如果用户不存在
     */
    public User getUserById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        User user = users.get(id);
        if (user == null) {
            logger.warn("User not found with id: {}", id);
            throw new ResourceNotFoundException("用户不存在");
        }

        logger.debug("Retrieved user with id: {}", id);
        return user;
    }

    /**
     * 创建新用户
     *
     * @param name  用户名
     * @param email 邮箱
     * @return 创建的用户对象
     */
    public User createUser(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }

        String userId = AppConstants.USER_ID_PREFIX + counter.getAndIncrement();
        long currentTime = System.currentTimeMillis();

        User user = new User(userId, name.trim(), email.trim().toLowerCase(), currentTime, null);
        users.put(userId, user);

        logger.info("Created user with id: {}, name: {}, email: {}", userId, name, email);
        return user;
    }

    /**
     * 获取所有用户（分页）
     *
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 用户列表
     */
    public List<User> getAllUsers(int page, int size) {
        List<User> allUsers = new ArrayList<>(users.values());

        // 按创建时间倒序排序
        allUsers.sort((u1, u2) -> {
            Long time1 = u1.getCreatedAt() != null ? u1.getCreatedAt() : 0L;
            Long time2 = u2.getCreatedAt() != null ? u2.getCreatedAt() : 0L;
            return time2.compareTo(time1);
        });

        // 计算分页
        int start = (page - 1) * size;
        if (start >= allUsers.size()) {
            return Collections.emptyList();
        }

        int end = Math.min(start + size, allUsers.size());
        return allUsers.subList(start, end);
    }

    /**
     * 获取用户总数
     *
     * @return 用户总数
     */
    public long getUserCount() {
        return users.size();
    }

    /**
     * 更新用户信息
     *
     * @param id    用户ID
     * @param name  新用户名
     * @param email 新邮箱
     * @return 更新后的用户对象
     * @throws ResourceNotFoundException 如果用户不存在
     */
    public User updateUser(String id, String name, String email) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }

        User user = getUserById(id);
        user.setName(name.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setUpdatedAt(System.currentTimeMillis());

        users.put(id, user);
        logger.info("Updated user with id: {}", id);
        return user;
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @throws ResourceNotFoundException 如果用户不存在
     */
    public void deleteUser(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        User user = users.remove(id);
        if (user == null) {
            logger.warn("Attempted to delete non-existent user with id: {}", id);
            throw new ResourceNotFoundException("用户不存在");
        }

        logger.info("Deleted user with id: {}", id);
    }

    /**
     * 检查用户是否存在
     *
     * @param id 用户ID
     * @return 如果用户存在返回true，否则返回false
     */
    public boolean userExists(String id) {
        return id != null && users.containsKey(id);
    }

    /**
     * 获取用户统计信息
     *
     * @return 统计信息Map
     */
    public Map<String, Object> getUserStatistics() {
        long total = users.size();
        long validEmails = countValidEmails();
        long recentUsers = countRecentUsers();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("validEmails", validEmails);
        stats.put("recentUsers", recentUsers);

        logger.debug("User statistics: total={}, validEmails={}, recentUsers={}", total, validEmails, recentUsers);
        return stats;
    }

    /**
     * 统计有效邮箱数量
     */
    private long countValidEmails() {
        return users.values().stream()
                .filter(user -> user.getEmail() != null && user.getEmail().contains("@"))
                .count();
    }

    /**
     * 统计最近7天创建的用户数量
     */
    private long countRecentUsers() {
        long now = System.currentTimeMillis();
        long timeRange = AppConstants.RECENT_USER_TIME_RANGE_MS;

        return users.values().stream()
                .filter(user -> {
                    Long createdAt = user.getCreatedAt();
                    return createdAt != null && (now - createdAt) < timeRange;
                })
                .count();
    }
}
