package ait.cohort46.student.service;

import ait.cohort46.student.dao.StudentRepository;
import ait.cohort46.student.dto.ScoreDto;
import ait.cohort46.student.dto.StudentAddDto;
import ait.cohort46.student.dto.StudentDto;
import ait.cohort46.student.dto.StudentUpdateDto;
import ait.cohort46.student.dto.exceptions.StudentNotFoundException;
import ait.cohort46.student.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceMockitoTest {
    @Mock
    private StudentRepository studentRepository;

    private ModelMapper modelMapper;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        studentService = new StudentServiceImpl(studentRepository, modelMapper);
    }

    @Test
    void testAddStudentWhenStudentExists() {
        // Arrange
        StudentAddDto dto = new StudentAddDto(1L, "John", "1234");
        when(studentRepository.existsById(dto.getId())).thenReturn(true);

        // Act
        Boolean result = studentService.addStudent(dto);

        // Assert
        assertFalse(result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testAddStudentWhenStudentNotExists() {
        // Arrange
        StudentAddDto dto = new StudentAddDto(1L, "John", "1234");
        when(studentRepository.existsById(dto.getId())).thenReturn(false);

        // Act
        Boolean result = studentService.addStudent(dto);

        // Assert
        assertTrue(result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testFindStudentWhenStudentExists() {
        // Arrange
        Student student = new Student(1L, "John", "1234");
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        // Act
        StudentDto result = studentService.findStudent(student.getId());

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void testFindStudentWhenStudentNotExists() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StudentNotFoundException.class, () -> studentService.findStudent(1L));
    }

    @Test
    void testRemoveStudent() {
        // Arrange
        Student student = new Student(1L, "John", "1234");
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        // Act
        StudentDto result = studentService.removeStudent(student.getId());

        // Assert
        assertNotNull(result);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateStudent() {
        // Arrange
        Student student = new Student(1L, "John", "1234");
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        StudentUpdateDto updateDto = new StudentUpdateDto("NewName", null);

        // Act
        StudentAddDto result = studentService.updateStudent(student.getId(), updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("NewName", result.getName());
        assertEquals("1234", result.getPassword());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testAddScore() {
        // Arrange
        Student student = new Student(1L, "John", "1234");
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        ScoreDto scoreDto = new ScoreDto("Math", 90);

        // Act
        Boolean result = studentService.addScore(student.getId(), scoreDto);

        // Assert
        assertNotNull(result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testFindStudentsByName() {
        // Arrange
        Student student = new Student(1L, "John", "1234");
        when(studentRepository.findByNameIgnoreCase(student.getName())).thenReturn(Stream.of(student));

        // Act
        List<StudentDto> result = studentService.findStudentsByName(student.getName());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(student.getName(), result.get(0).getName());
    }

    @Test
    void testGetStudentsQuantityByNames() {
        // Arrange
        Set<String> names = Set.of("John", "Peter");
        when(studentRepository.countStudentsByNameIn(names)).thenReturn(2L);

        // Act
        Long result = studentService.getStudentsQuantityByNames(names);

        // Assert
        assertEquals(2L, result);
    }

    @Test
    void testFindStudentsByExamMinScore() {
        // Arrange
        Student student = new Student(1L, "John", "1234");
        when(studentRepository.findAllByExamMinScore("Math", 90)).thenReturn(Stream.of(student));

        // Act
        List<StudentDto> result = studentService.findStudentsByExamMinScore("Math", 90);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(student.getName(), result.get(0).getName());
    }
}
