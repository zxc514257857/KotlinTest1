package com.example.app;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * @Des:
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app
 * @Author: zhr
 * @Date: 2021/11/27 22:46
 * @Version: V1.0
 */
public class TtsUtils {

    private static final String TAG = "TtsUtils";
    private static TtsUtils instance;
    private TextToSpeech textToSpeech;
    private boolean isSupport = true;

    // 语音引擎设置的跳转 startActivity(Intent("com.android.settings.TTS_SETTINGS"))
    // tts 语音播报：讯飞引擎以及讯飞语记
    private TtsUtils(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    textToSpeech.setPitch(1.0f); // 设置音调
                    textToSpeech.setSpeechRate(1.0f); // 设置语速
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        isSupport = false;
                        Log.i(TAG, "系统不支持中文语音播报");
                    }
                }
            }
        });
    }

    public static TtsUtils getInstance(Context context) {
        if (instance == null) {
            instance = new TtsUtils(context);
        }
        return instance;
    }

    public void speak(String text) {
        if (!isSupport) {
            return;
        }
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        instance = null;
    }
} 