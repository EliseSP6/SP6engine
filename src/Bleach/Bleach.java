package Bleach;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Bleach.InputManager.Receptionist;
import Bleach.InputManager.Receptionist.KeyBinding;
import Bleach.Loader.Discette;
import Bleach.PhysicsEngine.Physique;
import Bleach.Renderer.Picasso;

public class Bleach extends JPanel {
	/**
	 * The game can be paused by many reasons, this is an enumeration of those.
	 **/
	private enum PauseType {
		// The user used the pause functionality.
		USER,

		// The loader is working (e.g. save game)
		LOADER,

		// In-game information is displayed (e.g. a splash-screen is displayed,
		// a book, notepad, messageboard etc is displayed, inventory is
		// displayed)
		GAMEMESSAGE
	}

	// A handle to the window.
	private JFrame jWindow;
	private int winWidth;
	private int winHeight;
	private String winTitle;

	// FPS limiter, limits how often the game is rendered.
	private double FPS = 60;

	// Used for delta-time in the game loop (e.g. FPS limiting)
	private double timePreviousLoop;

	// Used for delta-time in the rendering (e.g. calculating actual rendering
	// FPS)
	private double timePreviousRender;

	// A (set of) bool to see if the game is paused by any subsystem.
	private Map<PauseType, Boolean> pause = new HashMap<>();

	// All the levels.
	private Map<String, Level> levels = new HashMap<>();

	// Pointer to the active level.
	private Level activeLevel;
	private Picasso renderer;
	private long timeDebug;
	private Receptionist receptionist = null;

	public Bleach() {

		// Let's try to HW-accelerate stuff.
		System.setProperty("sun.java2d.opengl", "True");

		timePreviousLoop = timePreviousRender = System.currentTimeMillis();
		winWidth = 800; // Default width
		winHeight = 600; // Default height
		winTitle = "Game window"; // Default title;
	}

	public void init(int windowWidth, int windowHeight, String windowTitle) {
		winWidth = windowWidth;
		winHeight = windowHeight;
		winTitle = windowTitle;
		init();
	}

	/**
	 * This sets up the window and starts the game. *
	 **/
	public void init() {

		// Set the size of this JPanel before inserting it into the window.
		setSize(winWidth, winHeight);

		// Sometimes setSize() just fails. Go figure.
		setPreferredSize(new Dimension(winWidth, winHeight));

		// This is a pointer to this JPanel used in the Event Dispatch Thread
		// (EDT).
		final Bleach EDTpointerToPanel = this;

		// This is the window title variable used in the Event Dispatch Thread
		// (EDT).
		final String EDTwindowTitle = winTitle;

		SwingUtilities.invokeLater(new Runnable() {
			/*
			 * Event Dispatch Thread - prevents potential race conditions that
			 * could lead to deadlock.
			 */
			@Override
			public void run() {
				jWindow = new JFrame(EDTwindowTitle);
				jWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jWindow.setResizable(false);
				jWindow.add(EDTpointerToPanel);

				// Fixes a bug that sometimes adds 10 pixels to width and
				// height. Weird stuff.
				jWindow.pack();
				jWindow.pack();

				// Center the window on the primary monitor.
				jWindow.setLocationRelativeTo(null);

				jWindow.setVisible(true);
			}
		});

		while (!isDisplayable()) {
			/*
			 * Wait for EDT to complete.
			 */
			Thread.yield();
		}

		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.cyan);

		renderer = new Picasso(winWidth, winHeight);
	}

	public void loadImages(String assetJsonPath) {
		Discette.loadImages(assetJsonPath);
	}

	public void loadSounds(String assetJsonPath) {
		Discette.loadSound(assetJsonPath);
	}
	
	public Discette.JsonObjectLevel loadLevel(String assetJsonPath){
		return Discette.loadLevel(assetJsonPath);
	}

	public double setFPS(double newFPS) {
		/* Sets the FPS, returns the old FPS. */
		double retval = FPS;
		FPS = newFPS;
		return retval;
	}

	public void setTitle(String title) {
		winTitle = title;
	}

	public void addLevel(Level level) {
		if (level != null) {
			levels.put(level.getKey(), level);

			// No active level has been set, let's set it to this one.
			if (activeLevel == null)
				activeLevel = level;
		}
	}

	public BufferedImage getTexture(String key) {
		Sprite sprite = Discette.getImage(key);

		return sprite == null ? null : sprite.getFrame();
	}

	public Sprite getSprite(String key) {
		return Discette.getImage(key);
	}

	private boolean setActiveLevel(String key) {
		Level newLevel = null;
		newLevel = levels.get(key);
		if (newLevel != null)
			activeLevel = newLevel;

		return newLevel != null;
	}

	@Override
	public void paintComponent(Graphics g) {
		double deltaTime = System.currentTimeMillis() - timePreviousRender;

		if (FPS > 0 && deltaTime < 1000.0 / FPS)
			return;

		double actualFPS = (1000.0 / Math.max(1, (deltaTime)));

		timeDebug += deltaTime;
		if (timeDebug >= 1000) {
			timeDebug = 0;
			renderer.clearDebugLines();
			// renderer.addDebugLine("FPS: " + new
			// Double(actualFPS).toString().substring(0, 5));
			renderer.addDebugLine("FPS: " + (int) actualFPS);
		}

		renderer.render(g, activeLevel);

		timePreviousRender = System.currentTimeMillis();
	}

	private boolean isPaused() {
		/* Check if any subsystem is pausing the game */
		for (Entry<PauseType, Boolean> entry : pause.entrySet()) {
			if (entry.getValue()) {
				return true;
			}
		}

		return false;
	}

	public void run() {
		gameLoop();
	}

	private void gameLoop() {

		boolean quit = false;
		boolean paused = false;
		double deltaTime;

		while (!quit) {
			deltaTime = System.currentTimeMillis() - timePreviousLoop;

			// Simulate work
			while (System.currentTimeMillis() - timePreviousLoop < 34) {
				Thread.yield();
			}

			if (!isPaused()) {
				/* Physics engine */
				Physique.step(activeLevel);

				
				/* Mobiles heartbeat */
				for (EntityTranslatable mob : activeLevel.getMobiles()) {
					((Entity) mob).tick(activeLevel);
				}

				/* Player Heartbeat */
				for (EntityTranslatable player : activeLevel.getPlayers()) {
					Entity p = ((Entity)player);
					p.tick(activeLevel);
					activeLevel.focusEntity(p, true);
				}

			}

			// if(FPS > 0){ // We're limiting the FPS. Check if it's time to
			// render.
			// if(System.nanoTime() - timePreviousRender > (1000000.0 / (FPS /
			// 1000))){
			// repaint();
			// }
			// }else{ // We're not limiting the FPS, render as often as
			// possible.
			// repaint();
			// }
			// repaint();
			paintComponent(this.getGraphics());
			timePreviousLoop = System.currentTimeMillis();
		}
	}

	public void addReceptionist(Receptionist receptionist) {
		this.receptionist = receptionist;

		for (KeyBinding keyBinding : receptionist.getKeyBindings()) {
			this.getInputMap().put(keyBinding.getKey(), keyBinding.getActionMapKey());
			this.getActionMap().put(keyBinding.getActionMapKey(), keyBinding.getAction());
		}

		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent event) {
				Bleach.this.receptionist.handleEvent(event);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// Ignore
			}
		});

	}
}
