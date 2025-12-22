package org.docencia.hotel.domain.impl;

import org.docencia.hotel.domain.api.RoomDomain;
import org.docencia.hotel.service.api.RoomService;
import org.springframework.stereotype.Service;

@Service
public class RoomDomainImpl implements RoomDomain {

    private final RoomService service;

    public RoomDomainImpl(RoomService service) {
        this.service = service;
    }

    // TODO
}
