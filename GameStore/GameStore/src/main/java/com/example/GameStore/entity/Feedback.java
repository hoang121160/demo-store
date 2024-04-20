package com.example.GameStore.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "OrderDetail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String feedbackContent;

    @Temporal(TemporalType.DATE)
    private Date feedbackDate;

}
