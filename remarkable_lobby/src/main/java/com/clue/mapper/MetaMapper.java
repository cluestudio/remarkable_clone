package com.clue.mapper;

import java.nio.*;

import com.clue.fbs.RmsMetaSet;
import com.clue.model.MetaSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import org.apache.commons.codec.binary.Base64;

public class MetaMapper {
    Logger logger = LoggerFactory.getLogger(getClass());

    public String getAllQuery(String database) {
        return String.format(
                "SELECT * FROM `%s`.`meta` ORDER BY `meta`.`no` DESC LIMIT 1;",
                database
        );
    }

    public MetaSet parseMeta(ResultSet resultSet) {
        if (resultSet.getNumRows() != 1) {
            return null;
        }

        MetaSet metaSet = new MetaSet();
        for (JsonArray row: resultSet.getResults()) {
            metaSet.version = row.getInteger(0);
            byte[] decodedBytes = row.getBinary(3);
            ByteBuffer buffer = ByteBuffer.wrap(decodedBytes);
            metaSet.data = RmsMetaSet.getRootAsRmsMetaSet(buffer);
            metaSet.binary = decodedBytes;
            break;
        }

        return metaSet;
    }
}
