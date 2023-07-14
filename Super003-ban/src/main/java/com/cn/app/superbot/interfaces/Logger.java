package com.cn.app.superbot.interfaces;

/**
 * The interface Logger.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public interface Logger {

    /**
     * Logs an information message.
     *
     * @param log the message to log
     */
    void Info(String log);

    /**
     * Logs an error message.
     *
     * @param log the message to log
     */
    void Error(String log);

    /**
     * Logs a warning message.
     *
     * @param log the message to log
     */
    void Warn(String log);

    /**
     * Logs a debug message.
     *
     * @param log the message to log
     */
    void Debug(String log);

}

