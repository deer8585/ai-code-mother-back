package com.wyb.aicodemotherback.core.saver;

import cn.hutool.core.util.StrUtil;
import com.wyb.aicodemotherback.ai.model.MultiFileCodeResult;
import com.wyb.aicodemotherback.exception.BusinessException;
import com.wyb.aicodemotherback.exception.ErrorCode;
import com.wyb.aicodemotherback.model.enums.CodeGenTypeEnum;

/**
 * 多文件代码保存器
 */

public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath,"index.html",result.getHtmlCode());
        writeToFile(baseDirPath,"script.js",result.getJsCode());
        writeToFile(baseDirPath,"style.css",result.getCssCode());
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        //至少要有HTML代码
        if(StrUtil.isBlank(result.getHtmlCode())){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"html代码不能为空");
        }
    }
}
