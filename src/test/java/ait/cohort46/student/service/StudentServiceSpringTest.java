package ait.cohort46.student.service;

import ait.cohort46.configuration.ServiceConfiguration;
import ait.cohort46.student.dao.StudentRepository;
import ait.cohort46.student.dto.StudentAddDto;
import ait.cohort46.student.dto.StudentDto;
import ait.cohort46.student.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = ServiceConfiguration.class)
@SpringBootTest
public class StudentServiceSpringTest {
    private final long studentId = 1000;
    private Student student;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private StudentRepository studentRepository;

    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        student = new Student(studentId, "John", "1234");
        studentService = new StudentServiceImpl(studentRepository, modelMapper);
    }

    @Test
    void testAddStudent() {
        StudentAddDto studentAddDto = new StudentAddDto(studentId, "John", "1234");
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        assertTrue(studentService.addStudent(studentAddDto));
        assertThat(studentService.addStudent(studentAddDto)).isTrue();
    }

    @Test
    void testFindStudent() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));

        StudentDto studentDto = studentService.findStudent(studentId);

        assertThat(studentDto).isNotNull();
        assertNotNull(studentDto);
        assertEquals(studentId, studentDto.getId());
    }
}
