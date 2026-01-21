package xyz.catuns.edupulse.quiz.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.catuns.edupulse.quiz.domain.entity.Session;

import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID>, JpaSpecificationExecutor<Session> {
    @Query("SELECT s FROM Session s " +
            "WHERE s.studentId = :studentId " +
            "   AND s.currentQuestion.topic.id = :topicId " +
            "   AND s.status != 'COMPLETED' " +
            "ORDER BY s.updatedAt DESC ")
    List<Session> findActiveByStudentIdAndTopicId(
            @Param("studentId") UUID studentId,
            @Param("topicId") UUID topicId);
}
