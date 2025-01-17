package amerebagatelle.github.io.mcg.utils;

import amerebagatelle.github.io.mcg.MCG;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;

public class Config {
    public static final File settingsFile = FabricLoader.getInstance().getConfigDir().resolve("mcg.json").toFile();
    public static final Gson gson = new GsonBuilder().create();

    public int overlayX, overlayY = 0;
    public boolean showCompass = true;
    public String overlayFormat = "%name @ %x %y %z";

    public Config() {
        if (!settingsFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                settingsFile.createNewFile();

                BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile));
                writer.write("{}");
                writer.flush();
                writer.close();

                writeSetting("overlayX", "20");
                writeSetting("overlayY", "20");
                writeSetting("showCompass", "true");
                writeSetting("overlayFormat", "%name @ %x %y %z");
            } catch (IOException e) {
                MCG.logger.error("Couldn't create a settings file.");
                e.printStackTrace();
            }
        }

        loadSettings();
    }

    public void loadSettings() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
            JsonObject settingsJson = gson.fromJson(reader, JsonObject.class);
            reader.close();

            try {
                overlayX = settingsJson.get("overlayX").getAsInt();
            } catch (NullPointerException e) {
                writeSetting("overlayX", "20");
            }
            try {
                overlayY = settingsJson.get("overlayY").getAsInt();
            } catch (NullPointerException e) {
                writeSetting("overlayY", "20");
            }
            try {
                overlayFormat = settingsJson.get("overlayFormat").getAsString();
            } catch (NullPointerException e) {
                writeSetting("overlayFormat", "%name @ %x %y %z");
            } try {
                showCompass = settingsJson.get("showCompass").getAsBoolean();
            } catch (NullPointerException e) {
                writeSetting("showCompass", "true");
            }
        } catch (IOException e) {
            MCG.logger.error("Couldn't read the settings file.\n");
            e.printStackTrace();
        }
    }



    public void writeSetting(String setting, String value) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
            JsonObject settingsJson = gson.fromJson(reader, JsonObject.class);
            reader.close();

            settingsJson.addProperty(setting, value);

            Files.write(settingsFile.toPath(), gson.toJson(settingsJson).getBytes());
        } catch (IOException e) {
            MCG.logger.error("Couldn't write to the settings file.\n");
            e.printStackTrace();
        }
    }
}
