package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "answer_choices",
        uniqueConstraints = @UniqueConstraint(
                name = "uc_value_question",
                columnNames = {"value", "question_id"}))
public class AnswerChoice extends BaseEntity {

    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    private String value;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public boolean isCorrect() {
        return this.question.getCorrectAnswer().equals(this);
    }
}
