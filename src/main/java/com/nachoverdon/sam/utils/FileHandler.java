package com.nachoverdon.sam.utils;

import com.github.slugify.Slugify;
import com.google.gson.Gson;
import com.nachoverdon.sam.model.Account;
import com.nachoverdon.sam.view.MainWindowView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class FileHandler {
    public static final String ACCOUNTS_FOLDER = "accounts";
    public static final String USER_JSON = "user.json";

    public static Gson gson = new Gson();
    public static Slugify slugify;
    public static File accountsFolder;
    public static File userJsonFile;

    public static Account getAccountFromJsonFile(File file) {
        try {
            String json = org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            return FileHandler.gson.fromJson(json, Account.class);
        } catch (Exception e) {
            log.warn("File " + file.getName() + " was not a valid json file.", e);
        }

        return null;
    }

    public static boolean saveAccountToJsonFileOnAccountsFolder(Account account) {
        initSlugify();

        String fileName = slugify.slugify(account.getDisplayName()) + ".json";
        File file = new File(accountsFolder, fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                FileUtils.writeStringToFile(file, gson.toJson(account), StandardCharsets.UTF_8);
                return true;
            } catch (IOException e) {
                log.warn("Couldn't create file " + fileName, e);
            }
        }

        return false;
    }

    public static void initSlugify() {
        if (slugify != null)
            return;

        slugify = new Slugify().withLowerCase(true).withUnderscoreSeparator(true);
    }

    public static void initUserJsonFile(MainWindowView mainWindowView) {
        userJsonFile = new File(FileHandler.USER_JSON);

        if (!userJsonFile.exists()) {
            JOptionPane.showMessageDialog(mainWindowView, "user.json not found. Download it at: https://slippi.gg/online/enable");
        }
    }

    public static void initAccountsFolder(MainWindowView mainWindowView) {
        // Handle accounts folder
        accountsFolder = new File(ACCOUNTS_FOLDER);

        if (accountsFolder.exists() && !accountsFolder.isDirectory()) {
            JOptionPane.showMessageDialog(mainWindowView,
                    "File 'accounts' already exists and its not a folder.\n" +
                            "Make sure that 'accounts' is a folder, not a file."
            );
        }

        tryCreatingAccountsFolder();
    }

    public static void tryCreatingAccountsFolder() {
        if (!accountsFolder.exists())
            accountsFolder.mkdir();
    }

    public static List<Account> getAccountsFromJsonFiles() {
        FileHandler.tryCreatingAccountsFolder();

        Iterator<File> files = FileUtils.iterateFiles(FileHandler.accountsFolder, new String[]{"json"}, true);
        List<Account> accounts = new ArrayList<>();

        files.forEachRemaining(file -> {
            Account account = getAccountFromJsonFile(file);

            if (account != null)
                accounts.add(account);
        });

        return accounts;
    }

    public static void saveAccountAsCurrent(Account account) {
        String json = FileHandler.gson.toJson(account);

        try {
            FileUtils.writeStringToFile(userJsonFile, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Cannot write to user.json", e);
        }
    }
}
