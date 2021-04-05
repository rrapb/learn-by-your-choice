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
@Table(name="tools")
public class Tool extends Auditable<String> {

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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @JsonManagedReference
    @OneToMany(mappedBy = "tool")
    private List<ToolDetail> toolDetails;
}
