package com.nachoverdon.sam;

import com.nachoverdon.sam.controller.MainWindowController;

import javax.swing.*;

public class SlippiAccountManager {
    public static final String APP_NAME = "Slippi Account Manager";

    public static void main(String[] args) {
        setLookAndFeelBasedOnOS();
        init();
    }

    private static void setLookAndFeelBasedOnOS() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        new MainWindowController();
    }
}
