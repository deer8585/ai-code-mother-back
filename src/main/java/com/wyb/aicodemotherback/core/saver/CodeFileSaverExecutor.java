package com.wyb.aicodemotherback.core.saver;

import com.wyb.aicodemotherback.ai.model.HtmlCodeResult;
import com.wyb.aicodemotherback.ai.model.MultiFileCodeResult;
import com.wyb.aicodemotherback.exception.BusinessException;
import com.wyb.aicodemotherback.exception.ErrorCode;
import com.wyb.aicodemotherback.model.enums.CodeGenTypeEnum;
import java.io.File;

/**
 * 代码文件保存执行器
 * 根据代码生成类型执行相应的保存逻辑
 */
public class CodeFileSaverExecutor {
    public static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();
    public static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType) {
      return switch (codeGenType) {
          case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult);
          case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult);
          default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型: " + codeGenType);
      };
    }
}
