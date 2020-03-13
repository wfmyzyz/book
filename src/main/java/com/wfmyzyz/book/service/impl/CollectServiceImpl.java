package com.wfmyzyz.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wfmyzyz.book.domain.Collect;
import com.wfmyzyz.book.mapper.CollectMapper;
import com.wfmyzyz.book.service.ICollectService;
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
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {

    @Override
    public Collect getCollectByUserIdAndBookId(Integer bookId, Integer userId) {
        return this.getOne(new QueryWrapper<Collect>().eq("user_id",userId).eq("book_id",bookId).eq("tb_status","正常"));
    }
}
