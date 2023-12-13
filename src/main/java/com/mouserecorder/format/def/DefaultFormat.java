package com.mouserecorder.format.def;

import com.mouserecorder.config.Config;
import com.mouserecorder.exceptions.MouseRecorderException;
import com.mouserecorder.format.api.Convertor;
import com.mouserecorder.format.api.Format;
import com.mouserecorder.recorder.event.*;
import com.mouserecorder.wrappers.KeyWrapper;
import com.mouserecorder.wrappers.MouseWrapper;
import com.mouserecorder.recorder.Record;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: eguller
 * Date: 2/23/14
 * Time: 2:38 AM
 */
public class DefaultFormat implements Format {
    private static final String ALGORITHM = "AES";

    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    // 加解密密钥
    private static final String KEY = new String(new byte[]{1,7,1,2,9,7,0,3,5,5,'x','i','a','w','e','i'});

    Cipher cipher;
    Config config;
    private static final String LINE_SEPERATOR = System.getProperty("line.separator");
    private static final Map<Class, Convertor> convertorFromClass = new HashMap<Class, Convertor>();

    private static final String DELAY_PATTERN_STR = "\\s*\\{\\s*delay\\s+(\\d+)\\s*\\}\\s*";
    private static final Pattern DELAY_PATTERN = Pattern.compile(DELAY_PATTERN_STR);

    private static final String KEY_PRESSED_PATTERN_STR = "\\s*\\{\\s*(?!(?:lmouse|rmouse|wheel))([a-zA-Z0-9]+)\\s+pressed\\s*}\\s*";
    private static final Pattern KEY_PRESSED_PATTERN = Pattern.compile(KEY_PRESSED_PATTERN_STR);

    private static final String KEY_RELEASED_PATTERN_STR = "\\s*\\{\\s*(?!(?:lmouse|rmouse|wheel))([a-zA-Z0-9]+)\\s+released\\s*}\\s*";
    private static final Pattern KEY_RELEASED_PATTERN = Pattern.compile(KEY_RELEASED_PATTERN_STR);

    private static final String MOUSE_PRESSED_PATTERN_STR = "\\s*\\{\\s*(lmouse|rmouse|wheel)\\s+pressed\\s*}\\s*";
    private static final Pattern MOUSE_PRESSED_PATTERN = Pattern.compile(MOUSE_PRESSED_PATTERN_STR);

    private static final String MOUSE_RELEASED_PATTERN_STR = "\\s*\\{\\s*(lmouse|rmouse|wheel)\\s+released\\s*}\\s*";
    private static final Pattern MOUSE_RELEASED_PATTERN = Pattern.compile(MOUSE_RELEASED_PATTERN_STR);


    private static final String MOUSE_MOVE_PATTERN_STR = "\\s*\\{\\s*move\\s+\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)\\s*\\}\\s*";
    private static final Pattern MOUSE_MOVE_PATTERN = Pattern.compile(MOUSE_MOVE_PATTERN_STR);

    private static final String MOUSE_WHEEL_PATTERN_STR = "\\s*\\{\\s*wheel\\s+([-]?\\s*\\d+)\\s*\\}\\s*";
    private static final Pattern MOUSE_WHEEL_PATTERN = Pattern.compile(MOUSE_WHEEL_PATTERN_STR);


    private static final Map<Pattern, Convertor> eventFromMapping = new HashMap<Pattern, Convertor>();

