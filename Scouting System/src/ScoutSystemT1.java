import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class ScoutSystemT1 {

	private JFrame frmScoutSystem;
	private JTextField TeamName;
	private JTextField TeamNumber;
	private JLabel lblClimb;
	private JLabel lblSwitchAuton;
	private JComboBox DriveType;
	private JLabel lblDriveType;
	private JLabel lblSwitch;
	private JRadioButton SwitchY;
	private JRadioButton SwitchN;
	private JLabel lblScale;
	private JRadioButton ScaleY;
	private JRadioButton ScaleN;
	private JLabel lblVault;
	private JRadioButton VaultY;
	private JRadioButton VaultN;
	private JLabel lblScaleAuton;
	private JRadioButton AScaleY;
	private JRadioButton ASwitchN;
	private JRadioButton ClimbY;
	private JLabel lblTeleopPeriod;
	private JLabel lblAuton;
	private JRadioButton ASwitchY;
	private JTextField OtherCom;
	private JLabel lblOtherComents;
	private JLabel lblCrossedLine;
	private JRadioButton CrossedLineY;
	private JRadioButton CrossedLineN;
	boolean Climb;
	boolean Switch;
	boolean Scale;
	boolean Vault;
	boolean ASwitch;
	boolean AScale;
	boolean CrossedLine;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScoutSystemT1 window = new ScoutSystemT1();
					window.frmScoutSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ScoutSystemT1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmScoutSystem = new JFrame();
		frmScoutSystem.getContentPane().setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 18));
		frmScoutSystem.setTitle("FridgeBot Scout System V1.0");
		frmScoutSystem.setBounds(100, 100, 416, 281);
		frmScoutSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmScoutSystem.getContentPane().setLayout(null);
		
		JLabel lblTeamName = new JLabel("Team Name & Number:");
		lblTeamName.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		lblTeamName.setBounds(10, 11, 176, 27);
		frmScoutSystem.getContentPane().add(lblTeamName);
		
		TeamName = new JTextField();
		TeamName.setBounds(176, 16, 91, 20);
		frmScoutSystem.getContentPane().add(TeamName);
		TeamName.setColumns(10);
		
		TeamNumber = new JTextField();
		TeamNumber.setColumns(10);
		TeamNumber.setBounds(277, 16, 99, 20);
		frmScoutSystem.getContentPane().add(TeamNumber);
		
		lblClimb = new JLabel("Climb:");
		lblClimb.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblClimb.setBounds(10, 133, 48, 20);
		frmScoutSystem.getContentPane().add(lblClimb);
		
		JRadioButton ClimbN = new JRadioButton("No");
		ClimbN.setBounds(103, 134, 52, 20);
		frmScoutSystem.getContentPane().add(ClimbN);
		
		lblSwitchAuton = new JLabel("Switch Auton:");
		lblSwitchAuton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSwitchAuton.setBounds(225, 95, 91, 27);
		frmScoutSystem.getContentPane().add(lblSwitchAuton);
		
		JRadioButton AScaleN = new JRadioButton("No");
		AScaleN.setBounds(357, 116, 48, 23);
		frmScoutSystem.getContentPane().add(AScaleN);
		
		DriveType = new JComboBox();
		DriveType.setBounds(81, 99, 74, 20);
		frmScoutSystem.getContentPane().add(DriveType);
		DriveType.addItem("Mecanum");
		DriveType.addItem("Tank");
		DriveType.addItem("Arcade");
		DriveType.addItem("Swerve");
		DriveType.addItem("H-Drive");
		
		lblDriveType = new JLabel("Drive Type:\r\n");
		lblDriveType.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblDriveType.setBounds(10, 95, 74, 27);
		frmScoutSystem.getContentPane().add(lblDriveType);
		
		lblSwitch = new JLabel("Switch:\r\n");
		lblSwitch.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSwitch.setBounds(10, 146, 48, 27);
		frmScoutSystem.getContentPane().add(lblSwitch);
		
		SwitchY = new JRadioButton("Yes");
		SwitchY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (SwitchY.isSelected()) {
	                Switch = true;
	            }
	            
	            else {
	            	Switch = false;
	            }
			}
		});
		SwitchY.setBounds(53, 150, 48, 20);
		frmScoutSystem.getContentPane().add(SwitchY);
		
		SwitchN = new JRadioButton("No");
		SwitchN.setBounds(103, 150, 52, 20);
		frmScoutSystem.getContentPane().add(SwitchN);
		
		lblScale = new JLabel("Scale:\r\n");
		lblScale.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblScale.setBounds(10, 167, 48, 14);
		frmScoutSystem.getContentPane().add(lblScale);
		
		ScaleY = new JRadioButton("Yes");
		ScaleY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (ScaleY.isSelected()) {
	                Scale = true;
	            }
	            
	            else {
	            	Scale = false;
	            }
			}
			
		});
		ScaleY.setBounds(53, 164, 48, 23);
		frmScoutSystem.getContentPane().add(ScaleY);
		
		ScaleN = new JRadioButton("No");
		ScaleN.setBounds(103, 164, 52, 23);
		frmScoutSystem.getContentPane().add(ScaleN);
		
		lblVault = new JLabel("Vault:");
		lblVault.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblVault.setBounds(10, 177, 38, 27);
		frmScoutSystem.getContentPane().add(lblVault);
		
		VaultY = new JRadioButton("Yes");
		VaultY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (VaultY.isSelected()) {
	                Vault = true;
	            }
	            
	            else {
	            	Vault = false;
	            }
			}
			
		});
		VaultY.setBounds(53, 180, 48, 23);
		frmScoutSystem.getContentPane().add(VaultY);
		
		VaultN = new JRadioButton("No");
		VaultN.setBounds(103, 180, 52, 23);
		frmScoutSystem.getContentPane().add(VaultN);
		
		lblScaleAuton = new JLabel("Scale Auton:");
		lblScaleAuton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblScaleAuton.setBounds(225, 113, 82, 26);
		frmScoutSystem.getContentPane().add(lblScaleAuton);
		
		AScaleY = new JRadioButton("Yes");
		AScaleY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	            if (AScaleY.isSelected()) {
	                AScale = true;
	            }
	            
	            else {
	            	AScale = false;
	            }
			
			}
		});
		AScaleY.setBounds(310, 116, 53, 23);
		frmScoutSystem.getContentPane().add(AScaleY);
		
		ASwitchN = new JRadioButton("No");
		ASwitchN.setBounds(357, 98, 48, 23);
		frmScoutSystem.getContentPane().add(ASwitchN);
		
		ClimbY = new JRadioButton("Yes");
		ClimbY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	            if (ClimbY.isSelected()) {
	                Climb = true;
	            }
	            
	            else {
	            	Climb = false;
	            }
			}
		});
		ClimbY.setBounds(53, 135, 48, 19);
		frmScoutSystem.getContentPane().add(ClimbY);
		
		lblTeleopPeriod = new JLabel("TeleOp Period:");
		lblTeleopPeriod.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTeleopPeriod.setBounds(10, 74, 161, 14);
		frmScoutSystem.getContentPane().add(lblTeleopPeriod);
		
		lblAuton = new JLabel("Auton:");
		lblAuton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblAuton.setBounds(238, 74, 68, 14);
		frmScoutSystem.getContentPane().add(lblAuton);
		
		ASwitchY = new JRadioButton("Yes");
		ASwitchY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	            if (ASwitchY.isSelected()) {
	                ASwitch = true;
	            }
	            
	            else {
	            	ASwitch = false;
	            }
			
			}
		});
		ASwitchY.setBounds(310, 98, 53, 23);
		frmScoutSystem.getContentPane().add(ASwitchY);
		
		OtherCom = new JTextField();
		OtherCom.setBounds(97, 43, 293, 20);
		frmScoutSystem.getContentPane().add(OtherCom);
		OtherCom.setColumns(10);
		
		lblOtherComents = new JLabel("Other Coments:");
		lblOtherComents.setBounds(2, 43, 99, 20);
		frmScoutSystem.getContentPane().add(lblOtherComents);
		
		JButton btnImputData = new JButton("Input Data");
		btnImputData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String DriveT = (String) (DriveType).getSelectedItem();
				String teamName = TeamName.getText();
				String teamNumber = TeamNumber.getText();
				String OCom = OtherCom.getText();
				
				
				Path path = Paths.get("c:/output.txt");
				 
				
				try (BufferedWriter writer = Files.newBufferedWriter(path))
				{
				    writer.write(teamName + "," + teamNumber + "," + DriveT + "," + OCom);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//Testing only
				System.out.println(teamName);
				System.out.println(teamNumber);
				System.out.println(DriveT);
				System.out.println(OCom);
			}
		});
		btnImputData.setBounds(205, 167, 185, 40);
		frmScoutSystem.getContentPane().add(btnImputData);
		
		lblCrossedLine = new JLabel("Crossed Line");
		lblCrossedLine.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblCrossedLine.setBounds(225, 133, 82, 26);
		frmScoutSystem.getContentPane().add(lblCrossedLine);
		
		CrossedLineY = new JRadioButton("Yes");
		CrossedLineY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	            if (CrossedLineY.isSelected()) {
	                CrossedLine = true;
	            }
	            
	            else {
	            	CrossedLine = false;
	            }
			
			}
		});
		CrossedLineY.setBounds(310, 133, 53, 23);
		frmScoutSystem.getContentPane().add(CrossedLineY);
		
		CrossedLineN = new JRadioButton("No");
		CrossedLineN.setBounds(357, 133, 48, 23);
		frmScoutSystem.getContentPane().add(CrossedLineN);
	}
}