package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.vo.admin.ContactMessageVO;
import com.suse.campus_rent.entity.ContactMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContactMessageMapper extends BaseMapper<ContactMessage> {

    /**
     * 分页查询留言列表（关联用户表）
     */
    IPage<ContactMessageVO> selectPageWithUser(Page<?> page, @Param("keyword") String keyword);
}