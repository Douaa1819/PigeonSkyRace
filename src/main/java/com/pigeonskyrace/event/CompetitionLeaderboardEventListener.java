package com.pigeonskyrace.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompetitionLeaderboardEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void onCompetitionUpdated(CompetitionUpdatedEvent event) {
        messagingTemplate.convertAndSend(
                "/topic/competitions/" + event.competitionId() + "/leaderboard",
                event.leaderboard());
    }
}
