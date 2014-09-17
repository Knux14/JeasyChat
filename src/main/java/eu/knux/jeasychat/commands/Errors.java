package eu.knux.jeasychat.commands;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 17/09/14.
 */
public enum Errors {

    CMD_NOT_FOUND(0, "Commande inconnue !"),
    ARGS_MISSING (1, "Arguments manquant !"),
    AUTH_FAILED(2, "Authentification échouée !"),
    NO_CLIENT(3, "Commencez par la commande \"CLIENT\""),
    BAD_PROTOCOL(4, "Mauvaise version du protocol."),
    NOT_ALLOWED(5, "Vous n'êtes pas autorisé à executer cette commande."),
    ALREADY_OP(6, "L'utilisateur est déjà OP."),
    ALREADY_NONOP(7, "L'utilisateur n'était déjà pas OP."),
    BANNED_USER(8, "Utilisateur est déjà bannit."),
    UNKNOWN_USER(9, "Utilisateur introuvable"),
    BANNED(10, "Vous êtes bannis: "),
    INTERNAL_ERROR(500, "Erreur interne du serveur");


    private int code;
    private String message;

    Errors(int code, String message){
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}
