package com.kamilglazer.coursesusers.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="role")
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="username",referencedColumnName = "username")
    private UserEntity userEntity;

    public UserRole(UserEntity userEntity,String role) {
        this.userEntity = userEntity;
        this.role=role;
    }
}
