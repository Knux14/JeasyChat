package eu.knux.jeasychat.network.handlers;

import eu.knux.jeasychat.gui.PanelServer;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 17/09/14.
 */
public interface Handler {

    public void handle(PanelServer ps, String[] args);

}
