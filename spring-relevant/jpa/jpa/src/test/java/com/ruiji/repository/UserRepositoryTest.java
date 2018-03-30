package com.ruiji.repository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ruijie.config.AppConfig;
import com.ruijie.entity.User;
import com.ruijie.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
// 测试数据自动回滚
//@Transactional(transactionManager = "transactionManager", rollbackForClassName = "UserRepositoryTest")
public class UserRepositoryTest {
    @Autowired
    UserRepository repository;

    @Test
    public void sampleTestCase() {
        User user1 = new User();
        user1.setFirstname("wang");
        user1.setLastname("james");
        user1 = repository.save(user1);

        User user2 = new User();
        user2.setFirstname("li");
        user2.setLastname("amox");
        
        user2 = repository.save(user2);

        List<User> result = repository.findByLastname("james");
        System.out.println(result.size());
    }
}