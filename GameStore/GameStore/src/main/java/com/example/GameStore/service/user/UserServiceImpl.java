package com.example.GameStore.service.user;

import com.example.GameStore.entity.*;
import com.example.GameStore.exeption.InvalidException;
import com.example.GameStore.repository.UserRepository;
import com.example.GameStore.service.emailMessage.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
//    private static final int RESET_TOKEN_EXPIRY_DURATION_IN_HOURS = 1;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public User createUser(User user) {
        validateUserData(user);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Tên người dùng đã tồn tại!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(Role.USER);
        return userRepository.save(user);
    }

    private void validateUserData(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Người dùng không được null");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được trống");
        }
        if (user.getUsername().length() < 6 || user.getUsername().length() > 10) {
            throw new IllegalArgumentException("Tên người dùng phải có từ 6 đến 10 ký tự");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được trống");
        }
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu không đáp ứng yêu cầu: ít nhất một chữ hoa, một số, một chữ thường và một ký tự đặc biệt");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email không được trống");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new EntityNotFoundException("Không tìm thấy người dùng với id: " + userId);
        }
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với id: " + userId);
        }
        validateUserData(updatedUser);
        User existingUser = userOptional.get();
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("Tài khoản không tồn tại !");
        }
        User user = userOptional.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new EntityNotFoundException("Mật khẩu không chính xác !");
        }
    }

    @Override
    public boolean checkUserRole(User user, Role role) {
        return false;
    }

    // change password
    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        if (!verifyOldPassword(userId, request.getOldPassword())) {
            throw new InvalidException("Mật khẩu cũ không chính xác.");
        } else {
            if (isValidPassword(request.getNewPassword())) {
                Optional<User> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    userRepository.save(user);
                } else {
                    throw new EntityNotFoundException("Không tìm thấy người dùng với id: " + userId);
                }
            } else {
                throw new InvalidException("Mật khẩu không đáp ứng yêu cầu: ít nhất một chữ hoa, một số, một chữ thường và một ký tự đặc biệt !");
            }
        }
    }

    // check password
    private boolean verifyOldPassword(Long userId, String oldPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(oldPassword, user.getPassword());
        }
        return false;
    }

//    public void forgotPassword(String email) {
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            String resetToken = genarateResetToken();
//            LocalDateTime expiryTime = LocalDateTime.now().plusHours(RESET_TOKEN_EXPIRY_DURATION_IN_HOURS);
//            user.setResetToken(resetToken);
//            user.setResetTokenExpiryTime(expiryTime);
//            userRepository.save(user);
//
//            // gui email xac thuc
//            EmailMessage emailMessage = new EmailMessage();
//            emailMessage.setTo(email);
//            emailMessage.setSubject("Yêu cầu đặt lại mật khẩu");
//            emailMessage.setBody("Để đặt lại mật khẩu của bạn, vui lòng sử dụng mã sau: " + resetToken);
//            emailService.sendEmail(emailMessage);
//
//        } else {
//            throw new EntityNotFoundException("Email không tồn tại !");
//        }
//    }
//    public void resetPassword(ResetPasswordRequest request){
//        Optional<User> userOptional = userRepository.findByEmail(request.getEmal());
//        if (userOptional.isPresent()){
//            User user = userOptional.get();
//
//            if (user.getResetToken() == null || !user.getResetToken().equals(request.getResetToken())){
//                throw new IllegalArgumentException("Reset token không hợp lệ !");
//            }
//            LocalDateTime curentDateTime = LocalDateTime.now();
//            if (user.getResetTokenExpiryTime() ==  null || curentDateTime.isAfter(user.getResetTokenExpiryTime())){
//                throw new IllegalArgumentException("Reset token đã hết hạn !");
//            }
//            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//            user.setResetToken(null);
//            user.setResetTokenExpiryTime(null);
//            userRepository.save(user);
//        }else {
//            throw new EntityNotFoundException("Ngưởi dùng không tồn tại !");
//        }
//    }


    //    private String genarateResetToken() {
//        Random random = new Random();
//        int min = 100000;
//        int max = 999999;
//        int reandomNumber= random.nextInt(max- min+1) + min;
//        return String.format("%06d",reandomNumber);
//    }
    @Override
    public void forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            //random password
            String newPassword = generateRandomPassword();

            //save
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // send email
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setTo(email);
            emailMessage.setSubject("Reset password");
            emailMessage.setBody("Your new password: " + newPassword);
            emailService.sendEmail(emailMessage);
        } else {
            throw new EntityNotFoundException("Email không tồn tại !");
        }
    }

    private String generateRandomPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%&_/?";
        String allChars = upperCaseLetters + lowerCaseLetters + numbers + specialChars;
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return password.toString();
    }
}
