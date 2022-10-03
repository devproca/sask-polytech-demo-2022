package ca.devpro.saskpolytech;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Student {
	@Id
	private Long studentId;
	private String name;
	private Integer graduationYear;
}
