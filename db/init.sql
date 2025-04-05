CREATE TABLE metrics (
                         id SERIAL PRIMARY KEY,
                         date DATE NOT NULL,
                         clicks INTEGER,
                         impressions INTEGER,
                         cost NUMERIC
);

CREATE TABLE strategies (
                            id SERIAL PRIMARY KEY,
                            name TEXT,
                            generated_at TIMESTAMP
);

CREATE TABLE report_log (
                            id SERIAL PRIMARY KEY,
                            file_path TEXT,
                            created_at TIMESTAMP
);
