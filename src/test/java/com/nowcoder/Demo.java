package com.nowcoder;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Demo  implements BeanPostProcessor{

    int a = 10;

    public static void main(String[] args) {
//        newClassPathXmlApplicationContext().get
        new Demo();
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        System.out.println("初始化");
        return this;
    }
}
