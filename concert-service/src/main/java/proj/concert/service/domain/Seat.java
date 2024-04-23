package proj.concert.service.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import proj.concert.common.types.BookingStatus;

@Entity
@Table(name = "SEATS")
public class Seat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "LABEL", nullable = false)
	private String label;

	@Column(name = "PRICE", nullable = false)
	private BigDecimal price;

	@Column(name = "ISBOOKED", nullable = false)
	@Enumerated(EnumType.STRING)
	private BookingStatus isBooked;

	@Column(name = "DATE")
	private LocalDateTime date;

	public Seat() {
	}

	public Seat(String label, BigDecimal price) {
		this.label = label;
		this.price = price;
	}

	public Seat(String label, BookingStatus isBooked, LocalDateTime date, BigDecimal price) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.price = price;
	}

	// Getters and setters

	public long getId() { return id; }

	public void setId(long id) { this.id = id; }

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BookingStatus getIsBooked() {
		return isBooked;
	}

	public void setIsBooked(BookingStatus isBooked) {
		this.isBooked = isBooked;
	}

	public LocalDateTime getDate() { return date; }

	public void setDate(LocalDateTime date) { this.date = date; }

	// Equals and hashCode methods
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Seat)) return false;
		Seat seat = (Seat) o;
		return Objects.equals(label, seat.label) &&
				Objects.equals(price, seat.price) &&
				isBooked == seat.isBooked;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(label)
				.append(price)
				.append(isBooked)
				.toHashCode();
	}
}
