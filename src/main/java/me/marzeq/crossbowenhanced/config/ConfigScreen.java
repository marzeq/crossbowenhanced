package me.marzeq.crossbowenhanced.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConfigScreen implements ModMenuApi {
    private net.minecraft.client.gui.screen.Screen screen(net.minecraft.client.gui.screen.Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("Crossbow Enhanced Config"));

        Config config = CrossbowEnhanced.config;

        ConfigEntryBuilder entry = builder.entryBuilder();

        builder.getOrCreateCategory(Text.of("Functionality"))
                .addEntry(entry
                        .startBooleanToggle(Text.of("Put explosive fireworks in off-hand"), config.fireworksInOffHand)
                        .setDefaultValue(Defaults.fireworksInOffHand)
                        .setTooltip(Text.of("Put fireworks with explosive effects in the off-hand for the duration of drawing the crossbow (they will be put back in their slot afterwards)"))
                        .setSaveConsumer(v -> config.fireworksInOffHand = v).build()
                )
                .addEntry(entry
                        .startBooleanToggle(Text.of("Auto shoot"), config.autoShoot)
                        .setDefaultValue(Defaults.autoShoot)
                        .setTooltip(Text.of("Automatically shoot the crossbow when it is fully charged and the player releases the right mouse button"))
                        .setSaveConsumer(v -> config.autoShoot = v).build()
                );

        builder.getOrCreateCategory(Text.of("Drawing order"))
                .addEntry(entry
                        .startEnumSelector(Text.of("Order"), Config.ORDER.class, config.order)
                        .setDefaultValue(Defaults.order)
                        .setEnumNameProvider(value -> switch (value) {
                            case Config.ORDER.FROM_TOP_LEFT -> Text.of("Top left to bottom right");
                            case Config.ORDER.FROM_BOTTOM_RIGHT -> Text.of("Bottom right to top left");
                            default -> Text.of(value.toString());
                        })
                        .setTooltip(Text.of("The order in which the mod will take fireworks from the inventory"))
                        .setSaveConsumer(v -> config.order = v).build()
                )
                .addEntry(entry
                        .startBooleanToggle(Text.of("Prioritise stacks with lower count"), config.prioritiseStacksWithLowerCount)
                        .setDefaultValue(Defaults.prioritiseStacksWithLowerCount)
                        .setTooltip(Text.of("If there is a stack with a lower count of fireworks, that would normally not be drawn from, take from it first"))
                        .setSaveConsumer(v -> config.prioritiseStacksWithLowerCount = v).build()
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