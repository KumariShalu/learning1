package controller;

import app.security.jwt.JWTToken;
import app.config.Constants;
import domain.User;
import javax.inject.Inject;
import controller.dto.LoginDTO;
import javax.servlet.ServletException;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import app.security.AuthenticationException;
import app.security.jwt.TokenProvider;
import app.security.UserAuthenticationToken;
import app.service.UserService;
import javax.validation.Valid;

@Path("/api")
public class UserJWTController {

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private UserService userService;

    /**
     * POST /authenticate : authenticate the credential.
     * <p>
     * Authenticate the user login and password.
     * </p>
     *
     * @param loginDTO the login details to authenticate
     * @return the Response with status 200 (OK) and with body the new jwt
     * token, or with status 401 (Unauthorized) if the authentication fails
     */
    @Path("/authenticate")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(@Valid LoginDTO loginDTO) throws ServletException {

        UserAuthenticationToken authenticationToken = new UserAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        try {
            User user = userService.authenticate(authenticationToken);
            boolean rememberMe = (loginDTO.isRememberMe() == null) ? false : loginDTO.isRememberMe();
            String jwt = tokenProvider.createToken(user, rememberMe);
            return Response.ok(new JWTToken(jwt)).header(Constants.AUTHORIZATION_HEADER, "Bearer " + jwt).build();
        } catch (AuthenticationException exception) {
            return Response.status(Status.UNAUTHORIZED).header("AuthenticationException", exception.getLocalizedMessage()).build();
        }
    }

}
