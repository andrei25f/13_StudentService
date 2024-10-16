package ait.cohort46.student.service;

import ait.cohort46.student.dao.StudentRepository;
import ait.cohort46.student.dto.ScoreDto;
import ait.cohort46.student.dto.StudentAddDto;
import ait.cohort46.student.dto.StudentDto;
import ait.cohort46.student.dto.StudentUpdateDto;
import ait.cohort46.student.dto.exceptions.StudentNotFoundException;
import ait.cohort46.student.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public Boolean addStudent(StudentAddDto studentAddDto) {
        if (studentRepository.findById(studentAddDto.getId()).isPresent()) {
            return false;
        }
        Student student = new Student(studentAddDto.getId(), studentAddDto.getName(), studentAddDto.getPassword());
        studentRepository.save(student);
        return true;
    }

    @Override
    public StudentDto findStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        StudentDto studentDto = new StudentDto(student.getId(), student.getName(), student.getScores());
        return studentDto;
    }

    @Override
    public StudentDto removeStudent(Long id) {
        StudentDto studentDto = findStudent(id);
        studentRepository.deleteById(id);
        return studentDto;
    }

    @Override
    public StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        student.setName(studentUpdateDto.getName());
        student.setPassword(studentUpdateDto.getPassword());
        studentRepository.save(student);
        StudentAddDto studentAddDto = new StudentAddDto(student.getId(), student.getName(), student.getPassword());
        return studentAddDto;
    }

    @Override
    public Boolean addScore(Long id, ScoreDto scoreDto) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
        studentRepository.save(student);
        return res;
    }

    @Override
    public List<StudentDto> findStudentsByName(String name) {
        return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
                .toList();
    }

    @Override
    public Long getStudentsQuantityByNames(Set<String> names) {
        return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
                .filter(s -> names.stream().anyMatch(n -> n.equalsIgnoreCase(s.getName())))
                .count();
    }

    @Override
    public List<StudentDto> findStudentsByExamMinScore(String exam, Integer minScore) {
        return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
                .filter(s -> s.getScores().entrySet().stream()
                        .anyMatch(x -> x.getKey().equalsIgnoreCase(exam) && x.getValue() >= minScore))
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
                .toList();
    }
}
