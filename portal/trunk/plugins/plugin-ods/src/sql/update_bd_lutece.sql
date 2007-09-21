delete from core_feature_group;

INSERT INTO `core_feature_group` VALUES 
    ('ODS_PROCHAINE_SEANCE','ods.prochaine_seance.description','ods.prochaine_seance.name',1),
    ('ODS_GESTION_AVAL','ods.gestion_aval.description','ods.gestion_aval.name',2),
    ('ODS_ADMINISTRATION','ods.administration.description','ods.administration.name',3),
    ('APPLICATIONS','portal.features.group.applications.description','portal.features.group.applications.label',4),
    ('STYLE','portal.features.group.charter.description','portal.features.group.charter.label',5),
    ('CONTENT','portal.features.group.content.description','portal.features.group.content.label',6),
    ('SITE','portal.features.group.site.description','portal.features.group.site.label',7),
    ('SYSTEM','portal.features.group.system.description','portal.features.group.system.label',8),
    ('USERS','portal.features.group.users.description','portal.features.group.users.label',9);

INSERT INTO `core_admin_right` (`id_right`, `name`, `level`, `admin_url`, `description`, `is_updatable`, `plugin_name`, `id_feature_group`, `icon_url`) VALUES 
('ODS_PROJET_DE_DELIB', 'ods.adminfeature.projetdelib.title', 2, 'jsp/admin/plugins/ods/projetdeliberation/Deliberation.jsp', 'ods.adminfeature.projetdelib.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/projetdeliberation/deliberation.png'),
('ODS_PROP_DE_DELIB', 'ods.adminfeature.propositiondelib.title', 2, 'jsp/admin/plugins/ods/propositiondedeliberation/Deliberation.jsp', 'ods.adminfeature.propositiondelib.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/projetdeliberation/deliberation.png'),
('ODS_ODJ', 'ods.adminfeature.odj.title', 2, 'jsp/admin/plugins/ods/ordredujour/Ordresdujour.jsp', 'ods.adminfeature.odj.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/ordredujour/ordredujour.png'),
('ODS_AMENDEMENTS', 'ods.adminfeature.amendement.title', 2, 'jsp/admin/plugins/ods/voeuamendement/Amendements.jsp', 'ods.adminfeature.amendement.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/voeuamendement/amendements.png'),
('ODS_VOEUXRATTACHES', 'ods.adminfeature.voeuattache.title', 2, 'jsp/admin/plugins/ods/voeuamendement/Voeuxrattaches.jsp', 'ods.adminfeature.voeuattache.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/voeuamendement/voeuxrattaches.png'),
('ODS_VA_NUMEROTES', 'ods.adminfeature.vanumerotes.title', 2, 'jsp/admin/plugins/ods/voeuamendement/VaNumerotes.jsp', 'ods.adminfeature.voeu.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/voeuamendement/vanumerotes.png'),
('ODS_VOEUX', 'ods.adminfeature.voeu.title', 2, 'jsp/admin/plugins/ods/voeuamendement/Voeuxnonrattaches.jsp', 'ods.adminfeature.voeu.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/voeuamendement/voeuxnonrattaches.png'),
('ODS_FASCICULES', 'ods.adminfeature.fascicule.title', 2, 'jsp/admin/plugins/ods/fascicule/Fascicules.jsp', 'ods.adminfeature.fascicule.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/fascicule/fascicules.png'),
('ODS_FICHIERS', 'ods.adminfeature.fichier.title', 2, 'jsp/admin/plugins/ods/fichiers/Fichiers.jsp', 'ods.adminfeature.fichier.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/fichiers/fichiers.png'),
('ODS_NATURE_DOSSIERS', 'ods.adminfeature.naturedossier.title', 2, 'jsp/admin/plugins/ods/naturedossier/Naturesdossiers.jsp', 'ods.adminfeature.naturedossier.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/naturedossier/naturesdossiers.png'),
('ODS_TOURNIQUET', 'ods.adminfeature.tourniquet.title', 2, 'jsp/admin/plugins/ods/tourniquet/Tourniquet.jsp', 'ods.adminfeature.tourniquet.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/tourniquet/tourniquet.png'),
('ODS_RELEVE_TRAVAUX', 'ods.adminfeature.releve.title', 2, 'jsp/admin/plugins/ods/relevetravaux/ReleveTravaux.jsp', 'ods.adminfeature.releve.description', 0, 'ods', 'ODS_PROCHAINE_SEANCE', 'images/admin/skin/plugins/ods/relevetravaux/releve.png'),
('ODS_PROJET_DE_DELIB_GESTION_AVAL', 'ods.adminfeature.projetdelib.gestion_aval.title', 2, 'jsp/admin/plugins/ods/projetdeliberation/ProjetsdeliberationGestionAval.jsp', 'ods.adminfeature.projetdelib.gestion_aval.description', 0, 'ods', 'ODS_GESTION_AVAL', 'images/admin/skin/plugins/ods/projetdelib_gestion_aval.png'),
('ODS_PROP_DE_DELIB_GESTION_AVAL', 'ods.adminfeature.propositiondelib.gestion_aval.title', 2, 'jsp/admin/plugins/ods/propositiondedeliberation/PropositiondeliberationGestionAval.jsp', 'ods.adminfeature.propositiondelib.gestion_aval.description', 0, 'ods', 'ODS_GESTION_AVAL', 'images/admin/skin/plugins/ods/propositiondeliberation/propositiondelib_gestion_aval.png'),
('ODS_AMENDEMENTS_GESTION_AVAL', 'ods.adminfeature.amendement.gestion_aval.title', 2, 'jsp/admin/plugins/ods/voeuamendement/AmendementsGestionAval.jsp', 'ods.adminfeature.amendement.gestion_aval.description', 0, 'ods', 'ODS_GESTION_AVAL', 'images/admin/skin/plugins/ods/voeuamendement/amendement_gestion_aval.png'),
('ODS_VOEUXRATTACHES_GESTION_AVAL', 'ods.adminfeature.voeuattache.gestion_aval.title', 2, 'jsp/admin/plugins/ods/voeuamendement/VoeuxrattachesGestionAval.jsp', 'ods.adminfeature.voeuattache.gestion_aval.description', 0, 'ods', 'ODS_GESTION_AVAL', 'images/admin/skin/plugins/ods/voeuamendement/voeuattache_gestion_aval.png'),
('ODS_VOEUX_GESTION_AVAL', 'ods.adminfeature.voeu.gestion_aval.title', 2, 'jsp/admin/plugins/ods/voeuamendement/VoeuxnonrattachesGestionAval.jsp', 'ods.adminfeature.voeu.gestion_aval.description', 0, 'ods', 'ODS_GESTION_AVAL', 'images/admin/skin/plugins/ods/voeuamendement/voeu_gestion_aval.png'),
('ODS_FICHIERS_GESTION_AVAL', 'ods.adminfeature.fichier.gestion_aval.title', 2, 'jsp/admin/plugins/ods/fichiers/FichiersGestionAval.jsp', 'ods.adminfeature.fichier.gestion_aval.description', 0, 'ods', 'ODS_GESTION_AVAL', 'images/admin/skin/plugins/ods/fichiers/fichier_gestion_aval.png'),
('ODS_SEANCES', 'ods.adminfeature.seance.title', 2, 'jsp/admin/plugins/ods/seance/Seances.jsp', 'ods.adminfeature.seance.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/seance/seances.png'),
('ODS_GROUPES_POLITIQUES', 'ods.adminfeature.groupespolitiques.title', 2, 'jsp/admin/plugins/ods/groupepolitique/Groupespolitiques.jsp', 'ods.adminfeature.groupespolitiques.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/groupepolitique/groupespolitiques.png'),
('ODS_ELUS', 'ods.adminfeature.elus.title', 2, 'jsp/admin/plugins/ods/elu/Elus.jsp', 'ods.adminfeature.elus.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/elu/elus.png'),
('ODS_COMMISSIONS', 'ods.adminfeature.commissions.title', 2, 'jsp/admin/plugins/ods/commission/Commissions.jsp', 'ods.adminfeature.commissions.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/commission/commissions.png'),
('ODS_DIRECTIONS', 'ods.adminfeature.directions.title', 2, 'jsp/admin/plugins/ods/direction/Directions.jsp', 'ods.adminfeature.directions.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/direction/directions.png'),
('ODS_CATEGORIES', 'ods.adminfeature.categories.title', 2, 'jsp/admin/plugins/ods/categoriedeliberation/Categories.jsp', 'ods.adminfeature.categories.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/categoriedeliberation/categories.png'),
('ODS_MODELES_ODJ', 'ods.adminfeature.modeleodj.title', 2, 'jsp/admin/plugins/ods/modeleordredujour/Modeles.jsp', 'ods.adminfeature.modeleodj.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/modeleordredujour/modeles.png'),
('ODS_EXPRESSIONS', 'ods.adminfeature.expressionusuelle.title', 2, 'jsp/admin/plugins/ods/expression/Expressions.jsp', 'ods.adminfeature.expressionusuelle.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/expressionusuelle/expressions.png'),
('ODS_HORODATAGE', 'ods.adminfeature.horodatage.title', 2, 'jsp/admin/plugins/ods/horodatage/Horodatage.jsp', 'ods.adminfeature.horodatage.description', 0, 'ods', 'ODS_ADMINISTRATION', 'images/admin/skin/plugins/ods/horodatage/horodatage.png'),
('ODS_STATUTVA_GESTION_AVAL', 'ods.adminfeature.statutva.gestion_aval.title', 2, 'jsp/admin/plugins/ods/voeuamendement/StatutVAGestionAval.jsp', 'ods.adminfeature.statutva.gestion_aval.description', 0, 'ods', 'ODS_GESTION_AVAL', 'images/admin/skin/plugins/ods/statut/statutva_gestion_aval.png'),
('ODS_INDEXER', 'ods.adminfeature.indexer.title', 2, 'jsp/admin/plugins/ods/indexer/Indexer.jsp', 'ods.adminfeature.indexer.description', 0, 'ods', 'SYSTEM', 'images/admin/skin/plugins/ods/indexer/indexer.png'),
('ODS_CERTIFICAT_AFFICHAGE', 'ods.adminfeature.certificatAffichage.title', 2, 'jsp/admin/plugins/ods/certificatAffichage/CertificatAffichage.jsp', 'ods.adminfeature.certificatAffichage.description', 0, 'ods', 'ODS_GESTION_AVAL', 'images/admin/skin/plugins/ods/certificatAffichage/certificatAffichage.png'), 
('WSSODATABASE_MANAGEMENT_ROLES', 'module.mylutece.wssodatabase.adminFeature.wssodatabase_management_role.name', 3, 'jsp/admin/plugins/mylutece/modules/wssodatabase/ManageRoles.jsp', 'module.mylutece.wssodatabase.adminFeature.wssodatabase_management_role.description', 0, 'mylutece-wssodatabase', NULL, NULL),
('WSSODATABASE_MANAGEMENT_USERS', 'module.mylutece.wssodatabase.adminFeature.wssodatabase_management_user.name', 3, 'jsp/admin/plugins/mylutece/modules/wssodatabase/ManageUsers.jsp', 'module.mylutece.wssodatabase.adminFeature.wssodatabase_management_user.description', 0, 'mylutece-wssodatabase', NULL, NULL);
    
