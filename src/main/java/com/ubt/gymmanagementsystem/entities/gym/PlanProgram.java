package com.ubt.gymmanagementsystem.entities.gym;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ubt.gymmanagementsystem.configurations.audit.Auditable;
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
@Table(name="plan_programs", uniqueConstraints = {@UniqueConstraint(columnNames = {"day", "person_id", "category_id"})})
public class PlanProgram extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String day;

    @Column
    private boolean enabled;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
}
