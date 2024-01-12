package com.cn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The enum Role enum.
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum RoleEnum {

    /**
     * Admin role enum.
     */
    ADMIN("ADMIN"),
    /**
     * User role enum.
     */
    USER("USER");

    private String desc;
}
