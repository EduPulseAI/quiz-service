package xyz.catuns.edupulse.quiz.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.catuns.edupulse.quiz.domain.entity.Session;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
}
