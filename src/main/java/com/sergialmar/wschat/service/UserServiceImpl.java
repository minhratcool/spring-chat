package com.sergialmar.wschat.service;

import com.sergialmar.wschat.domain.User;
import com.sergialmar.wschat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mac on 8/15/17.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByName(String name) {
        return userRepository.getByName(name);
    }
}
