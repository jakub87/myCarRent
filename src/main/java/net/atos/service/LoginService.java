package net.atos.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class LoginService {

    public String getLoginFromCredentials(Authentication auth){
        return Optional.ofNullable(auth).map(
                (optional) ->
                {
                    UserDetails userDetails = (UserDetails) auth.getPrincipal();
                    return userDetails.getUsername();
                }
        ).orElse("Guest!");
    }


    public boolean isAdmin(Authentication auth){
        return Optional.ofNullable(auth).map(
                (optional) ->
                {
                    UserDetails userDetails = (UserDetails) auth.getPrincipal();
                    Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) userDetails.getAuthorities();
                    if(authorities.toString().contains("ROLE_ADMIN")){
                        return true;
                }
                    return false;

                }
                ).orElse(false);
    }

}
