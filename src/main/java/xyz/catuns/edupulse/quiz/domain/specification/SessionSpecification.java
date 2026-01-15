package xyz.catuns.edupulse.quiz.domain.specification;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEventType;
import xyz.catuns.edupulse.quiz.domain.dto.session.SessionSearchCriteria;
import xyz.catuns.edupulse.quiz.domain.entity.Session;

import java.util.List;
import java.util.UUID;

public final class SessionSpecification {

    private SessionSpecification() {}

    public static Specification<Session> fromCriteria(SessionSearchCriteria criteria) {
        Specification<Session> spec = Specification.unrestricted();

        // difficulty between min and max (always present in your record)
        spec = spec.and(hasDifficultyBetween(criteria.minDifficultyLevel(), criteria.maxDifficultyLevel()));

        // filter by student id (only if not null)
        if (criteria.studentId() != null) {
            spec = spec.and(hasStudentId(criteria.studentId()));
        }

        // include by status list (IN)
        if (criteria.hasStatus() != null && !criteria.hasStatus().isEmpty()) {
            spec = spec.and(hasAnyStatusIn(criteria.hasStatus()));
        }

        // exclude by status list (NOT IN)
        if (criteria.ignoreStatus() != null && !criteria.ignoreStatus().isEmpty()) {
            spec = spec.and(doesNotHaveAnyStatusIn(criteria.ignoreStatus()));
        }

        return spec;
    }

    private static Specification<Session> hasDifficultyBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            Path<Integer> levelPath = root.get("currentDifficulty").get("levelValue");
            return cb.between(levelPath, min, max);
        };
    }


    private static Specification<Session> hasStudentId(UUID studentId) {
        return (root, query, cb) -> cb.equal(root.get("student").get("id"), studentId);
    }

    private static Specification<Session> hasAnyStatusIn(List<SessionEventType> statuses) {
        return (root, query, cb) -> root.get("status").in(statuses);
    }

    private static Specification<Session> doesNotHaveAnyStatusIn(List<SessionEventType> statuses) {
        return (root, query, cb) -> cb.not(root.get("status").in(statuses));
    }
}

