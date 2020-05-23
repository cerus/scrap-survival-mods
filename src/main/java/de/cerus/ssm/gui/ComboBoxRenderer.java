package de.cerus.ssm.gui;

import de.cerus.ssm.mod.Mod;

import javax.swing.*;
import java.awt.*;

public class ComboBoxRenderer implements ListCellRenderer<Mod> {
    @Override
    public Component getListCellRendererComponent(JList<? extends Mod> list, Mod value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value != null) {
            return new JLabel(value.getName());
        }
        return new JLabel("???");
    }
}
