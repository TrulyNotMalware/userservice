package com.cotae.platform.userservice.api;

import com.cotae.platform.userservice.application.RefreshTokenService;
import com.cotae.platform.userservice.application.UserService;
import com.cotae.platform.userservice.dto.JwtDto;
import com.cotae.platform.userservice.dto.UserDto;
import com.cotae.platform.userservice.dto.UserRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(value = "/auth/register2")
    public EntityModel<UserRegisterResponseDto> eventRegister(
            @RequestBody ConcurrentHashMap<String, Object> body
    ){
        String email = (String) body.remove("email");
        log.debug("Register new user {}",email);
        String password = (String) body.remove("password");
        UserRegisterResponseDto responseDto = this.userService.createUser(email, password);

        Links allLinks;
        Link selfLink = linkTo(methodOn(UserController.class).eventRegister(body)).withSelfRel();
        Link logout = linkTo(methodOn(UserController.class).logout(null)).withRel("logout");
        allLinks = Links.of(selfLink, logout);
        return EntityModel.of(responseDto, allLinks);
    }

    @PostMapping(value = "/auth/register", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<UserDto> doRegister(
            @RequestBody ConcurrentHashMap<String, Object> body
    ){

        String email = (String) body.remove("userEmail");
        log.debug("Register new user {}",email);
        String password = (String) body.remove("password");

        String regex = "^[a-zA-Z0-9_!#$%&'\\*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+$";

        //FIXME Exception Handling -> ObjectMapper 이용해서 한번에 처리.
//        List<ArgumentError> errors = new ArrayList<>();
//        Pattern pattern = Pattern.compile(regex);
//        if(email == null || password == null){
//            ArgumentError error = new ArgumentError(
//                    "userEmail, password",email,"Email And Password must required."
//            );
//            errors.add(error);
//            throw new UserExceptions(UserErrorCodeImpl.PARAMETER_IS_EMPTY,errors);
//        }
//        if(!pattern.matcher(email).matches()){
//            ArgumentError error = new ArgumentError(
//                    "userEmail",email,"Email is not valid type."
//            );
//            errors.add(error);
//            throw new UserExceptions(UserErrorCodeImpl.EMAIL_IS_NOT_VALID_TYPE,errors);
//        }
        UserDto user = this.userService.createNewUser(email, password);

        Links allLinks;
        Link selfLink = linkTo(methodOn(UserController.class).doRegister(body)).withSelfRel();
        Link logout = linkTo(methodOn(UserController.class).logout(null)).withRel("logout");
        allLinks = Links.of(selfLink, logout);
        return EntityModel.of(user, allLinks);
    }

    @GetMapping(value = "/auth/reissue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @CookieValue("refresh-token") String refreshToken
    ){
        JwtDto tokenDto = this.refreshTokenService.refreshToken(accessToken, refreshToken);
        ResponseCookie responseCookie = this.refreshTokenService.createRefreshToken(refreshToken);

        Map<String, String> body = new ConcurrentHashMap<>();
        body.put("accessToken", tokenDto.getAccessToken());
        body.put("expiredTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(tokenDto.getAccessTokenExpiredDate()));
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(body);
    }

    @PostMapping(value = "/logout",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(
            @RequestHeader("X-AUTH-TOKEN") String accessToken
    ){
        ResponseCookie cookie = this.refreshTokenService.logoutToken(accessToken);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("");
    }
}
