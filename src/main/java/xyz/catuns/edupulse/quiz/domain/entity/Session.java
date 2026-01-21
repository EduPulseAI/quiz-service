package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEventType;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "sessions",
    indexes = @Index(name = "idx_student_session", columnList = "student_id"))
public class Session extends BaseEntity {

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "instructor_id")
    private UUID instructorId;

    @ManyToOne(cascade = {PERSIST, DETACH, REFRESH, MERGE})
    @JoinColumn(name = "current_question_id")
    private Question currentQuestion;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_difficulty")
    private DifficultyLevel currentDifficulty = DifficultyLevel.INTERMEDIATE;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SessionEventType status;

}
