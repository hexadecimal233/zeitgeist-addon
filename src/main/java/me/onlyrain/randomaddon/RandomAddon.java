package me.onlyrain.randomaddon;

import me.onlyrain.randomaddon.commands.*;
import me.onlyrain.randomaddon.hud.MoonHud;
import me.onlyrain.randomaddon.modules.*;
import me.onlyrain.randomaddon.modules.crystalass.crash.*;
import me.onlyrain.randomaddon.modules.crystalass.misc.*;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class RandomAddon extends MeteorAddon {
    public static final Category CATEGORY = new Category("Random", new ItemStack(Items.POPPY));
    public static final Category CATEGORYC = new Category("RandomCrystalPort", new ItemStack(Items.STONE));

    @Override
    public void onInitialize() {
        // Modules
        Modules modules = Modules.get();

        // My
        modules.add(new HackerDetector());

        // QOL
        Modules.get().add(new AnyPlacer());
        Modules.get().add(new AutoAnchor());
        Modules.get().add(new AutoExecute());
        Modules.get().add(new ChunkCrash());
        Modules.get().add(new ClickNuke());
        Modules.get().add(new GhostBlockFly());
        Modules.get().add(new Printer());
        Modules.get().add(new ProjectileDeflector());
        Modules.get().add(new Totem());
        Modules.get().add(new VeloFly());

        // ScoreboardHelper & UglyScoreboardFix
        modules.add(new ScoreboardPlus());

        // LiveOverflowMod
        modules.add(new LOReach());
        modules.add(new LOClipReach());
        modules.add(new LOWorldGuardBypass());
        modules.add(new NoRandomPackets());

        // Allah
        modules.add(new BackTP());
        modules.add(new Bot());
        modules.add(new ConsoleFlood());
        modules.add(new VelocityBoost());
        // Greteor
        modules.add(new GameTweaks());
        modules.add(new PrivateChat());
        modules.add(new EasyFilter());
        modules.add(new AutoMessage());
        // Mathax
        modules.add(new Sniper());

        // FK U CRYSTAL

        modules.add(new XsDupe());
        modules.add(new ItemFrameDupe());
        modules.add(new ElytraFix());
        modules.add(new BoatFling());
        modules.add(new ServerOpNuke());
        modules.add(new FakeHacker());
        modules.add(new MassPayout());
        modules.add(new PingSpoofer());
        modules.add(new HeadRoll());
        modules.add(new LeftHanded());

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

        // LO
        Commands.add(new LOClipCommand());

        // HUD
        Hud.get().register(MoonHud.INFO);

        //Crystal

        Commands.add(new ItemCommand());
        Commands.add(new TeleportCommand());
        Commands.add(new UUIDCommand());
        Commands.add(new DropAllCommand());
        Commands.add(new IPLookupCommand());
        Commands.add(new DNSLookupCommand());
        Commands.add(new IPBlacklistCommand());
        Commands.add(new PingCommand());
        Commands.add(new SubnetCalculatorCommand());
        Commands.add(new SpoofNameCommand());
        Commands.add(new SpoofServerBrandCommand());
        Commands.add(new SpoofUUIDCommand());
        Commands.add(new WebhookDeleteCommand());
        Commands.add(new WebhookSendCommand());
        Commands.add(new NetProxyCommand());
        Commands.add(new NetProxyDisconnectCommand());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
        Modules.registerCategory(CATEGORYC);
    }

    @Override
    public String getPackage() {
        return "me.onlyrain.randomaddon";
    }
}
