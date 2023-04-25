package com.gymhomie;

public class AddFetchUserDetails {

    public String textEmail;
    public String textFirstName;
    public String textLastName;

    public AddFetchUserDetails(){

    }

    public AddFetchUserDetails(String emailAddress, String firstName, String lastName)
    {
        this.textEmail = emailAddress;
        this.textFirstName = firstName;
        this.textLastName = lastName;

    }

}
