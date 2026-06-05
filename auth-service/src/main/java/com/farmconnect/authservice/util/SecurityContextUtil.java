package com.farmconnect.authservice.util;

import com.farmconnect.authservice.security.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    public static CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CurrentUser) {
            return (CurrentUser) authentication.getPrincipal();
        }
        return null;
    }

    public static Long getUserId() {
        CurrentUser user = getCurrentUser();
        return user != null ? user.userId() : null;
    }

    public static String getEmail() {
        CurrentUser user = getCurrentUser();
        return user != null ? user.email() : null;
    }

    public static String getRole() {
        CurrentUser user = getCurrentUser();
        return user != null ? user.role() : null;
    }
}
