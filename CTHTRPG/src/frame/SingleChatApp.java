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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class SingleChatApp extends JFrame implements LayoutService {

	
	private JButton serBtn,cliBtn;
	boolean servOpn = false;
	
	public SingleChatApp(){
		super("채팅 어플리케이션");
		getContentPane().setBackground(Color.white);
		createComponent();
		addComponent();
		addListener();
		setSize(300, 250);
		setVisible(true);
		setLocation(200,100);
		setResizable(false);
		
		
	}
	

	@Override
	public void createComponent() {
		
		serBtn = new JButton("서버 작동");
		cliBtn = new JButton("클라이언트 작동");
	}

	@Override
	public void addComponent() {
		
		JPanel portPnl = new JMyPanel(new GridLayout(2,1));
		portPnl.setBorder(new TitledBorder("어플리케이션 선택"));
		portPnl.add(serBtn);
		portPnl.add(cliBtn);
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
		
		
		serBtn.addActionListener(listener);
		
		cliBtn.addActionListener(listener);
		
	}
	
	ActionListener listener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(source == serBtn&&!servOpn) {
				
				new SingleChatServer();
				servOpn = true;
				
			}else if(source == serBtn&&servOpn) {
				JOptionPane.showMessageDialog(SingleChatApp.this, "서버가 이미 열려있습니다.");
				return;
			}
			else {
				new SingleChatClient();
			}
			
		}
	};
	
	
	@Override
	public Insets getInsets() {
		return new Insets(40,20,20,20);
	}
	
	public static void main(String[] args) {
		new SingleChatApp();

	}

}
