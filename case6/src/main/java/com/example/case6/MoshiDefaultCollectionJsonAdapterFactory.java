package com.example.case6;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @Des:
 * @Title:
 * @Project:
 * @Package:
 * @Author: zhr
 * @Date: 2021/11/22 11:48
 * @Version:V1.0
 */
public abstract class MoshiDefaultCollectionJsonAdapterFactory<C extends Collection<T>, T> extends JsonAdapter<C> {

    public static final Factory FACTORY =
            new Factory() {
                @Override
                public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
                    Class<?> rawType = Types.getRawType(type);
                    if (!annotations.isEmpty()) return null;
                    if (rawType == List.class || rawType == Collection.class) {
                        return newArrayListAdapter(type, moshi);
                    } else if (rawType == Set.class) {
                        return newLinkedHashSetAdapter(type, moshi);
                    }
                    return null;
                }
            };

    private final JsonAdapter<T> elementAdapter;

    private MoshiDefaultCollectionJsonAdapterFactory(JsonAdapter<T> elementAdapter) {
        this.elementAdapter = elementAdapter;
    }

    static <T> JsonAdapter<Collection<T>> newArrayListAdapter(Type type, Moshi moshi) {
        Type elementType = Types.collectionElementType(type, Collection.class);
        JsonAdapter<T> elementAdapter = moshi.adapter(elementType);
        return new MoshiDefaultCollectionJsonAdapterFactory<Collection<T>, T>(elementAdapter) {
            @Override
            Collection<T> newCollection() {
                return new ArrayList<>();
            }
        };
    }

    static <T> JsonAdapter<Set<T>> newLinkedHashSetAdapter(Type type, Moshi moshi) {
        Type elementType = Types.collectionElementType(type, Collection.class);
        JsonAdapter<T> elementAdapter = moshi.adapter(elementType);
        return new MoshiDefaultCollectionJsonAdapterFactory<Set<T>, T>(elementAdapter) {
            @Override
            Set<T> newCollection() {
                return new LinkedHashSet<>();
            }
        };
    }

    abstract C newCollection();

    @Override
    public C fromJson(JsonReader reader) throws IOException {
        C result = newCollection();
        if (reader.peek() == JsonReader.Token.NULL) {
            // null 直接返回空collection
            reader.nextNull();
            return result;
        }
        reader.beginArray();
        while (reader.hasNext()) {
            result.add(elementAdapter.fromJson(reader));
        }
        reader.endArray();
        return result;
    }

    @Override
    public void toJson(JsonWriter writer, C value) throws IOException {
        writer.beginArray();
        if (value != null) {
            for (T element : value) {
                elementAdapter.toJson(writer, element);
            }
        }
        writer.endArray();
    }

    @Override
    public String toString() {
        return elementAdapter + ".collection()";
    }
}