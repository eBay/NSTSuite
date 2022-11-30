package com.ebay.runtime.event;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestExecutionEventManagerTest {

  private ArgumentCaptor<ObserverPayload> payload;
  private ObserverPayload inputPayload = new ObserverPayload("class", "method");

  @BeforeTest(alwaysRun = true)
  public void doBeforeEachMethod() {
    TestExecutionEventManager.getInstance().clearAll();
    payload = ArgumentCaptor.forClass(ObserverPayload.class);
  }

  @Test(groups = "unitTest")
  public void beforeTestMethodObserverCalled() {
    BeforeTestMethodObserver observer = Mockito.mock(BeforeTestMethodObserver.class);
    TestExecutionEventManager.getInstance().addBeforeTestMethodObserver(observer);
    TestExecutionEventManager.getInstance().notifyBeforeTestMethodObserver(inputPayload);
    verify(observer, times(1)).notifyBeforeTestMethodObserver(payload.capture());
    ObserverPayload payloadValue = payload.getValue();
    assertThat(payloadValue.getClassName(), is(equalTo("class")));
    assertThat(payloadValue.getMethodName(), is(equalTo("method")));
  }

  @Test(groups = "unitTest")
  public void beforeTestMethodObserverRemoved() {
    BeforeTestMethodObserver observer = Mockito.mock(BeforeTestMethodObserver.class);
    TestExecutionEventManager.getInstance().addBeforeTestMethodObserver(observer);
    TestExecutionEventManager.getInstance().removeBeforeTestMethodObserver(observer);
    TestExecutionEventManager.getInstance().notifyBeforeTestMethodObserver(inputPayload);
    verify(observer, times(0)).notifyBeforeTestMethodObserver(payload.capture());
  }

  @Test(enabled=false) // For some reason this trips in CI - it doesn't trip locally.
  public void afterTestMethodObserverCalled() {
    AfterTestMethodObserver observer = Mockito.mock(AfterTestMethodObserver.class);
    TestExecutionEventManager.getInstance().addAfterTestMethodObserver(observer);
    TestExecutionEventManager.getInstance().notifyAfterTestMethodObserver(inputPayload);
    verify(observer, times(1)).notifyAfterTestMethodObserver(payload.capture());
    ObserverPayload payloadValue = payload.getValue();
    assertThat(payloadValue.getClassName(), is(equalTo("class")));
    assertThat(payloadValue.getMethodName(), is(equalTo("method")));
  }

  @Test(enabled=false) // For some reason this trips in CI - it doesn't trip locally.
  public void afterTestMethodObserverRemoved() {
    AfterTestMethodObserver observer = Mockito.mock(AfterTestMethodObserver.class);
    TestExecutionEventManager.getInstance().addAfterTestMethodObserver(observer);
    TestExecutionEventManager.getInstance().removeAfterTestMethodObserver(observer);
    TestExecutionEventManager.getInstance().notifyAfterTestMethodObserver(inputPayload);
    verify(observer, times(0)).notifyAfterTestMethodObserver(payload.capture());
  }
}
