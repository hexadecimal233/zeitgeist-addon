package me.chirin.zeitgeist;

import me.chirin.zeitgeist.commands.*;
import me.chirin.zeitgeist.hud.ImageHud;
import me.chirin.zeitgeist.hud.MoonHud;
import me.chirin.zeitgeist.modules.*;
import me.chirin.zeitgeist.modules.crystal.UDPFlood;
import me.chirin.zeitgeist.modules.tokyo.*;
import me.chirin.zeitgeist.utils.Utils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.reflections.Reflections;

import java.util.Scanner;

public class Zeitgeist extends MeteorAddon {
    public static final Category CATEGORY = new Category("Zeitgeist", new ItemStack(Items.CLOCK));
    public static Category CATEGORYC = new Category(Utils.丨(Utils.丨[0], false), new ItemStack(Items.CLOCK));;

    @Override
    public void onInitialize() {
        // Modules
        Modules modules = Modules.get();

        // My
        modules.add(new HackerDetector());
        modules.add(new UDPFlood());
        modules.add(new LevitatingBoat());

        // QOL
        modules.add(new AnyPlacer());
        modules.add(new AutoAnchor());
        modules.add(new AutoExecute());
        modules.add(new ChunkCrash());
        modules.add(new GhostBlockFly());
        modules.add(new Printer());
        modules.add(new ProjectileDeflector());
        modules.add(new Totem());
        modules.add(new VeloFly());

        // ScoreboardHelper & UglyScoreboardFix
        modules.add(new ScoreboardPlus());
        // LiveOverflowMod
        modules.add(new LOReach());
        modules.add(new LOClipReach());
        modules.add(new LOWorldGuardBypass());
        modules.add(new NoRandomPackets());
        // Allah
        modules.add(new BackTP());
        modules.add(new ConsoleFlood());
        modules.add(new VelocityBoost());
        // Greteor
        modules.add(new GameTweaks());
        modules.add(new PrivateChat());
        modules.add(new EasyFilter());
        modules.add(new AutoMessage());
        // Mathax
        modules.add(new Sniper());
        // Wurst
        modules.add(new BuildRandom());
        // MeteorUtils
        modules.add(new OpenAnarchyAutoDupe());
        // Vector
        modules.add(new NoSwing());

        new Reflections(Utils.丨(Utils.丨[1], false), new Scanner[0]).getSubTypesOf(Module.class).forEach(cls -> {
            try {
                modules.add(cls.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        });

        // Tokyo
        modules.add(new Announcer());
        modules.add(new AutoTpa());
        modules.add(new AutoUnfriend());
        modules.add(ChatManager.INSTANCE);
        modules.add(Jukebox.INSTANCE);


        // Commands
        // Allah
        Commands.add(new BloatCommand());
        Commands.add(new StopCommand());

        // QOL
        Commands.add(new BeehiveCommand());
        Commands.add(new ClearCommand());
        Commands.add(new CorruptCommand());
        Commands.add(new EffectCommand());
        Commands.add(new ForceOpCommand());
        Commands.add(new HideCommand());
        Commands.add(new KillCommand());
        Commands.add(new LagCommand());
        Commands.add(new ShriekCommand());
        Commands.add(new ChangePlayerNameCommand());
        Commands.add(new LoginKick());

        // Vector Addon
        Commands.add(new TeleportCommand());
        Commands.add(new UUIDCommand());
        Commands.add(new ItemCommand());
        Commands.add(new PlayerHeadCommand());
        Commands.add(new DesyncCommand());

        // Tokyo
        Commands.add(new ChunkInfoCommand());
        Commands.add(new LookAtCommand());

        // LO
        Commands.add(new LOClipCommand());

        // HUD
        // My
        Hud.get().register(MoonHud.INFO);

        // Tokyo
        Hud.get().register(ImageHud.INFO);

        TokyoStarscript.init();
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
        try {
            Class.forName(Utils.丨(Utils.丨[1], true));
            Modules.registerCategory(CATEGORYC);
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Override
    public String getPackage() {
        return "me.chirin.zeitgeist";
    }
}
