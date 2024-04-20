package com.example.GameStore.service.user;

import com.example.GameStore.entity.ChangePasswordRequest;
import com.example.GameStore.entity.Role;
import com.example.GameStore.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    User getUserById(Long userId);

    List<User> getAllUser();

    User createUser(User user);

    User updateUser(Long userId, User updatedUser);

    void deleteUser(Long userId);

    User login(String username, String password);

    boolean checkUserRole(User user, Role role);

    void changePassword(Long userId, ChangePasswordRequest request);

    //    private String genarateResetToken() {
    //        Random random = new Random();
    //        int min = 100000;
    //        int max = 999999;
    //        int reandomNumber= random.nextInt(max- min+1) + min;
    //        return String.format("%06d",reandomNumber);
    //    }
    void forgotPassword(String email);
}
