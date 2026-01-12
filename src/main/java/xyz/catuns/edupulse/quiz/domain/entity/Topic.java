package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "topics",
    indexes = @Index(name = "idx_skill_topic", columnList = "skill"))
public class Topic extends BaseEntity {

    @Column(name = "skill", unique = true, nullable = false)
    private String skill;

    @OneToMany(mappedBy = "topic")
    private final List<Question> questions = new ArrayList<>();
}
