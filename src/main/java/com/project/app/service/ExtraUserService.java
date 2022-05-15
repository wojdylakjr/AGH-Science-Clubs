package com.project.app.service;

import com.project.app.domain.ExtraUser;
import com.project.app.domain.User;
import com.project.app.domain.enumeration.Blocks;
import com.project.app.domain.enumeration.Fields;
import com.project.app.repository.AuthorityRepository;
import com.project.app.repository.ExtraUserRepository;
import com.project.app.repository.UserRepository;
import com.project.app.service.dto.AdminUserDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExtraUserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final ExtraUserRepository extraUserRepository;

    private final CacheManager cacheManager;

    private final UserService userService;

    public ExtraUserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        ExtraUserRepository extraUserRepository,
        CacheManager cacheManager,
        UserService userService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.extraUserRepository = extraUserRepository;
        this.userService = userService;
    }

    public User registerExtraUser(AdminUserDTO userDTO, String password, Blocks block, Fields field) {
        User newUser = userService.registerUser(userDTO, password);
        //new extra user
        ExtraUser newExtraUser = new ExtraUser();
        newExtraUser.setUser(newUser);
        newExtraUser.setBlock(block);
        newExtraUser.setField(field);
        extraUserRepository.save(newExtraUser);
        log.debug("Created Information for ExtraUser: {}", newExtraUser);
        log.debug("Block:", block);
        log.debug("Field:", field);
        return newUser;
    }

    public ExtraUser createExtraUser(AdminUserDTO userDTO, Blocks block, Fields field) {
        log.debug("Dostaje: Created Information for userDTO: {}", userDTO);
        log.debug("Created Information for block: {}", block);
        User newUser = userService.createUser(userDTO);
        ExtraUser newExtraUser = new ExtraUser();
        newExtraUser.setUser(newUser);
        newExtraUser.setBlock(block);
        newExtraUser.setField(field);
        extraUserRepository.save(newExtraUser);
        log.debug("Created Information for ExtraUser: {}", newUser);
        log.debug("Block:", block);
        log.debug("Field:", field);

        return newExtraUser;
    }

    //    /**
    //     * Update all information for a specific user, and return the modified user.
    //     *
    //     * @param userDTO user to update.
    //     * @return updated user.
    //     */
    //    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
    //        return Optional
    //            .of(userRepository.findById(userDTO.getId()))
    //            .filter(Optional::isPresent)
    //            .map(Optional::get)
    //            .map(user -> {
    //                this.clearUserCaches(user);
    //                user.setLogin(userDTO.getLogin().toLowerCase());
    //                user.setFirstName(userDTO.getFirstName());
    //                user.setLastName(userDTO.getLastName());
    //                if (userDTO.getEmail() != null) {
    //                    user.setEmail(userDTO.getEmail().toLowerCase());
    //                }
    //                user.setImageUrl(userDTO.getImageUrl());
    //                user.setActivated(userDTO.isActivated());
    //                user.setLangKey(userDTO.getLangKey());
    //                Set<Authority> managedAuthorities = user.getAuthorities();
    //                managedAuthorities.clear();
    //                userDTO
    //                    .getAuthorities()
    //                    .stream()
    //                    .map(authorityRepository::findById)
    //                    .filter(Optional::isPresent)
    //                    .map(Optional::get)
    //                    .forEach(managedAuthorities::add);
    //                this.clearUserCaches(user);
    //                log.debug("Changed Information for User: {}", user);
    //                return user;
    //            })
    //            .map(AdminUserDTO::new);
    //    }
    public ExtraUser update(AdminUserDTO userDTO, Blocks block, Fields field) {
        //TODO: Check how to get normal user ID
        Optional<User> user = userRepository.findOneByLogin(userDTO.getLogin());
        userService.updateUser(userDTO);
        ExtraUser newExtraUser = new ExtraUser();
        newExtraUser.setBlock(block);
        newExtraUser.setField(field);
        extraUserRepository.save(newExtraUser);
        return newExtraUser;
    }

    public void deleteUser(Long id) {
        Optional<ExtraUser> user = extraUserRepository.findOneById(id);
        String login = user.get().getUser().getLogin();
        extraUserRepository
            .findOneById(id)
            .ifPresent(extraUser -> {
                extraUserRepository.delete(extraUser);
                log.debug("Deleted User: {}", extraUser);
            });
        userService.deleteUser(login);
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        //        SecurityUtils
        //            .getCurrentUserLogin()
        //            .flatMap(userRepository::findOneByLogin)
        //            .ifPresent(user -> {
        //                user.setFirstName(firstName);
        //                user.setLastName(lastName);
        //                if (email != null) {
        //                    user.setEmail(email.toLowerCase());
        //                }
        //                user.setLangKey(langKey);
        //                user.setImageUrl(imageUrl);
        //                this.clearUserCaches(user);
        //                log.debug("Changed Information for User: {}", user);
        //            });
    }
}
