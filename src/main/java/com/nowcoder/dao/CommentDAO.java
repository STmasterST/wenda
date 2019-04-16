package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = " wenda_comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);


    //将一个实体中所有的评论选出来,并按照时间降序排列
    @Select({"select ",SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId")int entityId,@Param("entityType")int entityType);

    //查询出一个Question中有多少条评论.更新Question的评论数
    @Select({"select count(id) from ",TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId")int entityId,@Param("entityType")int entityType);


    @Update({"update wenda_comment set status=#{status} where id=#{id}"})
    int updateCommentStatus(@Param("id")int id,@Param("status")int status);
}
