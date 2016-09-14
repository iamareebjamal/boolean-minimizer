import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import gui.DrawUtils;
import gui.KMapPanel;
import gui.TruthTablePanel;
import parser.Parser;
import parser.Simplify;

public class Main {
	
	private static  JFrame j = new JFrame();
	private static JTextPane jtp = new JTextPane();
	private static JTextPane messages = new JTextPane();
	
	private static void createGUI(){
		final TruthTablePanel ttp = new TruthTablePanel();
    	final KMapPanel kmp = new KMapPanel();
    	
		final Parser parser = new Parser();
        
		j.add(ttp, BorderLayout.WEST);
		j.add(kmp, BorderLayout.CENTER);
		
		messages.setEditable(false);
		
		final JPanel messagePanel = getMessagePanel();
		
		jtp.setEditable(false);
		j.add(jtp, BorderLayout.SOUTH);
		
		final JTextField jtf = new JTextField();
		j.add(jtf, BorderLayout.NORTH);
		j.setSize(new Dimension(700, 500));
		
		jtf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parser.setExpression(jtf.getText());
				parser.generateTable();
				
				List<Character> variables = parser.getVariables();
				Map<List<Boolean>, Boolean> truth_table = parser.getTruthTable();
				
				ttp.setVariables(variables);
		        ttp.setTable(truth_table);
		        kmp.setVariables(variables);
		        kmp.setTable(truth_table);
		        
		        Simplify simplify = Simplify.withVariables(variables).ofTable(truth_table);
		        String simplified = simplify.getExpression();
		        jtp.setText("Simplified Expression : " + simplified);
		        
		        messages.setText(simplify.toString());
		        j.add(messagePanel, BorderLayout.EAST);
		        if(messages.getText().isEmpty())
		        	messagePanel.setVisible(false);
		        else
		        	messagePanel.setVisible(true);
			}
		});
    }
	
	public static JPanel getMessagePanel(){
		JPanel jpanel = new JPanel();
		jpanel.setLayout(new BorderLayout());
		
		messages.setBackground(DrawUtils.parseColor("#90A4AE"));
		messages.setForeground(DrawUtils.parseColor("#ECEFF1"));
		messages.setFont(new Font("Arial", Font.PLAIN, 16));
		final JScrollPane jsp = new JScrollPane(messages);
		jsp.setPreferredSize(new Dimension(150, 490));
		jsp.setViewportBorder(null);
		jsp.setBorder(null);
		
		jpanel.setBorder(null);
		
		
		JLabel title = new JLabel("Steps", SwingConstants.CENTER);
		Font font = title.getFont();
		Font bold = new Font(font.getFontName(), Font.BOLD, 18);
		title.setFont(bold);
		title.setOpaque(true);
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setBackground(DrawUtils.parseColor("#607D8B"));
		title.setForeground(new Color(255, 255, 255, 200));
		
		jpanel.add(title, BorderLayout.NORTH);
		jpanel.add(jsp);
		
		return jpanel;
	}
	

    public static void main(String... args) {
    	
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

		
		j.setLayout(new BorderLayout());
		j.setTitle("Boolean Minimizer");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	createGUI();
		    }
		    	
		});
		
    }
    
}