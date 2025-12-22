package org.docencia.hotel.persistence.nosql.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "guest_preferences")
public class GuestPreferencesDocument {

    @Id
    private String id;

    // TODO: guestId + resto de campos
}
