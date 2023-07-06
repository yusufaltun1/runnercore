package com.jtestrunner.core;

import com.jtestrunner.UrlConfig;
import com.jtestrunner.core.resolver.PackagePathResolver;
import com.jtestrunner.model.ModuleType;
import com.jtestrunner.util.StringUtil;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

/**
 * Created by yusufaltun on 04/04/2017.
 */
public class ClassLoader
{
    private static HashMap<String, Class<?>> pages = new HashMap<String, Class<?>>();
    private static PackagePathResolver packagePathResolver;
    protected static void init()
    {
        if(packagePathResolver == null)
        {
            throw new RuntimeException("path resolver must not be null");
        }

        Reflections reflections = new Reflections(packagePathResolver.getPath());
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(PageType.class);

        for (Class<?> pageCls : annotated)
        {
            PageType type = pageCls.getAnnotation(PageType.class);
            String url;
            if(ModuleType.STOREFRONT.equals(type.module()))
            {
                if(type.useAbsolutePath())
                {
                    url = UrlConfig.FULL_URL + type.url();
                }else
                {
                    url = type.url();
                }
            }
            else if(ModuleType.HAC.equals(type.module()))
            {
                url = UrlConfig.BASE_URL +"/hac"+ type.url();
            }
            else {
                url = type.url();
            }
            pages.put(url, pageCls);
        }
    }

    private static Page createInstance(Class<?> clz, String url)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        Constructor<?> ctor = clz.getConstructor();
        Page page = (Page) ctor.newInstance();
        page.setUrl(url);
        return page;
    }

    public static Page get(String url) throws Exception
    {
        if(url != null)
        {
            if(pages.containsKey(url))
            {
                return createInstance(pages.get(url), url);
            }

            for (String key : pages.keySet())
            {
                try
                {
                    if(StringUtil.regexMatch(key, url))
                    {
                        return createInstance(pages.get(key), url);
                    }
                }
                catch (PatternSyntaxException e)
                {
                    // DO NOTHING
                }

            }
        }

        return null;
    }

    public static void setPagePackagePath(PackagePathResolver pagePackagePath) {
        packagePathResolver = pagePackagePath;
    }
}
