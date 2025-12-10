package com.wyb.aicodemotherback.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wyb.aicodemotherback.ai.model.HtmlCodeResult;
import com.wyb.aicodemotherback.ai.model.MultiFileCodeResult;
import com.wyb.aicodemotherback.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 文件保存器
 */
@Deprecated
public class CodeFileSaver {

    /**
     * 代码保存根目录
     */
    public static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output/";


    /**
     * 保存 HTML 网页代码
     * @param htmlCodeResult
     * @return
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult){
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDirPath, "index.html", htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }


    /**
     * 保存多文件代码
     * @param multiFileCodeResult
     * @return
     */
    public static File saveMultiFileCodeFile(MultiFileCodeResult multiFileCodeResult){
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDirPath, "index.html", multiFileCodeResult.getHtmlCode());
        writeToFile(baseDirPath, "style.css", multiFileCodeResult.getHtmlCode());
        writeToFile(baseDirPath, "script.js", multiFileCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 构建文件的唯一路径：tmp/code_output/bitType_雪花ID
     * @param bitType 代码生成类型
     * @return
     */
    private static String buildUniqueDir(String bitType) {
        String uniqueDirName = StrUtil.format("{}_{}", bitType, IdUtil.getSnowflakeNextIdStr()); // 生成唯一目录名
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        return dirPath;
    }

    /**
     *  保存单个文件
     * @param dirPath
     * @param filename
     * @param content
     */
    private static void writeToFile(String dirPath,String filename, String content) {
        String filePath = dirPath + File.separator + filename; // 文件路径 = 目录路径 + 分隔符("/") + 文件名
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }
}
