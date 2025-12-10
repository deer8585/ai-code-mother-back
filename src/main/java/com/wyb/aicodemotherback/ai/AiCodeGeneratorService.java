package com.wyb.aicodemotherback.ai;

import com.wyb.aicodemotherback.ai.model.HtmlCodeResult;
import com.wyb.aicodemotherback.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;


public interface AiCodeGeneratorService {

    /**
     * 生成 HTML 代码
     * @param userMessage
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    /**
     * 生成 多文件 代码
     * @param userMessage
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);

    /**
     * 生成 HTML 代码 (流式)
     * @param userMessage
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    /**
     * 生成 多文件 代码 (流式)
     * @param userMessage
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMessage);

}
