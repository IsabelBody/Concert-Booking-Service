package proj.concert.service.domain;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.types.Genre;

@Entity
@Table(name="PERFORMERS")
public class Performer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name="IMAGE_NAME", nullable = false)
    private String imageName;

    @Column(name="GENRE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name="BLURB", columnDefinition="TEXT", nullable = false)
    private String blurb;


    public Performer() {
    }

    public Performer(Long id, String name, String imageName, Genre genre, String blurb) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.genre = genre;
        this.blurb = blurb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}

        if (obj == null || getClass() != obj.getClass()) { return false; }

        Performer other = (Performer) obj;

        return new EqualsBuilder()
                .append(id, other.id)
                .append(name, other.name)
                .append(imageName, other.imageName)
                .append(genre, other.genre)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(imageName)
                .append(genre)
                .toHashCode();
    }


    public int compareTo(Performer other) {
        return other.getName().compareTo(getName());
    }
}
