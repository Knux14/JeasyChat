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
public class ClientSocket {

    private final CountDownLatch closeLatch;

    @SuppressWarnings("unused")
    private Session session;
    private PanelServer panel;

    public ClientSocket(PanelServer ps) {
        this.panel = ps;
        this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int dur, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(dur, unit);
    }

    @OnWebSocketClose
    public void onClose(int status, String reason) {
        Main.console.log(Level.FINE, "[" + panel.getName() + "] Connexion coupée (" + status + "): " + reason);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        Main.console.log(Level.FINE, "[" + panel.getName() + "] Connecté !");
        sendPacket(String.format(Command.CLIENT + " %s %s", _CLIENTNAME, _PROTOCOL));
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        String[] cmd = msg.split(" ");
        Main.console.log(Level.FINE, "Commande reçue de " + panel.getName() + ": " + cmd[0] + " avec les arguments: " + msg.substring(msg.indexOf(" ") + 1));
        Command c = Command.valueOf(cmd[0]);
        c.handle(panel, cmd);
    }

    public void sendPacket(String packet) {
        try {
            Future<Void> future;
            future = session.getRemote().sendStringByFuture(String.format("%s %s", packet, _ENDCHAR));
            future.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        session.close(StatusCode.NORMAL, "Done for today");
    }

}
