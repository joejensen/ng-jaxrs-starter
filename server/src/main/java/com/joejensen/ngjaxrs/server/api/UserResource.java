package com.joejensen.ngjaxrs.server.api;

import com.joejensen.ngjaxrs.server.api.model.UserDto;
import com.joejensen.ngjaxrs.server.mapper.UserMapper;
import com.joejensen.ngjaxrs.server.model.User;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the Users Api defined in the open api schema
 */
public class UserResource implements UsersApi
{
    private final UserMapper userMapper;

    @Inject
    public UserResource(UserMapper userMapper)
    {
        this.userMapper = userMapper;
    }

    @Override
    public Response createUsers()
    {
        return Response.ok().build();
    }

    @Override
    public Response listUsers(Integer limit)
    {
        List<User> users = userMapper.findUsers();
        List<UserDto> userDtos = users.stream().map(User::toDto).collect(Collectors.toList());
        return Response.ok(userDtos).build();
    }

    @Override
    public Response showUserById(String userId)
    {
        return Response.ok().build();
    }
}
