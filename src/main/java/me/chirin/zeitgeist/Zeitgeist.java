package me.chirin.zeitgeist;

import me.chirin.zeitgeist.commands.*;
import me.chirin.zeitgeist.hud.MoonHud;
import me.chirin.zeitgeist.modules.*;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Zeitgeist extends MeteorAddon {
    public static final Category CATEGORY = new Category("Zeitgeist", new ItemStack(Items.CLOCK));
    public static Category CATEGORYC = new Category("Zeitgeist Neo", new ItemStack(Items.CLOCK));;

    @Override
    public void onInitialize() {
        // Modules
        Modules modules = Modules.get();

        // My
        modules.add(new HackerDetector());
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

        // LO
        Commands.add(new LOClipCommand());

        // HUD
        // My
        Hud.get().register(MoonHud.INFO);

        // Conflict check
        Class<?> clazz = ClientPlayerEntity.class;
        for (Method method : clazz.getMethods()) {
            if (method.getName().contains("sendErr")) {
                throw new RuntimeException("We've found some of your mods was injecting ClientPlayerEntity class expected to cause crashes. Please spot the incompatible mod using binsearch.");
            }
        }

        // Load NEO extension
        new Reflections("me.chirin.zeitgeist.modules.neo", new Scanner[0]).getSubTypesOf(Module.class).forEach(cls -> {
            try {
                modules.add(cls.newInstance());
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
        try {
            // Check if Neo extension modules are found
            Class.forName("me.chirin.zeitgeist.modules.neo.Test");
            Modules.registerCategory(CATEGORYC);
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Override
    public String getPackage() {
        return "me.chirin.zeitgeist";
    }
}
