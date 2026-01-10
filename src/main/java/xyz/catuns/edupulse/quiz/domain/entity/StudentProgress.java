package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "student_progress")
public class StudentProgress {

    @EmbeddedId
    private StudentProgressId id;

    @Column(name = "questions_attempted")
    private Integer questionsAttempted = 0;

    @Column(name = "questions_correct")
    private Integer questionsCorrect = 0;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private Instant lastUpdated;


    public void incrementCorrectAttempt() {
        this.questionsAttempted++;
        this.questionsCorrect++;
    }

    public void incrementAttempt() {
        this.questionsAttempted++;
    }
}
