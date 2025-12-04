//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class GsonExclusion implements ExclusionStrategy {
    private List<String> exclusionFields;
    private List<Class> exclusionClasses;


    public void addExclusionField(String... fieldName) {
        if (this.exclusionFields == null) {
            this.exclusionFields = new ArrayList<>();
        }

        if (fieldName != null && fieldName.length > 0) {
            this.exclusionFields.addAll(Arrays.asList(fieldName));
        }
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (CollectionUtils.isEmpty(this.exclusionClasses)) {
            String fieldName = f.getName();
            return this.exclusionFields.contains(fieldName);
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        if (CollectionUtils.isEmpty(this.exclusionClasses)) {
            return this.exclusionClasses.contains(clazz);
        } else {
            return false;
        }
    }

}
