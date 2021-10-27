package com.hw.payAPI.service;

import com.hw.payAPI.dto.GetInfoDTO;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Payments;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        //given
        String uid = "pay20211027233088767";

        Payments payInfo = new Payments();
        payInfo.setUnique_id("pay20211027233088767");
        payInfo.setPayStr(" 446PAYMENT   pay202110272330887671234567890123456    001125777    1100000000010000                                                                                                                                                                                                                                                                            iQZ6wIyePzA%2BA7u6diA5ENdNe%2BWvi9i2DHlFOi5tv%2Bg%3D                                               ");

        given(payMapper.getPayInfo(uid)).willReturn(payInfo);

        //when
        GetInfoDTO getInfoDTO = getInfoService.getData(uid);

        //then
        Assertions.assertEquals(getInfoDTO.getMaskedCardNum(), "123456*******456");
        Assertions.assertEquals(getInfoDTO.getValidDate(), "1125");
        Assertions.assertEquals(getInfoDTO.getCvc(), "777");
    }
}