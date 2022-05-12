package com.VolAndRnD.github;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

@JsonInclude(JsonInclude.Include.NON_NULL)
@With
public class CreateIdRequest {

    @JsonProperty("firstname")
    public String firstname;
    @JsonProperty("lastname")
    public String lastname;
    @JsonProperty("totalprice")
    public Integer totalprice;
    @JsonProperty("depositpaid")
    public Boolean depositpaid;
    @JsonProperty("bookingdates")
    public Bookingdates bookingdates;
    @JsonProperty("additionalneeds")
    public String additionalneeds;

    CreateIdRequest(String firstname, String lastname, Integer totalprice, Boolean depositpaid, Bookingdates bookingdates, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }

    public static CreateIdRequestBuilder builder() {
        return new CreateIdRequestBuilder();
    }

    public static class CreateIdRequestBuilder {
        private String firstname;
        private String lastname;
        private Integer totalprice;
        private Boolean depositpaid;
        private Bookingdates bookingdates;
        private String additionalneeds;

        CreateIdRequestBuilder() {
        }

        public CreateIdRequestBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public CreateIdRequestBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public CreateIdRequestBuilder totalprice(Integer totalprice) {
            this.totalprice = totalprice;
            return this;
        }

        public CreateIdRequestBuilder depositpaid(Boolean depositpaid) {
            this.depositpaid = depositpaid;
            return this;
        }

        public CreateIdRequestBuilder bookingdates(Bookingdates bookingdates) {
            this.bookingdates = bookingdates;
            return this;
        }

        public CreateIdRequestBuilder additionalneeds(String additionalneeds) {
            this.additionalneeds = additionalneeds;
            return this;
        }

        public CreateIdRequest build() {
            return new CreateIdRequest(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        }

        public String toString() {
            return "CreateIdRequest.CreateIdRequestBuilder(firstname=" + this.firstname + ", lastname=" + this.lastname + ", totalprice=" + this.totalprice + ", depositpaid=" + this.depositpaid + ", bookingdates=" + this.bookingdates + ", additionalneeds=" + this.additionalneeds + ")";
        }
    }
}