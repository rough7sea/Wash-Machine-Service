delete from wash_machine;
insert into wash_machine(id, name, status)
values
    ('8a54fdd5-7f01-4ccd-a163-56af89e919a4', 'machine 1', 'ACTIVE'::machine_status),
    ('284aff4d-f4c3-42e3-bcaf-bc30fee4f1bf', 'machine 2', 'INACTIVE'::machine_status)