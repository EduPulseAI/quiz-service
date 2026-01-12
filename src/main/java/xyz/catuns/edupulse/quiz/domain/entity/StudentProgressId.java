package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class StudentProgressId implements Serializable {

    @Column(name = "student_id")
    private UUID studentId;

    @Column(name = "topic_id")
    private UUID topicId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudentProgressId that = (StudentProgressId) o;
        return Objects.equals(studentId, that.studentId) && Objects.equals(topicId, that.topicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, topicId);
    }
}
