package de.cerus.ssm.mod;

public class Replaceable {

    private final boolean regex;
    private final String replaceKey;
    private final String replaceValue;

    public Replaceable(boolean regex, String replaceKey, String replaceValue) {
        this.regex = regex;
        this.replaceKey = replaceKey;
        this.replaceValue = replaceValue;
    }

    public boolean isRegex() {
        return regex;
    }

    public String getReplaceKey() {
        return replaceKey;
    }

    public String getReplaceValue() {
        return replaceValue;
    }

}
