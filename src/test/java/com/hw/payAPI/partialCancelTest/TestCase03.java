package com.hw.payAPI.partialCancelTest;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.exception.TaxOverException;
import com.hw.payAPI.service.CancelService;
import com.hw.payAPI.service.PayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class TestCase03 {
    @Autowired
    private PayService payService;

    @Autowired
    private CancelService cancelService;

    @Test
    public void test03()throws Exception{
        PayInfoDTO payInfo = new PayInfoDTO();
        payInfo.setCardNum("1234567890123456");
        payInfo.setInstallments("0");
        payInfo.setValidDate("1125");
        payInfo.setCvc("777");
        payInfo.setCost("20000");
        payInfo.setTax(Optional.ofNullable(null));

        //결제
        String uniqueID = payService.savePayStr(payInfo);
        //부분취소
        CancelInfoDTO cancelInfo1 = new CancelInfoDTO();
        cancelInfo1.setUnique_id(uniqueID);
        cancelInfo1.setCancelCost("10000");
        cancelInfo1.setCancelTax(Optional.of("1000"));
        cancelService.saveCancel(cancelInfo1);

        CancelInfoDTO cancelInfo2 = new CancelInfoDTO();
        cancelInfo2.setUnique_id(uniqueID);
        cancelInfo2.setCancelCost("10000");
        cancelInfo2.setCancelTax(Optional.ofNullable("909"));
        Throwable exception = Assertions.assertThrows(TaxOverException.class, () -> {cancelService.saveCancel(cancelInfo2);});
        Assertions.assertEquals(exception.getMessage(), "부과세를 초과한 취소 입니다.");

        CancelInfoDTO cancelInfo3 = new CancelInfoDTO();
        cancelInfo3.setUnique_id(uniqueID);
        cancelInfo3.setCancelCost("10000");
        cancelInfo3.setCancelTax(Optional.ofNullable(null));
        cancelService.saveCancel(cancelInfo3);

    }
}
