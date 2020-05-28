package com.aregumsky.mousepointer;

import static org.junit.Assert.assertThat;

import java.awt.AWTException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

public class ApplicationTest {
	Application application = new Application();
	
	@Test
	public void testGetRandomShouldReturnValueWithinRange() {
		assertThat(application.getRandom(500), Matchers.lessThanOrEqualTo(500));
	}

	@Test
	public void testClickingStartButtonEnablesStop() {
		application.createStartButton();
		application.createStopButton();
		
		application.start.doClick();
		
		SoftAssertions softly = new SoftAssertions();
		softly.assertThat(application.start.isEnabled()).isFalse();
		softly.assertThat(application.stop.isEnabled()).isTrue();
		
		softly.assertAll();
		
		application.stop.doClick();
	}

	@Test
	public void testClickingStopButtonEnablesStart() {
		application.createStartButton();
		application.createStopButton();
		
		application.stop.doClick();
		
		SoftAssertions softly = new SoftAssertions();
		softly.assertThat(application.stop.isEnabled()).isFalse();
		softly.assertThat(application.start.isEnabled()).isTrue();
		
		softly.assertAll();
	}
	
	@Test
	public void verifyThatClickingStartButtonMovesMouse() throws InterruptedException, AWTException {
		application = Mockito.spy(application);
		application.createStartButton();
		application.start.doClick();
		
		CountDownLatch lock = new CountDownLatch(1);
		lock.await(1, TimeUnit.SECONDS);
		Mockito.verify(application, Mockito.times(1)).moveMouse();
	}

	
	@Test
	public void verifyThatClickingStopButtonStopsMoveMouse() throws InterruptedException, AWTException {
		application = Mockito.spy(application);
		application.createStartButton();
		application.createStopButton();
		application.start.doClick();
		
		CountDownLatch lock = new CountDownLatch(1);
		lock.await(1, TimeUnit.SECONDS);
		
		application.stop.doClick();

		lock = new CountDownLatch(1);
		lock.await(3, TimeUnit.SECONDS);
		Mockito.verify(application, Mockito.times(1)).moveMouse();
	}
}
