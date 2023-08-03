package me.chirin.zeitgeist.events;

public class ScoreboardChangedEvent {
    private static final ScoreboardChangedEvent INSTANCE = new ScoreboardChangedEvent();

    public static ScoreboardChangedEvent get() {
        return INSTANCE;
    }
}
