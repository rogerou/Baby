/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.utils;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseEmojiconInfoProvider;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.model.EaseDefaultEmojiconDatas;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EaseSmileUtils {
    public static final String DELETE_KEY = "em_delete_delete_expression";
    
	public static final String EE_1 = "[):]";
	public static final String EE_2 = "[:D]";
	public static final String EE_3 = "[;)]";
	public static final String EE_4 = "[:-o]";
	public static final String EE_5 = "[:p]";
	public static final String EE_6 = "[(H)]";
	public static final String EE_7 = "[:@]";
	public static final String EE_8 = "[:s]";
	public static final String EE_9 = "[:$]";
	public static final String EE_10 = "[:(]";
	public static final String EE_11 = "[:'(]";
	public static final String EE_12 = "[:|]";
	public static final String EE_13 = "[(a)]";
	public static final String EE_14 = "[8o|]";
	public static final String EE_15 = "[8-|]";
	public static final String EE_16 = "[+o(]";
	public static final String EE_17 = "[<o)]";
	public static final String EE_18 = "[|-)]";
	public static final String EE_19 = "[*-)]";
	public static final String EE_20 = "[:-#]";
	public static final String EE_21 = "[:-*]";
	public static final String EE_22 = "[^o)]";
	public static final String EE_23 = "[8-)]";
	public static final String EE_24 = "[(|)]";
	public static final String EE_25 = "[(u)]";
	public static final String EE_26 = "[(S)]";
	public static final String EE_27 = "[(*)]";
	public static final String EE_28 = "[(#)]";
	public static final String EE_29 = "[(R)]";
	public static final String EE_30 = "[({)]";
	public static final String EE_31 = "[(})]";
	public static final String EE_32 = "[(k)]";
	public static final String EE_33 = "[(F)]";
	public static final String EE_34 = "[(W)]";
	public static final String EE_35 = "[(D)]";
	
	private static final Factory spannableFactory = Factory
	        .getInstance();
	
	private static final Map<Pattern, Object> emoticons = new HashMap<>();
	

	static {
	    EaseEmojicon[] emojicons = EaseDefaultEmojiconDatas.getData();
	    for(int i = 0; i < emojicons.length; i++){
	        addPattern(emojicons[i].getEmojiText(), emojicons[i].getIcon());
	    }
	    EaseEmojiconInfoProvider emojiconInfoProvider = EaseUI.getInstance().getEmojiconInfoProvider();
	    if(emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null){
	        for(Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()){
	            addPattern(entry.getKey(), entry.getValue());
	        }
	    }
	    
	}

	private EaseSmileUtils() throws InstantiationException {
		throw new InstantiationException("This utility class is not created for instantiation");
	}

	/**
	 * 添加文字表情mapping
	 * @param emojiText emoji文本内容
	 * @param icon 图片资源id或者本地路径
	 */
	public static void addPattern(String emojiText, Object icon){
	    emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
	}
	

	/**
	 * replace existing spannable with smiles
	 * @param context
	 * @param spannable
	 * @return
	 */
	public static boolean addSmiles(Context context, Spannable spannable) {
	    boolean hasChanges = false;
	    for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(spannable);
	        while (matcher.find()) {
	            boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(),
	                    matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start()
	                        && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;
	                Object value = entry.getValue();
	                if(value instanceof String && !((String) value).startsWith("http")){
	                    File file = new File((String) value);
	                    if(!file.exists() || file.isDirectory()){
	                        return false;
	                    }
	                    spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
	                            matcher.start(), matcher.end(),
	                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	                }else{
	                    spannable.setSpan(new ImageSpan(context, (Integer)value),
	                            matcher.start(), matcher.end(),
	                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	                }
	            }
	        }
	    }
	    
	    return hasChanges;
	}

	public static Spannable getSmiledText(Context context, CharSequence text) {
	    Spannable spannable = spannableFactory.newSpannable(text);
	    addSmiles(context, spannable);
	    return spannable;
	}
	
	public static boolean containsKey(String key){
		boolean b = false;
		for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(key);
	        if (matcher.find()) {
	        	b = true;
	        	break;
	        }
		}
		
		return b;
	}
	
	public static int getSmilesSize(){
        return emoticons.size();
    }
    
	
}
