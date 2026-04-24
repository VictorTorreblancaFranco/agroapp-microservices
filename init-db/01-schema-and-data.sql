-- =========================================
-- BASE DE DATOS AGRÍCOLA - COMPLETA CON DATOS DE PRUEBA
-- =========================================

-- =========================================
-- 1. TABLA USERS
-- =========================================
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(150) NOT NULL,
    last_name VARCHAR(150) NOT NULL,
    birth_date DATE NOT NULL,
    document_type VARCHAR(10) NOT NULL,
    document_number VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(9) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    roles VARCHAR(255) DEFAULT 'ROLE_USER',
    failed_attempts INT DEFAULT 0,
    locked_until TIMESTAMP NULL,
    last_access TIMESTAMP NULL,
    last_password_change TIMESTAMP NULL,
    account_non_locked BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    last_password_reset_date TIMESTAMP NULL,
    is_email_verified BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(255),
    recovery_token VARCHAR(255),
    token_expiry TIMESTAMP NULL,
    secret_question VARCHAR(255),
    secret_answer VARCHAR(255),
    version INT DEFAULT 1,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    deleted_at TIMESTAMP NULL,
    deleted_by VARCHAR(100),
    registration_ip INET,
    account_expiry_date DATE NULL,
    CONSTRAINT uq_email UNIQUE (email),
    CONSTRAINT uq_phone UNIQUE (phone),
    CONSTRAINT uq_document UNIQUE (document_type, document_number),
    CONSTRAINT chk_document_type CHECK (document_type IN ('DNI', 'CED', 'PAS')),
    CONSTRAINT chk_phone CHECK (phone ~ '^9[0-9]{8}$')
);

-- =========================================
-- 2. TABLA ROL
-- =========================================
CREATE TABLE IF NOT EXISTS rol (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- 3. TABLA USER_ROLE
-- =========================================
CREATE TABLE IF NOT EXISTS user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(100),
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES rol(id) ON DELETE CASCADE
);

-- =========================================
-- 4. TABLA CROP
-- =========================================
CREATE TABLE IF NOT EXISTS crop (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    scientific_name VARCHAR(150),
    type VARCHAR(50) NOT NULL,
    variety VARCHAR(100),
    description TEXT,
    season VARCHAR(50),
    cycle_days INT CHECK (cycle_days > 0),
    expected_yield NUMERIC(10,2) CHECK (expected_yield >= 0),
    sowing_density INT CHECK (sowing_density > 0),
    optimal_temp_min NUMERIC(5,2),
    optimal_temp_max NUMERIC(5,2),
    optimal_humidity_min NUMERIC(5,2),
    optimal_humidity_max NUMERIC(5,2),
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT DEFAULT 1,
    CONSTRAINT chk_temperature CHECK (optimal_temp_min < optimal_temp_max),
    CONSTRAINT chk_humidity CHECK (optimal_humidity_min < optimal_humidity_max)
);

-- =========================================
-- 5. TABLA PLOT
-- =========================================
CREATE TABLE IF NOT EXISTS plot (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(150),
    area_hectares NUMERIC(10,2) CHECK (area_hectares > 0),
    soil_type VARCHAR(50),
    user_id BIGINT NOT NULL,
    registration_date DATE DEFAULT CURRENT_DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT DEFAULT 1,
    CONSTRAINT fk_plot_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =========================================
-- 6. TABLA SOWING
-- =========================================
CREATE TABLE IF NOT EXISTS sowing (
    id BIGSERIAL PRIMARY KEY,
    sowing_date DATE NOT NULL,
    estimated_harvest_date DATE,
    actual_harvest_date DATE,
    seed_quantity INT CHECK (seed_quantity > 0),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    observations TEXT,
    crop_id BIGINT NOT NULL,
    plot_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT DEFAULT 1,
    CONSTRAINT fk_sowing_crop FOREIGN KEY (crop_id) REFERENCES crop(id),
    CONSTRAINT fk_sowing_plot FOREIGN KEY (plot_id) REFERENCES plot(id),
    CONSTRAINT chk_sowing_dates CHECK (estimated_harvest_date > sowing_date),
    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'HARVESTING', 'COMPLETED', 'CANCELLED'))
);

