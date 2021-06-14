package com.ubt.lbyc.entities.lbyc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    private Long id;
    private String name;
    private String description;
    private String enabled;
}