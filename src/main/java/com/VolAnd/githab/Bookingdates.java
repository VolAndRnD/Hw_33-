package com.VolAnd.githab;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)

public class Bookingdates {

    @JsonProperty("checkin")
    public String checkin;
    @JsonProperty("checkout")
    public String checkout;

    public Bookingdates(String checkin, String checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public static BookingdatesBuilder builder() {
        return new BookingdatesBuilder();
    }

    public static class BookingdatesBuilder {
        private String checkin;
        private String checkout;

        BookingdatesBuilder() {
        }

        public BookingdatesBuilder checkin(String checkin) {
            this.checkin = checkin;
            return this;
        }

        public BookingdatesBuilder checkout(String checkout) {
            this.checkout = checkout;
            return this;
        }

        public Bookingdates build() {
            return new Bookingdates(checkin, checkout);
        }

        public String toString() {
            return "Bookingdates.BookingdatesBuilder(checkin=" + this.checkin + ", checkout=" + this.checkout + ")";
        }
    }
}