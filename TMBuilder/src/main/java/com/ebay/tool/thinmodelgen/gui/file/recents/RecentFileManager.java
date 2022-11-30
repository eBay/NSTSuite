package com.ebay.tool.thinmodelgen.gui.file.recents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class RecentFileManager implements ActionListener {

  private JMenu recentFilesMenu = new JMenu("Recent files");
  private Preferences preferences;
  private ArrayList<String> recentFileList = new ArrayList<>();

  private ArrayList<RecentFileManagerObserver> observers = new ArrayList<>();

  private static final int recentFileLimit = 5;
  private static final String recentFileKey = "RECENT_FILES";
  private static final String recentFileDelimiter = ";";

  private static RecentFileManager instance;

  private RecentFileManager() {
    preferences = Preferences.userNodeForPackage(RecentFileManager.class);
    loadRecentFilesFromPreferences();
    updateRecentFilesMenu();
  }

  /**
   * Get the instance of the recent file menu. This is a singleton so that all
   * recent file menus provide the same list of files.
   *
   * @return RecentFileManager instance.
   */
  public static RecentFileManager getInstance() {

    if (instance == null) {
      synchronized (RecentFileManager.class) {
        if (instance == null) {
          instance = new RecentFileManager();
        }
      }
    }

    return instance;
  }

  /**
   * Add an observer.
   *
   * @param observer
   *          Observer to notify.
   */
  public void addObserver(RecentFileManagerObserver observer) {
    observers.add(observer);
  }

  /**
   * Remove an observer.
   *
   * @param observer
   *          Observer to stop notifying.
   */
  public void removeObserver(RecentFileManagerObserver observer) {
    observers.remove(observer);
  }

  /**
   * Get the recent files menu to add to the UI.
   *
   * @return Recent files menu.
   */
  public JMenu getRecentFilesMenu() {
    return recentFilesMenu;
  }

  /**
   * Clear the recent files list.
   */
  public void clearRecentFiles() {
    recentFileList.clear();
    try {
      preferences.clear();
    } catch (BackingStoreException e) {
      e.printStackTrace();
    }
    updateRecentFilesMenu();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() instanceof JMenuItem) {
      JMenuItem menuItem = (JMenuItem) e.getSource();
      String filePath = menuItem.getText();

      for (RecentFileManagerObserver observer : observers) {
        observer.openRecentFile(filePath);
      }
    }
  }

  /**
   * Add a file to the recentFileList.
   *
   * @param filePath
   *          File path.
   */
  public void addRecentFile(String filePath) {

    if (!filePath.endsWith("tmb")) {
      return;
    }

    recentFileList.remove(filePath);
    recentFileList.add(0, filePath);
    if (recentFileList.size() > recentFileLimit) {
      for (int i = recentFileList.size() - 1; i >= recentFileLimit; i--) {
        recentFileList.remove(i);
      }
    }

    updateRecentFilesMenu();
    saveRecentFilesToPreferences();
  }

  /**
   * Load recent files from the persistent preferences data store.
   */
  private void loadRecentFilesFromPreferences() {
    recentFileList.clear();
    String recentFilesRaw = preferences.get(recentFileKey, "");
    String[] recentFiles = recentFilesRaw.split(recentFileDelimiter);
    for (String recentFile : recentFiles) {
      if (recentFile.equals("")) {
        continue;
      }
      recentFileList.add(recentFile);
    }
  }

  /**
   * Update the recent files menu with the current contents of the
   * recentFileList.
   */
  private void updateRecentFilesMenu() {

    recentFilesMenu.removeAll();

    for (String recentFile : recentFileList) {
      JMenuItem menuItem = new JMenuItem(recentFile);
      menuItem.addActionListener(this);
      recentFilesMenu.add(menuItem);
    }
  }

  /**
   * Save recent files to preferences for persistent storage between executions.
   */
  private void saveRecentFilesToPreferences() {

    StringBuilder savedPreferenceBuilder = new StringBuilder();
    for (String file : recentFileList) {

      if (savedPreferenceBuilder.length() > 0) {
        savedPreferenceBuilder.append(recentFileDelimiter);
      }

      savedPreferenceBuilder.append(file);
    }

    preferences.put(recentFileKey, savedPreferenceBuilder.toString());
    try {
      preferences.flush();
    } catch (BackingStoreException e) {
      System.out.println("Failed to store recent files preferences.");
      e.printStackTrace();
    }
  }
}
