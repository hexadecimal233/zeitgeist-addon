package me.soda.randomaddon;

import me.soda.randomaddon.commands.BloatCommand;
import me.soda.randomaddon.commands.RenameCommand;
import me.soda.randomaddon.commands.StopCommand;
import me.soda.randomaddon.commands.VoidCommand;
import me.soda.randomaddon.hud.ImageHud;
import me.soda.randomaddon.modules.*;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Random extends MeteorAddon {
    public static final Category CATEGORY = new Category("Random", new ItemStack(Items.POPPY));

    @Override
    public void onInitialize() {
        // Modules
        Modules modules = Modules.get();

        // My
        modules.add(new HackerDetector());

        // Allah
        modules.add(new BackTP());
        modules.add(new Bot());
        modules.add(new ConsoleFlood());
        modules.add(new SexCrash());
        modules.add(new Test());
        modules.add(new VelocityBoost());
        modules.add(new WorldGuardBypass());
        // Greteor
        modules.add(new GameTweaks());
        modules.add(new VanillaFeatures());
        modules.add(new PrivateChat());
        modules.add(new EasyFilter());
        modules.add(new AutoMessage());
        // Bidoof
        modules.add(new PingSpoofer());
        modules.add(new Jukebox());
        modules.add(new SpamPlus());
        // Mathax
        modules.add(new Sniper());

        // Commands
        // Allah
        Commands commands = Commands.get();
        commands.add(new BloatCommand());
        commands.add(new StopCommand());
        // Greteor
        commands.add(new RenameCommand());
        commands.add(new VoidCommand());

        //Bidoof
        Hud hud = Hud.get();
        hud.register(ImageHud.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "me.soda.randomaddon";
    }
}
