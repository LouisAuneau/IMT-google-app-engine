package com.zenika.zencontact.resource.auth;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthenticationService {

    private static AuthenticationService INSTANCE = new AuthenticationService();
    private UserService userService = UserServiceFactory.getUserService();

    public static AuthenticationService getInstance() {
        return INSTANCE;
    }

    public String getLoginURL(String url) {
        return userService.createLoginURL(url);
    }

    public String getLogoutURL(String url) {
        return userService.createLogoutURL(url);
    }

    public boolean isAdmin() {
        return userService.isUserAdmin();
    }

    public User getUser() {
        return userService.getCurrentUser();
    }
}