package com.example.util;

import com.example.dto.UserDTO;
import com.example.model.User;

/**
 * 用户对象转换工具类
 */
public final class UserConverter {

    private UserConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 将User实体转换为UserDTO
     */
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    /**
     * 将UserDTO转换为User实体
     */
    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setCreatedAt(dto.getCreatedAt());
        user.setUpdatedAt(dto.getUpdatedAt());
        return user;
    }
}

