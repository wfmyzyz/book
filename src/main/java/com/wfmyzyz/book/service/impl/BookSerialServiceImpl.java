package com.wfmyzyz.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wfmyzyz.book.domain.BookSerial;
import com.wfmyzyz.book.mapper.BookSerialMapper;
import com.wfmyzyz.book.service.IBookSerialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-11-20
 */
@Service
public class BookSerialServiceImpl extends ServiceImpl<BookSerialMapper, BookSerial> implements IBookSerialService {

    @Override
    public Integer removeByIdBring(Integer id) {
        BookSerial bookSerial = this.getById(id);
        bookSerial.setTbStatus("删除");
        this.updateById(bookSerial);
        UpdateWrapper<BookSerial> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("book_id",bookSerial.getBookId());
        updateWrapper.eq("tb_status","正常");
        updateWrapper.gt("serial_num",bookSerial.getSerialNum());
        updateWrapper.setSql("serial_num = serial_num-1");
        this.update(updateWrapper);
        return bookSerial.getBookId();
    }
}
