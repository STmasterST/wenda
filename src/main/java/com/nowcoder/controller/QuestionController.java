package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);


    @Autowired
    private QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;


    //用户提问Controller
    //标题And内容
    @RequestMapping(value = "/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content){
        try{
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);//问题初始评论数为0条评论
            //如果用户未登录,设置为匿名用户
            if(hostHolder.getUser() == null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else{
                //设置用户id,从ThreadLocal中获取
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){
                //返回JSON格式字符串
                //返回0表示提问成功
                return WendaUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("添加问题失败 " + e.getMessage());
        }

        return WendaUtil.getJSONString(1,"失败");
    }

    //通过前端页面传过来的question的id来查询具体的问题
    //就是当点击首页上的问题时候,其实是一个超链接,会将question的id传过来,根据id从数据库中查询具体的question
    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(@PathVariable("qid")int qid, Model model){
        Question question = questionService.selectById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserId()));

        //找出此问题评论的所有用户
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION) ;
        //需要将VO对象添加到Model中,返回的是VO对象
        List<ViewObject> vos = new ArrayList<>();
        for(Comment comment : commentList){
            ViewObject vo = new ViewObject();
            //将评论和用户都添加进VO对象中
            vo.set("comment",comment);
            if(hostHolder.getUser() == null){
                vo.set("liked",0);
            }else{
                //查询用户的喜欢状态
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),
                        EntityType.ENTITY_COMMENT,comment.getId()));
            }

            //查询出评论的点赞数
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            vo.set("user",userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("comments",vos);

        return "detail";
    }
}
