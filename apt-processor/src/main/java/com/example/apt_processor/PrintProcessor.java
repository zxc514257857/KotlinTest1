package com.example.apt_processor;

import com.example.apt_annotation.Print;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 注解扫描类的写法
 * 1.写一个类，继承AbstractProcessor
 * 2.添加AutoService注解（固定写法）
 */
@AutoService(Processor.class)
public class PrintProcessor extends AbstractProcessor {

    /**
     * 重写init方法，输出HelloAPT
     *
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // java输出方法
        System.out.println("HelloAPT");
        // 注解处理器提供的API输出
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "HelloAPT");
        // Make project ，如果成功输出 HelloAPT 则表示APT环境已经配置完成，可以继续进行下一步了
    }

    /**
     * 重写 getSupportedAnnotationTypes。添加要扫描的注解，可以添加多个
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> hashSet = new HashSet<>();
        // 获取规范名称
        hashSet.add(Print.class.getCanonicalName());
        return hashSet;
    }

    /**
     * 重写 getSupportedSourceVersion。获取编译版本，固定写法即可
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 真正解析注解的地方，是在process方法
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 获取注解元素
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Print.class);

        // 这样写没有生成 name age的打印
//        // 遍历注解元素
//        for (Element element : elements) {
//            // 拿到成员变量名
//            Name simpleName = element.getSimpleName();
//            // 输出成员变量名
//            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, simpleName);
//        }

        // 这样写没有在 app-build-generaged-ap_generated_sourse-debug-out 目录下生成PrintUtil类
//        try {
//            JavaFileObject fileObject = processingEnv.getFiler().createClassFile("PrintUtil");
//            Writer writer = fileObject.openWriter();
//            writer.write("package com.example.case7;\n");
//            writer.write("\n");
//            writer.write("public class PrintUtil { \n");
//            // 因为可能有多个方法，这里使用循环
//            for (Element element : elements) {
//                // 拿到成员变量名
//                Name simpleName = element.getSimpleName();
//                writer.write("    // 输出" + simpleName + "\n");
//                writer.write("    public static void print$$" + simpleName + "() { \n");
//                writer.write("    System.out.println(\"Hello " + simpleName + "\");\n } \n\n");
//            }
//            writer.write("}");
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return false;
    }

}