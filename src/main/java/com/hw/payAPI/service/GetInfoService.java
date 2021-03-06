package com.hw.payAPI.service;

import com.hw.payAPI.dto.GetInfoDTO;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Payments;
import com.hw.payAPI.util.AES256Util;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class GetInfoService {
    @Autowired
    private PayMapper payMapper;

    private static String key = "12345678912345678912345678912345";


    public GetInfoDTO getData(String uid) throws DecoderException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        Payments getInfo = payMapper.getPayInfo(uid);

        GetInfoDTO responseData = new GetInfoDTO();
        String payInfo = getInfo.getPayStr();
        String encrypted = payInfo.substring(103, 403).trim();

        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        String[] decStr = aes256.aesDecode(codec.decode(encrypted)).split("\\|");
        responseData.setUid(getInfo.getUnique_id());

        String cardNum = decStr[0].trim();
        String firstSix = cardNum.substring(0, 6);
        String endThree = cardNum.substring(cardNum.length()-3, cardNum.length());

        String maskedCardNum = firstSix;
        for(int i = 6; i < cardNum.length()-3; i++) maskedCardNum += "*";
        maskedCardNum += endThree;

        responseData.setMaskedCardNum(maskedCardNum);
        responseData.setValidDate(decStr[1]);
        responseData.setCvc(decStr[2]);

        responseData.setHeader(payInfo.substring(4, 14).trim());
        responseData.setCost(payInfo.substring(63, 73).trim());
        responseData.setTax(Integer.toString(Integer.parseInt(payInfo.substring(73, 83))));

        return responseData;
    }
}
