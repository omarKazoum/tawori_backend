package com.tawori.spaceManagment.service.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tawori.spaceManagment.dto.AuthDTO;
import com.tawori.spaceManagment.entity.Client;
import com.tawori.spaceManagment.entity.Owner;
import com.tawori.spaceManagment.entity.RoleEnum;
import com.tawori.spaceManagment.entity.User;
import com.tawori.spaceManagment.exceptions.RegisterException;
import com.tawori.spaceManagment.repository.UserRepository;
import com.tawori.spaceManagment.utils.JwtUtil;
import com.tawori.spaceManagment.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtil jwtUtil;
    ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser=userRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            throw new UsernameNotFoundException("could not find user with email "+email);

        User user=optionalUser.get();

        return new org.springframework.security.core.userdetails.User(email,
                user.getPassword(),
                SecurityUtils.getAuthoritiesForRole(user.getRole()));
    }
    public AuthDTO register(final AuthDTO userDTO) throws RegisterException {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent())
            throw new RegisterException("email already in use");

        if (userDTO.getRole().equals(RoleEnum.CLIENT)) {
            //if a client is registering
            Client c = new Client();
            c.setEmail(userDTO.getEmail());
            c.setFirstName(userDTO.getFirstName());
            c.setLastName(userDTO.getLastName());
            c.setBanned(false);
            c.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            c = userRepository.save(c);

            userDTO.setPassword(null);
            userDTO.setJwtToken(jwtUtil.generateToken(userDTO.getEmail()));
            return userDTO;
        }
        if (userDTO.getRole().equals(RoleEnum.OWNER)) {
            //if a supplier is registering
            Owner s = new Owner();
            s.setEmail(userDTO.getEmail());
            s.setFirstName(userDTO.getFirstName());
            s.setLastName(userDTO.getLastName());
            s.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            s = userRepository.save(s);
            userDTO.setPassword(null);
            userDTO.setJwtToken(jwtUtil.generateToken(userDTO.getEmail()));
            return userDTO;
        }

        throw new UsernameNotFoundException("invalid user role");
    }
    public AuthDTO login(AuthDTO loginDTO) throws AuthenticationCredentialsNotFoundException{
            UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);
            final AuthDTO responseDto=new AuthDTO();
            User u=userRepository.findByEmail(loginDTO.getEmail()).get();
            responseDto.setFirstName(u.getFirstName());
            responseDto.setLastName(u.getLastName());
            responseDto.setRole(u.getRole());
            String jwtToken=jwtUtil.generateToken(loginDTO.getEmail());
            responseDto.setJwtToken(jwtToken);
            return responseDto;
    }
}
