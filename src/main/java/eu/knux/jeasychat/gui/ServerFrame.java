package eu.knux.jeasychat.gui;

import eu.knux.jeasychat.Main;
import eu.knux.jeasychat.Resources;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

import static javax.swing.GroupLayout.Alignment.LEADING;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 11/09/14.
 */
public class ServerFrame extends JFrame {

    private boolean alreadyMD5ed = false;
    private JMenuBar menu = new JMenuBar();
    private JMenuItem ajouter = new JMenuItem("Ajouter");
    private JMenuItem supprimer = new JMenuItem("Supprimer");
    private JMenuItem modifier = new JMenuItem("Modifier");
    private JList<Server> serverList = new JList();
    private JLabel userLabel = new JLabel("Pseudo: ");
    private JTextField userField = new JTextField();
    private JLabel passLabel = new JLabel("MDP: ");
    private JPasswordField passField = new JPasswordField();
    private JButton connectBt = new JButton("Connexion");
    private int lastOne = -1;

    public ArrayList<Server> serverListName = new ArrayList<Server>();

    public ServerFrame() {
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Connexion");
        setLayout(new BorderLayout());

        menu.add(ajouter);
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddServer as = new AddServer(ServerFrame.this);
                as.setVisible(true);
            }
        });
        menu.add(modifier);
        modifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddServer as = new AddServer(ServerFrame.this, serverListName.get(serverList.getSelectedIndex()));
                as.setVisible(true);
            }
        });
        menu.add(supprimer);
        supprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Server s = serverListName.get(serverList.getSelectedIndex());
                int resp = JOptionPane.showConfirmDialog(null, "Êtes vous sûr de vouloir supprimer le serveur \"" + s.getName() + "\" ?", "Supprimer un serveur",
                        JOptionPane.YES_NO_OPTION);
                if(resp == JOptionPane.YES_OPTION) {
                    serverListName.remove(s);
                    saveServers();
                    loadServers();
                }
            }
        });
        setJMenuBar(menu);

        JScrollPane jsc = new JScrollPane(serverList);
        jsc.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        serverList.setCellRenderer(new DefaultListCellRenderer(){
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, java.lang.Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return (Component) value;
            }
        });

        add(jsc, BorderLayout.CENTER);

        JPanel btmPanel = new JPanel();
        GroupLayout gl = new GroupLayout(btmPanel);
        btmPanel.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
             .addGroup(gl.createParallelGroup(LEADING)
                             .addComponent(userLabel)
                             .addComponent(passLabel)
             )
            .addGroup(gl.createParallelGroup(LEADING)
                .addComponent(userField)
                .addComponent(passField)
            )
            .addComponent(connectBt)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(userLabel)
                                        .addComponent(userField)
                                        .addComponent(connectBt)
                        )
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(passLabel)
                                        .addComponent(passField)
                        )
        );

        add(btmPanel, BorderLayout.SOUTH);

        serverList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (lastOne != -1 && serverList.getSelectedIndex() != lastOne) {
                    serverListName.get(lastOne).setSelected(false);
                    serverListName.get(lastOne).repaint();
                }
                lastOne = serverList.getSelectedIndex();
                updateEnabled();
                if (serverList.getSelectedIndex() != -1) {
                    serverList.getSelectedValue().setSelected(true);
                    serverList.getSelectedValue().repaint();
                    alreadyMD5ed = true;
                }
            }
        });
        loadServers();
        updateEnabled();

        connectBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Server s = serverListName.get(serverList.getSelectedIndex());
                setVisible(false);
                PanelServer panelServer = new PanelServer(s);
                Main.instance.pane.addTab(s.getName(), panelServer);
                Main.instance.pane.setSelectedComponent(panelServer);
                Main.console.log(Level.FINE, "Connexion au serveur \"" + s.getName() + "\"");
            }
        });

    }

    public void updateEnabled() {
        if (serverList.getSelectedIndex() != -1) {
            connectBt.setEnabled(true);
            supprimer.setEnabled(true);
            modifier.setEnabled(true);

            Server s = serverListName.get(serverList.getSelectedIndex());
            userField.setText(s.getUsername());
            passField.setText(s.getPassword());

        } else {
            connectBt.setEnabled(false);
            supprimer.setEnabled(false);
            modifier.setEnabled(false);
        }
    }

    public void loadServers() {
        if (Resources.serverFile.exists()) {
            String json = "";
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(Resources.serverFile));
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    json += ligne;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null)
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            JSONObject root = new JSONObject(json);
            JSONArray array = root.getJSONArray("servers");
            serverListName.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject server = (JSONObject) array.get(i);
                Server s = new Server(server.getString("name"), server.getString("ip"), server.getString("username"), server.getString("password"));
                serverListName.add(s);
            }

            DefaultListModel listModel = new DefaultListModel();
            for (int i = 0; i < serverListName.size(); i++) {
                listModel.addElement(serverListName.get(i));
            }
            serverList.setModel(listModel);
        }
    }

    public void saveServers() {
        JSONObject root = new JSONObject();
        JSONArray arr = new JSONArray();
        for (Server s : serverListName) {
            JSONObject server = new JSONObject();
                server.put("name", s.getName());
                server.put("ip", s.getIp());
                server.put("username", s.getUsername());
                server.put("password", s.getPassword());
            arr.put(server);
        }
        root.put("servers", arr);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(Resources.serverFile));
            bw.write(root.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}

