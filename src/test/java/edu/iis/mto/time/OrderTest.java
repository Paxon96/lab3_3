package edu.iis.mto.time;

import org.junit.Before;
import org.junit.Test;

public class OrderTest {

    private Order order;

    @Before
    public void init(){
        order = new Order();
    }

    @Test(expected = OrderExpiredException.class)
    public void orderConfirmShouldThrowOrderExpiredExceprionTest(){
        order.submit();
        order.confirm();
    }
}
