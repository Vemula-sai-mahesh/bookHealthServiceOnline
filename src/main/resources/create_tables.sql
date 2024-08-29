-- create_tables.sql

-- Create schema if it does not exist
-- create_tables.sql

-- Create schema if it does not exist
CREATE SCHEMA IF NOT EXISTS %s;


-- Create user_category table
CREATE TABLE IF NOT EXISTS %s.user_category (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    status BOOLEAN NOT NULL
    );

-- Create user table
CREATE TABLE IF NOT EXISTS %s.users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_category_id BIGINT NOT NULL,
    email VARCHAR(255),
    gender VARCHAR(10) NOT NULL ,
    phone_number VARCHAR(20) NOT NULL,
    created_by VARCHAR(50),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (user_category_id) REFERENCES %s.user_category(id)
    );
-- Create patient table
CREATE TABLE IF NOT EXISTS %s.patient (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    date_of_birth DATE ,
    gender VARCHAR(10) NOT NULL ,
    contact_number VARCHAR(20) ,
    email VARCHAR(255),
    address TEXT,
    emergency_contact VARCHAR(20) UNIQUE NOT NULL,
    user_id BIGINT UNIQUE NOT NULL,
    created_by VARCHAR(50),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES %s.users(id)
    );


CREATE TABLE IF NOT EXISTS %s.doctors (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    specialty VARCHAR(100),
    qualification VARCHAR(100),
    experience_years INT,
    contact_number VARCHAR(15),
    email VARCHAR(100),
    user_id BIGINT UNIQUE NOT NULL,
    created_by VARCHAR(50),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES %s.users(id)
);

