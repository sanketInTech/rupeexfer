-- Create account table
CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    owner_name VARCHAR(255) NOT NULL,
    balance NUMERIC(19,2) NOT NULL DEFAULT 0.00,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create transaction status type
CREATE TYPE transaction_status AS ENUM ('SUCCESS', 'FAILED');

-- Create money_transaction table
CREATE TABLE money_transaction (
    id BIGSERIAL PRIMARY KEY,
    external_id UUID NOT NULL UNIQUE,
    from_account_number VARCHAR(50) NOT NULL,
    to_account_number VARCHAR(50) NOT NULL,
    amount NUMERIC(19,2) NOT NULL CHECK (amount > 0),
    status transaction_status NOT NULL,
    reference VARCHAR(255),
    idempotency_key VARCHAR(255) UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for money_transaction
CREATE INDEX idx_money_transaction_created_at ON money_transaction(created_at);
CREATE INDEX idx_money_transaction_from_account ON money_transaction(from_account_number);
CREATE INDEX idx_money_transaction_to_account ON money_transaction(to_account_number);

-- Add foreign key constraints
ALTER TABLE money_transaction 
    ADD CONSTRAINT fk_from_account 
    FOREIGN KEY (from_account_number) 
    REFERENCES account(account_number) 
    ON DELETE RESTRICT;

-- Note: We're not adding a foreign key for to_account_number 
-- to allow transfers to external accounts

-- Create a trigger function to update updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger for account table
CREATE TRIGGER update_account_updated_at
BEFORE UPDATE ON account
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
