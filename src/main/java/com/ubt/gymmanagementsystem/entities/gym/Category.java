package com.ubt.gymmanagementsystem.entities.gym;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ubt.gymmanagementsystem.configurations.audit.Auditable;
import com.ubt.gymmanagementsystem.entities.administration.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="categories")
public class Category extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String description;

    @Column
    private boolean enabled;

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private List<Tool> tools;

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private List<PlanProgram> planPrograms;
}
