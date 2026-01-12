package xyz.catuns.edupulse.quiz.domain.specification;

import org.springframework.data.jpa.domain.Specification;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.Topic;

public class QuestionSpecification {
    public static Specification<Question> hasTopic(Topic topic) {
        return ((root, query, cb) -> {

            return cb.equal(root.get("topic"), topic);
        });
    }
}
