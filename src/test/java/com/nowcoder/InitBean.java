package com.nowcoder;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class InitBean implements InitializingBean {


    {
        System.out.println("构造代码块");
    }

    public InitBean(){
        System.out.println("Construct");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init");
    }
}
