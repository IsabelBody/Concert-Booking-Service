package proj.concert.service.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SEATS")
public class Seat {

	@Id
	@Column(name = "label")
	private String label;

	@Column(name = "isBooked")
	private boolean isBooked;

	@Column(name = "date")
	private LocalDateTime date;

	@Column(name = "price")
	private BigDecimal price;

	public Seat() {}

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal price) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.price = price;
	}

	// getters and setters

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean booked) {
		isBooked = booked;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	// equals and hashCode methods

	// Override equals and hashCode methods
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Seat)) return false; // first check the type of object is a seat.
		Seat seat = (Seat) o; // we can now make it a seat because we know it is.
		return isBooked == seat.isBooked &&		// checking all the attributes match.
				Objects.equals(label, seat.label) &&
				Objects.equals(date, seat.date) &&
				Objects.equals(price, seat.price);
	}

	// we can now hash over seats in a list
	// needed for checking number of seats booked.
	@Override
	public int hashCode() {
		return Objects.hash(label, isBooked, date, price);
	}

	// could add compareTo method to query if object A.equals(object B)
	// could add toString() method to get description of seat value.
}
