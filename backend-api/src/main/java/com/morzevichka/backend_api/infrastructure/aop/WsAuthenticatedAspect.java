package com.morzevichka.backend_api.infrastructure.aop;

import com.morzevichka.backend_api.infrastructure.exception.user.UserNotFoundInContextException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Objects;

@Aspect
@Component
public class WsAuthenticatedAspect {

    @Before("@annotation(WsAuthenticated) && args(principal, ..)")
    public void checkAuthenticated(Principal principal) {
        if (Objects.isNull(principal)) {
            throw new UserNotFoundInContextException();
        }
    }
}
