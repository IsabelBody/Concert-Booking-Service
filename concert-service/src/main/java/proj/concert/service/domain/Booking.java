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

	@JoinColumn(name = "CONCERT_ID", nullable = false)
	private long concertId;

	@Column(name = "DATE", nullable = false)
	private LocalDateTime date;

	/* One booking to many seats.
	eager fetching as seats & booking are always used in conjunct,
	 so it is optimal to have that data pre-loaded. */
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
	private List<Seat> seats = new ArrayList<>();

	/*
	Each booking relates to one user.
	Fetch type is LAZY as user & booking objects are accessed independently sometimes,
	so data should not loaded until needed.

	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER", nullable = false)
	private User user;

	public Booking() {
		// Default constructor
	}

	public Booking(long concertId, LocalDateTime date, List<Seat> seats) {
		this.concertId = concertId;
		this.date = date;
		this.seats = seats;
	}


	// Getters & Setters
	public long getId() { return id; }

	public void setId(long id) { this.id = id; }
	public long getConcertId() {
		return concertId;
	}

	public void setConcertId(long concertId) {
		this.concertId = concertId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public List<Seat> getSeats() { return seats; }

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
		return Objects.equals(concertId, booking.concertId) &&
				Objects.equals(date, booking.date) &&
				Objects.equals(seats, booking.seats) &&
				Objects.equals(user, booking.user);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(concertId)
				.append(date)
				.append(seats)
				.append(user)
				.toHashCode();
	}
}
