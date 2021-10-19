package com.hw.payAPI.service;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.exception.ExceedCancelPayException;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Cancels;
import com.hw.payAPI.model.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class CancelService {
    @Autowired
    private PayMapper payMapper;

    @Transactional
    public String saveCancel(CancelInfoDTO cancelInfoDTO) {
        Payments payInfo = payMapper.getPayInfo(cancelInfoDTO.getUnique_id()); //원거래 데이터
        System.out.println(payInfo.getUnique_id());

        //원 거래 금액, 부가가치세
        int originalCost = Integer.parseInt(payInfo.getPayStr().substring(63, 73).replaceAll(" ", ""));
        int originalTax = Integer.parseInt(payInfo.getPayStr().substring(73, 83));

        int costSum = payMapper.getCostSum(payInfo.getUnique_id());
        int taxSum = payMapper.getTaxSum(payInfo.getUnique_id());

        //tax null 값인지 확인
        String cancelTax = cancelInfoDTO.getTax().orElse("");
        if(cancelTax.equals("")) {
            double taxCal= Math.round(Double.parseDouble(cancelInfoDTO.getCost()) / 11);
            cancelTax = String.format("%010d", (int)taxCal);
        } else {
            cancelTax = String.format("%010d", Integer.parseInt(cancelTax)); // 10 right 0
        }

        if(costSum + Integer.parseInt(cancelInfoDTO.getCost()) > originalCost) {
            throw new ExceedCancelPayException();
        }
        else if(taxSum + Integer.parseInt(cancelTax) > originalTax) {
            throw new ExceedCancelPayException();
        }


        Cancels data = makeCancelStr(cancelInfoDTO, payInfo);
        payMapper.saveCancel(data);
        return data.getUnique_id();
    }

    public Cancels makeCancelStr(CancelInfoDTO cancelInfoDTO, Payments payInfo){


        Cancels cancelData = new Cancels();

        //취소 데이터 작성
        //header 변경

        String header = String.format("%-10s","CANCEL");

        Locale country = new Locale("KOREAN", "KOREA");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", country);
        String headerUniqueID = "pay" + df.format(new Date()) +  String.format("%03d", 001);

        String sameStr = payInfo.getPayStr().substring(34, 63);

        String cancelCost = String.format("%10s", cancelInfoDTO.getCost()); //10 right space

        String cancelTax = cancelInfoDTO.getTax().orElse("");
        if(cancelTax.equals("")) {
            double taxCal= Math.round(Double.parseDouble(cancelInfoDTO.getCost()) / 11);
            cancelTax = String.format("%010d", (int)taxCal);
        } else {
            cancelTax = String.format("%010d", Integer.parseInt(cancelTax)); // 10 right 0
        }

        String originid = payInfo.getUnique_id(); //원거래관리번호

        String total = header + headerUniqueID + sameStr + cancelCost + cancelTax + originid
                + payInfo.getPayStr().substring(93);
        String cancelStr = String.format("%4d", total.length()) + total;


        cancelData.setCost(Integer.parseInt(cancelCost.replaceAll(" ", "")  ));
        cancelData.setTax(Integer.parseInt(cancelTax));
        cancelData.setOrigin_id(originid);
        cancelData.setUnique_id(headerUniqueID);
        cancelData.setCancelstr(cancelStr);

        return cancelData;
    }

}
