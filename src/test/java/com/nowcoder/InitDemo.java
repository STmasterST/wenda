package com.nowcoder;

import org.springframework.beans.factory.annotation.Autowired;

public class InitDemo {

    @Autowired
    static InitBean initBean;
    public static void main(String[] args) throws Exception {
        initBean.afterPropertiesSet();
    }
}

