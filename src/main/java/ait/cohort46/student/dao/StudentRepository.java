package ait.cohort46.student.dao;

import ait.cohort46.student.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);

    Optional<Student> findById(Long id);

    void deleteById(Long id);

    Iterable<Student> findAll();
}
