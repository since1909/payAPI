package com.hw.payAPI.controller;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.dto.GetInfoDTO;
import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.exception.CostOverException;
import com.hw.payAPI.exception.TaxOverException;
import com.hw.payAPI.service.CancelService;
import com.hw.payAPI.service.GetInfoService;
import com.hw.payAPI.service.PayService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private CancelService cancelService;
    @Autowired
    private GetInfoService getInfoService;

    @PostMapping("/payment")
    public String insertPayStr(@RequestBody PayInfoDTO payInfoDTO) throws EncoderException, InvalidAlgorithmParameterException,
            UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {
       return payService.savePayStr(payInfoDTO);
    }

    @PostMapping("/cancel")
    public String insertCancel(@RequestBody CancelInfoDTO cancelInfoDTO) {
        try{
            return cancelService.saveCancel(cancelInfoDTO).getUnique_id();
        } catch(CostOverException e) {
            return e.getMessage();
        } catch(TaxOverException e) {
            return e.getMessage();
        } catch (Exception e){
            e.printStackTrace();
            return "결제취소오류";
        }

    }

    @GetMapping("/payment/{uid}")
    public GetInfoDTO getPayInfo(@PathVariable String uid) throws DecoderException, InvalidAlgorithmParameterException,
            UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return getInfoService.getData(uid);
    }
}
