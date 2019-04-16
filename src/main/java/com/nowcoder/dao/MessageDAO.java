package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {

    String TABLE_NAME = " wenda_message ";
    String INSERT_FIELDS = " from_id, content, to_id, has_read, created_date, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{content},#{toId},#{hasRead},#{createdDate},#{conversationId})"})
    int addMessage(Message message);



    //查询出这次会话的详细Message信息,需要传入此次会话的ID
    @Select({"select ",SELECT_FIELDS, " from ", TABLE_NAME,
            " where conversation_id=#{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId")String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);


//    @Select({"select count(id) from ",TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
//    int getCommentCount(@Param("entityId")int entityId,@Param("entityType")int entityType);
//
//
//    @Update({"update wenda_comment set status=#{status} where id=#{id}"})
//    int updateCommentStatus(@Param("id")int id,@Param("status")int status);

    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId")int userId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConvesationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);


}
