package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import utils.Utils;

public class KMapPanel extends JPanel {

	private static final long serialVersionUID = 6511270597454282670L;
	
	private DrawUtils drawUtils;
	
	private List<Character> variables;
	private Map<List<Boolean>, Boolean> truth_table;
	
	public KMapPanel(){
		setLayout(new BorderLayout());
		
		JLabel title = new JLabel("KMap View", SwingConstants.CENTER);
		Font font = title.getFont();
		Font bold = new Font(font.getFontName(), Font.BOLD, 18);
		title.setFont(bold);
		title.setOpaque(true);
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setBackground(DrawUtils.parseColor("#ff266e"));
		title.setForeground(new Color(255, 255, 255, 200));
		
		add(title, BorderLayout.NORTH);
		
		KMapDrawPanel kmdp = new KMapDrawPanel();
		kmdp.setPreferredSize(new Dimension(3000, 300));
		
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
	
	class KMapDrawPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			graphics2d.setColor(DrawUtils.parseColor("#ffd8e5"));
			graphics2d.fillRect(0, 0, getWidth(), getHeight());
			
			if(truth_table == null || variables == null)
				return;
			
			drawUtils = new DrawUtils(graphics2d);
			
			
			int size = variables.size();
			int cols = size / 2;
			int rows = size - cols;

			int x;
			int y = 20;
			
			List<List<Boolean>> left = Utils.getGrayCode(rows);
			List<List<Boolean>> up = Utils.getGrayCode(cols);
			
			int step = drawUtils.getWidth(getString(left.get(0))) + 20;
			x=step+62;
			
			drawUtils.width = step;
			
			for (List<Boolean> columns : left) {
				drawUtils.drawHeader(getHeader(columns, cols), x, y);
				x+=step+2;
			}
			
			x=step+62;y+=22;
			for (List<Boolean> columns : left) {
				drawUtils.drawHeader(getString(columns), x, y);
				x+=step+2;
			}
			
			y+=22;
			
			for (List<Boolean> row : up) {
				x=60;
				
				x-=step+2;
				drawUtils.drawHeader(getHeader(row, 0), x, y);
				x+=step+2;
				drawUtils.drawHeader(getString(row), x, y);
				x+=step+2;
				
				for (List<Boolean> iter : left) {
					List<Boolean> key = new ArrayList<Boolean>();
					if(up.size()>1)
						key.addAll(row);
					key.addAll(iter);

					
					drawUtils.drawCell(String.valueOf(toInt(truth_table.get(key))), x, y);
					
					x+=step+2;
				}
				y+=22;
			}
			
		}
		
		private String getHeader(List<Boolean> row, int n){
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < row.size(); i++){
				sb.append(variables.get(i+n));
				if(!row.get(i))
					sb.append('\'');
			}
			return sb.toString();
		}
		
		private String getString(List<Boolean> row) {
			StringBuilder sb = new StringBuilder();
			for(boolean bool : row) {
				sb.append(bool?'1':'0');
			}
			
			return sb.toString();
		}
		
		private int toInt(boolean bool) {
			return bool?1:0;
		}
	}
	
	
}