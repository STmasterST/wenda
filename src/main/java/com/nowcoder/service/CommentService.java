package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentsByEntity(int entityId,int entityType){
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

    public int addComment(Comment comment){
        //过滤评论中的html标签和敏感词
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));//过滤评论中的敏感词
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    //获取一个Question实体中的评论数量
    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }


    //删除一条评论
    //修改评论的status
    public boolean deleteComment(int commentId){
      return commentDAO.updateCommentStatus(commentId,1) > 0 ;
    }

}
