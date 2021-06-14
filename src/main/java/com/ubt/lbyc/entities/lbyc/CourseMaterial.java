package com.ubt.lbyc.entities.lbyc;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ubt.lbyc.configurations.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="courses_materials")
public class CourseMaterial extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    @JsonManagedReference
    @OneToOne(mappedBy = "courseMaterial")
    private CourseMaterialFile file;
}
