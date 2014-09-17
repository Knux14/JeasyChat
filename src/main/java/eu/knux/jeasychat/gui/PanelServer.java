package eu.knux.jeasychat.gui;

import eu.knux.jeasychat.Main;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;

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

    private WebSocketClient client = new WebSocketClient();

    public  PanelServer(Server s) {
        setLayout(new BorderLayout());
        jep.setEditable(false);
/*
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
        typePanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
        centerPanel.add(typePanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        //Right

        add(new JScrollPane(userList), BorderLayout.EAST);

        // Bottom
        JLabel serv = new JLabel("Vous êtes connecté à " + s.getName());
        add(serv, BorderLayout.SOUTH);*/

        /**
         * Left panel
         */
        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(new JScrollPane(jep), BorderLayout.CENTER);

        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.add(typeZone, BorderLayout.CENTER);
        typePanel.add(sendButton, BorderLayout.EAST);
        typePanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));

        leftPane.add(typePanel, BorderLayout.SOUTH);

        /**
         * Right panel
         */
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(userList), BorderLayout.CENTER);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPane, rightPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.9d);

        Dimension minimumSize = new Dimension(100, 50);
        leftPane.setMinimumSize(minimumSize);
        rightPanel.setMinimumSize(minimumSize);

        add(splitPane);

        try {
            client.start();
        } catch (Exception e) {
            Main.console.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }
}