INSERT INTO cliente (
        id_cliente, fk_indicador, nome, email, rg, cpf, cnpj, telefone, data_nascimento, data_inicio,
        logradouro, bairro, localidade, numero, complemento, cep, descricao, inscricao_estadual, is_pro_bono, is_ativo, is_juridico
    )
    VALUES
    (DEFAULT, NULL, 'João da Silva', 'joao.silva@email.com', '1234567890', '12345678909', NULL, '+5511912345678',
     '1990-05-10', '2023-01-01', 'Rua das Laranjeiras', 'Centro', 'São Paulo', '100', 'Apto 101', '12345678',
     'Cliente regular', '123456789', FALSE, TRUE, FALSE),

    (DEFAULT, NULL, 'Empresa X Ltda', 'contato@empresax.com', NULL, NULL, '12345678000199', '+551130010202',
     '2000-01-01', '2022-06-15', 'Av. Paulista', 'Bela Vista', 'São Paulo', '1500', 'Sala 202', '87654321',
     'Cliente PJ padrão', '987654321', TRUE, TRUE, TRUE),

    (DEFAULT, NULL, 'Lucas Pereira', 'lucas.pereira@email.com', '9876543210', '98765432100', NULL, '+5521998765432',
     '1988-12-30', '2023-08-12', 'Rua do Mercado', 'Centro', 'Rio de Janeiro', '300', 'Casa 1', '44556677',
     'Cliente Pro Bono', '555666777', TRUE, TRUE, FALSE),

    (DEFAULT, NULL, 'Tech Solutions ME', 'contato@techsolutions.com', NULL, NULL, '44556677000188', '+553134567890',
     '1995-07-20', '2021-09-01', 'Rua das Flores', 'Savassi', 'Belo Horizonte', '900', 'Andar 3', '33445566',
     'Empresa de tecnologia', '223344556', FALSE, TRUE, TRUE),

    (DEFAULT, 1, 'Maria Oliveira', 'maria.oliveira@email.com', '0987654321', '98765432100', NULL, '+5511998876655',
     '1985-09-25', '2024-03-10', 'Rua do Sol', 'Jardim', 'São Paulo', '200', 'Bloco B', '11223344',
     'Indicada pelo João', '111222333', FALSE, TRUE, FALSE),

    (DEFAULT, 2, 'Serviços Gerais LTDA', 'servicos@gerais.com', NULL, NULL, '11223344000155', '+552125671122',
     '2010-02-01', '2020-05-20', 'Av. Central', 'Centro', 'Rio de Janeiro', '505', 'Sala 5', '77889900',
     'Indicada pela Empresa X', '332211000', FALSE, TRUE, TRUE),

    (DEFAULT, 3, 'Fernanda Lima', 'fernanda.lima@email.com', '1029384756', '19283746500', NULL, '+5541988223344',
     '1992-11-11', '2023-11-01', 'Rua Esperança', 'Centro', 'Curitiba', '101', 'Apto 2', '55667788',
     'Indicada por Lucas', '889900112', TRUE, TRUE, FALSE),

    (DEFAULT, NULL, 'Carlos Souza', 'carlos.souza@email.com', '0192837465', '56473829100', NULL, '+5511911112222',
     '1979-03-03', '2020-10-10', 'Rua Velha', 'Centro', 'São Paulo', '22', 'Casa', '66778899',
     'Cliente inativo', '777888999', FALSE, FALSE, FALSE),

    (DEFAULT, 4, 'Alpha Corp', 'contato@alphacorp.com', NULL, NULL, '55667788000122', '+551140047788',
     '1999-06-06', '2022-01-15', 'Av. Business', 'Centro', 'São Paulo', '700', 'Andar 7', '99887766',
     'Empresa inativa', '444555666', TRUE, FALSE, TRUE),

    (DEFAULT, NULL, 'Bruno Martins', 'bruno.martins@email.com', '2211334455', '32165498700', NULL, '+5511987651212',
        '1987-08-14', '2024-08-01', 'Rua das Acácias', 'Jardim Paulista', 'São Paulo', '350', 'Apto 502', '01415001',
        'Cliente PF ativo', '110220330', FALSE, TRUE, FALSE),

    (DEFAULT, 1, 'InovaTech Ltda', 'contato@inovatech.com', NULL, NULL, '77889966000155', '+551130104455',
     '1998-08-05', '2023-02-11', 'Rua do Progresso', 'Centro', 'São Paulo', '1200', 'Sala 804', '01310930',
     'PF pro bono inativa com indicador', '221144558', TRUE, FALSE, FALSE),

    (DEFAULT, 2, 'Paula Souza', 'paula.souza@email.com', '5566778899', '65432198700', NULL, '+5521988112233',
     '1991-09-03', '2024-09-10', 'Rua das Palmeiras', 'Botafogo', 'Rio de Janeiro', '42', 'Casa', '22250900',
     'Cliente PF pro bono', '990881122', TRUE, TRUE, FALSE),

    (DEFAULT, NULL, 'Global Services SA', 'contato@globalservices.com', NULL, NULL, '88990077000144', '+552125773344',
     '2001-09-19', '2022-04-20', 'Av. Atlântica', 'Copacabana', 'Rio de Janeiro', '800', 'Andar 10', '22041001',
     'Cliente PJ inativo', '220330110', FALSE, FALSE, TRUE),

    (DEFAULT, NULL, 'Prime Logística LTDA', 'contato@primelog.com', NULL, NULL, '99887766000133', '+551935124455',
     '1996-09-07', '2022-09-01', 'Av. das Indústrias', 'Ponte Preta', 'Campinas', '777', 'Galpão 2', '13045670',
     'PJ ativo sem indicador', '778899665', FALSE, TRUE, TRUE),

    (DEFAULT, NULL, 'Renato Alves', 'renato.alves@email.com', '3344556677', '74125896300', NULL, '+5531991234567',
     '1983-10-27', '2021-10-05', 'Rua Minas Gerais', 'Funcionários', 'Belo Horizonte', '75', 'Casa 2', '30130100',
     'Cliente PF inativo', '771122334', FALSE, FALSE, FALSE),

    (DEFAULT, 3, 'Delta Consultoria ME', 'contato@deltaconsult.com', NULL, NULL, '66778899000122', '+553135007788',
     '1997-10-12', '2020-07-18', 'Av. Afonso Pena', 'Centro', 'Belo Horizonte', '1020', 'Sala 305', '30130001',
     'Cliente PJ pro bono', '119977331', TRUE, TRUE, TRUE),

    (DEFAULT, 2, 'Helena Cardoso', 'helena.cardoso@email.com', '2233445566', '35715925840', NULL, '+5562996665544',
     '1989-10-05', '2024-10-02', 'Rua 24 de Outubro', 'Setor Oeste', 'Goiânia', '415', 'Apto 1302', '74125010',
     'PF ativa com indicador', '665544332', FALSE, TRUE, FALSE),

    (DEFAULT, 4, 'Camila Rocha', 'camila.rocha@email.com', '1122446688', '85236974100', NULL, '+5541999887766',
     '1992-11-08', '2024-02-14', 'Rua XV de Novembro', 'Centro', 'Curitiba', '111', 'Apto 901', '80020010',
     'Cliente PF ativo com indicador', '998877665', FALSE, TRUE, FALSE),

    (DEFAULT, 2, 'Omega Soluções LTDA', 'contato@omegasolucoes.com', NULL, NULL, '33445566000111', '+554132001122',
     '1999-11-25', '2023-03-03', 'Rua das Flores', 'Batel', 'Curitiba', '456', 'Conj. 12', '80420020',
     'Cliente PJ pro bono inativo', '667700559', TRUE, FALSE, TRUE),

    (DEFAULT, NULL, 'NeoTech Sistemas EIRELI', 'contato@neotech.com', NULL, NULL, '55660099000188', '+556230907788',
     '1994-11-17', '2023-11-18', 'Av. T-63', 'Setor Bueno', 'Goiânia', '700', 'Sala 402', '74230010',
     'PJ inativa sem indicador', '554433221', FALSE, FALSE, TRUE),

    (DEFAULT, NULL, 'Thiago Ribeiro', 'thiago.ribeiro@email.com', '6677889900', '96385274100', NULL, '+5551998001122',
     '1985-12-15', '2022-12-01', 'Rua da Praia', 'Centro', 'Porto Alegre', '88', 'Casa', '90020100',
     'Cliente PF pro bono', '445566778', TRUE, TRUE, FALSE),

    (DEFAULT, 1, 'Atlas Engenharia Ltda', 'contato@atlasengenharia.com', NULL, NULL, '11220033000177', '+555140097788',
     '2000-12-02', '2021-05-22', 'Av. Ipiranga', 'Azenha', 'Porto Alegre', '1500', 'Sala 1201', '90160090',
     'Cliente PJ ativo', '330022110', FALSE, TRUE, TRUE),

    (DEFAULT, 3, 'Mariana Andrade', 'mariana.andrade@email.com', '9988776655', '25896314700', NULL, '+5585998765432',
     '1993-12-29', '2025-01-11', 'Av. Beira Mar', 'Meireles', 'Fortaleza', '1020', 'Apto 701', '60165012',
     'PF ativa com indicador', '443322110', FALSE, TRUE, FALSE),

    (DEFAULT, 4, 'Quantum Labs LTDA', 'contato@quantumlabs.com', NULL, NULL, '44002233000166', '+558534567890',
     '1997-12-11', '2020-12-05', 'Rua Desembargador Moreira', 'Aldeota', 'Fortaleza', '1800', 'Conj. 1510', '60170010',
     'PJ pro bono ativa com indicador', '221199003', TRUE, TRUE, TRUE);

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
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 1, 1, 'Atendimento Jurídico', 150.00, 'Atendimento relacionado a questões jurídicas.', '2023-04-01 09:00:00', '2023-04-01 12:00:00', '2023-04-01 12:30:00', FALSE, TRUE);

