package com.wfmyzyz.book.service;

import com.wfmyzyz.book.domain.BookSerial;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-11-20
 */
public interface IBookSerialService extends IService<BookSerial> {

    /**
     * 删除章回并且修改大于该id章回数的章回
     * @param id
     * @return
     */
    Integer removeByIdBring(Integer id);
}
