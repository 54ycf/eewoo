package com.eewoo.platform.pojo.vo.request;

import lombok.Data;

/**
 * 管理员禁用用户的参数
 */
@Data
public class DisableUserRequest {
    Integer id;
    String role;
}
