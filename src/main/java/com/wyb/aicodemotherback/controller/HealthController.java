package com.wyb.aicodemotherback.controller;

import com.wyb.aicodemotherback.common.BaseResponse;
import com.wyb.aicodemotherback.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping ("/")
    public BaseResponse<String> Healthcheck() {
        return ResultUtils.success("ok");
    }
}
