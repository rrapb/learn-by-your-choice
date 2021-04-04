package com.ubt.gymmanagementsystem.entities.administration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private Long id;
    private String name;
    private boolean viewRoles;
    private boolean addRoles;
    private boolean viewUsers;
    private boolean addUsers;
    private boolean viewPersons;
    private boolean addPersons;
    private boolean viewCategories;
    private boolean addCategories;
    private boolean viewTools;
    private boolean addTools;
    private boolean viewPlanPrograms;
    private boolean addPlanPrograms;
}