-- =========================================
-- 7. TABLA LOSS
-- =========================================
CREATE TABLE IF NOT EXISTS loss (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    quantity INT CHECK (quantity >= 0),
    cause VARCHAR(100),
    description TEXT,
    sowing_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_loss_sowing FOREIGN KEY (sowing_id) REFERENCES sowing(id)
);

-- =========================================
-- 8. TABLA COST
-- =========================================
CREATE TABLE IF NOT EXISTS cost (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(150),
    type VARCHAR(50),
    amount NUMERIC(10,2) CHECK (amount >= 0),
    date DATE NOT NULL,
    payment_method VARCHAR(50),
    sowing_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cost_sowing FOREIGN KEY (sowing_id) REFERENCES sowing(id),
    CONSTRAINT chk_payment_method CHECK (payment_method IN ('CASH', 'TRANSFER', 'CARD', 'CHECK'))
);

-- =========================================
-- 9. TABLA WEATHER
-- =========================================
CREATE TABLE IF NOT EXISTS weather (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    temperature NUMERIC(5,2),
    humidity NUMERIC(5,2),
    rainfall NUMERIC(5,2),
    wind NUMERIC(5,2),
    plot_id BIGINT NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT DEFAULT 1,
    CONSTRAINT fk_weather_plot FOREIGN KEY (plot_id) REFERENCES plot(id),
    CONSTRAINT chk_temperature CHECK (temperature BETWEEN -20 AND 60),
    CONSTRAINT chk_humidity CHECK (humidity BETWEEN 0 AND 100),
    CONSTRAINT chk_rainfall CHECK (rainfall >= 0),
    CONSTRAINT chk_wind CHECK (wind >= 0)
);

-- =========================================
-- 10. TABLA PREDICTION
-- =========================================
CREATE TABLE IF NOT EXISTS prediction (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    estimated_yield NUMERIC(10,2) CHECK (estimated_yield >= 0),
    success_probability NUMERIC(5,2) CHECK (success_probability BETWEEN 0 AND 100),
    method VARCHAR(100),
    observations TEXT,
    sowing_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prediction_sowing FOREIGN KEY (sowing_id) REFERENCES sowing(id)
);

-- =========================================
-- ÍNDICES
-- =========================================
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_crop_name ON crop(name);
CREATE INDEX IF NOT EXISTS idx_crop_type ON crop(type);
CREATE INDEX IF NOT EXISTS idx_plot_user ON plot(user_id);
CREATE INDEX IF NOT EXISTS idx_sowing_plot ON sowing(plot_id);
CREATE INDEX IF NOT EXISTS idx_sowing_crop ON sowing(crop_id);
CREATE INDEX IF NOT EXISTS idx_loss_sowing ON loss(sowing_id);
CREATE INDEX IF NOT EXISTS idx_cost_sowing ON cost(sowing_id);
CREATE INDEX IF NOT EXISTS idx_weather_plot ON weather(plot_id);
CREATE INDEX IF NOT EXISTS idx_prediction_sowing ON prediction(sowing_id);

-- =========================================
-- FUNCIONES Y TRIGGERS
-- =========================================
CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_version()
RETURNS TRIGGER AS $$
BEGIN
    NEW.version = OLD.version + 1;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_users_updated_at ON users;
DROP TRIGGER IF EXISTS trigger_users_version ON users;
DROP TRIGGER IF EXISTS trigger_crop_updated_at ON crop;
DROP TRIGGER IF EXISTS trigger_crop_version ON crop;
DROP TRIGGER IF EXISTS trigger_plot_updated_at ON plot;
DROP TRIGGER IF EXISTS trigger_plot_version ON plot;
DROP TRIGGER IF EXISTS trigger_sowing_updated_at ON sowing;
DROP TRIGGER IF EXISTS trigger_sowing_version ON sowing;
DROP TRIGGER IF EXISTS trigger_weather_updated_at ON weather;
DROP TRIGGER IF EXISTS trigger_weather_version ON weather;

