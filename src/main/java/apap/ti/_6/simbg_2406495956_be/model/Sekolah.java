package apap.ti._6.simbg_2406495956_be.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sekolah")
@Getter
@Setter
@NoArgsConstructor
public class Sekolah {
    @Id
    private String id; // SKLxxxx

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String educationLevel; // SD, SMP, SMA, SMK

    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}