package ca.devpro.saskpolytech;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentsControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testListAll_FourStudents_ExpectFourResults() {
		ResponseEntity<List<Student>> response = restTemplate.exchange("/api/students", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(4, response.getBody().size());
	}

	@Test
	void testGet_StudentExists_ExpectStudentReturnedWithCorrectMappings() {
		ResponseEntity<Student> response = restTemplate.getForEntity("/api/students/1", Student.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());

		Student student = response.getBody();
		assertEquals(1, student.getStudentId());
		assertEquals("Jim", student.getName());
		assertEquals(2007, student.getGraduationYear());
	}

	@Test
	void testGet_StudentDoesNotExist_Expect404NotFound() {
		ResponseEntity<Student> response = restTemplate.getForEntity("/api/students/9999", Student.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testCreate_ValidStudent_ExpectCreatedWithId() {
		Student student = new Student();
		student.setName("Bob");
		student.setGraduationYear(2007);

		ResponseEntity<Student> response = restTemplate.postForEntity("/api/students", student, Student.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());

		Student createdStudent = response.getBody();
		assertNotNull(createdStudent.getStudentId());
		assertEquals("Bob", createdStudent.getName());
		assertEquals(2007, createdStudent.getGraduationYear());
	}

	@Test
	void testUpdate_UpdateName_ExpectNameChanged() {
		Student student = restTemplate.getForObject("/api/students/1", Student.class);
		student.setName("James");

		restTemplate.put("/api/students/1", student);

		student = restTemplate.getForObject("/api/students/1", Student.class);
		assertEquals(1, student.getStudentId());
		assertEquals("James", student.getName());
	}

	@Test
	void testDelete_StudentDeleted_ExpectNotFoundOnGet() {
		restTemplate.delete("/api/students/1");

		ResponseEntity<Student> response = restTemplate.getForEntity("/api/students/1", Student.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
