package proj.concert.service.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@IdClass(BookingKey.class)
@Table(name = "BOOKING")
public class Booking {

	// primary key 1
	@Id
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID") // foreign key. is this done correctly?
	private User user;

	// primary key 2
	@Id
	@ManyToOne // TODO: Is this the right relationship?
	@JoinColumn(name = "CONCERT_ID")
	private Concert concert; // will give error until jason implements his class.

	// primary key 3
	@Id
	@Column(name = "date")
	private LocalDateTime date;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "booking")
	private List<Seat> seats = new ArrayList<>();

	// default constructor
	public Booking() { }

	public Booking(Concert concert, LocalDateTime date, List<Seat> seats, User user) {
		this.user = user;
		this.concert = concert;
		this.date = date;
		this.seats = seats;
	}

	// Getters & Setters
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Concert getConcert() {
		return concert;
	}

	public void setConcert(Concert concert) {
		this.concert = concert;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	// other methods
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Booking booking = (Booking) o;
		return Objects.equals(user, booking.user) &&
				Objects.equals(concert, booking.concert) &&
				Objects.equals(date, booking.date) &&
				Objects.equals(seats, booking.seats);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(user)
				.append(concert)
				.append(date)
				.append(seats)
				.toHashCode();
	}
}
