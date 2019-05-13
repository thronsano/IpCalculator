package com.ipCalculator.MVC.services;

import com.ipCalculator.entity.db.User;
import com.ipCalculator.entity.exceptions.IpCalculatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserService extends PersistenceService<User> {

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return getObjectById("User", email);
    }

    public void changePassword(String currentPassword, String newPassword, String confirmNewPassword, PasswordEncoder passwordEncoder) throws IpCalculatorException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (passwordMatchesDatabase(email, currentPassword, passwordEncoder)) {
            if (newPassword.equals(confirmNewPassword)) {
                String hashedPassword = passwordEncoder.encode(newPassword);
                User user = getUserByEmail(email);

                user.setPassword(hashedPassword);

                persistObject(user);
            } else {
                throw new IpCalculatorException("Passwords do not match!");
            }
        } else {
            throw new IpCalculatorException("Incorrect password");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return getUserByEmail(currentUserName);
    }

    private boolean passwordMatchesDatabase(String email, String password, PasswordEncoder passwordEncoder) {
        User user = getUserByEmail(email);
        return passwordEncoder.matches(password, user.getPassword());
    }
}
