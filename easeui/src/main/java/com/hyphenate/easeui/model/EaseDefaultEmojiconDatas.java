package com.hyphenate.easeui.model;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojicon.Type;
import com.hyphenate.easeui.utils.EaseSmileUtils;

public class EaseDefaultEmojiconDatas {
    
    private static String[] emojis = new String[]{
        EaseSmileUtils.EE_1,
        EaseSmileUtils.EE_2,
        EaseSmileUtils.EE_3,
        EaseSmileUtils.EE_4,
        EaseSmileUtils.EE_5,
        EaseSmileUtils.EE_6,
        EaseSmileUtils.EE_7,
        EaseSmileUtils.EE_8,
        EaseSmileUtils.EE_9,
        EaseSmileUtils.EE_10,
        EaseSmileUtils.EE_11,
        EaseSmileUtils.EE_12,
        EaseSmileUtils.EE_13,
        EaseSmileUtils.EE_14,
        EaseSmileUtils.EE_15,
        EaseSmileUtils.EE_16,
        EaseSmileUtils.EE_17,
        EaseSmileUtils.EE_18,
        EaseSmileUtils.EE_19,
        EaseSmileUtils.EE_20,
        EaseSmileUtils.EE_21,
        EaseSmileUtils.EE_22,
        EaseSmileUtils.EE_23,
        EaseSmileUtils.EE_24,
        EaseSmileUtils.EE_25,
        EaseSmileUtils.EE_26,
        EaseSmileUtils.EE_27,
        EaseSmileUtils.EE_28,
        EaseSmileUtils.EE_29,
        EaseSmileUtils.EE_30,
        EaseSmileUtils.EE_31,
        EaseSmileUtils.EE_32,
        EaseSmileUtils.EE_33,
        EaseSmileUtils.EE_34,
        EaseSmileUtils.EE_35,
       
    };
    
    private static int[] icons = new int[]{
        R.drawable.ee_1,  
        R.drawable.ee_2,  
        R.drawable.ee_3,  
        R.drawable.ee_4,  
        R.drawable.ee_5,  
        R.drawable.ee_6,  
        R.drawable.ee_7,  
        R.drawable.ee_8,  
        R.drawable.ee_9,  
        R.drawable.ee_10,  
        R.drawable.ee_11,  
        R.drawable.ee_12,  
        R.drawable.ee_13,  
        R.drawable.ee_14,  
        R.drawable.ee_15,  
        R.drawable.ee_16,  
        R.drawable.ee_17,  
        R.drawable.ee_18,  
        R.drawable.ee_19,  
        R.drawable.ee_20,  
        R.drawable.ee_21,  
        R.drawable.ee_22,  
        R.drawable.ee_23,  
        R.drawable.ee_24,  
        R.drawable.ee_25,  
        R.drawable.ee_26,  
        R.drawable.ee_27,  
        R.drawable.ee_28,  
        R.drawable.ee_29,  
        R.drawable.ee_30,  
        R.drawable.ee_31,  
        R.drawable.ee_32,  
        R.drawable.ee_33,  
        R.drawable.ee_34,  
        R.drawable.ee_35,  
    };
    
    
    private static final EaseEmojicon[] DATA = createData();
    
    private static EaseEmojicon[] createData(){
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], emojis[i], Type.NORMAL);
        }
        return datas;
    }
    
    public static EaseEmojicon[] getData(){
        return DATA;
    }
}
