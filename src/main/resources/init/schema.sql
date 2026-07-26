CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;

CREATE TABLE IF NOT EXISTS vector_store (
    id TEXT PRIMARY KEY,
    content TEXT,
    metadata JSONB,
    embedding VECTOR(1024) -- Changed from 1536 to match mxbai-embed-large
);

CREATE INDEX IF NOT EXISTS vector_store_embedding_idx
ON vector_store USING HNSW (embedding vector_cosine_ops);


--
--Here is the exact reason why your setup evolved and why dropping that `schema.sql` file is the best practice for modern Spring AI applications.
--
-----
--
--### Why You Were Using `schema.sql` Earlier (The Old Way)
--
--In the earlier days of Spring AI (and standard Spring Boot database setups), the framework did not natively know how to set up the specialized tables required for vector embeddings.
--
--Because of this, you had to manage the database structure manually. You used `schema.sql` to explicitly tell PostgreSQL to do three things:
--
--* **Enable the Extension:** Run `CREATE EXTENSION IF NOT EXISTS vector;` so Postgres could understand vector math.
--* **Create the Table:** Write the exact `CREATE TABLE vector_store (...)` statement, defining columns for `id`, `content`, `metadata`, and the `embedding` vector.
--* **Set the Dimensions:** Hardcode the vector dimensions directly into the SQL script (e.g., `embedding VECTOR(1024)`).
--
--If you ever changed your embedding model (e.g., switching to a model that outputs 768 dimensions instead of 1024), you had to manually go into `schema.sql`, update the number, drop your old table, and rebuild it.
--
-----
--
--### Why You Don't Need It Now (The Modern Spring AI Way)
--
--With newer releases like Spring AI `1.0.0-M3`, the Spring team introduced deep, native auto-configuration for vector databases.
--
--By adding these two lines to your `application.properties`:
--
--```properties
--spring.ai.vectorstore.pgvector.initialize-schema=true
--spring.ai.vectorstore.pgvector.dimensions=1024
--
--```
--
--You are handing the keys over to Spring AI's `PgVectorStoreAutoConfiguration` class. Here is why this is much better:
--
--* **Zero Boilerplate:** When your app starts, Spring AI connects to PostgreSQL and dynamically executes the `CREATE EXTENSION` and `CREATE TABLE` commands for you. You don't have to write a single line of SQL.
--* **Dynamic Flexibility:** Notice how the dimensions are now a property variable (`1024`) instead of a hardcoded SQL string? If you change your model in the future, you just update the properties file, and Spring AI adjusts the table schema accordingly.
--* **Type Registration:** This is the most crucial part for your specific bug. When Spring AI handles the initialization, it ensures that the custom JDBC types (like `com.pgvector.PGvector`) are safely registered with the HikariCP connection pool *before* it tries to insert any data.