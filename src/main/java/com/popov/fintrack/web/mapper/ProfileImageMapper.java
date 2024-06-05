package com.popov.fintrack.web.mapper;

import com.popov.fintrack.user.dto.user.ProfileImageDTO;
import com.popov.fintrack.user.model.ProfileImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileImageMapper extends Mappable<ProfileImage, ProfileImageDTO> {
}
