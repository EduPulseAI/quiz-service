package xyz.catuns.edupulse.quiz.service;

import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionRequest;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionResponse;

public interface SessionService {

    StartSessionResponse startSession(StartSessionRequest request);
}
