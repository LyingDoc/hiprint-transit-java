//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GsonExclusion implements ExclusionStrategy {
    private List<String> exclusionFields;
    private List<Class> exclusionClasses;


    public GsonExclusion addExclusionField(String... fieldName) {
        if (this.exclusionFields == null) {
            this.exclusionFields = new ArrayList();
        }

        if (fieldName != null && fieldName.length > 0) {
            for (int i = 0; i < fieldName.length; ++i) {
                this.exclusionFields.add(fieldName[i]);
            }
        }

        return this;
    }

    public GsonExclusion addExclusionClass(Class<?>... classes) {
        if (this.exclusionClasses == null) {
            this.exclusionClasses = new ArrayList();
        }

        this.exclusionClasses.addAll(Arrays.asList(classes));
        return this;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        if (this.exclusionFields != null && this.exclusionFields.size() >= 1) {
            String fieldName = f.getName();
            return this.exclusionFields.contains(fieldName);
        } else {
            return false;
        }
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        if (this.exclusionClasses != null && this.exclusionClasses.size() >= 1) {
            return this.exclusionClasses.contains(clazz);
        } else {
            return false;
        }
    }

    public List<String> getExclusionFields() {
        return this.exclusionFields;
    }

    public void setExclusionFields(List<String> exclusionFields) {
        this.exclusionFields = exclusionFields;
    }
}
