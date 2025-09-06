package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.sky.entity.DishFlavor;

@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavors);

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteById(Long dishId);

}
