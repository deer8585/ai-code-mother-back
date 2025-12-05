package com.wyb.aicodemotherback.aop;

import com.wyb.aicodemotherback.annotation.AuthCheck;
import com.wyb.aicodemotherback.exception.BusinessException;
import com.wyb.aicodemotherback.exception.ErrorCode;
import com.wyb.aicodemotherback.model.entity.User;
import com.wyb.aicodemotherback.model.enums.UserRoleEnum;
import com.wyb.aicodemotherback.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect //声明这是一个AOP切面类
@Component // 让Spring容器管理这个Bean
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     * @param joinPoint
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)") //切入点表达式，表示拦截所有被@AuthCheck注解标记的方法
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole(); //获取注解中的必须角色,这个值表示访问该方法所需的最低权限角色

        //从当前线程的上下文获取请求属性
        //RequestContextHolder使用ThreadLocal存储请求信息
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        //将RequestAttributes转换为ServletRequestAttributes
        //获取当前的HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole); //把前面的字符串改为枚举类，更容易判断

        //不需要权限，放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }

        // 以下为：有该权限才通过
        //获取当前用户具有的权限
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());

        //没有权限，拒绝
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }

        // 要求必须有管理员权限，没有则拒绝
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }

        //要求必须有普通用户权限，没有则拒绝
        if(UserRoleEnum.USER.equals(mustRoleEnum) && !UserRoleEnum.USER.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
            }

        //通过权限校验，放行
        return joinPoint.proceed();
    }
}
