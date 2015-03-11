package Bleach.Renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Bleach.Entity;
import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;
import Bleach.Sprite;
import Bleach.TerrainBlock;
import Bleach.Loader.Discette;

public class Picasso {
	private int width, height;						// Width and height of rendering area.
	private List<String> debug;						// Debug data to be printed on the screen.
	private boolean doDebug;						// Whether to display the debug data or not.
	BufferedImage canvas;							// Back-buffer, this is where we draw stuff.

	public Picasso(int width, int height) {
		this.width = width;
		this.height = height;
		debug = new ArrayList<String>();
		doDebug = false;
		canvas = Discette.toCompatibleImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
	}

	public void addDebugLine(String line) {
		debug.add(line);
	}

	public void clearDebugLines() {
		debug.clear();
	}

	public void render(Graphics panelGraphics, LevelInteractable currentLevelSetting) {

		if (currentLevelSetting == null)
			return;

		double renderWidth = width < currentLevelSetting.getWidth() ? width : currentLevelSetting.getWidth();
		double renderHeight  = height < currentLevelSetting.getHeight() ? height : currentLevelSetting.getHeight();
		
		double paddingX = (width > currentLevelSetting.getWidth() ? (width - currentLevelSetting.getWidth()) / 2.0 : 0);
		double paddingY = (height > currentLevelSetting.getHeight() ? (height - currentLevelSetting.getHeight()) / 2.0 : 0);
		
		double offsetX = paddingX + currentLevelSetting.getViewport().x - width / 2.0;
		double offsetY = paddingY + currentLevelSetting.getViewport().y - height / 2.0;

		Graphics graphics = canvas.getGraphics();
		
		/* Render the backdrop if any. The backdrop is visible of the screen size is bigger than the level size */
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, width, height);
		
		BufferedImage backdrop = Discette.getImage(currentLevelSetting.getBackdropKey()) == null ? null : Discette.getImage(currentLevelSetting.getBackdropKey()).getFrame();
		if(backdrop != null){
			for(int i = 0; i < Math.ceil(width / backdrop.getWidth() + 1); i++){
				for(int j = 0; j < Math.ceil(height / backdrop.getHeight() + 1); j++){
					graphics.drawImage(backdrop, i * backdrop.getWidth(), j * backdrop.getHeight(), null);
				}
			}
		}
		
		/* If the level size is smaller than the screen size we have to clip it */
		graphics.setClip((int)paddingX, (int)paddingY, (int)renderWidth, (int)renderHeight);

		// Render level backgrounds using the parallax effect.
		int currentBackgroundNumber = 1;
		List<BufferedImage> backgrounds = currentLevelSetting.getBackgrounds();

		for (BufferedImage background : backgrounds) {
			// Calculate the position of this background and tile it if needed.
			int parallaxDistance = currentLevelSetting.getBackgroundParallaxDistance();
			double parallaxModifier = currentBackgroundNumber / (parallaxDistance / 1.5); // Do some math to get a modifier that seems to be Ok. This modifier alters the position of the current background in order to create the parallax effect.
			
			double scrollX = (paddingX + currentLevelSetting.getViewport().x - renderWidth / 2.0) / renderWidth * -1;
			double scrollY = (paddingY + currentLevelSetting.getViewport().y - renderHeight / 2.0) / renderHeight * -1;
			
			int tileCountX = (int) Math.ceil((double) renderWidth / background.getWidth() + 1);
			int tileCountY = (int) Math.ceil((double) renderHeight / background.getHeight() + 1);

			int startX = (int) ((renderWidth * scrollX * parallaxModifier) % background.getWidth());
			int startY = (int) ((renderHeight * scrollY * parallaxModifier) % background.getHeight());

			for (int i = 0; i < tileCountX; i++) {
				for (int j = 0; j < tileCountY; j++) {
					int x = (int)paddingX + startX + i * background.getWidth();
					int y = (int)paddingY + startY + j * background.getHeight();

					graphics.drawImage(background, x, y, background.getWidth(), background.getHeight(), null);
				}
			}
			currentBackgroundNumber++;
		}

		// Render TerrainBlocks
		for (EntityTranslatable block : currentLevelSetting.getTerrains()) {
			TerrainBlock tb = ((TerrainBlock) block);
			Sprite sprite = tb.getSprite();

			double x = tb.getPosition().x - offsetX;
			double y = tb.getPosition().y - offsetY;

			graphics.drawImage(sprite.getFrame(), (int) x, (int) y, sprite.getWidth(), sprite.getHeight(), null);
			if (doDebug) {
				graphics.setColor(Color.red);
				graphics.drawRect((int) (tb.getPosition().x - offsetX), (int) (tb.getPosition().y - offsetY), 1, 1);
				graphics.drawRect((int) (tb.getBoundary().x - offsetX), (int) (tb.getBoundary().y - offsetY), (int) tb.getBoundary().width, (int) tb.getBoundary().height);
			}
		}

		// List that will contain all the entities present on the level
		List<EntityTranslatable> entities = new ArrayList<EntityTranslatable>();

		// Accumulate objects on scene
		entities.addAll(currentLevelSetting.getLoots());
		entities.addAll(currentLevelSetting.getMobiles());
		entities.addAll(currentLevelSetting.getPlayers());
		entities.addAll(currentLevelSetting.getProjectiles());

		// Iterate over objects and render them
		for (EntityTranslatable entityTranslatable : entities) {
			Entity entity = (Entity) entityTranslatable;
			if (entity == null || entity.getSprite() == null)
				break; // If the entity or sprite is null then it's pointless to
						// try to draw anything.
			Point spriteOrigin = entity.getSprite().getOrigin();

			graphics.drawImage(entity.getSprite().getFrame(), (int) (entity.getPosition().x - spriteOrigin.x - offsetX), (int) (entity.getPosition().y - spriteOrigin.y - offsetY), entity.getSprite().getWidth(), entity.getSprite().getHeight(), null);
			if (doDebug) {
				graphics.setColor(Color.red);
				graphics.drawRect((int) (entity.getPosition().x - offsetX), (int) (entity.getPosition().y - offsetY), 1, 1);
				graphics.drawRect((int) (entity.getBoundary().x - offsetX), (int) (entity.getBoundary().y - offsetY), (int) entity.getBoundary().width, (int) entity.getBoundary().height);
			}
		}

		// Handle debug data
		if (doDebug) {
			graphics.setClip(null);
			
			int lineNumber = 0;
			for (String line : debug) {
				graphics.setColor(Color.black);
				graphics.drawString(line, 6, 16 + 10 * lineNumber);
				graphics.setColor(Color.white);
				graphics.drawString(line, 5, 15 + 10 * lineNumber);
				lineNumber++;
			}
			
			graphics.setColor(Color.red);
			graphics.drawRect((int)paddingX, (int)paddingY, (int)renderWidth, (int)renderHeight);		// Rendering rect
			graphics.setColor(Color.orange);
			graphics.drawRect((int)currentLevelSetting.getViewport().x - (currentLevelSetting.getWidth()/2), (int)currentLevelSetting.getViewport().y - (currentLevelSetting.getHeight()/2), currentLevelSetting.getWidth(), currentLevelSetting.getHeight());
		}

		graphics.dispose();
		panelGraphics.drawImage(canvas, 0, 0, null);
	}

	public void setDebug(boolean doDebug) {
		this.doDebug = doDebug;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		canvas = Discette.toCompatibleImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
	}
}
