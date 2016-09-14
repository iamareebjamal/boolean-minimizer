package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class DrawUtils {
	private Graphics2D g;
	public int width = 30;

	public DrawUtils(Graphics2D graphics2D) {
		g = graphics2D;
	}

	public static Color parseColor(String colorStr) {
		return new Color(
					Integer.valueOf(colorStr.substring(1, 3), 16), 
					Integer.valueOf(colorStr.substring(3, 5), 16),
					Integer.valueOf(colorStr.substring(5, 7), 16)
				);
	}
	
	public int getWidth(String text) {
		FontMetrics fm = g.getFontMetrics();
		return (int) fm.getStringBounds(text, g).getWidth();
	}
	
	public void drawCentreText(String text, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		double t_width = fm.getStringBounds(text, g).getWidth();
		g.drawString(text, (int) (x - t_width / 2), (y + fm.getMaxAscent() / 2));
	}
	
	public void drawCell(String s, int x, int y) {
		boolean green = s.equals("1");
		
		g.setColor(DrawUtils.parseColor("#ff9a9a"));
		if(green)
			g.setColor(DrawUtils.parseColor("#dbffb1"));
		
		g.fillRect(x, y, width, 20);
		
		g.setColor(DrawUtils.parseColor("#b80000"));
		
		if(green)
			g.setColor(DrawUtils.parseColor("#549607"));
		
		drawCentreText(s, x+width/2, y+8);
	}
	
	public void drawHeader(String s, int x, int y) {
		g.setColor(DrawUtils.parseColor("#e62b69"));
		g.fillRect(x, y, width, 20);
		g.setColor(DrawUtils.parseColor("#ffbdd3"));
		drawCentreText(s, x+width/2, y+8);
	}
	
	public void drawHeader(char c, int x, int y) {
		g.setColor(DrawUtils.parseColor("#673AB7"));
		g.fillRect(x, y, 30, 20);
		g.setColor(DrawUtils.parseColor("#EDE7F6"));
		drawCentreText(Character.toString(c), x+15, y+8);
	}
	
	public void drawCell(char c, int x, int y) {
		g.setColor(DrawUtils.parseColor("#B39DDB"));
		g.fillRect(x, y, 30, 20);
		g.setColor(DrawUtils.parseColor("#512DA8"));
		drawCentreText(Character.toString(c), x+15, y+8);
	}
}
