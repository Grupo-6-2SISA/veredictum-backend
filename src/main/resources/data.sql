-- Clientes base (sem indicador)

INSERT INTO cliente (
    id_cliente, fk_indicador, nome, email, rg, cpf, cnpj, data_nascimento, data_inicio,
    endereco, cep, descricao, inscricao_estadual, is_pro_bono, is_ativo, is_juridico
)
VALUES
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


-- Tipos de lembrete
INSERT INTO tipo_lembrete (id_tipo_lembrete, tipo) VALUES
                                                       (DEFAULT, 'Atendimento'),
                                                       (DEFAULT, 'Nota Fiscal'),
                                                       (DEFAULT, 'Conta'),
                                                       (DEFAULT, 'Aniversario');

-- Usuários administradores
INSERT INTO usuario (id_usuario, nome, email, senha, is_ativo, is_adm, fk_adm) VALUES
                                                                                   (DEFAULT, 'Lismara Ribeiro Matos', 'lismara.matos@email.com', 'senha123', TRUE, TRUE, NULL),
                                                                                   (DEFAULT, 'Orlando Alves de Matos', 'orlando.matos@email.com', 'senha123', TRUE, TRUE, NULL);

-- Usuários ativos não administradores
INSERT INTO usuario (id_usuario, nome, email, senha, is_ativo, is_adm, fk_adm) VALUES
                                                                                   (DEFAULT, 'Ana Clara Silva', 'ana.silva@email.com', 'senha123', TRUE, FALSE, 1),
                                                                                   (DEFAULT, 'Carlos Eduardo Santos', 'carlos.santos@email.com', 'senha123', TRUE, FALSE, 2);

-- Usuários inativos
INSERT INTO usuario (id_usuario, nome, email, senha, is_ativo, is_adm, fk_adm) VALUES
                                                                                   (DEFAULT, 'Fernanda Oliveira', 'fernanda.oliveira@email.com', 'senha123', FALSE, FALSE, 1),
                                                                                   (DEFAULT, 'João Pedro Lima', 'joao.lima@email.com', 'senha123', FALSE, TRUE, NULL);

-- Usuários com administrador associado
INSERT INTO usuario (id_usuario, nome, email, senha, is_ativo, is_adm, fk_adm) VALUES
                                                                                   (DEFAULT, 'Mariana Costa', 'mariana.costa@email.com', 'senha123', TRUE, FALSE, 1),
                                                                                   (DEFAULT, 'Rafael Souza', 'rafael.souza@email.com', 'senha123', TRUE, FALSE, 2);

-- Atendimentos (variação de clientes, usuários, status e valores)
-- Para clientes ativos, inativos e com indicador

-- Atendimento 1: Cliente João da Silva (Cliente Regular)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 1, 1, 'Atendimento Jurídico', 150.00, 'Atendimento relacionado a questões jurídicas.', '2023-04-01 09:00:00', '2023-04-01 12:00:00', '2023-04-01 12:30:00', FALSE);

-- Atendimento 2: Empresa X Ltda (Cliente PJ)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 2, 2, 'Consultoria PJ', 500.00, 'Consultoria para expansão de mercado.', '2023-04-10 10:00:00', '2023-04-10 15:00:00', '2023-04-10 18:00:00', TRUE);

-- Atendimento 3: Lucas Pereira (Cliente Pro Bono)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 3, 1, 'Aconselhamento Pro Bono', 0.00, 'Aconselhamento jurídico gratuito.', '2023-05-02 11:00:00', '2023-05-02 13:00:00', '2023-05-02 13:30:00', FALSE);

-- Atendimento 4: Tech Solutions ME (Empresa de Tecnologia)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 4, 2, 'Suporte Técnico', 350.00, 'Suporte para problemas em sistemas de TI.', '2023-06-15 14:00:00', '2023-06-15 16:00:00', '2023-06-15 16:30:00', TRUE);

-- Atendimento 5: Maria Oliveira (Indicada por João)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 5, 1, 'Consultoria Jurídica Maria', 250.00, 'Consultoria jurídica para resolver pendências.', '2024-03-12 09:00:00', '2024-03-12 11:00:00', '2024-03-12 11:30:00', FALSE);

-- Atendimento 6: Serviços Gerais LTDA (Indicada pela Empresa X)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 6, 2, 'Consultoria Empresarial', 600.00, 'Consultoria para melhoria de processos empresariais.', '2024-05-22 13:00:00', '2024-05-22 17:00:00', '2024-05-22 17:30:00', TRUE);

-- Atendimento 7: Fernanda Lima (Indicada por Lucas)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 7, 1, 'Consultoria Jurídica Fernanda', 400.00, 'Consultoria jurídica para questões trabalhistas.', '2023-11-15 09:00:00', '2023-11-15 12:00:00', '2023-11-15 12:30:00', FALSE);

-- Atendimento 8: Carlos Souza (Cliente Inativo)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 8, 1, 'Atendimento Inativo', 200.00, 'Atendimento finalizado e cliente inativo.', '2020-11-10 10:00:00', '2020-11-10 12:00:00', '2020-11-10 12:30:00', TRUE);

-- Atendimento 9: Alpha Corp (Empresa Inativa)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago)
VALUES (DEFAULT, 9, 2, 'Consultoria Inativa', 550.00, 'Consultoria empresarial finalizada e cliente inativo.', '2021-05-05 08:00:00', '2021-05-05 10:00:00', '2021-05-05 10:30:00', FALSE);
