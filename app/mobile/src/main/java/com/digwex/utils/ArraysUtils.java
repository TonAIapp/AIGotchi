package com.digwex.utils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArraysUtils {

    @Nullable
    public static List<Integer> intArrayToIntegerList(@Nullable int[] arr){
        if(arr == null){
            return null;
        }

        ArrayList<Integer> list = new ArrayList<>();
        for(int i : arr){
            list.add(i);
        }

        return list;
    }

    @Nullable
    public static int[] IntegerListToIntArray(@Nullable List<Integer> integers){
        if(integers == null){
            return null;
        }

        int[] arr = new int[integers.size()];

        for(int i = 0; i < integers.size(); i ++){
            arr[i] = integers.get(i);
        }

        return arr;
    }
}
