package org.airtribe.TaskMaster.service;



import org.airtribe.TaskMaster.dto.UserDTO;
import org.airtribe.TaskMaster.entity.User;
import org.airtribe.TaskMaster.entity.VerificationToken;
import org.airtribe.TaskMaster.repository.UserRepository;
import org.airtribe.TaskMaster.repository.VerificationTokenRepository;
import org.airtribe.TaskMaster.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Date;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    // using the UserDTO to register a new user
    public User registerUser(UserDTO user) {
        User dbUser = new User();
        dbUser.setUsername(user.getUsername());
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        dbUser.setIs_Enabled(false);
        dbUser.setRole("USER");
        // we cant directly save the user 
        // becoz we are using DTO to transfer data
        // so we need to convert the DTO to entity
        // and then save the entity to the database
        return userRepository.save(dbUser);
    }

    // this method is used by spring security to load the user by username
    // basically we are here plugging our database user repository with spring security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), 
        Collections.emptyList());
    }

    public void registerVerificationToken(User registeredUser, String generatedToken) {
        VerificationToken token = new VerificationToken();
        token.setUser(registeredUser);
        token.setToken(generatedToken);
        // Save the token to the database
        Long expiryDate = (new java.util.Date()).getTime() + 1000 * 60 * 60 * 24; // 24 hours in milliseconds
        token.setExpiryDate(new Date(expiryDate));
        verificationTokenRepository.save(token);
    }

    public boolean validateRegisteredToken(String token){
        VerificationToken registeredToken = verificationTokenRepository.findByToken(token);
        
        if(registeredToken == null){
            return false;
        }

        Long registeredTokenExpiryDate = registeredToken.getExpiryDate().getTime();

        if(registeredTokenExpiryDate < System.currentTimeMillis()){
            System.out.println("Token is expired");
            return false; // token is expired
        }
        return true;
    }

    public void enableUser(String token){
        // get the verification token from the verificationTokenRepository
        // then get the user entity from the token 
       VerificationToken registeredToken = verificationTokenRepository.findByToken(token);
        User registeredUser = registeredToken.getUser();
        registeredUser.setIs_Enabled(true);
        userRepository.save(registeredUser);
        verificationTokenRepository.delete(registeredToken);
    }

    public String loginUser(String userName, String password) {
        User registeredUser = userRepository.findByUsername(userName);
        if(registeredUser == null || !registeredUser.isIs_Enabled()) {
            return "User not found/ User not registered";
        }

        boolean isPasswordMatched = passwordEncoder.matches(password, registeredUser.getPassword());
        if(isPasswordMatched){
            return JwtUtil.generateToken(userName, registeredUser.getId()); // generate JWT token
        }
        return "user not authenticated, please check your credentials";
    }
}
