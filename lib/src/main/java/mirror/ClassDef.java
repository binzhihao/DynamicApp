package mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public final class ClassDef {

    private static HashMap<Class<?>,Constructor<?>> REF_TYPES = new HashMap<Class<?>, Constructor<?>>();
    static {
        try {
            REF_TYPES.put(FieldDef.class, FieldDef.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(MethodDef.class, MethodDef.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(IntFieldDef.class, IntFieldDef.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(LongFieldDef.class, LongFieldDef.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(BooleanFieldDef.class, BooleanFieldDef.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(StaticFieldDef.class, StaticFieldDef.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(StaticIntFieldDef.class, StaticIntFieldDef.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(StaticMethodDef.class, StaticMethodDef.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(CtorDef.class, CtorDef.class.getConstructor(Class.class, Field.class));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> init(Class<?> mappingClass, String className) {
        try {
            return init(mappingClass, Class.forName(className));
        } catch (Exception e) {
            return null;
        }
    }


    public static Class init(Class mappingClass, Class<?> realClass) {
        Field[] fields = mappingClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (Modifier.isStatic(field.getModifiers())) {
                    Constructor<?> constructor = REF_TYPES.get(field.getType());
                    if (constructor != null) {
                        field.set(null, constructor.newInstance(realClass, field));
                    }
                }else {
                    throw new IllegalStateException("Field is not static.");
                }
            }
            catch (Exception e) {
                // Ignore
            }
        }
        return realClass;
    }

}