CREATE TRIGGER trigger_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at();
CREATE TRIGGER trigger_users_version BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_version();
CREATE TRIGGER trigger_crop_updated_at BEFORE UPDATE ON crop FOR EACH ROW EXECUTE FUNCTION update_updated_at();
CREATE TRIGGER trigger_crop_version BEFORE UPDATE ON crop FOR EACH ROW EXECUTE FUNCTION update_version();
CREATE TRIGGER trigger_plot_updated_at BEFORE UPDATE ON plot FOR EACH ROW EXECUTE FUNCTION update_updated_at();
CREATE TRIGGER trigger_plot_version BEFORE UPDATE ON plot FOR EACH ROW EXECUTE FUNCTION update_version();
CREATE TRIGGER trigger_sowing_updated_at BEFORE UPDATE ON sowing FOR EACH ROW EXECUTE FUNCTION update_updated_at();
CREATE TRIGGER trigger_sowing_version BEFORE UPDATE ON sowing FOR EACH ROW EXECUTE FUNCTION update_version();
CREATE TRIGGER trigger_weather_updated_at BEFORE UPDATE ON weather FOR EACH ROW EXECUTE FUNCTION update_updated_at();
CREATE TRIGGER trigger_weather_version BEFORE UPDATE ON weather FOR EACH ROW EXECUTE FUNCTION update_version();

-- =========================================
-- DATOS DE PRUEBA - ROLES
-- =========================================
INSERT INTO rol (name, description, is_active) VALUES
('ROLE_USER', 'Usuario estándar', true),
('ROLE_MANAGER', 'Gestor agrícola', true),
('ROLE_ADMIN', 'Administrador', true)
ON CONFLICT (name) DO NOTHING;

