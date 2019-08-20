package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.exception.EntityNotExistException;
import io.github.packagewjx.brandreportbackend.repository.meta.IndexRepository;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
@Service
public class IndexServiceImpl extends BaseServiceImpl<Index, String> implements IndexService {
    private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);
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
    public List<Index> getLeafIndicesOfRoot(String rootIndexId) {
        logger.info("获取根指标{}的所有叶子指标中", rootIndexId);
        if (rootIndexId == null || "".equals(rootIndexId)) {
            // 若是空的话，则返回所有的
            return getAllLeafIndices();
        }
        if (!this.existById(rootIndexId)) {
            throw new EntityNotExistException("不存在ID为" + rootIndexId + "的指标");
        }
        List<Index> result = new ArrayList<>();
        logger.debug("获取所有指标中");
        Collection<Index> all = (Collection<Index>) this.getAll();
        logger.debug("提取{}的叶子指标中", rootIndexId);
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
        logger.info("获取所有叶子节点指标");
        return ((Collection<Index>) this.getAll()).parallelStream()
                .filter(index -> !Index.TYPE_INDICES.equals(index.getType())).collect(Collectors.toList());
    }

    @Override
    public List<Index> getAllByType(String type) {
        logger.info("获取类型为{}的指标", type);
        return indexRepository.findByType(type);
    }

    @Override
    public boolean isIdOfEntity(String s, Index entity) {
        return Objects.equals(s, entity.getIndexId());
    }
}
