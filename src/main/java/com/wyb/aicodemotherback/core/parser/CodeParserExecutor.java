package com.wyb.aicodemotherback.core.parser;

import com.wyb.aicodemotherback.exception.BusinessException;
import com.wyb.aicodemotherback.exception.ErrorCode;
import com.wyb.aicodemotherback.model.enums.CodeGenTypeEnum;

import javax.swing.text.html.HTML;

/**
 * 代码解析执行器
 * 根据代码生成类型执行相应的解析逻辑
 */
public class CodeParserExecutor {
    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();
    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();

    public static Object executeParser(String codeContent, CodeGenTypeEnum codeType) {

        return switch (codeType) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型" + codeType);
        };
    }
}
