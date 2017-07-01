package ui;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import rmi.RemoteHelper;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextPane txtpnEditor;
	private JTextPane txtpnInput;
	private JTextPane textPnOutput;
	
	private JTable memoryTable;
	private String memList[][];
	
	private JLabel labelUserName;
		
	private boolean isLogin=false;
	private String account,password;
	
	private static RemoteHelper remoteHelper;
	
	ArrayList<CodeVersion> historyCode = new ArrayList<CodeVersion>();
	private int currentVersion=0;
	private int topVersion=0;
	private boolean isReady=false;
	private boolean isUserChange=true;
	
	// execute
	private void pressRun(){
		try {
			setOutputText(remoteHelper.getExecuteService().execute(getEditorCode(), getInputText()));
			updateTable();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
// --------------------------  login logout ----------------------------------------
	
	private void login(){		
		if(isLogin){
			JOptionPane.showMessageDialog(null, "You are now login as " + account + ".", "Already login", JOptionPane.WARNING_MESSAGE);
		}
		while(!isLogin){
			account=JOptionPane.showInputDialog(null,"Input your account:");
			password=JOptionPane.showInputDialog(null,"Input your password:");
			try {
				isLogin=remoteHelper.getUserService().login(account, password);
				if(isLogin){
					JOptionPane.showMessageDialog(null, "You are now login as " + account + ".", "Success", JOptionPane.WARNING_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(null, "The account or password is incorrect.\r\nPlease try again.", "Login fail", JOptionPane.WARNING_MESSAGE);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			labelUserName.setText(account);
		}		
		
	}
	
	private void logout(){
		isLogin=false;
		account=null;
		password=null;
		labelUserName.setText("Guest");
		JOptionPane.showMessageDialog(null, "Logout successful.", "Logout", JOptionPane.WARNING_MESSAGE);
	}

// --------------------------------------------------------------------
	

// ---------------------------- code history ----------------------------------

	private void initCodeHistory(){
		CodeVersion cv = new CodeVersion(txtpnEditor.getText(),0,0);
		historyCode.add(0,cv);	
		isReady=true;
	}
	
	private void codeChange(){
		if(!isReady || !isUserChange)
			return;
		++topVersion;
		CodeVersion cv = new CodeVersion(txtpnEditor.getText(),topVersion,currentVersion);
		historyCode.get(currentVersion).setNextVersion(topVersion);
		historyCode.add(topVersion,cv);	
		currentVersion=cv.getVersion();
	}
	
	private void undo(){
		isUserChange=false;
		CodeVersion currentCodeVersion = historyCode.get(currentVersion);
		CodeVersion preCodeVersion = historyCode.get(currentCodeVersion.getPreVersion());
		txtpnEditor.setText(preCodeVersion.getCode());
		currentVersion=preCodeVersion.getVersion();	
		isUserChange=true;
	}
	
	private void redo(){
		isUserChange=false;
		CodeVersion currentCodeVersion = historyCode.get(currentVersion);
		if(currentCodeVersion.getNextVersion()==-1)
			return;		
		CodeVersion nextCodeVersion = historyCode.get(currentCodeVersion.getNextVersion());
		txtpnEditor.setText(nextCodeVersion.getCode());
		currentVersion=nextCodeVersion.getVersion();
		isUserChange=true;
	}

// ---------------------------------------------------------------------
	
// ---------------------------- code IO --------------------------------------
	
	private void checkAllCode(){
		try {
			String[] files = remoteHelper.getIOService().readFileList(account).split(",");
			StringBuffer sb = new StringBuffer();
			sb.append("Select a file: \r\n");
			for(int i=0;i<files.length;++i){
				sb.append(i);
				sb.append(":");
				sb.append(files[i]);
				sb.append("\r\n");
			}
			int selectFile;
			do{
				selectFile = Integer.parseInt(JOptionPane.showInputDialog(null,sb.toString()));
			}while(selectFile<0 || selectFile>files.length);
			
			txtpnEditor.setText(remoteHelper.getIOService().readFile(account, files[selectFile]));
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveCode(){
		if(!isLogin)
			JOptionPane.showMessageDialog(null, "Please login!", "", JOptionPane.WARNING_MESSAGE);
		String fileName=JOptionPane.showInputDialog(null,"Input a file name: ");
		try {
			remoteHelper.getIOService().writeFile(getEditorCode(), account, fileName);
			JOptionPane.showMessageDialog(null, "File saved!", "Success", JOptionPane.WARNING_MESSAGE);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
// -----------------------------------------------------------------------------
	
// ------------------------ Memory Table ---------------------------------------
	
	private void updateTable(){
		
		try {
			byte[] memBytes = remoteHelper.getExecuteService().getMemBytes();
			String[][] list = new String[memBytes.length+1][3];
			list[0][0]="Byte #";
			list[0][1]="Value";
			list[0][2]="ASCII";
			for(int i=1;i<list.length;++i){
				list[0][0]=String.valueOf(i-1);
				list[0][1]=String.valueOf(memBytes[i-1]);
				list[0][2]=String.valueOf(byte2char(memBytes[i-1]));
			}			
			memoryTable.setModel(new DefaultTableModel(
					list,
					new Object[] {
							null, null, null
					}
				));
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private char byte2char(byte b){
		if(b>='0' && b<='9')
			return (char) b;
		if(b>='a' && b<='z')
			return (char) b;
		if(b>='A' && b<='Z')
			return (char) b;
		return ' ';
	}
	
// -----------------------------------------------------------------------------

	
	public void setEditorCode(String code){
		txtpnEditor.setText(code);
	}	
	public String getEditorCode(){
		return txtpnEditor.getText();
	}	
	
	public void setInputText(String text){
		txtpnInput.setText(text);
	}
	public String getInputText(){
		return txtpnInput.getText();
	}	
	
	public void setOutputText(String text){
		textPnOutput.setText(text);
	}	
	
// --------------------- constructor of frame ------------------------------

	public MainFrame() {
		
		// 
		linkToServer();
		
		JFrame frame = new JFrame("BF Client");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(560, 240, 1024, 768);
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
				
		JMenu mnFile = new JMenu("File ");
		mnFile.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menuBar.add(mnFile);		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtpnEditor.setText(null);
			}
		});
		mnFile.add(mntmNew);		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveCode();
			}
		});
		mnFile.add(mntmSave);
		
		JMenu mnUser = new JMenu("User ");
		mnUser.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menuBar.add(mnUser);		
		JMenuItem mntmLogin = new JMenuItem("Login");
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		mntmLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				login();
			}
		});
		mnUser.add(mntmLogin);		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		mnUser.add(mntmLogout);		
		JMenuItem mntmMycode = new JMenuItem("My code");
		mntmMycode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkAllCode();
			}
		});
		mnUser.add(mntmMycode);
		
		frame.setJMenuBar(menuBar);
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		
		
		JLabel lblCodeEditor = new JLabel("Code editor");
		lblCodeEditor.setBounds(5, 5, 143, 29);
		lblCodeEditor.setFont(new Font("宋体", Font.BOLD, 24));
		contentPane.add(lblCodeEditor);
		
		JLabel lblMemoryForm = new JLabel("Memory form");
		lblMemoryForm.setBounds(717, 5, 143, 29);
		lblMemoryForm.setFont(new Font("宋体", Font.BOLD, 24));
		contentPane.add(lblMemoryForm);
		
		txtpnEditor = new JTextPane();
		txtpnEditor.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent arg0) {
			}
			public void inputMethodTextChanged(InputMethodEvent arg0) {
				codeChange();
			}
		});
		txtpnEditor.getDocument().addDocumentListener(new DocumentListener() {
	        @Override
	        public void removeUpdate(DocumentEvent e) {
	        	codeChange();
	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	codeChange();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {
	        	
	        }
	    });
		
		txtpnEditor.setBounds(15, 51, 661, 339);
		
		txtpnEditor.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		txtpnEditor.setText("Put your code here.\r\n: )");
		contentPane.add(txtpnEditor);
		
		memoryTable = new JTable();
		memoryTable.setBounds(717, 39, 266, 584);
		memoryTable.setEnabled(false);
		memoryTable.setFont(new Font("宋体", Font.PLAIN, 20));
		memoryTable.setRowHeight(24);
		memoryTable.setCellSelectionEnabled(true);
		memoryTable.setModel(new DefaultTableModel(
			new Object[][] {
				{"Byte #", "Value", "ASCII"},
				{"0", "0", "\\0"},
			},
			new Object[] {
					null, null, null
			}
		));
		contentPane.add(memoryTable);
		
		
		JLabel lblNewLabel = new JLabel("Input");
		lblNewLabel.setBounds(5, 421, 65, 31);
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 24));
		contentPane.add(lblNewLabel);
		
		txtpnInput = new JTextPane();
		txtpnInput.setBounds(5, 457, 521, 59);
		txtpnInput.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		txtpnInput.setText("program input");
		contentPane.add(txtpnInput);
		
		JButton btnRun = new JButton("RUN");
		btnRun.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnRun.setBounds(573, 457, 93, 59);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pressRun();
			}
		});
		contentPane.add(btnRun);
		
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(5, 521, 78, 31);
		lblOutput.setFont(new Font("宋体", Font.BOLD, 24));
		contentPane.add(lblOutput);
		
		textPnOutput = new JTextPane();
		textPnOutput.setBounds(5, 557, 661, 66);
		textPnOutput.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		textPnOutput.setEditable(false);
		textPnOutput.setText("program output");
		contentPane.add(textPnOutput);
		
		JLabel lblYouAreNow = new JLabel("You are now login as: ");
		lblYouAreNow.setFont(new Font("宋体", Font.BOLD, 22));
		lblYouAreNow.setBounds(5, 636, 309, 31);
		contentPane.add(lblYouAreNow);
		
		labelUserName = new JLabel("Guest");
		labelUserName.setFont(new Font("宋体", Font.BOLD, 24));
		labelUserName.setBounds(269, 636, 397, 31);
		contentPane.add(labelUserName);
		
		frame.setContentPane(contentPane);
		
		JButton btnUndo = new JButton("UNDO");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		btnUndo.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnUndo.setBounds(233, 391, 111, 59);
		contentPane.add(btnUndo);
		
		JButton btnRedo = new JButton("REDO");
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		
		btnRedo.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnRedo.setBounds(376, 391, 111, 59);
		contentPane.add(btnRedo);
		
		initCodeHistory();
		
	}	
	
// --------------------------------------------------------------------------
	
	
	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://127.0.0.1:8887/DataRemoteObject"));			
			System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}


