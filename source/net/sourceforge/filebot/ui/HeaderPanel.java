
package net.sourceforge.filebot.ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.sourceforge.filebot.ResourceManager;
import net.sourceforge.tuned.ui.GradientStyle;
import net.sourceforge.tuned.ui.notification.SeparatorBorder;
import net.sourceforge.tuned.ui.notification.SeparatorBorder.Position;


class HeaderPanel extends JComponent {
	
	private JLabel titleLabel = new JLabel();
	
	private float[] gradientFractions = { 0.0f, 0.5f, 1.0f };
	private Color[] gradientColors = { new Color(0xF6F6F6), new Color(0xF8F8F8), new Color(0xF3F3F3) };
	
	
	public HeaderPanel() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setOpaque(false);
		
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		titleLabel.setOpaque(false);
		titleLabel.setForeground(new Color(0x101010));
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
		
		int margin = 9;
		titleLabel.setBorder(new EmptyBorder(margin - 1, 90, margin + 1, 0));
		
		JLabel decorationLabel = new JLabel(ResourceManager.getIcon("decoration.header"));
		decorationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		decorationLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		centerPanel.setBorder(new EmptyBorder(0, 78, 0, 0));
		centerPanel.add(decorationLabel, BorderLayout.EAST);
		centerPanel.add(titleLabel, BorderLayout.CENTER);
		
		add(centerPanel, BorderLayout.CENTER);
		
		setBorder(new SeparatorBorder(1, new Color(0xB4B4B4), new Color(0xACACAC), GradientStyle.LEFT_TO_RIGHT, Position.BOTTOM));
	}
	

	public void setTitle(String title) {
		titleLabel.setText(title);
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		LinearGradientPaint paint = new LinearGradientPaint(0, 0, getWidth(), 0, gradientFractions, gradientColors);
		
		g2d.setPaint(paint);
		g2d.fill(getBounds());
	}
	
}
