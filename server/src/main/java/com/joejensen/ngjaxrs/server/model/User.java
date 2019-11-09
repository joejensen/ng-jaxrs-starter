package com.joejensen.ngjaxrs.server.model;

import com.joejensen.ngjaxrs.server.api.model.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Simple class representing a user of the system
 */
@Data
@EqualsAndHashCode(of = "id")
public class User
{
    private long id;
    private String email;

    public UserDto toDto()
    {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Test Name");
        dto.setTag("Me");
        return dto;
    }
}
