package xyz.catuns.edupulse.quiz.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.catuns.edupulse.quiz.domain.entity.Question;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID>, JpaSpecificationExecutor<Question> {

    @Query(value = """
            SELECT * FROM questions
        WHERE skill_tag_id = :skillTagId 
        ORDER BY RANDOM() 
        LIMIT 1
        """, nativeQuery = true)
    Optional<Question> findRandomBySkillTagId(@Param("skillTagId") UUID skillTagId);

    @Query(value = """
            SELECT * FROM questions
            WHERE skill_tag = :skillTag
            AND difficulty_level = :difficulty
            AND question_id NOT IN :recentIds
            ORDER BY RANDOM()
            LIMIT 1
            """, nativeQuery = true)
    Optional<Question> findNextQuestion(
            @Param("skillTagId") UUID skillTagId,
            @Param("difficulty") int difficulty,
            @Param("recentIds") List<String> recentIds
    );


}
