package com.baiiu.annotation_compiler;

import com.baiiu.annotations.RouterService;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class RouterServiceProcessor extends AbstractProcessor {
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 支持的注解
        Set<String> sets = new HashSet<>();
        sets.add(RouterService.class.getCanonicalName());
        return sets;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("MyClass#process: set: " + set);
        System.out.println("MyClass#process: roundEnvironment: " + roundEnvironment);
        System.out.println("MyClass#process: roundEnvironment#getRootElements: " + roundEnvironment.getRootElements());
        System.out.println("MyClass#process: roundEnvironment#getElementsAnnotatedWith: " + roundEnvironment.getElementsAnnotatedWith(RouterService.class));

        return true;
    }

}
