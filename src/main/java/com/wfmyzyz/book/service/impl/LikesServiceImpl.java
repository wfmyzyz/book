package com.wfmyzyz.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wfmyzyz.book.domain.Likes;
import com.wfmyzyz.book.mapper.LikesMapper;
import com.wfmyzyz.book.service.ILikesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements ILikesService {

    @Override
    public Likes getLikesByUserIdAndBookId(Integer userId,Integer bookId) {
        return this.getOne(new QueryWrapper<Likes>().eq("user_id",userId).eq("book_id",bookId).eq("tb_status","正常"));
    }
}
