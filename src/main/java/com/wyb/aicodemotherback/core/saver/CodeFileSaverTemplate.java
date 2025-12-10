package com.wyb.aicodemotherback.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wyb.aicodemotherback.exception.BusinessException;
import com.wyb.aicodemotherback.exception.ErrorCode;
import com.wyb.aicodemotherback.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 抽象代码文件保存器 - 模板方法模式
 * @param <T>
 */
public abstract class CodeFileSaverTemplate<T> {

    /**
     * 文件保存根目录
     */
    public static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output/";

    /**
     * 模板方法：保存代码的标准流程
     * @param result 代码结果对象
     * @return 保存的文件目录对象
     */
    public final File saveCode(T result){
        // 1. 验证输入
        validateInput(result);

        // 2. 构建唯一目录
        String baseDirPath = buildUniqueDir();

        //3. 保存文件(具体实现由子类提供)
        saveFiles(result,baseDirPath);

        //4. 返回文件目录对象
        return new File(baseDirPath);
    }



    /**
     * 验证输入参数(可由子类覆盖)
     * @param result
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"输入不能为空");
        }
    }

    /**
     * 构建文件的唯一路径：tmp/code_output/bitType_雪花ID
     * @return
     */
    protected  String buildUniqueDir() {
        String bitType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", bitType, IdUtil.getSnowflakeNextIdStr()); // 生成唯一目录名
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        return dirPath;
    }

    /**
     *  保存单个文件
     * @param dirPath 文件目录
     * @param filename 文件名
     * @param content  文件内容
     */
    public final void writeToFile(String dirPath,String filename, String content) {
        if(StrUtil.isNotBlank(content)){
            String filePath = dirPath + File.separator + filename; // 文件路径 = 目录路径 + 分隔符("/") + 文件名
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }


    /**
     * 获取代码生成类型枚举
     * @return
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 保存文件（具体实现由子类提供）
     * @param result
     * @param baseDirPath
     */
    protected abstract void saveFiles(T result, String baseDirPath) ;
}
