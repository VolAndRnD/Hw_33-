package com.github.VolAndRnD;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTokenRequest {
    public CreateTokenRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    @JsonProperty("username")
    public String username;
    @JsonProperty("password")
    public String password;

    public static CreateTokenRequestBuilder builder() {
        return new CreateTokenRequestBuilder();
    }


    public static class CreateTokenRequestBuilder {
        private String username;
        private String password;

        CreateTokenRequestBuilder() {
        }

        public CreateTokenRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CreateTokenRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CreateTokenRequest build() {
            return new CreateTokenRequest(username, password);
        }

        public String toString() {
            return "CreateTokenRequest.CreateTokenRequestBuilder(username=" + this.username + ", password=" + this.password + ")";
        }
    }
}