INSERT INTO `core_user_right` (`id_right`, `id_user`) VALUES 
('ODS_AMENDEMENTS', 1),
('ODS_AMENDEMENTS_GESTION_AVAL', 1),
('ODS_CATEGORIES', 1),
('ODS_CERTIFICAT_AFFICHAGE', 1),
('ODS_COMMISSIONS', 1),
('ODS_DIRECTIONS', 1),
('ODS_ELUS', 1),
('ODS_EXPRESSIONS', 1),
('ODS_FASCICULES', 1),
('ODS_FICHIERS', 1),
('ODS_FICHIERS_GESTION_AVAL', 1),
('ODS_GROUPES_POLITIQUES', 1),
('ODS_INDEXER', 1),
('ODS_MODELES_ODJ', 1),
('ODS_NATURE_DOSSIERS', 1),
('ODS_ODJ', 1),
('ODS_PROJET_DE_DELIB', 1),
('ODS_PROJET_DE_DELIB_GESTION_AVAL', 1),
('ODS_PROP_DE_DELIB', 1),
('ODS_PROP_DE_DELIB_GESTION_AVAL', 1),
('ODS_RAPPORTEURS', 1),
('ODS_RELEVE_TRAVAUX', 1),
('ODS_SEANCES', 1),
('ODS_TOURNIQUET', 1),
('ODS_VA_NUMEROTES', 1),
('ODS_VOEUX', 1),
('ODS_VOEUX_GESTION_AVAL', 1),
('ODS_VOEUXRATTACHES', 1),
('ODS_VOEUXRATTACHES_GESTION_AVAL', 1),
('ODS_STATUTVA_GESTION_AVAL', 1),
('WSSODATABASE_MANAGEMENT_ROLES', 1),
('WSSODATABASE_MANAGEMENT_USERS', 1);
      
