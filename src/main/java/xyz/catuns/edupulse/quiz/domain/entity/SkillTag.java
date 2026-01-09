package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "skill_tags",
    uniqueConstraints = @UniqueConstraint(
            name = "uc_skill_tag",
            columnNames = {"skill", "tag"}),
    indexes = @Index(name = "idx_skill_skill_tag", columnList = "skill"))
public class SkillTag extends BaseEntity {

    @Column(name = "skill", nullable = false)
    private String skill;

    @Column(name = "tag", nullable = false)
    private String tag;

    @OneToMany(mappedBy = "skillTag")
    private final List<Question> questions = new ArrayList<>();
}
