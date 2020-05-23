package de.cerus.ssm.gui;

import de.cerus.ssm.mod.Mod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainGui extends JFrame {

    public MainGui(List<String> modNames) {
        open(modNames);
    }

    private void open(List<String> modNames) {
        setTitle("Scrap Survival Mods");
        setSize(600, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(true);
        setLocationRelativeTo(null);

        setLayout(new FlowLayout(FlowLayout.CENTER));

        DefaultComboBoxModel<Mod> model = new DefaultComboBoxModel<>();
        new Thread(() -> {
            for (String modName : modNames) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://cerus-dev.de/projects/ssm/" + modName + ".json").openConnection();
                    connection.setDoInput(true);
                    Mod mod = Mod.fromJson(IOUtils.toString(new InputStreamReader(connection.getInputStream())));
                    if (mod != null) {
                        model.addElement(mod);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        JComboBox<Mod> modJComboBox = new JComboBox<>(model);
        modJComboBox.setRenderer(new ComboBoxRenderer());
        modJComboBox.setBounds(15, 15, 250, 30);

        JButton patchBtn = new JButton("Patch game with selected mod");
        patchBtn.setBounds(15 + 250 + 15, 15, 220, 30);
        patchBtn.addActionListener(e -> {
            Object selectedItem = model.getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            Mod mod = (Mod) selectedItem;
            boolean result = mod.patch();

            JOptionPane.showConfirmDialog(null, result ? "Game was patched successfully" : "Failed to patch game",
                    result ? "Success" : "Error", JOptionPane.DEFAULT_OPTION, result ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        });

        JButton addModBtn = new JButton("Add external mod");
        addModBtn.setBounds(15 + 250 + 15, 15 + 30 + 15, 220, 30);
        addModBtn.addActionListener(e -> {
            int i = JOptionPane.showOptionDialog(null, "Do you want to load a mod on your system or a mod from a url?",
                    "Please choose an option", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"File", "URL"}, "File");

            switch (i) {
                case 0:
                    //FILE
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int res = jFileChooser.showOpenDialog(this);

                    if (res == 0) {
                        File selectedFile = jFileChooser.getSelectedFile();
                        try {
                            Mod mod = Mod.fromJson(FileUtils.readFileToString(selectedFile, StandardCharsets.UTF_8));
                            model.addElement(mod);
                            model.setSelectedItem(mod);
                        } catch (IOException | IllegalStateException exception) {
                            exception.printStackTrace();
                            JOptionPane.showConfirmDialog(null, "Failed to load mod", "Error",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    break;
                case 1:
                    //URL
                    String in = JOptionPane.showInputDialog(null, "Please enter the url where the mod is located at:",
                            "Please enter a url", JOptionPane.QUESTION_MESSAGE);
                    if (in != null) {
                        try {
                            Mod mod = Mod.fromJson(IOUtils.toString(new URL(in), StandardCharsets.UTF_8));
                            model.addElement(mod);
                            model.setSelectedItem(mod);
                        } catch (IOException malformedURLException) {
                            malformedURLException.printStackTrace();
                            JOptionPane.showConfirmDialog(null, "Failed to load mod", "Error",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    break;
            }
        });

        add(modJComboBox);
        add(patchBtn);
        add(addModBtn);

        setVisible(true);
    }
}
