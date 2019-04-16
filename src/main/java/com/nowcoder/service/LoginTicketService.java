package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginTicketService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private LoginTicketDAO loginTicketDAO;


}
