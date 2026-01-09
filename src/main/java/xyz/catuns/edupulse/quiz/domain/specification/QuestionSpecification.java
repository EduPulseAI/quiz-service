package xyz.catuns.edupulse.quiz.domain.specification;

import org.springframework.data.jpa.domain.Specification;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.SkillTag;

public class QuestionSpecification {
    public static Specification<Question> hasSkillTag(SkillTag skillTag) {
        return ((root, query, cb) -> {

            return cb.equal(root.get("skillTag"), skillTag);
        });
    }
}
