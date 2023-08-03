package me.chirin.zeitgeist;

import me.chirin.zeitgeist.commands.*;
import me.chirin.zeitgeist.hud.ImageHud;
import me.chirin.zeitgeist.hud.MoonHud;
import me.chirin.zeitgeist.modules.*;
import me.chirin.zeitgeist.modules.crystalass.crash.*;
import me.chirin.zeitgeist.modules.crystalass.misc.*;
import me.chirin.zeitgeist.modules.tokyo.*;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Zeitgeist extends MeteorAddon {
    public static final Category CATEGORY = new Category("Zeitgeist", new ItemStack(Items.CLOCK));
    public static final Category CATEGORYC = new Category("RandomCrystalPort", new ItemStack(Items.CLOCK));

    @Override
    public void onInitialize() {
        // Modules
        Modules modules = Modules.get();

        // My
        modules.add(new HackerDetector());

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

        // CRYSTAL
        modules.add(new XsDupe());
        modules.add(new ItemFrameDupe());
        modules.add(new BoatFling());
        modules.add(new ServerOpNuke());
        modules.add(new FakeHacker());
        modules.add(new MassPayout());
        modules.add(new LeftHanded());

        // ------
        modules.add(new BoatCrash());
        modules.add(new PositionCrash());
        modules.add(new StorageCrash());
        modules.add(new LecternCrash());
        modules.add(new VehicleCrash());
        modules.add(new BookCrash());
        modules.add(new NullExceptionCrash());
        modules.add(new MovementCrash());
        modules.add(new ExceptionCrash());
        modules.add(new EntityCrash());
        modules.add(new CraftingCrash());
        modules.add(new CreativeCrash());
        modules.add(new AdvancedCrash());
        modules.add(new SignCrash());
        modules.add(new LagMessage());
        modules.add(new PacketFlooder());
        modules.add(new JigSawCrash());
        modules.add(new TradeCrash());
        modules.add(new WorldBorderCrash());
        modules.add(new UDPFlood());
        modules.add(new BungeeCrash());
        modules.add(new SwingCrash());
        modules.add(new AutoLagSign());
        modules.add(new ArmorStandCrash());

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

        // Crystal
        Commands.add(new ItemCommand());
        Commands.add(new TeleportCommand());
        Commands.add(new UUIDCommand());
        Commands.add(new IPLookupCommand());
        Commands.add(new DNSLookupCommand());
        Commands.add(new IPBlacklistCommand());
        Commands.add(new PingCommand());
        Commands.add(new SubnetCalculatorCommand());
        Commands.add(new SpoofServerBrandCommand());

        // Tokyo
        Commands.add(new ChunkInfoCommand());
        Commands.add(new LookAtCommand());

        // LO
        Commands.add(new LOClipCommand());

        // HUD
        Hud.get().register(MoonHud.INFO);

        // Tokyo
        Hud.get().register(ImageHud.INFO);

        TokyoStarscript.init();
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
        Modules.registerCategory(CATEGORYC);
    }

    @Override
    public String getPackage() {
        return "me.chirin.zeitgeist";
    }
}
