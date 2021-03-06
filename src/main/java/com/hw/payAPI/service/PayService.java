package com.hw.payAPI.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.exception.InvalidCostException;
import com.hw.payAPI.exception.InvalidInstallmentsException;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Payments;
import com.hw.payAPI.util.AES256Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


@Service
public class PayService {
    private static String key = "12345678912345678912345678912345";

    @Autowired
    private PayMapper payMapper;

    public Payments makeStr(PayInfoDTO payInfoDTO) throws UnsupportedEncodingException,
            InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, EncoderException {
        Payments payments = new Payments();

        //============================================HEADER 설정====================================================

        //데이터 길이 숫자 4
        String headerLength = "";

        //데이터 구분 문자 10 left
        String headerChar = String.format("%-10s", "PAYMENT");

        //데이터 관리번호 문자 20 (현재 날짜 시간 (14) + pay (3) + 일련번호 (001)
        Locale country = new Locale("KOREAN", "KOREA");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", country);
        String randNine = "";
        for(int i = 0; i < 9; i++) { randNine += Integer.toString(new Random().nextInt(9));}
        String headerUniqueID = "pay" + df.format(new Date()) +  randNine;

        //============================================================================================================

        String cardNum = String.format("%-20s", payInfoDTO.getCardNum());
        String installments = String.format("%02d", Integer.parseInt(payInfoDTO.getInstallments()));
        String validDate = payInfoDTO.getValidDate();
        String cvc = payInfoDTO.getCvc();
        String cost = String.format("%10s", payInfoDTO.getCost());
        String tax = payInfoDTO.getTax().orElse("");
        if(tax.equals("")) {
            double taxCal= Math.round(Double.parseDouble(payInfoDTO.getCost()) / 11);
            tax = String.format("%010d", (int)taxCal);
        } else { tax = String.format("%010d", Integer.parseInt(tax));}
        String spaces = String.format("%-20s", " ");

        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        String encStr = codec.encode(aes256.aesEncode(cardNum + "|" + validDate + "|" + cvc));
        String encrypted = String.format("%300s",  encStr);

        String reserveField = String.format("%47s", " ");

        String total = headerChar + headerUniqueID
                + cardNum + installments + validDate + cvc
                + cost + tax
                + spaces + encrypted + reserveField;
        headerLength = String.format("%4d" , total.length());

        total = headerLength + total;
        payments.setPayStr(total);
        payments.setUnique_id(headerUniqueID);
        return payments;
    }



    @Transactional
    public String savePayStr(PayInfoDTO payInfoDTO) throws EncoderException,
            InvalidAlgorithmParameterException, UnsupportedEncodingException,
            NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException,
            InvalidKeyException {

        if(Integer.parseInt(payInfoDTO.getInstallments()) < 0 || Integer.parseInt(payInfoDTO.getInstallments()) > 12)
            throw new InvalidInstallmentsException("할부는 12개월까지만 가능합니다.");
        if(Integer.parseInt(payInfoDTO.getCost()) < 100 || Integer.parseInt(payInfoDTO.getCost()) > 1000000000)
            throw new InvalidCostException("결제는 100원 이상 10억 이하만 가능합니다.");

        Payments payData = makeStr(payInfoDTO);
        payMapper.savePayStr(payData.getUnique_id(), payData.getPayStr());
        return payData.getUnique_id();
    }

}
