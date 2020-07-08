package com.peigong.springcloudalibaba.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author: lilei
 * @create: 2020-07-08 15:02
 **/
public class CustomImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{BizClass.class.getName(),AnotherBizClass.class.getName()};
    }
}
