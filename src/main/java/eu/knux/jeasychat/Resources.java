package eu.knux.jeasychat;

import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.util.logging.Level;

/**
 * @author Nathan J. <knux14@gmail.com>
 * @date 11/09/14.
 */
public class Resources {

    public static final String _CLIENTNAME = "JeasyChat",
                               _PROTOCOL   = "093",
                               _ENDCHAR    = "\r\n";
    public static File serverFile = new File("servers.json");
    public static File configFile = new File("config.json");

    public static UIDefaults defaults;

    public static String smileyFile = "smiley_pack_1.zip";

    public static void loadConfiguration() {
        Main.console.log(Level.FINE, "Chargement de la configuration");
        String json = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String ligne;
            while ((ligne = br.readLine()) != null) {
                json += ligne;
            }
            br.close();

            JSONObject root = new JSONObject(json);
            smileyFile = root.getString("SmileyFile");

        } catch (FileNotFoundException e) {
            Main.console.log(Level.SEVERE, e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Main.console.log(Level.FINE, e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void saveConfiguration() {
        JSONObject root = new JSONObject();
        root.put("SmileyFile", smileyFile);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));
            bw.write(root.toString(4));
            bw.close();
        } catch (IOException e) {
            Main.console.log(Level.SEVERE, e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

}
