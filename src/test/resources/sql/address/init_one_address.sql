insert into users (id, first_name, last_name, email, phone, cpf, created_at)
values ('11111111-1111-1111-1111-111111111111', 'Tony', 'Hawk', 'tony.hawk@email.com', '11999990001', '11144477735',
        '2026-07-21T14:39:00');

INSERT INTO addresses (id, street,number,complement,neighborhood,city,state, cep,user_id)
VALUES ('9f1a7b0c-1234-4d89-9e01-abcdef123456',
        'Rua das Oliveiras',
        '123',
        'Apto 402',
        'Centro',
        'Brasília',
        'DF',
        '70800-000',
        '11111111-1111-1111-1111-111111111111'
       );
