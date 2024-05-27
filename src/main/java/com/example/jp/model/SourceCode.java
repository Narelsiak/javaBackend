package com.example.jp.model;
import com.example.jp.model.Topics;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sourcecodes")
public class SourceCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "code", columnDefinition = "LONGTEXT", nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topics topic;
}
