package edu.iis.mto.time;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderTest {

    private Clock clock;
    private Instant currentTime;
    private Order order;

    @Before
    public void init() {
        clock = mock(Clock.class);
        currentTime = Instant.now();
        when(clock.instant()).thenAnswer((invocation) -> currentTime);
        order = new Order(clock);
    }

    @Test(expected = OrderExpiredException.class)
    public void orderConfirmShouldThrowOrderExpiredExceptionTest() {
        order.submit();
        currentTime = currentTime.plus(35, ChronoUnit.HOURS);
        order.confirm();
    }

    @Test
    public void orderConfirmShouldNotThrowOrderExpiredExceptionTest() {
        order.submit();
        currentTime = currentTime.plus(20, ChronoUnit.HOURS);
        order.confirm();
    }
}
