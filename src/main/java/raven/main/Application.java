package raven.main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import raven.login.Login;
import raven.main.form.MainForm;
import raven.manager.FormsManager;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {

    public static Application app;
    public static Login login;
    public final MainForm mainform;

    public Application() {
        login = new Login();
        mainform = new MainForm();
        init();
    }

    private void init() {
        setUndecorated(true);
        setTitle("FlatLaf Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 1500)); // 750 x 590 | FULL: 1200 x 1500
        setLocationRelativeTo(null);
        setContentPane(new Login());
        Notifications.getInstance().setJFrame(this);
        FormsManager.getInstance().initApplication(this);
    }

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainform.showForm(component);
    }

    public static void setSelectedMenu(int index, int subIndex) {
        app.mainform.setSelectedMenu(index, subIndex);
    }

    public static void login() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.mainform);
        app.mainform.applyComponentOrientation(app.getComponentOrientation());
        setSelectedMenu(0, 0);
        app.mainform.hideMenu();
        SwingUtilities.updateComponentTreeUI(app.mainform);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void logout() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(login);
        login.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(login);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 719, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        //EventQueue.invokeLater(() -> new Application().setVisible(true));

        EventQueue.invokeLater(() -> {
            app = new Application();
            app.setVisible(true);
        });
    }
}