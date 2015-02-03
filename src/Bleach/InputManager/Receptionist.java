package Bleach.InputManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public abstract class Receptionist {
	private static InputMap keyMap = new InputMap();
	private static ActionMap actionMap = new ActionMap();
	
	public void addKeyBinding(KeyStroke key, Object object, ActionListener onKeyPushed){
		keyMap.put(key, object);
		actionMap.put(object, (Action) onKeyPushed);
	}
	
	public abstract void handleEvent(ActionEvent event);

	public abstract void handleEvent(MouseEvent event);
}
