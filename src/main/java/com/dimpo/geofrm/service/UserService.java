package com.dimpo.geofrm.service;

import com.dimpo.geofrm.entity.User;
import com.dimpo.geofrm.dto.UserDto;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
