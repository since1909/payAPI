package com.hw.payAPI.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.model.Payments;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
class PayServiceTest {

    @Autowired
    private PayService payTestService;

    @Test
    void savePayStr() throws EncoderException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {
        //given (tax is not null)
        PayInfoDTO testDTO = new PayInfoDTO();

        testDTO.setId("1");
        testDTO.setCardNum("1234567890123456");
        testDTO.setInstallments("00");
        testDTO.setValidDate("1125");
        testDTO.setCvc("777");
        testDTO.setCost("110000");
        testDTO.setTax(Optional.of("10000"));

        //when
        Payments payTestData = payTestService.makeStr(testDTO);

        //then
        String payStr = payTestData.getPayStr();

        Assertions.assertEquals(" 446", payStr.substring(0, 4));
        Assertions.assertEquals("PAYMENT   ", payStr.substring(4, 14));
        //unique_id
        Assertions.assertEquals("1234567890123456    ", payStr.substring(34, 54));
        Assertions.assertEquals("00", payStr.substring(54, 56));
        Assertions.assertEquals("1125", payStr.substring(56, 60));
        Assertions.assertEquals("777", payStr.substring(60, 63));
        Assertions.assertEquals("    110000", payStr.substring(63, 73));
        Assertions.assertEquals("0000010000", payStr.substring(73, 83));

        //given when tax null
        testDTO.setTax(Optional.ofNullable(null));
        //when
        payTestData = payTestService.makeStr(testDTO);
        //then
        Assertions.assertEquals("0000010000", payStr.substring(73, 83));
    }

}