package com.jnu.pureaccount;

import static org.junit.Assert.assertEquals;
import static com.jnu.pureaccount.AccountTypeUtils.*;
import static com.jnu.pureaccount.utils.AccountTypeUtils.getCodeByName;
import static com.jnu.pureaccount.utils.AccountTypeUtils.getNameByCode;

import org.junit.Test;

public class AccountTypeUtils {
    @Test
    public void getNameByCode_isCorrect(){
        assertEquals("吃喝饮食",getNameByCode(1));
        assertEquals("逛街购物",getNameByCode(7));
        assertEquals("其他",getNameByCode(15));
    }
    @Test
    public void getCodeByName_isCorrect(){
        assertEquals(1,getCodeByName("吃喝饮食"));
        assertEquals(5,getCodeByName("房租水电"));
    }
}
