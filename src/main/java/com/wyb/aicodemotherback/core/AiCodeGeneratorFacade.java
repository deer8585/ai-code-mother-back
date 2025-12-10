package com.wyb.aicodemotherback.core;

import com.wyb.aicodemotherback.ai.AiCodeGeneratorService;
import com.wyb.aicodemotherback.ai.model.HtmlCodeResult;
import com.wyb.aicodemotherback.ai.model.MultiFileCodeResult;
import com.wyb.aicodemotherback.core.parser.CodeParserExecutor;
import com.wyb.aicodemotherback.core.saver.CodeFileSaverExecutor;
import com.wyb.aicodemotherback.exception.BusinessException;
import com.wyb.aicodemotherback.exception.ErrorCode;
import com.wyb.aicodemotherback.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成类，组合生成和保存功能
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 统一入口：根据类型生成并保存代码
     * @param userMessage 用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum){
        if(codeGenTypeEnum == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成类型不能为空");
        }

       return switch (codeGenTypeEnum){
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, codeGenTypeEnum.HTML);
            }
            case MULTI_FILE ->{
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, codeGenTypeEnum.MULTI_FILE);
            }
            default -> {
                String errorMsg = "不支持的代码生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
            }
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码 （流式返回）
     * @param userMessage 用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum){
        if(codeGenTypeEnum == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成类型不能为空");
        }

        return switch (codeGenTypeEnum){
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, codeGenTypeEnum.HTML);
            }
            case MULTI_FILE ->{
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, codeGenTypeEnum.MULTI_FILE);
            }
            default -> {
                String errorMsg = "不支持的代码生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
            }
        };
    }

    /**
     * 通用流式代码处理方法
     * @param codeStream 代码流
     * @param codeGenType 代码生成类型
     * @return 流式响应
     */
    private Flux<String> processCodeStream(Flux<String> codeStream,CodeGenTypeEnum codeGenType){
        //当流式返回生成代码完成后,再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
                    //实时收集代码片段
                    codeBuilder.append(chunk);
                })
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        // 流式返回完成后，保存代码
                        String completeCode = codeBuilder.toString();
                        // 解析代码为对象
                        Object parseCode = CodeParserExecutor.executeParser(completeCode, codeGenType);
                        //保存代码到文件
                        File savedDir = CodeFileSaverExecutor.executeSaver(parseCode, codeGenType);
                        log.info("代码保存成功，保存路径：{}", savedDir.getAbsolutePath());
                    }catch (Exception e){
                        log.error("代码保存失败:{}", e.getMessage());
                    }
                });
    }
}