-- Atendimento 2: Empresa X Ltda (Cliente PJ)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 2, 2, 'Consultoria PJ', 500.00, 'Consultoria para expansão de mercado.', '2023-04-10 10:00:00', '2023-04-10 15:00:00', '2023-04-10 18:00:00', TRUE, FALSE);

-- Atendimento 3: Lucas Pereira (Cliente Pro Bono)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 3, 1, 'Aconselhamento Pro Bono', 0.00, 'Aconselhamento jurídico gratuito.', '2023-05-02 11:00:00', '2023-05-02 13:00:00', '2023-05-02 13:30:00', FALSE, TRUE);

-- Atendimento 4: Tech Solutions ME (Empresa de Tecnologia)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 4, 2, 'Suporte Técnico', 350.00, 'Suporte para problemas em sistemas de TI.', '2023-06-15 14:00:00', '2023-06-15 16:00:00', '2023-06-15 16:30:00', TRUE, FALSE);

-- Atendimento 5: Maria Oliveira (Indicada por João)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 5, 1, 'Consultoria Jurídica Maria', 250.00, 'Consultoria jurídica para resolver pendências.', '2024-03-12 09:00:00', '2024-03-12 11:00:00', '2024-03-12 11:30:00', FALSE, TRUE);

-- Atendimento 6: Serviços Gerais LTDA (Indicada pela Empresa X)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 6, 2, 'Consultoria Empresarial', 600.00, 'Consultoria para melhoria de processos empresariais.', '2024-05-22 13:00:00', '2024-05-22 17:00:00', '2024-05-22 17:30:00', TRUE, FALSE);

