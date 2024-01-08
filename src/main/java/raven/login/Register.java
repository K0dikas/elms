package raven.login;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.component.PasswordStrengthStatus;
import raven.manager.FormsManager;
import raven.toast.Notifications;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends JPanel {
    public Register() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        cmdRegister = new JButton("Sign Up");
        Sex = new ButtonGroup();

        cmdRegister.addActionListener(e -> {
            String fullName = String.valueOf(txtFirstName.getText()).concat(" "+txtLastName.getText());

            if (isRegistrationValid()) {
                String sex = Sex.getSelection().getActionCommand();
                createCredential(fullName, String.valueOf(txtEmail.getText()), String.valueOf(txtConfirmPassword.getPassword()), sex);
            } else if (!isMatchPassword()) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Passwords don't match. Try again!");
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "One of the fields below are either blank or incorrect, fill it out");
            }
        });
        passwordStrengthStatus = new PasswordStrengthStatus();

        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        txtFirstName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "First name");
        txtLastName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Last name");
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");

        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        JLabel lbTitle = new JLabel("Welcome to SCHOOL ELMS");
        JLabel description = new JLabel("Join us to chat, connect, and help your classmates and other students. Sign up now and start your education!");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        passwordStrengthStatus.initPasswordField(txtPassword);

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Full Name"), "gapy 10");
        panel.add(txtFirstName, "split 2");
        panel.add(txtLastName);
        panel.add(new JLabel("Gender"), "gapy 8");
        panel.add(createGenderPanel());
        panel.add(new JSeparator(), "gapy 5 5");
        panel.add(new JLabel("Username or Email"));
        panel.add(txtEmail);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(passwordStrengthStatus, "gapy 0");
        panel.add(new JLabel("Confirm Password"), "gapy 0");
        panel.add(txtConfirmPassword);
        panel.add(cmdRegister, "gapy 20");
        panel.add(createLoginLabel(), "gapy 10");
        add(panel);
    }

    private Component createGenderPanel() {
        JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        jrMale = new JRadioButton("Male", false);
        jrFemale = new JRadioButton("Female", false);

        jrMale.setActionCommand(jrMale.getText());
        jrFemale.setActionCommand(jrFemale.getText());
        Sex = new ButtonGroup();
        Sex.add(jrMale);
        Sex.add(jrFemale);
        panel.add(jrMale);
        panel.add(jrFemale);
        return panel;
    }

    private Component createLoginLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");

        JButton cmdLogin = new JButton("<html><a href=\"#\">Sign in here</a></html>");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3");
        cmdLogin.setContentAreaFilled(false);
        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogin.addActionListener(e -> FormsManager.getInstance().showForm(new Login()));

        JLabel label = new JLabel("Already have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");
        panel.add(label);
        panel.add(cmdLogin);
        return panel;
    }

    public boolean isMatchPassword() {
        String password = String.valueOf(txtPassword.getPassword());
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
        return password.equals(confirmPassword);
    }

    public void createCredential(String username, String email, String password, String sex) {
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader("Records.txt"));
            boolean emailExists = false;

            // Checks if the email already exists in the .txt file
            while ((line = reader.readLine()) != null) {
                String[] passphrase = line.split(" : ");

                if (passphrase[1].equals(email)) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Email already exists, try another one");
                    emailExists = true;
                    break;
                }
            }
            reader.close();

            // If the email does not exist in the .txt file, it will try to write it in the file
            if (!emailExists) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("Records.txt", true));
                    writer.write(username);
                    writer.write(" : ");
                    writer.write(email);
                    writer.write(" : ");
                    writer.write(password);
                    writer.write(" : ");
                    writer.write(sex);
                    writer.newLine();
                    writer.close();
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Student Account Created!");
                    FormsManager.getInstance().showForm(new Login());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "File Not Found, try to register first...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isRegistrationValid() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String password = String.valueOf(txtPassword.getPassword());
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());


        return isMatchPassword() && isSexSelected() && isEmailValid(email) &&
                !firstName.isBlank() &&
                !lastName.isBlank() &&
                !email.isBlank() &&
                !password.isBlank() &&
                !confirmPassword.isBlank();
    }

    private boolean isSexSelected() {
        return Sex.getSelection() != null;
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z_-]+(\\.[A-Za-z_-]+)*@"
                + "[^-][A-Za-z-]+(\\.[A-Za-z-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JRadioButton jrMale;
    private JRadioButton jrFemale;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private ButtonGroup Sex;
    private JButton cmdRegister;
    private PasswordStrengthStatus passwordStrengthStatus;
}