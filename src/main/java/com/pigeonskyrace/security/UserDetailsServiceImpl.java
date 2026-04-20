package com.pigeonskyrace.security;

import com.pigeonskyrace.model.User;
import com.pigeonskyrace.model.enums.Role;
import com.pigeonskyrace.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        Role role = user.getRole() != null ? user.getRole() : Role.BREEDER;
        return new UserPrincipal(
                user.getId().toHexString(),
                user.getEmail(),
                user.getPassword(),
                role,
                List.of(new SimpleGrantedAuthority(role.getAuthority()))
        );
    }
}
