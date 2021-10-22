package com.hw.payAPI.partialCancelTest;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.mapper.PayMapper;
import com.hw.payAPI.service.CancelService;
import com.hw.payAPI.service.PayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class ConcurrencyControlTest {

    @Autowired
    private CancelService cancelService;
    @Autowired
    private PayService payService;
    @Autowired
    private PayMapper payMapper;

    private ExecutorService ex = Executors.newFixedThreadPool(50);
    private CountDownLatch latch=new CountDownLatch(50);

    @Test
    public void lockTest()throws Exception{
        PayInfoDTO payInfo = new PayInfoDTO();
        payInfo.setCardNum("1234567890123456");
        payInfo.setInstallments("0");
        payInfo.setValidDate("1125");
        payInfo.setCvc("777");
        payInfo.setCost("10000");
        payInfo.setTax(Optional.of("100"));

        String uniqueID = payService.savePayStr(payInfo);

        for(int i=0; i<50;i++){
            ex.execute(()->{
                try {
                    CancelInfoDTO cancelInfo1 = new CancelInfoDTO();
                    cancelInfo1.setUnique_id(uniqueID);
                    cancelInfo1.setCost("10");
                    cancelInfo1.setTax(Optional.of("1"));
                    cancelService.saveCancel(cancelInfo1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                latch.countDown();

            });
        }
        latch.await();

        Assertions.assertEquals(payMapper.getCostSum(uniqueID), "9500");
    }
}
