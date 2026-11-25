package com.muvix.backend.mapper;

import com.muvix.backend.dto.request.RegisterRequest;
import com.muvix.backend.dto.response.UserResponse;
import com.muvix.backend.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-25T14:47:57-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.13.0.2 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setPhoto( request.getPhoto() );
        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setEmail( request.getEmail() );
        user.setPhone( request.getPhone() );
        user.setCountry( request.getCountry() );
        user.setProvince( request.getProvince() );
        user.setCity( request.getCity() );
        user.setAddress( request.getAddress() );

        return user;
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( user.getId() );
        userResponse.setPhoto( user.getPhoto() );
        userResponse.setFirstName( user.getFirstName() );
        userResponse.setLastName( user.getLastName() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setPhone( user.getPhone() );
        userResponse.setCountry( user.getCountry() );
        userResponse.setProvince( user.getProvince() );
        userResponse.setCity( user.getCity() );
        userResponse.setAddress( user.getAddress() );

        userResponse.setRoles( user.isAdmin() ? java.util.List.of(new com.muvix.backend.dto.response.RoleResponse("ADMIN")) : java.util.List.of() );

        return userResponse;
    }
}
