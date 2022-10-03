package ca.devpro.saskpolytech;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentsController {

	private final StudentsRepository studentsRepository;

	@GetMapping
	public Iterable<Student> findAll() {
		return studentsRepository.findAll();
	}

	@GetMapping("/{id}")
	public Student getById(@PathVariable("id") Long studentId) {
		return studentsRepository.findById(studentId)
				.orElseThrow(() -> new StudentNotFoundException(studentId));
	}

	@PostMapping
	public Student create(@RequestBody Student student) {
		return studentsRepository.save(student);
	}

	@PutMapping("/{id}")
	public void update(@PathVariable("id") Long studentId, @RequestBody Student student) {
		student.setStudentId(studentId);
		studentsRepository.save(student);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long studentId) {
		studentsRepository.deleteById(studentId);
	}

	@GetMapping(params = "year")
	public Iterable<Student> findAll(@RequestParam("year") Integer year) {
		return studentsRepository.findAllByGraduationYear(year);
	}


}
