    CREATE TABLE USER (
        id SERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL
    );

    CREATE TABLE COURSE(
        id SERIAL PRIMARY KEY,
        user_id INT REFERENCES USER(id) ON DELETE CASCADE,
        name VARCHAR(100) NOT NULL,
        average_method TEXT NOT NULL,
        cut_off_grade DECIMAL(5,2) DEFAULT 6.00
    );

    CREATE TABLE PROJECTION(
        id SERIAL PRIMARY KEY,
        course_id INT REFERENCES COURSE(id) ON DELETE CASCADE,
        name VARCHAR(100) NOT NULL,
        final_grade DECIMAL(5,2) DEFAULT NULL
    );

    CREATE TABLE ASSESSMENT(
        id SERIAL PRIMARY KEY,
        projection_id INT REFERENCES PROJECTION(id) ON DELETE CASCADE,
        identifier VARCHAR(30) NOT NULL,
        grade DECIMAL(5,2) DEFAULT NULL,
        max_value DECIMAL(5,2) DEFAULT 10.00,
        required_grade DECIMAL(5,2),
        done BOOLEAN DEFAULT FALSE
    );

