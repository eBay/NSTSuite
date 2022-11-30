package com.ebay.runtime.event;

public interface BeforeTestMethodObserver {

  /**
   * Receive notification of after test method event.
   * @param payload Payload related to the event.
   */
  public void notifyBeforeTestMethodObserver(ObserverPayload payload);
}
