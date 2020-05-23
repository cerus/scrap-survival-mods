package de.cerus.ssm.mod;

import com.google.gson.Gson;
import de.cerus.ssm.ScrapSurvivalMods;
import de.cerus.ssm.mod.input.UserInput;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mod {

    private String name;
    private Map<String, List<Replaceable>> replaceables;
    private List<UserInput> inputs;

    public static Mod fromJson(String json) throws IllegalStateException {
        try {
            return new Gson().fromJson(json, Mod.class);
        } catch (IllegalStateException ex) {
            throw ex;
        }
    }

    public Mod(String name, Map<String, List<Replaceable>> replaceables, List<UserInput> inputs) {
        this.name = name;
        this.replaceables = replaceables;
        this.inputs = inputs;
    }

    public boolean verify() {
        for (String key : replaceables.keySet()) {
            if (key.contains("..") || !key.endsWith(".lua")) {
                return false;
            }
        }
        return true;
    }

    public boolean patch() {
        if (!verify()) {
            System.out.println("Verify failed");
            return false;
        }

        Map<String, String> inputValues = new HashMap<>();
        inputs.forEach(userInput -> {
            inputValues.put(userInput.getKey(), userInput.getInput().toString());
        });

        for (Map.Entry<String, List<Replaceable>> entry : replaceables.entrySet()) {
            String path = entry.getKey();
            List<Replaceable> list = entry.getValue();
            File file = new File(ScrapSurvivalMods.SURVIVAL_DIR, path);
            if (!file.exists()) {
                return false;
            }

            try {
                String contents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

                for (Replaceable replaceable : list) {
                    contents = (replaceable.isRegex() ? (contents.replaceAll(replaceable.getReplaceKey(), replaceable.getReplaceValue()))
                            : (contents.replace(replaceable.getReplaceKey(), replaceable.getReplaceValue())));
                }

                for (UserInput input : inputs) {
                    contents = contents.replace("{{" + input.getKey().replace(" ", "_").toUpperCase()
                            + "}}", inputValues.get(input.getKey()));
                }

                FileUtils.write(file, contents, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Patch failed (" + name + ")");
                return false;
            }

            System.out.println("Patched " + file.getPath() + " (" + name + ")");
        }
        return true;
    }

    public String getName() {
        return name;
    }

}
