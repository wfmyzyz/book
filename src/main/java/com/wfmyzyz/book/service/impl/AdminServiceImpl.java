package com.wfmyzyz.book.service.impl;

import com.wfmyzyz.book.domain.Admin;
import com.wfmyzyz.book.mapper.AdminMapper;
import com.wfmyzyz.book.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-09-12
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
