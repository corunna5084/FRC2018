import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;

public class ScoutSystemT1 {

	private JFrame frmScoutSystem;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblClimb;
	private JLabel lblSwitchAuton;
	private JComboBox comboBox;
	private JLabel lblDriveType;
	private JLabel lblSwitch;
	private JRadioButton rdbtnYes_1;
	private JRadioButton rdbtnNo_2;
	private JLabel lblScale;
	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private JLabel lblVault;
	private JRadioButton radioButton_2;
	private JRadioButton radioButton_3;
	private JLabel lblScaleAuton;
	private JRadioButton radioButton_4;
	private JRadioButton radioButton_5;
	private JRadioButton radioButton_6;
	private JLabel lblTeleopPeriod;
	private JLabel lblAuton;
	private JRadioButton radioButton_7;
	private JTextField textField_2;
	private JLabel lblOtherComents;

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
		
		textField = new JTextField();
		textField.setBounds(176, 16, 91, 20);
		frmScoutSystem.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(277, 16, 99, 20);
		frmScoutSystem.getContentPane().add(textField_1);
		
		lblClimb = new JLabel("Climb:");
		lblClimb.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblClimb.setBounds(10, 133, 48, 20);
		frmScoutSystem.getContentPane().add(lblClimb);
		
		JRadioButton rdbtnNo = new JRadioButton("No");
		rdbtnNo.setBounds(103, 134, 52, 20);
		frmScoutSystem.getContentPane().add(rdbtnNo);
		
		lblSwitchAuton = new JLabel("Switch Auton:");
		lblSwitchAuton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSwitchAuton.setBounds(225, 95, 91, 27);
		frmScoutSystem.getContentPane().add(lblSwitchAuton);
		
		JRadioButton rdbtnNo_1 = new JRadioButton("No");
		rdbtnNo_1.setBounds(357, 116, 48, 23);
		frmScoutSystem.getContentPane().add(rdbtnNo_1);
		
		comboBox = new JComboBox();
		comboBox.setBounds(81, 99, 74, 20);
		frmScoutSystem.getContentPane().add(comboBox);
		comboBox.addItem("Mecanum");
		comboBox.addItem("Tank");
		comboBox.addItem("Arcade");
		comboBox.addItem("Swerve");
		comboBox.addItem("H-Drive");
		
		lblDriveType = new JLabel("Drive Type:\r\n");
		lblDriveType.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblDriveType.setBounds(10, 95, 74, 27);
		frmScoutSystem.getContentPane().add(lblDriveType);
		
		lblSwitch = new JLabel("Switch:\r\n");
		lblSwitch.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSwitch.setBounds(10, 146, 48, 27);
		frmScoutSystem.getContentPane().add(lblSwitch);
		
		rdbtnYes_1 = new JRadioButton("Yes");
		rdbtnYes_1.setBounds(53, 150, 48, 20);
		frmScoutSystem.getContentPane().add(rdbtnYes_1);
		
		rdbtnNo_2 = new JRadioButton("No");
		rdbtnNo_2.setBounds(103, 150, 52, 20);
		frmScoutSystem.getContentPane().add(rdbtnNo_2);
		
		lblScale = new JLabel("Scale:\r\n");
		lblScale.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblScale.setBounds(10, 167, 48, 14);
		frmScoutSystem.getContentPane().add(lblScale);
		
		radioButton = new JRadioButton("Yes");
		radioButton.setBounds(53, 164, 48, 23);
		frmScoutSystem.getContentPane().add(radioButton);
		
		radioButton_1 = new JRadioButton("No");
		radioButton_1.setBounds(103, 164, 52, 23);
		frmScoutSystem.getContentPane().add(radioButton_1);
		
		lblVault = new JLabel("Vault:");
		lblVault.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblVault.setBounds(10, 177, 38, 27);
		frmScoutSystem.getContentPane().add(lblVault);
		
		radioButton_2 = new JRadioButton("Yes");
		radioButton_2.setBounds(53, 180, 48, 23);
		frmScoutSystem.getContentPane().add(radioButton_2);
		
		radioButton_3 = new JRadioButton("No");
		radioButton_3.setBounds(103, 180, 52, 23);
		frmScoutSystem.getContentPane().add(radioButton_3);
		
		lblScaleAuton = new JLabel("Scale Auton:");
		lblScaleAuton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblScaleAuton.setBounds(225, 113, 82, 26);
		frmScoutSystem.getContentPane().add(lblScaleAuton);
		
		radioButton_4 = new JRadioButton("Yes");
		radioButton_4.setBounds(310, 116, 53, 23);
		frmScoutSystem.getContentPane().add(radioButton_4);
		
		radioButton_5 = new JRadioButton("No");
		radioButton_5.setBounds(357, 98, 48, 23);
		frmScoutSystem.getContentPane().add(radioButton_5);
		
		radioButton_6 = new JRadioButton("Yes");
		radioButton_6.setBounds(53, 135, 48, 19);
		frmScoutSystem.getContentPane().add(radioButton_6);
		
		lblTeleopPeriod = new JLabel("TeleOp Period:");
		lblTeleopPeriod.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTeleopPeriod.setBounds(10, 74, 161, 14);
		frmScoutSystem.getContentPane().add(lblTeleopPeriod);
		
		lblAuton = new JLabel("Auton:");
		lblAuton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblAuton.setBounds(238, 74, 68, 14);
		frmScoutSystem.getContentPane().add(lblAuton);
		
		radioButton_7 = new JRadioButton("Yes");
		radioButton_7.setBounds(310, 98, 53, 23);
		frmScoutSystem.getContentPane().add(radioButton_7);
		
		textField_2 = new JTextField();
		textField_2.setBounds(97, 43, 293, 20);
		frmScoutSystem.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		lblOtherComents = new JLabel("Other Coments:");
		lblOtherComents.setBounds(2, 43, 99, 20);
		frmScoutSystem.getContentPane().add(lblOtherComents);
	}
}
