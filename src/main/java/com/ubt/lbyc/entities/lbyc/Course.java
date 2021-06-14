package com.ubt.lbyc.entities.lbyc;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ubt.lbyc.configurations.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="courses")
public class Course extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private boolean enabled;

    @JsonManagedReference
    @OneToMany(mappedBy = "course")
    private List<CourseMaterial> courseMaterials;
}
