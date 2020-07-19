package com.nachoverdon.sam.view;

import com.nachoverdon.sam.model.Account;
import lombok.Data;

import javax.swing.*;

@Data
public class MainWindowView extends JFrame {
    private JPanel mainPanel;
    private JComboBox<Account> accountsComboBox;
    private JTextField displayNameTextField;
    private JButton saveButton;
    private JLabel accountsLabel;
    private JLabel displayNameLabel;
    private JButton addNewButton;

    public MainWindowView(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
    }
}
