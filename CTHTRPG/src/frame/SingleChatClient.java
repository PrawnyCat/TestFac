package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;


public class SingleChatClient extends JFrame implements LayoutService {

	private static final String IDENTIF = "^k%@!검#5@t$";
	private static final String FIRST_CONN = "@r?총#2*q^";
	private static final String WHISPER = "*q@?%부^4r#";
	private static final String EXIT_CHAT = "#c/^2$o*";
	
	JTextArea chat;
	JTextField message,port,id;
	private JButton connBtn;
	JList<String> idList;
	
	
	Socket socket;
	
	DataInputStream dis;
	DataOutputStream dos;
	
	
	StringBuffer tempTxt,tempIdList;
	String idStr;
	Vector<String> idVec;
	
	public SingleChatClient(){
		
		
		
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
		
		chat = new JTextArea();
		chat.setEditable(false);
		port = new JTextField("127.0.0.1",10);
		message = new JTextField();
		id = new JTextField(10);
		connBtn = new JButton("로그인");
		idList = new JList<String>();
		idVec = new Vector<String>();
		tempTxt = new StringBuffer();
	}

	@Override
	public void addComponent() {
		
		JPanel portPnl = new JMyPanel(new FlowLayout(FlowLayout.LEFT));
		portPnl.setBorder(new TitledBorder("서버 접속"));
		portPnl.add(new JLabel("서버 주소"));
		portPnl.add(port);
		portPnl.add(new JLabel("아이디"));
		portPnl.add(id);
		portPnl.add(connBtn);
		add("North",portPnl);
		
		JPanel chatPnl = new JMyPanel(new BorderLayout()) {
			@Override
			public Insets getInsets() {
				return new Insets(20,10,10,10);
			}
		};
		chatPnl.setBorder(new TitledBorder("메시지 목록"));
		chatPnl.add(new JScrollPane(chat));
		JPanel listPnl = new JMyPanel(new BorderLayout());
		listPnl.setPreferredSize(new Dimension(150,150));
		idList.setBorder(new TitledBorder("접속자 명단"));
		idList.setListData(idVec);
		idList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPnl.add(new JScrollPane(idList));
		chatPnl.add("East",listPnl);

		
		add(chatPnl);
		
		
		
		
		
		
		JPanel messPnl = new JMyPanel(new BorderLayout()) {
			@Override
			public Insets getInsets() {
				return new Insets(20,10,10,10);
			}
		};
		
		messPnl.setBorder(new TitledBorder("메시지 입력"));
		messPnl.add(message);
		
		add("South",messPnl);
		
		
	}

	@Override
	public void addListener() {

		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				
				id.requestFocus();
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(socket!=null) {
					try {
						dos.writeUTF(idStr+" 님이 접속을 종료했습니다."+EXIT_CHAT);
					} catch (IOException e1) {
						chat.append("서버와 연결되어있지 않습니다.\r\n");
						e1.printStackTrace();
					}
				}
				dispose();
			}
		});
		
		
		connBtn.addActionListener(listener);
		
		message.addActionListener(listener);
		
		
	}
	
	ActionListener listener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(source == connBtn&&socket!=null) {
				JOptionPane.showMessageDialog(SingleChatClient.this, "이미 로그인되어 있습니다.");
				return;
			}
			else if(source == connBtn) {
				
				try {
				
					socket = new Socket(port.getText().trim(),SingleChatServer.PORT);
					
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
					idStr=id.getText().trim();
					
					dos.writeUTF(idStr+FIRST_CONN);
					id.setEnabled(false);
					chat.append("서버에 연결되었습니다.\r\n");
					new ReadSocket(socket).start();
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(SingleChatClient.this, "서버 연결에 실패했습니다.");
					e1.printStackTrace();
				}
				
			}else {
				
				if(socket!=null) {
					
					try {
						
						tempTxt.append(message.getText());
						if(!idList.isSelectionEmpty()) {
							tempTxt.append(WHISPER+idList.getSelectedValue());
						}
						else {
							chat.append("> "+tempTxt.toString()+"\r\n");
						}
						dos.writeUTF(idStr+IDENTIF+tempTxt.toString());
						tempTxt.delete(0,tempTxt.length());
						
					} catch (IOException e1) {
						chat.append("연결이 끊어졌습니다.\r\n");
						e1.printStackTrace();
					}finally {
						message.setText("");
						message.requestFocus();
					}
					
					
				}
			}
			
		}
	};
	
	class ReadSocket extends Thread {
		
		Socket socket;
		
		public ReadSocket(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			
			read(socket);
		
		}

		private void read(Socket socket) {
			
			if(socket!=null) {
				while(true) {
					try {
						
						tempTxt.append(dis.readUTF());
						if(tempTxt.toString().contains(FIRST_CONN)) {
							idList.removeAll();
							idVec.clear();
							String[] idLst = tempTxt.toString().split("  ");
							for(int i=1;i<idLst.length;i++) {
								if(!idLst[i].equals(id.getText())) {
									idVec.add(idLst[i]);
								}
								
							}
							
							idList.setListData(idVec);
							
							tempTxt.delete(tempTxt.toString().indexOf(FIRST_CONN),tempTxt.length());
							
							
						}else if(tempTxt.toString().contains(EXIT_CHAT)) {
							tempTxt.delete(tempTxt.toString().indexOf(EXIT_CHAT),tempTxt.length());
						}
						
						if(tempTxt.toString().trim().length()!=0&&!tempTxt.toString().contains(WHISPER)) {
							chat.append(tempTxt.toString()+"\r\n");
						}
						tempTxt.delete(0,tempTxt.length());
					}
					catch(Exception e1){
						chat.append("서버와 연결이 종료됐습니다.\r\n");
						e1.printStackTrace();
						break;
					}finally {
						
						SingleChatServer.autoScroll(chat);
					}
				}
			}
		}
		
	}
	
	@Override
	public Insets getInsets() {
		return new Insets(40,20,20,20);
	}
	
	

}
