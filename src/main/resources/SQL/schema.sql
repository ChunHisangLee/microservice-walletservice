CREATE DATABASE walletdb;

\c walletdb

-- After connecting to the `walletdb` database, run the following script:

-- Drop users' table if it exists
DROP TABLE IF EXISTS users CASCADE;

-- Create users' table
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100)        NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    CONSTRAINT email_unique UNIQUE (email)
);

-- Create indexes
CREATE INDEX idx_email ON users (email);

-- Drop users' table if it exists
DROP TABLE IF EXISTS wallet CASCADE;

-- Create wallet's table
CREATE TABLE wallet
(
    id          SERIAL PRIMARY KEY,                                                   -- Auto-incrementing ID for the wallet (INTEGER instead of BIGSERIAL)
    user_id     INTEGER                    NOT NULL,                                  -- User ID associated with the wallet (matching SERIAL size in users)
    usd_balance DECIMAL(18, 2) DEFAULT 0.0 NOT NULL CHECK (usd_balance >= 0),         -- USD balance with 2 decimal precision, non-negative
    btc_balance DECIMAL(18, 8) DEFAULT 0.0 NOT NULL CHECK (btc_balance >= 0),         -- BTC balance with 8 decimal precision, non-negative
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE, -- Foreign key to the users table, cascade on delete
    CONSTRAINT check_usd_balance CHECK (usd_balance >= 0),                            -- Ensure USD balance is non-negative
    CONSTRAINT check_btc_balance CHECK (btc_balance >= 0)                             -- Ensure BTC balance is non-negative
);

-- Create an index for faster lookups by user_id
CREATE INDEX idx_user_id ON wallet (user_id);
