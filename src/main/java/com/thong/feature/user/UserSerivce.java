package com.thong.feature.user;


import com.thong.feature.user.dto.CreateUserRequest;
import com.thong.feature.user.dto.UpdateProfileRequest;
import com.thong.feature.user.dto.UserProfileResponse;

import java.util.List;

public interface UserSerivce {
    void register(CreateUserRequest createUserRequest);
    void deleteUserById(Integer id);
    UserProfileResponse getUserProfile(Integer id);
    UserProfileResponse UpdateProfileById(Integer id, UpdateProfileRequest updateProfileRequest);
    List<UserProfileResponse> getUserProfiles();

    void adminCreateNewAdmin(CreateUserRequest createUserRequest);
}
