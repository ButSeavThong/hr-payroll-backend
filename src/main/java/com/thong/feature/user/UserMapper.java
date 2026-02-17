package com.thong.feature.user;


import com.thong.domain.User;
import com.thong.feature.auth.dto.RegisterRequest;
import com.thong.feature.user.dto.CreateUserRequest;
import com.thong.feature.user.dto.UpdateProfileRequest;
import com.thong.feature.user.dto.UserProfileResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromCreateUserRequest(CreateUserRequest createUserRequest);
    User fromRegisterRequest(RegisterRequest registerRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUserPartially(UpdateProfileRequest updateProfileRequest, @MappingTarget User user);
    UserProfileResponse toUserProfileResponse(User user);
    List<UserProfileResponse> toListOfUserProfileResponse(List<User> userList);
}
