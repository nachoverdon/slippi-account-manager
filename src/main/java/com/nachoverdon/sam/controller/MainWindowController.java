package com.nachoverdon.sam.controller;

import com.nachoverdon.sam.SlippiAccountManager;
import com.nachoverdon.sam.model.Account;
import com.nachoverdon.sam.utils.FileHandler;
import com.nachoverdon.sam.view.MainWindowView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class MainWindowController {
    private MainWindowView mainWindowView;
    private List<Account> accounts;
    private Account currentAccount;

    public MainWindowController() {
        this.mainWindowView = new MainWindowView(SlippiAccountManager.APP_NAME);
        this.accounts = new ArrayList<>();
        FileHandler.initUserJsonFile(mainWindowView);
        FileHandler.initAccountsFolder(mainWindowView);
        initComponents();
    }

    private void initComponents() {
        // Set listeners
        mainWindowView.getSaveButton().addActionListener(this::onSave);
        mainWindowView.getAccountsComboBox().addActionListener(this::onSelectAccount);
        mainWindowView.getAddNewButton().addActionListener(this::onAddNew);

        populateAccountsComboBox();

        // Show window
        mainWindowView.setVisible(true);
        mainWindowView.setLocationRelativeTo(null);
    }

    private void populateAccountsComboBox() {
        FileHandler.tryCreatingAccountsFolder();
        currentAccount = FileHandler.getAccountFromJsonFile(FileHandler.userJsonFile);
        accounts.add(currentAccount);
        accounts.addAll(FileHandler.getAccountsFromJsonFiles());
        accounts.forEach(this::addAccountToComboBox);

        if (currentAccount != null)
            currentAccount.setCurrent(true);
    }

    private void addAccountToComboBox(Account account) {
        if (account != null)
            mainWindowView.getAccountsComboBox().addItem(account);
    }

    private void onSave(ActionEvent event) {
        JComboBox<Account> comboBox = mainWindowView.getAccountsComboBox();
        Account selectedAccount = (Account)comboBox.getSelectedItem();
        String displayName = mainWindowView.getDisplayNameTextField().getText();

        if (selectedAccount == null)
            return;

        selectedAccount.setDisplayName(displayName);
        FileHandler.saveAccountAsCurrent(selectedAccount);
        currentAccount.setFields(selectedAccount);

        DefaultComboBoxModel<Account> model = (DefaultComboBoxModel<Account>) comboBox.getModel();

        model.removeAllElements();

        for (Account account: accounts)
            model.addElement(account);

        comboBox.setSelectedIndex(0);
    }

    private void onSelectAccount(ActionEvent event) {
        Account selectedAccount = (Account)mainWindowView.getAccountsComboBox().getSelectedItem();

        if (selectedAccount == null)
            return;

        String name = selectedAccount.getDisplayName();

        if (name != null)
            mainWindowView.getDisplayNameTextField().setText(selectedAccount.getDisplayName());
    }

    private void onAddNew(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");

        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        int result = fileChooser.showDialog(mainWindowView, "Select");

        if (result == JFileChooser.APPROVE_OPTION) {
            int notCopied = fileChooser.getSelectedFiles().length;

            for (File file: fileChooser.getSelectedFiles()) {
                Account account = FileHandler.getAccountFromJsonFile(file);

                if (FileHandler.saveAccountToJsonFileOnAccountsFolder(account))
                    notCopied--;
            }

            if (notCopied > 0)
                JOptionPane.showMessageDialog(mainWindowView, notCopied + " files could not be copied.");
        }
    }
}
