package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.repository.meta.IndexRepository;
import io.github.packagewjx.brandreportbackend.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
@Service
public class IndexService extends BaseServiceImpl<Index, String> {
    private IndexRepository indexRepository;

    protected IndexService(IndexRepository repository) {
        super(repository);
        this.indexRepository = repository;
    }

    /**
     * 寻找某个中间rootIndexId的子树的所有叶子节点
     *
     * @param rootIndexId 根IndexId
     * @return 叶子Index
     */
    public List<Index> findChildIndices(String rootIndexId) {
        if (rootIndexId == null || "".equals(rootIndexId) || !this.existById(rootIndexId)) {
            return Collections.emptyList();
        }
        List<Index> result = new ArrayList<>();
        Collection<Index> all = (Collection<Index>) this.getAll();
        List<String> queue = new ArrayList<>();
        queue.add(rootIndexId);
        while (queue.size() > 0) {
            String parentId = queue.get(0);
            queue.remove(0);
            // 叶子节点加入结果
            all.parallelStream()
                    .filter(index -> parentId.equals(index.getParentIndexId()))
                    .filter(index -> !index.getType().equals(Index.TYPE_INDICES))
                    .forEach(result::add);
            // 中间节点加入队列
            all.parallelStream()
                    .filter(index -> parentId.equals(index.getParentIndexId()))
                    .filter(index -> index.getType().equals(Index.TYPE_INDICES))
                    .forEach(index -> queue.add(index.getIndexId()));
        }
        return result;
    }
}
