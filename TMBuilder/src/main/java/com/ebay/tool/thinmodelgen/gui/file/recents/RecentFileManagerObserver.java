package com.ebay.tool.thinmodelgen.gui.file.recents;

public interface RecentFileManagerObserver {

  /**
   * Open the recent file selected.
   * @param filePath Recent file selected.
   */
  public void openRecentFile(String filePath);
}
