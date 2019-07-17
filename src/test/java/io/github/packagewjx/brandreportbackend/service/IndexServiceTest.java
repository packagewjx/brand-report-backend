package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
public class IndexServiceTest extends BaseTest {
    @Autowired
    IndexService indexService;

    @Test
    public void findChildIndices() {
        String testRootIndex = "spread-power";
        List<Index> childIndices = indexService.getChildIndices(testRootIndex);
        Assert.assertNotEquals(0, childIndices.size());
        childIndices.forEach(index -> {
            Assert.assertNotEquals(Index.TYPE_INDICES, index.getType());
        });

        // 验证是否在同一棵树

        // 构建父节点map
        Map<String, String> parentMap = new HashMap<>(300);
        Iterable<Index> all = indexService.getAll();
        all.forEach(index -> parentMap.put(index.getIndexId(), index.getParentIndexId()));

        Optional<Boolean> reduce = childIndices.parallelStream().map(index -> {
            String parent = parentMap.get(index.getIndexId());
            while (parent != null) {
                if (testRootIndex.equals(parent)) {
                    return true;
                }
                parent = parentMap.get(parent);
            }
            return false;
        }).reduce((b1, b2) -> b1 & b2);
        Assert.assertTrue(reduce.orElse(false));
    }
}
