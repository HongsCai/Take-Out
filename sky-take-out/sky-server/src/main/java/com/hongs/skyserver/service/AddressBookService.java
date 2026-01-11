package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.dto.AddressBookSaveDTO;
import com.hongs.skycommon.pojo.entity.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Hongs
* @description 针对表【address_book(地址簿)】的数据库操作Service
* @createDate 2026-01-11 21:42:57
*/
public interface AddressBookService extends IService<AddressBook> {

    /**
     * 新增地址
     * @param addressBookSaveDTO
     */
    void save(AddressBookSaveDTO addressBookSaveDTO);

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    List<AddressBook> listByUserId();

    /**
     * 查询当前登录用户的默认地址信息
     * @return
     */
    AddressBook getDefaultByUserId();

    /**
     * 设置默认地址
     * @param id
     */
    void setDefault(Long id);

}
