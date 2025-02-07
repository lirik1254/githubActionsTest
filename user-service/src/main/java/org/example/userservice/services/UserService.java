package org.example.userservice.services;




import org.example.userservice.models.Users;
import org.example.userservice.repos.UserRepository;
import org.example.userservice.utils.UserCreationException;
import org.example.userservice.utils.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> getAllUsers() {
        List<Users> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found in the database.");
        }
        return users;
    }

    public Users createUser(Users user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UserCreationException("Не удалось создать пользователя");
        }
    }

    public Optional<Users> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Users updateUser(Integer id, Users user) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existingUser.setEmail(user.getPassword());
        }
        if (user.getRole() != null) {
            existingUser.setEmail(user.getRole());
        }
        if (user.getProfilePicture() != null) {
            existingUser.setEmail(user.getProfilePicture());
        }
        try {
            return userRepository.save(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user", e);  // Обработка ошибки сохранения
        }
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
