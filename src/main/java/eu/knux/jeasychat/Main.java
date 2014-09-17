package eu.knux.jeasychat;

import eu.knux.jeasychat.gui.ConsolePanel;
import eu.knux.jeasychat.gui.PanelServer;
import eu.knux.jeasychat.gui.ServerFrame;
import eu.knux.jeasychat.network.ClientSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 11/09/14
 */
public class Main extends JFrame {

    public static Main instance; // LOL I NO DIS IZ NO OOP BUT I DNT CARRRRRRE
    public static ConsolePanel console;

    private JMenuBar menu;
    private JMenu file, options;
    private JMenuItem connection = new JMenuItem("Se connecter ...");
    private JMenuItem exit = new JMenuItem("Quitter");
    public ArrayList<PanelServer> connectedServers = new ArrayList<PanelServer>();
    public JTabbedPane pane;

    public Main() {
        instance = this;
        setSize(700, 500);
        setTitle("JeasyChat - INDEV");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);

        menu = new JMenuBar();

        file = new JMenu("Fichier");
        file.add(connection);
        connection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ServerFrame sf = new ServerFrame();
                sf.setVisible(true);
            }
        });
        file.addSeparator();
        file.add(exit);

        options = new JMenu("Options");

        menu.add(file);
        menu.add(options);

        setJMenuBar(menu);

        pane = new JTabbedPane();

        pane.addTab("Console", console);

        add(pane);

        setVisible(true);

        console.log(Level.FINE, "Démarrage de JeasyChat");

        ServerFrame sf = new ServerFrame();
        sf.setVisible(true);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Resources.defaults = UIManager.getLookAndFeel().getDefaults();
        console = new ConsolePanel();
        new Main();
    }

    public static void requestClose() {
        for (PanelServer s : instance.connectedServers) {

        }
    }

    private void useless() {
        String uri = "ws://localhost:42420";
        WebSocketClient client = new WebSocketClient();
        ClientSocket connexion = new ClientSocket();

        try {
            client.start();
            URI serverAddress = new URI(uri);
            ClientUpgradeRequest req = new ClientUpgradeRequest();
            client.connect(connexion, serverAddress, req);
            System.out.println("Connecting to " + serverAddress);
            connexion.awaitClose(15, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try{
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
