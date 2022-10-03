package ca.devpro.saskpolytech;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsRepository extends CrudRepository<Student, Long> {

	Iterable<Student> findAllByGraduationYear(Integer year);
}
