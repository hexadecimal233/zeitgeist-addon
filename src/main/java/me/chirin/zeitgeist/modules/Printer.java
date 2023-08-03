package me.chirin.zeitgeist.modules;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import org.apache.commons.lang3.RandomStringUtils;

public class Printer extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("how to like do it")
        .defaultValue(Modes.Tellraw)
        .build());

    private final Setting<TitleModes> tmode = sgGeneral.add(new EnumSetting.Builder<TitleModes>()
        .name("mode")
        .description("you know the drill")
        .defaultValue(TitleModes.title)
        .visible(() -> mode.get() == Modes.Title)
        .build());
    private final Setting<Integer> time = sgGeneral.add(new IntSetting.Builder()
        .name("time")
        .description("how long it stays on the players screen in ticks")
        .defaultValue(200)
        .sliderRange(0, 5000)
        .visible(() -> mode.get() == Modes.Title)
        .build());
    private final Setting<Integer> fadein = sgGeneral.add(new IntSetting.Builder()
        .name("fade-in")
        .description("how long it takes to fade in")
        .defaultValue(0)
        .sliderRange(0, 100)
        .visible(() -> mode.get() == Modes.Title)
        .build());
    private final Setting<Integer> fadeout = sgGeneral.add(new IntSetting.Builder()
        .name("fade-out")
        .description("how long it takes to fade out")
        .defaultValue(0)
        .sliderRange(0, 100)
        .visible(() -> mode.get() == Modes.Title)
        .build());
    private final Setting<ClickEvent> clickevent = sgGeneral.add(new EnumSetting.Builder<ClickEvent>()
        .name("click-event")
        .description("suggest message puts a message in their chatbar, others are self explanatory")
        .defaultValue(ClickEvent.None)
        .build());
    private final Setting<String> clickMsg = sgGeneral.add(new StringSetting.Builder()
        .name("click-msg")
        .defaultValue("https://www.google.com/")
        .visible(() -> clickevent.get() != ClickEvent.None)
        .build());
    private final Setting<Fonts> font = sgGeneral.add(new EnumSetting.Builder<Fonts>()
        .name("font")
        .defaultValue(Fonts.Default)
        .build());
    private final Setting<String> msg = sgGeneral.add(new StringSetting.Builder()
        .name("message")
        .defaultValue("i love boys")
        .build());
    private final Setting<String> players = sgGeneral.add(new StringSetting.Builder()
        .name("players")
        .description("who does it display to (can put player names)")
        .defaultValue("@a")
        .build());
    private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
        .name("color")
        .defaultValue(new SettingColor())
        .build());
    private final Setting<Boolean> bold = sgGeneral.add(new BoolSetting.Builder()
        .name("bold")
        .defaultValue(false)
        .build());
    private final Setting<Boolean> italic = sgGeneral.add(new BoolSetting.Builder()
        .name("italics")
        .defaultValue(false)
        .build());
    private final Setting<Boolean> underline = sgGeneral.add(new BoolSetting.Builder()
        .name("underline")
        .defaultValue(false)
        .build());
    private final Setting<Boolean> obf = sgGeneral.add(new BoolSetting.Builder()
        .name("obfuscate")
        .defaultValue(false)
        .build());
    private final Setting<Boolean> str = sgGeneral.add(new BoolSetting.Builder()
        .name("strikethrough")
        .defaultValue(false)
        .build());

    public Printer() {
        super(Zeitgeist.CATEGORY, "printer", "Prints a cool message");
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        WHorizontalList what = theme.horizontalList();
        WButton print = what.add(theme.button("Print")).widget();
        print.action = () -> {
            String rawtext = " {\"text\":\"" + msg.get() + "\",";
            if (bold.get()) rawtext += "\"bold\":true,";
            if (italic.get()) rawtext += "\"italic\":true,";
            if (str.get()) rawtext += "\"strikethrough\":true,";
            if (obf.get()) rawtext += "\"obfuscated\":true,";
            if (underline.get()) rawtext += "\"underlined\":true,";
            rawtext += "\"color\":\"" + toHexString(color.get()) + "\",";
            if (font.get() == Fonts.Enchanting_Table) rawtext += "\"font\":\"alt\",";
            if (font.get() == Fonts.Unicode) rawtext += "\"font\":\"uniform\",";
            if (clickevent.get() != ClickEvent.None && mode.get() == Modes.Tellraw) {
                rawtext += "\"clickEvent\":{\"action\":\"";
                if (clickevent.get() == ClickEvent.CopyToClipboard)
                    rawtext += "copy_to_clipboard\",\"value\":\"" + clickMsg.get() + "\"}";
                if (clickevent.get() == ClickEvent.OpenUrl)
                    rawtext += "open_url\",\"value\":\"" + clickMsg.get() + "\"}";
                if (clickevent.get() == ClickEvent.SendMessage)
                    rawtext += "run_command\",\"value\":\"" + clickMsg.get() + "\"}";
                if (clickevent.get() == ClickEvent.SuggestMessage)
                    rawtext += "suggest_command\",\"value\":\"" + clickMsg.get() + "\"}";
            }

            if (rawtext.endsWith(","))
                rawtext = rawtext.substring(0, rawtext.length() - 1);
            rawtext += "}";

            if (mode.get() == Modes.Tellraw) {
                String cmd = ("/tellraw " + players.get() + rawtext);
                ChatUtils.sendPlayerMsg(cmd);
            } else if (mode.get() == Modes.Title) {
                String cmd = ("/title " + players.get() + " " + tmode.get().toString() + rawtext);
                ChatUtils.sendPlayerMsg("/title " + players.get() + " times " + fadein.get() + " " + time.get() + " " + fadeout.get());
                ChatUtils.sendPlayerMsg(cmd);
            } else {
                String id = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
                String cmd = ("/bossbar add " + id + rawtext);
                ChatUtils.sendPlayerMsg(cmd);
                ChatUtils.sendPlayerMsg("/bossbar set " + id + " players " + players.get());
            }
        };

        WButton clear = what.add(theme.button("Clear")).widget();
        clear.action = () -> {
            if (isActive()) {
                ChatUtils.sendPlayerMsg("/title " + players.get() + " clear");
            }
        };
        return what;
    }

    private String toHexString(SettingColor c) {
        if (c != null) return String.format("#%02x%02x%02x", c.r, c.g, c.b);
        return "#000000";
    }

    public enum Modes {
        Tellraw, Title, Bossbar
    }

    public enum TitleModes {
        actionbar, title
    }

    public enum Fonts {
        Enchanting_Table, Default, Unicode
    }

    public enum ClickEvent {
        None, OpenUrl, SendMessage, SuggestMessage, CopyToClipboard
    }
}
