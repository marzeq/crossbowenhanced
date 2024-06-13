package me.marzeq.crossbowenhanced.config;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

public class Config {
    public boolean fireworksInOffHand = Defaults.fireworksInOffHand;
    public boolean autoShoot = Defaults.autoShoot;

    private transient File file;


    private Config() {
    }

    public static Config load() {
        File file = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                CrossbowEnhanced.MOD_ID + ".toml"
        );

        Config config;
        if (file.exists()) {
            Toml configTOML = new Toml().read(file);
            config = configTOML.to(Config.class);
            config.file = file;
        } else {
            config = new Config();
            config.file = file;
            config.save();
        }

        return config;
    }

    public void save() {
        TomlWriter writer = new TomlWriter();
        try {
            writer.write(this, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        fireworksInOffHand = Defaults.fireworksInOffHand;
        autoShoot = Defaults.autoShoot;
        save();
    }
}