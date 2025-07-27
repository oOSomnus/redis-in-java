package dispatcher.Registry;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HandlerRegistry {
    private final Map<String, Object> handlerMap = new HashMap<>();

    /**
     * scan and register handlers automatically
     *
     * @param basePackage
     */
    public void scanAndRegister(String basePackage) {
        try {
            String packagePath = basePackage.replace(".", "/");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();

                if ("file".equals(protocol)) {
                    // fs
                    scanDirectory(new File(resource.toURI()), basePackage);
                } else if ("jar".equals(protocol)) {
                    // jar
                    scanJarFile(resource, packagePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // fs
    private void scanDirectory(File directory, String packageName) throws Exception {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归扫描子目录
                    scanDirectory(file, packageName + "." + file.getName());
                } else if (file.getName().endsWith(".class")) {
                    // 处理class文件
                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                    registerClass(className);
                }
            }
        }
    }

    // jar
    private void scanJarFile(URL jarUrl, String packagePath) throws Exception {
        String jarFilepath = jarUrl.getFile().substring(5, jarUrl.getFile().indexOf("!"));
        try (JarFile jarFile = new JarFile(jarFilepath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.startsWith(packagePath) && entryName.endsWith(".class") && !entry.isDirectory()) {
                    String className = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                    registerClass(className);
                }
            }
        }
    }

    // register single class
    private void registerClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        HandlerName handlerName = clazz.getAnnotation(HandlerName.class);

        if (handlerName != null) {
            // 创建类的实例并放入map
            Object instance = clazz.getDeclaredConstructor().newInstance();
            handlerMap.put(handlerName.value(), instance);
        }
    }
    
    public Map<String, Object> getHandlerMap() {
        return Collections.unmodifiableMap(handlerMap);
    }
}
