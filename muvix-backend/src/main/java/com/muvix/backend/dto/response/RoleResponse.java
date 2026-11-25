package com.muvix.backend.dto.response;
public class RoleResponse {
    private String name;

    public RoleResponse(){}

    public RoleResponse(String name){
        this.name=name;
    }

    public String getName(){return name;} public void setName(String v){name=v;}
}
