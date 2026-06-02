DROP TABLE IF EXISTS NOTES;
DROP TABLE IF EXISTS STAKEHOLDER_LOCATION;
DROP TABLE IF EXISTS location_checklist_completion;
DROP TABLE IF EXISTS PROFILE_EXERCISES;
DROP TABLE IF EXISTS CHECKLIST;
DROP TABLE IF EXISTS FAQ;
DROP TABLE IF EXISTS WORKSHOPS;
DROP TABLE IF EXISTS PHASE;
DROP TABLE IF EXISTS STAKEHOLDERS;
DROP TABLE IF EXISTS DEVICES;
DROP TABLE IF EXISTS LOCATIONS;
DROP TABLE IF EXISTS EXERCISES;
DROP TABLE IF EXISTS PROFILES;
DROP TABLE IF EXISTS auth_tokens;

CREATE TABLE PROFILES (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255),
    AGE INT,
    EMAIL VARCHAR(255) NOT NULL UNIQUE,
    GENDER VARCHAR(255),
    PASSWORD VARCHAR(255),
    NATIONALITY CHAR(2),
    BMI DECIMAL(4, 1),
    performed_exercises INT,
    performed_training_sessions INT,
    number_of_benches INT,
    movement_minutes INT,
    registered_at DATETIME
);

CREATE TABLE EXERCISES (
    exercise_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exercise_number VARCHAR(255),
    exercise_type VARCHAR(100),
    exercise_level INT,
    exercise_performed INT,
    exercise_favorite INT,
    movement_minutes INT
);

CREATE TABLE LOCATIONS (
    bench_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bench_name VARCHAR(255),
    bench_owner VARCHAR(255),
    bench_city VARCHAR(255),
    bench_country CHAR(2),
    bench_size VARCHAR(50),
    bench_type VARCHAR(50),
    connected_routes INT,

    top10_1_exercise_id BIGINT,
    top10_2_exercise_id BIGINT,
    top10_3_exercise_id BIGINT,
    top10_4_exercise_id BIGINT,
    top10_5_exercise_id BIGINT,
    top10_6_exercise_id BIGINT,
    top10_7_exercise_id BIGINT,
    top10_8_exercise_id BIGINT,
    top10_9_exercise_id BIGINT,
    top10_10_exercise_id BIGINT,

    exercises_per_day_monday INT,
    exercises_per_day_tuesday INT,
    exercises_per_day_wednesday INT,
    exercises_per_day_thursday INT,
    exercises_per_day_friday INT,
    exercises_per_day_saturday INT,
    exercises_per_day_sunday INT,

    exercise_per_hour_00 INT,
    exercise_per_hour_01 INT,
    exercise_per_hour_02 INT,
    exercise_per_hour_03 INT,
    exercise_per_hour_04 INT,
    exercise_per_hour_05 INT,
    exercise_per_hour_06 INT,
    exercise_per_hour_07 INT,
    exercise_per_hour_08 INT,
    exercise_per_hour_09 INT,
    exercise_per_hour_10 INT,
    exercise_per_hour_11 INT,
    exercise_per_hour_12 INT,
    exercise_per_hour_13 INT,
    exercise_per_hour_14 INT,
    exercise_per_hour_15 INT,
    exercise_per_hour_16 INT,
    exercise_per_hour_17 INT,
    exercise_per_hour_18 INT,
    exercise_per_hour_19 INT,
    exercise_per_hour_20 INT,
    exercise_per_hour_21 INT,
    exercise_per_hour_22 INT,
    exercise_per_hour_23 INT,

    exercises_gender_m INT,
    exercises_gender_f INT,

    exercises_age_0_17 INT,
    exercises_age_18_24 INT,
    exercises_age_25_34 INT,
    exercises_age_35_44 INT,
    exercises_age_45_54 INT,
    exercises_age_55_64 INT,
    exercises_age_65_plus INT,

    exercises_bmi_0_18_5 INT,
    exercises_bmi_18_5_25 INT,
    exercises_bmi_25_30 INT,
    exercises_bmi_30_35 INT,
    exercises_bmi_35_plus INT,

    movement_minutes INT,
    gender_group VARCHAR(50),
    tags VARCHAR(255)
);

