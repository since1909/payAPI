package com.hw.payAPI.partialCancelTest;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.exception.CostOverException;
import com.hw.payAPI.exception.TaxOverException;
import com.hw.payAPI.service.CancelService;
import com.hw.payAPI.service.PayService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@SpringBootTest
public class TestCase01 {
    @Autowired
    private PayService payService;

    @Autowired
    private CancelService cancelService;

    @Test
    public void test01() throws EncoderException, DecoderException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        PayInfoDTO payInfo = new PayInfoDTO();
        payInfo.setCardNum("1234567890123456");
        payInfo.setInstallments("0");
        payInfo.setValidDate("1125");
        payInfo.setCvc("777");
        payInfo.setCost("11000");
        payInfo.setTax(Optional.of("1000"));

        //결제
        String uniqueID = payService.savePayStr(payInfo);
        //부분취소
        CancelInfoDTO cancelInfo1 = new CancelInfoDTO();
        cancelInfo1.setUnique_id(uniqueID);
        cancelInfo1.setCancelCost("1100");
        cancelInfo1.setCancelTax(Optional.of("100"));
        cancelService.saveCancel(cancelInfo1);

        CancelInfoDTO cancelInfo2 = new CancelInfoDTO();
        cancelInfo2.setUnique_id(uniqueID);
        cancelInfo2.setCancelCost("3300");
        cancelInfo2.setCancelTax(Optional.ofNullable(null));
        cancelService.saveCancel(cancelInfo2);

        CancelInfoDTO cancelInfo3 = new CancelInfoDTO();
        cancelInfo3.setUnique_id(uniqueID);
        cancelInfo3.setCancelCost("7000");
        cancelInfo3.setCancelTax(Optional.ofNullable(null));
        Throwable exception = Assertions.assertThrows(CostOverException.class, () -> {cancelService.saveCancel(cancelInfo3);});
        Assertions.assertEquals(exception.getMessage(), "결제 금액을 초과한 취소 입니다.");

        CancelInfoDTO cancelInfo4 = new CancelInfoDTO();
        cancelInfo4.setUnique_id(uniqueID);
        cancelInfo4.setCancelCost("6600");
        cancelInfo4.setCancelTax(Optional.ofNullable("700"));
        Throwable exception2 = Assertions.assertThrows(TaxOverException.class, () -> {cancelService.saveCancel(cancelInfo4);});
        Assertions.assertEquals(exception2.getMessage(), "부과세를 초과한 취소 입니다.");

        CancelInfoDTO cancelInfo5 = new CancelInfoDTO();
        cancelInfo5.setUnique_id(uniqueID);
        cancelInfo5.setCancelCost("6600");
        cancelInfo5.setCancelTax(Optional.ofNullable("600"));
        cancelService.saveCancel(cancelInfo5);

        CancelInfoDTO cancelInfo6 = new CancelInfoDTO();
        cancelInfo6.setUnique_id(uniqueID);
        cancelInfo6.setCancelCost("100");
        cancelInfo6.setCancelTax(Optional.ofNullable(null));
        Throwable exception3 = Assertions.assertThrows(CostOverException.class, () -> {cancelService.saveCancel(cancelInfo6);});
        Assertions.assertEquals(exception3.getMessage(), "결제 금액을 초과한 취소 입니다.");

    }
}
