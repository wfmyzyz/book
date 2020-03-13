package com.wfmyzyz.book.service;

import com.wfmyzyz.book.domain.Collect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
public interface ICollectService extends IService<Collect> {

    /**
     * 收藏书籍
     * @param bookId
     * @param userId
     * @return
     */
    Collect getCollectByUserIdAndBookId(Integer bookId, Integer userId);
}
