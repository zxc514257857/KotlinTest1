package com.example.case6;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @Des:
 * @Title:
 * @Project:
 * @Package:
 * @Author: zhr
 * @Date: 2021/11/22 11:47
 * @Version:V1.0
 */
public final class MoshiDefaultAdapterFactory {

    private MoshiDefaultAdapterFactory() {
    }

    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            if (type == String.class) return STRING_JSON_ADAPTER;
            return null;
        }
    };

    static final JsonAdapter<String> STRING_JSON_ADAPTER = new JsonAdapter<String>() {
        @Override
        public String fromJson(JsonReader reader) throws IOException {
            // 替换null为""
            if (reader.peek() != JsonReader.Token.NULL) {
                return reader.nextString();
            }
            reader.nextNull();
            return "";
        }

        @Override
        public void toJson(JsonWriter writer, String value) throws IOException {
            writer.value(value);
        }

        @Override
        public String toString() {
            return "JsonAdapter(String)";
        }
    };
}
