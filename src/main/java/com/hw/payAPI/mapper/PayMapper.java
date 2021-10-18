package com.hw.payAPI.mapper;

import com.hw.payAPI.model.Payments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PayMapper {
    void savePayStr(@Param("payments") Payments payments);
}
