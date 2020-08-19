package io.devarc.db.mongodb;

import lombok.NonNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A JUnit5 Extension based on SpringExtension for dropping mongo db collections and cleaning indices
 */
public class MongoDbCleanUpExtension extends SpringExtension {
    private void cleanupCollectionsAndIndices(@NonNull MongoTemplate mongoTemplate) {
        final MongoMappingContext mappingContext =
                (MongoMappingContext) mongoTemplate.getConverter().getMappingContext();

        final MongoPersistentEntityIndexResolver indexResolver =
                new MongoPersistentEntityIndexResolver(mappingContext);

        mappingContext.getManagedTypes().forEach(managedType -> {
                    Class<?> managedClass = managedType.getType();
                    mongoTemplate.dropCollection(managedClass);
                    indexResolver.resolveIndexFor(managedType).forEach(indexInfo ->
                            mongoTemplate.indexOps(managedClass).ensureIndex(indexInfo));
                });
    }

    @Override
    public void beforeEach(@NonNull ExtensionContext extensionContext) throws Exception {
        super.beforeEach(extensionContext);
        MongoTemplate mongoTemplate = getApplicationContext(extensionContext).getBean(MongoTemplate.class);
        cleanupCollectionsAndIndices(mongoTemplate);
    }
}
