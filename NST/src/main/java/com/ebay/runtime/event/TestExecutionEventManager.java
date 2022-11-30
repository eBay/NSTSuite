package com.ebay.runtime.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages test execution event notifications to observers.
 */
public class TestExecutionEventManager {

  private List<AfterTestMethodObserver> afterTestMethodObservers = new ArrayList<>();
  private List<BeforeTestMethodObserver> beforeTestMethodObservers = new ArrayList<>();

  private static TestExecutionEventManager instance;

  private TestExecutionEventManager() {}

  /**
   * Get the test execution event manager instance.
   * @return Instance of TestExdecutionEventManager.
   */
  public static TestExecutionEventManager getInstance() {

    if (instance == null) {
      synchronized (TestExecutionEventManager.class) {
        if (instance == null) {
          instance = new TestExecutionEventManager();
        }
      }
    }

    return instance;
  }

  /**
   * Add an after test method observer that will receive notifications immediately after a test case exits.
   * @param observer Observer that wants to receive notifications.
   */
  public void addAfterTestMethodObserver(AfterTestMethodObserver observer) {
    if (!afterTestMethodObservers.contains(observer)) {
      afterTestMethodObservers.add(observer);
    }
  }

  /**
   * Remove an observer from the set of after test method observers.
   * @param observer Observer to remove.
   */
  public void removeAfterTestMethodObserver(AfterTestMethodObserver observer) {
    afterTestMethodObservers.remove(observer);
  }

  /**
   * Add a before test method observer that will receive notifications immediately before a test case starts.
   * @param observer Observer that wants to receive notifications.
   */
  public void addBeforeTestMethodObserver(BeforeTestMethodObserver observer) {
    if (!beforeTestMethodObservers.contains(observer)) {
      beforeTestMethodObservers.add(observer);
    }
  }

  /**
   * Remove an observer from the set of before test method observers.
   * @param observer Observer to remove.
   */
  public void removeBeforeTestMethodObserver(BeforeTestMethodObserver observer) {
    beforeTestMethodObservers.remove(observer);
  }

  /**
   * Notify all after test method observers with the specified payload.
   * @param payload Observer payload to send to each observer.
   */
  public void notifyAfterTestMethodObserver(ObserverPayload payload) {
    synchronized (TestExecutionEventManager.class) {
      for (AfterTestMethodObserver observer : afterTestMethodObservers) {
        observer.notifyAfterTestMethodObserver(payload);
      }
    }
  }

  /**
   * Notify all before test method observers with the specified payload.
   * @param payload Observer payload to send to each observer.
   */
  public void notifyBeforeTestMethodObserver(ObserverPayload payload) {
    synchronized (TestExecutionEventManager.class) {
      for (BeforeTestMethodObserver observer : beforeTestMethodObservers) {
        observer.notifyBeforeTestMethodObserver(payload);
      }
    }
  }

  /**
   * Clear all observer lists.
   */
  protected void clearAll() {
    beforeTestMethodObservers.clear();
    afterTestMethodObservers.clear();
  }
}
