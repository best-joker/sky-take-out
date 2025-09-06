package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealsDishMapper {


    List<Long> getsetmealIdByDishIds(List<Long> ids);

}
