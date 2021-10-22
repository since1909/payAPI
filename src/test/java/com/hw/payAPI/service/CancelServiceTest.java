package com.hw.payAPI.service;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.exception.CostOverException;
import com.hw.payAPI.exception.TaxOverException;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Cancels;
import com.hw.payAPI.model.Payments;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.asserts.Assertion;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
public class CancelServiceTest {

    @InjectMocks
    private CancelService cancelTestService;

    @Mock
    private PayMapper payMapper;

    @Test
    public void saveCancel() {
        //given
        CancelInfoDTO cancelInfoDTO = new CancelInfoDTO();
        cancelInfoDTO.setUnique_id("pay20211018172253001");
        cancelInfoDTO.setCost("11000");
        cancelInfoDTO.setTax(Optional.ofNullable("1000"));

        Payments payInfo = new Payments();
        payInfo.setPayStr(" 446PAYMENT   pay202110181722530011234567890123456    001021123     110000000001000                                                                                                                                                                                                                                                                                lQUrO5X3EKg1B4A8rlN%2F2SCREEqTHoXsVmccQoD3Fn8%3D                                               ");
        payInfo.setUnique_id("pay20211018172253001");
        given(payMapper.getPayments(cancelInfoDTO.getUnique_id())).willReturn(payInfo);

        //when
        Cancels cancelData = cancelTestService.saveCancel(cancelInfoDTO);

        //then
        String str = cancelData.getCancelstr();
        String headerFlag = str.substring(4, 14);

        String cardNum = str.substring(34, 54);
        String installments = str.substring(54, 56);
        String validDate = str.substring(56, 60);
        String cvc = str.substring(60, 63);

        String cost = str.substring(63, 73);
        String tax = str.substring(73, 83);

        Assertions.assertEquals("CANCEL    ", headerFlag);
        Assertions.assertEquals("1234567890123456    ", cardNum);
        Assertions.assertEquals("00", installments);
        Assertions.assertEquals("1021", validDate);
        Assertions.assertEquals("123", cvc);
        Assertions.assertEquals("     11000", cost);
        Assertions.assertEquals("0000001000", tax);

        //System.out.println(str);
    }

    @Test
    public void whenTaxNull() {
        //given
        CancelInfoDTO cancelInfoDTO = new CancelInfoDTO();
        cancelInfoDTO.setUnique_id("pay20211018172253001");
        cancelInfoDTO.setCost("11000");
        cancelInfoDTO.setTax(Optional.ofNullable(null));

        Payments payInfo = new Payments();
        payInfo.setPayStr(" 446PAYMENT   pay202110181722530011234567890123456    001021123     110000000001000                                                                                                                                                                                                                                                                                lQUrO5X3EKg1B4A8rlN%2F2SCREEqTHoXsVmccQoD3Fn8%3D                                               ");
        payInfo.setUnique_id("pay20211018172253001");
        given(payMapper.getPayments(cancelInfoDTO.getUnique_id())).willReturn(payInfo);

        //when
        Cancels cancelData = cancelTestService.saveCancel(cancelInfoDTO);
        Assertions.assertEquals(1000, cancelData.getTax());
        Assertions.assertEquals("0000001000", cancelData.getCancelstr().substring(73, 83));

    }

}