-- Create the `walletdb` database
CREATE DATABASE walletdb;

-- Connect to `walletdb`
\c walletdb

-- Drop the users' table if it exists
DROP TABLE IF EXISTS users CASCADE;

-- Create users' table
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,            -- Auto-incrementing primary key
    name     VARCHAR(100)        NOT NULL,  -- User's name, not nullable
    email    VARCHAR(255) UNIQUE NOT NULL,  -- Unique email, not nullable
    password VARCHAR(255)        NOT NULL,  -- User's password, not nullable
    CONSTRAINT email_unique UNIQUE (email)  -- Ensure email uniqueness
);

-- Create index for faster lookup by email
CREATE INDEX idx_email ON users (email);

-- Drop the wallet table if it exists
DROP TABLE IF EXISTS wallet CASCADE;

-- Create the wallet table
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
-- Create an index for faster lookup by user_id
CREATE INDEX idx_user_id ON wallet (user_id);
