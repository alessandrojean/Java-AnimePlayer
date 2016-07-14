package ui;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class LoginForm extends JDialog
{

	// Components
	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordField;

	// Constants
	public static final String USER_NAME = "admin";
	public static final String PASSWORD = "123";

	public static void main(String[] args)
	{
		try
		{
			LoginForm lLoginForm = new LoginForm();
			lLoginForm.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			lLoginForm.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public LoginForm()
	{
		setResizable(false);
		setTitle("Login");
		setBackground(Color.BLACK);
		setBounds(100, 100, 325, 201);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);

		JLabel lbPromptText = new JLabel("Favor fazer login para continuar.");
		lbPromptText.setForeground(Color.WHITE);
		lbPromptText.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbPromptText.setBounds(57, 30, 205, 14);
		contentPanel.add(lbPromptText);

		JComboBox<String> comboBoxUsername = new JComboBox<>(new String[] { USER_NAME });
		comboBoxUsername.setEditable(true);
		comboBoxUsername.setBounds(112, 78, 198, 20);
		contentPanel.add(comboBoxUsername);

		JLabel lbLogin = new JLabel("Login");
		lbLogin.setForeground(Color.WHITE);
		lbLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbLogin.setBounds(57, 11, 205, 14);
		contentPanel.add(lbLogin);

		JLabel lbUsername = new JLabel("Usu\u00E1rio");
		lbUsername.setBounds(10, 81, 36, 14);
		contentPanel.add(lbUsername);

		JLabel lbPassword = new JLabel("Senha");
		lbPassword.setBounds(10, 105, 44, 14);
		contentPanel.add(lbPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(112, 102, 198, 20);
		contentPanel.add(passwordField);

		JLabel lbHeader = new JLabel("");
		lbHeader.setIcon(new ImageIcon(getClass().getResource("/login_header.png")));
		lbHeader.setBounds(0, 0, 320, 60);
		contentPanel.add(lbHeader);

		JButton btOK = new JButton("OK");
		btOK.setBounds(150, 143, 75, 23);
		btOK.addActionListener((e) ->
		{
			String password = new String(passwordField.getPassword());
			if (comboBoxUsername.getSelectedItem().toString().equals(USER_NAME) && password.equals(PASSWORD))
			{
				setVisible(false);
				dispose();
				ManagementForm lManagementForm = new ManagementForm();
				lManagementForm.setLocationRelativeTo(null);
				lManagementForm.setVisible(true);
			}
		});
		btOK.setActionCommand("OK");
		contentPanel.add(btOK);
		getRootPane().setDefaultButton(btOK);

		JButton btCancel = new JButton("Cancelar");
		btCancel.setBounds(234, 143, 75, 23);
		btCancel.addActionListener((e) ->
		{
			setVisible(false);
			dispose();
		});
		btCancel.setActionCommand("Cancel");
		contentPanel.add(btCancel);
	}
}
