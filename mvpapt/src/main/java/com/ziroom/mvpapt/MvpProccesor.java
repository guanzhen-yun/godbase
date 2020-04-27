package com.ziroom.mvpapt;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * 处理器
 */

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.ziroom.annotation.MvpEmptyViewFactory"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MvpProccesor extends AbstractProcessor {
    Filer mFiler;
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mFiler = processingEnv.getFiler();
        new MvpEmptyViewProcessor().process(roundEnvironment, this);
        return true;
    }
}
