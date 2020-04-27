package com.ziroom.mvpapt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.ziroom.annotation.MvpEmptyViewFactory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

class MvpEmptyViewProcessor {

    void process(RoundEnvironment roundEnv, MvpProccesor processor) {
        try {
            String CLASS_NAME = "MvpEmptyViewFactory";
            TypeSpec.Builder tb = TypeSpec.classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("empty view by apt");
            MethodSpec.Builder methodBuilder1 = MethodSpec.methodBuilder("create")
                    .returns(Object.class).addModifiers(PUBLIC, STATIC).addException(IllegalAccessException.class).addException(InstantiationException.class)
                    .addParameter(Class.class, "mClass");
            List<ClassName> mList = new ArrayList<>();
            CodeBlock.Builder blockBuilder = CodeBlock.builder();

            blockBuilder.beginControlFlow(" switch (mClass.getCanonicalName())");

            for (TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(MvpEmptyViewFactory.class))) {
                ClassName currentType = ClassName.get(element);
                if (mList.contains(currentType)){
                    continue;
                }
                mList.add(currentType);
                StringBuilder s = new StringBuilder();
                List<? extends Element> enclosedElements = element.getEnclosedElements();
                for (int i = 0; i < enclosedElements.size(); i++) {
                    if (enclosedElements.get(i) instanceof ExecutableElement) {
                        ExecutableElementBean elementBean = ExecutableElementParseUtil.parseElement((ExecutableElement) enclosedElements.get(i));
                        s.append("@Override ").append("public").append(" ").append(elementBean.returnType).append(" ").append(elementBean.methordName).append("(").append(elementBean.params).append(")").append(String.format("{%s}\n", ExecutableElementParseUtil.getReturnType(elementBean)));
                    }
                }
                getSuperFun(element, s);
                blockBuilder.addStatement("case $S : \n return  new $T(){ \n$L }", element.toString(), currentType, s);
            }

            blockBuilder.addStatement("default: return null");
            blockBuilder.endControlFlow();
            methodBuilder1.addCode(blockBuilder.build());
            tb.addMethod(methodBuilder1.build());
            JavaFile javaFile = JavaFile.builder("ziroom.zo.mvp", tb.build()).build();
            javaFile.writeTo(processor.mFiler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSuperFun(TypeElement element, StringBuilder s) {
        List<? extends TypeMirror> interfaces = element.getInterfaces();
        if (interfaces != null && interfaces.size() > 0) {
            for (int i = 0; i < interfaces.size(); i++) {
                TypeMirror typeMirror = interfaces.get(i);
                if (typeMirror instanceof DeclaredType) {
                    Element element1 = ((DeclaredType) typeMirror).asElement();
                    List<? extends Element> innerElements = element1.getEnclosedElements();
                    for (int j = 0; j < innerElements.size(); j++) {
                        if (innerElements.get(i) instanceof ExecutableElement) {
                            ExecutableElementBean elementBean = ExecutableElementParseUtil.parseElement((ExecutableElement) innerElements.get(j));
                            s.append("@Override ").append("public").append(" ").append(elementBean.returnType).append(" ").append(elementBean.methordName).append("(").append(elementBean.params).append(")").append(String.format("{%s}\n", ExecutableElementParseUtil.getReturnType(elementBean)));
                        }
                    }
                    if (element1 instanceof TypeElement) {
                        getSuperFun((TypeElement) element1, s);
                    }
                }
            }
        }
    }
}
