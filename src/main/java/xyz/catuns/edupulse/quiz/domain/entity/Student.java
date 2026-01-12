package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import xyz.catuns.spring.jwt.domain.entity.RoleEntity;
import xyz.catuns.spring.jwt.domain.entity.UserEntity;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student extends UserEntity {

    @Column(name = "name")
    private String name;

    @Override
    public Collection<? extends RoleEntity> getRoles() {
        return List.of();
    }
}
