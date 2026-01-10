package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "skill_tags",
    indexes = @Index(name = "idx_skill_skill_tag", columnList = "skill"))
public class SkillTag extends BaseEntity {

    @Column(name = "skill", unique = true, nullable = false)
    private String skill;

    @ElementCollection(fetch = FetchType.EAGER)
    private final Set<String> tags = new HashSet<>();

    @OneToMany(mappedBy = "skillTag")
    private final List<Question> questions = new ArrayList<>();
}
