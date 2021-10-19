package com.hw.payAPI.controller;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.service.PayService;
import org.apache.commons.codec.EncoderException;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.websocket.server.PathParam;
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

    @PostMapping("/cancel")
    public String insertCancel(@RequestBody CancelInfoDTO cancelInfoDTO) {
        return payService.saveCancel(cancelInfoDTO);
    }

    @GetMapping("/payment/{uid}")
    public String getPayInfo(@PathVariable String uid) {
        return payService.getPayStr(uid);
    }
}
