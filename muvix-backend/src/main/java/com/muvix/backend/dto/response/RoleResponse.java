package com.muvix.backend.dto.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class RoleResponse {
    private String name;

    public RoleResponse(String name){
        this.name=name;
    }

}
