package com.userauth.userauthenticate.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userauth.userauthenticate.dtos.SendEmailMessageDto;
import com.userauth.userauthenticate.exceptions.UserNotFoundException;
import com.userauth.userauthenticate.model.Token;
import com.userauth.userauthenticate.model.User;
import com.userauth.userauthenticate.repository.TokenRepository;
import com.userauth.userauthenticate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private UserRepository userrepos;

    @Autowired
    private TokenRepository tokenrepos;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    public User signup(String email, String password, String name){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        User savedUser = userrepos.save(user);

        SendEmailMessageDto messageDto = new SendEmailMessageDto();
        messageDto.setFrom("");
        messageDto.setTo(email);
        messageDto.setSubject("Welcome to Scaler");
        messageDto.setBody("Hi! Good to have you onboard, Looking forward on your achievements.");

        try {
            kafkaTemplate.send("sendEmail",
                    objectMapper.writeValueAsString(messageDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return savedUser;
    }

    public Token login(String email, String password) {
        Optional<User> userOptional = userrepos.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid User or Password");
        }

        User user = userOptional.get();
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid User or Password");
        }

        Token token = new Token();
        token.setUser(user);
        token.setValue(UUID.randomUUID().toString());
        Date expiredDate = getExpiredDate();
        token.setExpireAt(expiredDate);

        return tokenrepos.save(token);

    }

    //generating expirey date for created token
    private Date getExpiredDate() {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(new Date());
        calendarDate.add(Calendar.DAY_OF_MONTH, 30);
        Date expireDate = calendarDate.getTime();
        return expireDate;
    }

    //logging out and deleting token once we logout
    public void logout(String token) {
        Optional<Token> tokenOptional = tokenrepos.findByValueAndDeletedEquals(token, false);
        if (tokenOptional.isEmpty()) {
            throw new RuntimeException("Token is invalid");
        }
        Token tokenObject = tokenOptional.get();
        tokenObject.setDeleted(true);
        tokenrepos.save(tokenObject);
    }

    public boolean validateToken(String token) {
        /*
        To validate token
        1. check if token value is present
        2. check if token is not deleted
        3. check if token is not expired
        */

        Optional<Token> tokenOptional = tokenrepos.findByValueAndDeletedEqualsAndExpireAtGreaterThan(token, false, new Date());
        if (tokenOptional.isEmpty()) {
            return false;
        }
        return true;
    }

    public Boolean validateEmail(String email) {
        Optional<User> userOptional = userrepos.findByEmail(email);

        return userOptional.isPresent();
    }
}
