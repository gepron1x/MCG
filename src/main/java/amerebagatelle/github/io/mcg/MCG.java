package amerebagatelle.github.io.mcg;

import amerebagatelle.github.io.mcg.coordinates.CoordinatesManager;
import amerebagatelle.github.io.mcg.gui.screen.CoordinateFileManager;
import amerebagatelle.github.io.mcg.utils.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class MCG implements ClientModInitializer {
    public static final Logger logger = LogManager.getLogger();
    public static final CoordinatesManager coordinatesManager = new CoordinatesManager();
    public static final Config config = new Config();

    public static KeyBinding binding;
    public static Screen managerScreenInstance = new CoordinateFileManager();

    @Override
    public void onInitializeClient() {
        logger.info("Gathering your coordinates...");

        try {
            coordinatesManager.initCoordinatesFolder();
        } catch (IOException e) {
            throw new RuntimeException("Can't write coordinates folder.", e);
        }

        binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "mcg.keybinding.main",
                GLFW.GLFW_KEY_Y,
                "MCG"
        ));
    }
}
