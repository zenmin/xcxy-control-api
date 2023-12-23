package com.zh.controlcenter.common.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zh.controlcenter.common.apibase.ApiResultCode;
import com.zh.controlcenter.common.apibase.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2019/1/16 22:04
 */
@Slf4j
public class CommonUtil {

    /**
     * 业务线程池
     */
    public static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("business-pool-%d").build();

    public static ExecutorService executorService = new ThreadPoolExecutor(20, 300, 10, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(500), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    public static ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(30);
    public static final Integer THREAD_LENGTH = 20;  // 步长为10 即20个数据开一个线程

    /**
     * 校验英文
     */
    public static final Pattern COMPILE_ENGLISH = Pattern.compile("[A-Za-z]");

    /**
     * 校验中文
     */
    public static final Pattern COMPILE_CHINESE = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 校验手机号
     */
    public static final String COMPILE_PHONE = "^(13[0-9]|18[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|14[0|1|2|3|4|5|6|7|8|9]|16[0|1|2|3|4|5|6|7|8|9]|17[0|1|2|3|4|5|6|7|8|9]|19[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|4|5|6|7|8|9])\\d{8}$";

    /**
     * SpringSecurity默认密码编码器
     */
    public static final PasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 校验用户名
     */
    public static final String COMPILE_USERNAME = "^[a-zA-Z][a-zA-Z0-9_]{1,15}$";

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static final String ROBOT_COMPILE = "(HTTrack|Apache-HttpClient|harvest|audit|dirbuster|pangolin|nmap|sqln|hydra|Parser|libwww|BBBike|sqlmap|w3af|owasp|Nikto|fimap|havij|zmeu|BabyKrokodil|netsparker|httperf|Java/| SF/|axios|WinHttpRequest|Nexus 5 Build|Chrome/69.0.3947.100)";

    /**
     * 常规UUID
     *
     * @return
     */
    public static String UUID() {
        return IdWorker.get32UUID();
    }

    /**
     * UUID的hashcode +随机数 有几率重复
     *
     * @return
     */
    public static synchronized String uniqueKey() {
        int abs = Math.abs(Integer.parseInt(String.valueOf(CommonUtil.UUID().hashCode())));
        int random = (int) (Math.random() * 1000);
        String temp = String.valueOf(random + abs);
        while (temp.length() < 10) {
            temp += "0";
        }
        if (temp.length() > 10) {
            temp = temp.substring(0, 10);
        }
        return temp;
    }

    /**
     * 同一时间使用 唯一id 生成订单号
     *
     * @param date
     * @return
     */
    public static String getId(String date) {
        String dateTime = DateUtil.parseLocalDateTime(date, DatePattern.PURE_DATETIME_MS_PATTERN).toString();
        return dateTime + "0" + IdWorker.getId();
    }

    /**
     * 同一时间使用 当前时间 唯一id 生成订单号
     *
     * @return
     */
    public static String getId() {
        String dateTime = DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN);
        return dateTime + IdWorker.getIdStr();
    }

    /**
     * 获取当前时间字符
     *
     * @return
     */
    public static String NowFormateYyyyMMddHHmmss() {
        return DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 获取当前时间字符
     *
     * @return
     */
    public static String NowFormateYyyyMMdd() {
        return DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN);
    }

    /**
     * 加上咱们salt的md5 用于生成密码之类
     *
     * @param code
     * @return
     */
    public static String md5Hex(String code) {
        return DigestUtils.md5Hex(code.getBytes(Charset.defaultCharset()));
    }

    /**
     * 原生md5
     *
     * @param code
     * @return
     */
    public static String md5Hex(byte[] code) {
        return DigestUtils.md5Hex(code);
    }

    /**
     * base64加密
     *
     * @param code
     * @return
     */
    public static String base64Encode(String code) {
        BaseEncoding baseEncoding = BaseEncoding.base64();
        String encode = baseEncoding.encode(code.getBytes());
        return encode;
    }

