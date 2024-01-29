package com.example.jwtauth.security.token.refresh.service;

import com.example.jwtauth.security.service.utils.HashService;
import com.example.jwtauth.security.token.refresh.dao.repository.RefreshTokenRepository;
import com.example.jwtauth.security.token.refresh.model.RefreshToken;
import com.example.jwtauth.security.token.service.TokenUtils;
import com.example.jwtauth.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final HashService hashService;
    private final TokenUtils tokenUtils;

    @Transactional
    public RefreshToken saveToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void updateTokenValueAndEncode(String value, Long id) {
        String encode = hashService.hashStringWithSHA256(value);
        refreshTokenRepository.updateHashedTokenById(encode, id);
    }

    @Override
    public Optional<RefreshToken> findById(String id) {
        return refreshTokenRepository.findById(Long.valueOf(id));
    }

    @Override
    public boolean existsById(Long id) {
        return refreshTokenRepository.existsById(id);
    }

    @Override
    @Transactional
    public ResponseCookie createRefreshTokenByUserId(long userId) {
        User user = new User();
        user.setId(userId);
        RefreshToken refreshToken = new RefreshToken(
                user
        );
        RefreshToken tokenWithId = saveToken(refreshToken);
        ResponseCookie responseCookie = tokenUtils.generateRefreshTokenCookie(tokenWithId.getId());
        tokenWithId.setHashedToken(hashService.hashStringWithSHA256(responseCookie.getValue()));
        saveToken(tokenWithId);

        return responseCookie;
    }


    @Override
    @Transactional
    public void deleteTokenById(long id) {
        refreshTokenRepository.deleteById(id);
    }

}
