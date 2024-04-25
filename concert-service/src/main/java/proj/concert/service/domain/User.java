package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to represent a User.
 * A User object has an ID (a database primary key value), a username, a password and a version number.
 */
@Entity
@Table(name="USERS")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "VERSION")
    private Long version;

    @OneToMany(mappedBy = "user")
    private Set<Booking> bookings = new HashSet<>();

    @Column(name = "TOKEN")
    private String token;

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

    public Set<Booking> getBookings() {
        return Collections.unmodifiableSet(bookings);
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void setToken(String token) {this.token = token; }

    public String getToken() { return token; }

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
                append(password, rhs.password).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(username).
                append(password).
                hashCode();
    }

}
