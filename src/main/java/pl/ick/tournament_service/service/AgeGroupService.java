package pl.ick.tournament_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_service.entity.AgeGroup;
import pl.ick.tournament_service.exceptions.AgeGroupNotFoundException;
import pl.ick.tournament_service.model.answer.CreateAgeGroupAnswer;
import pl.ick.tournament_service.model.answer.EditAgeGroupAnswer;
import pl.ick.tournament_service.model.answer.GetAgeGroupAnswer;
import pl.ick.tournament_service.model.dto.AgeGroupDto;
import pl.ick.tournament_service.model.request.CreateAgeGroupRequest;
import pl.ick.tournament_service.model.request.EditAgeGroupRequest;
import pl.ick.tournament_service.repository.AgeGroupRepository;
import pl.ick.tournament_service.utils.mappers.AgeGroupMapper;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Transactional
public class AgeGroupService {

    private final AgeGroupRepository ageGroupRepository;
    private final AgeGroupMapper ageGroupMapper;

    public CreateAgeGroupAnswer createAgeGroup(CreateAgeGroupRequest request) {
        try{
            AgeGroup ag = ageGroupMapper.toEntity(request);
            AgeGroup saved = ageGroupRepository.save(ag);

            return new CreateAgeGroupAnswer(saved.getId(), "Age group created successfully");
        } catch (Exception e){
            throw new RuntimeException("Error during creating new Age Group: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public List<AgeGroupDto> getAllAgeGroups() {
        try {
            List<AgeGroup> ageGroups = ageGroupRepository.findAll();
            if(nonNull(ageGroups) && !ageGroups.isEmpty()) {
                return ageGroups
                        .stream()
                        .map(ageGroupMapper::toDto)
                        .toList();
            } else {
                throw new AgeGroupNotFoundException("No age groups found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting Age Groups: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public GetAgeGroupAnswer getAgeGroupInfo(Long id) {
        try {
            AgeGroup ag = getAgeGroupById(id);
            return ageGroupMapper.toGetAnswer(ag);
        } catch (Exception e) {
            throw new RuntimeException("Error during getting Age Group: {}", e);
        }
    }

    public EditAgeGroupAnswer editAgeGroup(Long id, EditAgeGroupRequest request) {
        try {
            AgeGroup ag = getAgeGroupById(id);
            ag.setName(request.name());
            ag.setMinAge(request.minAge());
            ag.setMaxAge(request.maxAge());

            AgeGroup updated = ageGroupRepository.save(ag);

            return new EditAgeGroupAnswer(updated.getId(), "Age group updated successfully");
        } catch (Exception e){
            throw new RuntimeException("Error during updating Age Group: {}", e);
        }
    }

    public void deleteAgeGroup(Long id) {
        try {
            AgeGroup ageGroup = getAgeGroupById(id);
            ageGroupRepository.delete(ageGroup);
        } catch (Exception e) {
           throw new RuntimeException("Error during deleting Age Group: {}", e) ;
        }
    }

    public AgeGroup getAgeGroupById(Long id) {
        try {
            return ageGroupRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Age group not found: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Error during getting Age Group: {}", e) ;
        }
    }

    public AgeGroup getCorrespondingAgeGroup(LocalDate birthDate) {
        if (nonNull(birthDate)) {
            int age = Period.between(birthDate, LocalDate.now()).getYears();

            List<AgeGroup> allGroups = ageGroupRepository.findAll();

            allGroups.sort(Comparator.comparingInt(AgeGroup::getMinAge));

            return allGroups.stream()
                    .filter(group -> age >= group.getMinAge() && age <= group.getMaxAge())
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException("No age group found for age " + age));
        } else {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
    }
}
