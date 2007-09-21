INSERT INTO `ods_type_document` (`id_type_document`, `libelle_type`, `est_creation_prochaine_seance`, `est_creation_aval`) VALUES 
    (1,'Exposé des motifs',NULL,NULL),
    (2,'Projet de délibéré',NULL,NULL),
    (3,'Piece annexe',NULL,NULL),
    (4,'Ordre du jour',NULL,NULL),
    (5,'Convocation du Maire',1,1),
    (6,'Liste intégrale des questions',1,1),
    (7,'Relevé de la Conférence d\'organisation',1,1),
    (8,'Résumé des questions',1,1),
    (9,'Document libre',1,1),
    (10,'Compte rendu',NULL,1),
    (11,'Désignation',1,1),
    (12,'Délibération',NULL,NULL),
    (13,'Délibération de désignation',NULL,1),
    (14,'Voeu',NULL,NULL),
    (15,'Amendement',NULL,NULL),
    (16,'Relevé',NULL,NULL),
    (17,'Liasse des VA',NULL,NULL),
    (18,'Zip',NULL,NULL);

INSERT INTO `ods_formation_conseil` (`id_formation_conseil`, `libelle_formation_conseil`) VALUES 
(0, 'Conseil municipal'),
(1, 'Conseil général');


INSERT INTO `ods_statut` (`id_statut`, `libelle_statut`,`est_pour_pdd`,`est_pour_voeu`,`est_pour_amendement`) VALUES 
(1, 'Adopté',1,1,1),
(2, 'Amendé',1,1,1),
(3, 'Non présenté',1,1,1),
(4, 'Pas de vote',1,1,1),
(5, 'Rejeté',1,1,1),
(6, 'Retiré',1,1,1),
(7, 'Voeu transformé en amendement adopté',0,1,0),
(8, 'Amendement transformé en voeu adopté',0,0,1);

INSERT INTO `ods_type_ordre_jour` (`id_type_odj`,`libelle_type_odj`,`libelle_long_type_odj`) VALUES
(0,'Prévisionnel','Ordre du jour prévisionnel du Conseil de Paris'),
(1,'Mis à jour','Mise à jour de l\'ordre du jour du Conseil de Paris'),
(2,'Définitif','Ordre du jour définitif du Conseil de Paris'),
(3,'Commission',NULL),
(4,'Additif',NULL),
(5,'Rectificatif',NULL);

INSERT INTO ods_type_de_critere (id_type_critere,nom_critere) VALUES 	(1,'premiere_date'),
	(2,'deuxieme_date'),
	(3,'champ_recherche'),
	(4,'types_document'),
	(5,'formations_conseil'),
	(6,'commissions'),
	(7,'rapporteurs'),
	(8,'arrondissements'),
	(9,'directions'),
	(10,'categories_deliberation'),
	(11,'groupes_depositaires'),
	(12,'elus_depositaires');


