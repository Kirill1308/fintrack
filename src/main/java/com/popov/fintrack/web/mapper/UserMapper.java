package com.popov.fintrack.web.mapper;

import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDTO> {
}