CREATE TABLE WORKSHOPS (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAAM VARCHAR(255),
    ZICHTBAAR_IN_APP BOOLEAN,
    START_DATUM DATE,
    EIND_DATUM DATE,
    TIJDSTIP DATE,
    DOELGROEP VARCHAR(255),
    PRIJS DECIMAL(10,2),
    BETALEN_VIA VARCHAR(255),
    ORGANISATOR VARCHAR(255),
    CONTACT_PERSOON VARCHAR(255),
    INSCHRIJVEN_VERPLICHT BOOLEAN,
    INSCHRIJVEN_VIA VARCHAR(255),
    COMMUNICATIE_VIA_APP BOOLEAN,
    LOCATION_BENCH_ID BIGINT,
    FOREIGN KEY (LOCATION_BENCH_ID) REFERENCES LOCATIONS(bench_id)
);

CREATE TABLE PHASE (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255)
);

CREATE TABLE CHECKLIST (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(550),
    INFO TEXT,
    PHASE_ID BIGINT,
    FOREIGN KEY (PHASE_ID) REFERENCES PHASE(ID)
);

CREATE TABLE location_checklist_completion (
    bench_id BIGINT NOT NULL,
    checklist_id BIGINT NOT NULL,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bench_id, checklist_id),
    FOREIGN KEY (bench_id) REFERENCES LOCATIONS(bench_id) ON DELETE CASCADE,
    FOREIGN KEY (checklist_id) REFERENCES CHECKLIST(ID) ON DELETE CASCADE
);

CREATE TABLE STAKEHOLDERS (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    ORGANISATION VARCHAR(255),
    CONTACTPERSON VARCHAR(255),
    EMAIL VARCHAR(255),
    PHONENUMBER VARCHAR(255),
    ROLE VARCHAR(255)
);

CREATE TABLE STAKEHOLDER_LOCATION (
    STAKEHOLDER_ID BIGINT,
    LOCATION_ID BIGINT,
    PRIMARY KEY (STAKEHOLDER_ID, LOCATION_ID),
    FOREIGN KEY (STAKEHOLDER_ID) REFERENCES STAKEHOLDERS(ID),
    FOREIGN KEY (LOCATION_ID) REFERENCES LOCATIONS(BENCH_ID)
);

CREATE TABLE FAQ (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    QUESTION VARCHAR(1000),
    ANSWER VARCHAR(2000)
);

CREATE TABLE NOTES (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    bench_id BIGINT NOT NULL,
    CONTENT TEXT,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bench_id) REFERENCES LOCATIONS(bench_id) ON DELETE CASCADE
);

CREATE TABLE PROFILE_EXERCISES (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    duration INT,
    started_at DATETIME(6),
    started_at_date DATE,
    started_at_day_of_week INT,
    started_at_hour INT,
    finished_at DATETIME(6),
    profile_id BIGINT NOT NULL,
    profile_age INT,
    profile_age_category VARCHAR(50),
    profile_gender VARCHAR(20),
    profile_country CHAR(2),
    profile_bmi DECIMAL(4, 1),
    profile_bmi_category VARCHAR(50),
    exercise_id BIGINT NOT NULL,
    exercise_name VARCHAR(255),
    exercise_types VARCHAR(255),
    exercise_level VARCHAR(50),
    bench_id BIGINT,
    bench_name VARCHAR(255),
    bench_owner VARCHAR(255),
    bench_city VARCHAR(255),
    bench_street VARCHAR(255),
    bench_country CHAR(2),
    bench_size VARCHAR(50),
    bench_type VARCHAR(50),
    bench_connected_routes INT,
    bench_tags VARCHAR(255),
    bench_hidden BOOLEAN,
    bench_accessibility VARCHAR(100),
    program_id BIGINT,
    is_program BOOLEAN,
    FOREIGN KEY (profile_id) REFERENCES PROFILES(ID),
    FOREIGN KEY (exercise_id) REFERENCES EXERCISES(exercise_id)
);

CREATE TABLE auth_tokens (
    TOKEN VARCHAR(255) PRIMARY KEY,
    EXPIRY_DATE TIMESTAMP
);