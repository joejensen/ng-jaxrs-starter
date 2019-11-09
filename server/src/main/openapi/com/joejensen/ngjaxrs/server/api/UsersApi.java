package com.joejensen.ngjaxrs.server.api;

import com.joejensen.ngjaxrs.server.api.model.ErrorDto;
import com.joejensen.ngjaxrs.server.api.model.UserDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/users")
@Api(description = "the users API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2019-11-08T22:17:06.021618100-05:00[America/New_York]")
public interface UsersApi {

    @POST
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a user", notes = "", tags={ "users",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Null response", response = Void.class),
        @ApiResponse(code = 200, message = "unexpected error", response = ErrorDto.class) })
    Response createUsers();

    @GET
    @Produces({ "application/json" })
    @ApiOperation(value = "List all users", notes = "", tags={ "users",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A paged array of users", response = UserDto.class, responseContainer = "List"),
        @ApiResponse(code = 200, message = "unexpected error", response = ErrorDto.class, responseContainer = "List") })
    Response listUsers(@QueryParam("limit")   @ApiParam("How many users to return at one time (max 100)")  Integer limit);

    @GET
    @Path("/{userId}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Info for a specific user", notes = "", tags={ "users" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Expected response to a valid request", response = UserDto.class),
        @ApiResponse(code = 200, message = "unexpected error", response = ErrorDto.class) })
    Response showUserById(@PathParam("userId") @ApiParam("The id of the user to retrieve") String userId);
}
