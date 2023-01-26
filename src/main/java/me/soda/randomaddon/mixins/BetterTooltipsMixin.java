package me.soda.randomaddon.mixins;

import meteordevelopment.meteorclient.events.game.ItemStackTooltipEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.render.BetterTooltips;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = BetterTooltips.class, remap = false)
public abstract class BetterTooltipsMixin {
    @Shadow
    @Final
    private SettingGroup sgOther;

    @Unique
    private Setting<Boolean> anvilUses;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        anvilUses = sgOther.add(new BoolSetting.Builder()
            .name("anvil-uses")
            .description("Shows the amount of times an item has been used in an anvil.")
            .defaultValue(true)
            .build()
        );
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    private void anvilTooltips(ItemStackTooltipEvent event, CallbackInfo ci) {
        if (anvilUses.get()) {
            NbtCompound tag = event.itemStack.getNbt();
            if (tag == null) return;
            boolean isBook = event.itemStack.getItem().equals(Items.ENCHANTED_BOOK);
            if (isBook && !tag.contains("StoredEnchantments")) return;
            if (event.itemStack.getItem().isEnchantable(event.itemStack) || isBook) {
                int repairCost = tag.contains("RepairCost") ? tag.getInt("RepairCost") : 0;
                int uses = log2(repairCost + 1);
                NbtList list = isBook ? tag.getList("StoredEnchantments", 10) : tag.getList("Enchantments", 10);
                if (list.isEmpty()) return;
                Formatting formatting = getFormatting(list, uses, isBook);
                event.list.add(Text.literal("%sAnvil Uses: %s%d%s.".formatted(Formatting.GRAY, formatting, uses, Formatting.GRAY)));
                event.list.add(Text.literal("%sBase Cost: %s%d%s.".formatted(Formatting.GRAY, formatting, isBook ? getBaseCost(list) + repairCost : repairCost, Formatting.GRAY)));
            }
        }
    }

    private static int getRarity(Enchantment enchantment) {
        return switch (enchantment.getRarity()) {
            case COMMON, UNCOMMON -> 1;
            case RARE -> 2;
            case VERY_RARE -> 4;
        };
    }

    private static int getBaseCost(NbtList enchantments) {
        int cost = 0;
        for (int i = 0; i < enchantments.size(); ++i) {
            NbtCompound nbtCompound = enchantments.getCompound(i);
            Optional<Enchantment> enchantment = Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound));
            if (enchantment.isEmpty()) continue;
            cost += getRarity(enchantment.get()) * EnchantmentHelper.getLevelFromNbt(nbtCompound);
        }
        return cost;
    }

    private static boolean isOptimized(NbtList enchantments, int anvilUses, boolean isBook) {
        if (anvilUses == 0) return true;
        int enchants = enchantments.size();
        int toReturn = (int) Math.pow(2, anvilUses - 1);
        return isBook ? enchants - 1 >= toReturn : enchants >= toReturn;
    }

    private static Formatting getFormatting(NbtList enchantments, int anvilUses, boolean isBook) {
        if (isOptimized(enchantments, anvilUses, isBook)) return Formatting.GREEN;
        else if (isOptimized(enchantments, anvilUses - 1, isBook)) return Formatting.YELLOW;
        return Formatting.RED;
    }

    private static int log2(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }
}
