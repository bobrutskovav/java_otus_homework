package ru.otus;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoggerTransformer implements ClassFileTransformer {


    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;


        try {
            className = className.replaceAll("/", ".");
            if (!className.startsWith("ru.otus.")) {
                return byteCode;
            }
            ClassPool classPool = ClassPool.getDefault();
            CtClass clazz = classPool.get(className);
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                if (method.hasAnnotation(Log.class)) {
                    MethodInfo methodInfo = method.getMethodInfo();
                    LocalVariableAttribute table = (LocalVariableAttribute) methodInfo.getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
                    int numberOfLocalVariables = table.tableLength();
                    StringBuilder builder = new StringBuilder();
                    builder.append(String.format("System.out.println(String.format(\"executed method: %s ", method.getName()));
                    List<String> args = new ArrayList<>();
                    for (int i = 1; i < numberOfLocalVariables; i++) {
                        builder.append(String.format("Parameter name = %s ", table.getConstPool().getUtf8Info(table.nameIndex(i))));
                        args.add(String.format("$args[%d]", i));
                        builder.append("Value = %s ");
                    }
                    builder.append("\",");
                    builder.append("$args");
                    builder.append("));");
                    try {
                        method.insertBefore(builder.toString());
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                }
            });

            byteCode = clazz.toBytecode();
            clazz.detach();
        } catch (RuntimeException | NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }

        return byteCode;
    }
}
