package xyz.catuns.edupulse.quiz.messaging.producer;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import xyz.catuns.edupulse.common.messaging.events.quiz.QuizAnswer;
import xyz.catuns.edupulse.common.messaging.events.quiz.QuizAnswerKey;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEvent;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEventKey;

@Component
@RequiredArgsConstructor
public class QuizEventProducer {

    private final KafkaTemplate<SpecificRecord, SpecificRecord> template;

    @Value("${app.kafka.topics.session}")
    private String sessionTopicName;

    @Value("${app.kafka.topics.answer}")
    private String answerTopicName;

    public void publishSessionEvent(SessionEvent event) {
        SessionEventKey key = SessionEventKey.newBuilder()
                .setSessionId(event.getEnvelope().getSessionId())
                .build();
        template.send(sessionTopicName, key, event);
    }

    public void publishQuizAnswerEvent(QuizAnswer quizAnswer) {
        QuizAnswerKey key = QuizAnswerKey.newBuilder()
                .setQuestionId(quizAnswer.getQuestionId())
                .setStudentId(quizAnswer.getEnvelope().getStudentId())
                .build();
        template.send(answerTopicName, key, quizAnswer);
    }
}
