package xyz.catuns.edupulse.quiz.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class StudentProgressId implements Serializable {

    @Column(name = "student_id")
    private UUID studentId;

    @Column(name = "skill_tag_id")
    private UUID skillTagId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudentProgressId that = (StudentProgressId) o;
        return Objects.equals(studentId, that.studentId) && Objects.equals(skillTagId, that.skillTagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, skillTagId);
    }
}
