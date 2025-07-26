package storage.typeCheckAspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import storage.StorageValue;

import java.lang.reflect.Method;

/**
 * 存储类型检查切面，用于拦截带StorageOperation注解的方法并进行类型校验
 */
@Aspect
public class StorageTypeCheckerAspect {

    @Around("@annotation(storageOperation)")
    public Object checkStorageType(ProceedingJoinPoint joinPoint, StorageOperation storageOperation) throws Throwable {
        StorageValue storageValue = (StorageValue) joinPoint.getTarget();

        if (storageValue.getStorageValueType() != storageOperation.value()) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Class<?> returnType = method.getReturnType();

            if (returnType.isPrimitive()) {
                if (returnType == boolean.class) return false;
                if (returnType == int.class) return 0;
            }
            return null;
        }

        return joinPoint.proceed();
    }
}
