package org.docencia.hotel.domain.impl;

import org.docencia.hotel.domain.api.HotelDomain;
import org.docencia.hotel.service.api.HotelService;
import org.springframework.stereotype.Service;

@Service
public class HotelDomainImpl implements HotelDomain {

    private final HotelService service;

    public HotelDomainImpl(HotelService service) {
        this.service = service;
    }

    // TODO
}
