package com.pigeonskyrace.event;

import com.pigeonskyrace.dto.response.ResultatResponseDTO;

import java.util.List;

/**
 * Published after {@link com.pigeonskyrace.service.ResultatService#calculatePoint} completes so realtime
 * clients can receive the fresh leaderboard.
 */
public record CompetitionUpdatedEvent(String competitionId, List<ResultatResponseDTO> leaderboard) {}
