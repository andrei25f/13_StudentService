package ait.cohort46.student.dao;

import ait.cohort46.student.model.Student;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface StudentRepository extends CrudRepository<Student, Long> {
    Stream<Student> findByNameIgnoreCase(String name);

    Stream<Student> getAllBy();

    Long countStudentsByNameIn(Set<String> names);

    @Query("{'scores.?0': {$gte: ?1}}")
    Stream<Student> findAllByExamMinScore(String exam, Integer minScore);
}
