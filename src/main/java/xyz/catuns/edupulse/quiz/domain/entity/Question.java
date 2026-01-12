package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "questions",
        indexes = {
                @Index(name = "idx_topic_difficulty_question", columnList = "difficulty_level, topic_id")
        })
public class Question extends BaseEntity {

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = ALL, orphanRemoval = true)
    private final Set<AnswerChoice> answerChoices = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private AnswerChoice correctAnswer;

    @ManyToOne(cascade = {PERSIST, DETACH, REFRESH, MERGE})
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "tag")
    private String tag;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "created_by")
    private String createdBy;

    public void setAnswerChoices(Set<AnswerChoice> choices) {
        this.answerChoices.clear();
        choices.forEach(this::addAnswerChoice);
    }

    public void addAnswerChoice(AnswerChoice answerChoice) {
        answerChoice.setQuestion(this);
        this.answerChoices.add(answerChoice);
    }

    public void setCorrectAnswer(AnswerChoice answerChoice) {
        if (!this.answerChoices.contains(answerChoice)) {
            this.addAnswerChoice(answerChoice);
        }

        this.correctAnswer = answerChoice;
    }

}
