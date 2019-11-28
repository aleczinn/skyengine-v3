package de.skyengine.core;

import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.screen.ScreenAdapter;
import de.skyengine.resources.Resources;

public class SplashScreen extends ScreenAdapter {

	public ImmediateRenderer renderer;
	
	private float alpha;
	
	public SplashScreen() {
		this.renderer = new ImmediateRenderer();
	}

	@Override
	public void init() {
		super.init();
		
		this.alpha = 0;
	}
	
	@Override
	public void update() {
		super.update();

		if(this.alpha <  1.0F) {
			this.alpha += 0.01F;	
		} else if(this.alpha > 0.9F) {
			SkyEngine.getInstance().getContainer().setScreen(null);
		}
	}
	
	@Override
	public void render(float partialTicks) {
		super.render(partialTicks);
		
		
		float pandaY = SkyEngine.application().getHeight() / 2 - 32;
		
		this.renderer.rect(0, 0, SkyEngine.application().getWidth(), SkyEngine.application().getHeight(), 0xFF000000);
		
		this.renderer.texture(Resources.textures().logoPanda, SkyEngine.application().getHalfWidth() - 128, pandaY, 256, 256, 0xFFFFFFFF, this.alpha);
		float titleWidth = this.renderer.drawCenteredString(Resources.fonts().splashScreen, "SkyEngine", SkyEngine.application().getHalfWidth(), pandaY - 72, 0xFFFFFFFF);
		this.renderer.drawString(Resources.fonts().splashScreenSmall, SkyEngine.getInstance().getEngineVersion(), SkyEngine.application().getHalfWidth() + (titleWidth / 2) - this.renderer.getWidth(Resources.fonts().splashScreenSmall, SkyEngine.getInstance().getEngineVersion()) - 3, pandaY - 14, 0xFFFFFFFF);
	}
}
