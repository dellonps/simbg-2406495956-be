package apap.ti._6.simbg_2406495956_be.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sekolah")
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

    public Sekolah() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEducationLevel() { return educationLevel; }
    public void setEducationLevel(String educationLevel) { this.educationLevel = educationLevel; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}