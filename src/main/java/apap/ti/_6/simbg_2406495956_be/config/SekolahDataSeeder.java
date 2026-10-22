package apap.ti._6.simbg_2406495956_be.config;

import apap.ti._6.simbg_2406495956_be.model.Sekolah;
import apap.ti._6.simbg_2406495956_be.repository.SekolahRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class SekolahDataSeeder implements CommandLineRunner {

    private final SekolahRepository sekolahRepository;

    public SekolahDataSeeder(SekolahRepository sekolahRepository) {
        this.sekolahRepository = sekolahRepository;
    }

    @Override
    public void run(String... args) {
        if (sekolahRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(Locale.of("id", "ID"));
        String[] levels = {"SD", "SMP", "SMA", "SMK"};
        LocalDateTime now = LocalDateTime.now();

        for (int i = 1; i <= 10; i++) {
            String level = levels[faker.random().nextInt(levels.length)];
            Sekolah s = new Sekolah();
            s.setId(String.format("SKL%04d", i));
            s.setNama(level + " " + faker.address().cityName());
            s.setEducationLevel(level);
            s.setAddress(faker.address().fullAddress());
            s.setCreatedAt(now);
            s.setUpdatedAt(now);
            sekolahRepository.save(s);
        }
    }
}