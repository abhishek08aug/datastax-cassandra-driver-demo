
package com.example.cassandra;

import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class AsynchronousClient extends SimpleClient {

    public ResultSetFuture getRows() {
        Statement statement = QueryBuilder.select().all().from("simplex", "songs");
        return session.executeAsync(statement);
    }

    public static void main(String[] args) {
        SimpleClient client = new AsynchronousClient();
        client.connect("127.0.0.1");
        client.dropSchema();
        client.createSchema();
        client.loadData();
        ResultSetFuture results = ((AsynchronousClient) client).getRows();
        for (Row row : results.getUninterruptibly()) {
            System.out.printf("%s: %s / %s\n", row.getString("artist"), row.getString("title"), row.getString("album"));
        }
        client.close();
    }

}
