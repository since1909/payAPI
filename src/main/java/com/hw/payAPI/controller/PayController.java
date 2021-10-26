package com.hw.payAPI.controller;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.dto.GetInfoDTO;
import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.exception.CostOverException;
import com.hw.payAPI.exception.InvalidCostException;
import com.hw.payAPI.exception.InvalidInstallmentsException;
import com.hw.payAPI.exception.TaxOverException;
import com.hw.payAPI.service.CancelService;
import com.hw.payAPI.service.GetInfoService;
import com.hw.payAPI.service.PayService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class PayController {
    @Autowired
    private PayService payService;
    @Autowired
    private CancelService cancelService;
    @Autowired
    private GetInfoService getInfoService;

    //@PostMapping("/payment")
    @ModelAttribute("/payment")
    public String insertPayStr(@Valid @RequestBody PayInfoDTO payInfoDTO,  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "";
            List<ObjectError> list =  bindingResult.getAllErrors();
            for(ObjectError e : list) {
                errorMessage += e.getDefaultMessage();
                errorMessage += "\n";
                System.out.println(e.getDefaultMessage());
            }
            return errorMessage;
        }

        try {
            return payService.savePayStr(payInfoDTO);
        } catch (InvalidCostException e) {
            return e.getMessage();
        } catch (InvalidInstallmentsException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "결제 승인 오류 : 결제 데이터를 확인하세요.";
        }
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