    /**
     * base64解码
     *
     * @param code
     * @return
     */
    public static String base64Decode(String code) {
        BaseEncoding baseEncoding = BaseEncoding.base64();
        byte[] decode = baseEncoding.decode(code);
        String s = null;
        try {
            s = new String(decode, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String sha1Hex(String code) {
        return DigestUtils.sha1Hex(code);
    }

    public static String sha1Hex(byte[] bytes) {
        return DigestUtils.sha1Hex(bytes);
    }

    public static String sha1512Hex(String code) {
        return DigestUtils.sha512Hex(code);
    }

    /**
     * 相加
     *
     * @param pre
     * @param suff
     * @return
     */
    public static Double add(Double pre, Double suff) {
        if (Objects.isNull(pre)) {
            pre = 0d;
        }
        if (Objects.isNull(suff)) {
            suff = 0d;
        }
        return BigDecimal.valueOf(pre).add(BigDecimal.valueOf(suff)).doubleValue();
    }

    /**
     * 相除 四舍五入 保留两位小数
     *
     * @param dividend
     * @param divisor
     * @return
     */
    public static Double divide(double dividend, double divisor) {
        if (dividend == 0d || divisor == 0d) {
            return 0d;
        }
        return BigDecimal.valueOf(dividend).divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 相乘 四舍五入 保留两位小数
     *
     * @param multiplicand
     * @param multiplier
     * @return
     */
    public static Double multiply(double multiplicand, double multiplier) {
        if (multiplicand == 0d || multiplier == 0d) {
            return 0d;
        }
        return BigDecimal.valueOf(multiplicand).multiply(BigDecimal.valueOf(multiplier)).doubleValue();
    }

    public static Long multiplyToLong(double multiplicand, double multiplier) {
        if (multiplicand == 0d || multiplier == 0d) {
            return 0L;
        }
        return BigDecimal.valueOf(multiplicand).multiply(BigDecimal.valueOf(multiplier)).longValue();
    }

    /**
     * 相减
     *
     * @param multiplicand
     * @param multiplier
     * @return
     */
    public static Double subtract(double multiplicand, double multiplier) {
        return BigDecimal.valueOf(multiplicand).subtract(BigDecimal.valueOf(multiplier)).doubleValue();
    }

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<String, Object>(0);
        if (StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    /**
     * @param orderNo
     * @return 数字匹配
     */
    public static boolean checkNum(String orderNo) {
        return orderNo.matches("^[0-9]*$");
    }

    /**
     * @param list
     * @return 加逗号
     */
    public static String joinQuota(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s).append(",");
        }
        return result.toString().substring(0, result.toString().length() - 1);
    }

    /**
     * @param comment
     * @return 根据顺序赋值
     */
    public static <T> T of(String[] comment, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            // 跳过第一个字段
            for (int i = 1; i <= declaredFields.length - 1; i++) {
                Field field = declaredFields[i];
                String name = field.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
                Method method = clazz.getDeclaredMethod("set" + name, String.class);
                try {
                    String value = comment[i - 1];  // 这里-1 因为i从1开始
                    if (StringUtils.isBlank(value)) {
                        method.invoke(obj, "");
                    } else {
                        method.invoke(obj, value);
                    }
                } catch (Exception e) {
                    return obj;
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param comment
     * @return 根据顺序赋值
     */
    public static <T> T of(List<String> comment, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            // 跳过第一个字段
            for (int i = 1; i <= declaredFields.length - 1; i++) {
                Field field = declaredFields[i];
                String name = field.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
                Method method = clazz.getDeclaredMethod("set" + name, String.class);
                try {
                    String value = comment.get(i - 1);  // 这里-1 因为i从1开始
                    if (StringUtils.isBlank(value)) {
                        method.invoke(obj, "");
                    } else {
                        method.invoke(obj, value);
                    }
                } catch (Exception e) {
                    return obj;
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map readToMap(String src, String field) {
        Map map = new HashMap();
        try {
            if (StringUtils.isNotBlank(src)) {
                map = objectMapper.readValue(src, HashMap.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List readToList(String src, String field) {
        List list = Lists.newArrayList();
        if (StringUtils.isNotBlank(src)) {
            try {
                list = objectMapper.readValue(src, List.class);
            } catch (IOException e) {
                log.error("字段{}JSON格式异常！", field);
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 耗时500+ms  尽量用JSONObject.parse
     *
     * @param src
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T readToClass(String src, Class<T> tClass) {
        try {
            if (StringUtils.isNotBlank(src)) {
                T t = objectMapper.readValue(src, tClass);
                return t;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String unicodeToCn(String unicode) {
        // 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格
        String[] strs = unicode.split("\\\\u");
        StringBuilder returnStr = new StringBuilder();
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr.append((char) Integer.valueOf(strs[i], 16).intValue());
        }
        return returnStr.toString();
    }

    /**
     * AES加密字符串
     *
     * @param content 需要被加密的字符串
     * @param key     加密需要的密钥
     * @return 密文
     */
    public static String AesEncode(String content, String key) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        AES aes = new AES(key.getBytes());
        return aes.encryptBase64(content, Charset.defaultCharset());
    }


    /**
     * 解密AES加密过的字符串
     *
     * @param code AES加密过过的内容
     * @param KEY  加密时的密钥
     * @return 明文
     */
    public static String decrypt(String code, String KEY) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        AES aes = new AES(KEY.getBytes(Charset.defaultCharset()));
        return new String(aes.decrypt(code));
    }

    /**
     * 生成六位验证码
     *
     * @param length 长度
     * @return
     */
    public static String genSmsCode(int length) {
        return CommonUtil.uniqueKey().substring(0, length);
    }

    /**
     * 按顺序设置map
     *
     * @param keys
     * @param values
     * @return
     */
    public static Map<String, Object> multiSetMap(List<String> keys, List<Object> values) {
        Map<String, Object> map = Maps.newHashMap();
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }


    /**
     * @return
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @return
     */
    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 验证身份证号码
     *
     * @return
     */
    public static void validIdCard(String idCard) {
        if (StringUtils.isNotBlank(idCard)) {
            String reg = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
            if (!idCard.matches(reg)) {
                throw new CommonException(ApiResultCode.PARAMSERROR.getCode(), "身份证号码格式错误！");
            }
        }
    }


    public static void validPassword(String password) {
        if (password.length() < 6 || password.length() > 18) {
            throw new CommonException(ApiResultCode.PARAMSERROR.getCode(), "密码长度在6~18位之间！");
        }

        if (COMPILE_CHINESE.matcher(password).find()) {
            throw new CommonException(ApiResultCode.PARAMSERROR.getCode(), "密码格式错误，不能包含中文！");
        }

    }

    public static void validPhone(String phone) {
        if (StringUtils.isBlank(phone) || !phone.startsWith("1") || phone.length() != 11) {
            throw new CommonException(ApiResultCode.PARAMSERROR.getCode(), "手机号有误！");
        }
    }

    public static void validUserName(String username) {
        if (!username.matches(COMPILE_USERNAME)) {
            throw new CommonException(ApiResultCode.PARAMSERROR.getCode(), "用户名格式错误，应由2~15位字母/数字/下划线组成且不能以数字开头！");
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * Es 判断是否有英文  有英文前后加上*
     *
     * @param name
     * @return
     */
    public static String fixEsQueryString(String name) {
        name = name.replace(" ", "");
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (CommonUtil.COMPILE_ENGLISH.matcher(String.valueOf(c)).find()) {
                str.append("*" + c + "*");
            } else {
                str.append(c);
            }
        }
        String replace = str.toString().replace("**", "*");
        return replace.toLowerCase();
    }

    /**
     * 校验手机号是否正确
     *
     * @param phone
     * @return
     */
    public static boolean verifyPhone(String phone) {
        String regex = "^[1]([3-9])[0-9]{9}$";
        boolean isMatch = false;
        if (StringUtils.isEmpty(phone)) {
            return false;
        } else if (phone.length() != 11) {
            return false;
        } else {
            isMatch = Pattern.matches(regex, phone);
        }
        return isMatch;
    }

    /**
     * 是否是机器人
     *
     * @param userAgent
     * @return
     */
    public static boolean isRobot(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return true;
        }
        Pattern r = Pattern.compile(ROBOT_COMPILE);
        Matcher m = r.matcher(userAgent);
        return m.find();
    }

}
