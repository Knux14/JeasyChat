package eu.knux.jeasychat;

import javax.swing.*;
import java.awt.*;

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

}