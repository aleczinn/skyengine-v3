package de.games.calculator;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import de.skyengine.core.GameContainer;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.ImmediateRenderer.ShapeType;
import de.skyengine.resources.Resources;

public class Calculator extends GameContainer {

	private ImmediateRenderer renderer;
	
	private String calculationText = "0";
	
	private List<CalculateButton> buttons;
	private final int[] numbers = new int[] {7, 8, 9, 4, 5, 6, 1, 2, 3, 0};
	
	private final int PAD_COLOR = 0xFF2f3542;
	private final int PAD_COLOR_HOVER = 0xFFff7f50;
	private final int PAD_COLOR_BACKGROUND = 0xFF242833;
	private final int PAD_COLOR_HOVER_BACKGROUND = 0xFFff6348;
	
	private final int BUTTON_COLOR = 0xFFa4b0be;
	private final int BUTTON_COLOR_HOVER = 0xFFdfe4ea;
	private final int BUTTON_COLOR_BACKGROUND = 0xFF747d8c;
	private final int BUTTON_COLOR_HOVER_BACKGROUND = 0xFFa4b0be;
	
	@Override
	public void init() {
		this.renderer = new ImmediateRenderer();
		
		this.buttons = new ArrayList<CalculateButton>();
		this.initButtons();
		super.init();
	}

	@Override
	public void input(Input input) {
		for(CalculateButton button : this.buttons) {
			if(this.isOver(input.getMouseX(), input.getMouseY(), button.getX(), button.getY(), button.getWidth(), button.getHeight())) {
				if(input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
					if(button.getButtonID() >= 0 && button.getButtonID() <= 9) {
						if(this.calculationText.equalsIgnoreCase("0")) this.calculationText = "";
						
						this.calculationText += button.getDisplayText();
					} else {
						switch (button.getButtonID()) {
						case 0:
							
							break;
						default:
							break;
						}
					}
				}
				break;
			}
		}
		
		if(input.isKeyPressed(GLFW.GLFW_KEY_F)) {
			this.initButtons();
			System.out.println("Init Buttons");
		}
		super.input(input);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		this.renderer.rect(0, 0, SkyEngine.getWidth(), SkyEngine.getHeight(), 0xFF1B1E26);
		this.renderer.lineWidth(2.0F);
		this.renderer.rect(ShapeType.LINE, 10, 10, SkyEngine.getWidth() - 10, 110, 0xFFEEEEEE);
		
		this.renderer.drawString(Resources.fonts().calculatorBig, this.calculationText, SkyEngine.getWidth() - 25 - this.renderer.getWidth(Resources.fonts().calculatorBig, this.calculationText), 27.0F, 0xFFEEEEEE);
		
		for(CalculateButton button : this.buttons) {
			boolean over = this.isOver(mouseX, mouseY, button.getX(), button.getY(), button.getWidth(), button.getHeight());
			
			int colorBackground = over ? this.PAD_COLOR_HOVER_BACKGROUND : this.PAD_COLOR_BACKGROUND;
			int color= over ? this.PAD_COLOR_HOVER : this.PAD_COLOR;
			
			boolean isPad = button.getButtonID() >= 0 && button.getButtonID() <= 9;
			
			if(!isPad) {
				colorBackground = over ? this.BUTTON_COLOR_HOVER_BACKGROUND : this.BUTTON_COLOR_BACKGROUND;
				color = over ? this.BUTTON_COLOR_HOVER : this.BUTTON_COLOR;
			}
			
			this.renderer.rect(button.getX() + 5, button.getY() + 5, button.getX() + 5 + button.getWidth(), button.getY() + 5 + button.getHeight(), colorBackground);
			this.renderer.rect(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), color);
			
			float fontWidth = this.renderer.getWidth(Resources.fonts().calculator, button.getDisplayText());
			float fontHeight = this.renderer.getHeight(Resources.fonts().calculator, button.getDisplayText());
			
			this.renderer.drawString(Resources.fonts().calculator, button.getDisplayText(), button.getX() + (button.getWidth() / 2.0F) - (fontWidth / 2.0F), button.getY() + (button.getHeight() / 2.0F) - (fontHeight / 2.0F), (isPad || !over) ? 0xFFdfe4ea : 0xFF747d8c);
		}
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void exitGame() {
		super.exitGame();
	}

	private void initButtons() {
		this.buttons.clear();
		
		float padSizeX = 64;
		float padSizeY = 64;
		
		float maxPadX = (3 * padSizeX) + (3 * 10.0F);
		float maxPadY = (4 * padSizeY) + (4 * 10.0F);
		
		float padX = (SkyEngine.getWidth() / 2.0F) - (maxPadX / 2.0F);
		float padY = 320;

		int index = 0;
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 3; x++) {
				if((x == 0 || x == 2) && y == 3) continue;
				
				float posX = padX + (x * padSizeX) + (x * 10);
				float posY = padY + (y * padSizeY) + (y * 10);
				
				this.buttons.add(new CalculateButton(this.numbers[index], posX, posY, padSizeX, padSizeY, String.valueOf(this.numbers[index])));
				index++;
			}
		}
		
		this.buttons.add(new CalculateButton(10, padX + maxPadX - padSizeX - 10.0F, padY + maxPadY - padSizeY - 10, padSizeX, padSizeY, ","));
		this.buttons.add(new CalculateButton(11, padX, padY + maxPadY - padSizeY - 10, padSizeX, padSizeY, "+/-"));
	}
	
	private boolean isOver(float mouseX, float mouseY, float x, float y, float width, float height) {
		return mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);
	}
}
