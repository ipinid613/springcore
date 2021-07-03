package com.sparta.springcore.service;

import com.sparta.springcore.dto.SignupRequestDto;
import com.sparta.springcore.model.User;
import com.sparta.springcore.model.UserRole;
import com.sparta.springcore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//UserService에서 비즈니스 로직 처리. 회원가입의 요청 처리!
//주의할 점 : 회원중복 시 ID를 확인해서 같은 것이 있다면 등록 되지 않게, 관리자 가입 요청이 있다면 관리자 토큰 확인해서 가입시켜야 함.
@Service
//Repository의 기능을 사용해야 하기 때문에 repository bean 주입
public class UserService {
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) { // findByUsername을 통해 찾은 값이 있다면(isPresent),
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }
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
}