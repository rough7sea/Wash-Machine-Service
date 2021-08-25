
create type machine_status as enum ('ACTIVE', 'INACTIVE', '');
create type wash_action_status as enum ('PROCESS', 'STOPPED', 'PAUSED', 'COMPLETED',  '');
create type wash_step as enum ('START', 'SOAK', 'WASH', 'SPIN', 'RINSE', 'SPEED_SPIN', 'FINAL_SPIN', 'COMPLETE', '');

create table if not exists wash_machine(
    id uuid NOT NULL,
    name varchar(2044),
    create_date timestamptz NOT NULL default now(),
    update_date timestamptz null,
    status machine_status NOT NULL DEFAULT 'ACTIVE'::machine_status,
    CONSTRAINT pk_wash_machine_id PRIMARY KEY (id)
    );
create index wash_machine_id_indx on wash_machine (id);


create table if not exists wash_action(
    action_id uuid NOT NULL PRIMARY KEY,
    machine_id uuid not null,
    wash_mode varchar(2044) not null default 'CUSTOM',
    status wash_action_status NOT NULL DEFAULT 'PROCESS'::wash_action_status,
    start_date timestamptz NOT NULL default now(),
    update_date timestamptz null,
    CONSTRAINT fk_wash_machine FOREIGN key (machine_id) REFERENCES wash_machine(id) on delete CASCADE
    );
create index action_id_indx on wash_action (action_id);
create index action_machine_id_indx on wash_action (machine_id);


CREATE TABLE wash_event(
	action_id uuid NOT NULL,
	step wash_step NOT NULL,
	start_date timestamptz NOT NULL default now(),
	CONSTRAINT fk_wash_action FOREIGN key (action_id) REFERENCES wash_action(action_id) on delete CASCADE
);
create index wash_event_action_id_indx on wash_event (action_id);


create table if not exists wash_params(
	action_id uuid not null primary key,
	spin_power int4,
	rinses_count int4,
    temperature int4,
    powder varchar,
    conditioner varchar,
	CONSTRAINT fk_wash_action_id_wash_params FOREIGN key (action_id) REFERENCES wash_action(action_id) on delete CASCADE
);
create index action_id_wash_params_indx on wash_params (action_id);


--CREATE OR REPLACE FUNCTION
--    upsert_wash_machine(w_id uuid, w_name varchar, w_status machine_status) RETURNS VOID AS
--        $$
--        BEGIN
--            LOOP
--                -- first try to update the key
--                UPDATE wash_machine SET
--                name = w_name,
--                update_date = now(),
--                status = w_status
--                WHERE id = w_id;
--                IF found THEN
--                    RETURN;
--                END IF;
--                -- not there, so try to insert the key
--                -- if someone else inserts the same key concurrently,
--                -- we could get a unique-key failure
--                BEGIN
--                    INSERT INTO wash_machine(id, name, create_date, status)
--                    VALUES (w_id, w_name, now(), w_status);
--                    RETURN;
--                EXCEPTION WHEN unique_violation THEN
--                    -- do nothing, and loop to try the UPDATE again
--                END;
--            END LOOP;
--        END;
--        $$
--LANGUAGE plpgsql;
--
--
--CREATE OR REPLACE FUNCTION
--    upsert_wash_action(a_id uuid, m_id uuid, a_mode varchar, a_status wash_action_status) RETURNS VOID AS
--        $$
--        BEGIN
--            LOOP
--                -- first try to update the key
--                UPDATE wash_action SET
--                status = a_status,
--                wash_mode = a_mode,
--                update_date = now()
--                WHERE action_id = a_id and machine_id = m_id;
--                IF found THEN
--                    RETURN;
--                END IF;
--                -- not there, so try to insert the key
--                -- if someone else inserts the same key concurrently,
--                -- we could get a unique-key failure
--                BEGIN
--                    INSERT INTO wash_action(action_id, machine_id, start_date, wash_mode, status)
--                    VALUES (a_id, m_id , now(), a_mode, a_status);
--                    RETURN;
--                END;
--            END LOOP;
--        END;
--        $$
--LANGUAGE plpgsql;