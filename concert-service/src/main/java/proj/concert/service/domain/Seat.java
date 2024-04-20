package proj.concert.service.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "SEATS")
public class Seat {

	@Id
	@Column(name = "LABEL", nullable = false)
	private String label;

	@Column(name = "PRICE", nullable = false)
	private BigDecimal price;

	@ManyToOne
	@JoinColumn(name = "BOOKING")
	private Booking booking;

	public Seat() {}

	public Seat(String label, BigDecimal price) {
		this.label = label;
		this.price = price;
	}

	// getters and setters

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

	// equals and hashCode methods
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Seat)) return false; // first check the type of object is a seat.
		Seat seat = (Seat) o;
		return 		// checking all the attributes match.
				Objects.equals(label, seat.label) &&
				Objects.equals(price, seat.price);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(label)
				.append(price)
				.toHashCode();
	}

}
