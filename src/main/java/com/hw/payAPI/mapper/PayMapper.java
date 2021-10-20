package com.hw.payAPI.mapper;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.model.Cancels;
import com.hw.payAPI.model.Payments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PayMapper {
    //@Insert("INSERT INTO payments(unique_id, payStr) VALUES(#{unique_id}, #{payStr})")
    int savePayStr(@Param("unique_id") String unique_id , @Param("payStr") String payStr);

    Payments getPayInfo(@Param("unique_id") String unique_id);

    int getCostSum(@Param("originid") String originid);
    int getTaxSum(@Param("originid") String originid);

    int saveCancel(@Param("canceldata") Cancels cancelData);
}
