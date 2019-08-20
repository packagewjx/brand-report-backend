package io.github.packagewjx.brandreportbackend.utils;

import org.slf4j.Logger;
import org.springframework.lang.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-20
 **/
public class UtilFunctions {
    /**
     * 将updateData中非null的部分赋值给original。
     * <p>
     * 对不同的类型，按顺序处理
     * <ol>
     * <li>对Map类型，将只会更新updateData的对应的map中存在的部分。也就是使用PutAll，替换掉存在的原键值对，增加不存在的</li>
     * <li>对Set类型，替换hashcode相同的对象。也是使用PutAll，将会替换掉HashCode相等的对象</li>
     * <li>对List与数组类型，替换对应下标的对象，并添加出的部分</li>
     * <li>对数字类型，字符串类型等原始类型，则直接更改原本的值</li>
     * <li>对POJO，则会递归调用本更新函数</li>
     * </ul>
     *
     * @param original   原数据
     * @param updateData 更新的部分数据
     */
    public static <S> void partialChange(S original, S updateData, @Nullable Logger logger) {
        Field[] declaredFields = original.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object newVal;
            Object oldVal;
            try {
                newVal = field.get(updateData);
                oldVal = field.get(original);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                if (logger != null) {
                    logger.error("不应该有此错误", e);
                }
                continue;
            }
            if (newVal == null) {
                continue;
            }
            if (newVal instanceof Map) {
                assert oldVal instanceof Map;
                ((Map) oldVal).putAll(((Map) newVal));
            } else if (newVal instanceof Set) {
                assert oldVal instanceof Set;
                Iterator it = ((Set) newVal).iterator();
                while (it.hasNext()) {
                    Object v = it.next();
                    // 替换
                    ((Set) oldVal).remove(v);
                    ((Set) oldVal).add(v);
                }
            } else if (newVal instanceof List) {
                assert oldVal instanceof List;
                for (int i = 0; i < ((List) oldVal).size() && i < ((List) newVal).size(); i++) {
                    if (((List) newVal).get(i) != null) {
                        ((List) oldVal).set(i, ((List) newVal).get(i));
                    }
                }
                if (((List) newVal).size() > ((List) oldVal).size()) {
                    // 这里，我们只加newVal多出的元素的非null的元素
                    for (int i = ((List) oldVal).size(); i < ((List) newVal).size(); i++) {
                        Object val;
                        if ((val = ((List) newVal).get(i)) != null) {
                            ((List) oldVal).add(val);
                        } else {
                            if (logger != null) {
                                logger.warn("{}中字段{}的列表第{}项为null", original.getClass().getName(), field.getName(), i);
                            }
                        }
                    }
                }
            } else if (newVal instanceof Number || newVal instanceof String || newVal instanceof Boolean || newVal instanceof Character) {
                // 基本类型的包装类，因此赋值
                try {
                    field.set(original, newVal);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    if (logger != null) {
                        logger.error("赋值异常", e);
                    }
                }
            } else if (newVal.getClass().isArray()) {
                if (Array.getLength(newVal) > Array.getLength(oldVal)) {
                    // 这种情况下需要替换数组
                    for (int i = 0; i < Array.getLength(oldVal); i++) {
                        Object v;
                        if (Array.get(newVal, i) != null) {
                            continue;
                        }
                        // 只需填充空的部分即可
                        Array.set(newVal, i, Array.get(oldVal, i));
                    }
                    // 对于newVal中oldVal长度后面的值，不需要处理了，我们直接替换原本field
                    try {
                        field.set(original, newVal);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        if (logger != null) {
                            logger.error("赋值异常", e);
                        }
                    }
                } else {
                    // 这种情况下只需要设置数组的值即可
                    for (int i = 0; i < Array.getLength(newVal) && i < Array.getLength(oldVal); i++) {
                        Object v;
                        if ((v = Array.get(newVal, i)) != null) {
                            Array.set(oldVal, i, v);
                        }
                    }
                }
            } else if (!newVal.getClass().equals(Object.class)) {
                // 非普通Object，递归进入
                partialChange(oldVal, newVal, logger);
            } else {
                // 其他情况，直接赋值
                try {
                    field.set(original, newVal);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <S> boolean deepEqualsWithExcludeFields(S val1, S val2, @Nullable Set<String> notEqualFieldName) {
        if (notEqualFieldName == null) {
            notEqualFieldName = Collections.emptySet();
        }
        Field[] declaredFields = val1.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (notEqualFieldName.contains(field.getName())) {
                continue;
            }
            try {
                field.setAccessible(true);
                return Objects.deepEquals(field.get(val1), field.get(val2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
