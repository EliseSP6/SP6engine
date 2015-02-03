package Bleach.InputManager;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public abstract class Receptionist {
	private List<KeyBinding> keyBindings = new ArrayList<>();

	public void addKeyBinding(KeyBinding newKeyBinding) {
		keyBindings.add(newKeyBinding);
	}

	public abstract void handleEvent(ActionEvent event);

	public abstract void handleEvent(MouseEvent event);

	public List<KeyBinding> getKeyBindings() {
		return keyBindings;
	}

	public static class KeyBinding {
		private KeyStroke key;
		private AbstractAction onKeyDown;
		private Object actionMapKey;

		public KeyBinding(KeyStroke key, Object actionMapKey, AbstractAction onKeyDown) {
			this.key = key;
			this.onKeyDown = onKeyDown;
			this.actionMapKey = actionMapKey;
		}

		public KeyStroke getKey() {
			return key;
		}

		public AbstractAction getAction() {
			return onKeyDown;
		}

		public Object getActionMapKey() {
			return actionMapKey;
		}
	}
}
