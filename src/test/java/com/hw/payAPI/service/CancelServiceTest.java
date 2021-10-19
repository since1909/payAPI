package com.hw.payAPI.service;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Payments;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
public class CancelServiceTest {

    @Autowired
    private CancelService cancelTestService;

    @Autowired
    private PayMapper payMapper;

    @Test
    public void saveCancel() {
        CancelInfoDTO cancelInfoDTO =  new CancelInfoDTO();
        cancelInfoDTO.setUnique_id("pay20211018172253001");
        cancelInfoDTO.setCost("110000");
        cancelInfoDTO.setTax(Optional.of("10000"));

        Payments payInfo = new Payments();

        //when
        payInfo = payMapper.getPayInfo(cancelInfoDTO.getUnique_id());

        //then
        Assertions.assertEquals(cancelInfoDTO.getUnique_id(), payInfo.getPayStr().substring(14, 34));
    }

    @Test
    public void makeCancelStr() {
    }
}