class Server extends JPanel {

    private boolean selected;
    private String name;
    private String ip;
    private String username;
    private String password;

    private static final Dimension dim = new Dimension(50, 60);

    public Server() {
        name = "";
        ip = "";
        username = "";
        password = "";
    }

    public Server(String name, String ip, String username, String password) {
        this.name = name;
        this.ip = ip;
        this.username = username;
        this.password = password;
        setLayout(null);
        setSize(dim);
        setPreferredSize(dim);
        JLabel nameLabel = new JLabel(name);
        JLabel text = new JLabel(username + "@" + ip);

        nameLabel.setSize(250, 20);
        nameLabel.setLocation(10, 10);

        text.setSize(250, 20);
        text.setLocation(10, 30);

        add(nameLabel);
        add(text);

    }

    public void setName(String name) { this.name = name; }
    public void setIp(String ip) { this.ip = ip; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public String getName() { return name; }
    public String getIp() { return ip; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean getSelected() { return selected; }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (selected) {
            g.setColor((Color)Resources.defaults.get("List.selectionBackground"));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

}

class AddServer extends JDialog {

    private ServerFrame sf = null;
    private int type = 0;
    private Server serv = null;
    private JLabel nameLab = new JLabel("Nom: "), ipLab = new JLabel("IP: "), userLab = new JLabel("Pseudo: "), passLab = new JLabel("MDP: ");
    private JTextField nameField = new JTextField(), ipField = new JTextField(), userField = new JTextField(), passField = new JTextField();
    private JButton addButton;

    public AddServer(final ServerFrame sf) {
        setTitle(type == 0 ? "Ajouter un serveur" : "Modifier un serveur");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        this.sf = sf;

        JPanel fields = new JPanel();
        GroupLayout gl = new GroupLayout(fields);
        fields.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(LEADING)
                                        .addComponent(nameLab)
                                        .addComponent(ipLab)
                                        .addComponent(userLab)
                                        .addComponent(passLab)
                        )
                        .addGroup(gl.createParallelGroup(LEADING)
                                        .addComponent(nameField)
                                        .addComponent(ipField)
                                        .addComponent(userField)
                                        .addComponent(passField)
                        )
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                        .addGroup(gl.createSequentialGroup()
                                        .addComponent(nameLab)
                                        .addComponent(nameField)
                        )
                        .addGroup(gl.createSequentialGroup()
                                        .addComponent(ipLab)
                                        .addComponent(ipField)
                        )
                        .addGroup(gl.createSequentialGroup()
                                        .addComponent(userLab)
                                        .addComponent(userField)
                        )
                        .addGroup(gl.createSequentialGroup()
                                        .addComponent(passLab)
                                        .addComponent(passField)
                        )
        );

        if (type == 0) addButton = new JButton("Ajouter");
        else addButton = new JButton("Modifier");

        JPanel container = new JPanel(new BorderLayout());
        container.add(fields, BorderLayout.CENTER);
        container.add(addButton, BorderLayout.SOUTH);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(container);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!nameField.getText().isEmpty() && !ipField.getText().isEmpty() && !userField.getText().isEmpty()) {
                    if (type != 0) sf.serverListName.remove(serv);
                    serv = new Server(nameField.getText(), ipField.getText(), userField.getText(), passField.getText());
                    sf.serverListName.add(serv);
                    Main.console.log(Level.FINE, "Ajout d'un serveur aux favoris: " + serv);
                    sf.saveServers();
                    sf.loadServers();
                    setVisible(false);
                }
            }
        });

    }

    public AddServer(ServerFrame frame, Server serv) {
        this(frame);
        nameField.setText(serv.getName());
        ipField.setText(serv.getIp());
        userField.setText(serv.getUsername());
        passField.setText(serv.getPassword());
        type = 1;
    }

}