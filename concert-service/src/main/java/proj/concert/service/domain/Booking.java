package proj.concert.service.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "BOOKING")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;


	@ManyToOne
	@JoinColumn(name = "CONCERT_ID", nullable = false)
	private Concert concert;

	@Column(name = "DATE", nullable = false)
	private LocalDateTime date;

	@OneToMany(mappedBy = "booking")
	private List<Seat> seats = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	public Booking() {
		// Default constructor
	}

	public Booking(Concert concert, LocalDateTime date, List<Seat> seats, User user) {
		this.concert = concert;
		this.date = date;
		this.seats = seats;
		this.user = user;
	}

	// Getters & Setters
	public Concert getConcert() {
		return concert;
	}

	public void setConcert(Concert concert) {
		this.concert = concert;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// Other methods
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Booking booking = (Booking) o;
		return Objects.equals(concert, booking.concert) &&
				Objects.equals(date, booking.date) &&
				Objects.equals(seats, booking.seats) &&
				Objects.equals(user, booking.user);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(concert)
				.append(date)
				.append(seats)
				.append(user)
				.toHashCode();
	}
}
