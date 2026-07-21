package me.marzeq.crossbowenhanced.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ConfigScreen implements ModMenuApi {
    private Screen screen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Crossbow Enhanced Config"));

        Config config = CrossbowEnhanced.config;

        ConfigEntryBuilder entry = builder.entryBuilder();

        var projectileManagementRequirement = new Requirement() {
            @Override
            public boolean check() {
                return config.enableProjectileManagementFeature;
            }
        };

        builder.getOrCreateCategory(Component.literal("Projectile management"))
                .addEntry(entry
                        .startBooleanToggle(Component.literal("Enable feature"), config.enableProjectileManagementFeature)
                        .setDefaultValue(Defaults.enableProjectileManagementFeature)
                        .setTooltip(Component.literal("This effectively replaces the vanilla projectile drawing process by automatically putting desired projectiles in your off-hand.\n" +
                                            "This has the added bonus of not having to worry about putting charged fireworks in your off-hand."))
                        .setSaveConsumer(v -> config.enableProjectileManagementFeature = v)
                        .build()
                )
                .addEntry(entry
                        .startEnumSelector(Component.literal("Preferred projectile type"), Config.PREFERRED_PROJECTILE.class, config.preferredProjectile)
                        .setDefaultValue(Defaults.preferredProjectile)
                        .setEnumNameProvider(value -> switch (value) {
                            case Config.PREFERRED_PROJECTILE.FIREWORKS -> Component.literal("Fireworks");
                            case Config.PREFERRED_PROJECTILE.TIPPED_ARROWS -> Component.literal("Tipped arrows");
                            case Config.PREFERRED_PROJECTILE.REGULAR_ARROWS -> Component.literal("Regular arrows");
                            default -> Component.literal(value.toString());
                        })
                        .setTooltip(Component.literal("The preferred projectile type. The mod will try and shoot with it first, and only when there's none will it move on to other ones"))
                        .setSaveConsumer(v -> config.preferredProjectile = v)
                        .setDisplayRequirement(projectileManagementRequirement)
                        .build()
                )
                .addEntry(entry
                        .startEnumSelector(Component.literal("Drawing order"), Config.DRAW_ORDER.class, config.drawOrder)
                        .setDefaultValue(Defaults.order)
                        .setEnumNameProvider(value -> switch (value) {
                            case Config.DRAW_ORDER.FROM_TOP_LEFT -> Component.literal("Top left to bottom right");
                            case Config.DRAW_ORDER.FROM_BOTTOM_RIGHT -> Component.literal("Bottom right to top left");
                            default -> Component.literal(value.toString());
                        })
                        .setTooltip(Component.literal("If there are multiple slots of projectiles with equal priority, what is the order they should be drawn from"))
                        .setSaveConsumer(v -> config.drawOrder = v)
                        .setDisplayRequirement(projectileManagementRequirement)
                        .build()
                )
                .addEntry(entry
                        .startBooleanToggle(Component.literal("Prioritise stacks with lower count"), config.prioritiseStacksWithLowerCount)
                        .setDefaultValue(Defaults.prioritiseStacksWithLowerCount)
                        .setTooltip(Component.literal("If there are multiple slots of projectiles with equal priority, but one has a lower count, should the drawing order be ignored and it be picked instead"))
                        .setSaveConsumer(v -> config.prioritiseStacksWithLowerCount = v)
                        .setDisplayRequirement(projectileManagementRequirement)
                        .build()
                );


        builder.getOrCreateCategory(Component.literal("Auto shoot"))
                .addEntry(entry
                    .startBooleanToggle(Component.literal("Enable feature"), config.enableAutoShootFeature)
                    .setDefaultValue(Defaults.enableAutoShootFeature)
                    .setTooltip(Component.literal("Automatically shoot the crossbow when it is fully charged and the player releases the right mouse button"))
                    .setSaveConsumer(v -> config.enableAutoShootFeature= v)
                    .build()
                );


        builder.setSavingRunnable(config::save);

        return builder.build();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                return screen(parent);
            } catch (Exception e) {
                Config config = CrossbowEnhanced.config;
                config.reset();
                return screen(parent);
            }
        };
    }
}