-- Atendimento 7: Fernanda Lima (Indicada por Lucas)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 7, 1, 'Consultoria Jurídica Fernanda', 400.00, 'Consultoria jurídica para questões trabalhistas.', '2023-11-15 09:00:00', '2023-11-15 12:00:00', '2023-11-15 12:30:00', FALSE, TRUE);

-- Atendimento 8: Carlos Souza (Cliente Inativo)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 8, 1, 'Atendimento Inativo', 200.00, 'Atendimento finalizado e cliente inativo.', '2020-11-10 10:00:00', '2020-11-10 12:00:00', '2020-11-10 12:30:00', TRUE, FALSE);

-- Atendimento 9: Alpha Corp (Empresa Inativa)
INSERT INTO atendimento (id_atendimento, fk_cliente, fk_usuario, etiqueta, valor, descricao, data_inicio, data_fim, data_vencimento, is_pago, should_enviar_email)
VALUES (DEFAULT, 9, 2, 'Consultoria Inativa', 550.00, 'Consultoria empresarial finalizada e cliente inativo.', '2021-05-05 08:00:00', '2021-05-05 10:00:00', '2021-05-05 10:30:00', FALSE, TRUE);



INSERT INTO conta (id_conta, data_criacao, etiqueta, valor, data_vencimento, descricao, is_pago, url_nuvem)
VALUES
(DEFAULT,  '2023-10-01', 'Conta Jurídica João', 200.00, '2023-10-15', 'Pagamento referente ao atendimento jurídico.', FALSE, NULL),
(DEFAULT,  '2023-10-05', 'Conta Consultoria Empresa Y', 600.00, '2023-10-20', 'Pagamento referente à consultoria empresarial.', TRUE, 'comprovante_empresa_y.pdf'),
(DEFAULT,  '2023-10-10', 'Conta Pro Bono Lucas', 0.00, '2023-10-25', 'Conta referente ao atendimento pro bono.', FALSE, NULL),
(DEFAULT, '2025-01-05', 'Conta Janeiro - Serviços Jurídicos', 320.00, '2025-01-20', 'Pagamento de serviços jurídicos de janeiro.', TRUE, 'comprovante_jan_2025.pdf'),
(DEFAULT, '2025-02-03', 'Conta Fevereiro - Consultoria', 780.50, '2025-02-18', 'Consultoria empresarial.', FALSE, NULL),
(DEFAULT, '2025-03-12', 'Conta Março - Assessoria Trabalhista', 410.00, '2025-03-25', 'Assessoria trabalhista mensal.', TRUE, 'comprovante_mar_2025.pdf'),
(DEFAULT, '2025-04-07', 'Conta Abril - Revisão Contratual', 560.00, '2025-04-22', 'Revisão de contratos.', FALSE, NULL),
(DEFAULT, '2025-05-10', 'Conta Maio - Auditoria', 900.00, '2025-05-27', 'Auditoria de compliance.', TRUE, 'comprovante_maio_2025.pdf'),
(DEFAULT, '2025-06-01', 'Conta Junho - Parecer Jurídico', 275.00, '2025-06-15', 'Emissão de parecer jurídico.', TRUE, 'comprovante_jun_2025.pdf'),
(DEFAULT, '2025-07-05', 'Conta Julho - Due Diligence', 1250.00, '2025-07-20', 'Due diligence societária.', FALSE, NULL),
(DEFAULT, '2025-08-11', 'Conta Agosto - Treinamento LGPD', 650.00, '2025-08-25', 'Treinamento de LGPD para equipe.', TRUE, 'comprovante_agosto_2025.pdf'),
(DEFAULT, '2025-09-09', 'Conta Setembro - Suporte Contínuo', 300.00, '2025-09-23', 'Suporte jurídico contínuo.', FALSE, NULL),
(DEFAULT, '2025-10-14', 'Conta Outubro - Mediação', 480.00, '2025-10-28', 'Serviço de mediação.', TRUE, 'comprovante_out_2025.pdf'),
(DEFAULT, '2025-11-06', 'Conta Novembro - Consultoria Tributária', 830.00, '2025-11-21', 'Consultoria tributária.', FALSE, NULL),
(DEFAULT, '2025-12-02', 'Conta Dezembro - Encerramento Anual', 1200.00, '2025-12-20', 'Encerramento jurídico anual.', TRUE, 'comprovante_dez_2025.pdf');

