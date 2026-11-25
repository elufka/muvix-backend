package com.muvix.backend.mapper;
import com.muvix.backend.dto.request.RegisterRequest;
import com.muvix.backend.dto.response.UserResponse;
import com.muvix.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel="spring")
public interface UserMapper {
    @Mapping(target="id", ignore=true)
    @Mapping(target="passwordHash", ignore=true)
    @Mapping(target="admin", ignore=true)
    @Mapping(target="active", ignore=true)
    User toEntity(RegisterRequest request);
    @Mapping(target="roles", expression="java(user.isAdmin() ? java.util.List.of(new com.muvix.backend.dto.response.RoleResponse(\"ADMIN\")) : java.util.List.of())")
    UserResponse toResponse(User user);
}
