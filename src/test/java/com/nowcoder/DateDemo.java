package com.nowcoder;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

public class DateDemo {

    @Autowired
    private MessageService messageService;


    public void test(){

            List<Message> list = messageService.getConversationDetail("13_29", 0, 10);
            for (Message message : list) {
                System.out.println(message.getCreatedDate());
            }

    }
}
