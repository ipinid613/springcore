package com.sparta.springcore.service;

import com.sparta.springcore.dto.SignupRequestDto;
import com.sparta.springcore.model.User;
import com.sparta.springcore.model.UserRole;
import com.sparta.springcore.repository.UserRepository;
import com.sparta.springcore.security.UserDetailsImpl;
import com.sparta.springcore.security.kakao.KakaoOAuth2;
import com.sparta.springcore.security.kakao.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//UserService에서 비즈니스 로직 처리. 회원가입의 요청 처리!
//주의할 점 : 회원중복 시 ID를 확인해서 같은 것이 있다면 등록 되지 않게, 관리자 가입 요청이 있다면 관리자 토큰 확인해서 가입시켜야 함.
@Service
//Repository의 기능을 사용해야 하기 때문에 repository bean 주입
public class UserService {
    //비밀번호 암호화 하는 passwordencoder 사용을 위해 씀.
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
    private final KakaoOAuth2 kakaoOAuth2;
    private final AuthenticationManager authenticationManager;

    //DI 주입 부분
    @Autowired
    public UserService(UserRepository userRepository, KakaoOAuth2 kakaoOAuth2, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        // passwordencoder도 사용할 것이기 때문에 di 주입 시킴!
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.authenticationManager = authenticationManager;
    }

    public void registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) { // findByUsername을 통해 찾은 값이 있다면(isPresent),
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }
        // 패스워드 인코딩. encode라는 함수를 사용하면 그 결과로서 복호화된 패스워드 나옴.
        String password = passwordEncoder.encode(requestDto.getPassword());
        //존재하지 않는다면 회원가입 계속 진행.
        String email = requestDto.getEmail();
        // 사용자 ROLE 확인. 기본값 : USER / ADMIN 아니고.
        UserRole role = UserRole.USER;
        // 만약 Admin 요청이 온다면~
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) { // 회원이 등록한 admintoken과 우리의 진짜 admintoken이 맞는지 여부 검사.
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다."); //equal이 아니라면, 에러메시지 송출.
            }
            role = UserRole.ADMIN; //관리자 암호가 맞다면 ADMIN role 부여.
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    public void kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        //DB에 중복된 kakao id가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        if (kakaoUser == null) {
        // 카카오 회원 DB에서 카카오 회원 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            User sameEmailUser = userRepository.findByEmail(email).orElse(null);
            if (sameEmailUser != null) { // 있다면
                // kakaoUser
                kakaoUser = sameEmailUser;
                // 이메일로 찾아온 회원정보에다가 kakao 서버에서 받아온 kakaoId를 추가해주는 것.
                kakaoUser.setKakaoId(kakaoId);
                // kakaoId가 추가된 회원정보를 저장하는 것
                userRepository.save(kakaoUser);
            }
            else { //처음 카카오를 통해 회원가입을 하는 경우
                // 우리 DB 에서 회원 Id 와 패스워드
                // 회원 Id = 카카오 nickname
                String username = nickname;
                // 패스워드 = 카카오 Id + ADMIN TOKEN. 어드민 토큰은 무의미한 문자열임.
                // 카카오 로그인 한 사용자가 카카오를 거치지 않고 일반 로그인 방식으로 접근하는 것을 방지하기 위해 어드민 토큰을 이어붙임!
                String password = kakaoId + ADMIN_TOKEN;
                // 패스워드 인코딩
                String encodedPassword = passwordEncoder.encode(password);
                UserRole role = UserRole.USER;
                kakaoUser = new User(username, encodedPassword, email, role, kakaoId);
                userRepository.save(kakaoUser);
            }
        }
        // 로그인 처리
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}