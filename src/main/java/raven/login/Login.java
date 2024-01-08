package raven.login;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import raven.main.Application;
import raven.main.form.MainForm;
import raven.manager.FormsManager;
import raven.menu.Menu;
import raven.toast.Notifications;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Login extends JPanel {

    public static Application app;
    public final MainForm mainForm;
    public static Menu menu;
    public static String head;
    public static String sex;

    public Login() {
        mainForm = new MainForm();
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Remember me");
        cmdLogin = new JButton("Login");
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));

        cmdLogin.addActionListener(e -> {
            if (isLoginValid()) {
                app.login();
            }
            // TODO: Create the student dashboard
        });

        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        JLabel lbTitle = new JLabel("Welcome back student!");
        JLabel description = new JLabel("Please sign in to access your student account");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Email"), "gapy 8");
        panel.add(txtEmail);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(chRememberMe, "grow 0");
        panel.add(cmdLogin, "gapy 10");
        panel.add(createSignupLabel(), "gapy 10");
        add(panel);
    }

    private Component createSignupLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JButton cmdRegister = new JButton("<html><a href=\"#\">Sign up</a></html>");
        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3");
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> {
            FormsManager.getInstance().showForm(new Register());
        });
        JLabel label = new JLabel("Don't have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");
        panel.add(label);
        panel.add(cmdRegister);
        return panel;
    }

    public boolean checkCredentials(String username, String password) {
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader("Records.txt"));

            while ((line = reader.readLine()) != null) {
                String[] passphrase = line.split(" : ");

                // TODO: Pass the full name of the user to the header, maybe through read and write .txt file (always being overwritten)
                if (passphrase.length == 4 && passphrase[1].equals(username) && passphrase[2].equals(password)) {
                    head = passphrase[0];
                    sex = passphrase[3];
                    // System.out.println("head after successful login: "+head);
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Successfully Logged In! Hi "+passphrase[0]+"!");
                    return true;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "File Not Found, Try To Register First");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isLoginValid() {
        String email = txtEmail.getText();
        String password = String.valueOf(txtPassword.getPassword());

        if (email.isBlank() || password.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "One of the fields are blank, fill it out");
            return false;
        }

        if (!checkCredentials(email, password)) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Invalid username or password");
            return false;
        }
        return true;
    }

    public static String getHeader() {
        return head;
    }

    public static String getSex() {
        return sex;
    }

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JCheckBox chRememberMe;
    private JButton cmdLogin;

    public void setContentPane(Login login) {
    }
}