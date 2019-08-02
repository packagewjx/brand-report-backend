package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.repository.meta.IndexRepository;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
@Service
public class IndexServiceImpl extends BaseServiceImpl<Index, String> implements IndexService {
    private IndexRepository indexRepository;

    protected IndexServiceImpl(IndexRepository repository) {
        super(repository);
        this.indexRepository = repository;
    }

    /**
     * 寻找某个中间rootIndexId的子树的所有叶子节点
     *
     * @param rootIndexId 根IndexId
     * @return 叶子Index
     */
    @Override
    public List<Index> getChildIndices(String rootIndexId) {
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

    @Override
    public List<Index> getAllLeafIndices() {
        return ((Collection<Index>) this.getAll()).parallelStream()
                .filter(index -> !Index.TYPE_INDICES.equals(index.getType())).collect(Collectors.toList());
    }

    @Override
    public List<Index> getAllByType(String type) {
        return indexRepository.findByType(type);
    }
}
