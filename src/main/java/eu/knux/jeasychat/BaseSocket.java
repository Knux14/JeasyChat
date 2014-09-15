package eu.knux.jeasychat;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.net.ssl.SSLEngineResult;
import java.util.concurrent.*;

import static eu.knux.jeasychat.Resources.*;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 11/09/14.
 */
@WebSocket(maxTextMessageSize = 64*1024)
public class BaseSocket {

    private final CountDownLatch closeLatch;

    @SuppressWarnings("unused")
    private Session session;

    public BaseSocket()
    {
        this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int dur, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(dur, unit);
    }

    @OnWebSocketClose
    public void onClose(int status, String reason) {
        System.out.println("Closed: " + status + " (" + reason + ")");
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        sendPacket(String.format("CLIENT %s %s", _CLIENTNAME, _PROTOCOL));
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.println("Message: " + msg);
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
