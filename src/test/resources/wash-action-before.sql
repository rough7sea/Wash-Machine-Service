delete from wash_machine;
insert into wash_machine(machine_id, name, status, create_date)
values
    (1, 'machine 1', 'ACTIVE', now()),
    (2, 'machine 2', 'INACTIVE', now())