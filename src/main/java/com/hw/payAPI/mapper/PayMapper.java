package com.hw.payAPI.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PayMapper {
    //@Insert("INSERT INTO payments(unique_id, payStr) VALUES(#{unique_id}, #{payStr})")
    int savePayStr(@Param("unique_id") String unique_id , @Param("payStr") String payStr);
}
