package com.hw.payAPI.controller;

import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.service.PayService;
import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class PayController {
    @Autowired
    private PayService payService;

    @PostMapping("/payment")
    public String insertPayStr(@RequestBody PayInfoDTO payInfoDTO) throws EncoderException, InvalidAlgorithmParameterException,
            UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
       return payService.savePayStr(payInfoDTO);
    }
}
