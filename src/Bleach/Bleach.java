package Bleach;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Bleach.InputManager.Receptionist;

public class Bleach extends JPanel{

	private enum PauseType{								// The game can be paused by many reasons, this is an enumeration of those.
		USER,											// The user used the pause functionality.
		LOADER,											// The loader is working (e.g. save game)
		GAMEMESSAGE										// In-game information is displayed (e.g. a splash-screen is displayed, a book, notepad, messageboard etc is displayed, inventory is displayed)
	}
	
	private JFrame jWindow;								// A handle to the window.
	private double FPS = 60;							// FPS limiter, limits how often the game is rendered.
	private double timePreviousLoop;					// Used for delta-time in the game loop (e.g. FPS limiting)
	private double timePreviousRender;					// Used for delta-time in the rendering (e.g. calculating actual rendering FPS)
	private Map<PauseType, Boolean> pause = new HashMap<>();	// A (set of) bool to see if the game is paused by any subsystem.
	
	public Bleach(){
		
		System.setProperty("sun.java2d.opengl","True");	// Let's try to HW-accelerate stuff.
		
		timePreviousLoop = timePreviousRender = System.nanoTime();
		
		// TODO: attach events
		
	}
	
	public void init(int windowWidth, int windowHeight, String windowTitle){
		/* This sets up the window and starts the game. */
		
		setSize(windowWidth, windowHeight);				// Set the size of this JPanel before inserting it into the window.
		final Bleach EDTpointerToPanel = this;			// This is a pointer to this JPanel used in the Event Dispatch Thread (EDT).
		final String EDTwindowTitle = windowTitle;		// This is the window title variable used in the Event Dispatch Thread (EDT).
		
		SwingUtilities.invokeLater(new Runnable() {
			/* Event Dispatch Thread - prevents potential race conditions that could lead to deadlock. */
            public void run() {
            	jWindow = new JFrame(EDTwindowTitle);
            	jWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	jWindow.setResizable(false);
            	jWindow.add(EDTpointerToPanel);
            	jWindow.pack();
            	//jWindow.pack();						// Fixes a bug that sometimes adds 10 pixels to width and height. Weird stuff.
            	jWindow.setLocationRelativeTo(null);	// Center the window on the primary monitor.
            	jWindow.setVisible(true);
            }
        });
		
		while(!isDisplayable()){
			/*
			 * Wait for EDT to complete.
			 * */
			Thread.yield();
		}
		
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.black);
		
		gameLoop();
	}
	
	public double setFPS(double newFPS){
		/* Sets the FPS, returns the old FPS. */
		double retval = FPS;
		FPS = newFPS;
		return retval;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		double actualFPS = (1000000000.0 / Math.max(1, (System.nanoTime() - timePreviousRender)));
		
		
		// TODO: render
		
		
		
		timePreviousRender = System.nanoTime();
	}
	
	private void gameLoop(){
		
		boolean quit = false;
		boolean paused = false;
		
		while(!quit){
			
			
			
			
			
			/* Check if anything is pausing the game */
			paused = false;
			for (Entry<PauseType, Boolean> entry : pause.entrySet()) {
				if(entry.getValue()){
					paused = true;
					break;
				}
			}
			if(!paused){
				/* The game is not paused, game objects may live. */
				
				
				// TODO: game logic etc
				
				
			}
			
			
			
			
			if(FPS > 0){	// We're limiting the FPS. Check if it's time to render.
				if(System.nanoTime() - timePreviousLoop > (1000000.0 / (FPS / 1000))){
					repaint();
				}
			}else{			// We're not limiting the FPS, render as often as possible.
				repaint();
			}
			timePreviousLoop = System.nanoTime();
		}
	}
}