    public DefaultFormat(Config config) {
        this.config = config;
        convertorFromClass.put(DelayEvent.class, delayConvertor);
        convertorFromClass.put(KeyPressedEvent.class, keyPressedConvertor);
        convertorFromClass.put(KeyReleasedEvent.class, keyReleasedConvertor);
        convertorFromClass.put(MousePressedEvent.class, mousePressedConvertor);
        convertorFromClass.put(MouseReleasedEvent.class, mouseReleasedConvertor);
        convertorFromClass.put(MouseMoveEvent.class, mouseMoveConvertor);
        convertorFromClass.put(MouseWheelEvent.class, mouseWheelConvertor);

        eventFromMapping.put(DELAY_PATTERN, delayConvertor);
        eventFromMapping.put(KEY_PRESSED_PATTERN, keyPressedConvertor);
        eventFromMapping.put(KEY_RELEASED_PATTERN, keyReleasedConvertor);
        eventFromMapping.put(MOUSE_PRESSED_PATTERN, mousePressedConvertor);
        eventFromMapping.put(MOUSE_RELEASED_PATTERN, mouseReleasedConvertor);
        eventFromMapping.put(MOUSE_MOVE_PATTERN, mouseMoveConvertor);
        eventFromMapping.put(MOUSE_WHEEL_PATTERN, mouseWheelConvertor);
    }

