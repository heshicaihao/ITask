package com.xxdc.itask.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil
{
    /**
     *把Bitmap转换成Bytes
     * @param Bitmap
     * @return byte[]
     */
    public static byte[] Bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 把Bytes转换成Bimap
     * @param byte[]
     * @return Bitmap
     */
    public static Bitmap Bytes2Bimap(byte[] b)
    {
        if (b.length != 0)
        {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        else
        {
            return null;
        }
    }

}
