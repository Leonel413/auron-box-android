-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: loja_online
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pedido_model_itens`
--

DROP TABLE IF EXISTS `pedido_model_itens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_model_itens` (
  `pedido_model_codigo` bigint NOT NULL,
  `codigo` bigint DEFAULT NULL,
  `descricao_produto` varchar(255) DEFAULT NULL,
  `imagem` varchar(255) DEFAULT NULL,
  `preco_final` double DEFAULT NULL,
  `preco_unitario` double DEFAULT NULL,
  `quantidade` int DEFAULT NULL,
  `cor` varchar(30) DEFAULT NULL,
  `marca` varchar(50) DEFAULT NULL,
  `modelo` varchar(80) DEFAULT NULL,
  KEY `fk_pedido_itens` (`pedido_model_codigo`),
  CONSTRAINT `fk_pedido_itens` FOREIGN KEY (`pedido_model_codigo`) REFERENCES `pedidos` (`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_model_itens`
--

LOCK TABLES `pedido_model_itens` WRITE;
/*!40000 ALTER TABLE `pedido_model_itens` DISABLE KEYS */;
INSERT INTO `pedido_model_itens` VALUES (1,1,'Smartphone top de linha da Samsung com câmera de 200MP','uploads/galaxy-s24.png',5399.99,5399.99,1,'Preto','Samsung','Galaxy S24 Ultra'),(1,54,'DUAS PORTAS','nulo',4002,2001,2,'Preto','Apple','IPHONE 14'),(2,54,'DUAS PORTAS','nulo',2185,2185,1,'Preto','Apple','IPHONE 14'),(2,1,'Smartphone top de linha da Samsung com câmera de 200MP','uploads/galaxy-s24.png',10799.98,5399.99,2,'Preto','Samsung','Galaxy S24 Ultra'),(3,52,'duas portas','nulo',2001,2001,1,'Preto','Apple','Iphone 14'),(3,53,'duas portas','nulo',2001,2001,1,'Preto','Apple','Iphone 14'),(3,2,'Smartphone top de linha da Samsung com câmera de 200MP','uploads/galaxy-s24.png',5399.99,5399.99,1,'Preto','Samsung','Galaxy S24 Ultra'),(3,54,'DUAS PORTAS','nulo',2185,2185,1,'Preto','Apple','IPHONE 14'),(3,1,'Smartphone top de linha da Samsung com câmera de 200MP','uploads/galaxy-s24.png',5399.99,5399.99,1,'Preto','Samsung','Galaxy S24 Ultra'),(52,1,'Smartphone top de linha da Samsung com câmera de 200MP','uploads/Galaxy S24 Ultra.jpeg',5399.991,5399.991,1,'Preto','Samsung','Galaxy S24 Ultra'),(52,302,'Indicado para produtividade, estudos e tarefas profissionais.','uploads/Notebook Dell Inspiron.jpeg',3840,3840,1,'Preto','Dell','Notebook Dell Inspiron'),(102,252,'4 rodas','uploads/iPhone 17 Pro Max.png',6300,6300,1,'Preto','Apple','Iphone 17 Pro MAX'),(102,403,'Monitor Gamer VA 34 \" Curva Antirreflexo LG WQHD UltraGear 34GP63A-B','uploads/Monitor Gamer VA 34 Curva Antirreflexo LG.jpg',1431,1431,1,'Preto','LG','Monitor Gamer VA 34 \" Curva Antirreflexo LG'),(102,413,'JETech Capa para iPhone 17 6,3 Polegadas, Case Protetora de Telefone à Prova de Choque, Anti-Amarelecimento, Parte Traseira Transparente Anti-Riscos','uploads/ETech Capa para iPhone 17.jpg',49.99,49.99,1,'Preto','Apple','ETech Capa para iPhone 17'),(102,412,'GTA V - PlayStation 5','uploads/GTA V.jpg',126.1,126.1,1,'Preto','Outra','GTA V ');
/*!40000 ALTER TABLE `pedido_model_itens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `celular` varchar(15) NOT NULL,
  `codigo_cliente` bigint NOT NULL,
  `cpf_cliente` varchar(15) NOT NULL,
  `data` varchar(25) NOT NULL,
  `email` varchar(80) NOT NULL,
  `endereco` varchar(250) NOT NULL,
  `motivo_rejeicao` varchar(300) DEFAULT NULL,
  `nome_cliente` varchar(80) NOT NULL,
  `numero` bigint NOT NULL,
  `pagamento` varchar(30) NOT NULL,
  `quantidade_itens` int NOT NULL,
  `quantidae_parcelas` int NOT NULL,
  `status` varchar(30) NOT NULL DEFAULT 'Aguardando confirmação',
  `valor` double NOT NULL,
  `valor_parcela` double NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `fk_pedido_usuario` (`codigo_cliente`),
  CONSTRAINT `fk_pedido_usuario` FOREIGN KEY (`codigo_cliente`) REFERENCES `usuarios` (`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (1,'61982511713',1,'07002231189','23/05/2026   19:19:54','ianleonelt06@gmail.com','72620153, DF, Brasília, Recanto das Emas, Quadra 300 Conjunto 52, 726201, casa branca e dourada','ALTO CUSTO','Ian Leonel Teles de Sousa',311686862,'PIX',3,1,'Pedido negado',9401.99,9401.99),(2,'61982511713',1,'07002231189','23/05/2026   23:08:17','ianleonelt06@gmail.com','72620153, DF, Brasília, Recanto das Emas, Quadra 300 Conjunto 52, 726201, casa branca e dourada','Confirmado','Ian Leonel Teles de Sousa',389559632,'PIX',3,1,'Pedido negado',12984.98,12984.98),(3,'61982511713',1,'07002231189','23/05/2026   23:08:33','ianleonelt06@gmail.com','72620153, DF, Brasília, Recanto das Emas, Quadra 300 Conjunto 52, 726201, casa branca e dourada','Confirmado','Ian Leonel Teles de Sousa',371258942,'PIX',5,1,'Pedido negado',16986.98,16986.98),(52,'61982511713',1,'07002231189','24/05/2026   13:16:21','ianleonelt06@gmail.com','72620153, DF, Brasília, Recanto das Emas, Quadra 300 Conjunto 52, 726201, casa branca e dourada',NULL,'Ian Leonel Teles de Sousa',721631233,'PIX',2,1,'Pedido confirmado',9239.991,9239.991),(102,'61982511713',1,'07002231189','25/05/2026   21:02:05','ianleonelt06@gmail.com','72620153, DF, Brasília, Recanto das Emas, Quadra 300 Conjunto 52, 726201, casa branca e dourada',NULL,'Ian Leonel Teles de Sousa',482610525,'PIX',4,1,'Pedido confirmado',7907.09,7907.09);
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_seq`
--

DROP TABLE IF EXISTS `pedidos_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_seq`
--

LOCK TABLES `pedidos_seq` WRITE;
/*!40000 ALTER TABLE `pedidos_seq` DISABLE KEYS */;
INSERT INTO `pedidos_seq` VALUES (201);
/*!40000 ALTER TABLE `pedidos_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produto` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `data_postagem` varchar(50) NOT NULL,
  `nome_produto` varchar(50) NOT NULL,
  `descricao` text,
  `imagem` varchar(255) DEFAULT NULL,
  `categoria` varchar(50) NOT NULL,
  `marca` varchar(50) NOT NULL,
  `modelo` varchar(80) NOT NULL,
  `cor` varchar(30) DEFAULT NULL,
  `garantia` varchar(30) DEFAULT NULL,
  `especificacoes` varchar(1000) DEFAULT NULL,
  `promocao` int NOT NULL DEFAULT '0',
  `quantidade_estoque` int NOT NULL DEFAULT '0',
  `valor_base` double NOT NULL,
  `valor_com_desconto` double NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=416 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto`
--

LOCK TABLES `produto` WRITE;
/*!40000 ALTER TABLE `produto` DISABLE KEYS */;
INSERT INTO `produto` VALUES (1,'24/05/2026   12:23:57','Galaxy S24 Ultra','Smartphone top de linha da Samsung com câmera de 200MP','uploads/Galaxy S24 Ultra.jpeg','Smartphones','Samsung','Galaxy S24 Ultra','Preto','12 meses',NULL,10,55,5999.99,5399.991),(52,'24/05/2026   12:29:28','iphone 14','duas portas','uploads/iphone 14.jpg','Smartphones','Apple','Iphone 14','Preto','3 meses',NULL,13,8,2300,2001),(252,'24/05/2026   12:04:39','Iphone 17 Pro MAX','4 rodas','uploads/iPhone 17 Pro Max.png','Smartphones','Apple','Iphone 17 Pro MAX','Preto','3 meses',NULL,10,2,7000,6300),(302,'24/05/2026   12:45:24','Notebook Dell Inspiron','Indicado para produtividade, estudos e tarefas profissionais.','uploads/Notebook Dell Inspiron.jpeg','Notebooks','Dell','Notebook Dell Inspiron','Preto','12 meses',NULL,4,4,4000,3840),(352,'24/05/2026   15:45:23','Notebook Asus X515 Intel Celeron','Modelo básico para navegação, textos e aulas online.','uploads/Notebook Asus X515 Intel Celeron.jpeg','Notebooks','Asus','Notebook Asus X515 Intel Celeron','Prata','12 meses',NULL,10,10,3700,3330),(353,'24/05/2026   15:49:52','Notebook Acer Nitro V15','Core i5 13240h 16gb Ram Ddr5 512gb SSD Geforce RTX3050 6gb 15.6” Fhd Linux - Preto','uploads/Notebook Acer Nitro V15.jpg','Notebooks','Outra','Notebook Acer Nitro V15','Preto','12 meses',NULL,5,7,5489.1,5214.645),(354,'24/05/2026   15:53:26','Notebook Samsung Galaxy Book Pro','360 Intel® Core i7-1165G7, Windows 10 Home, 16GB, 512 GB SSD, 13.3\'\' Full HD Super AMOLED, Bivolt','uploads/Notebook Samsung Galaxy Book Pro.png','Notebooks','Samsung','Notebook Samsung Galaxy Book Pro','Preto','12 meses',NULL,7,7,3550,3301.5),(355,'24/05/2026   15:56:42','Notebook Lenovo IdeaPad Slim ',' 3 15IRH10 Intel Core i5-13420H 8GB 512GB SSD Windows 11 15.3\" - 83NS0002BR Luna Grey','uploads/Notebook Lenovo IdeaPad Slim.jpg','Notebooks','Lenovo','Notebook Lenovo IdeaPad Slim ','Prata','6 meses',NULL,5,10,4099,3894.05),(356,'24/05/2026   16:01:03','Samsung Galaxy S24','Galaxy AI, Selfie de 12MP, Tela de 6.2\" 1-120Hz, 256GB, 8GB RAM - Cinza','uploads/Smartphone Samsung Galaxy S24.jpg','Smartphones','Samsung','Samsung Galaxy S24','Branco','12 meses',NULL,37,8,4079,2569.77),(357,'24/05/2026   16:04:47','Celular Xiaomi Redmi Note 15 ','256gb 8GB Ram De 6.77\" 108mp 20mp - Preto','uploads/Celular Xiaomi Redmi Note 15.jpeg','Smartphones','Xiaomi','Celular Xiaomi Redmi Note 15 ','Preto','12 meses',NULL,5,5,1899,1804.05),(402,'25/05/2026   19:32:21','Monitor Gamer: Curvo Samsung 24','Monitor Gamer Curvo Samsung 24\", FHD, 144 Hz, HDMI, DP, Freesync, C24RG50FQL','uploads/Monitor Gamer Curvo Samsung 24.png','Monitores','Samsung','Monitor Gamer: Curvo Samsung 24','Preto','12 meses',NULL,7,9,549.99,511.4907),(403,'25/05/2026   19:34:22','Monitor Gamer VA 34 \" Curva Antirreflexo LG','Monitor Gamer VA 34 \" Curva Antirreflexo LG WQHD UltraGear 34GP63A-B','uploads/Monitor Gamer VA 34 Curva Antirreflexo LG.jpg','Monitores','LG','Monitor Gamer VA 34 \" Curva Antirreflexo LG','Preto','12 meses',NULL,10,3,1590,1431),(404,'25/05/2026   19:37:19','Monitor Gamer Curvo Samsung Odyssey 34','Monitor Gamer Curvo Samsung Odyssey 34’’, Ultra WQHD, 165Hz, 1ms, tela ultrawide, HDMI, Display Port, Freesync Premium, preto, série G5','uploads/Monitor Gamer Curvo Samsung Odyssey.jpg','Monitores','Samsung','Monitor Gamer Curvo Samsung Odyssey 34','Preto','18 meses',NULL,15,10,1739,1478.15),(405,'25/05/2026   19:40:52','Monitor Gamer Curvo Samsung Odyssey 49','Monitor Gamer Curvo Samsung Odyssey 49\" DQHD, 240Hz, 1ms, HDMI, Display Port, USB, G-sync, Freesync Premium Pro','uploads/Monitor Gamer Curvo Samsung Odyssey 49.png','Monitores','Samsung','Monitor Gamer Curvo Samsung Odyssey 49','Preto','2 anos',NULL,7,13,1800,1674),(406,'25/05/2026   19:43:14','FIFINE Microfone dinâmico USB/XLR','FIFINE Microfone dinâmico USB/XLR para gravação de podcast,microfone streaming para jogos de computador com RGB,botão de mudo,conector para fones de ouvido,suporte de mesa，para cantar e YouTube- AM8','uploads/FIFINE Microfone dinâmico.jpg','Periféricos','Outra','FIFINE Microfone dinâmico USB/XLR','Preto','12 meses',NULL,5,5,350,332.5),(407,'25/05/2026   19:59:06','Teclado HyperX Alloy Origins Core','HyperX Alloy Origins Core - Mechanical Gaming Keyboard - HX Red (BR Layout)','uploads/HyperX Alloy Origins Core.jpg','Periféricos','Outra','HyperX Alloy Origins Core','Preto','3 meses',NULL,5,7,459,436.05),(408,'25/05/2026   19:48:21','Havit Headphone Fone de Ouvido','Havit Headphone Fone de Ouvido H2002d, Gamer, com Microfone, Falante 53mm, Plug 3, 5mm: compatível com XBOX ONE e PS4, HAVIT, HV-H2002d','uploads/Havit Headphone Fone de Ouvido.jpg','Periféricos','Outra','Havit Headphone Fone de Ouvido','Preto','3 meses',NULL,0,10,150,150),(409,'25/05/2026   19:50:06','Mouse Gamer Logitech','Mouse Gamer Logitech G502 HERO com RGB LIGHTSYNC, Ajustes de Peso, 11 Botões Programáveis e Sensor HERO 25K','uploads/Mouse Gamer Logitech.jpg','Periféricos','Logitech','Mouse Gamer Logitech','Preto','3 meses',NULL,31,4,299.9,206.93099999999998),(410,'25/05/2026   19:52:24','Gran Turismo 7 Edição Padrão','Gran Turismo 7 Edição Padrão - PlayStation 5','uploads/Gran Turismo 7 Edição Padrão - PlayStation 5.jpg','Games','Outra','Gran Turismo 7 Edição Padrão','Preto','3 meses',NULL,5,3,150,142.5),(411,'25/05/2026   19:54:51','God of War Ragnarök','God of War Ragnarök - Edição Standard - PlayStation 5','uploads/God of War Ragnarök.jpg','Games','Outra','God of War Ragnarök','Preto','3 meses',NULL,3,4,207,200.79),(412,'25/05/2026   19:56:18','GTA V ','GTA V - PlayStation 5','uploads/GTA V.jpg','Games','Outra','GTA V ','Preto','3 meses',NULL,3,3,130,126.1),(413,'25/05/2026   20:01:57','ETech Capa para iPhone 17','JETech Capa para iPhone 17 6,3 Polegadas, Case Protetora de Telefone à Prova de Choque, Anti-Amarelecimento, Parte Traseira Transparente Anti-Riscos','uploads/ETech Capa para iPhone 17.jpg','Acessórios','Apple','ETech Capa para iPhone 17','Preto','12 meses',NULL,0,4,49.99,49.99),(414,'25/05/2026   20:03:31','Ventosa para Capa de Celular','Ventosa para Capa de Celular','uploads/Ventosa para Capa de Celular.jpg','Acessórios','Outra','Ventosa para Capa de Celular','Preto','12 meses',NULL,1,7,9.99,9.8901),(415,'25/05/2026   20:05:20','Suporte Veicular Celular','Suporte Veicular Celular GPS Universal Smartphone Ventosa Gruda Painel Carro Trava Automática Anti Queda Waze Uber PREMIUM, ECOM SUPRIMENTOS','uploads/Suporte Veicular Celular GPS Universal.jpg','Acessórios','Outra','Suporte Veicular Celular','Preto','3 meses',NULL,0,15,29.9,29.9);
/*!40000 ALTER TABLE `produto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produto_seq`
--

DROP TABLE IF EXISTS `produto_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produto_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto_seq`
--

LOCK TABLES `produto_seq` WRITE;
/*!40000 ALTER TABLE `produto_seq` DISABLE KEYS */;
INSERT INTO `produto_seq` VALUES (501);
/*!40000 ALTER TABLE `produto_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_model_itens`
--

DROP TABLE IF EXISTS `usuario_model_itens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_model_itens` (
  `usuario_model_codigo` bigint NOT NULL,
  `codigo` bigint DEFAULT NULL,
  `descricao_produto` varchar(255) DEFAULT NULL,
  `imagem` varchar(255) DEFAULT NULL,
  `preco_final` double DEFAULT NULL,
  `preco_unitario` double DEFAULT NULL,
  `quantidade` int DEFAULT NULL,
  `cor` varchar(30) DEFAULT NULL,
  `marca` varchar(50) DEFAULT NULL,
  `modelo` varchar(80) DEFAULT NULL,
  KEY `fk_usuario_itens` (`usuario_model_codigo`),
  CONSTRAINT `fk_usuario_itens` FOREIGN KEY (`usuario_model_codigo`) REFERENCES `usuarios` (`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_model_itens`
--

LOCK TABLES `usuario_model_itens` WRITE;
/*!40000 ALTER TABLE `usuario_model_itens` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_model_itens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `admin` bit(1) NOT NULL DEFAULT b'0',
  `bairro` varchar(50) NOT NULL,
  `celular` varchar(15) NOT NULL,
  `cep` varchar(10) NOT NULL,
  `cidade` varchar(50) NOT NULL,
  `complemento` varchar(80) DEFAULT NULL,
  `cpf` varchar(20) NOT NULL,
  `data_nascimento` varchar(16) NOT NULL,
  `email` varchar(80) NOT NULL,
  `estado` varchar(3) NOT NULL,
  `nome` varchar(80) NOT NULL,
  `numero` int NOT NULL,
  `quantidade_itens` int NOT NULL DEFAULT '0',
  `role` varchar(20) NOT NULL DEFAULT 'ROLE_USER',
  `rua` varchar(100) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `valor_total_itens` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,_binary '','Recanto das Emas','61982511713','72620153','Brasília','casa branca e dourada','07002231189','13/03/2002','ianleonelt06@gmail.com','DF','Ian Leonel Teles de Sousa',726201,0,'ROLE_ADMIN','Quadra 300 Conjunto 52','$2a$10$qP6mtvZrXJ.xKOq0RWNUuutmDsEc/XfLJa5O7aUqrR5qJ8OnjldbS',0);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios_seq`
--

DROP TABLE IF EXISTS `usuarios_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios_seq`
--

LOCK TABLES `usuarios_seq` WRITE;
/*!40000 ALTER TABLE `usuarios_seq` DISABLE KEYS */;
INSERT INTO `usuarios_seq` VALUES (51);
/*!40000 ALTER TABLE `usuarios_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-31 12:29:34
