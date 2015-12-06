/*
 *
 * Copyright 2015 magiclen.org
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

/**
 * MSON的toString方法可能會耗用大量的時間，因此有個獨立的監聽者來監看程式的運作情形。
 *
 * @author Magic Len
 * @see Mson
 * @see JSONType
 */
public interface MsonToStringListener {

    /**
     * 當toString方法逐一執行各項資料的轉換時，會執行這個方法。
     *
     * @param object 正在處理的該筆資料物件
     * @param enter 是否正開始處理該筆資料，若為false，則表示資料已處理完
     * @param depth 資料的深度，從1開始
     */
    public void doing(final Object object, final boolean enter, final int depth);
}
