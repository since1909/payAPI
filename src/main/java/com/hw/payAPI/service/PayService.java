package com.hw.payAPI.service;

import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Payments;
import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@Service
public class PayService {

    private static int keyCount = 0;

    @Autowired
    private PayMapper payMapper;

    @Transactional
    public void savePayStr(PayInfoDTO payInfoDTO) {

        Payments payments = new Payments();

        //HEADER 설정

        //데이터 길이 숫자 4
        String headerLength = "";
        //데이터 구분 문자 10 left
        String headerChar = String.format("%-10s", "PAYMENT");
        //데이터 관리번호 문자 20 (현재 날짜 시간 (14) + pay (3) + 일련번호 (001)
        Locale country = new Locale("KOREAN", "KOREA");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", country);
        keyCount++;
        String headerUniqueID = df.format(new Date()) + "pay" + String.format("%03d", keyCount);


        String cardNum = String.format("%-20s", payInfoDTO.getCardNum()); //20 left
        String installments = String.format("%02d", Integer.parseInt(payInfoDTO.getInstallments())); //2 right 0
        String validDate = payInfoDTO.getValidDate(); //4
        String cvc = payInfoDTO.getCvc(); //3
        String cost = String.format("%10s", payInfoDTO.getCost()); //10 right space
        String tax = String.format("%010d", Integer.parseInt(payInfoDTO.getTax())); // 10 right 0


        String spaces = String.format("%-20s", " "); //원거래관리번호 (결제시 공백) left 20 space
        String encrypted = String.format("%300s",  " "); // encrypted left 300 space
        String reserveField = String.format("%47s", " "); //47

        payMapper.savePayStr(payments);
    }
}
