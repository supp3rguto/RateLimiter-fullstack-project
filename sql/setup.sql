USE ratelimit_db;

DROP TABLE IF EXISTS endpoint_costs;
DROP TABLE IF EXISTS api_keys;
DROP TABLE IF EXISTS plans;
DROP TABLE IF EXISTS api_limit; 

CREATE TABLE plans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE, 
    bucket_capacity INT NOT NULL, 
    refill_rate_seconds INT NOT NULL  
);

CREATE TABLE api_keys (
    api_key VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    plan_id INT NOT NULL,
    tokens INT NOT NULL,
    last_refill_timestamp BIGINT NOT NULL,
    FOREIGN KEY (plan_id) REFERENCES plans(id)
);

CREATE TABLE endpoint_costs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    endpoint_name VARCHAR(100) NOT NULL UNIQUE, -- get_limited_data
    token_cost INT NOT NULL DEFAULT 1   
);


-- test examples
INSERT INTO plans (name, bucket_capacity, refill_rate_seconds) 
VALUES ('Free', 10, 60); -- 10 tokens / 60 seconds

INSERT INTO plans (name, bucket_capacity, refill_rate_seconds) 
VALUES ('Pro', 1000, 60); -- 1000 tokens / 60 seconds

INSERT INTO api_keys (api_key, user_id, plan_id, tokens, last_refill_timestamp)
VALUES ('augusto-dev-key', 'augusto_user', 1, 10, 0);

--  endpoint cost
INSERT INTO endpoint_costs (endpoint_name, token_cost)
VALUES ('get_limited_data', 1); 


SELECT * FROM plans;
SELECT * FROM api_keys;
SELECT * FROM endpoint_costs;