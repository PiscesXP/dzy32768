package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import rmi.RemoteHelper;
import runner.ClientRunner;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextPane txtpnEditor;
	private JTextPane txtpnInput;
	
	public static double zoomRatio=1.0;
	
	private JTable memoryTable;
	private String memList[][];
	
	private RemoteHelper remoteHelper;
	
	// execute
	private void pressRun(){
		
	}
	
	public void setEditorCode(String code){
		txtpnEditor.setText(code);
	}	
	
	public String getEditorCode(){
		return txtpnEditor.getText();
	}
	
	public void setConsoleText(String text){
		txtpnInput.setText(text);
	}

	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
		// 
		linkToServer();
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(560, 240, 1024, 768);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File ");
		mnFile.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menuBar.add(mnFile);		
		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenu mnUser = new JMenu("User ");
		mnUser.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menuBar.add(mnUser);		
		JMenuItem mntmLogin = new JMenuItem("Login");
		mnUser.add(mntmLogin);		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mnUser.add(mntmLogout);		
		JMenuItem mntmMycode = new JMenuItem("My code");
		mnUser.add(mntmMycode);
		
		
		
		
		JMenu mnAbout = new JMenu("About");
		mnAbout.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		mnAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, (int)(zoomRatio*5), (int)(zoomRatio*5)));
		setContentPane(contentPane);
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
		txtpnEditor.setBounds(5, 39, 661, 339);
		
		txtpnEditor.setFont(new Font("宋体", Font.PLAIN, 20));
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
			new String[] {
				"Byte #", "Value", "ASCII"
			}
		));
		contentPane.add(memoryTable);
		
		JLabel lblNewLabel = new JLabel("Input");
		lblNewLabel.setBounds(5, 421, 65, 31);
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 24));
		contentPane.add(lblNewLabel);
		
		//JTextPane txtpnConsole = new JTextPane();
		txtpnInput = new JTextPane();
		txtpnInput.setBounds(5, 457, 521, 59);
		txtpnInput.setFont(new Font("宋体", Font.PLAIN, 20));
		txtpnInput.setText("program input");
		contentPane.add(txtpnInput);
		
		JButton btnRun = new JButton("RUN");
		btnRun.setFont(new Font("宋体", Font.PLAIN, 20));
		btnRun.setBounds(589, 457, 77, 59);
//		btnRun.setIcon(new ImageIcon(getIconPath("play")));
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pressRun();
			}
		});
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//lblNewLabel.setText("Hello world!");
			}
		});
		contentPane.add(btnRun);
		
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(5, 521, 78, 31);
		lblOutput.setFont(new Font("宋体", Font.BOLD, 24));
		contentPane.add(lblOutput);
		
		JTextPane textPnOutput = new JTextPane();
		textPnOutput.setBounds(5, 557, 661, 66);
		textPnOutput.setFont(new Font("宋体", Font.PLAIN, 20));
		textPnOutput.setEditable(false);
		textPnOutput.setText("program output");
		contentPane.add(textPnOutput);
		
		JLabel lblYouAreNow = new JLabel("You are now login as: ");
		lblYouAreNow.setFont(new Font("宋体", Font.BOLD, 22));
		lblYouAreNow.setBounds(5, 636, 309, 31);
		contentPane.add(lblYouAreNow);
		
		JLabel labelUserName = new JLabel("Guest");
		labelUserName.setFont(new Font("宋体", Font.BOLD, 24));
		labelUserName.setBounds(269, 636, 397, 31);
		contentPane.add(labelUserName);
	}	
	
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