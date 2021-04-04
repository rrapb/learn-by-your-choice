package com.ubt.gymmanagementsystem.entities.administration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import com.ubt.gymmanagementsystem.configurations.audit.Auditable;
import com.ubt.gymmanagementsystem.entities.gym.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="users")
public class User extends Auditable<String> implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private boolean enabled;

    @Column
    private boolean accountNonExpired;

    @Column
    private boolean accountNonLocked;

    @Column
    private boolean credentialsNonExpired;

    @Column
    private boolean tokenExpired;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "id", unique = true)
    private Person person;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return getGrantedAuthorities(getPrivileges(getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    private List<String> getPrivileges(Role role) {

        List<String> privileges = new ArrayList<>();
        List<Permission> collection = new ArrayList<>(role.getPermissions());
        for (Permission item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }
}

