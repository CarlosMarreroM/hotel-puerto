package org.docencia.hotel.domain.impl;

import org.docencia.hotel.domain.api.BookingDomain;
import org.docencia.hotel.service.api.BookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingDomainImpl implements BookingDomain {

    private final BookingService service;

    public BookingDomainImpl(BookingService service) {
        this.service = service;
    }

    // TODO
}
