package com.gameworld.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Adress.
 */
@Entity
@Table(name = "adress")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "adress")
public class Adress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "street_name", nullable = false)
    private String streetName;

    @NotNull
    @Column(name = "house_no", nullable = false)
    private String houseNo;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Size(min = 6, max = 6)
    @Pattern(regexp = "[0-9]{2}\\-[0-9]{3}")
    @Column(name = "zip_code", length = 6, nullable = false)
    private String zipCode;

    @OneToOne(mappedBy = "adress")
    @JsonIgnore
    private GamerProfile gamerProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public Adress streetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public Adress houseNo(String houseNo) {
        this.houseNo = houseNo;
        return this;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getCity() {
        return city;
    }

    public Adress city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Adress zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public GamerProfile getGamerProfile() {
        return gamerProfile;
    }

    public Adress gamerProfile(GamerProfile gamerProfile) {
        this.gamerProfile = gamerProfile;
        return this;
    }

    public void setGamerProfile(GamerProfile gamerProfile) {
        this.gamerProfile = gamerProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Adress adress = (Adress) o;
        if(adress.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, adress.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Adress{" +
            "id=" + id +
            ", streetName='" + streetName + "'" +
            ", houseNo='" + houseNo + "'" +
            ", city='" + city + "'" +
            ", zipCode='" + zipCode + "'" +
            '}';
    }
}
