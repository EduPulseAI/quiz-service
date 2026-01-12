package xyz.catuns.edupulse.quiz.service;

import xyz.catuns.edupulse.quiz.domain.dto.session.SessionResponse;
import xyz.catuns.edupulse.quiz.domain.dto.session.SessionSearchCriteria;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionRequest;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionResponse;

import java.util.List;
import java.util.UUID;

public interface SessionService {

    StartSessionResponse startSession(StartSessionRequest request);

    List<SessionResponse> getAllSessions(SessionSearchCriteria criteria);

    SessionResponse getSession(UUID sessionId);
}
