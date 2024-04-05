package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * Class to represent a User.
 * A User object has an ID (a database primary key value), a username, a password and a version number.
 */
@Entity
public class User {

    @Id
    @GeneratedValue()
    private Long id;
    private String username;
    private String password;
    private Long version;

    public User() { }

    public User(Long id, String username, String password, Long version) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.version = version;
    }

    public User(String username, String password) {
        this(null, username, password, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("User, id: ");
        buffer.append(id);
        buffer.append(", username: ");
        buffer.append(username);
        buffer.append(", password: ");
        buffer.append(password);
        buffer.append(", version: ");
        buffer.append(version);

        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;

        User rhs = (User) obj;
        return new EqualsBuilder().
                append(username, rhs.username).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(username).hashCode();
    }

}
