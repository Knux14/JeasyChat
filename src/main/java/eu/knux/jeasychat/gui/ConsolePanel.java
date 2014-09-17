package eu.knux.jeasychat.gui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 15/09/14.
 */
public class ConsolePanel extends JPanel {

    private JEditorPane jep        = new JEditorPane();
    private JTextField  sendText   = new JTextField();
    private JButton     sendButton = new JButton("Envoyer");

    public ConsolePanel() {
        setLayout(new BorderLayout());
        add(new JScrollPane(jep), BorderLayout.CENTER);

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(sendText, BorderLayout.CENTER);
        pane.add(sendButton, BorderLayout.EAST);

        pane.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));

        add(pane, BorderLayout.SOUTH);

    }

    public void log(Level level, String text) {
        Date f = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm:ss");
        String message = "[" + level.getLocalizedName() + " - " + df.format(f) + "] -> " + text;

        // Writing to the console
        jep.setText(jep.getText() + message + "\n" );

        // Writing to the log file
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("jeasychat.log", true)));
            out.println(message);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}