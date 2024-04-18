package proj.concert.service.domain;

import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.dto.PerformerDTO;

@Entity
@Table(name="CONCERTS")
public class Concert{

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="TITLE", nullable = false)
    private String title;

    @Column(name="IMAGE_NAME", nullable = false)
    private String imageName;

    @Column(name="BLURB", nullable = false)
    private String blrb;


    @ElementCollection
    @CollectionTable(name="CONCERT_DATES", joinColumns = @JoinColumn(name = "Concert_ID"))
    @Column(name="DATE")
    private List<LocalDateTime> dates = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "CONCERT_PERFORMER",
            joinColumns = @JoinColumn(name = "CONCERT_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERFORMER_ID")
    )
    private Set<Performer> performers = new HashSet<>();

    public Concert() {}

    public Concert(Long id, String title, String imageName, String blurb) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blrb = blurb;
    }

    public Concert(Long id, String title, String imageName) {
        this(id, title, imageName, null);
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title; }

    public String getImageName() { return imageName; }

    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getBlurb() { return blrb; }

    public void setBlurb( String blurb) { this.blrb = blurb; }

    public Set<LocalDateTime> getDates() {
        return new HashSet<>(dates);
    }

    public void setDates(List<LocalDateTime> dates) {
        this.dates = dates;
    }

    public Set<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Performer> performers) {
        this.performers = (Set<Performer>) performers;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Concert, id: ");
        buffer.append(id);
        buffer.append(", title: ");
        buffer.append(title);
        buffer.append(", image name: ");
        buffer.append(imageName);
        buffer.append(", blurb: ");
        buffer.append(blrb);

        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        // Implement value-equality based on a Concert's title alone. ID isn't
        // included in the equality check because two Concert objects could
        // represent the same real-world Concert, where one is stored in the
        // database (and therefore has an ID - a primary key) and the other
        // doesn't (it exists only in memory).
        if (!(obj instanceof Concert))
            return false;
        if (obj == this)
            return true;

        Concert rhs = (Concert) obj;
        return new EqualsBuilder().
                append(title, rhs.title).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(title).hashCode();
    }

}
