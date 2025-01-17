package amerebagatelle.github.io.mcg.gui.screen;

import amerebagatelle.github.io.mcg.MCG;
import amerebagatelle.github.io.mcg.gui.MCGButtonWidget;
import amerebagatelle.github.io.mcg.gui.overlay.ErrorDisplayOverlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CoordinateFileCreationScreen extends Screen {
    private TextFieldWidget fileNameWidget;
    private MCGButtonWidget confirmButton;
    private MCGButtonWidget cancelButton;

    private final String fileType;
    private final Path folderPath;

    public CoordinateFileCreationScreen(String fileType, Path folderPath) {
        super(Text.literal("CoordinateFileCreationScreen"));
        this.fileType = fileType;
        this.folderPath = folderPath;
    }

    @Override
    public void init() {
        fileNameWidget = new TextFieldWidget(textRenderer, width / 2 - 100, 100, 200, 20, Text.translatable("mcg.button.name"));
        this.addSelectableChild(fileNameWidget);

        confirmButton = new MCGButtonWidget(width / 2 - 50, height - 100, 100, 20, Text.translatable("mcg.button.confirm"), press -> confirm());
        this.addDrawableChild(confirmButton);
        cancelButton = new MCGButtonWidget(width / 2 - 50, height - 70, 100, 20, Text.translatable("mcg.button.cancel"), press -> cancel());
        this.addDrawableChild(cancelButton);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        updateButtonStates();
        this.renderBackground(matrices);
        drawCenteredText(matrices, textRenderer, I18n.translate("mcg.file.new" + fileType), this.width / 2, 50, 16777215);
        fileNameWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        ErrorDisplayOverlay.INSTANCE.render(matrices, height);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void confirm() {
        try {
            if (fileType.equals("folder")) {
                MCG.coordinatesManager.createFolder(Paths.get(folderPath.toString(), fileNameWidget.getText()));
            } else {
                MCG.coordinatesManager.initNewCoordinatesFile(Paths.get(folderPath.toString(), fileNameWidget.getText().endsWith(".coordinates") ? fileNameWidget.getText() : fileNameWidget.getText() + ".coordinates"));
            }
            Objects.requireNonNull(client).setScreen(new CoordinateFileManager());
        } catch (IOException e) {
            MCG.logger.debug("Can't make new coordinates file.");
            ErrorDisplayOverlay.INSTANCE.addError(I18n.translate("mcg.file.creationfail"));
        } catch (InvalidPathException e) {
            MCG.logger.debug("Invalid name for new coordinates file.");
            ErrorDisplayOverlay.INSTANCE.addError(I18n.translate("mcg.file.creationfail") + ": " + I18n.translate("mcg.file.invalidpath"));
        }
    }

    private void cancel() {
        Objects.requireNonNull(client).setScreen(new CoordinateFileManager());
    }

    private void updateButtonStates() {
        confirmButton.active = fileNameWidget.getText().length() != 0;
    }
}
