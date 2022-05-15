package com.project.app.web.rest.vm;

import com.project.app.domain.enumeration.Blocks;
import com.project.app.domain.enumeration.Fields;
import com.project.app.service.dto.AdminUserDTO;
import javax.validation.constraints.Size;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends AdminUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private Blocks block;
    private Fields field;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public Blocks getBlock() {
        return block;
    }

    public Fields getField() {
        return field;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
