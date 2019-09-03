package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class SingleChatServer extends JFrame implements LayoutService {

	private static final String IDENTIF = "^k%@!검#5@t$";
	private static final String FIRST_CONN = "@r?총#2*q^";
	private static final String WHISPER = "*q@?%부^4r#";
	private static final String EXIT_CHAT = "#c/^2$o*";
	
	private JTextArea chat;
	private JTextField message;
	
	//통신용 컴포넌트
	ServerSocket listenSocket;
	Socket socket;
	
	//입출력 필터 스트림
	DataInputStream dis;
	DataOutputStream dos;
	
	//서버용 포트
	public static final int PORT = 9070;
	
	//다중채팅용 Map : String에 아이디, Socket에 해당 아이디를 식별자로 갖는 클라이언트의 socket을 지정.
	String id,chatTxt;
	Map<Socket,DataOutputStream> clientMap;
	Map<String,Socket> socMap;
	StringBuffer chatStr,idStr;
	
	
	
	public SingleChatServer(){
		
		super("일대일 채팅 서버");
		
		getContentPane().setBackground(Color.white);
		createComponent();
		addComponent();
		addListener();
		setSize(600, 500);
		setVisible(true);
		setLocation(200,100);
		setResizable(false);
		
		new ListenSocket(socket).start();
		
		
	}
	
	@Override
	public void createComponent() {
		
		chat = new JTextArea();
		chat.setEditable(false);
		message = new JTextField();
		message.setEditable(false);
		clientMap = new HashMap<Socket,DataOutputStream>();
		chatStr = new StringBuffer();
		idStr = new StringBuffer();
		socMap = new HashMap<String, Socket>();
		
		try {
			listenSocket = new ServerSocket(PORT);
			chat.append("Server Created.\r\n");

			
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "서버 소켓 생성에 실패했습니다.");
		}
		
	}

	@Override
	public void addComponent() {
		JPanel chatPnl = new JMyPanel(new BorderLayout()) {
			@Override
			public Insets getInsets() {
				return new Insets(20,10,10,10);
			}
		};
		chatPnl.setBorder(new TitledBorder("메시지 목록"));
		chatPnl.add(new JScrollPane(chat));
		
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
				message.requestFocus();
			}
			
			@Override
			public void windowClosing(WindowEvent e) {

				System.exit(0);
			}
		});
		
		message.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				if(socket!=null) {
					/*
					Set keys = clientMap.keySet();
					for(Object key : keys) {
						try {
							
							dos.writeUTF(message.getText());
								
							chat.append("Server> "+message.getText()+"\r\n");
							
							
						} catch (IOException e1) {
							chat.append("연결이 끊어졌습니다.\r\n");
							e1.printStackTrace();
						}finally {
							autoScroll(chat);
							message.setText("");
							message.requestFocus();
						}
					}
					*/
				}
			}
		});
		
	}
	
	class ListenSocket extends Thread {
		
		Socket socket;
		
		public ListenSocket(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			listen(socket);
			
		}

		private void listen(Socket socket) {
			if(listenSocket!=null) {
				while(true) {
					try {
						socket = listenSocket.accept();
						dis = new DataInputStream(socket.getInputStream());
						dos = new DataOutputStream(socket.getOutputStream());
						String tempId=dis.readUTF();
						id = tempId.substring(0,tempId.indexOf(FIRST_CONN));

						clientMap.put(socket,dos);
						socMap.put(id,socket);
						chat.append(id+" 님이 입장하셨습니다.\r\n");
						Set keys = clientMap.keySet();
						Set iKeys = socMap.keySet();
						for(Object key:iKeys){
							idStr.append(key+"  ");
						}
						for(Object key:keys){
							if(!key.equals(socket)) {
								
								clientMap.get(key).writeUTF(id+" 님이 입장하셨습니다."+FIRST_CONN+"  "+idStr.toString());
							}
							else {
								clientMap.get(key).writeUTF(FIRST_CONN+"  "+idStr.toString());
							}
						}
						idStr.delete(0, idStr.length());
						new ReadSocket(socket,dis,dos).start();
						
						
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(SingleChatServer.this, "클라이언트 연결에 실패했습니다.");
					}
				}
			}
		}
	}
	
	class ReadSocket extends Thread {
		
		Socket socket;
		DataOutputStream dos;
		DataInputStream dis;
		
		public ReadSocket(Socket socket,DataInputStream dis,DataOutputStream dos) {
			this.socket = socket;
			this.dis=dis;
			this.dos=dos;
		}

		@Override
		public void run() {
			
			read(socket,dis,dos);
		
		}


		private void read(Socket socket,DataInputStream dis,DataOutputStream dos) {
			
			if(socket!=null) {
				
				while(true) {
						
					try {
						
						chatStr.append(dis.readUTF());
						if(chatStr.toString().contains(IDENTIF)) {
							if(!chatStr.toString().contains(WHISPER)) {
								id=chatStr.toString().substring(0,chatStr.toString().indexOf(IDENTIF));
								chatTxt = chatStr.toString().substring(
									chatStr.toString().lastIndexOf(IDENTIF)+IDENTIF.length());
								chat.append(id+"> "+chatTxt+"\r\n");
								castMessage(chatStr,socket);
								
							}else {
								
								id=chatStr.toString().substring(0,chatStr.toString().indexOf(IDENTIF));
								String toId = chatStr.toString().substring(chatStr.toString().indexOf(WHISPER)+WHISPER.length());
								chatTxt = chatStr.toString().substring(
										chatStr.toString().lastIndexOf(IDENTIF)+IDENTIF.length(),chatStr.indexOf(WHISPER));
								clientMap.get(socMap.get(id)).writeUTF("귓속말 > "+chatTxt);
								clientMap.get(socMap.get(toId)).writeUTF(id+" 님의 귓속말 > "+chatTxt);
								chat.append(id+" 님이 "+toId+" 님에게 보낸 귓속말 > "+chatTxt+"\r\n");
							}
						}else if(chatStr.toString().contains(EXIT_CHAT)) {
							id=chatStr.toString().split(" ")[0];
							Set iKeys = socMap.keySet();
							Set keys = clientMap.keySet();
							for(Object obj:iKeys) {
								if(id.equals(obj.toString())) {
									socMap.remove(id);
									break;
								}
							}
							for(Object obj:iKeys) {
								idStr.append(obj+"  ");
							}
							for(Object key:keys){
								clientMap.get(key).writeUTF(FIRST_CONN+"  "+idStr.toString());
							}
							idStr.delete(0, idStr.length());
							chat.append(id+" 님이 접속을 종료했습니다.\r\n");
							castMessage(chatStr,socket);
						}
						
						
					}
					catch(IOException e1){
						chat.append(id+" 님의 접속이 끊어졌습니다.\r\n");
						break;
					}finally {
						id="";
						chatTxt="";
						chatStr.delete(0, chatStr.length());
						autoScroll(chat);
							
					}
						
					
				}
				
			}
			
		}
		
	}
	
	public void castMessage(StringBuffer chatStr,Socket socket) throws IOException {
		Set keys = clientMap.keySet();
		for(Object key:keys){
			if(!key.equals(socket)&&chatStr.toString().contains(IDENTIF))
				
				clientMap.get(key).
				writeUTF(id+"> "+chatTxt);
			else if(!key.equals(socket)) {
				clientMap.get(key).
				writeUTF(chatStr.toString());
			}
		}
	}
	
	public static void autoScroll(JTextArea textArea) {
		
		int length = textArea.getText().length();
		
		textArea.setCaretPosition(length);
		//textArea.requestFocus();
		
	}
	
	
	@Override
	public Insets getInsets() {
		return new Insets(40,20,20,20);
	}
	
	
	

}
