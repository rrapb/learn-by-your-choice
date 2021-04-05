package com.ubt.gymmanagementsystem.entities.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanProgramDTO {

    private Long id;
    private String day;
    private Long personId;
    private Long categoryId;
}