INSERT INTO `core_admin_role` (`role_key`, `role_description`) VALUES
('charge_mise_en_ligne', 'Chargé de mise en ligne'),
('admin_fonctionnel', 'Administrateur fonctionnel'),
('admin_technique', 'Administrateur technique');

INSERT INTO `core_admin_role_resource` 
(`rbac_id`, `role_key`, `resource_type`, `resource_id`, `permission`) VALUES
(201, 'admin_technique', 'PROJET_PROCHAINE_SEANCE', '*', '*'),
(202, 'admin_technique', 'MODELES_ODJ', '*', '*'),
(203, 'admin_technique', 'ADMINISTRATION_SAUF_ODJ', '*', '*'),
(204, 'admin_technique', 'PDD_GESTION_AVAL', '*', '*'),
(205, 'admin_technique', 'RELEVES_DES_TRAVAUX', '*', '*'),
(206, 'admin_technique', 'PROPOSITION_PROCHAINE_SEANCE', '*', '*'),
(207, 'admin_technique', 'CONSTITUTION_ODJ', '*', '*'),
(208, 'admin_technique', 'FICHIERS', '*', '*'),
(209, 'admin_technique', 'VOEUX_AMENDEMENTS', '*', '*'),
(210, 'admin_fonctionnel', 'PROJET_PROCHAINE_SEANCE', '*', '*'),
(211, 'admin_fonctionnel', 'ADMINISTRATION_SAUF_ODJ', '*', '*'),
(212, 'admin_fonctionnel', 'PDD_GESTION_AVAL', '*', '*'),
(213, 'admin_fonctionnel', 'RELEVES_DES_TRAVAUX', '*', '*'),
(214, 'admin_fonctionnel', 'PROPOSITION_PROCHAINE_SEANCE', '*', '*'),
(215, 'admin_fonctionnel', 'CONSTITUTION_ODJ', '*', '*'),
(216, 'admin_fonctionnel', 'FICHIERS', '*', '*'),
(217, 'admin_fonctionnel', 'VOEUX_AMENDEMENTS', '*', '*');  

INSERT INTO `core_user_role` (`role_key`, `id_user`) VALUES ('admin_technique', 1);    

UPDATE `core_admin_user` set access_code = '1EDB9B49FE3011DB86E84182996D80ED00000000' where id_user= 1;

DELETE from core_user_right where id_right = 'CORE_LOGS_VISUALISATION';
