package de.cerus.ssm.mod.input;

import javax.swing.*;

public class UserInput {

    private final Type type;
    private final String key;
    private final String description;

    public UserInput(Type type, String key, String description) {
        this.type = type;
        this.key = key;
        this.description = description;
    }

    public <T> T getInput() {
        Object obj = null;
        String in = JOptionPane.showInputDialog(null, description, "User input", JOptionPane.QUESTION_MESSAGE);
        if (in == null) {
            JOptionPane.showInputDialog(null, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            return getInput();
        }

        switch (type) {
            case NUMBER:
                try {
                    obj = Integer.parseInt(in);
                } catch (NumberFormatException ignored) {
                    JOptionPane.showInputDialog(null, "Invalid input! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return getInput();
                }
                break;
            case STRING:
                obj = in;
                break;
            case BOOLEAN:
                switch (in.toLowerCase()) {
                    case "true":
                    case "yes":
                        obj = true;
                        break;
                    case "false":
                    case "no":
                        obj = false;
                        break;
                    default:
                        JOptionPane.showInputDialog(null, "Invalid input! Please enter a boolean value (true/false/yes/no).", "Error", JOptionPane.ERROR_MESSAGE);
                        return getInput();
                }
                break;
        }

        return (T) obj;
    }

    public Type getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

}
