package com.ubt.lbyc.entities.lbyc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseMaterialDTO {

    private Long id;
    private String name;
    private String fileId;
    private Long courseId;
}