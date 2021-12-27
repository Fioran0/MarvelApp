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


@Table(name = "Characters", schema = "public")
@Data
@Entity
@AllArgsConstructor
public class Characters implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "character_id")
    private Long id;

    @Column(name = "name")
    private String characterName;

    @Column(name = "description")
    private String characterDescription;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "modified_time")
    private Date modifiedTime = new Date();

    @Column(name = "resourceURL")
    private String resourceURL;

//    @Column(name = "urls")
//    private List<String> urls;

/*    @Lob
    @Column (name = "image")
    private byte[] characterImage;*/

    @ManyToMany (mappedBy = "charactersSet"
/*            , fetch = FetchType.LAZY,
                    cascade = {
                        CascadeType.PERSIST,
                            CascadeType.MERGE*/
//                    }
                    )
    @JsonIgnoreProperties("characterSet")
    private Set<Comics> comicsSet;

    public Characters() {
    }

    public Characters(String characterName, String characterDescription, Date modifiedTime, String resourceURL, Set<Comics> comicsSet) {
        this.characterName = characterName;
        this.characterDescription = characterDescription;
        this.modifiedTime = modifiedTime;
        this.resourceURL = resourceURL;
        this.comicsSet = comicsSet;
    }

    @JsonBackReference
    public Set<Comics> getComicsSet() {
        return comicsSet;
    }
}
