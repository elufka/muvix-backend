package com.muvix.backend.dto.response;
import java.util.List;
public class UserResponse {
    private Long id;
    private String photo, firstName, lastName, email, phone, country, province, city, address;
    private List<RoleResponse> roles;

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getPhoto(){return photo;} public void setPhoto(String v){photo=v;}
    public String getFirstName(){return firstName;} public void setFirstName(String v){firstName=v;}
    public String getLastName(){return lastName;} public void setLastName(String v){lastName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getCountry(){return country;} public void setCountry(String v){country=v;}
    public String getProvince(){return province;} public void setProvince(String v){province=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public List<RoleResponse> getRoles(){return roles;} public void setRoles(List<RoleResponse> v){roles=v;}
}