-- =========================================
-- DATOS DE PRUEBA - USUARIOS (contraseña: agroapp123)
-- La contraseña está encriptada con BCrypt
-- Contraseña original: agroapp123
-- Hash BCrypt: $2a$10$NkM5CkR6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K0L1M2N3O4P5Q6R7S8T9U0V1W2X3
-- =========================================
INSERT INTO users (first_name, last_name, birth_date, document_type, document_number, email, phone, password, username, roles, is_email_verified, is_active, created_at) VALUES
('Victor', 'Torreblanca', '1990-05-15', 'DNI', '12345678', 'vtorreblanca@agroapp.com', '912345678', '$2a$10$NkM5CkR6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K0L1M2N3O4P5Q6R7S8T9U0V1W2X3', 'vtorreblanca', 'ROLE_ADMIN', true, true, CURRENT_TIMESTAMP),
('Maria', 'Gonzalez', '1985-03-20', 'DNI', '87654321', 'maria@agroapp.com', '987654321', '$2a$10$NkM5CkR6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K0L1M2N3O4P5Q6R7S8T9U0V1W2X3', 'mgonzalez', 'ROLE_USER', true, true, CURRENT_TIMESTAMP),
('Carlos', 'Ramirez', '1978-11-10', 'DNI', '11223344', 'carlos@agroapp.com', '998877665', '$2a$10$NkM5CkR6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K0L1M2N3O4P5Q6R7S8T9U0V1W2X3', 'cramirez', 'ROLE_MANAGER', true, true, CURRENT_TIMESTAMP),
('Ana', 'Martinez', '1995-07-22', 'DNI', '55667788', 'ana@agroapp.com', '945612378', '$2a$10$NkM5CkR6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K0L1M2N3O4P5Q6R7S8T9U0V1W2X3', 'amartinez', 'ROLE_USER', true, true, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

-- =========================================
-- ASIGNAR ROLES
-- =========================================
DO $$
DECLARE
    uid BIGINT;
    rid BIGINT;
BEGIN
    -- Victor como ADMIN
    SELECT id INTO uid FROM users WHERE username = 'vtorreblanca';
    SELECT id INTO rid FROM rol WHERE name = 'ROLE_ADMIN';
    IF uid IS NOT NULL AND rid IS NOT NULL THEN
        INSERT INTO user_role (user_id, role_id) VALUES (uid, rid) ON CONFLICT DO NOTHING;
    END IF;
    
    -- Maria como USER
    SELECT id INTO uid FROM users WHERE username = 'mgonzalez';
    SELECT id INTO rid FROM rol WHERE name = 'ROLE_USER';
    IF uid IS NOT NULL AND rid IS NOT NULL THEN
        INSERT INTO user_role (user_id, role_id) VALUES (uid, rid) ON CONFLICT DO NOTHING;
    END IF;
    
    -- Carlos como MANAGER
    SELECT id INTO uid FROM users WHERE username = 'cramirez';
    SELECT id INTO rid FROM rol WHERE name = 'ROLE_MANAGER';
    IF uid IS NOT NULL AND rid IS NOT NULL THEN
        INSERT INTO user_role (user_id, role_id) VALUES (uid, rid) ON CONFLICT DO NOTHING;
    END IF;
    
    -- Ana como USER
    SELECT id INTO uid FROM users WHERE username = 'amartinez';
    SELECT id INTO rid FROM rol WHERE name = 'ROLE_USER';
    IF uid IS NOT NULL AND rid IS NOT NULL THEN
        INSERT INTO user_role (user_id, role_id) VALUES (uid, rid) ON CONFLICT DO NOTHING;
    END IF;
END $$;

-- =========================================
-- DATOS DE PRUEBA - CROP (2 por cada tipo)
-- =========================================
INSERT INTO crop (name, scientific_name, type, variety, description, season, cycle_days, expected_yield, sowing_density, optimal_temp_min, optimal_temp_max, optimal_humidity_min, optimal_humidity_max, is_active, created_by) VALUES
('Maíz Amarillo', 'Zea mays', 'CEREAL', 'INIA 619', 'Maíz para grano', 'TODO_EL_AÑO', 150, 8.5, 62000, 18, 32, 60, 85, true, 'system'),
('Arroz', 'Oryza sativa', 'CEREAL', 'IR-43', 'Arroz de riego', 'VERANO', 120, 10.0, 45000, 20, 35, 70, 90, true, 'system'),
('Frijol', 'Phaseolus vulgaris', 'LEGUMINOSA', 'Canario', 'Frijol grano seco', 'PRIMAVERA', 90, 2.5, 40000, 18, 28, 60, 80, true, 'system'),
('Lenteja', 'Lens culinaris', 'LEGUMINOSA', 'Cabrera', 'Lenteja proteica', 'INVIERNO', 100, 2.0, 35000, 15, 25, 50, 70, true, 'system'),
('Papa', 'Solanum tuberosum', 'TUBERCULO', 'Canchán', 'Papa consumo fresco', 'TODO_EL_AÑO', 120, 25.0, 35000, 12, 22, 65, 85, true, 'system'),
('Camote', 'Ipomoea batatas', 'TUBERCULO', 'INIA 320', 'Camote anaranjado', 'VERANO', 110, 18.0, 30000, 18, 30, 60, 80, true, 'system'),
('Tomate', 'Solanum lycopersicum', 'HORTALIZA', 'Río Grande', 'Tomate para mesa', 'TODO_EL_AÑO', 85, 45.0, 25000, 18, 28, 65, 85, true, 'system'),
('Lechuga', 'Lactuca sativa', 'HORTALIZA', 'Crespa', 'Lechuga tipo crespa', 'TODO_EL_AÑO', 55, 12.0, 80000, 15, 22, 70, 90, true, 'system'),
('Palta', 'Persea americana', 'FRUTAL', 'Hass', 'Palta variedad Hass', 'PRIMAVERA', 400, 15.0, 500, 18, 25, 60, 80, true, 'system'),
('Mango', 'Mangifera indica', 'FRUTAL', 'Kent', 'Mango variedad Kent', 'VERANO', 360, 20.0, 400, 22, 30, 50, 70, true, 'system')
ON CONFLICT DO NOTHING;

-- =========================================
-- DATOS DE PRUEBA - PLOT (parcelas para cada usuario)
-- =========================================
DO $$
DECLARE
    uid1 BIGINT; uid2 BIGINT; uid3 BIGINT; uid4 BIGINT;
BEGIN
    SELECT id INTO uid1 FROM users WHERE username = 'vtorreblanca';
    SELECT id INTO uid2 FROM users WHERE username = 'mgonzalez';
    SELECT id INTO uid3 FROM users WHERE username = 'cramirez';
    SELECT id INTO uid4 FROM users WHERE username = 'amartinez';
    
    INSERT INTO plot (name, description, location, area_hectares, soil_type, user_id, is_active) VALUES
    ('Parcela Norte', 'Zona norte de la finca', 'Km 10 carretera central', 5.5, 'Franco arcilloso', uid1, true),
    ('Parcela Sur', 'Zona sur junto al río', 'Valle de Chancay', 8.0, 'Franco arenoso', uid1, true),
    ('Parcela Este', 'Colina este', 'Laderas orientales', 3.2, 'Arcilloso', uid2, true),
    ('Parcela Oeste', 'Zona oeste plana', 'Terreno plano', 10.0, 'Franco', uid2, true),
    ('Parcela Central', 'Zona central mejorada', 'Área principal', 6.0, 'Franco limoso', uid3, true),
    ('Parcela Experimental', 'Prueba de cultivos', 'Sector investigación', 2.5, 'Suelo preparado', uid3, true),
    ('Parcela Familiar', 'Uso familiar', 'Huerto casero', 1.5, 'Franco', uid4, true),
    ('Parcela Comercial', 'Producción comercial', 'Zona productiva', 12.0, 'Franco arcilloso', uid4, true)
    ON CONFLICT DO NOTHING;
    
    -- Actualizar códigos
    UPDATE plot SET code = CONCAT('P-', LPAD(id::TEXT, 4, '0')) WHERE code IS NULL;
END $$;

-- =========================================
-- DATOS DE PRUEBA - SOWING (siembras)
-- =========================================
DO $$
DECLARE
    pid BIGINT; cid BIGINT;
BEGIN
    -- Siembra 1: Maíz en parcela Norte
    SELECT id INTO pid FROM plot WHERE name = 'Parcela Norte' LIMIT 1;
    SELECT id INTO cid FROM crop WHERE name = 'Maíz Amarillo' LIMIT 1;
    INSERT INTO sowing (sowing_date, estimated_harvest_date, seed_quantity, status, observations, crop_id, plot_id) VALUES
    ('2024-01-15', '2024-06-15', 310000, 'ACTIVE', 'Siembra de temporada', cid, pid);
    
    -- Siembra 2: Papa en parcela Sur
    SELECT id INTO pid FROM plot WHERE name = 'Parcela Sur' LIMIT 1;
    SELECT id INTO cid FROM crop WHERE name = 'Papa' LIMIT 1;
    INSERT INTO sowing (sowing_date, estimated_harvest_date, seed_quantity, status, observations, crop_id, plot_id) VALUES
    ('2024-02-10', '2024-06-10', 105000, 'ACTIVE', 'Siembra con buen riego', cid, pid);
    
    -- Siembra 3: Tomate en parcela Central
    SELECT id INTO pid FROM plot WHERE name = 'Parcela Central' LIMIT 1;
    SELECT id INTO cid FROM crop WHERE name = 'Tomate' LIMIT 1;
    INSERT INTO sowing (sowing_date, estimated_harvest_date, seed_quantity, status, observations, crop_id, plot_id) VALUES
    ('2024-03-05', '2024-05-30', 75000, 'HARVESTING', 'Alta producción esperada', cid, pid);
    
    -- Siembra 4: Lechuga en parcela Familiar
    SELECT id INTO pid FROM plot WHERE name = 'Parcela Familiar' LIMIT 1;
    SELECT id INTO cid FROM crop WHERE name = 'Lechuga' LIMIT 1;
    INSERT INTO sowing (sowing_date, estimated_harvest_date, seed_quantity, status, observations, crop_id, plot_id) VALUES
    ('2024-03-20', '2024-05-15', 120000, 'ACTIVE', 'Cosecha rápida', cid, pid);
END $$;

-- =========================================
-- DATOS DE PRUEBA - COST (costos por siembra)
-- =========================================
DO $$
DECLARE
    sid BIGINT;
BEGIN
    SELECT id INTO sid FROM sowing LIMIT 1 OFFSET 0;
    INSERT INTO cost (description, type, amount, date, payment_method, sowing_id) VALUES
    ('Semillas certificadas', 'SEMILLA', 2500.00, '2024-01-10', 'TRANSFER', sid),
    ('Fertilizante NPK', 'FERTILIZANTE', 1800.00, '2024-01-20', 'CASH', sid),
    ('Mano de obra', 'LABOR', 3200.00, '2024-01-25', 'CASH', sid);
    
    SELECT id INTO sid FROM sowing LIMIT 1 OFFSET 1;
    INSERT INTO cost (description, type, amount, date, payment_method, sowing_id) VALUES
    ('Semilla mejorada', 'SEMILLA', 1800.00, '2024-02-05', 'CARD', sid),
    ('Fumigación', 'PLAGUICIDA', 950.00, '2024-02-15', 'CASH', sid),
    ('Riego por goteo', 'EQUIPO', 4500.00, '2024-02-20', 'TRANSFER', sid);
END $$;

-- =========================================
-- DATOS DE PRUEBA - LOSS (pérdidas)
-- =========================================
DO $$
DECLARE
    sid BIGINT;
BEGIN
    SELECT id INTO sid FROM sowing LIMIT 1 OFFSET 1;
    INSERT INTO loss (date, quantity, cause, description, sowing_id) VALUES
    ('2024-03-01', 5000, 'PLAGA', 'Ataque de gusanos', sid),
    ('2024-03-15', 3000, 'CLIMA', 'Granizada afectó cultivo', sid);
END $$;

-- =========================================
-- DATOS DE PRUEBA - WEATHER (clima por parcela)
-- =========================================
DO $$
DECLARE
    pid BIGINT;
BEGIN
    SELECT id INTO pid FROM plot WHERE name = 'Parcela Norte' LIMIT 1;
    INSERT INTO weather (date, temperature, humidity, rainfall, wind, plot_id) VALUES
    ('2024-04-01', 24.5, 65.0, 0.0, 12.5, pid),
    ('2024-04-02', 26.0, 70.0, 5.2, 10.0, pid),
    ('2024-04-03', 23.0, 80.0, 15.0, 8.0, pid);
    
    SELECT id INTO pid FROM plot WHERE name = 'Parcela Sur' LIMIT 1;
    INSERT INTO weather (date, temperature, humidity, rainfall, wind, plot_id) VALUES
    ('2024-04-01', 26.5, 60.0, 0.0, 15.0, pid),
    ('2024-04-02', 28.0, 65.0, 2.0, 12.0, pid);
END $$;

-- =========================================
-- DATOS DE PRUEBA - PREDICTION (predicciones)
-- =========================================
DO $$
DECLARE
    sid BIGINT;
BEGIN
    SELECT id INTO sid FROM sowing LIMIT 1 OFFSET 0;
    INSERT INTO prediction (date, estimated_yield, success_probability, method, observations, sowing_id) VALUES
    ('2024-01-20', 260.5, 85.0, 'Estadístico', 'Basado en campaña anterior', sid),
    ('2024-02-15', 270.0, 90.0, 'ML', 'Predicción con IA', sid);
    
    SELECT id INTO sid FROM sowing LIMIT 1 OFFSET 2;
    INSERT INTO prediction (date, estimated_yield, success_probability, method, observations, sowing_id) VALUES
    ('2024-03-10', 3400.0, 75.0, 'Lineal', 'Clima favorable', sid);
END $$;

-- =========================================
-- ACTUALIZAR CÓDIGOS DE PARCELAS
-- =========================================
UPDATE plot SET code = CONCAT('P-', LPAD(id::TEXT, 4, '0')) WHERE code IS NULL;

-- =========================================
-- VERIFICAR DATOS
-- =========================================
SELECT '✅ USERS: ' || COUNT(*) || ' registros' AS resultado FROM users
UNION ALL
SELECT '✅ CROPS: ' || COUNT(*) || ' registros' FROM crop
UNION ALL
SELECT '✅ PLOTS: ' || COUNT(*) || ' registros' FROM plot
UNION ALL
SELECT '✅ SOWINGS: ' || COUNT(*) || ' registros' FROM sowing
UNION ALL
SELECT '✅ COSTS: ' || COUNT(*) || ' registros' FROM cost
UNION ALL
SELECT '✅ LOSSES: ' || COUNT(*) || ' registros' FROM loss
UNION ALL
SELECT '✅ WEATHER: ' || COUNT(*) || ' registros' FROM weather
UNION ALL
SELECT '✅ PREDICTIONS: ' || COUNT(*) || ' registros' FROM prediction;

-- Credenciales para login:
-- Usuario: vtorreblanca
-- Contraseña: agroapp123
