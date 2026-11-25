package com.muvix.backend.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class RegisterRequest {
    private String photo;
    @NotBlank
    @Size(max=100)
    private String firstName;

    @NotBlank
    @Size(max=100)
    private String lastName;

    @NotBlank
    @Email
    @Size(max=254)
    private String email;

    @NotBlank
    @Size(min=8, max=100)
    private String password;

    @NotBlank
    @Size(max=40)
    private String phone;

    @NotBlank
    @Size(max=100)
    private String country;

    @NotBlank
    @Size(max=100)
    private String province;

    @NotBlank
    @Size(max=100)
    private String city;

    @NotBlank
    @Size(max=255)
    private String address;

    public String getPhoto(){return photo;} public void setPhoto(String v){photo=v;}
    public String getFirstName(){return firstName;} public void setFirstName(String v){firstName=v;}
    public String getLastName(){return lastName;} public void setLastName(String v){lastName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getCountry(){return country;} public void setCountry(String v){country=v;}
    public String getProvince(){return province;} public void setProvince(String v){province=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
}
