package com.flappyburdddd.mcautomacebreach.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import com.flappyburdddd.mcautomacebreach.client.config.ModConfig;

public class SettingsScreen extends Screen {
    private ButtonWidget autoBreachSwapButton;
    private ButtonWidget autoStunSlamButton;
    private ButtonWidget shieldDrainButton;
    private ButtonWidget closeButton;
    private TextFieldWidget shieldDrainDelayField;
    
    public SettingsScreen() {
        super(Text.of("§6[McAutomaceBreach] Settings"));
    }
    
    @Override
    protected void init() {
        super.init();
        this.clearChildren();
        
        int centerX = this.width / 2;
        int startY = 40;
        int spacing = 35;
        
        // AutoBreachSwap Button
        this.autoBreachSwapButton = this.addDrawableChild(
            ButtonWidget.builder(
                Text.of(this.getButtonText("AutoBreachSwap", ModConfig.configData.autoBreachSwapEnabled)),
                button -> toggleAutoBreachSwap()
            )
            .dimensions(centerX - 100, startY, 200, 20)
            .build()
        );
        
        // Auto Stunslam Button
        this.autoStunSlamButton = this.addDrawableChild(
            ButtonWidget.builder(
                Text.of(this.getButtonText("Auto Stunslam", ModConfig.configData.autoStunSlamEnabled)),
                button -> toggleAutoStunSlam()
            )
            .dimensions(centerX - 100, startY + spacing, 200, 20)
            .build()
        );
        
        // Shield Drain Button
        this.shieldDrainButton = this.addDrawableChild(
            ButtonWidget.builder(
                Text.of(this.getButtonText("Shield Drain", ModConfig.configData.shieldDrainEnabled)),
                button -> toggleShieldDrain()
            )
            .dimensions(centerX - 100, startY + spacing * 2, 200, 20)
            .build()
        );
        
        // Shield Drain Delay Field
        this.shieldDrainDelayField = new TextFieldWidget(
            this.textRenderer,
            centerX - 100,
            startY + spacing * 3,
            200,
            20,
            Text.of("Shield Drain Delay (Ticks)")
        );
        this.shieldDrainDelayField.setText(String.valueOf(ModConfig.configData.shieldDrainDelayTicks));
        this.addDrawableChild(this.shieldDrainDelayField);
        
        // Close Button
        this.closeButton = this.addDrawableChild(
            ButtonWidget.builder(
                Text.of("§cClose"),
                button -> this.close()
            )
            .dimensions(centerX - 100, startY + spacing * 4 + 10, 200, 20)
            .build()
        );
    }
    
    private String getButtonText(String name, boolean enabled) {
        return name + ": " + (enabled ? "§a✓ ON" : "§c✗ OFF");
    }
    
    private void toggleAutoBreachSwap() {
        ModConfig.configData.autoBreachSwapEnabled = !ModConfig.configData.autoBreachSwapEnabled;
        ModConfig.saveConfig();
        this.autoBreachSwapButton.setMessage(
            Text.of(this.getButtonText("AutoBreachSwap", ModConfig.configData.autoBreachSwapEnabled))
        );
    }
    
    private void toggleAutoStunSlam() {
        ModConfig.configData.autoStunSlamEnabled = !ModConfig.configData.autoStunSlamEnabled;
        ModConfig.saveConfig();
        this.autoStunSlamButton.setMessage(
            Text.of(this.getButtonText("Auto Stunslam", ModConfig.configData.autoStunSlamEnabled))
        );
    }
    
    private void toggleShieldDrain() {
        ModConfig.configData.shieldDrainEnabled = !ModConfig.configData.shieldDrainEnabled;
        ModConfig.saveConfig();
        this.shieldDrainButton.setMessage(
            Text.of(this.getButtonText("Shield Drain", ModConfig.configData.shieldDrainEnabled))
        );
    }
    
    @Override
    public void tick() {
        super.tick();
        this.shieldDrainDelayField.tick();
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            this.title,
            this.width / 2,
            20,
            0xFFFFFF
        );
        super.render(context, mouseX, mouseY, delta);
        
        // Draw help text
        context.drawTextWithShadow(
            this.textRenderer,
            Text.of("§7Press 'U' to toggle Stunslam in-game"),
            10,
            this.height - 20,
            0xAAAAAA
        );
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER && this.shieldDrainDelayField.isFocused()) {
            try {
                int delay = Integer.parseInt(this.shieldDrainDelayField.getText());
                if (delay >= 1 && delay <= 20) {
                    ModConfig.configData.shieldDrainDelayTicks = delay;
                    ModConfig.saveConfig();
                }
            } catch (NumberFormatException e) {
                // Invalid input, ignore
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public void close() {
        this.client.setScreen(null);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
