package com.example.washmachine.common.util;

import com.example.washmachine.common.exception.ExceptionId;
import com.example.washmachine.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceUtils {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceUtils.class);

    private ServiceUtils(){}

    @SuppressWarnings("unchecked")
    public static <T> T executeWithCatch(Callable callable, ExceptionId exId, String exMessage){
        T obj;
        try {
            obj = (T) callable.call();
        } catch (Exception e) {
            LOGGER.error(exMessage, e);
            throw new ServiceException(exId, exMessage, e);
        }
        return obj;
    }

    public static void executeWithCatch(Executable executable, ExceptionId exId, String exMessage){
        try {
            executable.execute();
        } catch (Exception e) {
            LOGGER.error(exMessage, e);
            throw new ServiceException(exId, exMessage, e);
        }
    }
}
