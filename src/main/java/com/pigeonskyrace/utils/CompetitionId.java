package com.pigeonskyrace.utils;

import org.bson.types.ObjectId;

public class CompetitionId {

    private final String id;

    public CompetitionId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Competition ID cannot be null or empty");
        }
        this.id = id;
    }

    public static CompetitionId fromString(String id) {
        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid Competition ID format: " + id);
        }
        return new CompetitionId(id);
    }
    public static CompetitionId toCompetitionId(String id) {
        return new CompetitionId(id);
    }

    public String getValue() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
