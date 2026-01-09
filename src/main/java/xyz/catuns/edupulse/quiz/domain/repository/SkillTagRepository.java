package xyz.catuns.edupulse.quiz.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.catuns.edupulse.quiz.domain.entity.SkillTag;

import java.util.Optional;
import java.util.UUID;

public interface SkillTagRepository extends JpaRepository<SkillTag, UUID> {
    Optional<SkillTag> findBySkillAndTag(String skill, String tag);
}
