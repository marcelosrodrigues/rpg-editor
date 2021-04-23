package com.pmrodrigues.rpg.services.security.helpers;

import com.pmrodrigues.rpg.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationHelper {

    public User getLoggedUser() {

        log.info("trying to get logged user");

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth instanceof AnonymousAuthenticationToken){
            log.debug("not logged user");
            return null;
        } else {
            log.debug("logged");
            return (User)auth.getPrincipal();
        }

    }
}
