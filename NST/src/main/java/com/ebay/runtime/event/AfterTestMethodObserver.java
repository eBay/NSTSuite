package com.ebay.runtime.event;

public interface AfterTestMethodObserver {

  /**
   * Receive notification of after test method event. This is triggered
   * immediately following the completion of a single test case execution.
   *
   * @param payload
   *          Payload related to the event.
   */
  public void notifyAfterTestMethodObserver(ObserverPayload payload);
}
