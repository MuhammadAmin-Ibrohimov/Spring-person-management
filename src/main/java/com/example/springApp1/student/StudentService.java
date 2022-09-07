package com.example.springApp1.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository repository) {
        this.repository=repository;
    }

    public List<Student> getStudent(){
        return repository.findAll();
    }

    public void addNewStudent(Student student){
        Optional<Student> studentByEmail = repository.findStudentByEmail(student.getEmail());
        if(studentByEmail.isPresent()){
            throw new IllegalStateException("email taken");
        }
        repository.save(student);

    }

    public void deleteStudent(Long studentId) {
        boolean exist = repository.existsById(studentId);
        if(!exist){
            throw new IllegalStateException(
                    "student with " + studentId + " does not exist"
            );
        }
        repository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = repository.findById(studentId).orElseThrow(
                () -> new IllegalStateException(
                        "student with" + studentId + "does not exist"
                )
        );
        if(name != null && name.length() > 0 && !Objects.equals(student.getName(), name) ){
            student.setName(name);
        }
        if(email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email) ){
            Optional<Student> studentOptional = repository.findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException(
                        "email taken"
                );
            }
            student.setEmail(email);
        }

    }
}