INSERT INTO conta (id_conta, data_criacao, etiqueta, valor, data_vencimento, descricao, is_pago, url_nuvem)
VALUES
(DEFAULT,  '2023-11-01', 'Conta Maria Oliveira - Consulta', 250.00, '2023-11-15', 'Pagamento por consulta jurídica.', FALSE, NULL),
(DEFAULT,  '2023-11-10', 'Conta Serviços Gerais LTDA - Projeto', 750.00, '2023-11-25', 'Pagamento por projeto de consultoria.', TRUE, 'comprovante_servicos_gerais.pdf'),
(DEFAULT,  '2023-12-01', 'Conta Fernanda Lima - Assessoria', 300.00, '2023-12-15', 'Pagamento por assessoria trabalhista.', FALSE, NULL),
(DEFAULT,  '2024-01-10', 'Conta Alpha Corp - Revisão Contratual', 550.00, '2024-01-25', 'Pagamento por revisão de contratos.', TRUE, 'comprovante_alpha_corp.pdf');


-- Notas Fiscais (associadas a clientes)
INSERT INTO nota_fiscal (id_nota_fiscal, fk_cliente, data_criacao, etiqueta, valor, data_vencimento, descricao, url_nuvem, is_emitida) VALUES
(DEFAULT, 1, '2023-04-02', 'NF Atendimento João', 165.00, '2023-04-16', 'Nota fiscal do atendimento jurídico', 'nuvem.com/nf_joao.pdf', TRUE),
(DEFAULT, 2, '2023-04-11', 'NF Consultoria Empresa X', 550.00, '2023-04-26', 'Nota fiscal da consultoria de mercado', 'nuvem.com/nf_empresa_x.pdf', TRUE),
(DEFAULT, 4, '2023-06-16', 'NF Suporte Tech Solutions', 385.00, '2023-06-30', 'Nota fiscal do suporte técnico', 'nuvem.com/nf_tech.pdf', FALSE),
(DEFAULT, 6, '2024-05-23', 'NF Consultoria Serviços Gerais', 660.00, '2024-06-06', 'Nota fiscal da consultoria empresarial', 'nuvem.com/nf_servicos_gerais.pdf', TRUE);

