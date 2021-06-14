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
public class PersonDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthDateString;
    private Date birthDate;
    private char gender;
    private String personalId;
    private String enabled;
}