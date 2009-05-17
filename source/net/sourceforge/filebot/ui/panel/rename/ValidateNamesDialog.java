
package net.sourceforge.filebot.ui.panel.rename;


import static net.sourceforge.filebot.FileBotUtilities.*;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;
import net.sourceforge.filebot.ResourceManager;
import net.sourceforge.tuned.ui.TunedUtilities;


class ValidateNamesDialog extends JDialog {
	
	private final List<String> source;
	private String[] validatedValues;
	
	private boolean cancelled = true;
	
	protected final JList list;
	
	protected final Action validateAction = new ValidateAction();
	protected final Action continueAction = new ContinueAction();
	protected final Action cancelAction = new CancelAction();
	
	
	public ValidateNamesDialog(Window owner, List<String> source) {
		super(owner, "Invalid Names", ModalityType.DOCUMENT_MODAL);
		
		this.source = source;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		list = new JList(source.toArray());
		list.setEnabled(false);
		
		list.setCellRenderer(new HighlightListCellRenderer(INVALID_CHARACTERS_PATTERN, new CharacterHighlightPainter(new Color(0xFF4200), new Color(0xFF1200)), 4));
		
		JLabel label = new JLabel("Some names contain invalid characters:");
		
		JComponent c = (JComponent) getContentPane();
		
		c.setLayout(new MigLayout("insets dialog, nogrid, fill"));
		
		c.add(label, "wrap");
		c.add(new JScrollPane(list), "grow, wrap 2mm");
		
		c.add(new JButton(validateAction), "align center");
		c.add(new AlphaButton(continueAction), "gap related");
		c.add(new JButton(cancelAction), "gap 12mm");
		
		setSize(365, 280);
		setLocation(TunedUtilities.getPreferredLocation(this));
		
		TunedUtilities.installAction(c, KeyStroke.getKeyStroke("released ESCAPE"), cancelAction);
	}
	

	public boolean isCancelled() {
		return cancelled;
	}
	

	private void finish(boolean cancelled) {
		this.cancelled = cancelled;
		
		setVisible(false);
		dispose();
		
		if (validatedValues != null && !cancelled) {
			// update source list
			for (int i = 0; i < validatedValues.length; i++) {
				source.set(i, validatedValues[i]);
			}
		}
	}
	
	
	private class ValidateAction extends AbstractAction {
		
		public ValidateAction() {
			super("Validate", ResourceManager.getIcon("dialog.continue"));
			putValue(SHORT_DESCRIPTION, "Remove invalid characters");
		}
		

		@Override
		public void actionPerformed(ActionEvent e) {
			validatedValues = new String[source.size()];
			
			for (int i = 0; i < validatedValues.length; i++) {
				validatedValues[i] = validateFileName(source.get(i));
			}
			
			setEnabled(false);
			
			continueAction.putValue(SMALL_ICON, getValue(SMALL_ICON));
			continueAction.putValue(ContinueAction.ALPHA, 1.0f);
			
			// update displayed values
			list.setModel(new AbstractListModel() {
				
				@Override
				public Object getElementAt(int i) {
					return validatedValues[i];
				}
				

				@Override
				public int getSize() {
					return validatedValues.length;
				}
			});
		}
	};
	

	private class ContinueAction extends AbstractAction {
		
		public static final String ALPHA = "Alpha";
		
		
		public ContinueAction() {
			super("Continue", ResourceManager.getIcon("dialog.continue.invalid"));
			putValue(ALPHA, 0.75f);
		}
		

		public void actionPerformed(ActionEvent e) {
			finish(false);
		}
	};
	

	protected class CancelAction extends AbstractAction {
		
		public CancelAction() {
			super("Cancel", ResourceManager.getIcon("dialog.cancel"));
		}
		

		public void actionPerformed(ActionEvent e) {
			finish(true);
		}
	};
	

	protected static class AlphaButton extends JButton {
		
		private float alpha;
		
		
		public AlphaButton(Action action) {
			super(action);
			alpha = getAlpha(action);
		}
		

		@Override
		protected void actionPropertyChanged(Action action, String propertyName) {
			super.actionPropertyChanged(action, propertyName);
			
			if (propertyName.equals(ContinueAction.ALPHA)) {
				alpha = getAlpha(action);
			}
		}
		

		private float getAlpha(Action action) {
			Object value = action.getValue(ContinueAction.ALPHA);
			
			if (value instanceof Float) {
				return (Float) value;
			}
			
			return 1.0f;
		}
		

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
			super.paintComponent(g2d);
		}
	}
	
	
	public static boolean showDialog(Component parent, List<String> source) {
		ValidateNamesDialog dialog = new ValidateNamesDialog(TunedUtilities.getWindow(parent), source);
		
		dialog.setVisible(true);
		
		return !dialog.isCancelled();
	}
}
