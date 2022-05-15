package com.project.app.domain;

import com.project.app.domain.enumeration.Blocks;
import com.project.app.domain.enumeration.Fields;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A ExtraUser.
 */
@Entity
@Table(name = "extra_user")
public class ExtraUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "block")
    private Blocks block;

    @Enumerated(EnumType.STRING)
    @Column(name = "field")
    private Fields field;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExtraUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blocks getBlock() {
        return this.block;
    }

    public ExtraUser block(Blocks block) {
        this.setBlock(block);
        return this;
    }

    public void setBlock(Blocks block) {
        this.block = block;
    }

    public Fields getField() {
        return this.field;
    }

    public ExtraUser field(Fields field) {
        this.setField(field);
        return this;
    }

    public void setField(Fields field) {
        this.field = field;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExtraUser user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtraUser)) {
            return false;
        }
        return id != null && id.equals(((ExtraUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtraUser{" +
            "id=" + getId() +
            ", block='" + getBlock() + "'" +
            ", field='" + getField() + "'" +
            "}";
    }
}
