package org.example.documentservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents", schema = "public")
@Getter
@Setter
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer documentId;

    private String title;
    private String fileUrl;
    private String category;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    private Integer authorId;
}
