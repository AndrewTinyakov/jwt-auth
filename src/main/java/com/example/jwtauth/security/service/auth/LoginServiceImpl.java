package com.example.jwtauth.security.service.auth;

import com.example.jwtauth.global.exception.BadRequestException;
import com.example.jwtauth.security.payload.request.LoginRequest;
import com.example.jwtauth.security.service.utils.HashService;
import com.example.jwtauth.security.token.dto.TokensDto;
import com.example.jwtauth.security.token.refresh.exception.InvalidRefreshTokenException;
import com.example.jwtauth.security.token.refresh.model.RefreshToken;
import com.example.jwtauth.security.token.refresh.service.RefreshTokenService;
import com.example.jwtauth.security.token.service.TokenUtils;
import com.example.jwtauth.security.user.model.UserDetailsImpl;
import com.example.jwtauth.user.model.User;
import com.example.jwtauth.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.example.jwtauth.security.constraint.exception.AuthExceptionMessageConstants.BAD_CREDENTIALS;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final RefreshTokenService refreshTokenService;
    private final HashService hashService;

    @Override
    @Transactional(noRollbackFor = {BadCredentialsException.class})
    public TokensDto login(LoginRequest loginRequest) {
        User user = userService.findUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException(BAD_CREDENTIALS));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) Objects.requireNonNull(authentication).getPrincipal();

        ResponseCookie accessTokenCookie = tokenUtils.generateJwtCookieByUserId(userDetails.getId());
        ResponseCookie refreshTokenCookie = refreshTokenService.createRefreshTokenByUserId(userDetails.getId());

        log.debug("User logged in: id={}, username={}", user.getId(), user.getUsername());
        return new TokensDto(
                accessTokenCookie,
                refreshTokenCookie
        );
    }

    @Override
    @Transactional
    public TokensDto logout(HttpServletRequest request) {
        String refreshToken = tokenUtils.getRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            throw new BadRequestException();
        }
        String idFromRefreshToken;
        try {
            idFromRefreshToken = tokenUtils.getIdFromRefreshToken(refreshToken);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException();
        }
        long id = Long.parseLong(idFromRefreshToken);
        if (!refreshToken.isBlank() && refreshTokenService.existsById(id)) {
            refreshTokenService.deleteTokenById(id);
        }

        ResponseCookie jwtAccessCookie = tokenUtils.getCleanAccessTokenCookie();
        ResponseCookie jwtRefreshCookie = tokenUtils.getCleanRefreshTokenCookie();

        log.debug("User logged out");
        return new TokensDto(
                jwtAccessCookie,
                jwtRefreshCookie
        );
    }

    @Override
    @Transactional
    public TokensDto refreshToken(HttpServletRequest request) {
        String refreshToken = tokenUtils.getRefreshTokenFromCookies(request);

        if (refreshToken == null || refreshToken.isBlank()) {
            log.warn("Invalid refresh token");
            throw new InvalidRefreshTokenException();
        }
        boolean valid = tokenUtils.validateJwtRefreshToken(refreshToken);
        if (!valid) {
            throw new InvalidRefreshTokenException();
        }
        String idFromRefreshToken = tokenUtils.getIdFromRefreshToken(refreshToken);

        RefreshToken token =
                refreshTokenService.findById(idFromRefreshToken)
                        .orElseThrow(InvalidRefreshTokenException::new);

        boolean matches = hashService.verifySHA256Hash(refreshToken, token.getHashedToken());
        if (!matches) {
            log.warn("Refresh token hash doesnt match");
            throw new InvalidRefreshTokenException();
        }
        ResponseCookie newRefreshTokenCookie = tokenUtils.generateRefreshTokenCookie(token.getId());

        refreshTokenService.updateTokenValueAndEncode(newRefreshTokenCookie.getValue(), token.getId());

        ResponseCookie accessTokenCookie = tokenUtils.generateJwtCookieByUserId(token.getUser().getId());

        //
        log.debug("Refresh token: userId={}, tokenId={}", token.getUser().getId(), token.getId());
        return new TokensDto(
                accessTokenCookie,
                newRefreshTokenCookie);
    }


}