    @Override
    public void save(File file, Record record) {
//        String path = System.getProperty("user.dir") + "\\" + "data" + "\\";

        file = new File(file.getParent() +"\\"+ file.getName() + ".wm");

        BufferedWriter bw = null;
        try {
            // 获取Cipher对象并设置解密模式、填充方式等参数
            cipher = Cipher.getInstance(TRANSFORMATION);
            bw = new BufferedWriter(new FileWriter(file));
            for (Event event : record.getEventList()) {
                Convertor convertor = getConvertor(event);
                String str = convertor.event2String(event);
                bw.write(encryptString(str,KEY));
                bw.write(LINE_SEPERATOR);
            }

        } catch (Exception e) {
            throw new MouseRecorderException(String.format("File %s cannot be saved. \n" +
                    "Please check that you have enough disk space or\n" +
                    "Enough permissions to write file.")).popup().immeadiateLog();
        } finally {
            if (bw != null) {
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    //TODO - log here
                    e.printStackTrace();
                }

            }
        }

    }

    @Override
    public Record load(File file) {
        Record record = new Record(config);
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                //文件解密
                line = decryptString(line,KEY);
                Convertor convertor = getConvertor(line);
                Event event = convertor.string2Event(line);
                record.add(event);
            }
        } catch (Exception e) {
            throw new MouseRecorderException(String.format("File %s cannot be loaded. \n")).popup().immeadiateLog();
        }
        return record;
    }

    @Override
    public Convertor getConvertor(Event event) {
        Convertor convertor = convertorFromClass.get(event.getClass());
        if (convertor == null) {
            return noneEventConvertor;
        } else {
            return convertor;
        }
    }

    @Override
    public Convertor getConvertor(String str) {
        for (Pattern pattern : eventFromMapping.keySet()) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches()) {
                return eventFromMapping.get(pattern);
            }
        }
        return noneEventConvertor;
    }

    Convertor delayConvertor = new Convertor<DelayEvent>() {
        @Override
        public String event2String(DelayEvent event) {
            return String.format("{delay %d}", event.getDelay());
        }

        @Override
        public Event string2Event(String str) {
            Matcher matcher = DELAY_PATTERN.matcher(str);
            if (matcher.find()) {
                String delayStr = matcher.group(1);
                return new DelayEvent(Long.parseLong(delayStr), config);
            }
            return NoneEvent.INSTANCE;
        }
    };

    Convertor keyPressedConvertor = new Convertor<KeyPressedEvent>() {
        @Override
        public String event2String(KeyPressedEvent event) {
            return String.format("{%s pressed}", KeyWrapper.codeToKey(event.getKey()));
        }

        @Override
        public Event string2Event(String str) {
            Matcher matcher = KEY_PRESSED_PATTERN.matcher(str);
            if (matcher.find()) {
                String key = matcher.group(1);
                return new KeyPressedEvent(KeyWrapper.keyToCode(key));
            }
            return NoneEvent.INSTANCE;
        }
    };

    Convertor keyReleasedConvertor = new Convertor<KeyReleasedEvent>() {
        @Override
        public String event2String(KeyReleasedEvent event) {
            return String.format("{%s released}", KeyWrapper.codeToKey(event.getKey()));
        }

        @Override
        public Event string2Event(String str) {
            Matcher matcher = KEY_RELEASED_PATTERN.matcher(str);
            if (matcher.find()) {
                String key = matcher.group(1);
                return new KeyReleasedEvent(KeyWrapper.keyToCode(key));
            }
            return NoneEvent.INSTANCE;
        }
    };

    Convertor mousePressedConvertor = new Convertor<MousePressedEvent>() {
        @Override
        public String event2String(MousePressedEvent event) {
            int button = event.getButton();
            return String.format("{%s pressed}", MouseWrapper.codeToKey(button));
        }

        @Override
        public Event string2Event(String str) {
            Matcher matcher = MOUSE_PRESSED_PATTERN.matcher(str);
            if (matcher.find()) {
                String key = matcher.group(1);
                return new MousePressedEvent(MouseWrapper.keyToCode(key));
            }
            return NoneEvent.INSTANCE;
        }
    };

    Convertor mouseReleasedConvertor = new Convertor() {
        @Override
        public String event2String(Event event) {
            String str = "";
            MouseReleasedEvent mousePressedEvent = (MouseReleasedEvent) event;
            int button = mousePressedEvent.getButton();
            return String.format("{%s released}", MouseWrapper.codeToKey(button));
        }

        @Override
        public Event string2Event(String str) {
            Matcher matcher = MOUSE_RELEASED_PATTERN.matcher(str);
            if (matcher.find()) {
                String key = matcher.group(1);
                return new MouseReleasedEvent(MouseWrapper.keyToCode(key));
            }
            return NoneEvent.INSTANCE;
        }
    };

    Convertor mouseMoveConvertor = new Convertor<MouseMoveEvent>() {
        @Override
        public String event2String(MouseMoveEvent event) {
            return String.format("{move (%d, %d)}", event.getX(), event.getY());
        }

        @Override
        public Event string2Event(String str) {
            Matcher matcher = MOUSE_MOVE_PATTERN.matcher(str);

            if (matcher.find() && matcher.groupCount() > 1) {
                String xStr = matcher.group(1);
                String yStr = matcher.group(2);
                try {
                    int x = Integer.parseInt(xStr);
                    int y = Integer.parseInt(yStr);
                    return new MouseMoveEvent(x, y);
                } catch (NumberFormatException iae) {
                    return NoneEvent.INSTANCE;
                }

            } else {
                return NoneEvent.INSTANCE;
            }
        }
    };

    Convertor mouseWheelConvertor = new Convertor<MouseWheelEvent>() {
        @Override
        public String event2String(MouseWheelEvent event) {
            return String.format("{wheel %d}", event.getScrollAmount());
        }

        @Override
        public Event string2Event(String str) {
            Matcher matcher = MOUSE_WHEEL_PATTERN.matcher(str);
            if (matcher.find()) {
                String scrollAmountStr = matcher.group(1);
                try {
                    int scrollAmount = Integer.parseInt(scrollAmountStr);
                    return new MouseWheelEvent(scrollAmount);
                } catch (NumberFormatException nfe) {
                    return NoneEvent.INSTANCE;
                }
            } else {
                return NoneEvent.INSTANCE;
            }
        }
    };

    Convertor noneEventConvertor = new Convertor<NoneEvent>() {
        @Override
        public String event2String(NoneEvent event) {
            return "";
        }

        @Override
        public NoneEvent string2Event(String str) {
            return NoneEvent.INSTANCE;
        }
    };

    /**
     * 加密字符串
     * @param input 待加密字符串
     * @param key 密钥
     * @return
     */
    public  String encryptString(String input, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密字符串
     * @param encryptedInput 需解密的字符串
     * @param key 密钥
     * @return
     */
    public  String decryptString(String encryptedInput, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedInput);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
