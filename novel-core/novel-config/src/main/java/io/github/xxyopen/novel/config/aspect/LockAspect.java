package io.github.xxyopen.novel.config.aspect;

import io.github.xxyopen.novel.config.annotation.Key;
import io.github.xxyopen.novel.config.annotation.Lock;
import io.github.xxyopen.novel.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁 切面
 *
 * @author xiongxiaoyang
 * @date 2022/6/20
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LockAspect {
    private final RedissonClient redissonClient;
    private static final String KEY_PREFIX = "Lock";

    private static final String KEY_SEPARATOR = "::";

    @Around(value = "@annotation(io.github.xxyopen.novel.config.annotation.Lock)")
    @SneakyThrows
    public Object doAround(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        String lockKey = KEY_PREFIX + buildLockKey(lock.prefix(), method, joinPoint.getArgs());
        RLock rLock = redissonClient.getLock(lockKey);
        if (lock.isWait() ? rLock.tryLock(lock.waitTime(), TimeUnit.SECONDS) : rLock.tryLock()) {
            try {
                return joinPoint.proceed();
            } finally {
                rLock.unlock();
            }
        }
        throw new BusinessException(lock.failCode());
    }

    private String buildLockKey(String prefix, Method method, Object[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.hasText(prefix)) {
            stringBuilder.append(KEY_SEPARATOR).append(prefix);
        }
        Parameter[] parameters = method.getParameters();
        int len = 0;
        for (Parameter parameter : parameters) {
            stringBuilder.append(KEY_SEPARATOR);
            if (parameter.isAnnotationPresent(Key.class)) {
                Key key = parameter.getAnnotation(Key.class);
                stringBuilder.append(parseKeyExpr(key.expr(), args[len++]));
            }
        }
        return stringBuilder.toString();
    }

    private String parseKeyExpr(String expr, Object arg) {
        if (!StringUtils.hasText(expr)) {
            return arg.toString();
        }
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(expr, new TemplateParserContext());
        return expression.getValue(arg, String.class);
    }
}
