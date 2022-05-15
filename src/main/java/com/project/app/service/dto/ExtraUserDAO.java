package com.project.app.service.dto;

import com.project.app.domain.Authority;
import com.project.app.domain.User;
import com.project.app.domain.enumeration.Blocks;
import com.project.app.domain.enumeration.Fields;
import com.project.app.repository.AuthorityRepository;
import com.project.app.service.dto.AdminUserDTO;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.Size;
import javax.validation.constraints.Size;

public class ExtraUserDAO extends AdminUserDTO {

    private Blocks block;
    private Fields field;

    //    private  AuthorityRepository authorityRepository;

    public ExtraUserDAO() {}

    //
    //    public ExtraUserDAO(AuthorityRepository authorityRepository) {
    //        this.authorityRepository = authorityRepository;
    //    }

    public Blocks getBlock() {
        return block;
    }

    public Fields getField() {
        return field;
    }

    public AdminUserDTO getUser() {
        User newUser = new User();
        newUser.setEmail(this.getEmail());
        newUser.setLogin(this.getLogin());
        newUser.setFirstName(this.getFirstName());
        newUser.setLastName(this.getLastName());
        return new AdminUserDTO(newUser);
    }

    @Override
    public String toString() {
        return "ExtraUserDAO{" + super.toString() + "} ";
    }
}
