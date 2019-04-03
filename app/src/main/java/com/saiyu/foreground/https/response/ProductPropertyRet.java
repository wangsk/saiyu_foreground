package com.saiyu.foreground.https.response;

import java.util.ArrayList;
import java.util.List;

public class ProductPropertyRet extends BaseRet{
    private ArrayList<String> data;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
