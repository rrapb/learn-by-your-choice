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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="persons")
public class Person extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private char gender;

    @Column
    private Date birthDate;

    @Column(unique = true, name = "personal_id")
    private String personalId;

    @Column
    private boolean enabled;

    @JsonManagedReference
    @OneToOne(mappedBy = "person")
    private User user;
}
