package xyz.catuns.edupulse.quiz.service;

import xyz.catuns.edupulse.quiz.domain.dto.session.*;

import java.util.List;
import java.util.UUID;

public interface SessionService {

    StartSessionResponse startSession(StartSessionRequest request);

    List<SessionResponse> getAllSessions(SessionSearchCriteria criteria);

    SessionResponse getSession(UUID sessionId);

    SendSessionEventResponse sendSessionEvent(SendSessionEventRequest request);
}
