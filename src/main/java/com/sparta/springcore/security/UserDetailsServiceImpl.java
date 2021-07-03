package com.sparta.springcore.security;

import com.sparta.springcore.model.User;
import com.sparta.springcore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//스프링 시큐리티가 보내준 username을 갖고 DB를 확인해 회원이 있는지 여부를 확인함.
//findByUsername으로 찾아지지 않는다면 에러 발생시킴. 인증 불가.
//회원정보를 찾았다면, 회원정보를 UserDetailsImpl 안에 넣어주고, UserDetailsImpl을 만들어서(new) 스프링 시큐리티에 전달해줌.

@Service
public class UserDetailsServiceImpl implements UserDetailsService{ // UserDetailsService 인터페이스를 구현하고 있음. 그리고 UserDetailServiceImpl로 객체화 했음.

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));

        return new UserDetailsImpl(user);
    }
}