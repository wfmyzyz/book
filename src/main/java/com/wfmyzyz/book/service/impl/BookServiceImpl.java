package com.wfmyzyz.book.service.impl;

import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.mapper.BookMapper;
import com.wfmyzyz.book.service.IBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-10-08
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

}
