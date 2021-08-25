delete from wash_machine;
insert into wash_machine(id, name, status)
values
    ('1e54f1fa-ee01-4e59-bc1f-84f3c0071474', 'machine 1', 'ACTIVE'::machine_status),
    ('8a54fdd5-7f01-4ccd-a163-56af89e919a4', 'machine 2', 'ACTIVE'::machine_status),
    ('284aff4d-f4c3-42e3-bcaf-bc30fee4f1bf', 'machine 3', 'INACTIVE'::machine_status);

INSERT INTO wash_action(action_id, machine_id, start_date, wash_mode, status)
		VALUES ('384aff4d-f4c3-42e3-bcaf-bc30fee4f1b1'::uuid,'1e54f1fa-ee01-4e59-bc1f-84f3c0071474'::uuid, now(),
		'BABY', 'PROCESS'::wash_action_status);

INSERT INTO wash_action(action_id, machine_id, start_date, wash_mode, status)
		VALUES ('d7b2c7ee-5d50-40dd-8993-b099491f37e1'::uuid,'8a54fdd5-7f01-4ccd-a163-56af89e919a4'::uuid, now(),
		'SYNTHETICS', 'STOPPED'::wash_action_status);
