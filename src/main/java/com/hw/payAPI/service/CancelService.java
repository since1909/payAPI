package com.hw.payAPI.service;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.exception.CostOverException;
import com.hw.payAPI.exception.TaxOverException;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.model.Cancels;
import com.hw.payAPI.model.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
public class CancelService {
    private static int keyNumber = 0;

    @Autowired
    private PayMapper payMapper;

    @Transactional
    public Cancels saveCancel(CancelInfoDTO cancelInfoDTO) {
        Payments payInfo = payMapper.getPayments(cancelInfoDTO.getUnique_id()); //원거래 데이터

        //원 거래 금액, 부가가치세
        int originalCost = Integer.parseInt(payInfo.getPayStr().substring(63, 73).trim());
        int originalTax = Integer.parseInt(payInfo.getPayStr().substring(73, 83));

        int costSum = payMapper.getCostSum(cancelInfoDTO.getUnique_id());
        int taxSum = payMapper.getTaxSum(cancelInfoDTO.getUnique_id());

        System.out.println((originalCost - costSum) + " " + (originalTax - taxSum));

        //금액이 over 되면 무조건 실패
        if (costSum + Integer.parseInt(cancelInfoDTO.getCost()) > originalCost) {
            throw new CostOverException("결제 금액을 초과한 취소 입니다.");
        }

        //tax null 값인지 확인
        String cancelTax = cancelInfoDTO.getTax().orElse("");
        if (cancelTax.equals("")) {
            //System.out.println("yes is null~~~~~~~~~~~~~~~~~~~~~~~~");
            //마지막 부분취소 (남은 금액 전체 취소이면)
            if (costSum + Integer.parseInt(cancelInfoDTO.getCost()) == originalCost) {
                //부가가치세 계산하지 않고 남은 부가가치세 전부로 tax 설정
                cancelTax = String.format("%010d", originalTax - taxSum);
            }
            else {
                double taxCal = Math.round(Double.parseDouble(cancelInfoDTO.getCost()) / 11);
                cancelTax = String.format("%010d", (int) taxCal);
            }
            cancelInfoDTO.setTax(Optional.of(cancelTax));
        } else {
            if (taxSum + Integer.parseInt(cancelTax) > originalTax) {
                throw new TaxOverException("부과세를 초과한 취소 입니다.");
            }
            cancelTax = String.format("%010d", Integer.parseInt(cancelTax)); // 10 right 0
        }


        Cancels data = makeCancelStr(cancelInfoDTO, payInfo);
        payMapper.saveCancel(data);
        return data;
    }

    public Cancels makeCancelStr(CancelInfoDTO cancelInfoDTO, Payments payInfo){


        Cancels cancelData = new Cancels();

        //취소 데이터 작성
        //header 변경

        String header = String.format("%-10s","CANCEL");

        Locale country = new Locale("KOREAN", "KOREA");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", country);
        String headerUniqueID = "pay" + df.format(new Date()) +  String.format("%03d", ++keyNumber);

        String cardNum = payInfo.getPayStr().substring(34, 54);
        String installments = "00";
        String validDate = payInfo.getPayStr().substring(56, 60);
        String cvc = payInfo.getPayStr().substring(60, 63);

        String cancelCost = String.format("%10s", cancelInfoDTO.getCost()); //10 right space
        String cancelTax = String.format("%010d", Integer.parseInt(cancelInfoDTO.getTax().get()));

        String originid = payInfo.getUnique_id(); //원거래관리번호

        String total = header + headerUniqueID + cardNum + installments +validDate + cvc + cancelCost + cancelTax + originid
                + payInfo.getPayStr().substring(103);
        String cancelStr = String.format("%4d", total.length()) + total;


        cancelData.setCost(Integer.parseInt(cancelCost.trim()));
        cancelData.setTax(Integer.parseInt(cancelTax));
        cancelData.setOrigin_id(originid);
        cancelData.setUnique_id(headerUniqueID);
        cancelData.setCancelstr(cancelStr);

        return cancelData;
    }

}
