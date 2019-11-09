package com.joejensen.ngjaxrs.server.mapper;

import com.joejensen.ngjaxrs.server.model.User;

import java.util.Collections;
import java.util.List;

public class UserMapper
{
    public List<User> findUsers()
    {
        User testUser = new User();
        testUser.setId(1);
        testUser.setEmail("joe@joejensen.com");
        return Collections.singletonList(testUser);
    }
}
