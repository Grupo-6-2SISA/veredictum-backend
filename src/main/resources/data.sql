-- Clientes base (sem indicador)
INSERT INTO cliente (
    id_cliente, fk_indicador, nome, email, rg, cpf, cnpj, data_nascimento, data_inicio,
    endereco, cep, descricao, inscricao_estadual, is_pro_bono, is_ativo, is_juridico
) VALUES
      (DEFAULT, NULL, 'João da Silva', 'joao.silva@email.com', '1234567890', '12345678909', NULL, '1990-05-10', '2023-01-01',
       'Rua das Laranjeiras, 100', '12345678', 'Cliente regular', '123456789', FALSE, TRUE, FALSE),

      (DEFAULT, NULL, 'Empresa X Ltda', 'contato@empresax.com', NULL, NULL, '12345678000199', '2000-01-01', '2022-06-15',
       'Av. Paulista, 1500', '87654321', 'Cliente PJ padrão', '987654321', TRUE, TRUE, TRUE),

      (DEFAULT, NULL, 'Lucas Pereira', 'lucas.pereira@email.com', '9876543210', '98765432100', NULL, '1988-12-30', '2023-08-12',
       'Rua do Mercado, 300', '44556677', 'Cliente Pro Bono', '555666777', TRUE, TRUE, FALSE),

      (DEFAULT, NULL, 'Tech Solutions ME', 'contato@techsolutions.com', NULL, NULL, '44556677000188', '1995-07-20', '2021-09-01',
       'Rua das Flores, 900', '33445566', 'Empresa de tecnologia', '223344556', FALSE, TRUE, TRUE),

-- Clientes com indicador
      (DEFAULT, 1, 'Maria Oliveira', 'maria.oliveira@email.com', '0987654321', '98765432100', NULL, '1985-09-25', '2024-03-10',
       'Rua do Sol, 200', '11223344', 'Indicada pelo João', '111222333', FALSE, TRUE, FALSE),

      (DEFAULT, 2, 'Serviços Gerais LTDA', 'servicos@gerais.com', NULL, NULL, '11223344000155', '2010-02-01', '2020-05-20',
       'Av. Central, 505', '77889900', 'Indicada pela Empresa X', '332211000', FALSE, TRUE, TRUE),

      (DEFAULT, 3, 'Fernanda Lima', 'fernanda.lima@email.com', '1029384756', '19283746500', NULL, '1992-11-11', '2023-11-01',
       'Rua Esperança, 101', '55667788', 'Indicada por Lucas', '889900112', TRUE, TRUE, FALSE),

-- Clientes inativos
      (DEFAULT, NULL, 'Carlos Souza', 'carlos.souza@email.com', '0192837465', '56473829100', NULL, '1979-03-03', '2020-10-10',
       'Rua Velha, 22', '66778899', 'Cliente inativo', '777888999', FALSE, FALSE, FALSE),

      (DEFAULT, 4, 'Alpha Corp', 'contato@alphacorp.com', NULL, NULL, '55667788000122', '1999-06-06', '2022-01-15',
       'Av. Business, 700', '99887766', 'Empresa inativa', '444555666', TRUE, FALSE, TRUE);

-- Status de agendamentos (conta, nota e atendimento)
INSERT INTO status_agendamento (id_status_agendamento, descricao) VALUES
                                                                      (DEFAULT, 'Agendado'),
                                                                      (DEFAULT, 'Concluído'),
                                                                      (DEFAULT, 'Cancelado'),
                                                                      (DEFAULT, 'Atrasado');