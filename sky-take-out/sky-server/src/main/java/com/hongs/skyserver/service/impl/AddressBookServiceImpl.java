package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.hongs.skycommon.constant.DefaultConstant;
import com.hongs.skycommon.context.BaseContext;
import com.hongs.skycommon.pojo.dto.AddressBookSaveDTO;
import com.hongs.skycommon.pojo.entity.AddressBook;
import com.hongs.skyserver.service.AddressBookService;
import com.hongs.skyserver.mapper.AddressBookMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Hongs
* @description 针对表【address_book(地址簿)】的数据库操作Service实现
* @createDate 2026-01-11 21:42:57
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

    /**
     * 新增地址
     * @param addressBookSaveDTO
     */
    @Override
    public void save(AddressBookSaveDTO addressBookSaveDTO) {
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressBookSaveDTO, addressBook);
        addressBook.setUserId(BaseContext.getCurrentId());

        if (this.getDefaultByUserId() == null) {
            addressBook.setIsDefault(DefaultConstant.DEFAULT);
        }

        this.save(addressBook);
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @Override
    public List<AddressBook> listByUserId() {
        return this.list(new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, BaseContext.getCurrentId()));
    }

    /**
     * 查询当前登录用户的默认地址信息
     * @return
     */
    @Override
    public AddressBook getDefaultByUserId() {
        return this.getOne(new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getIsDefault, DefaultConstant.DEFAULT));
    }

    /**
     * 设置默认地址
     * @param id
     */
    @Override
    public void setDefault(Long id) {
        // 将当前用户的所有地址修改为非默认
        this.update(new LambdaUpdateWrapper<AddressBook>()
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getIsDefault, DefaultConstant.DEFAULT)
                .set(AddressBook::getIsDefault, DefaultConstant.NOT_DEFAULT));

        // 将当前选中的地址修改为默认
        this.update(new LambdaUpdateWrapper<AddressBook>()
                .eq(AddressBook::getId, id)
                .set(AddressBook::getIsDefault, DefaultConstant.DEFAULT));
    }
}




