package com.testCase.MarvelApp.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "Comics", schema = "public")
@Data
@Entity
@AllArgsConstructor
@EqualsAndHashCode(exclude = "charactersSet")
public class Comics implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column(name = "comic_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "issue_number")
    private Double issueNumber;

    @Column(name = "description")
    private String description;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "modified_time")
    private Date modifiedTime = new Date();

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "resourceURL")
    private String resourceURL;

//    private List<String> urls;

/*    @Lob
    @Column (name = "comic_image")
    private byte[] comicImage;*/

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.ALL
//                CascadeType.PERSIST,
//                    CascadeType.MERGE
            })
    @JoinTable(
            name = "Comics_characters",
            joinColumns = @JoinColumn(name = "comics_id"),
            inverseJoinColumns = @JoinColumn(name = "characters_id"))
    @JsonIgnoreProperties("comicsSet")
//    @JsonManagedReference
    private Set<Characters> charactersSet;

    @Column(name = "comic_price")
    private Float price;

    public Comics() {
    }

    public Comics(String title, Double issueNumber, String description, Date modifiedTime, Integer pageCount, String resourceURL, Set<Characters> charactersSet, Float price) {
        this.title = title;
        this.issueNumber = issueNumber;
        this.description = description;
        this.modifiedTime = modifiedTime;
        this.pageCount = pageCount;
        this.resourceURL = resourceURL;
        this.charactersSet = charactersSet;
        this.price = price;
    }
    public Comics(Comics comics){
        this.title = comics.getTitle();
        this.issueNumber = comics.getIssueNumber();
        this.description = comics.getDescription();
        this.modifiedTime = comics.getModifiedTime();
        this.pageCount = comics.getPageCount();
        this.resourceURL = comics.getResourceURL();
        this.price = comics.getPrice();
    }

    @JsonBackReference
    public Set<Characters> getCharactersSet() {
        return charactersSet;
    }
}
