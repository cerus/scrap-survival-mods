package de.cerus.ssm;

import com.google.gson.JsonParser;
import de.cerus.ssm.gui.MainGui;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScrapSurvivalMods {

    public static File SURVIVAL_DIR = null;

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int response = fileChooser.showOpenDialog(null);

        if (response != 0) {
            return;
        }

        File currentDirectory = fileChooser.getSelectedFile();
        File survivalDir = new File(currentDirectory, "Survival");
        if (!survivalDir.exists()) {
            System.out.println("Invalid dir");
            JOptionPane.showConfirmDialog(null, "Invalid directory",
                    "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            main(args);
            return;
        }

        SURVIVAL_DIR = survivalDir;

        List<String> modNames = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://cerus-dev.de/projects/ssm/index.json").openConnection();
            connection.setDoInput(true);
            InputStream inputStream = connection.getInputStream();
            JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonArray().forEach(element -> {
                modNames.add(element.getAsString());
            });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Loaded official mods: " + modNames.size() + "\n" + String.join(", ", modNames));
        MainGui mainGui = new MainGui(modNames);

/*        Mod mod = new Mod("Test", new HashMap<String, List<Replaceable>>() {
            {
                put("Scripts\\game\\harvestable\\OilGeyser.lua", Arrays.asList(
                        new Replaceable(true, "sm.container.collect\\(\\s+container,\\s+obj_resource_crudeoil,\\s+\\d+\\s+\\)",
                                "sm.container.collect( container, obj_resource_crudeoil, {{OIL_QUANTITY}} )")
                ));
            }
        }, Collections.singletonList(
                new UserInput(Type.NUMBER, "OIL_QUANTITY", "Please enter the quantity")
        ));
        mod.patch();

        String s = new GsonBuilder().setPrettyPrinting().create().toJson(mod, Mod.class);
        System.out.println(s);*/
    }

}
