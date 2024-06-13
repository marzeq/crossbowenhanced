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

        builder.getOrCreateCategory(Text.of("Config Values"))
                .addEntry(entry
                        .startBooleanToggle(Text.of("Put explosive fireworks in off-hand"), config.fireworksInOffHand)
                        .setDefaultValue(Defaults.fireworksInOffHand)
                        .setSaveConsumer(v -> config.fireworksInOffHand = v).build()
                )
                .addEntry(entry
                        .startBooleanToggle(Text.of("Auto shoot"), config.autoShoot)
                        .setDefaultValue(Defaults.autoShoot)
                        .setSaveConsumer(v -> config.autoShoot = v).build()
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