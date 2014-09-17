package eu.knux.jeasychat.commands;

import eu.knux.jeasychat.gui.PanelServer;
import eu.knux.jeasychat.network.handlers.Handler;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 17/09/14.
 */
public enum Command {

    /**
     * @Protocol 092
     * @arguments: [client] [protocol_version]
     * @description: CLIENT permet de savoir quel client vous utilisez, ainsi que la version du protocole que vous utilisez (trÃ¨s utile pour la commande WHOIS)
     **/
    CLIENT(null),
    /**
     * @Protocol 092
     * @arguments c->s: [pseudo] [message]
     * @arguments s->c: [sender] [message]
     * @description: Envois un message privé à la personne [pseudo].
     **/
    PRIVMSG(null),
    /**
     * @Protocol 092
     * @arguments c->s: [pseudo d'un utilisateur]
     * @arguments s->c: [ID_client] [client_utilisé] [type(0=user, 1=modo, 2=admin)]
     * @description: Permet d'avoir plus d'informations sur l'utilisateur [pseudo].
     **/
    WHOIS(null),
    ADDADMIN(null),
    REMADMIN(null),
    LOG(null),
    ADDOP(null),
    REMOP(null),
    KICK(null),
    BAN(null),
    UNBAN(null),
    LOGIN(null),
    LIST(null),
    JOIN(null),
    MSG(null),
    QUIT(null),
    ERROR(null),
    OK(null);

    private Handler handler;

    Command(Handler handler) {
        this.handler = handler;
    }

    public void handle(PanelServer ps, String[] command) {
        String[] args = new String[command.length-1];
        for (int x = 1; x < command.length; x++) {
            args[x-1] = command[x];
        }
        handler.handle(ps, args);
    }
}
