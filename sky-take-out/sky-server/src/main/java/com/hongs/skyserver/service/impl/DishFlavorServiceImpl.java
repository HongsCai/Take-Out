package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.pojo.entity.DishFlavor;
import com.hongs.skyserver.service.DishFlavorService;
import com.hongs.skyserver.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author Hongs
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2025-12-10 22:38:19
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




