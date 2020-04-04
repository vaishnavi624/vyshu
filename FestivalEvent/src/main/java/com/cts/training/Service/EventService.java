package com.cts.training.Service;

import java.util.List;

import com.cts.training.Entity.EventEntity;

public interface EventService {
	void saveEvent(EventEntity eventEntity);

	List<EventEntity> showEvent();

	EventEntity showEvent(int eventId);

	boolean registeredToevent(int eventId, int visitorId, int seats);

	List<EventEntity> getRegisteredEvent(int visitorId);

	boolean isAlreadyRegistered(int visitorId, int eventId);

	boolean cancelEventTicket(int visitorId, int eventId);

}
