package edu.iis.mto.time;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Order extends Clock {
	private static final int VALID_PERIOD_HOURS = 24;
	private State orderState;
	private List<OrderItem> items = new ArrayList<OrderItem>();
	private Instant subbmitionDate;

	private final ZoneId ZONE = ZoneId.systemDefault();
	private final Instant START = Instant.now();
	private long count = 0;

	public Order() {
		orderState = State.CREATED;
	}

	@Override
	public ZoneId getZone() {
		return ZONE;
	}

	@Override
	public Clock withZone(ZoneId zone) {
		return Clock.fixed(START, zone);
	}

	@Override
	public Instant instant() {
		return nextInstant();
	}

	private Instant nextInstant(){
		count++;
		return START.plusSeconds(count);
	}

	public void addItem(OrderItem item) {
		requireState(State.CREATED, State.SUBMITTED);

		items.add(item);
		orderState = State.CREATED;

	}

	public void submit() {
		requireState(State.CREATED);

		orderState = State.SUBMITTED;
		subbmitionDate = Instant.now().plusSeconds(-90001);

	}

	public void confirm() {
		requireState(State.SUBMITTED);
		int hoursElapsedAfterSubmittion = Hours.hoursBetween(DateTime.parse(subbmitionDate.toString()), new DateTime()).getHours();
		if(hoursElapsedAfterSubmittion > VALID_PERIOD_HOURS){
			orderState = State.CANCELLED;
			throw new OrderExpiredException();
		}
	}

	public void realize() {
		requireState(State.CONFIRMED);
		orderState = State.REALIZED;
	}

	State getOrderState() {
		return orderState;
	}
	
	private void requireState(State... allowedStates) {
		for (State allowedState : allowedStates) {
			if (orderState == allowedState)
				return;
		}

		throw new OrderStateException("order should be in state "
				+ allowedStates + " to perform required  operation, but is in "
				+ orderState);

	}

	public static enum State {
		CREATED, SUBMITTED, CONFIRMED, REALIZED, CANCELLED
	}
}
