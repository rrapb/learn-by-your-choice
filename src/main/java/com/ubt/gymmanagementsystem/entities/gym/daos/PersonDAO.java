package com.ubt.gymmanagementsystem.entities.gym.daos;

import com.ubt.gymmanagementsystem.entities.gym.PersonImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDAO {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthDateString;
    private Date birthDate;
    private char gender;
    private String personalId;
    private String enabled;
}