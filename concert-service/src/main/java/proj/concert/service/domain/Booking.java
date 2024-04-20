package proj.concert.service.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "BOOKING")
public class Booking {

	@Id
	@ManyToOne
	@JoinColumn(name = "CONCERT_ID", nullable = false)
	private Concert concert;

	@Id
	@Column(name = "DATE", nullable = false)
	private LocalDateTime date;

	@OneToMany(mappedBy = "BOOKING")
	private List<Seat> seats = new ArrayList<>();

	// default constructor
	public Booking() { }

	public Booking(Concert concert, LocalDateTime date, List<Seat> seats) {
		this.concert = concert;
		this.date = date;
		this.seats = seats;
	}

	// Getters & Setters
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
		return	Objects.equals(concert, booking.concert) &&
				Objects.equals(date, booking.date) &&
				Objects.equals(seats, booking.seats);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(concert)
				.append(date)
				.append(seats)
				.toHashCode();
	}
}
