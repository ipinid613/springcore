package com.sparta.springcore.model;

// entity 설계에서 user와 admin의 역할은 Enum타입으로 받겠다고 정해서 enum 사용
public enum UserRole {
    USER,  // 사용자 권한
    ADMIN  // 관리자 권한
}