package telran.java53.student.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java53.student.dao.StudentRepository;
import telran.java53.student.dto.ScoreDto;
import telran.java53.student.dto.StudentAddDto;
import telran.java53.student.dto.StudentDto;
import telran.java53.student.dto.StudentUpdateDto;
import telran.java53.student.dto.exceptions.StudentNotFoundException;
import telran.java53.student.model.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
	
	final StudentRepository studentRepository;

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
		return new StudentDto(id, student.getName(), student.getScores());
	}

	@Override
	public StudentDto removeStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
	    studentRepository.deleteById(id);
	    return new StudentDto(id, student.getName(), student.getScores());
	}

	@Override
	public StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
	    if (studentUpdateDto.getName() != null) {
	        student.setName(studentUpdateDto.getName());
	    }
	    if (studentUpdateDto.getPassword() != null) {
	        student.setPassword(studentUpdateDto.getPassword());
	    }
	    studentRepository.save(student);
	    return new StudentAddDto(id, student.getName(), student.getPassword());
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
//		Collection<Student> allStudents = studentRepository.findAll();
//	    List<Student> filteredStudents = allStudents.stream()
//	    		.filter(student -> student.getName().equalsIgnoreCase(name))
//	            .collect(Collectors.toList());
//
//	    return filteredStudents.stream()
//	    		.map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
//	            .collect(Collectors.toList());
		return studentRepository.findByNameIgnoreCase(name)
				.map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
				.toList();
	}

	@Override
	public Long getStudentsQuantityByNames(Set<String> names) {
//		Collection<Student> allStudents = studentRepository.findAll();
//	    long count = allStudents.stream()
//	    		.filter(student -> names.contains(student.getName()))
//	            .count();
//	    return count;
//		return studentRepository.findAll()
//				.stream()
//				.filter(s -> names.contains(s.getName()))
//				.count();
		return studentRepository.countByNameInIgnoreCase(names);
	}

	@Override
	public List<StudentDto> getStudentsByExamMinScore(String exam, Integer minScore) {
//		Collection<Student> allStudents = studentRepository.findAll();
//	    return allStudents.stream()
//	    		.filter(student -> {
//	    			Integer score = student.getScores().get(exam);
//	                return score != null && score >= minScore;
//	                })
//	    		.map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
//	            .collect(Collectors.toList()); 
		return studentRepository.findByExamAndScoreGreaterThan(exam, minScore)
				.map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
				.toList();
	}
	
}

