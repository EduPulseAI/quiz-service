package xyz.catuns.edupulse.quiz.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.catuns.edupulse.quiz.domain.entity.StudentProgress;
import xyz.catuns.edupulse.quiz.domain.entity.StudentProgressId;

public interface StudentProgressRepository extends JpaRepository<StudentProgress, StudentProgressId> {
}
