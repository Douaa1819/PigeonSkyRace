package com.pigeonskyrace.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.MapperConfig;
import org.mapstruct.Named;

@MapperConfig(componentModel = "spring")

public interface GlobalMapperConfig {

    @Named("objectIdToString")
    default String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id != null ? new ObjectId(id) : null;
    }
}

