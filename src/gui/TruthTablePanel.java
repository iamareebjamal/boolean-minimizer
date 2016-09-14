package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class TruthTablePanel extends JPanel {
	
	private static final long serialVersionUID = -6031579170943998924L;

	private DrawUtils drawUtils;
	
	private List<Character> variables;
	private Map<List<Boolean>, Boolean> truth_table;
	
	public TruthTablePanel() {
		setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Truth Table View", SwingConstants.CENTER);
		Font font = title.getFont();
		Font bold = new Font(font.getFontName(), Font.BOLD, 18);
		title.setFont(bold);
		title.setOpaque(true);
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setBackground(DrawUtils.parseColor("#a126ff"));
		title.setForeground(new Color(255, 255, 255, 200));
		
		add(title, BorderLayout.NORTH);
		
		TruthTableDrawPanel kmdp = new TruthTableDrawPanel();
		kmdp.setPreferredSize(new Dimension(500, 1000));
		
		JScrollPane jsp = new JScrollPane(kmdp);
		jsp.setPreferredSize(new Dimension(250, 500));
		jsp.setViewportBorder(null);
		jsp.setBorder(null);
		add(jsp);
	}
	
	public void setVariables(List<Character> vars){
		this.variables = vars;
	}
	
	public void setTable(Map<List<Boolean>, Boolean> table){
		truth_table = table;
		revalidate();
		repaint();
	}
	
	class TruthTableDrawPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			

			graphics2d.setColor(DrawUtils.parseColor("#EDE7F6"));
			graphics2d.fillRect(0, 0, getWidth(), getHeight());
			
			if(truth_table == null || variables == null)
				return;
			
			drawUtils = new DrawUtils(graphics2d);
			
			int x = 20;
			int y = 20;
			
			for (char c : variables){
				drawUtils.drawHeader(c, x, y);
				x+=32;
			}
			
			drawUtils.drawHeader('F', x+10, y);
			
			y+=22;

			for (List<Boolean> minterm : truth_table.keySet()) {
				x=20;
				for (Boolean bool : minterm){
					drawUtils.drawCell(toChar(bool), x, y);
					x+=32;
				}
				drawUtils.drawCell(toChar(truth_table.get(minterm)), x+10, y);
				y+=22;
			}
			
			if(y>1000)
				setPreferredSize(new Dimension(500, y+200));
			
		}
		
		private char toChar(boolean bool) {
			return bool?'1':'0';
		}
	}
}
