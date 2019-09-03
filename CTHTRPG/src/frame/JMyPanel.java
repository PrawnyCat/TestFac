package frame;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JMyPanel extends JPanel {
	
	public JMyPanel() {
		this.setBackground(Color.white);
	}
	
	public JMyPanel(LayoutManager mgr) {
		
		super(mgr);
		this.setBackground(Color.white);
		
	}
	
}
