package com.scnu.yxp.tetris.factory;

import com.scnu.yxp.tetris.base.Component;
import com.scnu.yxp.tetris.component.IComponent;
import com.scnu.yxp.tetris.component.JComponent;
import com.scnu.yxp.tetris.component.LComponent;
import com.scnu.yxp.tetris.component.OComponent;
import com.scnu.yxp.tetris.component.SComponent;
import com.scnu.yxp.tetris.component.TComponent;
import com.scnu.yxp.tetris.component.ZComponent;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by yxp on 2015/10/20.
 */
public class ComponentFactory {
    public static Random random = new Random();
    public static Class[] array = new Class[]{IComponent.class, JComponent.class, ZComponent.class, OComponent.class,
            LComponent.class, SComponent.class, TComponent.class};

    public static Component createComponent(){
        int i = random.nextInt(8);
        int mode = random.nextInt(4);
        int n = random.nextInt(7);
        Component component = null;
        try {
            component = (Component)array[n].getDeclaredConstructor(new Class[]{int.class, int.class}).newInstance(new Object[]{i, mode});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }
}
