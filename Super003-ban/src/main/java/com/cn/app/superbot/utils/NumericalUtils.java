package com.cn.app.superbot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The type Numerical utils.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@SuppressWarnings("all")
public final class NumericalUtils {

    /**
     * The constant COMMON_CHAR.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static final String COMMON_CHAR = "@&*%";


    /**
     * Is integer boolean.
     *
     * @param obj the obj
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean isInteger(Object obj) {
        Pattern p = Pattern.compile("^[0-9]+$");
        Matcher m = p.matcher(obj.toString());

        return m.matches();
    }


    /**
     * Is double boolean.
     *
     * @param obj the obj
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean isDouble(Object obj) {

        return obj.toString().matches("\\d+\\.\\d+$");
    }


    /**
     * Check double length boolean.
     *
     * @param obj    the obj
     * @param length the length
     * @param na     the na
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean checkDoubleLength(Object obj, int length, int na) {
        return true;
    }

    /**
     * Is pure digital boolean.
     *
     * @param str the str
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean isPureDigital(String str) {
        String regEx1 = "^[1-9]+\\d*$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return m.matches();
        } else {
            return false;
        }
    }


    /**
     * Check string boolean.
     *
     * @param str the str
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean checkString(String str) {
        Pattern p = Pattern.compile("^[A-Za-z0-9_]+$");
        Matcher m = p.matcher(str);
        boolean isValid = m.matches();

        return isValid;
    }


    /**
     * Is valid char boolean.
     *
     * @param str the str
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean isValidChar(String str) {
        for (int i = 0; i < COMMON_CHAR.length(); i++) {
            char ch = COMMON_CHAR.charAt(i);
            if (str.indexOf(ch) != -1) {
                return false;
            }
        }

        return true;
    }


    /**
     * Is date boolean.
     *
     * @param obj the obj
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean isDate(Object obj) {
        try {
            Pattern pattern = Pattern
                    .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
            Matcher m = pattern.matcher(obj.toString());
            if (m.matches()) {
                return true;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Is null or empty boolean.
     *
     * @param obj the obj
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null || obj.toString().equals("")) return true;
        return false;
    }

    /**
     * Is not null boolean.
     *
     * @param obj the obj
     * @return the boolean
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static boolean isNotNull(Object obj) {
        if (obj != null && !(obj.toString().equals(""))) {
            return true;
        }
        return false;
    }


    /**
     * 校验是否为数字
     *
     * @param
     * @return boolean
     */
    public static boolean isNumber(Object obj) {
        if (obj == null) {
            return false;
        }
        Pattern p = Pattern.compile("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?");
        Matcher m = p.matcher(obj.toString());
        return m.matches();
    }

    /**
     * 校验时间格式
     *
     * @param
     * @return boolean
     */
    public static boolean isNumberNotMinus(Object obj) {
        if (obj == null) {
            return false;
        }
        Pattern p = Pattern.compile("(([0-1]?[0-9]{1})|([2]{1}[0-3]{1})):[0-5]{1}[0-9]{1}-(([0-1]?[0-9]{1})|([2]{1}[0-3]{1})):[0-5]{1}[0-9]{1}");
        Matcher m = p.matcher(obj.toString());
        return m.matches();
    }
}
