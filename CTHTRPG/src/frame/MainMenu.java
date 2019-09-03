package frame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


@SuppressWarnings("serial")
public class MainMenu extends JFrame implements LayoutService {

	
	private JButton kpBtn,plBtn;
	
	public MainMenu(){
		super("TApp ver. 0.1");
		getContentPane().setBackground(Color.white);
		createComponent();
		addComponent();
		addListener();
		setSize(250, 150);
		setVisible(true);
		setLocation(200,100);
		setResizable(false);
		
		
	}
	

	@Override
	public void createComponent() {
		
		kpBtn = new JButton("KP로 접속");
		plBtn = new JButton("PL로 접속");
	}

	@Override
	public void addComponent() {
		
		JPanel portPnl = new JMyPanel(new GridLayout(2,1));
		portPnl.setBorder(new TitledBorder("KP / PL 선택"));
		portPnl.add(kpBtn);
		portPnl.add(plBtn);
		add(portPnl);
		
		
		
	}

	@Override
	public void addListener() {

		addWindowListener(new WindowAdapter() {
			
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				
				System.exit(0);
			}
		});
		
		
		kpBtn.addActionListener(listener);
		
		plBtn.addActionListener(listener);
		
	}
	
	ActionListener listener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(source == kpBtn) {
				
				new SingleChatServer();
				dispose();
				
			}
			else {
				new PL_CharGen();
				dispose();
			}
			
		}
	};
	
	
	@Override
	public Insets getInsets() {
		return new Insets(40,20,20,20);
	}
	
	public static void main(String[] args) {
		new MainMenu();

	}

}
