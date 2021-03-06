package eu.knux.jeasychat.gui;

import eu.knux.jeasychat.Main;
import eu.knux.jeasychat.commands.Command;
import eu.knux.jeasychat.network.ClientSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 14/09/14.
 */
public class PanelServer extends JPanel {

    private JTextField typeZone = new JTextField();
    private JEditorPane jep = new JEditorPane();
    private JList userList = new JList();
    private JButton sendButton = new JButton("Envoyer");
    private ThreadSocket ts;

    public  PanelServer(Server s) {
        ts = new ThreadSocket(this, s);
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

        typeZone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendMessage();
            }
        });

        add(splitPane);
        ts.start();
    }

    private void sendMessage() {
        //ts.getSocket().sendPacket(Command.MSG, typeZone.getText());
    }

    public ClientSocket getSocket() {
        return ts.getSocket();
    }
}

class ThreadSocket extends Thread {

    private PanelServer ps;
    private Server s;
    private WebSocketClient client = new WebSocketClient();
    private URI connectionAddress;
    private ClientSocket cs;

    ThreadSocket(PanelServer ps, Server s) {
        this.ps = ps;
        this.s = s;
    }

    @Override
    public void run() {
        String url = "ws://" + s.getIp();
        System.out.println(url);
        try {
            connectionAddress = new URI(url);
        } catch (URISyntaxException e) {
            Main.console.log(Level.SEVERE, e.getLocalizedMessage());
            e.printStackTrace();
        }
        try {
            client.start();
        } catch (Exception e) {
            Main.console.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        ClientUpgradeRequest req = new ClientUpgradeRequest();
        Main.console.log(Level.FINE, "[" + s.getName() + "] Connexion en cours à " + connectionAddress.getHost() + "...");
       // cs = new ClientSocket(ps);
        /*try {
            client.connect(cs, connectionAddress, req);
            cs.awaitClose(2, TimeUnit.SECONDS);
        } catch (IOException e) {
            Main.console.log(Level.SEVERE, e.getLocalizedMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Main.console.log(Level.SEVERE, e.getLocalizedMessage());
            e.printStackTrace();
        }*/
    }

    public ClientSocket getSocket() {
        return cs;
    }

}