package top.watech.backmonitor.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.watech.backmonitor.entity.Student;
import top.watech.backmonitor.repository.StudRepository;



/**
 * Created by wuao.tp on 2018/7/9.
 */
@RestController
public class StudentController {
    @Autowired
    StudRepository studRepository;

    //测试数据//http://localhost:8080/student/1
    @GetMapping("/student/{id}")
    public Student getStudent(@PathVariable("id") Integer id){
        Student student = studRepository.findById(id).get();
        return student;
    }

    //测试数据 http://localhost:8080/student?lastName=haha&email=aa
    @GetMapping("/student")
    public Student insertStudent(Student student){
        Student save = studRepository.save(student);
        return save;
    }

//    public Student login(){
//
//    }

}
