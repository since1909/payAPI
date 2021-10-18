package com.hw.payAPI.controller;

import com.hw.payAPI.dto.PayInfoDTO;
import com.hw.payAPI.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {
    @Autowired
    private PayService payService;

    @PostMapping
    public void insertPayStr(@RequestBody PayInfoDTO payInfoDTO){
        payService.savePayStr(payInfoDTO);
    }
}
