package eu.knux.jeasychat.network;

import eu.knux.jeasychat.commands.Command;
import eu.knux.jeasychat.gui.PanelServer;
import eu.knux.jeasychat.Main;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.concurrent.*;
import java.util.logging.Level;

import static eu.knux.jeasychat.Resources.*;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 11/09/14.
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class ClientSocket  {

    private final CountDownLatch closeLatch;
    private PanelServer panel;

    @SuppressWarnings("unused")
    private Session session;

    public ClientSocket(PanelServer ps) {
        this.panel = ps;
        this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.printf("Got connect: %s%n", session);
        this.session = session;
        try {
            Future<Void> fut;
            fut = session.getRemote().sendStringByFuture("Hello");
            fut.get(2, TimeUnit.SECONDS);
            fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
            fut.get(2, TimeUnit.SECONDS);
            session.close(StatusCode.NORMAL, "I'm done");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        String[] cmd = msg.split(" ");
        Main.console.log(Level.FINE, "Commande reçue de " + panel.getName() + ": " + cmd[0] + " avec les arguments: " + msg.substring(msg.indexOf(" ") + 1));
        Command c = Command.valueOf(cmd[0]);
        c.handle(panel, cmd);
    }
}