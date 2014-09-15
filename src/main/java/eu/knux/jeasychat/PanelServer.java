package eu.knux.jeasychat;

import javax.swing.*;
import java.awt.*;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 14/09/14.
 */
public class PanelServer extends JPanel {

    private JTextField typeZone = new JTextField();
    private JEditorPane jep = new JEditorPane();
    private JList userList = new JList();
    private JButton sendButton = new JButton("Envoyer"), disconnectButton = new JButton("Se déconnecter");
    private JLabel log = new JLabel("Logs: ");

    public  PanelServer(Server s) {
        setLayout(new BorderLayout());

        //Top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(log, BorderLayout.CENTER);
        topPanel.add(disconnectButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        //Center
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(jep), BorderLayout.CENTER);
        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.add(typeZone, BorderLayout.CENTER);
        typePanel.add(sendButton, BorderLayout.EAST);
        centerPanel.add(typePanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        //Right

        add(new JScrollPane(userList), BorderLayout.EAST);

        // Bottom
        JLabel serv = new JLabel("Vous êtes connecté à " + s.getName());
        add(serv, BorderLayout.SOUTH);
    }
}
