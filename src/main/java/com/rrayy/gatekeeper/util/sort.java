package com.rrayy.gatekeeper.util;

import java.util.ArrayList;
import java.util.Collections;

public class sort {
    public static ArrayList<Integer> itemSort(int amount) {
        ArrayList<Integer> result = new ArrayList<Integer>(Collections.nCopies(9, null));
        double interval = (amount % 2 == 0 ? 8 : 9) / (double) amount;
        int half_interval = (int) Math.round(interval / 2);
        
        for (int i = 0; i < amount; i++) {
            int index = (int) (i * Math.round(interval) + half_interval);
            result.set(index - (amount % 2 == 0 ? 0 : 1), i);
        }
        
        return result;
    }
}