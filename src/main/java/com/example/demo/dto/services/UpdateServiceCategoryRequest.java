package com.example.demo.dto.services;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UpdateServiceCategoryRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must not exceed 100 characters")
    private String name;

    private boolean enabled;

    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//
//public class UpdateServiceCategoryRequest {
//
//    @NotBlank(message = "name is required")
//    @Size(max = 100, message = "name must not exceed 100 characters")
//    private String name;
//
//    private boolean enabled;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//}
