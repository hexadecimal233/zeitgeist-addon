package me.onlyrain.randomaddon.events;

public class ScoreboardEvent {
    private static final ScoreboardEvent INSTANCE = new ScoreboardEvent();

    public static ScoreboardEvent get() {
        return INSTANCE;
    }
}
