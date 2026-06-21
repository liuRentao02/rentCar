package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suse.campus_rent.entity.NoticeAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeAttachmentMapper extends BaseMapper<NoticeAttachment> {

    @Select("SELECT * FROM notice_attachment WHERE notice_id = #{noticeId}")
    List<NoticeAttachment> selectByNoticeId(Long noticeId);
}