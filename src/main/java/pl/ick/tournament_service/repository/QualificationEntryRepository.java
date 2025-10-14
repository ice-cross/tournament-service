package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.QualificationEntry;

import java.util.List;

public interface QualificationEntryRepository extends JpaRepository<QualificationEntry, Long> {
    List<QualificationEntry> findByQualificationId(Long qualificationId);
}
