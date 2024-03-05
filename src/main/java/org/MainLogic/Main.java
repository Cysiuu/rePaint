package org.MainLogic;

import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.put("Button[focus].color", new Color(0, 0, 0, 255));
            UIManager.put("Button.mouseHoverEnable", false);
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
            new Workspace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

    }
}