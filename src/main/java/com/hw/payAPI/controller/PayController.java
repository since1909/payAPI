package com.hw.payAPI.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hw.payAPI.dto.CancelInfoDTO;
import com.hw.payAPI.dto.GetInfoDTO;
import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.exception.CostOverException;
import com.hw.payAPI.exception.InvalidCostException;
import com.hw.payAPI.exception.InvalidInstallmentsException;
import com.hw.payAPI.exception.TaxOverException;
import com.hw.payAPI.model.Cancels;
import com.hw.payAPI.service.CancelService;
import com.hw.payAPI.service.GetInfoService;
import com.hw.payAPI.service.PayService;
import com.hw.payAPI.util.ScriptUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class PayController {
    @Autowired
    private PayService payService;
    @Autowired
    private CancelService cancelService;
    @Autowired
    private GetInfoService getInfoService;

    @PostMapping(value = "/payment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String insertPayStrJsonReq(@Valid @RequestBody PayInfoDTO payInfoDTO,  BindingResult bindingResult) {
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
            return "?????? ?????? ?????? : ?????? ???????????? ???????????????.";
        }
    }

    @PostMapping(value = "/payment", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void insertPayStrFormReq(@Valid @ModelAttribute PayInfoDTO payInfoDTO,  BindingResult bindingResult, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=euc-kr");

        if (bindingResult.hasErrors()) {
            String errorMessage = "";
            List<ObjectError> list =  bindingResult.getAllErrors();
            for(ObjectError e : list) {
                errorMessage += e.getDefaultMessage();
                errorMessage += "\\r\\n";
            }
            ScriptUtil.alertAndBackPage(response, errorMessage);
            return;
        }

        try {
            String uid = payService.savePayStr(payInfoDTO);
            ScriptUtil.alertAndBackPage(response, "[?????? ??????] : " + uid);
            return;
        } catch (InvalidCostException e) {
           ScriptUtil.alertAndBackPage(response, "[Error] " + e.getMessage());
            return;
        } catch (InvalidInstallmentsException e) {
            ScriptUtil.alertAndBackPage(response, "[Error] " + e.getMessage());
            return;
        } catch (Exception e) {
            ScriptUtil.alertAndBackPage(response,"[Error] ?????? ???????????? ???????????????.");
            return;
        }
    }

    @PostMapping(value = "/cancel", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String insertCancelJsonReq(@RequestBody CancelInfoDTO cancelInfoDTO) {
        try{
            return cancelService.saveCancel(cancelInfoDTO).getUnique_id();
        } catch(CostOverException e) {
            return e.getMessage();
        } catch(TaxOverException e) {
            return e.getMessage();
        } catch (Exception e){
            e.printStackTrace();
            return "??????????????????";
        }

    }

    @PostMapping(value = "/cancel", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void insertCancelFormReq(@ModelAttribute CancelInfoDTO cancelInfoDTO, HttpServletResponse response) throws IOException {
        try{
            Cancels data = cancelService.saveCancel(cancelInfoDTO);
            ScriptUtil.alertAndBackPage(response, "[?????? ??????] ????????? ??????????????? ?????????????????????!\\r\\n(ID : " + data.getUnique_id() + ")");
            return;
        } catch(CostOverException e) {
            ScriptUtil.alertAndBackPage(response, "[Error] " + e.getMessage());
            return;
        } catch(TaxOverException e) {
            ScriptUtil.alertAndBackPage(response, "[Error] " + e.getMessage());
            return;
        } catch (Exception e){
            ScriptUtil.alertAndBackPage(response, "[Error] ?????? ???????????? ???????????????." + e.getMessage());
            return;
        }

    }

    @GetMapping("/payment/{uid}")
    public GetInfoDTO getPayInfo(@PathVariable String uid) throws DecoderException, InvalidAlgorithmParameterException,
            UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return getInfoService.getData(uid);
    }
}
