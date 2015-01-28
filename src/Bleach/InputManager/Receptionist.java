package Bleach.InputManager;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Receptionist {
	public void handleEvent(ActionEvent event);

	public void handleEvent(MouseEvent event);

	public void handleEvent(KeyEvent event);
}
