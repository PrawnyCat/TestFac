package frame;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class PL_CharGen extends JFrame implements LayoutService {

	
	JTextField[] genResult;
	private JButton rollBtn,connBtn;
	
	
	public PL_CharGen(){
		
		
		
		super("일대일 채팅 클라이언트");
		getContentPane().setBackground(Color.white);
		createComponent();
		addComponent();
		addListener();
		setSize(600, 500);
		setVisible(true);
		setLocation(200,100);
		setResizable(false);
		
		
	}
	

	@Override
	public void createComponent() {
		
		genResult = new JTextField[17];
		
	}

	@Override
	public void addComponent() {
		
		
		
		
		
		
		
		
		
	}

	
	@Override
	public Insets getInsets() {
		return new Insets(40,20,20,20);
	}


	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		
	}
	
	

}
