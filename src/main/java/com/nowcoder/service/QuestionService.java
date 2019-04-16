package com.nowcoder.service;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    /**
     * 从数据库中查询出最新的wenda数据Question
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    /**
     * 添加问题
     * @param question
     * @return
     */
    public int addQuestion(Question question){
        //敏感词过滤
        //过滤问题内容和标题中的html标签
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //实现敏感词过滤,使用字典树
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }


    //根据问题ID查询出问题
    public Question selectById(int id){
        return questionDAO.selectById(id);
    }

    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }

}
