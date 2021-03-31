package com.ubt.gymmanagementsystem.entities.administration.daos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDAO {

    private Long id;
    private String name;
    private boolean viewRoles;
    private boolean addRoles;
    private boolean viewUsers;
    private boolean addUsers;
    private boolean viewPersons;
    private boolean addPersons;
}