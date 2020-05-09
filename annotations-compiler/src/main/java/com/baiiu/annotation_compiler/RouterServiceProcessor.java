package com.baiiu.annotation_compiler;

import com.baiiu.annotations.RouterService;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

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
        return ImmutableSet.of(RouterService.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        System.out.println("RouterServiceProcessor#process: set: " + annotations);
        System.out.println("RouterServiceProcessor#process: roundEnvironment: " + roundEnvironment);
        System.out.println("RouterServiceProcessor#process: roundEnvironment#processingOver: " + roundEnvironment.processingOver());
        System.out.println("RouterServiceProcessor#process: roundEnvironment#getRootElements: " + roundEnvironment.getRootElements());
        System.out.println("RouterServiceProcessor#process: roundEnvironment#getElementsAnnotatedWith: " + roundEnvironment.getElementsAnnotatedWith(RouterService.class));

        try {
            return processImpl(annotations, roundEnvironment);
        } catch (Exception e) {
            // We don't allow exceptions of any kind to propagate to the compiler
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            fatalError(writer.toString());
            return true;
        }
    }


    private boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (roundEnv.processingOver()) {
            // 已经处理过了
        } else {

            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterService.class);
            if (elements == null || elements.size() == 0) {
                return false;
            }

            for (Element element : elements) {
                System.out.println("RouterServiceProcessor#process: element: " + element.toString());
            }

        }

        return true;
    }

    private void fatalError(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "FATAL ERROR: " + msg);
    }

    /**
     * 构建类
     */
    private void buildCustomClass() {
        //创建 main 方法
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)//返回值
                .addParameter(String[].class, "args")//参数
                .addStatement("$T.out.println($S)", System.class, "Hello World from JavaPoet!")
                .build();
        //创建类
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        try {
            JavaFile javaFile = JavaFile.builder("com.example", helloWorld)
                    .addFileComment("This codes are generated automatically. Do not modify!")
                    .build();
            System.out.println("path:" + filer);
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
