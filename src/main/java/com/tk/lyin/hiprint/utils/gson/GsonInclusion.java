//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GsonInclusion implements ExclusionStrategy {
    private List<String> inclusionFields;


    public GsonInclusion addInclusionFields(String... fieldName) {
        if (this.inclusionFields == null) {
            this.inclusionFields = new ArrayList();
        }

        if (fieldName != null && fieldName.length > 0) {
            Collections.addAll(this.inclusionFields, fieldName);
        }

        return this;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        String fieldName = f.getName();
        return !this.inclusionFields.contains(fieldName);
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    public List<String> getInclusionFields() {
        return this.inclusionFields;
    }

    public void setInclusionFields(List<String> inclusionFields) {
        this.inclusionFields = inclusionFields;
    }
}
