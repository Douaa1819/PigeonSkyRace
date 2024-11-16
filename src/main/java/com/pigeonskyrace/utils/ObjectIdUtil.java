package com.pigeonskyrace.utils;

import org.bson.types.ObjectId;

public class ObjectIdUtil {


    public static ObjectId stringToObjectId(String id) {
        return id != null && !id.isEmpty() ? new ObjectId(id) : null;
    }
}
