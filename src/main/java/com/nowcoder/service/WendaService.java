package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by nowcoder.
 */
@Service
public class WendaService {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
