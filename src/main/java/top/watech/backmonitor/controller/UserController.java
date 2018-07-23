package top.watech.backmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import top.watech.backmonitor.entity.User;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.UserService;

import javax.persistence.criteria.*;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fhm on 2018/7/19.
 */
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/users/{username}")
    public List<User> findUser1(@PathVariable("username") String username){
        List<User> users = this.userRepository.findByUsernameContaining(username);
        return users;
    }

    @GetMapping("/specuser")
    public User findUser2(String username,String userpwd){
        User user = userRepository.getByUsernameIsAndUserpwdIs(username,userpwd);
        return user;
    }

    @GetMapping("/insert")
    public User userSave(User user){
        User save = userRepository.save(user);
//        userRepository.getByUsernameIsAndUserpwdIs()
        return save;
    }

    @Autowired
    private UserService userService ;
    @GetMapping("/insertAll")
    public void testSaveAll(){
        List<User> users = new ArrayList<>();

        for (int i = 'a' ; i<'z' ; i ++){
            User user = new User();
            user.setUserId(i+3);

            user.setUsername((char)i + "" + (char)i);
            user.setUserpwd("111111");

            users.add(user);
        }
        userService.saveUsers(users);
    }

    @GetMapping("/updateUser/{userId,username}")
    public void updateUser(@PathVariable("userId") Long userId,@PathVariable("username") String username){
        userService.updateUsers(userId,username);
    }

    @GetMapping("/Page")
    public void testPage(){

        int pageNo = 2;
        int pageSize = 5;

        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC,"userId");
        Sort sort = new Sort(order1);

        PageRequest request = new PageRequest(pageNo, pageSize, sort);

        Page<User> page = userRepository.findAll(request);

        System.out.println("总记录数: " + page.getTotalElements());
        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("当前页面的 List: " + page.getContent());
        System.out.println("当前页面的记录数: " + page.getNumberOfElements());
    }

    //带条件分页
    @GetMapping("/Page2")
    public void testPage2() {

        int pageNo = 2;
        int pageSize = 5;

        PageRequest request = new PageRequest(pageNo,pageSize);

        //使用Specification的匿名内部类
        Specification<User> specification = new Specification<User>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path path = root.get("id");
                Predicate predicate = criteriaBuilder.le(path,13);
                return predicate;
            }
        };

        Page page = userRepository.findAll(request);

        System.out.println("总记录数: " + page.getTotalElements());
        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("当前页面的 List: " + page.getContent());
        System.out.println("当前页面的记录数: " + page.getNumberOfElements());
    }
}
