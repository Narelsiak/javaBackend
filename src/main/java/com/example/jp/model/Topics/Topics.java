package com.example.jp.model.Topics;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topics")
public class Topics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade= CascadeType.ALL)
    @JsonIgnoreProperties("topic")
    private List<SourceCode> sourceCodeList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade= CascadeType.ALL)
    @JsonIgnoreProperties("topic")
    private List<Link> linkList;
}