-- Mais Notas Fiscais (associadas a diferentes clientes)
INSERT INTO nota_fiscal (id_nota_fiscal, fk_cliente, data_criacao, etiqueta, valor, data_vencimento, descricao, url_nuvem, is_emitida) VALUES
                                                                                                                                           (DEFAULT, 5, '2023-07-01', 'NF Consulta Maria Oliveira', 275.00, '2023-07-15', 'Nota fiscal da consulta jurídica.', 'nuvem.com/nf_maria.pdf', TRUE),
                                                                                                                                           (DEFAULT, 6, '2023-08-01', 'NF Projeto Serviços Gerais', 825.00, '2023-08-15', 'Nota fiscal do projeto de consultoria.', 'nuvem.com/nf_servicos.pdf', TRUE),
                                                                                                                                           (DEFAULT, 7, '2023-09-01', 'NF Assessoria Fernanda Lima', 330.00, '2023-09-15', 'Nota fiscal da assessoria trabalhista.', NULL, FALSE),
                                                                                                                                           (DEFAULT, 9, '2024-02-01', 'NF Revisão Alpha Corp', 605.00, '2024-02-15', 'Nota fiscal da revisão contratual.', 'nuvem.com/nf_alpha.pdf', TRUE),
                                                                                                                                           (DEFAULT, 1, '2026-01-05', 'NF Janeiro João da Silva', 320.00, '2026-01-20', 'Nota fiscal referente a serviços jurídicos de janeiro.', 'nuvem.com/nf_joao_2026_01.pdf', TRUE),
                                                                                                                                           (DEFAULT, 2, '2026-02-04', 'NF Fevereiro Empresa X', 780.50, '2026-02-18', 'Nota fiscal de consultoria empresarial.', 'nuvem.com/nf_empresax_2026_02.pdf', TRUE),
                                                                                                                                           (DEFAULT, 3, '2026-03-12', 'NF Março Lucas Pereira', 0.00, '2026-03-25', 'Nota fiscal de atendimento pro bono.', NULL, FALSE),
                                                                                                                                           (DEFAULT, 4, '2026-04-07', 'NF Abril Tech Solutions', 560.00, '2026-04-22', 'Nota fiscal de revisão contratual.', 'nuvem.com/nf_tech_2026_04.pdf', FALSE),
                                                                                                                                           (DEFAULT, 5, '2026-05-10', 'NF Maio Maria Oliveira', 650.00, '2026-05-25', 'Nota fiscal de treinamento LGPD.', 'nuvem.com/nf_maria_2026_05.pdf', TRUE),
                                                                                                                                           (DEFAULT, 6, '2026-06-01', 'NF Junho Serviços Gerais', 275.00, '2026-06-15', 'Nota fiscal de parecer jurídico.', NULL, TRUE),
                                                                                                                                           (DEFAULT, 7, '2026-07-05', 'NF Julho Fernanda Lima', 1250.00, '2026-07-20', 'Nota fiscal de due diligence societária.', 'nuvem.com/nf_fernanda_2026_07.pdf', FALSE),
                                                                                                                                           (DEFAULT, 9, '2026-08-11', 'NF Agosto Alpha Corp', 830.00, '2026-08-25', 'Nota fiscal de consultoria tributária.', 'nuvem.com/nf_alpha_2026_08.pdf', TRUE),
                                                                                                                                           (DEFAULT, 10, '2025-01-08', 'NF Janeiro Bruno Martins', 210.00, '2025-01-22', 'Nota fiscal de consulta jurídica.', 'nuvem.com/nf_bruno_2025_01.pdf', TRUE),
