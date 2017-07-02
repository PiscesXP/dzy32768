package ui;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import rmi.RemoteHelper;
import runner.ClientRunner;

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
	
	private JLabel labelUserName;
	
	private ClientRunner clientRunner;	
	
	private boolean isReady=false;
	private boolean isUserChange=true;
	

// --------------------------------------------------------------------------
		
	// execute
	private void pressRun(){
		setOutputText(clientRunner.runProgram(getEditorCode(), getInputText()));
		updateTable();
	}
	
// --------------------------  login logout ----------------------------------------
	
	private void login(){
		labelUserName.setText(clientRunner.login());
	}
	
	private void logout(){
		clientRunner.logout();
		labelUserName.setText("Guest");
	}

// --------------------------------------------------------------------
	

// ---------------------------- code history ----------------------------------

	private void codeChange(){
		if(isReady && isUserChange)
			clientRunner.codeChange(getEditorCode());
	}
	
	private void undo(){
		isUserChange=false;
		setEditorCode(clientRunner.undo());
		isUserChange=true;
	}
	
	private void redo(){
		isUserChange=false;
		String redoTxt=clientRunner.redo();
		if(redoTxt!=null)
			setEditorCode(redoTxt);
		isUserChange=true;
		
	}

// ---------------------------------------------------------------------
	
// ---------------------------- file version controller --------------------------------------

	
	// check all code files
	// select a file to open
	private void checkAllCode(){
		String newCode=clientRunner.SelectCode();
		if(newCode!=null)
			setEditorCode(newCode);
	}

	
	private void saveCode(){
		clientRunner.saveCode(getEditorCode());
	}
	
// -----------------------------------------------------------------------------
	
// ------------------------ Memory Table ---------------------------------------
	
	private void updateTable(){
		
		byte[] memBytes = clientRunner.getMemBytes();
		String[][] list = new String[memBytes.length][3];
		for(int i=0;i<list.length;++i){
			list[i][0]=String.valueOf(i);
			list[i][1]=String.valueOf(memBytes[i]);
			
			// convert to ASCII char
			char c = (char) memBytes[i];
			if((c>='0' && c<='9') || (c>='a' && c<='z') || (c>='A' && c<='Z'))
				list[i][2]=String.valueOf(c);
		}
		
		// set memory table
		memoryTable.setModel(new DefaultTableModel(
				list,
				new String[] {
						"Byte #", "Value", "ASCII"
				}
			));
	}
	
// ---------------------------- Set & Get ---------------------------------------

	
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
	
	public void setIsReady(boolean isReady){
		this.isReady=isReady;
	}
	
	public void setIsUserChange(boolean isUserChange){
		this.isUserChange=isUserChange;
	}
	
// --------------------- constructor of frame ------------------------------

	public MainFrame() {
		
		
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
		
		JMenu mnUser = new JMenu("User");
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
		lblCodeEditor.setBounds(15, 5, 143, 29);
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
		
		txtpnEditor.setBounds(15, 37, 661, 339);
		
		txtpnEditor.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		txtpnEditor.setText("Put your code here.\r\n: )");
		contentPane.add(txtpnEditor);
		
		
		
		memoryTable = new JTable();
		memoryTable.setBounds(720, 37, 256, 600);
		memoryTable.setFont(new Font("宋体", Font.PLAIN, 20));
		memoryTable.setRowHeight(24);
		memoryTable.setCellSelectionEnabled(true);
		memoryTable.setModel(new DefaultTableModel(
			new Object[][] {
				{"0", "0", ""},
			},
			new String[] {
				"Byte #", "Value", "ASCII"
			}
		));
		contentPane.add(memoryTable);
		
		JLabel lblNewLabel = new JLabel("Input");
		lblNewLabel.setBounds(15, 455, 65, 31);
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 24));
		contentPane.add(lblNewLabel);
		
		txtpnInput = new JTextPane();
		txtpnInput.setBounds(14, 488, 662, 34);
		txtpnInput.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		contentPane.add(txtpnInput);
		
		JButton btnRun = new JButton("RUN");
		btnRun.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnRun.setBounds(470, 385, 111, 59);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pressRun();
			}
		});
		contentPane.add(btnRun);
		
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(15, 535, 78, 31);
		lblOutput.setFont(new Font("宋体", Font.BOLD, 24));
		contentPane.add(lblOutput);
		
		textPnOutput = new JTextPane();
		textPnOutput.setBounds(15, 566, 661, 66);
		textPnOutput.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		textPnOutput.setEditable(false);
		textPnOutput.setText("//program output");
		contentPane.add(textPnOutput);
		
		JLabel lblYouAreNow = new JLabel("You are now login as: ");
		lblYouAreNow.setFont(new Font("宋体", Font.BOLD, 22));
		lblYouAreNow.setBounds(14, 656, 309, 31);
		contentPane.add(lblYouAreNow);
		
		labelUserName = new JLabel("Guest");
		labelUserName.setFont(new Font("宋体", Font.BOLD, 24));
		labelUserName.setBounds(279, 655, 397, 31);
		contentPane.add(labelUserName);
		
		frame.setContentPane(contentPane);
		
		JButton btnUndo = new JButton("UNDO");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		btnUndo.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnUndo.setBounds(72, 385, 111, 59);
		btnUndo.setVisible(true);
		contentPane.add(btnUndo);
		
		JButton btnRedo = new JButton("REDO");
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		
		btnRedo.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnRedo.setBounds(247, 385, 111, 59);
		btnRedo.setVisible(true);
		contentPane.add(btnRedo);
		
		JScrollPane scrollPane = new JScrollPane(memoryTable);
		scrollPane.setBounds(720, 37, 256, 600);
		scrollPane.setColumnHeaderView(null);
		contentPane.add(scrollPane);


		JScrollPane scrollPane_1 = new JScrollPane(txtpnEditor);
		scrollPane_1.setBounds(15, 37, 661, 339);
		contentPane.add(scrollPane_1);
		
		clientRunner = new ClientRunner(getEditorCode());
		isReady=true;
	}	
}


