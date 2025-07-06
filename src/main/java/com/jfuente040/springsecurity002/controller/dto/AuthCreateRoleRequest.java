package com.jfuente040.springsecurity002.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateRoleRequest(@NotBlank String roleListName) {
}