(DEFAULT, 11, '2025-02-12', 'NF Fevereiro InovaTech', 780.50, '2025-02-26', 'Nota fiscal de consultoria empresarial.', 'nuvem.com/nf_inovatech_2025_02.pdf', TRUE),
(DEFAULT, 12, '2025-03-05', 'NF Março Sofia Mendes', 0.00, '2025-03-19', 'Nota fiscal de atendimento pro bono.', NULL, FALSE),
(DEFAULT, 13, '2025-04-09', 'NF Abril Paula Souza', 320.00, '2025-04-23', 'Nota fiscal de serviços jurídicos.', 'nuvem.com/nf_paula_2025_04.pdf', TRUE),
(DEFAULT, 14, '2025-05-14', 'NF Maio Global Services', 650.00, '2025-05-28', 'Nota fiscal de treinamento LGPD.', 'nuvem.com/nf_global_2025_05.pdf', TRUE),
(DEFAULT, 15, '2025-06-03', 'NF Junho Prime Logística', 275.00, '2025-06-17', 'Nota fiscal de parecer jurídico.', 'nuvem.com/nf_prime_2025_06.pdf', TRUE),
(DEFAULT, 16, '2025-07-07', 'NF Julho Renato Alves', 1250.00, '2025-07-21', 'Nota fiscal de due diligence societária.', 'nuvem.com/nf_renato_2025_07.pdf', FALSE),
(DEFAULT, 17, '2025-08-11', 'NF Agosto Delta Consultoria', 480.00, '2025-08-25', 'Nota fiscal de mediação.', 'nuvem.com/nf_delta_2025_08.pdf', TRUE),
(DEFAULT, 18, '2025-09-04', 'NF Setembro Helena Cardoso', 300.00, '2025-09-18', 'Nota fiscal de suporte jurídico contínuo.', NULL, FALSE),
(DEFAULT, 19, '2025-10-15', 'NF Outubro Camila Rocha', 410.00, '2025-10-29', 'Nota fiscal de assessoria trabalhista.', 'nuvem.com/nf_camila_2025_10.pdf', TRUE),
(DEFAULT, 20, '2025-11-06', 'NF Novembro Omega Soluções', 830.00, '2025-11-21', 'Nota fiscal de consultoria tributária.', 'nuvem.com/nf_omega_2025_11.pdf', TRUE),
(DEFAULT, 21, '2025-12-02', 'NF Dezembro NeoTech Sistemas', 1200.00, '2025-12-20', 'Nota fiscal de encerramento anual.', 'nuvem.com/nf_neotech_2025_12.pdf', TRUE);

-- Inserts para rotina
INSERT INTO rotina (id_rotina, nome_rotina, hora_inicio, data_inicio, data_fim, rotina_chamada, is_ativo) VALUES
                                                                                                              (DEFAULT, 'Aniversário (Cliente)', '09:00:00', '2025-06-01 00:00:00', '9999-12-31', 'aniversario_cliente', TRUE),
                                                                                                              (DEFAULT, 'Aniversário (Funcionário)', '09:00:00', '2025-06-01 00:00:00', '9999-12-31', 'aniversario_funcionario', TRUE),
                                                                                                              (DEFAULT, 'Atendimento (Cliente)', '09:00:00', '2025-06-01 00:00:00', '9999-12-31', 'atendimento_cliente', TRUE),
                                                                                                              (DEFAULT, 'Atendimento (Funcionario)', '09:00:00', '2025-06-01 00:00:00', '9999-12-31', 'atendimento_funcionario', TRUE),
                                                                                                              (DEFAULT, 'Pesquisa Satisfacao (Cliente)', '09:00:00', '2025-06-01 00:00:00', '9999-12-31', 'pesquisa_satisfacao', TRUE),
                                                                                                              (DEFAULT, 'Aniversario Inicio (Cliente)', '09:00:00', '2025-06-01 00:00:00', '9999-12-31', 'aniversario_inicio_cliente', TRUE),
                                                                                                              (DEFAULT, 'Agradecimento Indicacao (Funcionario)', '09:00:00', '2025-06-01 00:00:00', '9999-12-31', 'agradecimento_indicacao', TRUE);

