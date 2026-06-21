package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.NoticeQueryDTO;
import com.suse.campus_rent.dto.app.AppNoticeQueryDTO;
import com.suse.campus_rent.entity.Notice;
import com.suse.campus_rent.vo.admin.NoticeListVO;
import com.suse.campus_rent.vo.admin.NoticeStatisticsVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 分页查询公告列表（自定义，关联发布者姓名等）
     */
    IPage<NoticeListVO> selectNoticeList(Page<?> page, @Param("query") NoticeQueryDTO query);

    /**
     * 统计各状态数量
     */
    @Select("SELECT " +
            "   COUNT(*) AS total, " +
            "   SUM(CASE WHEN status = 'published' THEN 1 ELSE 0 END) AS published, " +
            "   SUM(CASE WHEN status = 'draft' THEN 1 ELSE 0 END) AS draft, " +
            "   SUM(CASE WHEN status = 'archived' THEN 1 ELSE 0 END) AS archived " +
            "FROM notice")
    NoticeStatisticsVO selectStatistics();

    /**
     * 增加浏览量
     */
    @Update("UPDATE notice SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(@Param("id") Long id);


    /**
     * 用户端分页查询已发布的公告
     */
    IPage<Notice> selectPublishedNotices(Page<?> page, @Param("query") AppNoticeQueryDTO query);

    /**
     * 自动发布所有已到定时时间的草稿公告
     * 直接在数据库层面原子操作，高效且安全
     */
    @Update("UPDATE notice " +
            "SET status = 'published', publish_time = NOW() " +
            "WHERE status = 'draft' " +
            "AND scheduled_time IS NOT NULL " +
            "AND scheduled_time <= NOW() ")
    int publishScheduledNotices();
}