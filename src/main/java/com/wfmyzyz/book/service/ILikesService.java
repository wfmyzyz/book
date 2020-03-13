package com.wfmyzyz.book.service;

import com.wfmyzyz.book.domain.Likes;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
public interface ILikesService extends IService<Likes> {

    /**
     * 根据用户Id获取点赞记录
     * @param userId
     * @param bookId
     * @return
     */
    Likes getLikesByUserIdAndBookId(Integer userId,Integer bookId);
}
