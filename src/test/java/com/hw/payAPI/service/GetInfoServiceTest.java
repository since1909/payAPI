package com.hw.payAPI.service;

import com.hw.payAPI.dto.GetInfoDTO;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Payments;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetInfoServiceTest {
    @InjectMocks
    private GetInfoService getInfoService;

    @Mock
    private PayMapper payMapper;

    @Test
    public void getDataTest() throws DecoderException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!암호화 key 변경됨 -> 삽입된 암호화 문자열 변경 후 test 실행!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //given
        String uid = "pay20211018172253001";

        Payments payInfo = new Payments();
        payInfo.setUnique_id("pay20211018172253001");
        payInfo.setPayStr(" 446PAYMENT   pay202110181722530011234567890123456    001021123     110000000001000                                                                                                                                                                                                                                                                                lQUrO5X3EKg1B4A8rlN%2F2SCREEqTHoXsVmccQoD3Fn8%3D                                               ");

        given(payMapper.getPayInfo(uid)).willReturn(payInfo);

        //when
        GetInfoDTO getInfoDTO = getInfoService.getData(uid);

        //then
        Assertions.assertEquals(getInfoDTO.getMaskedCardNum(), "123456*******456");
        Assertions.assertEquals(getInfoDTO.getValidDate(), "1021");
        Assertions.assertEquals(getInfoDTO.getCvc(), "123");
    }
}