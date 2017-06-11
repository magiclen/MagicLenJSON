/*
 *
 * Copyright 2015-2017 magiclen.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.magiclen.mson;

import org.magiclen.json.JSONArray;
import org.magiclen.json.JSONObject;

/**
 * JSONObject與JSONArray共同實作的抽象類別。
 *
 * @author Magic Len
 * @see Mson
 * @see JSONArray
 * @see JSONObject
 */
public abstract class JSONType {

    /**
     * JSON物件的類型列舉。
     */
    public static enum Type {

        OBJECT, ARRAY;
    }

    /**
     * 取得JSON物件的類型。
     *
     * @return 傳回JSON物件的類型
     */
    public abstract Type getType();

    /**
     * 將此JSON物件(JSONType)透過Mson轉成字串。
     *
     * @param format 是否要進行編排格式化
     * @return 傳回字串
     */
    public String toString(final boolean format) {
        if (format) {
            return Mson.toString(this, true);
        } else {
            return toString();
        }
    }

    /**
     * 將此JSON物件(JSONType)透過Mson轉成XML。
     *
     * @return 傳回XML字串
     */
    public String toXML() {
        return Mson.toXML(this);
    }

    /**
     * 將此JSON物件(JSONType)透過Mson轉成Object物件。
     *
     * @param type 傳入物件型態(Class)，愈前面的優先嘗試，如果不傳入型態，預設使用Object
     * @return 傳回物件
     */
    public Object toObject(final java.lang.reflect.Type... type) {
        final Type t = getType();
        switch (t) {
            case ARRAY:
                return Mson.toArray((JSONArray) this, type);
            case OBJECT:
                return Mson.toObject((JSONObject) this, type);
            default:
                return null;
        }
    }
}
