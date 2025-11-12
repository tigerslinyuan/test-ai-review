package com.example.controller;

import com.example.constants.AppConstants;
import com.example.dto.*;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.UserConverter;
import com.example.validation.ValidUserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(
            @PathVariable @ValidUserId String id) {

        logger.info("Getting user with id: {}", id);

        User user = userService.getUserById(id);
        UserDTO userDTO = UserConverter.toDTO(user);

        return ResponseEntity.ok(ApiResponse.success(userDTO));
    }

    /**
     * 创建用户
     *
     * @param userDTO 用户信息
     * @return 创建的用户信息
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        logger.info("Creating user with name: {}, email: {}", userDTO.getName(), userDTO.getEmail());

        User user = userService.createUser(userDTO.getName(), userDTO.getEmail());
        UserDTO createdDTO = UserConverter.toDTO(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("用户创建成功", createdDTO));
    }

    /**
     * 获取所有用户（分页）
     *
     * @param page 页码（从1开始，可选，默认为1）
     * @param size 每页大小（可选，默认为10，最大100）
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getAllUsers(
            @RequestParam(required = false, defaultValue = "1")
            @Min(value = 1, message = "页码必须大于0")
            Integer page,

            @RequestParam(required = false, defaultValue = "10")
            @Min(value = 1, message = "每页大小必须大于0")
            Integer size) {

        logger.info("Getting all users with page: {}, size: {}", page, size);

        // 验证并调整分页参数
        int validPage = Math.max(1, page);
        int validSize = Math.min(Math.max(1, size), AppConstants.MAX_PAGE_SIZE);

        List<User> users = userService.getAllUsers(validPage, validSize);
        long total = userService.getUserCount();

        List<UserDTO> userDTOs = users.stream()
                .map(UserConverter::toDTO)
                .collect(Collectors.toList());

        PageResponse<UserDTO> pageResponse = new PageResponse<>(userDTOs, validPage, validSize, total);

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 更新用户信息
     *
     * @param id      用户ID
     * @param userDTO 更新的用户信息
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable @ValidUserId String id,
            @Valid @RequestBody UserDTO userDTO) {

        logger.info("Updating user with id: {}", id);

        User updatedUser = userService.updateUser(id, userDTO.getName(), userDTO.getEmail());
        UserDTO updatedDTO = UserConverter.toDTO(updatedUser);

        return ResponseEntity.ok(ApiResponse.success("用户更新成功", updatedDTO));
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(
            @PathVariable @ValidUserId String id) {

        logger.info("Deleting user with id: {}", id);

        userService.deleteUser(id);

        return ResponseEntity.ok(ApiResponse.success("用户删除成功", null));
    }

    /**
     * 获取用户统计信息
     *
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Object>> getUserStatistics() {
        logger.info("Getting user statistics");

        return ResponseEntity.ok(ApiResponse.success(userService.getUserStatistics()));
    }
}
